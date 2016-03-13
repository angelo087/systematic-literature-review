package es.uca.pfc

class TypeDocument {
	
	static hasMany = [references: Reference]
	
	String nombre
	String nomenclatura

    static constraints = {
    }
}
