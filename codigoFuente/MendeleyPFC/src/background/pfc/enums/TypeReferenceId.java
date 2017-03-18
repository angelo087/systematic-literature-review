package background.pfc.enums;

public enum TypeReferenceId {
	DOI("doi"), ISBN("isbn"), ISSN("issn"), ARXIV("arxiv"), PMID("pmid");
	
	/** key.*/
	private final String key;
	
	/**
	 * Constructor del enumerado TypeEgnineSearch
	 * @param key
	 */
	TypeReferenceId(String key)
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
	public static TypeReferenceId fromKey(String key)
	{
		for(TypeReferenceId typeId : TypeReferenceId.values()) {
            if(typeId.getKey().equals(key)) {
                 return typeId;
            }
       }
       return null;
	}
}
