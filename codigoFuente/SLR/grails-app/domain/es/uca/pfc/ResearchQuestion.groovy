package es.uca.pfc

class ResearchQuestion {
	
	static belongsTo = [slr: Slr]

	String enunciado
	Date submitDate = new Date()
	
    static constraints = {
    }
}
