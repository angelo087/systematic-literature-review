package mendeley.pfc.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Random;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.schemas.TokenResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;

public class MendeleyService {
	
	private String clientId;
	private String clientSecret;
	private String redirectUri;	
	private TokenResponse tokenResponse = new TokenResponse();	
	private String email_mend;
	private String pass_mend;	
	private static String templateUriAuthorize = "https://api.mendeley.com/oauth/authorize?response_type=code&client_id=@CLIENT_ID@&scope=all&redirect_uri=@REDIRECT_URI@&state=@STATE@";
	
	/**
	 * Constructor de la clase MendeleyService
	 * 
	 * @param clientId
	 * @param clientSecret
	 * @param redirectUri
	 * @param email
	 * @param pass
	 * @throws Exception
	 */
	public MendeleyService(String clientId, String clientSecret, String redirectUri, String email, String pass) throws Exception
	{
		this.clientId     = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri  = redirectUri;
		this.email_mend   = email;
		this.pass_mend    = pass;
		
		getTokens();
	}
	
	/**
	 * Constructor de la clase MendeleyService
	 * 
	 * @param clientId
	 * @param clientSecret
	 * @param redirectUri
	 * @param email
	 * @param pass
	 * @param accessToken
	 * @param refreshToken
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws MendeleyException
	 * @throws HttpException 
	 */
	public MendeleyService(String clientId, String clientSecret, String redirectUri, String email, String pass, String accessToken, String refreshToken) throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException, HttpException
	{
		this.clientId     = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri  = redirectUri;
		this.email_mend   = email;
		this.pass_mend    = pass;
		
		this.tokenResponse.setAcessToken(accessToken);
		this.tokenResponse.setRefreshToken(refreshToken);
		this.tokenResponse.setExpiresIn("3600");
		this.tokenResponse.setTokenType("bearer");
		
		try
		{
			// Refrescamos tokens, en caso de excepción, volvemos a pedir los tokens por autorizacion.
			refreshTokens();
		}
		catch(MendeleyException ex)
		{
			getTokens();
		}
	}

	/**
	 * Constructor de la clase MendeleyService
	 * 
	 * @param clientId
	 * @param clientSecret
	 * @param redirectUri
	 * @param email
	 * @param pass
	 * @param accessToken
	 * @param refreshToken
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws MendeleyException
	 * @throws HttpException 
	 */
	public MendeleyService(String clientId, String clientSecret, String redirectUri, String email, String pass, TokenResponse tokenResponse) throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException, HttpException
	{
		this.clientId     = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri  = redirectUri;
		this.email_mend   = email;
		this.pass_mend    = pass;
		
		this.tokenResponse = tokenResponse;
		
		try
		{
			// Refrescamos tokens, en caso de excepción, volvemos a pedir los tokens por autorizacion.
			refreshTokens();
		}
		catch(MendeleyException ex)
		{
			getTokens();
		}
	}

	/**
	 * Método privado que obtiene los tokens necesarios para la conexión con Mendeley.
	 * 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 * @throws MendeleyException 
	 * @throws Exception 
	 */
	private void getTokens() throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException
	{
		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.html.HtmlScript").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.WindowProxy").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.http").setLevel(java.util.logging.Level.OFF);
		
		Random rnd = new Random();
		double state = rnd.nextDouble() * 100000;
		String uriAuthorize = templateUriAuthorize;
		
		uriAuthorize = uriAuthorize.replaceAll("@CLIENT_ID@", clientId)
								   .replaceAll("@REDIRECT_URI@", URLEncoder.encode(redirectUri,"UTF-8"))
								   .replaceAll("@STATE@", Double.toString(state));
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
		
		//Opciones para la conexion
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(true);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.waitForBackgroundJavaScript(10000);
		
		HtmlPage currentPage = webClient.getPage(uriAuthorize);
		HtmlForm form = (HtmlForm) currentPage.getElementById("login");
		form.getInputByName("username").setValueAttribute(email_mend);
		form.getInputByName("password").setValueAttribute(pass_mend);

		HtmlButton button = (HtmlButton) currentPage.getElementsByTagName("button").get(0);
		
