package es.uca.pfc

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

class EngineSearchController {

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
				
				def engineSearchListInstance = EngineSearch.findAllByNameNotEqual('OTHER', [sort: 'name', order: 'asc'])
				
				[engineSearchListInstance: engineSearchListInstance, updateOk: updateOk]
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
				for(EngineSearch engine : EngineSearch.list())
				{
					String input = "input"+engine.name
					if(params.containsKey(input))
					{
						String value = (String)params.get(input);
						engine.apiKey = value;
						engine.save(failOnError: true)
					}
				}
				updateOk = true;
			}
	
			redirect(controller: 'engineSearch', action: 'index', params: [updateOk: updateOk])
		}
	}
}
