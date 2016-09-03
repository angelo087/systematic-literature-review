package mendeley.pfc.engines;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.ParamSearch;
import mendeley.pfc.commons.TypeEngine;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.schemas.ReferenceSearch;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

public abstract class EngineSearch implements Runnable {
	
	public static int totalEncontrados = 0;
	
	public static final int MAX_PARALLEL_SEARCHS = 5;
	
	protected MendeleyService mendeleyService = null;
	protected FolderService folderService = null;
	protected DocumentService documentService = null;
	protected double TAM_MAX          = 0;
	protected double TAM_DEF			= 0;
	protected String nameSLR   		= "";
	protected String terminos  		= "";
	protected String query			= "";
	protected String tags				= "";
	protected String operator			= "";
	protected int start_year			= 0;
	protected int end_year			= 0;
	protected String componente 		= "";
	protected String idFolderSlr		= "";
	protected TypeEngine typeEngine;
	protected List<ParamSearch> paramsSearchs = new ArrayList<ParamSearch>();
	
	public EngineSearch(TypeEngine typeEngine, MendeleyService mendeleyService, String name, List<ParamSearch> paramsSearchs, int tammax, List<String> etiquetas, String operator, int start_year, int end_year, String componente) throws HttpException, MendeleyException, IOException
	{
		this.typeEngine = typeEngine;
		this.mendeleyService = mendeleyService;
		this.folderService = new FolderService(this.mendeleyService);
		this.documentService = new DocumentService(this.mendeleyService);
		nameSLR   = name;
		this.paramsSearchs = paramsSearchs;
		idFolderSlr = getIdFolderSlr();
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
	
	public void startSearchs() throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException, InterruptedException
	{
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		createQuerySearch();
		getLinksReferences();
		downloadReferencesToMendeley();
		moveToFolder();
		TFin = System.currentTimeMillis();
		tiempo = (TFin - TInicio)/1000; //Calculamos los milisegundos de diferencia
		if(TypeEngine.ACM == typeEngine)
		{
			System.out.println("RESULTADOS ACM: " + EngineSearchACM.references.size() + " referencias en " + tiempo + " segundos.");
		}
		else if(TypeEngine.IEEE == typeEngine)
		{
			System.out.println("RESULTADOS IEEE: " + EngineSearchIEEE.references.size() + " referencias en " + tiempo + " segundos.");
		}
	}
	
	private String getIdFolderSlr() throws MendeleyException, HttpException, IOException
	{
		Folder folderSlr = folderService.getFolderByName(nameSLR);
		
		return folderSlr.getId();
	}
	
	public abstract void createQuerySearch();
	
	public abstract int getLinksReferences() throws FailingHttpStatusCodeException, MalformedURLException, IOException;
			
	public abstract void downloadReferencesToMendeley() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException;
	
	private void moveToFolder() throws MendeleyException, HttpException, IOException
	{
		Folder folderEngine = folderService.getSubFolder(idFolderSlr, typeEngine.getKey());
		
		if (folderEngine == null)
		{
			folderEngine = folderService.createSubFolderByIdParent(typeEngine.getKey(), idFolderSlr);
		}
		
		List<ReferenceSearch> referencesSearchs = null;
		
		if(TypeEngine.ACM == typeEngine)
		{
			referencesSearchs = EngineSearchACM.references;
		}
		else if (TypeEngine.IEEE == typeEngine)
		{
			referencesSearchs = EngineSearchIEEE.references;
		}
		
		if (referencesSearchs != null)
		{
			for(ReferenceSearch ref : referencesSearchs)
			{
				Document doc = documentService.getDocument(ref.getIdMendeley());
				folderService.addDocument(folderEngine, doc);
			}
		}
	}
	
	@Override
	public void run() {
		try 
		{
			startSearchs();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
