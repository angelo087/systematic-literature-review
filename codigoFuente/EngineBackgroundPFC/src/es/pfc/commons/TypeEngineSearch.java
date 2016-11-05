package es.pfc.commons;

public enum TypeEngineSearch {

	ACM("acm"), IEEE("ieee"), SCIENCE("science"), SPRINGER("springer");
	
	private final String key;
	
	TypeEngineSearch(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static TypeEngineSearch fromKey(String key)
	{
		for(TypeEngineSearch engine : TypeEngineSearch.values()) {
            if(engine.getKey().equals(key)) {
                 return engine;
            }
       }
       return null;
	}
	
}
