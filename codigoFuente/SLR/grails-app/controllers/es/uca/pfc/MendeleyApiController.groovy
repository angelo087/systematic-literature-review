package es.uca.pfc

import grails.transaction.Transactional;

class MendeleyApiController {
	
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

	def springSecurityService
	
    def index() 
	{ 
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def userInstance = User.get(springSecurityService.principal.id)
			if(userInstance.authorities.any { it.authority != "ROLE_USER" })
			{
				def updateOk = null
				if(params.updateOk != null)
				{
					updateOk = params.updateOk
				}
				
				[mendeleyApiInstance: MendeleyApi.list().first(), updateOk: updateOk]
			}
			else
			{
				redirect(controller: 'index', action: 'index')
			}
		}
	}
	
	@Transactional
	def save()
	{
		def isLogin = springSecurityService.loggedIn
		
		if (!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def userInstance = User.get(springSecurityService.principal.id)
			def updateOk = false;
			
			if(userInstance.authorities.any { it.authority != "ROLE_USER" })
			{
				def mendeleyApi = MendeleyApi.list().first()
				
				mendeleyApi.clientId = (params.inputClientId == null ? "" : params.inputClientId)
				mendeleyApi.clientSecret = (params.inputClientSecret == null ? "" : params.inputClientSecret)
				mendeleyApi.redirectUri = (params.inputRedirectUri == null ? "" : params.inputRedirectUri)
				mendeleyApi.totalHilos = (params.inputTotalHilos == null ? 2 : Integer.parseInt(params.inputTotalHilos.toString()))
				mendeleyApi.totalTries = (params.inputTotalTries == null ? 1 : Integer.parseInt(params.inputTotalTries.toString()))
				mendeleyApi.save(failOnError: true)
				
				updateOk = true;
			}
			
			redirect(controller: 'mendeleyApi', action: 'index', params: [updateOk: updateOk])
		}
	}
}
