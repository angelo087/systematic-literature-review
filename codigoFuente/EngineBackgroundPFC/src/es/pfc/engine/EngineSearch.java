package es.pfc.engine;

import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;
import es.pfc.commons.DownloadReference;
import es.pfc.commons.Reference;
import es.pfc.commons.SearchTermParam;
import es.pfc.commons.TypeEngineSearch;

public abstract class EngineSearch implements Runnable {
	
	public final int TOTAL_HILOS = 5;
	
	protected TypeEngineSearch engine;
	
	//private MendeleyService mendeleyService = null;
	protected String clientId;
	protected String clientSecret;
	protected String redirectUri;
	protected String emailMend;
	protected String passMend;
	
	protected String nameSLR;
	protected List<String> tags;
	protected String strTags = "";
	protected int start_year;
	protected int end_year;
	protected List<SearchTermParam> searchsTerms;
	protected int TAM_MAX          = 0;
	protected int TAM_DEF			= 0;
	protected String web              = "";
	protected String idEngine			= "";
	protected List<String> linksDocuments = new ArrayList<String>();
	public static List<Reference> referencesEngineSearch = new ArrayList<Reference>();
	
	/**
	 * Constructor EngineSearch
	 * 
	 * @param engine
	 * @param mendeleyService
	 * @param nameSLR
	 * @param tammax
	 * @param tags
	 * @param start_year
	 * @param end_year
	 * @param searchsTerms
	 * @throws Exception 
	 */
	public EngineSearch(TypeEngineSearch engine, String clientId, String clientSecret, String redirectUri, String emailMend, String passMend,
			String nameSLR, int tammax, List<String> tags, int start_year,
			int end_year, List<SearchTermParam> searchsTerms) throws Exception {
		
		this.engine = engine;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
		this.emailMend = emailMend;
		this.passMend = passMend;
		//this.mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri, emailMend, passMend);
		this.nameSLR = nameSLR;
		this.TAM_MAX= tammax;
		this.tags = tags;
		this.strTags = "";
		
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
	}
	
	public EngineSearch(TypeEngineSearch engine, MendeleyService mendeleyService,
			String nameSLR, int tammax, List<String> tags, int start_year,
			int end_year, List<SearchTermParam> searchsTerms) throws Exception {
		
		this.engine = engine;
		//this.mendeleyService = mendeleyService;
		this.clientId = mendeleyService.getClientId();
		this.clientSecret = mendeleyService.getClientSecret();
		this.redirectUri = mendeleyService.getRedirectUri();
		this.emailMend = mendeleyService.getEmailMend();
		this.passMend = mendeleyService.getPassMend();
		this.nameSLR = nameSLR;
		this.TAM_MAX= tammax;
		this.tags = tags;
		this.strTags = "";
		
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
	}
	
