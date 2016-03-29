package es.uca.pfc

import java.util.Date;

class Slr {
	
	static belongsTo = [userProfile: UserProfile]
	static hasMany = [searchs: Search, criterions: Criterion, questions: ResearchQuestion, specAttributes: SpecificAttribute]
	
	String title
	String justification
	String state = 'fase1'
	boolean noDrop = false
	int numVisits = 0
	Date submitDate = new Date()	// fecha creación
	Date lastModified = new Date()	// fecha modificación
	String guid = UUID.randomUUID().toString();
	String idmend = "" 				//id de folder en mendeley
	int totalReferences = 0
	//String formatTitle
	
	static constraints = {
		state(inList:['fase1','fase2','fase3'], display: false, blank: false)
		noDrop(display: false)
		numVisits(display: false)
		submitDate(display: false)
	}
	
	// Sobrecarga método toString()
	String toString()
	{
		return "${titulo}";
	}
	
	// Indicamos que la justificacion es un texto
	static mapping = {
		justification type: 'text'
	}
	
	def beforeUpdate = {
		// Actualizamos la fecha de modificacion
		lastModified = new Date()
		
		// Actualizamos el estado
		// ...
	}
	
	def beforeInsert = {
		// Creamos un criterio "Included"
		addToCriterions(new Criterion(name: "included", description: "Referencia incluida en el estudio.", nomenclatura: "cr_included"))
		//formatTitle = URLEncoder.encode(title, "UTF-8")
	}
	
	def afterInsert = {
		// Insertamos un logger al usuario
		userProfile.addToLoggers(new LoggerSlr(slr: this, tipo: 'crear')).save(failOnError: true)
	}
}
