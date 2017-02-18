package es.uca.pfc.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlButton;

import es.uca.pfc.commons.SearchTermParam;
import es.uca.pfc.engine.EngineSearch;
import es.uca.pfc.engine.EngineSearchACM;
import es.uca.pfc.engine.EngineSearchIEEE;
import es.uca.pfc.engine.EngineSearchSCIENCE;
import es.uca.pfc.enums.ComponentSearch;
import es.uca.pfc.enums.OperatorSearch;
import es.uca.pfc.enums.TypeEngineSearch;
import mendeley.pfc.services.MendeleyService;

public class BackgroundSearchMendeley {

	private MendeleyService mendeleyService = null;
	private List<WebClient> webClients = new ArrayList<WebClient>();
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private Map<TypeEngineSearch, Boolean> optionsEngine = new HashMap<TypeEngineSearch, Boolean>();
	private String nameSLR;
	private int tammax;
	private List<String> tags = new ArrayList<String>();
	private int start_year;
	private int end_year;
	private List<SearchTermParam> searchsTerms;
	private Map<TypeEngineSearch,String> apiKeysEngine;
	private int total_hilos;
	private int total_tries;
	
	public static void main(String[] args) {
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();

		String 	client_id = "1044", 
				client_secret = "5qQ6zm5iYpvUehj4", 
				redirect_url = "http://localhost:8095/SLR/indexMendeley/",
				access_token  = "asdsadasd",
				refresh_token = "asdasdaaaaaaa";
		
		String email = "angel.gonzatoro@gmail.com", password = "Number98*";
		String nameSLR = "SLR1: spem study";
		
		int total_hilos = 5;
		int total_tries = 5;
		int tammax = 5;
		
		int start_year = 2010, end_year = 2017;
		
		SearchTermParam stp01 = new SearchTermParam(ComponentSearch.ANYFIELD, OperatorSearch.ALL, "word");
		SearchTermParam stp02 = new SearchTermParam(ComponentSearch.AUTHOR, OperatorSearch.NONE, "toro");
		//SearchTermParam stp03 = new SearchTermParam(ComponentSearch.TITLE, OperatorSearch.ANY, "of");
		List<SearchTermParam> searchsTerms = new ArrayList<SearchTermParam>();
		searchsTerms.add(stp01); searchsTerms.add(stp02); //searchsTerms.add(stp03);
		
		List<String> tags = new ArrayList<String>();
		tags.add("cr_included"); tags.add("met_met1_yes"); tags.add("met_met2_35"); tags.add("met_met3_ingles");
		
		Map<TypeEngineSearch, Boolean> optionsEngine = new HashMap<TypeEngineSearch, Boolean>();
		optionsEngine.put(TypeEngineSearch.ACM, false);
		optionsEngine.put(TypeEngineSearch.IEEE, true);
		optionsEngine.put(TypeEngineSearch.SCIENCE, false);
		optionsEngine.put(TypeEngineSearch.SPRINGER, false);
		
		Map<TypeEngineSearch,String> apiKeysEngine = new HashMap<TypeEngineSearch, String>();
		apiKeysEngine.put(TypeEngineSearch.SCIENCE, "80aa542193705ce36ebfe094078b9aa3");
		apiKeysEngine.put(TypeEngineSearch.SPRINGER, "c8c8ee4b2c20f0046806762317d0d6e2");
		
		try
		{
			BackgroundSearchMendeley backgroundSearch = new BackgroundSearchMendeley(client_id, client_secret, access_token, 
					refresh_token, redirect_url, email, password, optionsEngine, nameSLR, 
					tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, total_hilos, total_tries);
			backgroundSearch.startSearchs();
			//backgroundSearch.startSearchsWithThreads();
			//System.out.println(backgroundSearch.getTotalReferences());
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPCION: " + ex.getMessage());
		}
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecucion en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecucion en milisegundos
	}
	
	public BackgroundSearchMendeley(String ci, String cs, String at, String rt, String ru, String email, String pass,
			Map<TypeEngineSearch, Boolean> optionsEngine, String nameSLR, int tammax, List<String> tags, 
			int start_year, int end_year, List<SearchTermParam> searchsTerms, 
			Map<TypeEngineSearch,String> apiKeysEngine, int total_hilos, int total_tries) throws Exception {

		this.mendeleyService = new MendeleyService(ci,cs,ru,email,pass);
		this.clientId = ci;
		this.clientSecret = cs;
		this.redirectUri = ru;
		this.optionsEngine= optionsEngine;
		this.nameSLR = nameSLR;
		this.tammax = tammax;
		this.tags = tags;
		this.start_year = start_year;
		this.end_year = end_year;
		this.searchsTerms = searchsTerms;
		this.apiKeysEngine = apiKeysEngine;
		this.total_hilos = total_hilos;
		this.total_tries = total_tries;
		
		inicialiceArrayWebClients();
	}
	
