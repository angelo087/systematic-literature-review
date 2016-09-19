package es.uca.pfc

import grails.transaction.Transactional;

import java.util.Date;

class SlrController {
	
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
	def mendeleyToolService
	def exportService
	def graphService
	def strSearch = ""
	int maxPerPage = 10
	
    def index() 
	{ 
		redirect(controller: 'slr', action: 'myList')
	}
	
	@Transactional
	def myList()
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			if(params.guidNotif != null)
			{
				def notification = Notification.findByGuidLike(params.guidNotif.toString())
				notification.leido = true;
				notification.save(failOnError: true, flush: true)
			}
			
			// Comprobamos si se va a crear un nuevo SLR.
			def error = ""
			def errorCriterion = ""
			def errorAttribute = ""
			def errorQuestion = ""
			def tituloSlr = ""
			def justificacionSlr = ""
			def success = false
			def successCriterion = false
			def successAttribute = false
			def successQuestion = false
			def nombreCriterion = ""
			def descripcionCriterion = ""
			def guidSlrError = "0"
			def nombreAttribute = ""
			def opcionesAttribute = ""
			def tipoAttribute = "string"
			def enunciadoQuestion = ""
			
			if(null != params.success)
			{
				success = true
			}
			
			if(params.successCriterion.toString().equals("true"))
			{
				successCriterion = true
			}
			
			if(params.successAttribute.toString().equals("true"))
			{
				successAttribute = true
			}
			
			if(params.successQuestion.toString().equals("true"))
			{
				successQuestion = true
			}
			
			if(!(null == params.error || params.error.equals(null)))
			{
				error = params.error.toString()
			}
			
			if(!(null == params.errorCriterion || params.errorCriterion.equals(null)))
			{
				errorCriterion = params.errorCriterion.toString()
			}
			
			if(!(null == params.errorAttribute || params.errorAttribute.equals(null)))
			{
				errorAttribute = params.errorAttribute.toString()
			}
			
			if(!(null == params.errorQuestion || params.errorQuestion.equals(null)))
			{
				errorQuestion = params.errorQuestion.toString()
			}
			
			if(!(params.tituloSlr == null || params.tituloSlr.equals(null)))
			{
				tituloSlr = params.tituloSlr.toString()
			}
			
			if(!(params.justificacionSlr == null || params.justificacionSlr.equals(null)))
			{
				justificacionSlr = params.justificacionSlr.toString()
			}
			
			if(!(params.nombreCriterion == null || params.nombreCriterion.equals(null) || successCriterion))
			{
				nombreCriterion = params.nombreCriterion.toString()
			}
			
			if(!(params.descripcionCriterion == null || params.descripcionCriterion.equals(null) || successCriterion))
			{
				descripcionCriterion = params.descripcionCriterion.toString()
			}
			
			if(!(params.guidSlrError == null || params.guidSlrError.equals(null)))
			{
				guidSlrError = params.guidSlrError.toString()
			}
			
			if(!(params.nombreAttribute == null || params.nombreAttribute.equals(null) || successAttribute))
			{
				nombreAttribute = params.nombreAttribute.toString()
			}
			
			if(!(params.opcionesAttribute == null || params.opcionesAttribute.equals(null) || successAttribute))
			{
				opcionesAttribute = params.opcionesAttribute.toString()
			}
			
			if(!(params.tipoAttribute == null || params.tipoAttribute.equals(null) || successAttribute))
			{
				tipoAttribute = params.tipoAttribute.toString()
			}
			
			if(!(params.enunciadoQuestion == null || params.enunciadoQuestion.equals(null) || successQuestion))
			{
				enunciadoQuestion = params.enunciadoQuestion.toString()
			}
			
			def errorSynchro = ""

			if(params.isSynchro != null && params.isSynchro.toString().equals("false"))
			{
				errorSynchro = "Ha habido problemas de sincronización. Inténtelo más tarde."
			}
						
			def userInstance = User.get(springSecurityService.principal.id)
			def slrListInstance = userInstance.userProfile.slrs
			
