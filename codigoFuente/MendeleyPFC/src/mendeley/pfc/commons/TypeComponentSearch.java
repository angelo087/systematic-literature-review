package mendeley.pfc.commons;

public enum TypeComponentSearch {

	ANY("Any Field"), TITLE("Title"), AUTHOR("Author"), ABSTRACT("Abstract"), FULLTEXT("Full-text"), PUBLISHER("Publisher"),
	ISBN("ISBN"), ISSN("ISSN"), DOI("DOI"), KEYWORDS("Keywords");
	
	private String key;
	
	TypeComponentSearch(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static TypeComponentSearch fromKey(String key)
	{
		for(TypeComponentSearch type : TypeComponentSearch.values()) {
            if(type.getKey().equals(key)) {
                 return type;
            }
       }
       return null;
	}
}