	@Override
	/**
	 * Método que realizar el proceso de búsqueda y descarga de las referencias a través
	 * de la interfaz Runnable.
	 */
	public void run() {
		
		try
		{
			System.out.println("Paso 1 de 3 => GetLinksReferences()");
			getLinksReferences();
			
			System.out.println("Paso 2 de 3 => downloadReferencesToMendeley()");
			downloadReferencesToMendeley();
			
			System.out.println("Paso 3 de 3 => collectAllReferences()");
			collectAllReferences();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Método que obtiene los enlaces de las referencias. Es implementado
	 * por las clases que heredan de EngineSearch.
	 * 
	 * @throws Exception
	 */
	public abstract void getLinksReferences() throws Exception;
	
	/**
	 * Método que realiza la descarga de las referencias a través de los enlaces
	 * links obtenidos en un paso previo.
	 * 
	 * @throws InterruptedException
	 */
	public void downloadReferencesToMendeley() throws InterruptedException
	{
		int index = 0;
		
		// Creamos 'N' hilos para busquedas paralelas
		List<Thread> threads = inicialiceArrayThreads();
		List<DownloadReference> downloadReferences = inicialiceArrayDownloadReferences();
		
		synchronized (linksDocuments) {
			
			int contMax = getContMax();
			List<Reference> references = getReferences();
			int contHilos = getContHilos();
			
			while(contMax < TAM_DEF && index < linksDocuments.size() && ((references.size() + contHilos) < TAM_MAX))
			{
				contMax = getContMax();
				contHilos = getContHilos();
				references = getReferences();
				
				int posTHread = getDisponibleThread(threads);
				
				if(posTHread != -1)
				{
					Thread thread = threads.get(posTHread);
					DownloadReference downloadReference = downloadReferences.get(posTHread);
					
					if (downloadReference == null)
					{
						downloadReference = new DownloadReference(engine,engine+"-Hilo-"+(posTHread+1), linksDocuments.get(index), 
								"", emailMend, passMend, nameSLR);
					}
					else
					{
						downloadReference.setUrl(linksDocuments.get(index));
					}
					downloadReferences.set(posTHread, downloadReference);
					thread = new Thread(downloadReference);
					threads.set(posTHread, thread);
					index++;
					increaseContHilos();
					thread.start();
				}
			} // fin-while
			
			while(!isFinishedThreads(threads))
			{
				// Esperamos a que finalicen los hilos
			}
			
		} // fin-synchronized
		
		closeDownloadReferences(downloadReferences);
	}
	
	/**
	 * Método privado que obtiene todas las referencias por motor de búsqueda
	 * y las almacena en una lista general.
	 * @throws Exception 
	 * 
	 */
	private void collectAllReferences() throws Exception
	{
		// ACM
		setIdMendeletFolderEngine(EngineSearchACM.references, TypeEngineSearch.ACM);
		referencesEngineSearch.addAll(EngineSearchACM.references);
		
		// IEEE
		setIdMendeletFolderEngine(EngineSearchACM.references, TypeEngineSearch.IEEE);
		referencesEngineSearch.addAll(EngineSearchIEEE.references);
	}
	
	private String getIdMendeleyFolderEngine(TypeEngineSearch typeEngineSearch) throws Exception
	{
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri, emailMend, passMend);
		
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
	
	private void setIdMendeletFolderEngine(List<Reference> references, TypeEngineSearch typeEngineSearch) throws Exception
	{
		String idMendeleyFolder = getIdMendeleyFolderEngine(typeEngineSearch);
		
		for(Reference reference : references)
		{
			reference.setIdFolderEngine(idMendeleyFolder);
		}
	}
	
	/**
	 * Método que añade una referencia a la lista de referencias que tiene cada
	 * uno de los motores de búsquedas posibles.
	 * 
	 * @param typeEngine Tipo de motor de búsqueda
	 * @param reference Referencia a insertar
	 * @return Verdadero si la inserción ha tenido éxito.
	 */
	public static boolean addReferenceToEngineSearch(Reference reference)
	{
		boolean ok = false;
		
		if (TypeEngineSearch.ACM == reference.getTypeEngineSearch())
		{
			synchronized (EngineSearchACM.references) {
				EngineSearchACM.references.add(reference);
				ok = true;
			}
		}
		else if (TypeEngineSearch.IEEE == reference.getTypeEngineSearch())
		{
			synchronized (EngineSearchIEEE.references) {
				EngineSearchIEEE.references.add(reference);
				ok = true;
			}
		}
		
		return ok;
	}
	
	/**
	 * Método privado que incrementa el contador de hilos
	 */
	private void increaseContHilos()
	{
		if (TypeEngineSearch.ACM == engine)
		{
			EngineSearchACM.contHilos++;
		} 
		else if (TypeEngineSearch.IEEE == engine)
		{
			EngineSearchIEEE.contHilos++;
		}
	}
	
	/**
	 * Método estático que decrementa el contador de hilos.
	 * @param typeEngine
	 */
	public static void decreaseContHilos(TypeEngineSearch typeEngine)
	{
		if (TypeEngineSearch.ACM == typeEngine)
		{
			EngineSearchACM.contHilos--;
		}
		else if (TypeEngineSearch.IEEE == typeEngine)
		{
			EngineSearchIEEE.contHilos--;
		}
	}
	
	/**
	 * Método que incrementa el contador de referencias.
	 * 
	 * @param typeEngine Motor de búsqueda
	 */
	public static void increaseContMax(TypeEngineSearch typeEngine)
	{
		if (TypeEngineSearch.ACM == typeEngine)
		{
			EngineSearchACM.contMax++;
		}
		else if (TypeEngineSearch.IEEE == typeEngine)
		{
			EngineSearchIEEE.contMax++;
		}
	}
	
	/**
	 * Método privado que obtiene el contador de hilos según el motor
	 * de búsqueda.
	 * 
	 * @return Número de hilos que el motor de búsqueda está usando en ese momento.
	 */
	private int getContHilos() {
		int contHilos = 0;

		if (this instanceof EngineSearchACM)
		{
			contHilos = EngineSearchACM.contHilos;
		}
		else if (this instanceof EngineSearchIEEE)
		{
			contHilos = EngineSearchIEEE.contHilos;
		}
		
		return contHilos;
	}

	/**
	 * Método que obtiene el número de referencias obtenidas hasta ese momento.
	 * 
	 * @return Número de referencias obtenidas hasta ese momento.
	 */
	private int getContMax() 
	{
		int contMax = 0;

		if (this instanceof EngineSearchACM)
		{
			contMax = EngineSearchACM.contMax;
		}
		else if (this instanceof EngineSearchIEEE)
		{
			contMax = EngineSearchIEEE.contMax;
		}
		
		return contMax;
	}

	/**
	 * Método que cierra las conexiones de los hilos para descargar
	 * las referencias.
	 * 
	 * @param downloadReferences
	 */
	private void closeDownloadReferences(List<DownloadReference> downloadReferences) {
		
		for(DownloadReference d : downloadReferences)
		{
			if (d != null)
			{
				d.closeWebClient();
			}
		}
		
	}

	/**
	 * Método que devuelve todas las referencias obtenidas.
	 * 
	 * @return
	 */
	private List<Reference> getReferences()
	{
		List<Reference> references = new ArrayList<Reference>();
		if (TypeEngineSearch.ACM == engine)
		{
			references = EngineSearchACM.references;
		}
		else if (TypeEngineSearch.IEEE == engine)
		{
			references = EngineSearchIEEE.references;
		}
		return references;
	}
	
	/**
	 * Método que comprueba si todos los hilos han finalizado su
	 * ejecución.
	 * 
	 * @param threads
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
		
		for(int i=0; i < TOTAL_HILOS; i++)
		{
			threads.add(null);
		}
		
		return threads;
	}

	/**
	 * Método que inicializa con valores nulos los objetos DownloadReference
	 * que emplearan los hilos.
	 * 
	 * @return Vector de objetos DownloadReference
	 */
	private List<DownloadReference> inicialiceArrayDownloadReferences() {
		
		List<DownloadReference> downloadReferences = new ArrayList<DownloadReference>();
		
		for(int i=0; i < TOTAL_HILOS; i++)
		{
			downloadReferences.add(null);
		}
		
		return downloadReferences;
	}

	/**
	 * Método privado que obtiene la posición del vector de hilos de 
	 * aquel hilo que se encuentre disponible para ejecutarse.
	 * 
	 * @param threads
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

	// Gets y Sets
	
	public TypeEngineSearch getEngine() {
		return engine;
	}

	public void setEngine(TypeEngineSearch engine) {
		this.engine = engine;
	}

	/*public MendeleyService getMendeleyService() {
		return mendeleyService;
	}

	public void setMendeleyService(MendeleyService mendeleyService) {
		this.mendeleyService = mendeleyService;
	}*/

	public String getNameSLR() {
		return nameSLR;
	}

	public void setNameSLR(String nameSLR) {
		this.nameSLR = nameSLR;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public int getStart_year() {
		return start_year;
	}

	public void setStart_year(int start_year) {
		this.start_year = start_year;
	}

	public int getEnd_year() {
		return end_year;
	}

	public void setEnd_year(int end_year) {
		this.end_year = end_year;
	}

	public List<SearchTermParam> getSearchsTerms() {
		return searchsTerms;
	}

	public void setSearchsTerms(List<SearchTermParam> searchsTerms) {
		this.searchsTerms = searchsTerms;
	}

	public int getTAM_MAX() {
		return TAM_MAX;
	}

	public void setTAM_MAX(int tAM_MAX) {
		TAM_MAX = tAM_MAX;
	}

	public int getTAM_DEF() {
		return TAM_DEF;
	}

	public void setTAM_DEF(int tAM_DEF) {
		TAM_DEF = tAM_DEF;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getIdEngine() {
		return idEngine;
	}

	public void setIdEngine(String idEngine) {
		this.idEngine = idEngine;
	}

	public List<String> getLinksDocuments() {
		return linksDocuments;
	}

	public void setLinksDocuments(List<String> linksDocuments) {
		this.linksDocuments = linksDocuments;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getEmailMend() {
		return emailMend;
	}

	public void setEmailMend(String emailMend) {
		this.emailMend = emailMend;
	}

	public String getPassMend() {
		return passMend;
	}

	public void setPassMend(String passMend) {
		this.passMend = passMend;
	}
}
