package es.uca.pfc.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import es.uca.pfc.engine.EngineSearch;
import es.uca.pfc.enums.TypeEngineSearch;

public class ImportReferenceMendeley implements Runnable {
	
	private String name;
	private TypeEngineSearch typeEngine;
	private WebClient webClient;
	private String url;
	private String nameSLR;
	private MendeleyService mendeleyService;
	private String prefixUrl;
	
	private static final String PREFIX_URL_DOI = "http://www.mendeley.com/import/?doi=";
	private static final String PREFIX_URL_URL = "http://www.mendeley.com/import/?url=";
	
	public ImportReferenceMendeley(String name, TypeEngineSearch typeEngine,
			WebClient webClient, String url, String nameSLR,
			MendeleyService mendeleyService) {
		
		this.name = name;
		this.typeEngine = typeEngine;
		
		if (TypeEngineSearch.ACM == this.typeEngine)
		{
			prefixUrl = PREFIX_URL_URL;
		}
		else
		{
			prefixUrl = PREFIX_URL_DOI;
		}
		
		this.webClient = webClient;
		this.url = prefixUrl + url;
		this.nameSLR = nameSLR;
		this.mendeleyService = mendeleyService;
	}
	
	public void closeWebClient() {
		if (webClient != null) {
			webClient.closeAllWindows();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeEngineSearch getTypeEngine() {
		return typeEngine;
	}

	public void setTypeEngine(TypeEngineSearch typeEngine) {
		this.typeEngine = typeEngine;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (TypeEngineSearch.ACM == this.typeEngine)
		{
			prefixUrl = PREFIX_URL_URL;
		}
		else
		{
			prefixUrl = PREFIX_URL_DOI;
		}
		this.url = prefixUrl + url;
	}

	public String getNameSLR() {
		return nameSLR;
	}

	public void setNameSLR(String nameSLR) {
		this.nameSLR = nameSLR;
	}

	public MendeleyService getMendeleyService() {
		return mendeleyService;
	}

	public void setMendeleyService(MendeleyService mendeleyService) {
		this.mendeleyService = mendeleyService;
	}
	
	public String getPrefixUrl() {
		return prefixUrl;
	}

	@Override
	public void run() {
		System.out.println("Soy " + this.name + " con " + this.url);
		
		boolean ok = false;
		
		try
		{
			ok = doImport();
		}
		catch(Exception ex)
		{
			ok = false;
			System.out.println(this.name + "=> Excepcion: " + ex.getMessage());
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
	
	private boolean doImport() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		boolean ok = false;
		
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		HtmlPage currentPage = webClient.getPage(this.url);
		this.webClient.waitForBackgroundJavaScript(5000);
		
		if(currentPage.getWebResponse().getStatusCode() == 200 && currentPage.getTitleText().toLowerCase().contains("importer"))
		{
			HtmlSelect folderSelect = (HtmlSelect) currentPage.getElementById("document-add-to");
			int tries = EngineSearch.TOTAL_TRIES;
			while(tries > 0 && folderSelect == null)
			{
				tries--;				
				synchronized(currentPage) {
					currentPage.wait(5000);
				}
				folderSelect = (HtmlSelect) currentPage.getElementById("document-add-to");
			}
			
			if (folderSelect == null)
			{
				System.out.println(this.name + " => Tras varios intentos, no ha cargado la página. => " + currentPage.getTitleText());
				/*try
				{
					PrintWriter writer = new PrintWriter("codeACM.txt", "UTF-8");
				    writer.println(currentPage.asXml());
				    writer.close();
				}
				catch(Exception ex) { }*/
			}
			else
			{
				HtmlOption optionSelected = getFolderOption(folderSelect);
				if (optionSelected != null)
				{
					folderSelect.setSelectedAttribute(optionSelected, true);
				}
				HtmlAnchor anchor = currentPage.getAnchorByText("Save");
				if (anchor != null)
				{
					currentPage = anchor.click();
					webClient.waitForBackgroundJavaScript(10000);
					
					Reference reference = new Reference(url, "", typeEngine);
					
					ok = EngineSearch.addReferenceToEngineSearch(reference);
				}
			}
		}
		else
		{
			System.out.println(this.name + " => No es importer (" + currentPage.getWebResponse().getStatusCode() + ") " + currentPage.getTitleText());
		}
		
		return ok;
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
