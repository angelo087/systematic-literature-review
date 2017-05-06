package es.uca.pfc

class TaskSearch {
	
	int percentage = 0
	String state = "Buscando referencias..."
	Date submitDate = new Date()
	Date endDate = new Date()
	boolean hasErrors = false
	String titleSlr
	String guidSlr
	String guid
	String strException
	String username

    static constraints = {
		strException(nullable: true)
    }
	
	// Indicamos que strException es un texto
	static mapping = {
		strException type: 'text'
	}
}
