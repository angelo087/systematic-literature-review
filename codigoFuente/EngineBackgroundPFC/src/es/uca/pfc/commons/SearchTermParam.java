package es.uca.pfc.commons;

import es.uca.pfc.enums.ComponentSearch;
import es.uca.pfc.enums.OperatorSearch;

public class SearchTermParam {
	
	private ComponentSearch componentSearch;
	private OperatorSearch operatorSearch;
	private String terminos;
	
	public SearchTermParam()
	{
		this.componentSearch = ComponentSearch.ANYFIELD;
		this.operatorSearch = OperatorSearch.ALL;
		this.terminos = "";
	}
	
	public SearchTermParam(ComponentSearch componentSearch, OperatorSearch operatorSearch, String terminos)
	{
		this.componentSearch = componentSearch;
		this.operatorSearch = operatorSearch;
		this.terminos = terminos;
	}

	public ComponentSearch getComponentSearch() {
		return componentSearch;
	}

	public void setComponentSearch(ComponentSearch componentSearch) {
		this.componentSearch = componentSearch;
	}

	public OperatorSearch getOperatorSearch() {
		return operatorSearch;
	}

	public void setOperatorSearch(OperatorSearch operatorSearch) {
		this.operatorSearch = operatorSearch;
	}

	public String getTerminos() {
		return terminos;
	}

	public void setTerminos(String terminos) {
		this.terminos = terminos;
	}
}
