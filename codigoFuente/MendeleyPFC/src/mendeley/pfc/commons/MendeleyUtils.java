package mendeley.pfc.commons;

import java.io.IOException;

import org.apache.http.HttpException;

import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;

public class MendeleyUtils {

	public static void validateIsNotNull(Object object, String nameObject) throws MendeleyException
	{
		if(object == null)
		{
			throw new MendeleyException("Error: " + object + " is null.");
		}
	}
	
	public static void validateIsNotNullOrEmpty(String object) throws MendeleyException
	{
		if(object == null || object.equals(""))
		{
			throw new MendeleyException("Error: " + object + " is null.");
		}
	}
	
	public static void validateNameFolder(String nameFolder, FolderService folderService) throws MendeleyException, HttpException, IOException
	{
		validateIsNotNullOrEmpty(nameFolder);
		validateIsNotNull(folderService, "folderService");
		
		Folder folder = folderService.getFolderByName(nameFolder);
		
		if(folder != null)
		{
			throw new MendeleyException("Error: The folder's name " + nameFolder + " is in use.");
		}
	}
	
	public static void validateFolderExists(Folder folder, FolderService folderService) throws MendeleyException, HttpException, IOException
	{
		validateIsNotNull(folder, "folder");
		validateIsNotNull(folderService, "folderService");
		validateFolderExists(folder.getName(), folderService);
	}
	
	public static void validateFolderExists(String idFolder, FolderService folderService) throws MendeleyException, HttpException, IOException
	{
		validateIsNotNullOrEmpty(idFolder);
		validateIsNotNull(folderService, "folderService");
		
		Folder folder = folderService.getFolderById(idFolder);
		
		if(folder == null)
		{
			throw new MendeleyException("Error: The folder with id: '" + idFolder + "' is not exists.");
		}
	}
	
	public static void validateDocumentExists(Document document, DocumentService documentService) throws HttpException, MendeleyException, IOException
	{
		validateIsNotNull(document, "document");
		validateIsNotNull(documentService, "documentService");
		validateDocumentExists(document.getId(), documentService);
	}
	
	public static void validateDocumentExists(String idDocument, DocumentService documentService) throws MendeleyException, HttpException, IOException
	{
		validateIsNotNullOrEmpty(idDocument);
		validateIsNotNull(documentService, "documentService");
		
		Document document = documentService.getDocument(idDocument);
		
		if(document == null)
		{
			throw new MendeleyException("Error: The document with id: " + idDocument + " is not exists.");
		}
	}
}
