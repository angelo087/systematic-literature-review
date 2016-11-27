package mendeley.pfc.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Library;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.MendeleyUrl;
import mendeley.pfc.commons.MendeleyUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class FolderService 
{
	private MendeleyService mendeleyService;
	
	public static final int LIMIT_DOCUMENTS_BY_FOLDER = 500;
	public static final int LIMIT_FOLDERS = 500;
	
	/**
	 * Constructor clase FolderService
	 * 
	 * @param mendeleyService
	 * @throws MendeleyException
	 */
	public FolderService(MendeleyService mendeleyService) throws MendeleyException
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
	
	public List<Folder> getAllFolders(int limit) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		List<Folder> folders = new ArrayList<Folder>();
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.FOLDER_LIST_ALL.replaceAll("\\{limit\\}", Integer.toString(limit)));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-folder.1+json");	    

	    int status = httpclient.executeMethod(get);
	    
	    if(status != 200)
	    {
	    	throw new MendeleyException("FolderService.getAllFolders() -> status = ." + status);
	    }

	    Gson gson = new Gson();
		Type typeListFolder = new TypeToken<List<Folder>>(){}.getType();
		folders = gson.fromJson(new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"), typeListFolder);
	    
		return folders;
	}
	
	/**
	 * Listado de todas las carpetas de un usuario.
	 * 
	 * @return Carpetas del usuario.
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public List<Folder> getAllFolders() throws HttpException, IOException, MendeleyException
	{
		return getAllFolders(LIMIT_FOLDERS);
	}
	
	/**
	 * Obtiene un folder a través de su identificador
	 * 
	 * @param idFolder
	 * @return Folder con identificador pasado por parámetro o null si no existe.
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public Folder getFolderById(String idFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idFolder);
		
		List<Folder> folders = getAllFolders();
		Folder folderSearch = null;
		
		for(Folder folder : folders)
		{
			if(idFolder.equals(folder.getId()))
			{
				folderSearch = folder;
			}
		}
		
		return folderSearch;
	}
	
	/**
	 * Método que devuelve una carpeta que coincida con el nombre pasado por parámetro.
	 * @param nameFolder
	 * @return Carpeta con el nombre pasado por parámetro o null si no existe.
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public Folder getFolderByName(String nameFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(nameFolder);
		
		List<Folder> folders = getAllFolders();
		Folder folderSearch = null;
		
		for(Folder folder : folders)
		{
			if(nameFolder.toLowerCase().equals(folder.getName().toLowerCase()))
			{
				folderSearch = folder;
			}
		}
		
		return folderSearch;
	}
	
	/**
	 * Método que devuelve las subcarpetas de una carpeta (identificador del padre).
	 * @param idFolder
	 * @return Subcarpetas de una carpeta.
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<Folder> getSubFolders(String idFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idFolder);
		
		List<Folder> subfolders = new ArrayList<Folder>();
		
		for(Folder folder : getAllFolders())
		{
			if(folder.getParent() != null && folder.getParent().equals(idFolder))
			{
				subfolders.add(folder);
			}
		}
		
		return subfolders;
	}
	
	public Folder getSubFolder(String idFolder, String nameFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNullOrEmpty(idFolder);
		MendeleyUtils.validateIsNotNullOrEmpty(nameFolder);
		
		List<Folder> subfolders = getSubFolders(idFolder);
		Folder subFolder = null;

		for(Folder folder : subfolders)
		{
			if(folder.getName().toLowerCase().equals(nameFolder.toLowerCase()))
			{
				subFolder = folder;
				break;
			}
		}
		
		return subFolder;
	}
	
	/**
	 * Método que devuelve las subcarpetas dada la carpeta padre.
	 * @param folder
	 * @return Subcarpetas de una carpeta.
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<Folder> getSubFolders(Folder folder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(folder, "folder");
		return getSubFolders(folder.getId());
	}
	
	
	/**
	 * Crea una carpeta en Mendeley
	 * 
	 * @param folderChanges
	 * @return Carpeta creada
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public Folder createFolder(Folder folderChanges) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(folderChanges, "folderChanges");
		
		HttpClient httpclient = new HttpClient();
	    
	    PostMethod post = new PostMethod(MendeleyUrl.FOLDER_CREATE);
	    
	    post.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Accept", "application/vnd.mendeley-folder.1+json");
	    post.addRequestHeader("Content-type", "application/vnd.mendeley-folder.1+json");
	    
	    Gson gson = new Gson();
				
		String jsonstring = gson.toJson(folderChanges);
		
	    StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    post.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(post);
	    
	    if(status != 201)
	    {
	    	throw new MendeleyException("FolderService.createFolder(String name) -> status = " + status);
	    }
	    
	    String responseBody = new String(post.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Folder folder = gson.fromJson(json, Folder.class);
	    
		return folder;
	}
	
	/**
	 * Crea una carpeta en Mendeley.
	 * 
	 * @param nameFolder
	 * @return Carpeta creada
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public Folder createFolder(String nameFolder) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateNameFolder(nameFolder, this);
		
		Folder folder = new Folder();
		folder.setName(nameFolder);
		return createFolder(folder);
	}
	
	/**
	 * Crea una subcarpeta dado el nombre de la subcarpeta y la carpeta padre
	 * 
	 * @param nameSubFolder
	 * @param folderParent
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public Folder createSubFolder(String nameSubFolder, Folder folderParent) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(folderParent, "folderParent");
		
		Folder subFolder = new Folder();
		subFolder.setName(nameSubFolder);
		subFolder.setParent(folderParent.getId());
		
		return createFolder(subFolder);
	}
	
	/**
	 * Crea una subcarpeta dado el nombre de la subcarpeta y el nombre de la carpeta padre
	 * 
	 * @param nameSubFolder
	 * @param nameFolderParent
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public Folder createSubFolder(String nameSubFolder, String nameFolderParent) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(nameFolderParent, "nameFolderParent");
		
		Folder parent = getFolderByName(nameFolderParent);
		return createSubFolder(nameSubFolder, parent);
	}
	
	/**
	 * Crea una subcarpeta dado el nombre de la subcarpeta y el id de la carpeta padre.
	 * 
	 * @param nameSubFolder
	 * @param idParent
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public Folder createSubFolderByIdParent(String nameSubFolder, String idParent) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		MendeleyUtils.validateIsNotNull(idParent, "idParent");
		
		Folder parent = getFolderById(idParent);
		return createSubFolder(nameSubFolder, parent);
	}
	
	/**
	 * Método que modifica una carpeta en Mendeley
	 * 
	 * @param folder carpeta a moficiar
	 * @param folderChanges carpeta con los cambios
	 * @return carpeta con las modificaciones
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public Folder updateFolder(Folder folder, Folder folderChanges) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		Folder updateFolder = null;
		
		HttpClient httpclient = new HttpClient();
		
		PostMethod patch = new PostMethod(MendeleyUrl.FOLDER_UPDATE.replaceAll("\\{id\\}", folder.getId())) 
		{
			@Override public String getName() { return "PATCH"; }
		};
		
		patch.addRequestHeader("_HttpMethod", "PATCH");
		patch.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    patch.addRequestHeader("Accept", "application/vnd.mendeley-folder.1+json");
	    patch.addRequestHeader("Content-type", "application/vnd.mendeley-folder.1+json");
	    
	    Gson gson = new Gson();
	    String jsonstring = gson.toJson(folderChanges);
	    
	    StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    patch.setRequestEntity(requestEntity);

	    int status = httpclient.executeMethod(patch);
	    String responseBody = new String(patch.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		updateFolder = gson.fromJson(json, Folder.class);
	    
		return updateFolder;
	}
	
	/**
	 * Método que elimina una carpeta de mendeley.
	 * 
	 * @param idFolder
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public void deleteFolder(String idFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		HttpClient httpclient = new HttpClient();
		
		DeleteMethod delete = new DeleteMethod(MendeleyUrl.FOLDER_DELETE.replaceAll("\\{id\\}", idFolder));
		
		delete.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    delete.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    
	    int status = httpclient.executeMethod(delete);
	}
	
	/**
	 * Método que elimina una carpeta de mendeley.
	 * 
	 * @param folder
	 * @throws HttpException
	 * @throws MendeleyException
	 * @throws IOException
	 */
	public void deleteFolder(Folder folder) throws HttpException, MendeleyException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		deleteFolder(folder.getId());
	}
	
	/**
	 * Método que devuelve los identificadores de los documentos de una carpeta junto
	 * a su límite.
	 * 
	 * @param idFolder
	 * @param limit
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<String> getIdsDocuments(String idFolder, int limit) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		Folder folder = getFolderById(idFolder);
		return getIdsDocuments(folder, limit);
	}
	
	/**
	 * Método que devuelve los identificadores de los documentos pertenecientes
	 * a una carpeta.
	 * 
	 * @param idFolder
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<String> getIdsDocuments(String idFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		return getIdsDocuments(idFolder, LIMIT_DOCUMENTS_BY_FOLDER);
	}
	
	/**
	 * Método que devuelve los identificadores de ls documentos pertenecientes a una carpeta
	 * 
	 * @param folder
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<String> getIdsDocuments(Folder folder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		return getIdsDocuments(folder, LIMIT_DOCUMENTS_BY_FOLDER);
	}
	
	/**
	 * Método que devuelve los identificadores de los documentos de una carpeta.
	 * @param folder
	 * @param limit
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<String> getIdsDocuments(Folder folder, int limit) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		
		if(limit < 0 || limit > LIMIT_DOCUMENTS_BY_FOLDER)
		{
			limit = LIMIT_DOCUMENTS_BY_FOLDER;
		}
		
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.FOLDER_LIST_DOCUMENTS.replaceAll("\\{id\\}", folder.getId()).replaceAll("\\{limit\\}", Integer.toString(limit)));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-document.1+json");

	    int status = httpclient.executeMethod(get);

	    Library libreria = new Library(new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"));
		
		return libreria.getDocumentsIds();
	}
	
	/**
	 * Método que devuelve los documentos pertenecientes a una carpeta.
	 * 
	 * @param folder
	 * @return
	 * @throws MendeleyException
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public List<Document> getDocuments(Folder folder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		return getDocuments(folder, LIMIT_DOCUMENTS_BY_FOLDER);
	}
	
	/**
	 * Método que devuelve los documentos pertenecientes a una carpeta.
	 * 
	 * @param idFolder
	 * @return
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public List<Document> getDocuments(String idFolder) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		Folder folder = getFolderById(idFolder);
		return getDocuments(folder);
	}
	
	/**
	 * Método que devuelve los documentos pertenecientes a una carpeta.
	 * 
	 * @param foder
	 * @param limit
	 * @return
	 * @throws MendeleyException
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public List<Document> getDocuments(Folder folder, int limit) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		List<Document> documents = new ArrayList<Document>();
		DocumentService documentservice = new DocumentService(mendeleyService);

		for(String id : getIdsDocuments(folder, limit))
		{
			documents.add(documentservice.getDocument(id));
		}
		
		return documents;
	}
	
	/**
	 * Método que inserta un documento en una carpeta.
	 * 
	 * @param folder
	 * @param document
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public void addDocument(Folder folder, Document document) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		addDocument(folder.getId(), document.getId());
	}
	
	/**
	 * Método que inserta un docuento en una carpeta.
	 * 
	 * @param idFolder
	 * @param idDocument
	 * @throws MendeleyException
	 * @throws HttpException
	 * @throws IOException
	 */
	public void addDocument(String idFolder, String idDocument) throws MendeleyException, HttpException, IOException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		HttpClient httpclient = new HttpClient();
	    
	    PostMethod post = new PostMethod(MendeleyUrl.FOLDER_ADD_DOCUMENT.replaceAll("\\{id\\}", idFolder));
	    
	    System.out.println(MendeleyUrl.FOLDER_ADD_DOCUMENT.replaceAll("\\{id\\}", idFolder));
	    
	    post.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Content-type", "application/vnd.mendeley-document.1+json");

	    //StringRequestEntity requestEntity = new StringRequestEntity("{\"id\": \""+ idDocument +"\"}", "application/json", "UTF-8");
	    StringRequestEntity requestEntity = new StringRequestEntity("{\"id\": \""+ idDocument +"\"}", "application/json", null);
	    post.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(post);
	    
	    System.out.println("addDocument => " + status);
	}
	
	/**
	 * Método que borra un documento de una carpeta.
	 * 
	 * @param folder
	 * @param document
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public void deleteDocument(Folder folder, Document document) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		deleteDocument(folder.getId(), document.getId());
	}
	
	/**
	 * Método que borra un documento de una carpeta.
	 * 
	 * @param idFolder
	 * @param idDocument
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	public void deleteDocument(String idFolder, String idDocument) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		HttpClient httpclient = new HttpClient();
		
		DeleteMethod delete = new DeleteMethod(MendeleyUrl.FOLDER_REMOVE_DOCUMENT.replaceAll("\\{id\\}", idFolder).replaceAll("\\{document\\_id\\}", idDocument));
		
		delete.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    delete.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    
	    int status = httpclient.executeMethod(delete);
	}
}
