package es.uca.pfc

import java.util.Date;

class Search {

	static belongsTo = [slr: Slr, engine: EngineSearch, component: SearchComponent, operator: SearchOperator]
	static hasMany = [references: Reference]
	
	String terminos = ""
	//Engine engine
	Date fecha = new Date()
	//String operator = ""
	String startYear = ""
	String endYear = ""
	//String components = ""
	int maxTotal = 0
	String guid = UUID.randomUUID().toString();
	
    static constraints = {
    }
	
	String toString()
	{
		return "${terminos}"
	}
	
	String getStrEngineSearch()
	{
		return engine.display_name;
	}
	
	String getStSearchComponent()
	{
		return component.name;
	}
	
	String getStrSearchOperator()
	{
		return operator.name;
	}
}
