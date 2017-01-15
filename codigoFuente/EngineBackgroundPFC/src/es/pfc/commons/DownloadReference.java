package es.pfc.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import es.pfc.engine.EngineSearch;

public class DownloadReference implements Runnable 
{
	private String name;
	private TypeEngineSearch typeEngine;
	private WebClient webClient;
	private String url;
	private String tags;
	private String emailMend;
	private String passMend;
	private String nameSLR;
	
	public DownloadReference(TypeEngineSearch typeEngine, String name, String url, String tags,
			String emailMend, String passMend, String nameSLR) {
		this.typeEngine = typeEngine;
		this.name = name;
		//this.url = "http://www.mendeley.com/import/?url=" + url;
		this.url = (this.typeEngine == TypeEngineSearch.IEEE ? "http://www.mendeley.com/import/?doi=" : "http://www.mendeley.com/import/?url=") + url;
		this.emailMend = emailMend;
		this.passMend = passMend;
		this.tags = tags;
		this.nameSLR = nameSLR;
		
		// Inicializamos WebClient
		setWebClient();
	}
	
	public DownloadReference(TypeEngineSearch typeEngine, String name, String url, String tags,
			MendeleyService mendeleyService) {
		this.typeEngine = typeEngine;
		this.name = name;
		//this.url = "http://www.mendeley.com/import/?url=" + url;
		this.url = (this.typeEngine == TypeEngineSearch.IEEE ? "http://www.mendeley.com/import/?doi=" : "http://www.mendeley.com/import/?url=") + url;
		this.emailMend = mendeleyService.getEmailMend();
		this.passMend = mendeleyService.getPassMend();
		this.tags = tags;
		
		// Inicializamos WebClient
		setWebClient();
	}
	
	@Override
	public void run() 
	{
		System.out.println("Soy " + this.name + " con " + this.url);
		
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
				form.getInputByName("username").setValueAttribute(this.emailMend);
				form.getInputByName("password").setValueAttribute(this.passMend);
				
				currentPage = (HtmlPage)((HtmlButton)currentPage.getElementById("signin-form-submit")).click();
				this.webClient.waitForBackgroundJavaScript(5000);
				//Thread.sleep(10000);
			}
			
			HtmlSelect folderSelect = (HtmlSelect) currentPage.getElementById("document-add-to");
			HtmlOption optionSelected = getFolderOption(folderSelect);
			if (optionSelected != null)
			{
				folderSelect.setSelectedAttribute(optionSelected, true);
			}
			
			HtmlAnchor anchor = currentPage.getAnchorByText("Save");
			
			currentPage = anchor.click();
			this.webClient.waitForBackgroundJavaScript(10000);
			
			Reference reference = new Reference(this.url, "", typeEngine);
			
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
		//this.url = "http://www.mendeley.com/import/?url=" + url;
		this.url = (this.typeEngine == TypeEngineSearch.IEEE ? "http://www.mendeley.com/import/?doi=" : "http://www.mendeley.com/import/?url=") + url;
	}
	
	private HtmlOption getFolderOption(HtmlSelect folderSelect)
	{
		HtmlOption optionSelected = null;
		
		boolean canCatch = false;
		
		for(HtmlOption option : folderSelect.getOptions())
		{
			if (canCatch)
			{
				if (option.getText().toLowerCase().trim().contains(typeEngine.getKey().toLowerCase().trim()))
				{
					optionSelected = option;
					break;
				}
			}
			
			if (option.getText().toLowerCase().trim().contains(nameSLR.toLowerCase().trim()))
			{
				canCatch = true;
			}
		}
		
		return optionSelected;
	}
}
