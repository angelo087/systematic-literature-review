package background.pfc.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.Catalog;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.services.CatalogService;
import mendeley.pfc.services.DocumentService;

import org.apache.http.HttpException;

import background.pfc.engine.EngineSearch;
import background.pfc.enums.TypeEngineSearch;
import background.pfc.enums.TypeReferenceId;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;


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
	/** typeReferenceId .*/
	private TypeReferenceId typeReferenceId;
	/** url .*/
	private String codeReference;
	/** nameSLR .*/
	private String nameSLR;
	/** catalogService .*/
	private CatalogService catalogService;
	/** documentService. */
	private DocumentService documentService;
	/** guidStaticData.*/
	private String guidStaticData;
	
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
			String codeReference, String nameSLR, TypeReferenceId typeReferenceId, 
			CatalogService catalogService, DocumentService documentService,
			String guidStaticData) {
		
		this.name = name;
		this.typeEngine = typeEngine;
		this.typeReferenceId = typeReferenceId;		
		this.codeReference = codeReference;
		this.nameSLR = nameSLR;
		this.catalogService = catalogService;
		this.documentService = documentService;
		this.guidStaticData = guidStaticData;
	}

	/**
	 * Metodo que devuelve el nombre del hilo asociado.
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

	public TypeReferenceId getTypeReferenceId() {
		return typeReferenceId;
	}

	public void setTypeReferenceId(TypeReferenceId typeReferenceId) {
		this.typeReferenceId = typeReferenceId;
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
	 * M�todo que devuelve el code de la referencia a importar.
	 * 
	 * @return String
	 */
	public String getCodeReference() {
		return codeReference;
	}

	/**
	 * M�todo que establece la url de la referencia a importar.
	 * 
	 * @param url String
	 */
	public void setCodeReference(String codeReference) {
		this.codeReference = codeReference;
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

	public CatalogService getCatalogService() {
		return catalogService;
	}

	public void setCatalogService(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	public DocumentService getDocumentService() {
		return documentService;
	}

	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}

	public String getGuidStaticData() {
		return guidStaticData;
	}

	public void setGuidStaticData(String guidStaticData) {
		this.guidStaticData = guidStaticData;
	}

	/**
	 * M�todo que realiza el proceso paralelo para importar la referencia.
	 */
	@Override
	public void run() {
		boolean ok = false;
		
		try
		{
			ok = doImport();
		}
		catch(Exception ex)
		{
			ok = false;
		}
		
		if(ok)
		{
			//EngineSearch.increaseContMax(typeEngine);
			PoolReferences.increaseContMax(guidStaticData);
			System.out.println(this.name + " ha insertado referencia con " + this.typeReferenceId.getKey() + " = " + this.codeReference);
		}
		
		// Decrementamos el contador de hilos
		//EngineSearch.decreaseContHilos(typeEngine);
		PoolReferences.decreaseContHilos(guidStaticData);
	}
	
	/**
	 * Metodo privado que realiza la importaci�n de la referencia a Mendeley e indica
	 * si se ha realizado correctamente o no.
	 * 
	 * @return boolean
	 * @throws HttpException 
	 * @throws FailingHttpStatusCodeException FailingHttpStatusCodeException
	 * @throws MalformedURLException MalformedURLException
	 * @throws IOException IOException
	 * @throws MendeleyException 
	 * @throws InterruptedException InterruptedException
	 */
	private boolean doImport() throws HttpException, IOException, MendeleyException
	{
		boolean ok = false;
		List<Catalog> catalogs = new ArrayList<Catalog>();
		
		switch(typeReferenceId)
		{
			case DOI:
				catalogs = catalogService.getCatalogDocumentByDOI(codeReference);
				break;
			case ISBN:
				catalogs = catalogService.getCatalogDocumentByISBN(codeReference);
				break;
			case PMID:
				catalogs = catalogService.getCatalogDocumentByPMID(codeReference);
				break;
			case ARXIV:
				catalogs = catalogService.getCatalogDocumentByARXIV(codeReference);
				break;
			case ISSN:
				catalogs = catalogService.getCatalogDocumentByISSN(codeReference);
				break;
		}
		
		if (catalogs != null && catalogs.size() > 0)
		{
			Catalog catalog = catalogs.get(0);
			Document document = catalog.convertToDocument();
			Document docResult = documentService.createDocument(document);
			
			if(docResult != null && docResult.getId() != null)
			{
				Reference reference = new Reference(codeReference, docResult.getId(), "", typeEngine);
				//ok = EngineSearch.addReferenceToEngineSearch(reference);
				ok = PoolReferences.addReferenceToEngineSearch(guidStaticData, reference);
			}
		}
		
		return ok;
	}
}
