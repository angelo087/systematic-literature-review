package es.pfc.main;

import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.services.AnnotationService;
import mendeley.pfc.services.DocumentService;
import mendeley.pfc.services.MendeleyService;

public class NotesPrueba {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String 	client_id = "1044", 
				client_secret = "5qQ6zm5iYpvUehj4", 
				redirect_url = "http://localhost:8090/SLR/indexMendeley/",
				access_token  = "MSwxNDc2NTI1MjAwMjY3LDI0OTc0NDIxLDEwNDQsYWxsLCwsLDAxNGM4OGRlLTkyNjktMzdjZC1iZjA4LTM0MTZiZTA0MTVhOSw4RjFPZF9iWUlvaTY5bzk0anJwY3QtV2FQb28",
				refresh_token = "MSwyNDk3NDQyMSwxMDQ0LGFsbCwsLCwwMTRjODhkZS05MjY5LTM3Y2QtYmYwOC0zNDE2YmUwNDE1YTksMmt2bTFBWG1GaDZiUi1Lb1BiMjhGblBPYktZ";

		String email = "angel.gonzatoro@gmail.com", password = "angel.gonzatoro";

		MendeleyService mendeleyService = new MendeleyService(client_id, client_secret, redirect_url, email, password);
		
		DocumentService documentService = new DocumentService(mendeleyService);
		AnnotationService annotationService = new AnnotationService(mendeleyService);
		
		System.out.println("TOTAL => " + annotationService.getAllAnnotations().size());
		System.out.println("DOCUMENTOS => " + documentService.getAllDocuments().size());
		for(Annotation anno : annotationService.getAllAnnotations())
		{
			System.out.println("\t"+anno.getDocument() + " => " + anno.getText());
		}
	}

}
