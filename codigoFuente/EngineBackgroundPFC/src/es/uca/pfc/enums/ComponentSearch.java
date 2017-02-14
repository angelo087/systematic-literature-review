package es.uca.pfc.enums;

/**
 * ComponentSearch es un enumerado para representar los diferentes tipos de parámetros que existen.
 * 
 * @author agonzatoro
 *
 */
public enum ComponentSearch {

	FULLTEXT("full-text"), ABSTRACT("abstract"), REVIEW("review"), TITLE("title"), AUTHOR("author"), ANYFIELD("any-field"),
	PUBLISHER("publisher"), ISBN("isbn"), ISSN("issn"), DOI("doi"), KEYWORDS("keywords");
	
	/** key.*/
	private final String key;
	
	/**
	 * Constructor del enumerado ComponentSearch.
	 * 
	 * @param key String
	 */
	ComponentSearch(String key)
	{
		this.key = key;
	}
	
	/**
	 * Método que devuelve el key de un enumerado.
	 * 
	 * @return String
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Método estático que devuelve un ComponentSearch a través de su key.
	 * @param key
	 * @return
	 */
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
