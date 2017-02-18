package es.uca.pfc.engine;

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

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;

import es.uca.pfc.commons.Reference;
import es.uca.pfc.commons.SearchTermParam;
import es.uca.pfc.enums.OperatorSearch;
import es.uca.pfc.enums.TypeEngineSearch;

/**
 * EngineSearchSCIENCE es una clase que hereda de EngineSearch para representar el motor de busqueda
 * para las referencias de la pagina Science Direct.
 * 
 * @author agonzatoro
 *
 */
public class EngineSearchSCIENCE extends EngineSearch {

	/** contMax.*/
	public static int contMax	= 0;
	/** contHilos.*/
	public static int contHilos = 0;
	/** references.*/
	public static List<Reference> references = new ArrayList<Reference>();
	
	/**
	 * Constructor de la clase EngineSearchSCIENCE.
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
	public EngineSearchSCIENCE(String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year,  int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			List<WebClient> webClients, int total_hilos, int total_tries) throws Exception {
		
		super(TypeEngineSearch.SCIENCE, clientId, clientSecret, redirectUri, mendeleyService, nameSLR, tammax, tags, 
				start_year, end_year, searchsTerms, apiKeysEngine, webClients, total_hilos, total_tries);
		
		this.web = "http://api.elsevier.com/content/search/scidir";
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
		String q = createQueryScience(searchsTerms);
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
	private String createQueryScience(List<SearchTermParam> searchsTerms)
	{
		String query = web;
		
		if(searchsTerms.size() > 0)
		{
			query += "?query=";
			
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
			query += "pub-date AFT " + start_year + "0101 AND pub-date BEF " + end_year + "1231 AND ";
			
			// Construimos la query
			int index = 0;
			String subquery = "";
			
			// Parameters AND
			for(SearchTermParam stp : andParameters) 
			{
				subquery += convertParametersScienceDirect(stp);
				index++;
				if (index != andParameters.size())
				{
					subquery += " AND ";
				}
				
				query += subquery;
			}
			
			// Parameters OR
			subquery = "";
			index = 1;
			if (orParameters.size() > 0)
			{
				for(SearchTermParam stp : orParameters)
				{
					subquery += convertParametersScienceDirect(stp);
					index++;
					if(index != orParameters.size())
					{
						subquery = " OR " + subquery;
					}
					
					query += subquery;
				}
			}
			
			// Parameters NOT
			subquery = "";
			index = 1;
			if (noneParameters.size() > 0)
			{
				for (SearchTermParam stp : noneParameters)
				{
					subquery += convertParametersScienceDirect(stp);
					index++;
					if (index != noneParameters.size())
					{
						subquery = " AND NOT " + subquery;
					}
					
					query += subquery;
				}
			}
			
			// Insertamos la api-key y el tipo de formato a obtener (xml)
			String apiKeyScience = (String) apiKeysEngine.get(TypeEngineSearch.SCIENCE);
			query += "&httpAccept=application/xml&apiKey=" + apiKeyScience;
			
			// Indicamos numero de resultados a obtener
			query += "&count="+TAM_DEF;
			
			// Indicamos posicion por donde se empieza (index)
			query += "&start=0";   // comienza desde 0
		}
		
		query = query.trim();
		
		return query;
	}
	
	/**
	 * M�todo que realiza la conversi�n de los par�metros a insertar en la url de b�squeda.
	 * 
	 * @param stp SearchTermParam
	 * @return String
	 */
	private String convertParametersScienceDirect(SearchTermParam stp)
	{
		String strParam = "";
		
		String terms = stp.getTerminos().trim();
		
		switch(stp.getComponentSearch())
		{
			case FULLTEXT:
			case REVIEW:
			case ANYFIELD:
				strParam = "All(" + terms + ")";
				break;
			case ABSTRACT:
				strParam = "abs(" + terms + ")";
				break;
			case TITLE:
			case PUBLISHER:
				strParam = "tak(" + terms + ")";
				break;
			case AUTHOR:
				strParam = "aut(" + terms + ")";
				break;
			case ISBN:
				strParam = "ISBN(" + terms + ")";
				break;
			case ISSN:
				strParam = "ISSN(" + terms + ")";
				break;
			case DOI:
				strParam = "DOI(" + terms + ")";
				break;
			case KEYWORDS:
				strParam = "key(" + terms + ")";
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
			if(folder.getName().equals("science"))
			{
				idSubFolder = folder.getId();
				break;
			}
		}
		
		if(idSubFolder.equals("")) // Creamos una carpeta
		{
			idSubFolder = folderservice.createSubFolder("SCIENCE", folderservice.getFolderByName(nameSLR).getId()).getId();
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
		int start = 1;
		if (web.contains("&start="))
		{
			start = Integer.parseInt(web.substring(web.indexOf("&start=")).trim().replaceAll("&start=", ""));
			web = web.replaceAll(web.substring(web.indexOf("&start=")), "");				
		}
		web = web + "&start=" + (start + TAM_DEF);
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
			
			NodeList nList = doc.getElementsByTagName("entry");
			
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
