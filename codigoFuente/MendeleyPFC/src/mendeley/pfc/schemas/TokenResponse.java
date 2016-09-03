package mendeley.pfc.schemas;

public class TokenResponse 
{
	private String access_token = "";
	private String token_type = "";
	private String expires_in = "";
	private String refresh_token = "";
	
	public String getAccessToken() { return access_token; }
	
	public String getTokenType() { return token_type; }
	
	public String getExpiresIn() { return expires_in; }
	
	public String getRefreshToken() { return refresh_token; }
	
	public void setAcessToken(String at)
	{
		this.access_token = at;
	}
	
	public void setTokenType(String tp)
	{
		this.token_type = tp;
	}
	
	public void setExpiresIn(String ei)
	{
		this.expires_in = ei;
	}
	
	public void setRefreshToken(String rt)
	{
		this.refresh_token = rt;
	}
	
	@Override
	public String toString()
	{
		return "[Access Token: '"+access_token+"', Refresh Token: '"+refresh_token+
				"', Token Type: '"+token_type+"', Expires In: '"+expires_in+"']";
	}
}
