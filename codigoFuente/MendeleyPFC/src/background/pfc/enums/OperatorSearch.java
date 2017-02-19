package background.pfc.enums;

/**
 * OperatorSearch es un enumerado que representa las diferentes operaciones que pueden existir.
 * 
 * @author agonzatoro
 *
 */
public enum OperatorSearch {

	ALL("all"), ANY("any"), NONE("none");
	
	/** key.*/
	private final String key;
	
	/**
	 * Constructor del enumerado OperatorSearch.
	 * 
	 * @param key String
	 */
	OperatorSearch(String key)
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
