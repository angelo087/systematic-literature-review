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
import background.pfc.commons.ReferenceToImport;
import background.pfc.commons.SearchTermParam;
import background.pfc.enums.OperatorSearch;
import background.pfc.enums.TypeEngineSearch;
import background.pfc.enums.TypeReferenceId;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import background.pfc.engine.EngineSearch;

/**
 * EngineSearchIEEE es una clase que hereda de EngineSearch para representar el motor de busqueda
 * para las referencias de la pagina IEEE.
 * 
 * @author agonzatoro
 *
 */
public class EngineSearchIEEE extends EngineSearch {

	/**
	 * Constructor de la clase EngineSearchIEEE.
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
	public EngineSearchIEEE(String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year, int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			int total_hilos, int total_tries) throws Exception {
		
		super(TypeEngineSearch.IEEE, clientId, clientSecret, redirectUri, mendeleyService, nameSLR, tammax, tags, 
				start_year, end_year, searchsTerms, apiKeysEngine, total_hilos, total_tries);
		
		this.web = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp";
		this.idEngine = getIdSubFolder();
		this.TAM_DEF = 100;
	}
	
	/**
	 * M�todo que obtiene las url/doi de las referencias.
	 * 
	 */
	@Override
	public void getIdsReferences() throws ElementNotFoundException, IOException
	{
		double tamDefDouble = Double.parseDouble(Integer.toString(TAM_DEF));
		int num_paginas = (int) Math.ceil(TAM_MAX/tamDefDouble);
				
		// Insertamos los parámetros necesarios en la web
		String q = createQueryIEEE(searchsTerms);
		web = URIUtil.encodeQuery(q);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		List<ReferenceToImport> linksBib = new ArrayList<ReferenceToImport>();		
		System.out.println("num_paginas => " + num_paginas);
		for(int i = 1; i <= num_paginas; i++)
		{
			System.out.println("Conectando a " + web);
			linksBib.addAll(getDoiReferences());
			nextPage(i);
		}
		
		idsDocuments.addAll(linksBib);
		System.out.println("Se ha obtenido " + idsDocuments.size());
	}
	
	/**
	 * M�todo que construye la query para obtener los enlaces/dois de las referencias a importar.
	 * 
	 * @param searchsTerms List<SearchTermParam>
	 * @return String
	 */
	private String createQueryIEEE(List<SearchTermParam> searchsTerms)
	{
		String query = web;
		if(searchsTerms.size() > 0)
		{
			query += "?";
			List<SearchTermParam> othersParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> noneParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> andParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> orParameters = new ArrayList<SearchTermParam>();
			
			for(SearchTermParam stp : searchsTerms)
			{
				if(stp.getOperatorSearch() == OperatorSearch.NONE)
				{
					othersParameters.add(stp);
				}
				else
				{
					String subquery = "";
					switch(stp.getComponentSearch())
					{
						case ABSTRACT:
							subquery = "ab=\"" + stp.getTerminos().trim() + "\"";
							break;
						case TITLE:
							subquery = "ti=\"" + stp.getTerminos().trim() + "\"";
							break;
						case AUTHOR:
							subquery = "au=\"" + stp.getTerminos().trim() + "\"";
							break;
						case PUBLISHER:
							subquery = "jn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case ISBN:
							subquery = "isbn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case ISSN:
							subquery = "issn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case DOI:
							subquery = "doi=\"" + stp.getTerminos().trim() + "\"";
							break;
						default:
							othersParameters.add(stp);
					}
					
					if(!subquery.equals(""))
					{
						query += subquery + "&";
					}
					
					query = query.trim();
				}
			}
			
			// Restriccion para el rango de years
			if (query.charAt(query.length()-1) != '&')
			{
				query += "&";
			}
			query += "pys=" + start_year;
			query += "&pye=" + end_year;
			
			// Restriccion total a mostrar
			query += "&hc=" + TAM_DEF;
			
			// QueryText
			if (othersParameters.size() > 0)
			{
				if (query.charAt(query.length()-1) != '&')
				{
					query += "&";
				}
				
				query += "querytext=(";
				
				for(SearchTermParam param : othersParameters) {
					if (param.getOperatorSearch() == OperatorSearch.ALL) 
					{
						andParameters.add(param);
					} 
					else if (param.getOperatorSearch() == OperatorSearch.ANY) 
					{
						orParameters.add(param);
					}
					else if (param.getOperatorSearch() == OperatorSearch.NONE)
					{
						noneParameters.add(param);
					}
				}
				
				String firstTerm = "";
				if(andParameters.size() > 0)
				{
					firstTerm = andParameters.get(0).getTerminos();
					andParameters.remove(0);
				}
				else
				{
					if (orParameters.size() > 0)
					{
						firstTerm = orParameters.get(0).getTerminos();
						orParameters.remove(0);
					}
					else
					{
						firstTerm = "article";
					}
				}
				
				query += firstTerm;
				
				// Resto de parametros AND
				if (andParameters.size() > 0)
				{
					for (SearchTermParam param : andParameters) {
						query += " AND " + param.getTerminos();
					}
				}
				
				// Resto de parametros OR
				if (orParameters.size() > 0)
				{
					for (SearchTermParam param : orParameters) {
						query += " OR " + param.getTerminos();
					}
				}
				
				// Resto de parametros NOT
				if (noneParameters.size() > 0)
				{
					for (SearchTermParam param : noneParameters) {
						query += " NOT " + param.getTerminos();
					}
				}
				
				query += ")";
			}
			else if (query.charAt(query.length()-1) == '&') 
			{
				query = query.substring(0, query.length()-1);
			}
			query = query.trim() + "&rs=1";
		}
		
		return query;
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
			if(folder.getName().equals("ieee"))
			{
				idSubFolder = folder.getId();
				break;
			}
		}
		
		if(idSubFolder.equals("")) // Creamos una carpeta
		{
			idSubFolder = folderservice.createSubFolder("IEEE", folderservice.getFolderByName(nameSLR).getId()).getId();
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
		int rs = 1;
		if (web.contains("&rs="))
		{
			rs = Integer.parseInt(web.substring(web.indexOf("&rs=")).trim().replaceAll("&rs=", ""));
			web = web.replaceAll(web.substring(web.indexOf("&rs=")), "");				
		}
		//web = web + "&rs=" + ((page * TAM_DEF)-1);
		web = web + "&rs=" + (rs + TAM_DEF);
	}
	
	/**
	 * M�todo que extrae las url/doi de las referencias a importar.
	 * 
	 * @return List<String>
	 */
	private List<ReferenceToImport> getDoiReferences()
	{
		List<ReferenceToImport> bibs = new ArrayList<ReferenceToImport>();
		
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(web);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("document");
			
			if (nList.getLength() > 0)
			{
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
					try
					{
						Node nNode = nList.item(temp);
	
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
							Element eElement = (Element) nNode;
							//bibs.add(eElement.getElementsByTagName("doi").item(0).getTextContent());
							String doi = eElement.getElementsByTagName("doi").item(0).getTextContent();
							bibs.add(new ReferenceToImport(doi, TypeReferenceId.DOI, doi));
						}
					}
					catch(Exception ex) { }
				}
			}
		}
		catch(Exception ex)	{ }
		
		return bibs;
	}
}
