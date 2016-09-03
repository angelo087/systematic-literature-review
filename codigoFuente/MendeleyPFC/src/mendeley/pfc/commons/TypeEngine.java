package mendeley.pfc.commons;

public enum TypeEngine {
	
	ACM("acm"), IEEE("ieee"), SCIENCE("science"), SPRINGER("springer");
	
	private final String key;
	
	TypeEngine(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static TypeEngine fromKey(String key)
	{
		for(TypeEngine type : TypeEngine.values()) {
            if(type.getKey().equals(key)) {
                 return type;
            }
       }
       return null;
	}
	
}
