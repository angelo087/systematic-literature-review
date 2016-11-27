package es.pfc.commons;

public class Reference {
	
	private String url;
	private String idMendeley = "";
	private String notesCont = "";
	private TypeEngineSearch typeEngineSearch;
	private String idFolderEngine = "";
	
	public Reference(String url, String idmendeley, String notesCont, TypeEngineSearch typeEngineSearch)
	{
		this.url = url;
		this.idMendeley = idmendeley;
		this.typeEngineSearch = typeEngineSearch;
		this.notesCont = notesCont;
	}
	
	public Reference(String url, String notesCont, TypeEngineSearch typeEngineSearch)
	{
		this.url = url;
		this.idMendeley = "";
		this.typeEngineSearch = typeEngineSearch;
		this.notesCont = notesCont;
	}
	
	public String getIdMendeley() { return idMendeley; }
	public String getUrl() { return url; }
	public TypeEngineSearch getTypeEngineSearch() { return typeEngineSearch; }

	public String getNotesCont() {
		return notesCont;
	}

	public void setNotesCont(String notesCont) {
		this.notesCont = notesCont;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setIdMendeley(String idMendeley) {
		this.idMendeley = idMendeley;
	}

	public void setTypeEngineSearch(TypeEngineSearch typeEngineSearch) {
		this.typeEngineSearch = typeEngineSearch;
	}

	public String getIdFolderEngine() {
		return idFolderEngine;
	}

	public void setIdFolderEngine(String idFolderEngine) {
		this.idFolderEngine = idFolderEngine;
	}
}