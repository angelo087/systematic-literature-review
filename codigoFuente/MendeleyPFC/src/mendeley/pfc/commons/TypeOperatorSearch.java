package mendeley.pfc.commons;

public enum TypeOperatorSearch {
	
	ALL("ALL"), ANY("ANY"), NOT("NOT");

	private String key;
	
	TypeOperatorSearch(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static TypeOperatorSearch fromKey(String key)
	{
		for(TypeOperatorSearch type : TypeOperatorSearch.values()) {
            if(type.getKey().equals(key)) {
                 return type;
            }
       }
       return null;
	}
	

}
