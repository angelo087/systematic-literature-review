package es.uca.pfc

import mendeley.pfc.services.MendeleyService

class LoginMendeleyController {
	
	def springSecurityService
	def passwordEncoder
	def mendeleyToolService
	
    def index() { 
		redirect(controller: "loginMendeley", action: "auth")
	}
	
	def auth() {
		
		if (springSecurityService.isLoggedIn()) 
		{
			redirect(controller: "index", action: "index")
		}
		else 
		{
			def jUsername = (params.j_username == null ? "" : params.j_username)
			def error     = (params.error == null ? "" : params.error)
			
			if (error != null && !error.equals("")) {
				flash.message = params.error.toString()
			}
			
			[jUsername: jUsername]
		}
	}
	
	def loginMendeley() {
		
		def jUsername = (params.j_username == null ? "" : params.j_username)
		def jPassword = (params.j_password == null ? "" : params.j_password)
		def error = ""
		
		User userInstance = User.findByUsernameIlike(jUsername)
		
		if (userInstance == null)
		{
			error = "Username/Password no correctos."
			redirect(controller: "loginMendeley", action: "auth", params: [j_username: jUsername, error: error])
		}
		else
		{
			MendeleyService mendeleyService = mendeleyToolService.getTokenResponseMendeley(jUsername, jPassword)
			
			if(mendeleyService == null)
			{
				error = "Username/Password no correctos."
				redirect(controller: "loginMendeley", action: "auth", params: [j_username: jUsername, error: error])
			}
			else
			{
				/*if(!passwordEncoder.isPasswordValid(userInstance.password, jPassword, null))
				{*/
					def jPasswordEnc = mendeleyToolService.encodePasswordMendeley(jPassword)				
					userInstance.password = jPassword
					userInstance.save(failOnError: true, flush: true)
					userInstance.userMendeley.email_mend = jUsername
					userInstance.userMendeley.pass_mend = jPasswordEnc
					userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken()
					userInstance.userMendeley.token_type = mendeleyService.getTokenResponse().getTokenType()
					userInstance.userMendeley.expires_in = mendeleyService.getTokenResponse().getExpiresIn()
					userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken()
					userInstance.userMendeley.save(failOnError: true, flush: true)
				/*}*/

				springSecurityService.reauthenticate(userInstance.username, userInstance.password)
				redirect(controller: 'index', action: 'index')
				return
			}
		}		
	}
}
