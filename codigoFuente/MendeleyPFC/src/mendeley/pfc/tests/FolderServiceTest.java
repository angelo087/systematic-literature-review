package mendeley.pfc.tests;

import java.util.List;

import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;

public class FolderServiceTest {
	
	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "5qQ6zm5iYpvUehj4";
	
	public static void main(String[] args)
	{
		try
		{
			String email = APP_EMAIL;
			String pass  = APP_PASS;
			String accessToken = "MSwxNDcyMzgwNTI4OTg3LDI0MzA0MjAxLDEwNDQsYWxsLCwsNzZkMjE5ZWQzNDBkNjUxY2E3NTkwMjItY2ViNmFkYzNkMjgxMmJhNiwxMmRmMWZjNy1lODY0LTNhYWYtYjFlZS1iNTRlNzFkMzc3MjgsUmhCOEpFOGM3UUpkNXFkY0R4Ym1zQzcxRGVZ";
			String refreshToken = "MSwyNDMwNDIwMSwxMDQ0LGFsbCw3NmQyMTllZDM0MGQ2NTFjYTc1OTAyMi1jZWI2YWRjM2QyODEyYmE2LGE2ZDIxOWVkMzQwZDY1MWNhNzU5MDIyLWNlYjZhZGMzZDI4MTJiYTYsMTQ3MjM3NjkyODY0MiwxMmRmMWZjNy1lODY0LTNhYWYtYjFlZS1iNTRlNzFkMzc3MjgsMmd4cFNfeUE0T1hQVlU0cGk2SG1rZVJ1WWpZ";
			
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass, accessToken, refreshToken);
			FolderService folderService = new FolderService(mendeleyService);
			DocumentService documentService = new DocumentService(mendeleyService);
			
			/*folderService.createFolder("Carpeta Creada");
			
			for(Folder folder : folderService.getAllFolders())
			{
				if(folder.getParent() == null || folder.getParent().equals(""))
				{
					System.out.println(folder.getName());
				}
				else
				{
					System.out.println("- " + folder.getName());
				}
			}*/
			
			//folderService.createSubFolderByIdParent("other", "1aacc5c4-ee2b-4292-9b26-95fe2744542e");
			
			Folder folder = folderService.getFolderById("1aacc5c4-ee2b-4292-9b26-95fe2744542e");
			
			System.out.println(folder.getName());
			
			List<Folder> subfolders = folderService.getSubFolders(folder);
			
			for(Folder subfolder : subfolders)
			{
				System.out.println("- "+subfolder.getName());
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
}
