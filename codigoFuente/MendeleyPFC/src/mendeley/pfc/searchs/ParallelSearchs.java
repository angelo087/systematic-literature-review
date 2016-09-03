package mendeley.pfc.searchs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.commons.TypeEngine;
import mendeley.pfc.engines.EngineSearch;
import mendeley.pfc.engines.EngineSearchACM;
import mendeley.pfc.engines.EngineSearchIEEE;
import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.schemas.ReferenceSearch;
import mendeley.pfc.services.AnnotationService;
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
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

public class ParallelSearchs implements Runnable {

	private String name;
	private TypeEngine engine;
	private WebClient webClient;
	private String url;
	private String notesCont;
	private String tags;
	private MendeleyService mendeleyService = null;
	
	public ParallelSearchs(String name, String notesCont, String url, String tags, TypeEngine engine, MendeleyService mendeleyService)
	{
		this.name = name;
		this.url = "http://www.mendeley.com/import/?url=" + url;
		this.mendeleyService = mendeleyService;
		this.engine = engine;
		this.notesCont = notesCont;
		this.tags = tags;
		
		// Inicializamos WebClient
		setWebClient();
	}
	
	public ParallelSearchs(String name, String notesCont, String url, String tags, TypeEngine engine, MendeleyService mendeleyService, WebClient webClient)
	{
		this.name = name;
		this.url = "http://www.mendeley.com/import/?url=" + url;
		this.mendeleyService = mendeleyService;
		this.engine = engine;
		this.notesCont = notesCont;
		this.tags = tags;
		
		setWebClient(webClient);
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
	
	private void setWebClient(WebClient webClient)
	{
		setWebClient();
		this.webClient.setCache(webClient.getCache());
		this.webClient.setCookieManager(webClient.getCookieManager());
		this.webClient.setCredentialsProvider(webClient.getCredentialsProvider());
		this.webClient.setWebConnection(webClient.getWebConnection());
	}
	
	public void closeWebClient()
	{
		if(!this.webClient.equals(null))
		{
			this.webClient.closeAllWindows();
		}
	}
	
	public void setUrl(String url)
	{
		this.url = "http://www.mendeley.com/import/?url=" + url;
	}
	
	public String getUrl() { return url; }
	
	private String getIdDoc() throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		String idDoc = "";
		
		AnnotationService annotationservice = new AnnotationService(this.mendeleyService);
		Annotation annotationSelected = null;
		for(Annotation annotation : annotationservice.getAllAnnotations())
		{
			if(annotation.getText().equals(this.notesCont))
			{
				annotationSelected = annotation;
				idDoc = annotation.getDocument();
				break;
			}
		}
		
		annotationservice.deleteAnnotation(annotationSelected);
		
		return idDoc;
	}
	
	private boolean processDownload() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		boolean ok = false;
		
		//System.out.println("ProcessDownload: " + url);
		
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
				
		HtmlPage currentPage = (HtmlPage) webClient.getPage(this.url);	
		webClient.waitForBackgroundJavaScript(5000);
		
		int codePage = currentPage.getWebResponse().getStatusCode();
		
		if(codePage == 200)
		{	
			if(currentPage.getTitleText().toLowerCase().contains("sign")) //Hay que realizar el Login en Mendeley
			{
				//System.out.println(this.name + " tiene que hacer login.");
				HtmlForm form = currentPage.getForms().get(0);
				form.getInputByName("username").setValueAttribute(this.mendeleyService.getEmailMend());
				form.getInputByName("password").setValueAttribute(this.mendeleyService.getPassMend());
				
				HtmlButton btSignUp = (HtmlButton) currentPage.getElementById("signin-form-submit");
				currentPage = (HtmlPage) btSignUp.click();
				Thread.sleep(5000);
			}
						
			HtmlTextArea notes = (HtmlTextArea) currentPage.getElementById("import-notes");
			notes.setText(this.notesCont);
			
			HtmlInput importTags = (HtmlInput) currentPage.getElementById("import-tags");
			importTags.setValueAttribute(this.tags);
			
			HtmlAnchor anchor = currentPage.getAnchorByText("Save");
			
			currentPage = anchor.click();
			this.webClient.waitForBackgroundJavaScript(10000);
			
			String idDoc = getIdDoc();
			
			if(TypeEngine.ACM == engine)
			{
				synchronized (EngineSearchACM.references)
				{
					EngineSearchACM.references.add(new ReferenceSearch(this.url, idDoc, TypeEngine.ACM));
					ok = true;
				}
			}
			else if(TypeEngine.IEEE == engine)
			{
				synchronized (EngineSearchIEEE.references)
				{
					EngineSearchIEEE.references.add(new ReferenceSearch(this.url, idDoc, TypeEngine.IEEE));
					ok = true;
				}
			}
			else if(TypeEngine.SCIENCE == engine)
			{

			}
			else if(TypeEngine.SPRINGER == engine)
			{

			}
			
			if(ok)
			{
				EngineSearch.totalEncontrados++;
			}
		}
		
		return ok;
	}
	
	public static void insertText(String txt) throws IOException
	{
		File file = new File("C:/prueba.txt");
		if(file.exists())
		{
			if(file.delete()) {}
		}
		BufferedWriter out = null;   
		try {   
		    out = new BufferedWriter(new FileWriter(file));
		    out.write(txt);
		} catch (IOException e) {   
		    e.getStackTrace();
		} finally {   
		    if (out != null) {   
		        out.close();   
		    }   
		}
	}
	
	@Override
	public void run() {
		
		//System.out.println("Soy " + this.name);
		boolean ok = false;
		
		try
		{
			ok = processDownload();
		}
		catch (Exception ex) 
		{ 
			//System.out.println("Excepcion encontrada."); 
			setWebClient(); // Reiniciamos el WebClient.
		}
		
		if(ok)
		{
			if(TypeEngine.ACM == engine)
			{
				EngineSearchACM.contMax++;
			}
			
			if(TypeEngine.IEEE == engine)
			{
				EngineSearchIEEE.contMax++;
			}
			
			if(TypeEngine.SCIENCE == engine)
			{
				//EngineSearchSCIENCE.contMax++;
			}

			if(TypeEngine.SPRINGER == engine)
			{
				//EngineSearchSPRINGER.contMax++;
			}
			
			//System.out.println(this.name + " ha finalizado.");
		}
		else
		{
			//System.out.println(this.name + " NO ha finalizado.");
		}
		
		// Decrementamos el contador de hilos
		if(TypeEngine.ACM == engine)
		{
			EngineSearchACM.contHilos--;
		}
		if(TypeEngine.IEEE == engine)
		{
			EngineSearchIEEE.contHilos--;
		}
		if(TypeEngine.SCIENCE == engine)
		{
			//EngineSearchScience.contHilos--;
		}
		if(TypeEngine.SPRINGER == engine)
		{
			//EngineSearchSpringer.contHilos--;
		}
	}
}
