package es.pfc.engine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.util.URIUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;
import es.pfc.commons.Reference;
import es.pfc.commons.SearchTermParam;
import es.pfc.commons.TypeEngineSearch;

public class EngineSearchACM extends EngineSearch {

	public static int contMax	= 0;
	public static int contHilos = 0;
	public static List<Reference> references = new ArrayList<Reference>();
	
	public EngineSearchACM(String clientId, String clientSecret, String redirectUri, String emailMend, String passMend,
			String nameSLR, int tammax, List<String> tags, int start_year,
			int end_year, List<SearchTermParam> searchsTerms) throws Exception {
		
		super(TypeEngineSearch.ACM, clientId, clientSecret, redirectUri, emailMend, passMend, nameSLR, tammax, tags, start_year, end_year, searchsTerms);
		
		this.web = "http://dl.acm.org/results.cfm?query=@@query@@&within=owners.owner=HOSTED&filtered=@@filtered@@&start=@@start@@";
		this.idEngine = getIdSubFolder();
		this.TAM_DEF = 100;
	}
	
	@Override
	public void getLinksReferences() throws ElementNotFoundException, IOException
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		double tamDefDouble = Double.parseDouble(Integer.toString(TAM_DEF));
		//int num_paginas = (int) Math.ceil(TAM_MAX/TAM_DEF);
		int num_paginas = (int) Math.ceil(TAM_MAX/tamDefDouble);
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
		
		//Opciones para la conexion
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(true);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.waitForBackgroundJavaScript(10000);
		
		// Insertamos los parámetros necesarios en la web
		String q = QueryACM(searchsTerms);
		String query = URIUtil.encodeQuery(q);
		query = query.replaceAll("\\+", "%252B");
		String filtered = "&dte=" + start_year + "&bfr=" + end_year;
		web = web.replaceAll("@@query@@", query).replaceAll("@@filtered@@", filtered).replaceAll("@@start@@","0");
		
		System.out.println("Conectando a " + web);
		
		HtmlPage currentPage = webClient.getPage(web);
		webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		ArrayList<String> linksBib = new ArrayList<String>();
		
		for(int i = 0; i < num_paginas; i++)
		{
			if(currentPage == null)
			{
				break;
			}

			linksBib.addAll(getLinksBib(currentPage.asXml()));
			currentPage = nextPage(webClient,currentPage,i);
		}
		
		linksDocuments.addAll(linksBib);
		
		webClient.closeAllWindows();
		
		System.out.println("Se ha obtenido " + linksDocuments.size());
	}
	
	private String QueryACM(List<SearchTermParam> searchsTerms)
	{
		String query = "";
		int cont = 0;
		
		for(SearchTermParam stp : searchsTerms)
		{
			String subquery = "";
			String operator = "";
			switch (stp.getOperatorSearch())
			{
				case ALL:
					operator="+";
					break;
				case NONE:
					operator="-";
					break;
				default:
					operator=""; //any
			}
			
			String terminos = "";
			String[] wordsTerms = stp.getTerminos().split(" ");

			for(String w : wordsTerms)
			{
				terminos += operator + w + " ";
			}
			terminos = "(" + terminos.trim() + ")";
			
			switch(stp.getComponentSearch())
			{
			case ANYFIELD:
			case REVIEW:
				subquery = terminos;
				break;
			case ABSTRACT:
				subquery = "recordAbstract:"+terminos;
				break;
			case TITLE:
				subquery = "acmdlTitle:"+terminos;
				break;
			case AUTHOR:
				subquery = "persons.authors.personName:"+terminos;
				break;
			case FULLTEXT:
				subquery = "content.ftsec:"+terminos;
				break;
			case PUBLISHER:
				subquery = "acmdlPublisherName:"+terminos;
				break;
			case ISBN:
			case ISSN:
				subquery = "isbnissn.isbnissn:"+terminos;
				break;
			case DOI:
				subquery = "allDOIs.doi:"+terminos;
				break;
			case KEYWORDS:
				subquery = "keywords.author.keyword:"+terminos;
				break;
			}
			
			if (cont != searchsTerms.size()-1)
			{
				subquery += " AND ";
			}
			
			query += subquery;
			
			cont++;
		}
		
		return query;
	}
	
	private String getIdSubFolder() throws Exception
	{
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri, emailMend, passMend);
		FolderService folderservice = new FolderService(mendeleyService);
		
		List<Folder> folders = folderservice.getSubFolders(folderservice.getFolderByName(nameSLR));
		
		String idSubFolder = "";
		
		for(Folder folder : folders)
		{
			if(folder.getName().equals("acm"))
			{
				idSubFolder = folder.getId();
				break;
			}
		}
		
		if(idSubFolder.equals("")) // Creamos una carpeta
		{
			idSubFolder = folderservice.createSubFolder("ACM", folderservice.getFolderByName(nameSLR).getId()).getId();
		}
		
		return idSubFolder;
	}
	
	private HtmlPage nextPage(WebClient webClient, HtmlPage currentPage, int page) throws ElementNotFoundException, IOException
	{
		web = web.replaceAll(web.substring(web.indexOf("&start=")), "");
		web = web + "&start=" + (page * TAM_DEF);
		
		try
		{
			currentPage = (HtmlPage) webClient.getPage(web);
			webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		}
		catch(Exception ex)
		{
			currentPage = null;
		}
		
		return currentPage;
	}
	
	private ArrayList<String> getLinksBib(String code)
	{
		ArrayList<String> bibs = new ArrayList<String>();
		
		String textSource = code;
		
		textSource = textSource.replaceAll("[\n\r]", "");
		textSource = textSource.replaceAll(" +", " ");
		textSource = textSource.replaceAll("<i>", "");
		textSource = textSource.replaceAll("</i>", "");
		
		String regex = "<div class\\=\"title\"> <a href\\=\"citation\\.cfm\\?id\\=";
		String replacement = "\n" + regex;
		textSource = textSource.replaceAll(regex, replacement);
		
		String pattern = "<div class=\"title\"> <a href=\"(.+?)\".*>(.*)</a>.*";		
		Matcher matcher = Pattern.compile(pattern).matcher(textSource);
		
		while(matcher.find())
		{
			if(matcher.group(1) != null)
			{
				String link = ("http://dl.acm.org/" + matcher.group(1)).trim();
				bibs.add(link); //enlaces
			}
		}
		
		return bibs;
	}
}
