package mendeley.pfc.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.TypeDocument;
import mendeley.pfc.schemas.Document;
import mendeley.pfc.schemas.Folder;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.FolderService;
import mendeley.pfc.services.MendeleyService;

public class DocumentServiceTest {

	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "FJRrPdYqo08P01rn";
	
	public static void main(String[] args) {
		
		String email = "alpha_snake087@hotmail.com";
		String pass  = "serpiente2";
		String accessToken = "MSwxNDY3MzA3NTcwMjQ2LDI0OTc0NDIxLDEwNDQsYWxsLCwsMWRkMTFlOTdiMDJhNTUxOWZkMzcxYTctZjZiZTYxMzExNjY3NzVhOSwwMTRjODhkZS05MjY5LTM3Y2QtYmYwOC0zNDE2YmUwNDE1YTksZlY5VEN5WU9mNkdOUFJzV3ExaTJPZVNUaTBj";
		String refreshToken = "MSwyNDk3NDQyMSwxMDQ0LGFsbCwxZGQxMWU5N2IwMmE1NTE5ZmQzNzFhNy1mNmJlNjEzMTE2Njc3NWE5LDcyMjctN2M2YmMzNTQ1NTE5NTIyYzM3MS1hNThiMjA2NWY5NTE0YzgzLDE0NjczMDM5NzAyNDQsMDE0Yzg4ZGUtOTI2OS0zN2NkLWJmMDgtMzQxNmJlMDQxNWE5LE1palhPdWNORjNydjJMRm1zaXRaTW9oQlNuTQ";
		
		try 
		{
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass, accessToken, refreshToken);

			FolderService folderService = new FolderService(mendeleyService);
			DocumentService documentService = new DocumentService(mendeleyService);
			
			//7c349b52-2ada-4f6d-8897-78523bc59028 <- slr
			//be607364-e65a-4510-bb13-c85bdc5566c1 <- slr > acm
			//350f181a-dafe-389c-ad25-f5ac1e786b30 <- document
			
			folderService.addDocument(folderService.getFolderById("be607364-e65a-4510-bb13-c85bdc5566c1"), 
					documentService.getDocument("350f181a-dafe-389c-ad25-f5ac1e786b30"));
			
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
