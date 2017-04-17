package mendeley.pfc.services;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import mendeley.pfc.commons.MendeleyUrl;
import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.MendeleyUtils;
import mendeley.pfc.commons.TypeDocument;
import mendeley.pfc.commons.TypeDocumentDeserializer;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;

public class DocumentService {

	private MendeleyService mendeleyService;
	
	public DocumentService(MendeleyService mendeleyService) throws MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		this.mendeleyService = mendeleyService;
	}
	
	/**
	 * Método que devuelve los parámetros de conexión.
	 * 
	 * @return Parámetros de conexión con Mendeley.
	 */
	public MendeleyService getMendeleyService() { return mendeleyService; }
	
	public Document getDocument(String idDocument) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idDocument);
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.DOCUMENT_DETAILS.replaceAll("\\{id\\}", idDocument));
		
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
		Document document = gson.fromJson(json, Document.class);
		
		if (document.getId() != null && !document.getId().equals(""))
		{
			document.setBibtex(getBibtex(document));
		}
		
		return document;
	}
	
	public String getBibtex(Document document) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(document, "document");
		return getBibtex(document.getId());
	}
	
	public String getBibtex(String idDocument) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idDocument);
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.DOCUMENT_BIBTEXT.replaceAll("\\{id\\}", idDocument));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/x-bibtex");
	    
	    int status = httpclient.executeMethod(get);
	    
	    return get.getResponseBodyAsString();
	}
	
	public List<Document> getAllDocuments() throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.DOCUMENT_LIST_ALL);
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");
	    
	    int status = httpclient.executeMethod(get);
	    
	    GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		Type typeListDocument = new TypeToken<List<Document>>(){}.getType();
		List<Document> documents = gson.fromJson(new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"), typeListDocument);
	    
		// Bibtex
		for(Document doc : documents)
		{
			if (doc.getId() != null && !doc.getId().equals(""))
			{
				doc.setBibtex(getBibtex(doc));
			}
		}
		
		return documents;
	}
	
	public List<Document> getAllDocuments(int limit) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		if(limit < 0 || limit > 500)
		{
			limit = 10;
		}
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.DOCUMENT_LIST_ALL+"?limit="+Integer.toString(limit));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");
	    
	    int status = httpclient.executeMethod(get);
	    
	    GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		Type typeListDocument = new TypeToken<List<Document>>(){}.getType();
		List<Document> documents = gson.fromJson(new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"), typeListDocument);
		
		// Bibtex
		for(Document doc : documents)
		{
			if (doc.getId() != null && !doc.getId().equals(""))
			{
				doc.setBibtex(getBibtex(doc));
			}
		}
	    		
		return documents;
	}
	
	public String getAllBibtexDocuments() throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.DOCUMENT_LIST_ALL_BIBTEXT);
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/x-bibtex");
	    
	    int status = httpclient.executeMethod(get);
	    
	    return get.getResponseBodyAsString();
	}
	
	public Document createDocument(Document metadatos) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(metadatos, "document");
		
		HttpClient httpclient = new HttpClient();
	    
	    PostMethod post = new PostMethod(MendeleyUrl.DOCUMENT_CREATE);
	    
	    post.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");
	    post.addRequestHeader("Content-type", "application/vnd.mendeley-document.1+json");
	    
	    GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		String jsonstring = gson.toJson(metadatos);
		
		jsonstring = jsonstring.replaceAll(",\"year\":0", "");
		jsonstring = jsonstring.replaceAll(",\"month\":0", "");
		jsonstring = jsonstring.replaceAll(",\"day\":0", "");
		jsonstring = jsonstring.replaceAll("\"id\":\"\",", "");
		jsonstring = jsonstring.replaceAll("\"notes\":\"\",", "");
		jsonstring = jsonstring.replaceAll("\"notes\":\"\"", "");
		jsonstring = jsonstring.replaceAll("\"docAbstract\"", "\"abstract\"");
		
		StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    post.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(post);
	    
	    String responseBody = new String(post.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    responseBody = responseBody.replaceAll("\"abstract\"", "\"docAbstract\"");
		
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		Document document = gson.fromJson(json, Document.class);
		
		// Bibtex
		if (document.getId() != null && !document.getId().equals(""))
		{
			document.setBibtex(getBibtex(document));
		}
		
		return document;
	}
	
	public Document updateDocument(Document document, Document challenges) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(document, "document");
		MendeleyUtils.validateIsNotNull(challenges, "document-challenges");
		
		HttpClient httpclient = new HttpClient();
		
		PostMethod patch = new PostMethod(MendeleyUrl.DOCUMENT_UPDATE.replaceAll("\\{id\\}", document.getId())) 
		{
			@Override public String getName() { return "PATCH"; }
		};
		
		patch.addRequestHeader("_HttpMethod", "PATCH");
		patch.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    patch.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");
	    patch.addRequestHeader("Content-type", "application/vnd.mendeley-document.1+json");
	    
	    GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TypeDocument.class, new TypeDocumentDeserializer());
		Gson gson = gsonBuilder.create();
		
	    challenges.setCreated(document.getCreated());
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    Date today = Calendar.getInstance().getTime();        
	    String reportDate = df.format(today);
	    challenges.setLastModified(reportDate);
	    challenges.setFileAttached(document.getFileAttached());
	    
	    String jsonstring = gson.toJson(challenges);
	    
	    jsonstring = jsonstring.replaceAll(",\"year\":0", "");
		jsonstring = jsonstring.replaceAll(",\"month\":0", "");
		jsonstring = jsonstring.replaceAll(",\"day\":0", "");
		jsonstring = jsonstring.replaceAll("\"id\":\"\",", "");
		jsonstring = jsonstring.replaceAll("\"notes\":\"\",", "");
		jsonstring = jsonstring.replaceAll("\"notes\":\"\"", "");
		jsonstring = jsonstring.replaceAll("\"docAbstract\"", "\"abstract\"");
		jsonstring = jsonstring.replaceAll("\"abstract\":\"\",", "");
		jsonstring = jsonstring.replaceAll("\"keywords\":\\[\\],", "");
		jsonstring = jsonstring.replaceAll("\"websites\":\\[\\],", "");
		jsonstring = jsonstring.replaceAll("\"editors\":\\[\\],", "");
		jsonstring = jsonstring.replaceAll("\"tags\":\\[\\],", "");
		jsonstring = jsonstring.replaceAll("\"identifiers\":\\{\"arxiv\":\"\",\"doi\":\"\",\"isbn\":\"\",\"issn\":\"\",\"pmid\":\"\",\"scopus\":\"\"\\},", "");
		jsonstring = jsonstring.replaceAll("\"identifiers\":\\{\\},", "");
		jsonstring = jsonstring.replaceAll("\"authors\":\\[\\],", "");
		
		StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    patch.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(patch);
	    
	    String responseBody = new String(patch.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    responseBody = responseBody.replaceAll("\"abstract\"", "\"docAbstract\"");
		
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Document updateDocument = gson.fromJson(json, Document.class);
	    
		// Bibtex
		if (updateDocument.getId() != null && !updateDocument.getId().equals(""))
		{
			updateDocument.setBibtex(getBibtex(updateDocument));
		}
		
	    return updateDocument;
	}
	
	public void deleteDocument(String idDocument) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idDocument);
		
		HttpClient httpclient = new HttpClient();
		
		DeleteMethod delete = new DeleteMethod(MendeleyUrl.DOCUMENT_DELETE.replaceAll("\\{id\\}", idDocument));
		
		delete.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    delete.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    
	    int status = httpclient.executeMethod(delete);
	    
	    //String responseBody = new String(delete.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	}
	
	public void deleteDocument(Document document) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(document, "document");
		deleteDocument(document.getId());
	}
	
	public Folder getFolder(Document document) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(document,"document");
		
		Folder folder = null;
		
		FolderService folderService = new FolderService(mendeleyService);
		
		for(Folder f : folderService.getAllFolders())
		{
			List<String> ids = folderService.getIdsDocuments(f);
			
			if(ids.contains(document.getId()))
			{
				folder = f;
				break;
			}
		}
		
		return folder;
	}
	
	public Folder getSubFolder(Document document, Folder parentFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(document,"document");
		FolderService folderService = new FolderService(mendeleyService);
		MendeleyUtils.validateFolderExists(parentFolder, folderService);
		Folder subFolder = null;
		
		for(Folder f : folderService.getSubFolders(parentFolder))
		{
			List<String> ids = folderService.getIdsDocuments(f);
			if(ids.contains(f.getId()))
			{
				subFolder = f;
				break;
			}
		}
		
		return subFolder;
	}
	
	public void deleteDocumentsFromFolder(Folder folder) throws MendeleyException, HttpException, IOException
	{
		FolderService folderService = new FolderService(mendeleyService);
		
		List<Document> documents = folderService.getDocuments(folder);
		
		for(Document document : documents)
		{
			deleteDocument(document.getId());
		}
	}
}
