package mendeley.pfc.tests;

import mendeley.pfc.services.MendeleyService;

public class MendeleyServiceTest {

	public static final String APP_ID = "1044";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "5qQ6zm5iYpvUehj4";
	
	public static void main(String[] args) {
		try
		{
			String email = "angel.gonzatoro@gmail.com";
			String pass  = "angel.gonzatoro";
			String accessToken = "asdasdasdsad";
			String refreshToken = "asdadasdsadaaaaa";
			
			//MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass, accessToken, refreshToken);
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass);
			
			System.out.println("ACCESS TOKEN: " + mendeleyService.getTokenResponse().getAccessToken());
			System.out.println("EXPIRES IN: " + mendeleyService.getTokenResponse().getExpiresIn());
			System.out.println("REFRESH TOKEN: " + mendeleyService.getTokenResponse().getRefreshToken());
			System.out.println("TOKEN TYPE: " + mendeleyService.getTokenResponse().getTokenType());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
