package background.pfc.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.services.MendeleyService;
import background.pfc.engine.EngineSearch;
import background.pfc.enums.TypeEngineSearch;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;


/**
 * Clase que recoge cada una de las referencias para ser importadas posteriormente
 * a Mendeley.
 * 
 * @author agonzatoro
 *
 */
public class ImportReferenceMendeley implements Runnable {
	
	/** name .*/
	private String name;
	/** typeEngine .*/
	private TypeEngineSearch typeEngine;
	/** webClient .*/
	private WebClient webClient;
	/** url .*/
	private String url;
	/** nameSLR .*/
	private String nameSLR;
	/** mendeleyService .*/
	private MendeleyService mendeleyService;
	/** prefixUrl .*/
	private String prefixUrl;
	
	/** PREFIX_URL_DOI .*/
	private static final String PREFIX_URL_DOI = "http://www.mendeley.com/import/?doi=";
	/** PREFIX_URL_URL .*/
	private static final String PREFIX_URL_URL = "http://www.mendeley.com/import/?url=";
	
	/**
	 * Constructor de la clase ImportReferenceMendeley
	 * 
	 * @param name String
	 * @param typeEngine TypeEngineSearch
	 * @param webClient WebClient
	 * @param url String
	 * @param nameSLR String
	 * @param mendeleyService MendeleyService
	 */
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
	
	/**
	 * M�todo que cierra la conexi�n del WebClient asociado.
	 */
	public void closeWebClient() {
		if (webClient != null) {
			webClient.closeAllWindows();
		}
	}

	/**
	 * M�todo que devuelve el nombre del hilo asociado.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * M�todo que inserta el nombre del hilo asociado.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * M�todo que devuelve el TypeEngine asociado a la referencia.
	 * 
	 * @return TypeEngineSearch
	 */
	public TypeEngineSearch getTypeEngine() {
		return typeEngine;
	}

	/**
	 * M�todo que establece el TypeEngineSearch a la referencia a importar.
	 * 
	 * @param typeEngine TypeEngineSearch
	 */
	public void setTypeEngine(TypeEngineSearch typeEngine) {
		this.typeEngine = typeEngine;
	}

	/**
	 * M�todo que devuelve el WebClient asociado a la referencia a importar.
	 * 
	 * @return WebClient
	 */
	public WebClient getWebClient() {
		return webClient;
	}

	/**
	 * M�todo que establece el WebClient de la referencia a importar.
	 * 
	 * @param webClient WebClient
	 */
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}

	/**
	 * M�todo que devuelve la url de la referencia a importar.
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * M�todo que establece la url de la referencia a importar.
	 * 
	 * @param url String
	 */
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

	/**
	 * M�todo que devuelve el nombre del SLR de la referencia a importar.
	 * 
	 * @return String
	 */
	public String getNameSLR() {
		return nameSLR;
	}

	/**
	 * M�todo que establece el nombre del SLR de la referencia a importar.
	 * 
	 * @param nameSLR String
	 */
	public void setNameSLR(String nameSLR) {
		this.nameSLR = nameSLR;
	}

	/**
	 * M�todo que devuelve la conexi�n con Mendeley de la referencia a importar.
	 * 
	 * @return MendeleyService
	 */
	public MendeleyService getMendeleyService() {
		return mendeleyService;
	}

	/**
	 * M�todo que establece la conexi�n con Mendeley de la referencia a importar.
	 * 
	 * @param mendeleyService MendeleyService
	 */
	public void setMendeleyService(MendeleyService mendeleyService) {
		this.mendeleyService = mendeleyService;
	}
	
	/**
	 * M�todo que devuelve la url base que usa la referencia para importar a mendeley.
	 * 
	 * @return String
	 */
	public String getPrefixUrl() {
		return prefixUrl;
	}

	/**
	 * M�todo que realiza el proceso paralelo para importar la referencia.
	 */
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
	
	/**
	 * M�todo privado que realiza la importaci�n de la referencia a Mendeley e indica
	 * si se ha realizado correctamente o no.
	 * 
	 * @return boolean
	 * @throws FailingHttpStatusCodeException FailingHttpStatusCodeException
	 * @throws MalformedURLException MalformedURLException
	 * @throws IOException IOException
	 * @throws InterruptedException InterruptedException
	 */
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
	
	/**
	 * M�todo privado que devuelve el HtmlOption que contiene el engine (folder) donde se escoge
	 * la carpeta donde va a ser importada.
	 * 
	 * @param folderSelect HtmlSelect
	 * @return HtmlOption
	 */
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
