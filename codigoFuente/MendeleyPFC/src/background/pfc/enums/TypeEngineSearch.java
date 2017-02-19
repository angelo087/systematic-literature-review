package background.pfc.enums;

/**
 * TypeEgnineSearch es un enumerado que representa los distintos tipos de motores de b�squeda
 * con los que podemos trabajar.
 * 
 * @author agonzatoro
 *
 */
public enum TypeEngineSearch {

	ACM("acm"), IEEE("ieee"), SCIENCE("science"), SPRINGER("springer");
	
	/** key.*/
	private final String key;
	
	/**
	 * Constructor del enumerado TypeEgnineSearch
	 * @param key
	 */
	TypeEngineSearch(String key)
	{
		this.key = key;
	}
	
	/**
	 * M�todo que devuelve el key de un enumerado.
	 * 
	 * @return String
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * M�todo est�tico que devuelve un ComponentSearch a trav�s de su key.
	 * @param key
	 * @return
	 */
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
