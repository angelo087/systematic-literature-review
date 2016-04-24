package es.uca.pfc

class SpecificAttributeMultipleValue extends SpecificAttribute {
	
	static hasMany = [options: String]
	
	String optionDefault = ""
		
    static constraints = {
    }
	
	def beforeInsert = {
		tipo = "list"
	}
	
	String getStrOptions()
	{
		return options.toString();
	}
}
