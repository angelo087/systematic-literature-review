package es.pfc.commons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.services.AnnotationService;
import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.util.Cookie;

import es.pfc.engine.EngineSearch;

public class DownloadReference implements Runnable 
{
	private String name;
	private TypeEngineSearch typeEngine;
	private WebClient webClient;
	private String url;
	private String notesCont;
	private String tags;
	private MendeleyService mendeleyService = null;
	
	public DownloadReference(TypeEngineSearch typeEngine, String name, String url, String notesCont, String tags,
			MendeleyService mendeleyService) {
		this.typeEngine = typeEngine;
		this.name = name;
		this.url = "http://www.mendeley.com/import/?url=" + url;
		this.mendeleyService = mendeleyService;
		this.notesCont = notesCont;
		this.tags = tags;
		
		// Inicializamos WebClient
		setWebClient();
	}
	
	@Override
	public void run() 
	{
		System.out.println("Soy " + this.name);
		
		boolean ok = false;
		
		try
		{
			ok = processDownload();
		}
		catch (Exception ex) 
		{ 
			System.out.println("Excepcion encontrada."); 
			setWebClient(); // Reiniciamos el WebClient.
		}
		
		if(!ok)
		{
			System.out.println(this.name + " NO ha finalizado.");
		}
		else
		{
			EngineSearch.increaseContMax(typeEngine);
			System.out.println(this.name + " ha finalizado.");
		}
		
		// Decrementamos el contador de hilos
		EngineSearch.decreaseContHilos(typeEngine);
	}
	
	private boolean processDownload() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		boolean ok = false;
		
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		HtmlPage currentPage = webClient.getPage(this.url);
		this.webClient.waitForBackgroundJavaScript(5000);
		
		int codePage = currentPage.getWebResponse().getStatusCode();
		
		if (codePage == 200)
		{
			// Comprobamos si hay que realizar el login
			if (currentPage.getTitleText().toLowerCase().contains("sign"))
			{
				System.out.println(this.name + " tiene que hacer login");
				HtmlForm form = currentPage.getForms().get(0);
				form.getInputByName("username").setValueAttribute(this.mendeleyService.getEmailMend());
				form.getInputByName("password").setValueAttribute(this.mendeleyService.getPassMend());
				
				currentPage = (HtmlPage)((HtmlButton)currentPage.getElementById("signin-form-submit")).click();
				this.webClient.waitForBackgroundJavaScript(5000);
				//Thread.sleep(10000);
			}
			
			HtmlTextArea notes = (HtmlTextArea) currentPage.getElementById("import-notes");
			notes.setText(this.notesCont);
			
			HtmlAnchor anchor = currentPage.getAnchorByText("Save");
			
			currentPage = anchor.click();
			this.webClient.waitForBackgroundJavaScript(10000);
			
			Reference reference = new Reference(this.url, this.notesCont, typeEngine);
			
			ok = EngineSearch.addReferenceToEngineSearch(reference);
		}
		
		return ok;
	}
	
	private void setWebClient()
	{
		// Inicializamos WebClient
		webClient = new WebClient(BrowserVersion.FIREFOX_17);
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
	}
	
	public void closeWebClient()
	{
		if(!this.webClient.equals(null))
		{
			this.webClient.closeAllWindows();
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = "http://www.mendeley.com/import/?url=" + url;
	}
}
