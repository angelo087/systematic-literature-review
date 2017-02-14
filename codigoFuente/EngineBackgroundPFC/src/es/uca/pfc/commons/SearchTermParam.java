package es.uca.pfc.commons;

import es.uca.pfc.enums.ComponentSearch;
import es.uca.pfc.enums.OperatorSearch;

/**
 * SearchTermParam es la clase que recoge los términos de búsqueda de las referencias a importar
 * en Mendeley.
 * 
 * @author agonzatoro
 *
 */
public class SearchTermParam {
	
	/** componentSearch.*/
	private ComponentSearch componentSearch;
	/** operatorSearch.*/
	private OperatorSearch operatorSearch;
	/** terminos.*/
	private String terminos;
	
	/**
	 * Constructor de la clase SearchTermparam.
	 */
	public SearchTermParam()
	{
		this.componentSearch = ComponentSearch.ANYFIELD;
		this.operatorSearch = OperatorSearch.ALL;
		this.terminos = "";
	}
	
	/**
	 * Constructor de la clase SearchTermParam.
	 * 
	 * @param componentSearch ComponentSearch
	 * @param operatorSearch OperatorSearch
	 * @param terminos String
	 */
	public SearchTermParam(ComponentSearch componentSearch, OperatorSearch operatorSearch, String terminos)
	{
		this.componentSearch = componentSearch;
		this.operatorSearch = operatorSearch;
		this.terminos = terminos;
	}

	/**
	 * Método que devuelve el ComponentSearch asociado al término de búsqueda.
	 * 
	 * @return ComponentSearch
	 */
	public ComponentSearch getComponentSearch() {
		return componentSearch;
	}

	/**
	 * Método que establece el ComponentSearch al término de búsqueda.
	 * 
	 * @param componentSearch ComponentSearch
	 */
	public void setComponentSearch(ComponentSearch componentSearch) {
		this.componentSearch = componentSearch;
	}

	/**
	 * Método que devuelve el OperatorSearch asociado al término de búsqueda.
	 * 
	 * @return OperatorSearch
	 */
	public OperatorSearch getOperatorSearch() {
		return operatorSearch;
	}

	/**
	 * Método que establece el OperatorSearch asociado al término de búsqueda.
	 * 
	 * @param operatorSearch OperatorSearch
	 */
	public void setOperatorSearch(OperatorSearch operatorSearch) {
		this.operatorSearch = operatorSearch;
	}

	/**
	 * Método que devuelve los términos de búsqueda.
	 * 
	 * @return String
	 */
	public String getTerminos() {
		return terminos;
	}

	/**
	 * Método que establece los términos de búsqueda.
	 * 
	 * @param terminos String
	 */
	public void setTerminos(String terminos) {
		this.terminos = terminos;
	}
}
