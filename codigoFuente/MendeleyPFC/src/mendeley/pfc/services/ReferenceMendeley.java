package mendeley.pfc.services;

public class ReferenceMendeley {

	private String url;
	private String idMendeley = "";
	
	public ReferenceMendeley(String url, String idmendeley)
	{
		this.url = url;
		this.idMendeley = idmendeley;
	}
	
	public String getIdMendeley() { return idMendeley; }
	public String getUrl() { return url; }
	
}
