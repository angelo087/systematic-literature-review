package mendeley.pfc.services;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.commons.MendeleyUrl;
import mendeley.pfc.schemas.Annotation;
import mendeley.pfc.schemas.Document;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class AnnotationService {
	
	private MendeleyService mendeleyService;
	
	// Constructor del servicio de anotaciones
	public AnnotationService(MendeleyService mendeley) 
	{
		this.mendeleyService = mendeley;
	}
	
	public List<Annotation> getAllAnnotations() throws HttpException, IOException
	{
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.ANNOTATION_LIST_ALL);
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-annotation.1+json");
	    
	    int status = httpclient.executeMethod(get);
	    
	    Gson gson = new Gson();
		Type typeListAnnotation = new TypeToken<List<Annotation>>(){}.getType();
		List<Annotation> annotations = gson.fromJson(new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8"), typeListAnnotation);
		
		return annotations;
	}
	
	public Annotation createAnnotation(Annotation metadatos) throws HttpException, IOException
	{
		HttpClient httpclient = new HttpClient();
	    
	    PostMethod post = new PostMethod(MendeleyUrl.ANNOTATION_CREATE);
	    
	    post.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    post.addRequestHeader("Accept", "application/vnd.mendeley-annotation.1+json");
	    post.addRequestHeader("Content-type", "application/vnd.mendeley-annotation.1+json");
	    
	    Gson gson = new Gson();
		String jsonstring = gson.toJson(metadatos);
		
		StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    post.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(post);
	    
	    String responseBody = new String(post.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Annotation annotation = gson.fromJson(json, Annotation.class);
		
		return annotation;
	}
	
	public Annotation updateAnnotation(Annotation annotation, Annotation challenges) throws HttpException, IOException
	{
		HttpClient httpclient = new HttpClient();
		
		PostMethod patch = new PostMethod(MendeleyUrl.ANNOTATION_UPDATE.replaceAll("\\{id\\}", annotation.getId())) 
		{
			@Override public String getName() { return "PATCH"; }
		};
		
		patch.addRequestHeader("_HttpMethod", "PATCH");
		patch.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    patch.addRequestHeader("Accept", "application/vnd.mendeley-annotation.1+json");
	    patch.addRequestHeader("Content-type", "application/vnd.mendeley-annotation.1+json");
		
	    Gson gson = new Gson();
	    
	    String jsonstring = gson.toJson(challenges);
	    
	    StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    patch.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(patch);
	    
	    String responseBody = new String(patch.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Annotation updateAnnotation = gson.fromJson(json, Annotation.class);
	    
		return updateAnnotation;
	}
	
	public Annotation getAnnotationByDocument(Document document) throws HttpException, IOException
	{
		return getAnnotationByDocument(document.getId());
	}
	
	public Annotation getAnnotationByDocument(String idDocument) throws HttpException, IOException
	{
		Annotation annotation = null;
		
		for(Annotation a : getAllAnnotations())
		{
			if(a.getDocument_id().equals(idDocument))
			{
				annotation = a;
				break;
			}
		}
		
		return annotation;
	}
	
	public Annotation getAnnotationById(String id) throws HttpException, IOException
	{
		Annotation annotation = null;
		
		for(Annotation a : getAllAnnotations())
		{
			if(a.getId().equals(id))
			{
				annotation = a;
				break;
			}
		}
		
		return annotation;
	}
	
	public Annotation getAnnotationByText(String text) throws HttpException, IOException
	{
		return getAnnotationByText(text, false);
	}
	
	public Annotation getAnnotationByText(String text, boolean ignoreUpCase) throws HttpException, IOException
	{
		Annotation annotation = null;
		String textAux = (ignoreUpCase ? text.toLowerCase() : text);
		
		for(Annotation a : getAllAnnotations())
		{
			String strA = (ignoreUpCase ? a.getText().toLowerCase() : a.getText());
			if(strA.equals(textAux))
			{
				annotation = a;
				break;
			}
		}
		
		return annotation;
	}
	
	public List<Annotation> getAllAnnotationsContainsText(String text) throws HttpException, IOException
	{
		return getAllAnnotationsContainsText(text, false);
	}
	
	public List<Annotation> getAllAnnotationsContainsText(String text, boolean ignoreUpCase) throws HttpException, IOException
	{
		List<Annotation> annotations = new ArrayList<Annotation>();
		String textAux = (ignoreUpCase ? text.toLowerCase() : text);
		
		for(Annotation a : getAllAnnotations())
		{
			String strA = (ignoreUpCase ? a.getText().toLowerCase() : a.getText());

			if(strA.contains(textAux))
			{
				annotations.add(a);
			}
		}
		
		return annotations;
	}
	
	public void deleteAnnotation(Annotation annotation) throws HttpException, IOException
	{
		HttpClient httpclient = new HttpClient();
		
		DeleteMethod delete = new DeleteMethod(MendeleyUrl.ANNOTATION_DELETE.replaceAll("\\{id\\}", annotation.getId()));
		
		delete.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    delete.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    
	    int status = httpclient.executeMethod(delete);
	    
	    System.out.println("deleteAnnotation => " + status);
	}
}
