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

    static constraints = {
    }
}
