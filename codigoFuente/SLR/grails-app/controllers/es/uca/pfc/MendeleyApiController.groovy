package es.uca.pfc

import grails.transaction.Transactional;

class MendeleyApiController {

    def index() 
	{ 
		def updateOk = null
		if(params.updateOk != null)
		{
			updateOk = params.updateOk
		}
		
		[mendeleyApiInstance: MendeleyApi.list().first(), updateOk: updateOk]
	}
	
	@Transactional
	def save()
	{
		def mendeleyApi = MendeleyApi.list().first()
		
		mendeleyApi.clientId = (params.inputClientId == null ? "" : params.inputClientId)
		mendeleyApi.clientSecret = (params.inputClientSecret == null ? "" : params.inputClientSecret)
		mendeleyApi.redirectUri = (params.inputRedirectUri == null ? "" : params.inputRedirectUri)
		mendeleyApi.totalHilos = (params.inputTotalHilos == null ? 2 : Integer.parseInt(params.inputTotalHilos.toString()))
		mendeleyApi.totalTries = (params.inputTotalTries == null ? 1 : Integer.parseInt(params.inputTotalTries.toString()))
		mendeleyApi.save(failOnError: true)
		
		redirect(controller: 'mendeleyApi', action: 'index', params: [updateOk: true])
	}
}