			for(Slr slr : slrListInstance)
			{
				slr.state = toolService.updateStatus(slr)
				slr.save(flush: true)
			}
			
			[slrListInstance: slrListInstance,
			 error: error,
			 errorCriterion: errorCriterion,
			 tituloSlr: tituloSlr,
			 justificacionSlr: justificacionSlr,
			 success: success,
			 successCriterion: successCriterion,
			 nombreCriterion: nombreCriterion,
			 descripcionCriterion: descripcionCriterion,
			 guidSlrError: guidSlrError,
			 nombreAttribute: nombreAttribute,
			 opcionesAttribute: opcionesAttribute,
			 tipoAttribute: tipoAttribute,
			 successAttribute: successAttribute,
			 errorAttribute: errorAttribute,
			 enunciadoQuestion: enunciadoQuestion,
			 errorQuestion: errorQuestion,
			 successQuestion: successQuestion,
			 errorSynchro: errorSynchro
			]
		}
	}
	
	def searchs()
	{
		def isLogin = springSecurityService.loggedIn
		
		// Si no existe el guid, redirigimos a index
		if(!isLogin || !params.guid || params.guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def slrInstance = Slr.findByGuid(params.guid)
			
			def userLogin = User.get(springSecurityService.principal.id)
			if(userLogin.userProfile != slrInstance.userProfile)
			{
				redirect(controller: 'index', action: 'index')
			}
			
			if(params.guidNotif != null)
			{
				def notification = Notification.findByGuidLike(params.guidNotif.toString())
				notification.leido = true;
				notification.save(failOnError: true, flush: true)
			}
			
			[slrInstance: slrInstance, searchListInstance: slrInstance.searchs]
		}
	}
	
	def references()
	{
		def isLogin = springSecurityService.loggedIn
		
		// Si no existe el guid, redirigimos a index
		if(!isLogin || !params.guid || params.guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def slrInstance = Slr.findByGuid(params.guid)
			def userLogin = User.get(springSecurityService.principal.id)
			
			if(null != slrInstance && userLogin.userProfile != slrInstance.userProfile)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{			
				def searchInstance = Search.findByGuid(params.guid)
				
				SortedSet<Reference> referenceListInstance = new TreeSet<Reference>()
				
				if (searchInstance == null && slrInstance == null)
				{
					redirect(controller: 'index', action: 'index')
				}
				else
				{
					if (searchInstance != null)
					{
						referenceListInstance.addAll(searchInstance.references)
						//referenceListInstance = searchInstance.references
						slrInstance = searchInstance.slr
					}
					else
					{
						referenceListInstance = toolService.getReferences(slrInstance)
					}
					
					// Obtenemos los parametros de filtro
					toolService.getAllParamsFilter(referenceListInstance)
					
					// Paginamos los resultados
					
					def totalRefs  = referenceListInstance.size()
					def totalPages = Math.round(totalRefs / maxPerPage);
					def page = 1
					def offset = (page * maxPerPage) - maxPerPage
					
					referenceListInstance = toolService.getPaginatedReferences(referenceListInstance, maxPerPage,offset)
					
					strSearch = ""
				
					[
						referenceListInstance: referenceListInstance, 
						slrInstance: slrInstance, 
						guidSlr: slrInstance.guid,
						authorsListInstance: toolService.getAuthors(),
						criterionsListInstance: toolService.getCriterions(),
						minYear: Integer.parseInt(toolService.getMinYear()),
						maxYear: Integer.parseInt(toolService.getMaxYear()),
						enginesListInstance: toolService.getEngines(),
						languagesListInstance: toolService.getLanguages(),
						departmentsListInstance: toolService.getDepartaments(),
						typesListInstance: toolService.getTypes(),
						strSearch: strSearch,
						referenceListCount: totalRefs,
						totalPages: totalPages,
						page: page,
						offset: offset
					]
				}
			}
		}
	}
	
	def filtredReferencesByParam = {
		
		int page = 1
		String filter = (null == params.filter ? "" : params.filter.toString())
		String guidSlr =  (null == params.guidSlr ? "" : params.guidSlr.toString())
		
		if (!filter.equals(""))
		{
			page = 1
			strSearch = toolService.formatSearchString(strSearch.toString(), filter)
		}
		else
		{
			page = Integer.parseInt(null == params.p ? "1" : params.p.toString())
		}
		
		def offset = (page * maxPerPage) - maxPerPage
		
		SortedSet<Reference> referenceListInstance = toolService.getReferencesWithFilter(guidSlr, strSearch)

		def totalRefs  = referenceListInstance.size()
		def totalPages = Math.round(totalRefs / maxPerPage);
		
		referenceListInstance = toolService.getPaginatedReferences(referenceListInstance, maxPerPage, offset)
		
		render(template:'referencesSearchResult', model:[referenceListInstance:referenceListInstance, 
														 strSearch: strSearch, 
														 guidSlr:guidSlr,
														 referenceListCount: totalRefs,
														 totalPages: totalPages,
														 page: page,
														 offset: offset
														 ])
	}
	
	@Transactional
	def save()
	{
		String tituloSlr = params.titulo.toString()
		String justificacionSlr = params.justificacion.toString()
		User userInstance = User.get(Long.parseLong(springSecurityService.principal.id.toString()))
		String error = ""
		
		if(userInstance == null)
		{
			redirect(controller: 'index', action: 'index')
		}
		else if (tituloSlr.equals("null") || tituloSlr.length() <= 5 )
		{
			error = "ERROR: El nombre debe contener mas de cinco caracteres."
		}
		else if (justificacionSlr.equals("null") || justificacionSlr.length() <= 2 )
		{
			error = "ERROR: La justificacion debe contener mas de dos caracteres."
		}
		else
		{
			tituloSlr = "SLR: " + params.titulo.toString().trim()
			
			def mySlrs = Slr.findAllByUserProfileAndTitleLike(userInstance.userProfile, tituloSlr)
			
			if(mySlrs.size() > 0) // Hay repetidos con ese nombre
			{
				error = "ERROR: Ya existe otro SLR con ese nombre."
			}
		}
		
		if(!error.equals(""))
		{
			redirect(controller: 'slr', action: 'myList',
				params: [error: error,
						 tituloSlr: params.titulo,
						 justificacionSlr: params.justificacion])
		}
		else
		{
			// Creamos el Slr en BD
			Slr newSlrInstance = new Slr(title: tituloSlr, justification: justificacionSlr, userProfile: userInstance.userProfile, idmend: tituloSlr)
			newSlrInstance.save flush: true
			
			// Creamos el Slr en Mendeley
			mendeleyToolService.createSlrMendeley(userInstance, newSlrInstance)
			
			redirect(controller: 'slr', action: 'myList', params: [success: true])
		}
	}
	
	@Transactional
	def delete()
	{
		def slrInstance = Slr.findByGuid(params.guidSlr.toString())
		
		toolService.deleteSlr(slrInstance)
		
		redirect(controller: 'slr', action: 'myList')
	}
	
	def show()
	{
		def slrInstance = Slr.findByGuid(params.guidSlr.toString())
		
		if(slrInstance == null)
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			[slrInstance: slrInstance]
		}
	}
	
	def criterions()
	{
		def isLogin = springSecurityService.loggedIn
		
		// Si no existe el guid, redirigimos a index
		if(!isLogin || !params.guid || params.guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Comprobamos si se va a crear un nuevo SLR.
			def errorCriterion = ""
			def successCriterion = false
			def nombreCriterion = ""
			def descripcionCriterion = ""
			
			if(params.successCriterion.toString().equals("true"))
			{
				successCriterion = true
			}

			if(!(null == params.errorCriterion || params.errorCriterion.equals(null)))
			{
				errorCriterion = params.errorCriterion.toString()
			}
			
			if(!(params.nombreCriterion == null || params.nombreCriterion.equals(null) || successCriterion))
			{
				nombreCriterion = params.nombreCriterion.toString()
			}
			
			if(!(params.descripcionCriterion == null || params.descripcionCriterion.equals(null) || successCriterion))
			{
				descripcionCriterion = params.descripcionCriterion.toString()
			}
			
			def slrInstance = Slr.findByGuid(params.guid)
			
			def userLogin = User.get(springSecurityService.principal.id)
			if(userLogin.userProfile != slrInstance.userProfile)
			{
				redirect(controller: 'index', action: 'index')
			}
			
			// Calculamos el n�mero de referencias con los criterios
			Map<String, Integer> totalReferences = new HashMap<String,Integer>()
			for(Search search : slrInstance.searchs)
			{
				for(Reference reference : search.references)
				{
					if (null == totalReferences.get(reference.criterion.name))
					{
						totalReferences.put(reference.criterion.name, 1)
					}
					else
					{
						totalReferences.put(reference.criterion.name, totalReferences.get(reference.criterion.name).value+1)
					}
				}
			}
			
			[slrInstance: slrInstance, criterionListInstance: slrInstance.criterions,
			 errorCriterion: errorCriterion, successCriterion: successCriterion,
			 nombreCriterion: nombreCriterion, descripcionCriterion: descripcionCriterion,
			 totalReferences: totalReferences]
		}
	}
	
	def specAttributes()
	{
		def isLogin = springSecurityService.loggedIn
		
		// Si no existe el guid, redirigimos a index
		if(!isLogin || !params.guid || params.guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Comprobamos si se va a crear un nuevo SLR.
			def errorAttribute = ""
			def successAttribute = false
			def nombreAttribute = ""
			def opcionesAttribute = ""
			def tipoAttribute = "string"
			
			if(params.successAttribute.toString().equals("true"))
			{
				successAttribute = true
			}
		
			if(!(null == params.errorAttribute || params.errorAttribute.equals(null)))
			{
				errorAttribute = params.errorAttribute.toString()
			}
			
			if(!(params.nombreAttribute == null || params.nombreAttribute.equals(null) || successAttribute))
			{
				nombreAttribute = params.nombreAttribute.toString()
			}
			
			if(!(params.tipoAttribute == null || params.tipoAttribute.equals(null) || successAttribute))
			{
				tipoAttribute = params.tipoAttribute.toString()
			}
			
			if(!(params.opcionesAttribute == null || params.opcionesAttribute.equals(null) || successAttribute))
			{
				opcionesAttribute = params.opcionesAttribute.toString()
			}
			
			def slrInstance = Slr.findByGuid(params.guid)
			
			def userLogin = User.get(springSecurityService.principal.id)
			if(userLogin.userProfile != slrInstance.userProfile)
			{
				redirect(controller: 'index', action: 'index')
			}
			
			[slrInstance: slrInstance, specAttributesListInstance: slrInstance.specAttributes,
			 errorAttribute: errorAttribute, successAttribute: successAttribute,
			 nombreAttribute: nombreAttribute, opcionesAttribute: opcionesAttribute,
			 tipoAttribute: tipoAttribute]
		}
	}
	
	def researchQuestions()
	{
		def isLogin = springSecurityService.loggedIn
		
		// Si no existe el guid, redirigimos a index
		if(!isLogin || !params.guid || params.guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Comprobamos si se va a crear un nuevo SLR.
			def errorQuestion = ""
			def successQuestion = false
			def enunciadoQuestion = ""

			if(params.successQuestion.toString().equals("true"))
			{
				successQuestion = true
			}
		
			if(!(null == params.errorQuestion || params.errorQuestion.equals(null)))
			{
				errorQuestion = params.errorQuestion.toString()
			}
			
			if(!(params.enunciadoQuestion == null || params.enunciadoQuestion.equals(null) || successQuestion))
			{
				enunciadoQuestion = params.enunciadoQuestion.toString()
			}
			
			def slrInstance = Slr.findByGuid(params.guid)
			
			def userLogin = User.get(springSecurityService.principal.id)
			if(userLogin.userProfile != slrInstance.userProfile)
			{
				redirect(controller: 'index', action: 'index')
			}
			
			[slrInstance: slrInstance, questionListInstance: slrInstance.questions,
			 errorQuestion: errorQuestion, successQuestion: successQuestion,
			 enunciadoQuestion: enunciadoQuestion]
		}
	}
	
	def exportToExcel()
	{
		def slrInstance = Slr.findByGuidLike(params.guid.toString())
		
		if(slrInstance == null)
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			def file = exportService.exportToExcel(slrInstance)
			
			if (file.exists()) {
				response.setContentType("application/octet-stream")
				response.setHeader("Content-disposition", "filename=${file.name}")
				response.outputStream << file.bytes
				if(!file.delete())
					println "No se ha borrado el fichero " + file.name
				return
			 }
		}
	}
	
	def exportToPdf()
	{
		def slrInstance = Slr.findByGuidLike(params.guid.toString())
		
		if(slrInstance == null)
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			def file = exportService.exportToPdf(slrInstance)
			
			if (file.exists()) {
				response.setContentType("application/octet-stream")
				response.setHeader("Content-disposition", "filename=${file.name}")
				response.outputStream << file.bytes
				if(!file.delete())
					println "No se ha borrado el fichero " + file.name
				return
			 }
		}
	}
	
	def exportToBibTex()
	{
		def slrInstance = Slr.findByGuidLike(params.guid.toString())
		
		if(slrInstance == null)
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			def file = exportService.exportToBibTex(slrInstance)
			
			if (file.exists()) {
				response.setContentType("application/octet-stream")
				response.setHeader("Content-disposition", "filename=${file.name}")
				response.outputStream << file.bytes
				if(!file.delete())
					println "No se ha borrado el fichero " + file.name
				return
			}
		}
	}
		
	def graphs()
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		
		def slrInstance = Slr.findByGuidLike(params.guid)
		
		if(slrInstance == null)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			List<String> queriesChart = graphService.chartsByTag(slrInstance)
						
			[
				slrInstance: slrInstance, 
				criterionShowTextEvery: queriesChart.get(0),
				queryCriterion1: queriesChart.get(1),
				queryCriterion2: queriesChart.get(2),
				queryCriterion3: queriesChart.get(3),
				totalCriterions: Integer.parseInt(queriesChart.get(4)),
				queryEngine1: queriesChart.get(5),
				queryEngine2: queriesChart.get(6),
				queryEngine3: queriesChart.get(7),
				totalEngines: Integer.parseInt(queriesChart.get(8)),
				queryDepartment1: queriesChart.get(9),
				queryDepartment2: queriesChart.get(10),
				queryDepartment3: queriesChart.get(11),
				totalDepartments: Integer.parseInt(queriesChart.get(12)),
				queryType1: queriesChart.get(13),
				queryType2: queriesChart.get(14),
				queryType3: queriesChart.get(15),
				totalTypes: Integer.parseInt(queriesChart.get(16)),
				queryLanguage1: queriesChart.get(17),
				queryLanguage2: queriesChart.get(18),
				queryLanguage3: queriesChart.get(19),
				totalLanguages: Integer.parseInt(queriesChart.get(20)),
				querySearch1: queriesChart.get(21)
			]
		}			
	}
		
	@Transactional
	def syncronizeListSlrMendeley()
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Si no existe el guid, redirigimos a index
			def userLogin = User.get(springSecurityService.principal.id)
			
			boolean isSynchro = mendeleyToolService.synchronizeSlrList(userLogin)

			redirect(controller: 'slr', action: 'myList', params: [isSynchro: isSynchro])
		}
	}
	
	@Transactional
	def syncronizeSlrMendeley()
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin || !params.guidSlr || params.guidSlr.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def slrInstance = Slr.findByGuidIlike(params.guidSlr.toString())
			
			if (slrInstance == null)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				// Si no existe el guid, redirigimos a index
				def userLogin = User.get(springSecurityService.principal.id)
				
				mendeleyToolService.synchronizeSlr(userLogin, slrInstance)
				
				redirect(controller: 'slr', action: 'searchs', params: [guid: slrInstance.guid])
			}
		}
	}
}
