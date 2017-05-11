package background.pfc.engine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.CatalogService;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

import org.apache.http.HttpException;

import background.pfc.commons.ImportReferenceMendeley;
import background.pfc.commons.PoolReferences;
import background.pfc.commons.Reference;
import background.pfc.commons.ReferenceToImport;
import background.pfc.commons.SearchTermParam;
import background.pfc.enums.TypeEngineSearch;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public abstract class EngineSearch implements Runnable {
	
	/** TOTAL_TRIES.*/
	public static int TOTAL_TRIES = 5;
	
	/** TypeEngineSearch.*/
	protected TypeEngineSearch engine;
	/** clientId.*/
	protected String clientId;
	/** clientSecret.*/
	protected String clientSecret;
	/** redirectUri.*/
	protected String redirectUri;
	/** mendeleyService.*/
	protected MendeleyService mendeleyService;
	/** total_hilos.*/
	protected int total_hilos;
	/** nameSLR.*/
	protected String nameSLR;
	/** tags.*/
	protected List<String> tags;
	/** strTags.*/
	protected String strTags = "";
	/** start_year.*/
	protected int start_year;
	/** end_year.*/
	protected int end_year;
	/** searchsTerms.*/
	protected List<SearchTermParam> searchsTerms;
	/** TAM_MAX.*/
	protected int TAM_MAX          = 0;
	/** TAM_DEF.*/
	protected int TAM_DEF			= 0;
	/** web.*/
	protected String web              = "";
	/** idEngine.*/
	protected String idEngine			= "";
	/** linksDocuments.*/
	protected List<ReferenceToImport> idsDocuments = new ArrayList<ReferenceToImport>();
	/** apiKeysEngine.*/
	protected Map<TypeEngineSearch,String> apiKeysEngine = new HashMap<TypeEngineSearch, String>();
	
	private String guidStaticData = "";
	
	/**
	 * M�todo de la clase EngineSearch.
	 * 
	 * @param engine TypeEngineSearch
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
	public EngineSearch(TypeEngineSearch engine,
			String clientId, String clientSecret, String redirectUri, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year, int end_year, 
			List<SearchTermParam> searchsTerms, Map<TypeEngineSearch,String> apiKeysEngine,
			int total_hilos, int total_tries, String guidStaticData) throws Exception {
		
		this.engine = engine;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.mendeleyService = mendeleyService;
		this.nameSLR = nameSLR;
		this.TAM_MAX= tammax;
		this.tags = tags;
		this.strTags = "";
		this.apiKeysEngine = apiKeysEngine;
		this.total_hilos = total_hilos;
		this.guidStaticData = guidStaticData;
		TOTAL_TRIES = total_tries;
		
		for(String t : tags)
		{
			this.strTags += t+";";
		}
		this.searchsTerms = searchsTerms;
		
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
		
		resetParamsOfEngineSearch();
	}
	
	@Override
	/**
	 * Método que realizar el proceso de búsqueda y descarga de las referencias a través
	 * de la interfaz Runnable.
	 */
	public void run() {
		
		try
		{
			startSearch();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * M�todo que realiza la b�squeda y descarga de las referencias.
	 * @throws Exception
	 */
	public void startSearch() throws Exception {	
		System.out.println(engine.getKey().toUpperCase() + " Paso 1 de 4 => GetLinksReferences()");
		getIdsReferences();
		
		System.out.println(engine.getKey().toUpperCase() + " Paso 2 de 4 => downloadReferencesToMendeley()");
		downloadReferencesToMendeley();
		
		System.out.println(engine.getKey().toUpperCase() + " Paso 3 de 4 => getMendeleyFolderEngine()");
		getMendeleyFolderEngine();
		
		System.out.println(engine.getKey().toUpperCase() + " Paso 4 de 4 => moveReferencesToFolder()");
		moveReferencesToFolder();
	}
	
	/**
	 * Método que obtiene los enlaces de las referencias. Es implementado
	 * por las clases que heredan de EngineSearch.
	 * 
	 * @throws Exception
	 */
	public abstract void getIdsReferences() throws Exception;
	
	/**
	 * Método que realiza la descarga de las referencias a través de los enlaces
	 * links obtenidos en un paso previo.
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public void downloadReferencesToMendeley() throws InterruptedException, FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		//Para evitar que salga el texto por la salida estandar
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		
		try
		{
			List<Thread> threads = inicialiceArrayThreads();
			List<ImportReferenceMendeley> importsRefMendeley = inicialiceArrayImports();
			
			DocumentService documentService = new DocumentService(mendeleyService);
			CatalogService catalogService = new CatalogService(mendeleyService);
			
			System.out.println("Login realizado correctamente.");
			int index = 0;
			boolean exit = false;
						
			synchronized (idsDocuments) {
				
				// Mientras que haya referencias a importar y no sobrepase el tamaño fijado
				while(!exit)
				{
					while(PoolReferences.getStaticData(guidStaticData).getContMax() < TAM_MAX && index < idsDocuments.size() && (PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch().size() + PoolReferences.getStaticData(guidStaticData).getContHilos() <= TAM_MAX))
					{						
						int posThread = getDisponibleThread(threads);
						if(posThread != -1)
						{
							Thread thread = threads.get(posThread);
							ImportReferenceMendeley importReference = importsRefMendeley.get(posThread);
							if (importReference == null)
							{
								importReference = new ImportReferenceMendeley(engine.getKey()+"-Hilo-"+(posThread+1), 
										engine, idsDocuments.get(index).getId(), nameSLR, idsDocuments.get(index).getTypeReferenceId(),
										catalogService, documentService, guidStaticData);
							}
							else
							{
								importReference.setCodeReference(idsDocuments.get(index).getId());
								importReference.setTypeReferenceId(idsDocuments.get(index).getTypeReferenceId());
							}
							importsRefMendeley.set(posThread, importReference);
							thread = new Thread(importReference);
							threads.set(posThread, thread);
							index++;
							increaseContHilos();
							thread.start();
						}// fin-if posThread
					}// fin - while
					
					// Esperamos a que finalicen los hilos
					while(!isFinishedThreads(threads))
					{
						// Esperamos a que finalicen los hilos
						System.out.println(engine.getKey() + " => Esperando a que finalicen hilos pendientes");
						Thread.sleep(5000);
					}
					
					if(!(index < idsDocuments.size()) || PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch().size() >= TAM_MAX)
					{
						exit = true;
					}
				} // fin-while
			} // fin-synchronized
		}
		catch(Exception e)
		{
			System.out.println("Exception in EngineSearch.downloadReferencesToMendeley()");
		}
	}
	
	/**
	 * M�todo privado que inicializa los importRefMendeleys.
	 * 
	 * @return List<ImportReferenceMendeley>
	 */
	private List<ImportReferenceMendeley> inicialiceArrayImports()
	{
		List<ImportReferenceMendeley> importRefMendeley = new ArrayList<ImportReferenceMendeley>();
		
		for(int i = 0; i < total_hilos; i++)
		{
			importRefMendeley.add(null);
		}
		
		return importRefMendeley;
	}
	
	/**
	 * Método privado que obtiene todas las referencias por motor de búsqueda
	 * y las almacena en una lista general.
	 * @throws Exception 
	 * 
	 */
	private void getMendeleyFolderEngine() throws Exception
	{
		if (this instanceof EngineSearchACM)
		{
			// ACM
			setIdMendeleyFolderEngine(PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch(), TypeEngineSearch.ACM);
		}
		
		if (this instanceof EngineSearchIEEE)
		{
			// IEEE
			setIdMendeleyFolderEngine(PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch(), TypeEngineSearch.IEEE);
		}
		
		if (this instanceof EngineSearchSCIENCE)
		{
			// SCIENCE
			setIdMendeleyFolderEngine(PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch(), TypeEngineSearch.SCIENCE);
		}
		
		if (this instanceof EngineSearchSPRINGER)
		{
			// SPRINGER
			setIdMendeleyFolderEngine(PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch(), TypeEngineSearch.SPRINGER);
		}
	}
	
	private void moveReferencesToFolder() throws MendeleyException, HttpException, IOException, InterruptedException
	{
		//List<Reference> references = getReferencesByEngineSearch();
		
		FolderService folderService = new FolderService(mendeleyService);
		
		for(Reference reference : PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch())
		{
			int intentos = 5;
			while(intentos != 0)
			{
				try
				{
					folderService.addDocument(reference.getIdFolderEngine(), reference.getIdMendeley());
					System.out.println(reference.getIdMendeley() + " ubicado correctamente.");
					intentos=0;
				}
				catch(Exception ex)
				{
					intentos--;
					System.out.println(reference.getIdMendeley() + " tiene que ubicarse de nuevo (" + intentos + " intentos )");
					System.out.println(reference.getIdMendeley() + " => " + ex.getMessage());
					Thread.sleep(5000);
				}
			}
		}
	}
	
	/**
	 * M�todo que obtiene el identificador de mendeley de la carpeta (engine).
	 * 
	 * @param typeEngineSearch TypeEngineSearch
	 * @return String
	 * @throws Exception Exception
	 */
	private String getIdMendeleyFolderEngine(TypeEngineSearch typeEngineSearch) throws Exception
	{
		String nameEngine = typeEngineSearch.getKey();
		
		FolderService folderService = new FolderService(mendeleyService);
		Folder folderSLR = folderService.getFolderByName(nameSLR);
		Folder folderEngine = null;
		
		if (folderSLR != null)
		{
			folderEngine = folderService.getSubFolder(folderSLR.getId(), nameEngine);
		}
		
		return (folderEngine == null ? "" : folderEngine.getId());
	}
	
	/**
	 * M�todo que establece el identificador de la carpeta (engine).
	 * 
	 * @param references List<Reference>
	 * @param typeEngineSearch TypeEngineSearch
	 * @throws Exception Exception
	 */
	private void setIdMendeleyFolderEngine(List<Reference> references, TypeEngineSearch typeEngineSearch) throws Exception
	{
		String idMendeleyFolder = getIdMendeleyFolderEngine(typeEngineSearch);
		
		for(Reference reference : references)
		{
			reference.setIdFolderEngine(idMendeleyFolder);
		}
	}
	
	/**
	 * M�todo que establece una referencia a la lista de referencias que tiene cada 
	 * uno de los motores de busquedas disponibles.
	 * 
	 * @param reference Reference
	 * @return boolean.
	 */
	public static boolean addReferenceToEngineSearch(Reference reference)
	{
		boolean ok = false;
		
		try
		{
			/*synchronized (PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch()) {
				PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch().add(reference);
				ok = true;
			}*/
		}
		catch(Exception ex) { ok = false; }
		
		return ok;
	}
	
	private void resetParamsOfEngineSearch()
	{
		/*EngineSearch.referencesEngineSearch = new ArrayList<Reference>();
		EngineSearch.contMax	= 0;
		EngineSearch.contHilos = 0;*/
		PoolReferences.getStaticData(guidStaticData).setReferencesEngineSearch(new ArrayList<Reference>());
		PoolReferences.getStaticData(guidStaticData).setContHilos(0);
		PoolReferences.getStaticData(guidStaticData).setContMax(0);
	}
	
	/**
	 * Método privado que incrementa el contador de hilos.
	 * 
	 */
	private void increaseContHilos()
	{
		if (PoolReferences.getStaticData(guidStaticData).getContHilos() < total_hilos)
		{
			PoolReferences.increaseContHilos(guidStaticData);
		}
	}
	
	/**
	 * Método estático que decrementa el contador de hilos.
	 * 
	 * @param typeEngine TypeEngineSearch
	 */
	/*public static void decreaseContHilos(TypeEngineSearch typeEngine)
	{
		if (PoolReferences.getStaticData(guidStaticData).getContHilos() != 0)
		{
			PoolReferences.decreaseContHilos(guidStaticData);
		}
	}*/
	
	/**
	 * Método que incrementa el contador de referencias.
	 * 
	 * @param typeEngine Motor de búsqueda
	 */
	/*public static void increaseContMax(TypeEngineSearch typeEngine)
	{
		PoolReferences.increaseContMax(guidStaticData);
	}*/
	
	/**
	 * Método privado que obtiene el contador de hilos según el motor
	 * de búsqueda.
	 * 
	 * @return Número de hilos que el motor de búsqueda está usando en ese momento.
	 */
	private int getContHilos() {
		return PoolReferences.getStaticData(guidStaticData).getContHilos();
	}

	/**
	 * Método que obtiene el número de referencias obtenidas hasta ese momento.
	 * 
	 * @return Número de referencias obtenidas hasta ese momento.
	 */
	private int getContMax() 
	{
		return PoolReferences.getStaticData(guidStaticData).getContMax();
	}

	/**
	 * Método que devuelve todas las referencias obtenidas.
	 * 
	 * @return List<Reference>
	 */
	private List<Reference> getReferences()
	{
		return PoolReferences.getStaticData(guidStaticData).getReferencesEngineSearch();
	}
	
	/**
	 * Método que comprueba si todos los hilos han finalizado su
	 * ejecución.
	 * 
	 * @param threads List<Thread>
	 * @return Verdadero o Falso dependiendo si todos los hilos han
	 * 		   finalizado su ejecución o no.
	 */
	private boolean isFinishedThreads(List<Thread> threads)
	{
		boolean isFinished = true;
		
		for(Thread t : threads)
		{
			if (t != null && t.isAlive())
			{
				isFinished = false;
				break;
			}
		}
		
		return isFinished;
	}
	
	/**
	 * Método privado que que inicializa el vector
	 * de hilos con valores nulos.
	 * 
	 * @return Vector de hilos inicializado.
	 */
	private List<Thread> inicialiceArrayThreads() {
		
		List<Thread> threads = new ArrayList<Thread>();
		
		for(int i=0; i < total_hilos; i++)
		{
			threads.add(null);
		}
		
		return threads;
	}

	/**
	 * Método privado que obtiene la posición del vector de hilos de 
	 * aquel hilo que se encuentre disponible para ejecutarse.
	 * 
	 * @param threads List<Thread>
	 * @return Posición del vector donde se encuentra un hilo para ejecutarse.
	 * 		   En caso de que no haya ninguno, devuelve -1.
	 */
	private int getDisponibleThread(List<Thread> threads)
	{
		int pos = -1;
		for(int i=0; i<threads.size(); i++)
		{
			if (threads.get(i) == null || !threads.get(i).isAlive())
			{
				pos = i;
				break;
			}
		}
		
		return pos;
	}

	/**
	 * M�todo que devuelve el TypeEngineSearch asociado al Engine.
	 * 
	 * @return TypeEngineSearch
	 */
	public TypeEngineSearch getEngine() {
		return engine;
	}

	/**
	 * M�todo que establece el TypeEngineSearch asociado al Engine.
	 * 
	 * @param engine TypeEngineSearch
	 */
	public void setEngine(TypeEngineSearch engine) {
		this.engine = engine;
	}

	/**
	 * M�todo que devuelve el nombre del SLR.
	 * 
	 * @return String
	 */
	public String getNameSLR() {
		return nameSLR;
	}

	/**
	 * M�todo que establece el nombre del SLR.
	 * 
	 * @param nameSLR String
	 */
	public void setNameSLR(String nameSLR) {
		this.nameSLR = nameSLR;
	}

	/**
	 * M�todo que devuelve los tags.
	 * 
	 * @return List<String>
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * M�todo que establece los tags.
	 * 
	 * @param tags List<String>
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * M�todo que devuelve el ano de comienzo.
	 * 
	 * @return int
	 */
	public int getStart_year() {
		return start_year;
	}

	/**
	 * M�todo que establece el ano de comienzo.
	 * 
	 * @param start_year int
	 */
	public void setStart_year(int start_year) {
		this.start_year = start_year;
	}

	/**
	 * M�todo que establece el ano final.
	 * 
	 * @return int
	 */
	public int getEnd_year() {
		return end_year;
	}

	/**
	 * M�todo que establece el ano final.
	 * 
	 * @param end_year int
	 */
	public void setEnd_year(int end_year) {
		this.end_year = end_year;
	}

	/**
	 * M�todo que devuelve los SearchTermParam asociados al Engine.
	 * 
	 * @return List<SearchTermParam>
	 */
	public List<SearchTermParam> getSearchsTerms() {
		return searchsTerms;
	}

	/**
	 * M�todo que establece los SearchTermParam asociados al Engine.
	 * 
	 * @param searchsTerms List<SearchTermParam>
	 */
	public void setSearchsTerms(List<SearchTermParam> searchsTerms) {
		this.searchsTerms = searchsTerms;
	}

	/**
	 * M�todo que devuelve el tamano maximo de referencias a buscar.
	 * 
	 * @return int
	 */
	public int getTAM_MAX() {
		return TAM_MAX;
	}

	/**
	 * M�todo que establece el tamano maximo de referencias a buscar.
	 * 
	 * @param tAM_MAX
	 */
	public void setTAM_MAX(int tAM_MAX) {
		TAM_MAX = tAM_MAX;
	}

	/**
	 * M�todo que establece el tamano definido por defecto de referencias a buscar.
	 * 
	 * @return int
	 */
	public int getTAM_DEF() {
		return TAM_DEF;
	}

	/**
	 * M�todo que establece el tamano definido por defecto de referencias a buscar.
	 * 
	 * @param tAM_DEF int
	 */
	public void setTAM_DEF(int tAM_DEF) {
		TAM_DEF = tAM_DEF;
	}

	/**
	 * M�todo que devuelve la url asociada al EngineSearch.
	 * 
	 * @return String
	 */
	public String getWeb() {
		return web;
	}

	/**
	 * M�todo que establece la url asociada al EngineSearch.
	 * 
	 * @param web String
	 */
	public void setWeb(String web) {
		this.web = web;
	}

	/**
	 * M�todo que devuelve el idEngine.
	 * 
	 * @return String
	 */
	public String getIdEngine() {
		return idEngine;
	}

	/**
	 * M�todo que establece el idEngine.
	 * 
	 * @param idEngine String
	 */
	public void setIdEngine(String idEngine) {
		this.idEngine = idEngine;
	}

	/**
	 * M�todo que devuelve el listado de los enlaces de las referencias.
	 * 
	 * @return List<String>
	 */
	public List<ReferenceToImport> getIdsDocuments() {
		return idsDocuments;
	}

	/**
	 * M�todo que establece el listado de los enlaces de las referencias.
	 * 
	 * @param linksDocuments List<String>
	 */
	public void setIdsDocuments(List<ReferenceToImport> idsDocuments) {
		this.idsDocuments = idsDocuments;
	}

	/**
	 * M�todo que obtiene el clientId.
	 * 
	 * @return String
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * M�todo que establece el clientId
	 * 
	 * @param clientId String
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * M�todo que obtiene el clientSecret
	 * 
	 * @return String
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * M�todo que establece el clientSecret.
	 * 
	 * @param clientSecret String
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * M�todo que obtiene la url de redireccion.
	 * 
	 * @return String
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * M�todo que establece la url de direccion.
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * M�todo que obtiene el servicio login de mendeley.
	 * 
	 * @return MendeleyService
	 */
	public MendeleyService getMendeleyService() {
		return mendeleyService;
	}
	
	/**
	 * M�todo que establece el servicio login de mendeley.
	 * 
	 * @param mendeleyService MendeleyService
	 */
	public void setMendeleyService(MendeleyService mendeleyService) {
		this.mendeleyService = mendeleyService;
	}

	public String getGuidStaticData() {
		return guidStaticData;
	}

	public void setGuidStaticData(String guidStaticData) {
		this.guidStaticData = guidStaticData;
	}
}
