package mendeley.pfc.tests;

import mendeley.pfc.services.MendeleyService;

public class MendeleyServiceTest {

	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "FJRrPdYqo08P01rn";
	
	public static void main(String[] args) {
		try
		{
			String email = "alpha_snake087@hotmail.com";
			String pass  = "serpiente2";
			String accessToken = "MSwxNDY1NzQ5MjE4Mjc2LDI0OTc0NDIxLDEwNDQsYWxsLCwsYTIyNy03YzZiYzM1NDU1MTk1MjJjMzcxLWE1OGIyMDY1Zjk1MTRjODMsMDE0Yzg4ZGUtOTI2OS0zN2NkLWJmMDgtMzQxNmJlMDQxNWE5LHprMFNfZC0xVjdnbzd6d3dKRzdNSEt2bXlnVQ";
			String refreshToken = "MSwyNDk3NDQyMSwxMDQ0LGFsbCxhMjI3LTdjNmJjMzU0NTUxOTUyMmMzNzEtYTU4YjIwNjVmOTUxNGM4Myw3MjI3LTdjNmJjMzU0NTUxOTUyMmMzNzEtYTU4YjIwNjVmOTUxNGM4MywxNDY1NzQ1NjE3ODk0LDAxNGM4OGRlLTkyNjktMzdjZC1iZjA4LTM0MTZiZTA0MTVhOSxpa09TekxONzMyWWFZTDBzdk5rWUI0cUNXUk0";
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass, accessToken, refreshToken);
			
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
