package background.pfc.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

import org.apache.commons.httpclient.util.URIUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import background.pfc.commons.Reference;
import background.pfc.commons.SearchTermParam;
import background.pfc.enums.OperatorSearch;
import background.pfc.enums.TypeEngineSearch;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;

import background.pfc.engine.EngineSearch;

/**
 * EngineSearchSPRINGER es una clase que hereda de EngineSearch para representar el motor de busqueda
 * para las referencias de la pagina Springer Link.
 * 
 * @author agonzatoro
 *
 */
public class EngineSearchSPRINGER extends EngineSearch {

	/** contMax.*/
	public static int contMax	= 0;
	/** contHilos.*/
	public static int contHilos = 0;
	/** references.*/
	public static List<Reference> references = new ArrayList<Reference>();
	
	/**
	 * Constructor de la clase EngineSearchSPRINGER.
	 * 
	 * @param clientId String
	 * @param clientSecret String
	 * @param redirectUri String
	 * @param mendeleyService MendeleyService
	 * @param nameSLR String
	 * @param tammax int
	 * @param tags List<String>
	 * @param start_year int
	 * @param end_year int
	 * @param searchsTerms List<SearchTermParam>
	 * @param apiKeysEngine Map<TypeEngineSearch,String>
	 * @param webClients List<WebClient>
	 * @param total_hilos int
	 * @param total_tries int
	 * @throws Exception Exception
	 */
	public EngineSearchSPRINGER(String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year,  int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			List<WebClient> webClients, int total_hilos, int total_tries) throws Exception {
		
		super(TypeEngineSearch.SPRINGER, clientId, clientSecret, redirectUri, mendeleyService, nameSLR, tammax, tags, 
				start_year, end_year, searchsTerms, apiKeysEngine, webClients, total_hilos, total_tries);
		
		this.web = "http://api.springer.com/meta/v1/pam";
		this.idEngine = getIdSubFolder();
		this.TAM_DEF = 100;
	}
	
	/**
	 * M�todo que obtiene las url/doi de las referencias.
	 * 
	 */
	@Override
	public void getLinksReferences() throws ElementNotFoundException, IOException
	{
		double tamDefDouble = Double.parseDouble(Integer.toString(TAM_DEF));
		int num_paginas = (int) Math.ceil(TAM_MAX/tamDefDouble);
				
		// Insertamos los parámetros necesarios en la web
		String q = createQuerySpringer(searchsTerms);
		web = URIUtil.encodeQuery(q);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		List<String> linksBib = new ArrayList<String>();		
		
		for(int i = 1; i <= num_paginas; i++)
		{
			System.out.println("Conectando a " + web);
			linksBib.addAll(getLinksBib());
			nextPage(i);
		}
		
		linksDocuments.addAll(linksBib);
		System.out.println("Se ha obtenido " + linksDocuments.size());
	}
	
	/**
	 * M�todo que construye la query para obtener los enlaces/dois de las referencias a importar.
	 * 
	 * @param searchsTerms List<SearchTermParam>
	 * @return String
	 */
	private String createQuerySpringer(List<SearchTermParam> searchsTerms)
	{
		String query = web;
		
		// Insertamos la api-key y el tipo de formato a obtener (xml)
		String apiKeySpringer = (String) apiKeysEngine.get(TypeEngineSearch.SPRINGER);
		query += "?api_key=" + apiKeySpringer;
		
		if(searchsTerms.size() > 0)
		{
			query += "&q=((";
			
			List<SearchTermParam> noneParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> andParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> orParameters = new ArrayList<SearchTermParam>();
			
			for(SearchTermParam stp : searchsTerms)
			{
				if (OperatorSearch.ALL == stp.getOperatorSearch()) {
					andParameters.add(stp);
				} else if (OperatorSearch.ANY == stp.getOperatorSearch()) {
					orParameters.add(stp);
				} else {
					noneParameters.add(stp);
				}
			}
			
			// Introducimos los years de comienzo y fin
			for(int y = start_year; y <= end_year; y++)
			{
				query += "year:" + y;
				if (y != end_year)
				{
					query += " OR ";
				}
			}
			query += ")";
			
			// Construimos la query
			String subquery = "";
			
			// Parameters AND
			for(SearchTermParam stp : andParameters) 
			{
				subquery += convertParametersSpringerLink(stp);
				
				query += " AND " + subquery;
			}
			
			// Parameters OR
			subquery = "";
			if (orParameters.size() > 0)
			{
				for(SearchTermParam stp : orParameters)
				{
					subquery += convertParametersSpringerLink(stp);
					
					query += " OR " + subquery;
				}
			}
			
			// Parameters NOT
			subquery = "";
			if (noneParameters.size() > 0)
			{
				for (SearchTermParam stp : noneParameters)
				{
					subquery += convertParametersSpringerLink(stp);
					query += "-(" + subquery + ")";
				}
			}
			
			query += ")"; // fin param "q"
			
			// Indicamos numero de resultados a obtener
			query += "&p="+TAM_DEF; //maximo es 100
			
			// Indicamos posicion por donde se empieza (index)
			query += "&s=1";   // comienza desde 1
		}
		
		return query;
	}
	
