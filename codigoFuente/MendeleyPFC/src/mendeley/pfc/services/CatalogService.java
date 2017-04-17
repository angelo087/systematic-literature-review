package mendeley.pfc.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import mendeley.pfc.commons.MendeleyUrl;
import mendeley.pfc.commons.TypeDocument;
import mendeley.pfc.commons.TypeDocumentDeserializer;
import mendeley.pfc.schemas.Catalog;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;

public class CatalogService {

	private MendeleyService mendeleyService;
	
	public CatalogService(MendeleyService mendeleyService)
	{
		this.mendeleyService = mendeleyService;
	}
	
	/**
	 * Método que devuelve los parámetros de conexión.
	 * 
	 * @return Parámetros de conexión con Mendeley.
	 */
	public MendeleyService getMendeleyService() { return mendeleyService; }
	
	public List<Catalog> getCatalogDocumentByDOI(String id) throws HttpException, IOException
	{
		return getCatalogDocument(id, MendeleyUrl.CATALOG_DETAILS_BY_DOI);
	}
	
	public List<Catalog> getCatalogDocumentByISBN(String id) throws HttpException, IOException
	{
		return getCatalogDocument(id, MendeleyUrl.CATALOG_DETAILS_BY_ISBN);
	}
	
	public List<Catalog> getCatalogDocumentByISSN(String id) throws HttpException, IOException
	{
		return getCatalogDocument(id, MendeleyUrl.CATALOG_DETAILS_BY_ISSN);
	}
	
	public List<Catalog> getCatalogDocumentByARXIV(String id) throws HttpException, IOException
	{
		return getCatalogDocument(id, MendeleyUrl.CATALOG_DETAILS_BY_ARXIV);
	}
	
	public List<Catalog> getCatalogDocumentByPMID(String id) throws HttpException, IOException
	{
		return getCatalogDocument(id, MendeleyUrl.CATALOG_DETAILS_BY_PMID);
	}
	
	private List<Catalog> getCatalogDocument(String id, String urlMendeley) throws HttpException, IOException
	{
		List<Catalog> catalogs = new ArrayList<Catalog>();
		
		HttpClient httpclient = new HttpClient();

		GetMethod get = new GetMethod(urlMendeley.replaceAll("\\{id\\}", id));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");

	    int status = httpclient.executeMethod(get);
	    
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());

		Gson gson = gsonBuilder.create();
		Type typeListCatalog = new TypeToken<List<Catalog>>(){}.getType();
		
		String responseBody = get.getResponseBodyAsString().replace("\"abstract\":", "\"docAbstract\":");
		catalogs = gson.fromJson(new String(responseBody.getBytes("ISO-8859-1"), "UTF-8"), typeListCatalog);
		
		return getAllDetails(catalogs);
	}
	
	private List<Catalog> getAllDetails(List<Catalog> incompleteCatalogs) throws UnsupportedEncodingException, IOException
	{
		List<Catalog> completeCatalogs = new ArrayList<Catalog>();
		
		for(Catalog catalog : incompleteCatalogs)
		{
			Catalog completeCatalog = getCatalogById(catalog.getId());
			if(completeCatalog != null)
			{
				completeCatalogs.add(completeCatalog);
			}
		}
		
		return completeCatalogs;
	}
	
	public Catalog getCatalogById(String id) throws UnsupportedEncodingException, IOException
	{
		Catalog catalog = null;
		
		HttpClient httpclient = new HttpClient();

		GetMethod get = new GetMethod(MendeleyUrl.CATALOG_DETAILS_ALL.replaceAll("\\{id\\}", id));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");
	    
	    int status = httpclient.executeMethod(get);
	    
	    String responseBody = new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    responseBody = responseBody.replace("\"abstract\":", "\"docAbstract\":");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		catalog = gson.fromJson(json, Catalog.class);
	    
		return catalog;
	}
	
	public static void main(String[] args) throws Exception {
		String 	client_id = "1044", 
				client_secret = "5qQ6zm5iYpvUehj4", 
				redirect_url = "http://localhost:8095/SLR/indexMendeley/",
				email = "angel.gonzatoro@gmail.com",
				password = "Number98*";

		MendeleyService mendeleyService = new MendeleyService(client_id,client_secret,redirect_url,email,password);
		DocumentService documentService = new DocumentService(mendeleyService);
		CatalogService catalogService = new CatalogService(mendeleyService);
		FolderService folderService = new FolderService(mendeleyService);
		
		String idFolderEngine = "b63c2513-d03d-44e7-87d7-d1d61287ebe7";
		
		List<String> dois = new ArrayList<String>();
		dois.add("10.1109/TC.2017.2651828");
		dois.add("10.1109/IIH-MSP.2015.81");
		dois.add("10.1109/TIM.2016.2644898");
		dois.add("10.1109/eScience.2014.24");
		dois.add("10.1109/CSSS.2011.5974986");
		dois.add("10.1109/CAOL.2010.5634281");
		dois.add("10.1109/IEMBS.2010.5628010");
		dois.add("10.1109/ISDEA.2013.552");
		dois.add("10.1109/CVPRW.2014.57");		
		
		for(String doi : dois)
		{
			String doiDocument = doi;
			
			List<Catalog> catalogs = catalogService.getCatalogDocumentByDOI(doiDocument);
			
			if (catalogs == null || catalogs.size() == 0)
			{
				System.out.println("No existe un catalog con doi= " + doiDocument);
			}
			else
			{
				for(Catalog catalog : catalogs)
				{
					Document document = catalog.convertToDocument();
					Document docResult = documentService.createDocument(document);
					
					System.out.println("Documento de catalog con id=" 
					+ catalog.getId() + " creado => " + docResult.getId());
					
					folderService.addDocument(idFolderEngine, docResult.getId());
				}
			}
		}
	}
}