	public void startSearchsWithThreads() throws Exception
	{
		// Comprobamos que los webClients esten inicializados correctamente.
		int tries = total_tries;
		while(tries > 0 && !hasAllFirstLogin())
		{
			System.out.println("Hay que reiniciar algun webClient.");
			inicialiceWebClient();
			tries--;
		}
		
		if (!hasAllFirstLogin())
		{
			System.out.println("Todos los hilos no tienen el login.");
		}
		else
		{
			System.out.println("COMIENZA PROCESO BUSQUEDA");
			
			Thread tACM = new Thread(), tIEEE = new Thread(), tSCIENCE = new Thread();
			
			if(optionsEngine.get(TypeEngineSearch.ACM))
			{
				tACM = new Thread(new EngineSearchACM(clientId, clientSecret, redirectUri, mendeleyService, 
						nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
						total_hilos, total_tries));
				tACM.start();
			}
			
			if(optionsEngine.get(TypeEngineSearch.IEEE))
			{
				tIEEE = new Thread(new EngineSearchIEEE(clientId, clientSecret, redirectUri, mendeleyService, 
						nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
						total_hilos, total_tries));
				tIEEE.start();
			}
			
			if(optionsEngine.get(TypeEngineSearch.SCIENCE))
			{
				tSCIENCE = new Thread(new EngineSearchSCIENCE(clientId, clientSecret, redirectUri, mendeleyService, 
						nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
						total_hilos, total_tries));
				tSCIENCE.start();
			}
			
			while(tACM.isAlive() || tIEEE.isAlive() || tSCIENCE.isAlive())
			{
				// Wait finish process
			}
			
			int totalReferences = EngineSearchACM.references.size() + EngineSearchIEEE.references.size() +
					EngineSearchSCIENCE.references.size();
			
			System.out.println("TOTAL ENCONTRADOS => " + totalReferences);
			
			System.out.println("PROCESO BUSQUEDA FINALIZADO.");
		}
	}
	
	public void startSearchs() throws Exception
	{
		if(hasEnginesForSearch())
		{
			// Comprobamos que los webClients esten inicializados correctamente.
			int tries = total_tries;
			while(tries > 0 && !hasAllFirstLogin())
			{
				System.out.println("Hay que reiniciar algun webClient.");
				inicialiceWebClient();
				tries--;
			}
			
			if (!hasAllFirstLogin())
			{
				System.out.println("Todos los hilos no tienen el login.");
			}
			else
			{				
				EngineSearch engineSearch = null;
				for(Map.Entry<TypeEngineSearch, Boolean> entry : optionsEngine.entrySet())
				{
					if (Boolean.TRUE.equals(entry.getValue()))
					{
						System.out.println("COMIENZA PROCESO BUSQUEDA DE " + entry.getKey().getKey());
						switch(entry.getKey()) {
							case ACM:
								engineSearch = new EngineSearchACM(clientId, clientSecret, redirectUri, mendeleyService, 
										nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
										total_hilos, total_tries);
								break;
							case IEEE:
								engineSearch = new EngineSearchIEEE(clientId, clientSecret, redirectUri, mendeleyService, 
										nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
										total_hilos, total_tries);
								break;
							case SCIENCE:
								engineSearch = new EngineSearchSCIENCE(clientId, clientSecret, redirectUri, mendeleyService, 
										nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, webClients, 
										total_hilos, total_tries);
								break;
							case SPRINGER:
								break;
							default:
								break;
						}
						engineSearch.startSearch();
					} // fin if
				} //fin for
				
				int totalReferences = EngineSearchACM.references.size() + EngineSearchIEEE.references.size() +
						EngineSearchSCIENCE.references.size();
				
				System.out.println("TOTAL ENCONTRADOS => " + totalReferences);
				
				System.out.println("PROCESO BUSQUEDA FINALIZADO.");
				
			} // fin else
		} // fin if
	}
	
	private boolean hasEnginesForSearch()
	{
		boolean result = false;
		
		for(Map.Entry<TypeEngineSearch, Boolean> entry : optionsEngine.entrySet())
		{
			if (Boolean.TRUE.equals(entry.getValue()))
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	private void inicialiceArrayWebClients() throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		webClients = new ArrayList<WebClient>();
		WebClient webClient = firstLogin();
		WebClient wbAux = null;
		
		int tries = 1;
		while(webClient == null && tries <= total_tries)
		{
			System.out.println("Login incorrecto => Intento " + tries + " de " + total_tries);
			webClient = firstLogin();
			tries++;
		}
		
		if(webClient != null)
		{
			for(int i = 0; i < total_hilos; i++)
			{
				wbAux = createWebClient();
				wbAux.setCookieManager(webClient.getCookieManager());
				webClients.add(wbAux);
			}
		}
	}
	
	private WebClient firstLogin() throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		WebClient webClient = null;
		try
		{
			webClient = createWebClient();
			
			HtmlPage currentPage = webClient.getPage("https://www.mendeley.com/sign/in/");
			webClient.waitForBackgroundJavaScript(5000);
			
			HtmlForm form = currentPage.getForms().get(0);
			form.getInputByName("username").setValueAttribute(mendeleyService.getEmailMend());
			form.getInputByName("password").setValueAttribute(mendeleyService.getPassMend());
	
			currentPage = (HtmlPage)((HtmlButton)currentPage.getElementById("signin-form-submit")).click();
			webClient.waitForBackgroundJavaScript(5000);
			
			if (!currentPage.getTitleText().toLowerCase().contains("feed"))
			{
				webClient = null;
			}
		}
		catch(Exception ex)
		{
			webClient = null;
		}
		
		return webClient;
	}

	private WebClient createWebClient()
	{
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
		
		return webClient;
	}
	
	private boolean hasAllFirstLogin()
	{
		boolean result = true;
		
		for(WebClient wc : webClients)
		{
			if (wc == null)
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	private void inicialiceWebClient() throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		int pos = 0;
		List<WebClient> webClientsCopy = new ArrayList<WebClient>();
		for(WebClient wc : webClientsCopy)
		{
			if (wc == null)
			{
				webClients.remove(pos);
				webClients.add(pos, firstLogin());
			}
			pos++;
		}
	}
	
	public int getTotalReferences()
	{
		return EngineSearch.referencesEngineSearch.size();
	}
}
