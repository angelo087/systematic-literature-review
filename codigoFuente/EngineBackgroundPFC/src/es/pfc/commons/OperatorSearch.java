package es.pfc.commons;

public enum OperatorSearch {

	ALL("all"), ANY("any"), NONE("none");
	
	private final String key;
	
	OperatorSearch(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static OperatorSearch fromKey(String key)
	{
		for(OperatorSearch operator : OperatorSearch.values()) {
            if(operator.getKey().equals(key)) {
                 return operator;
            }
       }
       return null;
	}
	
}
