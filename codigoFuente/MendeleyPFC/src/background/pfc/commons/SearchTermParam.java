package background.pfc.commons;

import background.pfc.enums.ComponentSearch;
import background.pfc.enums.OperatorSearch;

/**
 * SearchTermParam es la clase que recoge los t�rminos de b�squeda de las referencias a importar
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
	 * M�todo que devuelve el ComponentSearch asociado al t�rmino de b�squeda.
	 * 
	 * @return ComponentSearch
	 */
	public ComponentSearch getComponentSearch() {
		return componentSearch;
	}

	/**
	 * M�todo que establece el ComponentSearch al t�rmino de b�squeda.
	 * 
	 * @param componentSearch ComponentSearch
	 */
	public void setComponentSearch(ComponentSearch componentSearch) {
		this.componentSearch = componentSearch;
	}

	/**
	 * M�todo que devuelve el OperatorSearch asociado al t�rmino de b�squeda.
	 * 
	 * @return OperatorSearch
	 */
	public OperatorSearch getOperatorSearch() {
		return operatorSearch;
	}

	/**
	 * M�todo que establece el OperatorSearch asociado al t�rmino de b�squeda.
	 * 
	 * @param operatorSearch OperatorSearch
	 */
	public void setOperatorSearch(OperatorSearch operatorSearch) {
		this.operatorSearch = operatorSearch;
	}

	/**
	 * M�todo que devuelve los t�rminos de b�squeda.
	 * 
	 * @return String
	 */
	public String getTerminos() {
		return terminos;
	}

	/**
	 * M�todo que establece los t�rminos de b�squeda.
	 * 
	 * @param terminos String
	 */
	public void setTerminos(String terminos) {
		this.terminos = terminos;
	}
}