		currentPage = (HtmlPage) button.click();
		webClient.waitForBackgroundJavaScript(5000);
		
		uriAuthorize = currentPage.getUrl().toString();
		
		String[] url = currentPage.getUrl().toString().split("&");
		String code = "";
		
		for(String p : url)
		{
			if(p.contains("code="))
			{
				code = p.split("\\=")[1].trim();
			}
		}
				
		if(code.equals(""))
		{
			throw new MendeleyException("MendeleyService.getTokens() -> Code empty.");
		}
		
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod("https://api.mendeley.com/oauth/token");
		
		byte[] encodedBytes = Base64.encodeBase64((clientId+":"+clientSecret).getBytes());
		String encoding = new String(encodedBytes);
		
		post.addParameter("grant_type", "authorization_code");
		post.addParameter("code", code);
		post.addParameter("redirect_uri", redirectUri);
		post.addRequestHeader("Content-type", "application/x-www-form-urlencoded");
		post.addRequestHeader("Authorization", "Basic "+encoding);
		
		int status = httpclient.executeMethod(post);
				
		if(status != 200)
		{
			throw new MendeleyException("MendeleyService.getTokens() -> status = ." + status);
		}
		
		Gson gson = new Gson();
		
		this.tokenResponse = gson.fromJson(post.getResponseBodyAsString(), TokenResponse.class);
	}
	
	/**
	 * Método privado que refresca los tokens.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws MendeleyException
	 */
	private void refreshTokens() throws HttpException, IOException, MendeleyException
	{
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod("https://api-oauth2.mendeley.com/oauth/token");
		
		byte[] encodedBytes = Base64.encodeBase64((clientId+":"+clientSecret).getBytes());
	    String encoding = new String(encodedBytes);
	    
	    post.addParameter("grant_type","refresh_token");
	    post.addParameter("refresh_token",tokenResponse.getRefreshToken());
	    post.addParameter("redirect_uri",redirectUri);
	    post.addRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    post.addRequestHeader("Authorization", "Basic "+encoding);
	    
	    int status = httpclient.executeMethod(post);
	    
	    if (status != 200)
	    {
	    	throw new MendeleyException("MendeleyService.refreshTokens() -> Status = ." + status);
	    }

	    Gson gson = new Gson();
	    
	    this.tokenResponse = gson.fromJson(post.getResponseBodyAsString(), TokenResponse.class);
	}

	/**
	 * Método que devuelve el id de la app.
	 * @return
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Método que inserta el id de la app.
	 * @param clientId
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * Método que obtiene la contraseña del cliente
	 * 
	 * @return contraseña del cliente
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Método que establece la contraseña del cliente
	 * 
	 * @param clientSecret
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * Método que devuelve la redirección URI de la app.
	 * 
	 * @return direccion URI en formato String de la app.
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * Método que establece la dirección URI (formato cadena) de la app.
	 * 
	 * @param redirectUri
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	/**
	 * Método que devuelve los tokens de la app.
	 * 
	 * @return tokens de la app
	 */
	public TokenResponse getTokenResponse() { return tokenResponse; }
	
	/**
	 * Método que establece los tokens de la app.
	 * 
	 * @param tokenResponse
	 */
	public void setTokenResponse(TokenResponse tokenResponse) {
		this.tokenResponse = tokenResponse;
	}

	/**
	 * Método que devuelve el email del usuario que realiza la petición
	 * de tokens
	 * 
	 * @return email de usuario
	 */
	public String getEmailMend() {
		return email_mend;
	}

	/**
	 * Método que establece el email de usuario que realiza la petición
	 * de tokens.
	 * 
	 * @param email_mend
	 */
	public void setEmailMend(String email_mend) {
		this.email_mend = email_mend;
	}

	/**
	 * Método que devuelve el pass del usuario que realiza la acción.
	 * 
	 * @return pass del usuario.
	 */
	public String getPassMend() {
		return pass_mend;
	}

	/**
	 * Método que establece el pass del usuario
	 * 
	 * @param pass_mend
	 */
	public void setPassMend(String pass_mend) {
		this.pass_mend = pass_mend;
	}
}
