package es.uca.pfc

class Criterion {

	static belongsTo = [slr: Slr]
	
	String name
	String description
	String nomenclatura
	Date submitDate = new Date()
	Date modifyDate = new Date()
	
	static constraints = {
		name(size:2..30)
	}
	
	static mapping = {
		description type: 'text'
	}
	
	String toString()
	{
		return name;
	}
	
	def beforeUpdate = {
		modifyDate = new Date()
	}
}
