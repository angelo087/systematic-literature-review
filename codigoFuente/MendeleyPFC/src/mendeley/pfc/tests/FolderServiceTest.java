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
			
			List<Folder> folders = folderService.getAllFolders();
			
			for(Folder folder : folders)
			{
				if(folder.getParent() == null || folder.getParent() == "")
				{
					System.out.println(folder.getId() + " -> " + folder.getName());
				}
				else
				{
					System.out.println("\t" + folder.getId() + " -> " + folder.getName());
				}
			}
			
			//fa1c3ae3-82ea-4ed7-aedc-4ac7d01f0760 -> acm
			//23be5f27-ede6-3447-bcbc-c34774005963 -> document

			Document document = documentService.getDocument("23be5f27-ede6-3447-bcbc-c34774005963");
			
			System.out.println("ID -> " + document.getId());
			System.out.println("TITLE -> " + document.getTitle());
			System.out.println("ABSTRACT -> " + document.getAbstract());
			System.out.println("SOURCE -> " + document.getSource());
			System.out.println("YEAR -> " + document.getYear());
			System.out.println("PAGES -> " + document.getPages());
			System.out.println("VOLUME -> " + document.getVolume());
			System.out.println("ISSUE -> " + document.getIssue());
			System.out.println("PUBLISHER -> " + document.getPublisher());
			System.out.println("CITY -> " + document.getCity());
			System.out.println("INSTITUTION -> " + document.getInstitution());
			System.out.println("SERIES -> " + document.getSeries());
			System.out.println("CHAPTER -> " + document.getChapter());
			System.out.println("CITATION KEY-> " + document.getCitationKey());
			System.out.println("SOURCE TYPE -> " + document.getSourceType());
			System.out.println("GENRE -> " + document.getGenre());
			System.out.println("COUNTRY -> " + document.getCountry());
			System.out.println("DEPARTMENT -> " + document.getDepartment());
			System.out.println("ARXIV -> " + document.getIdentifiers().getArxiv());
			System.out.println("DOI -> " + document.getIdentifiers().getDoi());
			System.out.println("ISBN -> " + document.getIdentifiers().getIsbn());
			System.out.println("ISSN -> " + document.getIdentifiers().getIssn());
			System.out.println("PMID -> " + document.getIdentifiers().getPmid());
			System.out.println("SCOPUS -> " + document.getIdentifiers().getScopus());
			System.out.println("MONTH -> " + document.getMonth());
			System.out.println("DAY -> " + document.getDay());
			System.out.println("FILE ATTACHED -> " + document.getFileAttached());
			System.out.println("BIBTEX -> " + documentService.getBibtex(document));
			System.out.println("KEYWORDS -> " + document.getKeywords());
			System.out.println("WEBSITES -> " + document.getWebsites());
			System.out.println("TAGS -> " + document.getTags());
			System.out.println("TYPE -> " + document.getType());
			System.out.println("LANGUAGE -> " + document.getLanguage());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
}
