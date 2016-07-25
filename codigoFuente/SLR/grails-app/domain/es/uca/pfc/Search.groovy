package es.uca.pfc

import java.util.Date;

class Search {

	static belongsTo = [slr: Slr]
	static hasMany = [references: Reference, engines: EngineSearch, termParams: SearchTermParam]
	
	Date fecha = new Date()
	String startYear = ""
	String endYear = ""
	int maxTotal = 5
	String guid = UUID.randomUUID().toString();
	
    static constraints = {
    }
	
	String toString()
	{
		return "${terminos}"
	}
}
