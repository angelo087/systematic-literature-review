package mendeley.pfc.services;

import java.io.IOException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.MendeleyUrl;
import mendeley.pfc.commons.MendeleyUtils;
import mendeley.pfc.schemas.Profile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ProfileService {
	
	private MendeleyService mendeleyService;
	
	public ProfileService(MendeleyService mendeleyService) throws MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		this.mendeleyService = mendeleyService;
	}
	
	public Profile getProfile(String idProfile) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.PROFILE_DETAILS.replaceAll("\\{id\\}", idProfile));
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-profiles.1+json");
	    
	    int status = httpclient.executeMethod(get);
	    
	    String responseBody = new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Gson gson = new Gson();
		Profile profile = gson.fromJson(json, Profile.class);
	    	    
		return profile;
	}
	
	public Profile getCurrentProfile() throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		HttpClient httpclient = new HttpClient();
		
		GetMethod get = new GetMethod(MendeleyUrl.PROFILE_CURRENT_DETAILS);
		
		get.addRequestHeader("access_token", mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    get.addRequestHeader("Accept", "application/vnd.mendeley-profiles.1+json;");
	    
	    int status = httpclient.executeMethod(get);
	    
	    String responseBody = new String(get.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Gson gson = new Gson();
		Profile profile = gson.fromJson(json, Profile.class);
	    	    
		return profile;
	}
	
	public Profile updateCurrentProfile(Profile profile, Profile challenges) throws HttpException, IOException, MendeleyException
	{
		MendeleyUtils.validateIsNotNull(mendeleyService, "mendeleyService");
		HttpClient httpclient = new HttpClient();
		
		PostMethod patch = new PostMethod(MendeleyUrl.PROFILE_UPDATE) 
		{
			@Override public String getName() { return "PATCH"; }
		};
		
		patch.addRequestHeader("_HttpMethod", "PATCH");
		patch.addRequestHeader("Authorization", "Bearer " + mendeleyService.getTokenResponse().getAccessToken());
	    patch.addRequestHeader("Accept", "application/vnd.mendeley-profiles.1+json");
	    patch.addRequestHeader("Content-type", "application/vnd.mendeley-profile-amendment.1+json");
	    
	    Gson gson = new Gson();
	    
	    challenges.setVerified(profile.isVerified());
	    challenges.setMarketing(profile.isMarketing());
	    
	    String jsonstring = gson.toJson(challenges);
	    
	    jsonstring = jsonstring.replaceAll("\"location\":\\{\\},", "");
	    jsonstring = jsonstring.replaceAll("\"education\":\\[\\],", "");
	    jsonstring = jsonstring.replaceAll("\"employment\":\\[\\],", "");
	    jsonstring = jsonstring.replaceAll("\"photos\":\\[\\],", "");
	    jsonstring = jsonstring.replaceAll("\"disciplines\":\\[\\],", "");
	    jsonstring = jsonstring.replaceAll("\"discipline\":\\{\"subdisciplines\":\\[\\]\\},", "");
	    
	    StringRequestEntity requestEntity = new StringRequestEntity(jsonstring, "application/json", "UTF-8");
	    patch.setRequestEntity(requestEntity);
	    
	    int status = httpclient.executeMethod(patch);
	    
	    String responseBody = new String(patch.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
	    
	    JsonParser parser = new JsonParser();
		JsonElement json = (JsonElement) parser.parse(responseBody);
		
		Profile updateProfile = gson.fromJson(json, Profile.class);
	    
	    return updateProfile;
	}
}
