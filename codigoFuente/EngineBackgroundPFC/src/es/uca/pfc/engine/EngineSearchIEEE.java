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

public class EngineSearchIEEE extends EngineSearch {

	public static int contMax	= 0;
	public static int contHilos = 0;
	public static List<Reference> references = new ArrayList<Reference>();

	public EngineSearchIEEE(String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year, int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			List<WebClient> webClients, int total_hilos, int total_tries) throws Exception {
		
		super(TypeEngineSearch.IEEE, clientId, clientSecret, redirectUri, mendeleyService, nameSLR, tammax, tags, 
				start_year, end_year, searchsTerms, apiKeysEngine, webClients, total_hilos, total_tries);
		
		this.web = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp";
		this.idEngine = getIdSubFolder();
		this.TAM_DEF = 100;
	}
	
	@Override
	public void getLinksReferences() throws ElementNotFoundException, IOException
	{
		double tamDefDouble = Double.parseDouble(Integer.toString(TAM_DEF));
		int num_paginas = (int) Math.ceil(TAM_MAX/tamDefDouble);
				
		// Insertamos los parámetros necesarios en la web
		String q = QueryIEEE(searchsTerms);
		web = URIUtil.encodeQuery(q);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		ArrayList<String> linksBib = new ArrayList<String>();		
		System.out.println("num_paginas => " + num_paginas);
		for(int i = 1; i <= num_paginas; i++)
		{
			System.out.println("Conectando a " + web);
			linksBib.addAll(getLinksBib(web));
			nextPage(i);
		}
		
		linksDocuments.addAll(linksBib);
		System.out.println("Se ha obtenido " + linksDocuments.size());
	}
	
	private String QueryIEEE(List<SearchTermParam> searchsTerms)
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
	
	private void nextPage(int page)
	{
		System.out.println(web);
		int rs = 1;
		if (web.contains("&rs="))
		{
			rs = Integer.parseInt(web.substring(web.indexOf("&rs=")).trim().replaceAll("&rs=", ""));
			web = web.replaceAll(web.substring(web.indexOf("&rs=")), "");				
		}
		//web = web + "&rs=" + ((page * TAM_DEF)-1);
		web = web + "&rs=" + (rs + TAM_DEF);
	}
	
	private ArrayList<String> getLinksBib(String code)
	{
		ArrayList<String> bibs = new ArrayList<String>();
		
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

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						//bibs.add(eElement.getElementsByTagName("mdurl").item(0).getTextContent());
						bibs.add(eElement.getElementsByTagName("doi").item(0).getTextContent());
					}
				}
			}
		}
		catch(Exception ex)	{ }
		
		return bibs;
	}
}
