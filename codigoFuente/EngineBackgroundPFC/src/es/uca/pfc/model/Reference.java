package es.uca.pfc.model;

public class Reference {
	
	private String url;
	private String idMendeley = "";
	
	public Reference(String url, String idmendeley)
	{
		this.url = url;
		this.idMendeley = idmendeley;
	}
	
	public String getIdMendeley() { return idMendeley; }
	public String getUrl() { return url; }
	
}
