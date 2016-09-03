package mendeley.pfc.schemas;

import mendeley.pfc.commons.TypeEngine;

public class ReferenceSearch {

	private String url;
	private String idMendeley = "";
	private TypeEngine typeEngine;
	
	public ReferenceSearch(String url, String idmendeley)
	{
		this.url = url;
		this.idMendeley = idmendeley;
		this.typeEngine = TypeEngine.ACM;
	}
	
	public ReferenceSearch(String url, String idmendeley, TypeEngine typeEngine)
	{
		this.url = url;
		this.idMendeley = idmendeley;
		this.typeEngine = typeEngine;
	}
	
	public String getIdMendeley() { return idMendeley; }
	public String getUrl() { return url; }
	public TypeEngine getTypeEngine() { return typeEngine; }
}
