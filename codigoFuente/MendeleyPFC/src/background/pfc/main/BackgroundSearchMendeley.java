package background.pfc.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;
import background.pfc.commons.Reference;
import background.pfc.commons.SearchTermParam;
import background.pfc.engine.EngineSearch;
import background.pfc.engine.EngineSearchACM;
import background.pfc.engine.EngineSearchIEEE;
import background.pfc.engine.EngineSearchSCIENCE;
import background.pfc.engine.EngineSearchSPRINGER;
import background.pfc.enums.TypeEngineSearch;

/**
 * BackgroundSearchMendeley es la clase encargada de realizar el proceso de búsqueda
 * en segundo plano de las referencias a importar a Mendeley.
 * 
 * @author angelo
 *
 */
public class BackgroundSearchMendeley {

	private MendeleyService mendeleyService = null;
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
	private List<Reference> references = new ArrayList<Reference>();
	
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
		
		FolderService folderService = new FolderService(mendeleyService);
		
		Folder folder = folderService.getFolderByName(nameSLR);
		
		if(folder == null)
		{
			throw new Exception("La carpeta no existe.");
		}
	}
	
	public void startSearchsWithThreads() throws Exception
	{
		System.out.println("COMIENZA PROCESO BUSQUEDA");

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

		Thread tACM = new Thread(), tIEEE = new Thread(), tSCIENCE = new Thread();
		
		if(optionsEngine.get(TypeEngineSearch.ACM))
		{
			tACM = new Thread(new EngineSearchACM(clientId, clientSecret, redirectUri, mendeleyService, 
					nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
					total_hilos, total_tries));
			tACM.start();
		}
		
		if(optionsEngine.get(TypeEngineSearch.IEEE))
		{
			tIEEE = new Thread(new EngineSearchIEEE(clientId, clientSecret, redirectUri, mendeleyService, 
					nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
					total_hilos, total_tries));
			tIEEE.start();
		}
		
		if(optionsEngine.get(TypeEngineSearch.SCIENCE))
		{
			tSCIENCE = new Thread(new EngineSearchSCIENCE(clientId, clientSecret, redirectUri, mendeleyService, 
					nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
					total_hilos, total_tries));
			tSCIENCE.start();
		}
		
		while(tACM.isAlive() || tIEEE.isAlive() || tSCIENCE.isAlive())
		{
			// Wait finish process
		}
		
		System.out.println("PROCESO BUSQUEDA FINALIZADO.");
	}
	
	public void startSearchs() throws Exception
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
				
		if(hasEnginesForSearch())
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
									nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
									total_hilos, total_tries);
							break;
						case IEEE:
							engineSearch = new EngineSearchIEEE(clientId, clientSecret, redirectUri, mendeleyService, 
									nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
									total_hilos, total_tries);
							break;
						case SCIENCE:
							engineSearch = new EngineSearchSCIENCE(clientId, clientSecret, redirectUri, mendeleyService, 
									nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
									total_hilos, total_tries);
							break;
						case SPRINGER:
							engineSearch = new EngineSearchSPRINGER(clientId, clientSecret, redirectUri, mendeleyService, 
									nameSLR, tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, 
									total_hilos, total_tries);
							break;
						default:
							break;
					}
					engineSearch.startSearch();
					references.addAll(EngineSearch.referencesEngineSearch);
				} // fin if
			} //fin for
			
			System.out.println("PROCESO BUSQUEDA FINALIZADO.");
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
	
	public int getTotalReferences()
	{
		return references.size();
	}
	
	public List<Reference> getReferences()
	{
		return references;
	}
}
