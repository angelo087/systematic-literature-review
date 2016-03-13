package es.uca.pfc

import java.util.Date;

class Notification {

    static belongsTo = [profile: UserProfile]
	
	Date fecha = new Date()
	String fechaString = ''
	String asunto = ''
	String texto = ''
	boolean leido = false
	boolean papelera = false

    static constraints = {
    }
	
	static mapping = {
		texto type: 'text'
	}
	
	def toolService
	
	def beforeInsert = {
		fechaString = toolService.getTimeString(fecha)
	}
}