	/**
	 * M�todo que realiza la conversi�n de los par�metros a insertar en la url de b�squeda.
	 * 
	 * @param stp SearchTermParam
	 * @return String
	 */
	private static String convertParametersSpringerLink(SearchTermParam stp)
	{
		String strParam = "";
		
		String terms = stp.getTerminos().trim();
		
		switch(stp.getComponentSearch())
		{
			case FULLTEXT:
			case REVIEW:
			case ANYFIELD:
			case ABSTRACT:
				strParam = "\"" + terms + "\"";
				break;
			case TITLE:
				strParam = "title:\"" + terms + "\"";
				break;
			case PUBLISHER:
				strParam = "pub:\"" + terms + "\"";
				break;
			case AUTHOR:
				strParam = "name:\"" + terms + "\"";
				break;
			case ISBN:
				strParam = "isbn:\"" + terms + "\"";
				break;
			case ISSN:
				strParam = "issn:\"" + terms + "\"";
				break;
			case DOI:
				strParam = "doi:\"" +  terms + "\"";
				break;
			case KEYWORDS:
				strParam = "keyword:\"" + terms + "\"";
				break;
		}
		
		return strParam;
	}
	
	/**
	 * M�todo privado que obtiene el id de la carpeta del engine procedente de Mendeley.
	 * 
	 * @return String
	 * @throws Exception Exception
	 */
	private String getIdSubFolder() throws Exception
	{
		FolderService folderservice = new FolderService(mendeleyService);
		
		List<Folder> folders = folderservice.getSubFolders(folderservice.getFolderByName(nameSLR));
		
		String idSubFolder = "";
		
		for(Folder folder : folders)
		{
			if(folder.getName().equals(TypeEngineSearch.SPRINGER.getKey()))
			{
				idSubFolder = folder.getId();
				break;
			}
		}
		
		if(idSubFolder.equals("")) // Creamos una carpeta
		{
			idSubFolder = folderservice.createSubFolder(TypeEngineSearch.SPRINGER.getKey(), folderservice.getFolderByName(nameSLR).getId()).getId();
		}
		
		return idSubFolder;
	}
	
	/**
	 * M�todo que obtiene la p�gina siguiente con m�s referencias a importar.
	 * 
	 * @param page int
	 */
	private void nextPage(int page)
	{
		int s = 1;
		if (web.contains("&s="))
		{
			s = Integer.parseInt(web.substring(web.indexOf("&s=")).trim().replaceAll("&s=", ""));
			web = web.replaceAll(web.substring(web.indexOf("&s=")), "");				
		}
		web = web + "&s=" + (s + TAM_DEF);
	}
	
	/**
	 * M�todo que extrae las url/doi de las referencias a importar.
	 * 
	 * @return List<String>
	 */
	private List<String> getLinksBib()
	{
		List<String> bibs = new ArrayList<String>();
		
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(web);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("pam:message");
			
			if (nList.getLength() > 0)
			{
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						bibs.add(eElement.getElementsByTagName("prism:doi").item(0).getTextContent());
					}
				}
			}
		}
		catch(Exception ex)	{ }
		
		return bibs;
	}
}
