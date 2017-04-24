package es.uca.pfc

class RegisterController {

	def springSecurityService
	def mendeleyToolService
	def toolService
	
    def index() { 
		
		if(springSecurityService.isLoggedIn())
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def emailMend = (params.emailMend == null ? "" : params.emailMend.toString())
		
			[emailMend: emailMend]
		}
	}
		
	def registerUser() {
		
		log.info 'Registramos usuario'
		def emailMend    = (params.j_email_mend == null ? "" : params.j_email_mend.toString())
		def passMend     = (params.j_pass_mend == null ? "" : params.j_pass_mend.toString())
		def passMendRep  = (params.j_pass_mend_rep == null ? "" : params.j_pass_mend_rep.toString())
		
		def error = ""
		
		def userRepeatInstance = User.findByUsernameLike(emailMend)
		
		if (emailMend.equals("") || passMend.equals("") || passMendRep.equals("")) {
			error = "Error: Ningún campo puede estar vacío."
		}
		else if(!toolService.isValidEmail(emailMend)) {
			error = "Error: Email inválido."
		}
		else if(!passMend.equals(passMendRep)) {
			error = "Error: Las contraseñas no coinciden."
		}
		else if (userRepeatInstance != null) {
			error = "Ese usuario ya se encuentra registrado."
		}
		
		if(error.equals(""))
		{
			if(!mendeleyToolService.isRegisteredMendeley(emailMend, passMend))
			{
				error = "Error: Login incorrecto en Mendeley. Compruebe sus credenciales o inténtelo de nuevo."
			}
		}
		
		if(!error.equals("")) {
			log.info "Validacion incorrecta en el registro de usuarios"
			flash.message = error
			redirect(controller: 'register', action: 'index', params: [emailMend: emailMend])
		}
		else
		{
			User userInstance = mendeleyToolService.getUserFromMendeley(emailMend, passMend)
			
			if (!userInstance.validate())
			{
				userInstance.errors.each {
					println "error => " + it
				}
			}
		
			if(userInstance == null || !userInstance.validate())
			{
				flash.message = "Error: Ha habido problemas para hacer login. Inténtelo más tarde."
				redirect(controller: 'register', action: 'index', params: [emailMend: emailMend])
			}
			else
			{
				userInstance.save flush: true
				
				// Creamos role
				def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
				
				if (!userInstance.authorities.contains(userRole)) {
					println "Creamos el role"
					UserRole.create userInstance, userRole
				}
				
				userInstance.save(failOnError: true, flush: true)
				
				log.info "Usuario registrado: " + userInstance.username + "=>" + userInstance.authorities
				
				// Hacemos Login automaticamente
				springSecurityService.reauthenticate(userInstance.username, userInstance.password)
				redirect(controller: 'index', action: 'index')
				
				return
			}
		}
	}
}
