package mendeley.pfc.engines;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.ParamSearch;
import mendeley.pfc.commons.TypeEngine;
import mendeley.pfc.schemas.ReferenceSearch;
import mendeley.pfc.searchs.ParallelSearchs;
import mendeley.pfc.services.MendeleyService;

import org.apache.commons.httpclient.HttpException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class EngineSearchIEEE extends EngineSearch {

	// Listado de referencias
	public static List<ReferenceSearch> references = new ArrayList<ReferenceSearch>();

	private final static String web = "http://dl.acm.org/";
	
	private ArrayList<String> idsDocuments = new ArrayList<String>();
	private ArrayList<String> linksDocuments = new ArrayList<String>();
	private ArrayList<String> titlesDocuments = new ArrayList<String>();
	private ArrayList<String> publisherDocuments = new ArrayList<String>();
	private ArrayList<String> yearDocuments = new ArrayList<String>();
	private ArrayList<String> keysDocuments = new ArrayList<String>();
	private ArrayList<String> abstractDocuments = new ArrayList<String>();
	
	public static int contMax	= 0;
	public static int contHilos = 0;
	
	public EngineSearchIEEE(MendeleyService mendeleyService, String name,
			List<ParamSearch> terms, int tammax, List<String> etiquetas, String operator,
			int start_year, int end_year, String componente) throws HttpException, MendeleyException, IOException {

		super(TypeEngine.IEEE, mendeleyService, name, terms, tammax, etiquetas, operator, start_year,
				end_year, componente);
	}
	
	@Override
	public void createQuerySearch()
	{
		terminos = "";
		for(int p=0; p<paramsSearchs.size(); p++)
		{
			ParamSearch param = paramsSearchs.get(p);
			String[] values = param.getValue().trim().split(" ");
			
			String value = "(";
			
			for(int i=0; i<values.length; i++)
			{
				String v = values[i];
				
				switch(param.getOperator())
				{
					case ALL:
						value += "+"+v;
						break;
					case ANY:
						value += v;
						break;
					case NOT:
						value += "-"+v;
						break;
				}
				if((i+1) != values.length)
				{
					value += " ";
				}
			}
			
			value += ")";
			
			switch(param.getComponent())
			{
				case TITLE:
					value = "acmdlTitle:" + value;
					break;
				case AUTHOR:
					value = "persons.authors.personName:" + value;
					break;
				case ABSTRACT:
					value = "recordAbstract:" + value;
					break;
				case FULLTEXT:
					value = "content.ftsec:" + value;
					break;
				case PUBLISHER:
					value = "acmdlPublisherName:" + value;
					break;
				case ISBN:
				case ISSN: 
					value = "isbnissn.isbnissn:" + value;
					break;
				case DOI:
					value = "allDOIs.doi:" + value;
					break;
				case KEYWORDS:
					value = "keywords.author.keyword:" + value;
					break;
			}
			
			terminos += value;
			
			if((p+1) != paramsSearchs.size())
			{
				terminos += " AND ";
			}
			terminos = "(+spem)";
			System.out.println("TERMINOS: " + terminos);
			
		}
	}

	@Override
	public int getLinksReferences() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		int num_paginas = (int) Math.ceil(TAM_MAX/TAM_DEF);
		
		//Opciones para la conexion
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
		webClient.getOptions().setJavaScriptEnabled(false);
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
		
		//Accedemos al formulario de busqueda de ACM
		HtmlPage currentPage = webClient.getPage(web);		
		
		HtmlForm form = (HtmlForm) currentPage.getFormByName("qiksearch");
		
		form.getInputByName("query").setValueAttribute(terminos);
		
		currentPage = (HtmlPage) form.getInputByName("Go").click();
		webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		ArrayList<String> linksBib = new ArrayList<String>();

		for(int i = 1; i <= num_paginas; i++)
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
		
		return linksDocuments.size();
	}
	
	public WebClient getWebClientForMendeley(List<String> linksBib) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		boolean isLogin = false;
		int index = -1;
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
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
		
		while(!isLogin)
		{
			index++;
			//Para evitar que salga el texto por la salida estandar
			org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
			Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
			Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);

			String url = linksBib.get(index);
			//System.out.println("ProcessDownload: " + url);
			
			HtmlPage currentPage = (HtmlPage) webClient.getPage("http://www.mendeley.com/import/?url="+url);
			webClient.waitForBackgroundJavaScript(5000);
			
			int codePage = currentPage.getWebResponse().getStatusCode();
			
			if(codePage == 200)
			{
				if(currentPage.getTitleText().toLowerCase().contains("sign")) //Hay que realizar el Login en Mendeley
				{
					//System.out.println("Realizando Login... Intento " + (index+1) + " de " + linksBib.size());
					HtmlForm form = currentPage.getForms().get(0);
					form.getInputByName("username").setValueAttribute(this.mendeleyService.getEmailMend());
					form.getInputByName("password").setValueAttribute(this.mendeleyService.getPassMend());
					
					HtmlButton btSignUp = (HtmlButton) currentPage.getElementById("signin-form-submit");
					currentPage = (HtmlPage) btSignUp.click();
					Thread.sleep(5000);
					if(currentPage.getTitleText().toLowerCase().trim().equals(("Web Importer | Mendeley").toLowerCase().trim()))
					{
						isLogin = true;
					}
				}
			}
		}
		
		return webClient;
	}
	
	// Metodo privado que descarga las referencias y las importa a Mendeley
	@Override
	public void downloadReferencesToMendeley() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		ArrayList<String> linksBib = linksDocuments;
		int index    = 0;
		WebClient webClient = getWebClientForMendeley(linksBib);
		
		// Creamos 5 hilos para  busquedas paralelas
		Thread t1 = new Thread(), t2 = new Thread(), t3 = new Thread(), t4 = new Thread(), t5 = new Thread();
		ParallelSearchs search01 = null, search02 = null, search03 = null, search04 = null, search05 = null;
		
		synchronized (linksBib) 
		{
			while(EngineSearchIEEE.contMax < TAM_MAX && index < linksBib.size())
			{
				if(EngineSearchIEEE.references.size() + EngineSearchIEEE.contHilos < TAM_MAX)
				{
					if(t1 == null || !t1.isAlive())
					{
						if(search01 == null)
						{
							search01 = new ParallelSearchs("IEEE-Hilo1","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,TypeEngine.IEEE, mendeleyService, webClient);
						}
						else
						{
							search01.setUrl(linksBib.get(index));
						}
						t1 = new Thread(search01);
						index++;
						EngineSearchIEEE.contHilos++;
						t1.start();
					}
					else if(t2 == null || !t2.isAlive())
					{
						if(search02 == null)
						{
							search02 = new ParallelSearchs("IEEE-Hilo2","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,TypeEngine.IEEE, mendeleyService, webClient);
						}
						else
						{
							search02.setUrl(linksBib.get(index));
						}
						t2 = new Thread(search02);
						index++;
						EngineSearchIEEE.contHilos++;
						t2.start();
					}
					else if(t3 == null || !t3.isAlive())
					{
						if(search03 == null)
						{
							search03 = new ParallelSearchs("IEEE-Hilo3","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,TypeEngine.IEEE, mendeleyService, webClient);
						}
						else
						{
							search03.setUrl(linksBib.get(index));
						}
						t3 = new Thread(search03);
						index++;
						EngineSearchIEEE.contHilos++;
						t3.start();
					}
					else if(t4 == null || !t4.isAlive())
					{
						if(search04 == null)
						{
							search04 = new ParallelSearchs("IEEE-Hilo4","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,TypeEngine.IEEE, mendeleyService, webClient);
						}
						else
						{
							search04.setUrl(linksBib.get(index));
						}
						t4 = new Thread(search04);
						index++;
						EngineSearchIEEE.contHilos++;
						t4.start();
					}
					else if(t5 == null || !t5.isAlive())
					{
						if(search05 == null)
						{
							search05 = new ParallelSearchs("IEEE-Hilo5","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,TypeEngine.IEEE, mendeleyService, webClient);
						}
						else
						{
							search05.setUrl(linksBib.get(index));
						}
						t5 = new Thread(search05);
						index++;
						EngineSearchIEEE.contHilos++;
						t5.start();
					}
					
				}
			} //fin-while
			
			while(t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive() || t5.isAlive())
			{
				// Esperamos a que finalicen los hilos
			}
		}
		
		if(search01 != null)
			search01.closeWebClient();
		if(search02 != null)
			search02.closeWebClient();
		if(search03 != null)
			search03.closeWebClient();
		if(search04 != null)
			search04.closeWebClient();
		if(search05 != null)
			search05.closeWebClient();
	}
	
	private ArrayList<String> getLinksBib(String code) throws IOException
	{
		ArrayList<String> bibs = new ArrayList<String>();
		
		String textSource = code.trim();
		
		textSource = textSource.replaceAll("[\n\r]", "");
		textSource = textSource.replaceAll(" +", " ");
		textSource = textSource.replaceAll("<i>", "");
		textSource = textSource.replaceAll("</i>", "");
		
		String regex = "<a href=\"citation.cfm";
		String replacement = "\n" + regex;
		textSource = textSource.replaceAll(regex, replacement);
		
		String pattern = "<a href=\"(.+?)\" target=\"_self\">.*";

		Matcher matcher = Pattern.compile(pattern).matcher(textSource);
		
		while(matcher.find())
		{
			bibs.add(web+matcher.group(1).trim());
		}

		return bibs;
	}
	
	private HtmlPage nextPage(WebClient webClient, HtmlPage currentPage, int p) throws IOException
	{		
		HtmlPage nextPage = currentPage;
		
		try
		{
			HtmlAnchor next = (HtmlAnchor) nextPage.getAnchorByText(Integer.toString((p+1)));
			nextPage = (HtmlPage) next.click();
			webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		}
		catch(ElementNotFoundException ex)
		{
			nextPage = null;
		}
		
		return nextPage;
	}

	public ArrayList<String> getIdsDocuments() {
		return idsDocuments;
	}

	public void setIdsDocuments(ArrayList<String> idsDocuments) {
		this.idsDocuments = idsDocuments;
	}

	public ArrayList<String> getLinksDocuments() {
		return linksDocuments;
	}

	public void setLinksDocuments(ArrayList<String> linksDocuments) {
		this.linksDocuments = linksDocuments;
	}

	public ArrayList<String> getTitlesDocuments() {
		return titlesDocuments;
	}

	public void setTitlesDocuments(ArrayList<String> titlesDocuments) {
		this.titlesDocuments = titlesDocuments;
	}

	public ArrayList<String> getPublisherDocuments() {
		return publisherDocuments;
	}

	public void setPublisherDocuments(ArrayList<String> publisherDocuments) {
		this.publisherDocuments = publisherDocuments;
	}

	public ArrayList<String> getYearDocuments() {
		return yearDocuments;
	}

	public void setYearDocuments(ArrayList<String> yearDocuments) {
		this.yearDocuments = yearDocuments;
	}

	public ArrayList<String> getKeysDocuments() {
		return keysDocuments;
	}

	public void setKeysDocuments(ArrayList<String> keysDocuments) {
		this.keysDocuments = keysDocuments;
	}

	public ArrayList<String> getAbstractDocuments() {
		return abstractDocuments;
	}

	public void setAbstractDocuments(ArrayList<String> abstractDocuments) {
		this.abstractDocuments = abstractDocuments;
	}

	public static String getWeb() {
		return web;
	}
}
