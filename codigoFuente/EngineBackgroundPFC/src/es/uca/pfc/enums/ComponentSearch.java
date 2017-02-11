package es.uca.pfc.enums;

public enum ComponentSearch {

	FULLTEXT("full-text"), ABSTRACT("abstract"), REVIEW("review"), TITLE("title"), AUTHOR("author"), ANYFIELD("any-field"),
	PUBLISHER("publisher"), ISBN("isbn"), ISSN("issn"), DOI("doi"), KEYWORDS("keywords");
	
	private final String key;
	
	ComponentSearch(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static ComponentSearch fromKey(String key)
	{
		for(ComponentSearch component : ComponentSearch.values()) {
            if(component.getKey().equals(key)) {
                 return component;
            }
       }
       return null;
	}
	
}
