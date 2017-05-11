package background.pfc.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.util.URIUtil;

import background.pfc.commons.Reference;
import background.pfc.commons.ReferenceToImport;
import background.pfc.commons.SearchTermParam;
import background.pfc.enums.TypeEngineSearch;
import background.pfc.enums.TypeReferenceId;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * EngineSearchACM es una clase que hereda de EngineSearch para representar el motor de busqueda
 * para las referencias de la pagina ACM.
 * 
 * @author agonzatoro
 *
 */
public class EngineSearchACM extends EngineSearch {
	
	/**
	 * Constructor de la clase EngineSearchACM.
	 * 
	 * @param clientId String
	 * @param clientSecret String
	 * @param redirectUri String
	 * @param mendeleyService MendeleyService
	 * @param nameSLR String
	 * @param tammax int
	 * @param tags List<String>
	 * @param start_year int
	 * @param end_year int
	 * @param searchsTerms List<SearchTermParam>
	 * @param apiKeysEngine Map<TypeEngineSearch,String>
	 * @param webClients List<WebClient>
	 * @param total_hilos int
	 * @param total_tries int
	 * @throws Exception Exception
	 */
	public EngineSearchACM(String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year, int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			int total_hilos, int total_tries, String guidStaticData) throws Exception {
		
		super(TypeEngineSearch.ACM, clientId, clientSecret, redirectUri, mendeleyService, nameSLR, tammax, tags, 
				start_year, end_year, searchsTerms, apiKeysEngine, total_hilos, total_tries, guidStaticData);
		
		this.web = "http://dl.acm.org/results.cfm?query=@@query@@&within=owners.owner=HOSTED&filtered=@@filtered@@&start=@@start@@";
		this.idEngine = getIdSubFolder();
		//this.TAM_DEF = 10;
		this.TAM_DEF = 100;
	}
	
	/**
	 * M�todo que obtiene las url/doi de las referencias.
	 * 
	 */
	@Override
	public void getIdsReferences() throws ElementNotFoundException, IOException
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		double tamDefDouble = Double.parseDouble(Integer.toString(TAM_DEF));
		//int num_paginas = (int) Math.ceil(TAM_MAX/TAM_DEF);
		int num_paginas = (int) Math.ceil(TAM_MAX/tamDefDouble);
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
		
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
		String q = createQueryACM(searchsTerms);
		//String query = URIUtil.encodeQuery(q);
		String query = URIUtil.encodePath(q);
		query = query.replaceAll("\\+", "%252B");
		String filtered = "&dte=" + start_year + "&bfr=" + end_year;
		web = web.replaceAll("@@query@@", query).replaceAll("@@filtered@@", filtered).replaceAll("@@start@@","0");
		
		System.out.println("Conectando a " + web);
		
		HtmlPage currentPage = webClient.getPage(web);
		webClient.waitForBackgroundJavaScriptStartingBefore(10000);
		
		//Obtenemos los enlaces de cada uno de las bibliografias encontradas
		idsDocuments.addAll(getReferencesToImport(currentPage));
		
		webClient.close();

		System.out.println("Se ha obtenido " + idsDocuments.size());		
	}
	
	/**
	 * M�todo que construye la query para obtener los enlaces/dois de las referencias a importar.
	 * 
	 * @param searchsTerms List<SearchTermParam>
	 * @return String
	 */
	private String createQueryACM(List<SearchTermParam> searchsTerms)
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
	
	/**
	 * M�todo privado que obtiene el id de la carpeta del engine procedente de Mendeley.
	 * 
	 * @return String
	 * @throws Exception Exception
	 */
	private String getIdSubFolder() throws Exception
	{
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
	
	private List<ReferenceToImport> getReferencesToImport(HtmlPage currentPage) throws IOException
	{		
		List<ReferenceToImport> referencesToImport = new ArrayList<ReferenceToImport>();
		
		HtmlAnchor anchorBibTex = (HtmlAnchor) currentPage.getAnchorByText("bibtex");
		
		if (anchorBibTex != null)
		{
			InputStream is = anchorBibTex.click().getWebResponse().getContentAsStream();
			
			String bibtex = getStringFromInputStream(is);
			
			bibtex = bibtex.replaceAll("[\n\r]", "").replaceAll(" +", " ").replaceAll("booktitle", "book").trim();
			String regex = "@";
			String replacement = "\n" + regex;
			bibtex = bibtex.replaceAll(regex, replacement);
			referencesToImport = getAllReferences(bibtex);
		}
		
		return referencesToImport;
	}
	
	private String getStringFromInputStream(InputStream is)
	{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		String line;
		
		try
		{
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		catch(IOException e) { }
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}
	
	private List<ReferenceToImport> getAllReferences(String bibtex)
	{
		List<ReferenceToImport> references = new ArrayList<ReferenceToImport>();
		
		String[] bibRefs = bibtex.split("\n");
		int cont = 0;
		
		for(String b : bibRefs)
		{
			if (cont > TAM_DEF)
			{
				break;
			}
			else
			{
				String title = getByField("title",b);
				TypeReferenceId typeReferenceId = TypeReferenceId.DOI;
				
				if (title != null && !title.equals(""))
				{
					// Primero buscamos 'doi'
					String code = getByField("doi",b);
					
					if(code.equals(""))
					{
						code = getByField("isbn",b); // Por isbn
						typeReferenceId = TypeReferenceId.ISBN;
						
						if (code.equals(""))
						{
							code = getByField("issn", b);
							typeReferenceId = TypeReferenceId.ISSN;
							
							if (code.equals(""))
							{
								code = getByField("pmid",b);
								typeReferenceId = TypeReferenceId.PMID;
								
								if (code.equals(""))
								{
									code = getByField("arxiv",b);
									typeReferenceId = TypeReferenceId.ARXIV;
								}
							}
						}
					}
					
					if(!code.equals(""))
					{
						references.add(new ReferenceToImport(title, typeReferenceId, code));
						cont++;
					}
				}
			}
		}
		
		return references;
	}
	
	private String getByField(String field, String bibtex)
	{
		String value = "";
		
		try
		{
			String[] array = bibtex.split(field + " =");
	
			if (array.length == 2)
			{
				String textSource = array[1].trim();
				String pattern = "\\{(.+?)\\}.*";
				Matcher matcher = Pattern.compile(pattern).matcher(textSource);
				
				matcher.find();
				
				if (matcher.group(1) != null)
				{
					value = matcher.group(1);
				}
			}
		}
		catch(Exception ex) { }
		
		return value;
	}
}
