package es.uca.pfc.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.services.AnnotationService;
import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import es.uca.pfc.engine.EngineACM;
import es.uca.pfc.engine.EngineIEEE;
import es.uca.pfc.engine.EngineScience;
import es.uca.pfc.model.Reference;

public class ParallelSearch implements Runnable {
	
	private String name;
	private String engine;
	private WebClient webClient;
	private String url;
	private String notesCont;
	private String tags;
	private MendeleyService mendeleyService = null;
	
	public ParallelSearch(String name, String notesCont, String url, String tags, String engine, MendeleyService mendeleyService)
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
	
	public void setUrl(String url)
	{
		this.url = "http://www.mendeley.com/import/?url=" + url;
	}
	
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
		
		// Si todo ok, incrementamos el contador en el engine correspondiente.
		if(ok)
		{
			if(this.engine.equals("ACM"))
			{
				EngineACM.contMax++;
			}
			
			if(this.engine.equals("IEEE"))
			{
				EngineIEEE.contMax++;
			}
			
			if(this.engine.equals("SCIENCE"))
			{
				EngineScience.contMax++;
			}
			// ...
			
			System.out.println(this.name + " ha finalizado.");
		}
		else
		{
			System.out.println(this.name + " NO ha finalizado.");
		}
		
		// Decrementamos el contador de hilos
		if(this.engine.equals("ACM"))
		{
			EngineACM.contHilos--;
		}
		if(this.engine.equals("IEEE"))
		{
			EngineIEEE.contHilos--;
		}
		if(this.engine.equals("SCIENCE"))
		{
			EngineScience.contHilos--;
		}
		// ...
	}
	
	private boolean processDownload() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		boolean ok = false;
		
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		String cad = ""; //cadena auxiliar
		
		HtmlPage currentPage = webClient.getPage(this.url);
		this.webClient.waitForBackgroundJavaScript(5000);
		
		int codePage = currentPage.getWebResponse().getStatusCode();
		
		if(codePage == 200)
		{
			cad = currentPage.asXml();
			
			if(cad.contains("Sign in to Mendeley")) //Hay que realizar el Login en Mendeley
			{
				System.out.println(this.name + " tiene que hacer login.");
				HtmlForm form = currentPage.getForms().get(0);
				form.getInputByName("email").setValueAttribute(this.mendeleyService.getEmailMend());
				form.getInputByName("password").setValueAttribute(this.mendeleyService.getPassMend());
			
				currentPage = form.getInputByValue("Sign in").click();
				Thread.sleep(5000);
			}
			
			cad = currentPage.asXml();
			
			HtmlTextArea notes = (HtmlTextArea) currentPage.getElementById("import-notes");
			notes.setText(this.notesCont);
			
			HtmlInput importTags = (HtmlInput) currentPage.getElementById("import-tags");
			importTags.setValueAttribute(this.tags);
			
			HtmlAnchor anchor = currentPage.getAnchorByText("Save");
			
			currentPage = anchor.click();
			this.webClient.waitForBackgroundJavaScript(10000);
			
			String idDoc = getIdDoc();
			
			if(this.engine.equals("ACM"))
			{
				synchronized (EngineACM.references)
				{
					EngineACM.references.add(new Reference(this.url, idDoc));
					ok = true;
				}
			}
			else if(this.engine.equals("IEEE"))
			{
				synchronized (EngineIEEE.references)
				{
					EngineIEEE.references.add(new Reference(this.url, idDoc));
					ok = true;
				}
			}
			else if(this.engine.equals("SCIENCE"))
			{
				synchronized (EngineScience.references)
				{
					EngineScience.references.add(new Reference(this.url, idDoc));
					ok = true;
				}
			}
		}
		
		return ok;
	}
	
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
}
