package mendeley.pfc.tests;

import java.util.List;

import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.services.AnnotationService;
import mendeley.pfc.services.MendeleyService;

public class AnnotationServiceTest {
	
	public static final String APP_ID = "1044";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "5qQ6zm5iYpvUehj4";

	public static void main(String[] args) throws Exception {

		String email = "angel.gonzatoro@gmail.com";
		String pass  = "angel.gonzatoro";
		
		MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass);
		AnnotationService annotationService = new AnnotationService(mendeleyService);
		
		System.out.println("ANTES: " + annotationService.getAllAnnotations().size());
		
		for(Annotation a : annotationService.getAllAnnotations())
		{
			annotationService.deleteAnnotation(a);
		}
		
		System.out.println("AHORA: " + annotationService.getAllAnnotations().size());
	}

}
