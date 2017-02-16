package es.uca.pfc

class ApiKeyEngine {

	String engine
	String apiKey
	
    static constraints = {
		apiKey(blank:true, nullable: true)
    }
}
