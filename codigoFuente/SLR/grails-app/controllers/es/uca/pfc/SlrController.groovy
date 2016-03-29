package es.uca.pfc

import grails.transaction.Transactional;

import java.util.Date;

class SlrController {
	
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
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
			 successQuestion: successQuestion
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
		
		if(userInstance == null)
		{
			redirect(controller: 'index', action: 'index')
		}
		else if (tituloSlr.equals("null") || tituloSlr.length() <= 5 )
		{
			redirect(controller: 'slr', action: 'myList',
				params: [error: 'ERROR: El nombre debe contener mas de cinco caracteres.',
						 tituloSlr: params.titulo,
						 justificacionSlr: params.justificacion])
		}
		else if (justificacionSlr.equals("null") || justificacionSlr.length() <= 2 )
		{
			redirect(controller: 'slr', action: 'myList',
				params: [error: 'ERROR: La justificacion debe contener mas de dos caracteres.',
						 tituloSlr: params.titulo,
						 justificacionSlr: params.justificacion])
		}
		else
		{
			tituloSlr = params.titulo.toString().trim()
				
			def mySlrs = Slr.findAllByUserProfileAndTitleLike(userInstance.userProfile, tituloSlr)
				
			if(mySlrs.size() > 0) // Hay repetidos con ese nombre
			{
				redirect(controller: 'slr', action: 'myList',
					params: [error: 'ERROR: Ya existe otro SLR con ese nombre.',
							 tituloSlr: params.titulo,
							 justificacionSlr: params.justificacion])
			}
			else
			{
				// Creamos el Slr en BD
				Slr newSlrInstance = new Slr(title: tituloSlr, justification: justificacionSlr, userProfile: userInstance.userProfile, idmend: tituloSlr)
				newSlrInstance.save flush: true
				
				// Creamos el Slr en Mendeley
				
				redirect(controller: 'slr', action: 'myList', params: [success: true])
			}
		}
	}
	
	@Transactional
	def delete()
	{
		def slrInstance = Slr.findByGuid(params.guidSlr.toString())
		
		if(slrInstance != null)
		{
			for(Search search : slrInstance.searchs)
			{
				for (Reference reference : search.references)
				{
					// Borramos las referencias con los autores
					AuthorReference.deleteAll(AuthorReference.findAllByReference(reference))
					
					// Borramos las referencias con los atributos especificos
					SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReference(reference))
				
					reference.criterion = null
				}
			}
			
			slrInstance.delete flush: true
		}
		
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
			
			// Calculamos el número de referencias con los criterios
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
}
