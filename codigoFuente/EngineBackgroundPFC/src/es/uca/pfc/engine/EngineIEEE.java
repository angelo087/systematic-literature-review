package es.uca.pfc.engine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import es.uca.pfc.main.ParallelSearch;
import es.uca.pfc.model.Reference;

public class EngineIEEE {

	private ArrayList<String> idsDocuments = new ArrayList<String>();
	private ArrayList<String> linksDocuments = new ArrayList<String>();
	private ArrayList<String> titlesDocuments = new ArrayList<String>();
	private ArrayList<String> publisherDocuments = new ArrayList<String>();
	private ArrayList<String> yearDocuments = new ArrayList<String>();
	private ArrayList<String> keysDocuments = new ArrayList<String>();
	private ArrayList<String> abstractDocuments = new ArrayList<String>();
	
	// Listado de referencias
	public static List<Reference> references = new ArrayList<Reference>();
	
	private MendeleyService mendeleyService = null;
	
	private double TAM_MAX          = 0;
	private double TAM_DEF			= 0;
	private String web              = "";
	private String nameSLR   		= "";
	private String terminos  		= "";
	private String tags				= "";
	private String operator			= "";
	private int start_year			= 0;
	private int end_year			= 0;
	private String componente 		= "";
	private String idEngine			= "";
	public static int contMax	= 0;
	public static int contHilos = 0;
	
	/**
	 * Constructor EngineIEEE
	 * 
	 * @param mendeleyService
	 * @param name
	 * @param terms
	 * @param tammax
	 * @param etiquetas
	 * @param operator
	 * @param start_year
	 * @param end_year
	 * @param componente
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws MendeleyException 
	 */
	public EngineIEEE(MendeleyService mendeleyService, String name, String terms, int tammax, List<String> etiquetas, String operator, int start_year, int end_year, String componente) throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException
	{
		this.web = "http://dl.acm.org/";
		this.mendeleyService = mendeleyService;
		nameSLR   = name;
		terminos  = terms;
		idEngine = getIdSubFolder();
		idsDocuments = new ArrayList<String>();
		TAM_MAX = tammax;
		TAM_DEF = 20;
		tags = "";
		for(String t : etiquetas)
		{
			tags += t+";";
		}
		
		this.operator = operator;
		
		if(start_year <= end_year)
		{
			this.start_year = start_year;
			this.end_year = end_year;
		}
		else
		{
			this.start_year = end_year;
			this.end_year = start_year;
		}
		
		this.componente = componente;
	}
	
	/**
	 * Método que realiza las consultas
	 * 
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws MendeleyException 
	 */
	public void startSearchs() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException, MendeleyException
	{
		// Obtenemos los Url
		getLinksReferences();
		
		// Descargamos la informaci�n de las referencias en Mendeley
		downloadReferencesToMendeley();
		
		// Mover a carpeta correspondiente
		moveToFolder();
	}
	
	/**
	 * Método privado que mueve las referencias a su carpeta correspondiente.
	 * 
	 * @throws MendeleyException 
	 */
	private void moveToFolder() throws MendeleyException
	{
		DocumentService documentservice = new DocumentService(mendeleyService);
		FolderService folderservice = new FolderService(mendeleyService);
		
		for(Reference reference : references)
		{
			try
			{
				folderservice.addDocument(folderservice.getFolderById(idEngine), documentservice.getDocument(reference.getIdMendeley()));
			}
			catch(Exception ex) {}
		}
	}
	
