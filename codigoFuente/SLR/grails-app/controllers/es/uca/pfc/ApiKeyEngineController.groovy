package es.uca.pfc

import grails.transaction.Transactional;

class ApiKeyEngineController {

    def index() 
	{ 
		def updateOk = null
		if(params.updateOk != null)
		{
			updateOk = params.updateOk
		}
		
		[apiKeyEngineListInstance: ApiKeyEngine.list(), updateOk: updateOk]
	}
	
	@Transactional
	def save()
	{
		println "ApiKeyEngineController => save()"
		for(ApiKeyEngine api : ApiKeyEngine.list())
		{
			String input = "input"+api.engine
			if(params.containsKey(input))
			{
				String value = (String)params.get(input);
				api.apiKey = value;
				api.save(failOnError: true)
			}
		}

		redirect(controller: 'apiKeyEngine', action: 'index', params: [updateOk: true])
	}
}
