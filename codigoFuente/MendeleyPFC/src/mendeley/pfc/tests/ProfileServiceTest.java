package mendeley.pfc.tests;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.Profile;
import mendeley.pfc.services.MendeleyService;
import mendeley.pfc.services.ProfileService;

public class ProfileServiceTest {

	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "FJRrPdYqo08P01rn";

	public static void main(String[] args)
	{
		//String email = "alpha_snake087@hotmail.com";
		//String pass  = "serpiente2";
		String email = "angel.gonzatoro@gmail.com";
		String pass  = "angel.gonzatoro";
		String accessToken = "";//"MSwxNDY2MDE4ODAzNzY1LDI0OTc0NDIxLDEwNDQsYWxsLCwsNWU2My01MmRkNTQ1NTU1MTgyYWQzYzk1NTUxMTQ1NjhlNjk1NmU0LDAxNGM4OGRlLTkyNjktMzdjZC1iZjA4LTM0MTZiZTA0MTVhOSxTMzhiLVZjSko5SXFncV9IMUlLOXhaYzZ0U2M";
		String refreshToken = "";//"MSwyNDk3NDQyMSwxMDQ0LGFsbCw1ZTYzLTUyZGQ1NDU1NTUxODJhZDNjOTU1NTExNDU2OGU2OTU2ZTQsNzIyNy03YzZiYzM1NDU1MTk1MjJjMzcxLWE1OGIyMDY1Zjk1MTRjODMsMTQ2NjAxNTIwMzc2MywwMTRjODhkZS05MjY5LTM3Y2QtYmYwOC0zNDE2YmUwNDE1YTksQkhNdi1iWnA3cWlXd2NuZDhrUEVZRUpjVlo0";
		
		try 
		{
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass, accessToken, refreshToken);
			ProfileService profileService = new ProfileService(mendeleyService);
			
			Profile profile = profileService.getCurrentProfile();
			
			System.out.println(profile);
		}
		catch (FailingHttpStatusCodeException | IOException
				| MendeleyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