	/**
	 * Método privado que descarga las referencias y las importa en Mendeley
	 * 
	 * @throws InterruptedException
	 */
	private void downloadReferencesToMendeley() throws InterruptedException
	{
		ArrayList<String> linksBib = linksDocuments;
		int index    = 0;
		
		// Creamos 5 hilos para b�squedas paralelas
		Thread t1 = new Thread(), t2 = new Thread(), t3 = new Thread(), t4 = new Thread(), t5 = new Thread();
		ParallelSearch search01 = null, search02 = null, search03 = null, search04 = null, search05 = null;
		
		synchronized (linksBib) 
		{
			while(EngineIEEE.contMax < TAM_MAX && index < linksBib.size())
			{
				if(EngineIEEE.references.size() + EngineIEEE.contHilos < TAM_MAX)
				{
					
					if(t1 == null || !t1.isAlive())
					{
						if(search01 == null)
						{
							search01 = new ParallelSearch("IEEE-Hilo1","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,"IEEE", mendeleyService);
						}
						else
						{
							search01.setUrl(linksBib.get(index));
						}
						t1 = new Thread(search01);
						index++;
						EngineIEEE.contHilos++;
						t1.start();
					}
					else if(t2 == null || !t2.isAlive())
					{
						if(search02 == null)
						{
							search02 = new ParallelSearch("IEEE-Hilo2","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,"IEEE", mendeleyService);
						}
						else
						{
							search02.setUrl(linksBib.get(index));
						}
						t2 = new Thread(search02);
						index++;
						EngineIEEE.contHilos++;
						t2.start();
					}
					else if(t3 == null || !t3.isAlive())
					{
						if(search03 == null)
						{
							search03 = new ParallelSearch("IEEE-Hilo3","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,"IEEE", mendeleyService);
						}
						else
						{
							search03.setUrl(linksBib.get(index));
						}
						t3 = new Thread(search03);
						index++;
						EngineIEEE.contHilos++;
						t3.start();
					}
					else if(t4 == null || !t4.isAlive())
					{
						if(search04 == null)
						{
							search04 = new ParallelSearch("IEEE-Hilo4","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,"IEEE", mendeleyService);
						}
						else
						{
							search04.setUrl(linksBib.get(index));
						}
						t4 = new Thread(search04);
						index++;
						EngineIEEE.contHilos++;
						t4.start();
					}
					else if(t5 == null || !t5.isAlive())
					{
						if(search05 == null)
						{
							search05 = new ParallelSearch("IEEE-Hilo5","ieee"+UUID.randomUUID().toString(),linksBib.get(index),tags,"IEEE", mendeleyService);
						}
						else
						{
							search05.setUrl(linksBib.get(index));
						}
						t5 = new Thread(search05);
						index++;
						EngineIEEE.contHilos++;
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
	
	/**
	 * Método que devuelve los identificadores de los documentos en Mendeley
	 * 
	 * @return
	 */
	public ArrayList<String> getIdsDocuments() { return idsDocuments; }
	
	/**
	 * Método que obtiene las url's donde se encuentran las referencias
	 * 
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void getLinksReferences() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		int num_paginas = (int) Math.ceil(TAM_MAX/TAM_DEF);
		
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
		
		//Accedemos al formulario de busqueda de IEEE
		HtmlPage currentPage = webClient.getPage(web);
		
		// Hacemos click sobre el enlace de b�squedas
		
		HtmlAnchor anchorFind = currentPage.getAnchorByText("Advanced Search");
		
		currentPage = (HtmlPage) anchorFind.click();
		
		// Buscamos el formulario e introducimos todos los par�metros de b�squeda
		HtmlForm form = currentPage.getForms().get(0);
				
		if(operator.equals("all"))
			form.getInputByName("allofem").setValueAttribute(terminos);
		else if(operator.equals("any"))
			form.getInputByName("anyofem").setValueAttribute(terminos);
		else //none
			form.getInputByName("noneofem").setValueAttribute(terminos);
		
		HtmlSelect select_start_year = (HtmlSelect) form.getSelectByName("since_year");
		HtmlSelect select_end_year = (HtmlSelect) form.getSelectByName("before_year");
		
		HtmlOption optionMinYear = (HtmlOption) select_start_year.getOption(select_start_year.getOptionSize()-1);
		HtmlOption optionMaxYear = (HtmlOption) select_start_year.getOption(1);
		
		int minYear = Integer.parseInt(optionMinYear.getText());
		int maxYear = Integer.parseInt(optionMaxYear.getText());
		
		if(start_year < minYear)
			start_year = minYear;
		
		if(end_year > maxYear)
			end_year = maxYear;
		
		select_start_year.setSelectedAttribute(select_start_year.getOptionByText(Integer.toString(start_year)), true);
		select_end_year.setSelectedAttribute(select_end_year.getOptionByText(Integer.toString(end_year)), true);
				
		if(componente.equals("full-text") || componente.equals("abstract") || componente.equals("review") || componente.equals("title") || componente.equals("author"))
		{
			if(componente.equals("review"))
			{
				form.getInputByName("hasrev").setChecked(true);
			}
			else if(componente.equals("abstract"))
			{
				form.getInputByName("hasabs").setChecked(true);
			}
			else //full-text or other
			{
				form.getInputByName("hasft").setChecked(true);
			}
		}
		
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
			currentPage = nextPage(webClient,currentPage);
		}
		
		webClient.closeAllWindows();
		
		linksDocuments.addAll(linksBib);
		
		webClient.closeAllWindows();
	}
	
	/**
	 * Método privado que obtiene una nueva página con enlaces disponibles para descargar
	 * 
	 * @param webClient
	 * @param currentPage
	 * @return
	 * @throws ElementNotFoundException
	 * @throws IOException
	 */
	private HtmlPage nextPage(WebClient webClient, HtmlPage currentPage) throws ElementNotFoundException, IOException
	{
		boolean encontrado = true;
		String label = "next";
		HtmlPage nextPage = currentPage;
		
		try
		{
			HtmlAnchor next = currentPage.getAnchorByText(label);
		}
		catch(ElementNotFoundException ex) { encontrado = false; }
		
		if(encontrado == true)
		{
			nextPage = currentPage.getAnchorByText(label).click();
			webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		}
		else
			nextPage = null;
		
		return nextPage;
	}
	
	/**
	 * Método que obtiene los enlaces de cada uno de los metadatos en IEEE
	 * 
	 * @param code
	 * @return
	 */
	private ArrayList<String> getLinksBib(String code)
	{
		ArrayList<String> bibs = new ArrayList<String>();
		
		String textSource = code;
		
		textSource = textSource.replaceAll("[\n\r]", "");
		textSource = textSource.replaceAll(" +", " ");
		textSource = textSource.replaceAll("<i>", "");
		textSource = textSource.replaceAll("</i>", "");
		
		String regex = "<tbody align=\"left\"> <tr> <td colspan=\"3\">";
		String replacement = "\n" + regex;
		textSource = textSource.replaceAll(regex, replacement);
		
		String pattern = "<tbody align=\"left\"> <tr> <td colspan=\"3\"> <a href=\"(.+?)\".*>(.*)</a>.*<div class=\"authors\">(.+?)</div>.*<td class=\"small\\-text\" nowrap=\"\">(.+?)</td>.*<div class=\"addinfo\">(.+?)</div> .* <div class=\"abstract2\"> <br/> (.+?) </div> </td> </tr> </tbody> </table> </td>.*";

		Matcher matcher = Pattern.compile(pattern).matcher(textSource);
		
		while(matcher.find())
		{
			bibs.add(("http://dl.acm.org/" + matcher.group(1)).trim()); //enlaces
			titlesDocuments.add(matcher.group(2).trim()); //titulos
			yearDocuments.add(matcher.group(4).trim().split(" ")[1].trim()); //A�os - Ex: July 2010 --> obtenemos el 2010
			//publishers
			if(!matcher.group(5).trim().contains("<strong>"))
				publisherDocuments.add(matcher.group(5).trim());
			else
			{
				String value = matcher.group(5).trim().split("</strong>")[0].replaceAll("<strong>", "").trim();
				
				if(value.charAt(value.length()-1) == ':')
					value = value.substring(0, value.length()-1);

				publisherDocuments.add(value);
			}
			//keywords
			if(!matcher.group(6).trim().contains("<b> Keywords </b> :"))
				keysDocuments.add("");
			else
				keysDocuments.add(matcher.group(6).trim().split("<b> Keywords </b> :")[1].trim());
		}
		
		return bibs;
	}
	
	/**
	 * Método que obtiene el Identificador de la Carpeta de IEEE en el SLR del usuario
	 * 
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws MendeleyException 
	 */
	private String getIdSubFolder() throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException
	{
		FolderService folderservice = new FolderService(mendeleyService);
		
		//List<Folder> folders = folderservice.getSubfolders(folderservice.getFolderByName(nameSLR));
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
}
