package es.uca.pfc

import grails.transaction.Transactional;

class ReferenceController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
	def exportService
	def mendeleyToolService
	
    def index() {
		redirect(controller: 'slr', action: 'myList')
	}
	
	def show()
	{
		def isLogin = springSecurityService.loggedIn
		def idmend = params.idmend.toString()
		
		if(!isLogin || null == idmend || idmend.equals(""))
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			def referenceInstance = Reference.findByIdmend(idmend)
			
			if(null == referenceInstance)
			{
				redirect(controller: 'slr', action: 'myList')
			}
			else
			{
				def error = (params.error != null ? params.error : "")
				def isSynchro = (params.isSynchro != null ? params.isSynchro : "")
				def success = false
				if (null != params.success)
				{
					//success = true
					success = params.success
				}
				
				if(error == "" && isSynchro.toString().equals("false"))
				{
					error = "ERROR: Cambios guardados pero no sincronizados con Mendeley. Inténtelo más tarde."
				}
				
				// Listamos los autores en una sola cadena de texto
				String listAuthorsString = ""
				int cont = 0
				for(AuthorReference ar : referenceInstance.authorsRefs)
				{
					listAuthorsString += ar.author.display_name
					if(cont != referenceInstance.authorsRefs.size()-1)
					{
						listAuthorsString += "; "
					}
				}
				
				// Listamos los keywords
				String listKeywordsString = ""
				cont = 0
				for(String key : referenceInstance.keywords)
				{
					listKeywordsString += key
					if (cont != referenceInstance.keywords.size()-1)
					{
						listKeywordsString += "; "
					}
				}
				
				// Listamos las webs
				String listWebsString = ""
				cont = 0
				for(String web : referenceInstance.websites)
				{
					listWebsString += web
					if (cont != referenceInstance.websites.size()-1)
					{
						listWebsString += "; "
					}
				}
				
				// Usuario de la referencia
				def userOwnerInstance = referenceInstance.search.slr.userProfile.user
				
				// Listamos las webs
				String listTagsString = ""
				cont = 0
				for(String tag : referenceInstance.tags)
				{
					listTagsString += tag
					if (cont != referenceInstance.tags.size()-1)
					{
						listTagsString += "; "
					}
				}
				
				boolean noDrop = referenceInstance.search.slr.noDrop
				
				[
					referenceInstance: referenceInstance,
					typeListInstance: TypeDocument.all,
					languageListInstance: Language.all,
					criterionListInstance: referenceInstance.search.slr.criterions,
					listAuthorsString: listAuthorsString,
					listKeywordsString: listKeywordsString,
					listWebsString: listWebsString,
					listTagsString: listTagsString,
					userOwnerInstance: userOwnerInstance,
					error: error,
					success: success,
					noDrop: noDrop
				]
			}
		}
	}
	
	@Transactional
	def save()
	{
		def idmend = params.idmend.toString()
		
		if (null == idmend || idmend.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			User userInstance = User.get(springSecurityService.principal.id)
			
			if(userInstance == null)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				
				def referenceInstance = Reference.findByIdmend(idmend)
				
				if (null == referenceInstance)
				{
					redirect(controller: 'index', action: 'index')
				}
				else
				{
					def error = ""
					if (params.inputTitle.toString().equals("") || params.inputTitle.toString().size() < 5)
					{
						error = "ERROR: El título debe contener mas de cinco caracteres"
					}
					else if(params.inputAuthors.toString().equals("") || !toolService.validateAuthorString(params.inputAuthors.toString()))
					{
						error = "ERROR: Debe haber al menos un autor o el formato para un autor no es correcto."
					}
					
					if(!error.equals(""))
					{
						redirect(controller: 'reference', action: 'show',
							params: [
										idmend: idmend,
										error: error
									])
					}
					else
					{
						referenceInstance.title = (null != params.inputTitle ? params.inputTitle : "")
						referenceInstance.source = (null != params.inputSource ? params.inputSource : "")
						referenceInstance.source_type = (null != params.inputSourceType ? params.inputSourceType : "")
						referenceInstance.pages = (null != params.inputPages ? params.inputPages : "")
						referenceInstance.volume = (null != params.inputVolume ? params.inputVolume : "")
						referenceInstance.issue = (null != params.inputIssue ? params.inputIssue : "")
						referenceInstance.country = (null != params.inputCountry ? params.inputCountry : "")
						referenceInstance.department = (null != params.inputDepartment ? params.inputDepartment : "")
						referenceInstance.docAbstract = (null != params.inputAbstract ? params.inputAbstract : "")
						referenceInstance.publisher = (null != params.inputPublisher ? params.inputPublisher : "")
						referenceInstance.city = (null != params.inputCity ? params.inputCity : "")
						referenceInstance.institution = (null != params.inputInstitution ? params.inputInstitution : "")
						referenceInstance.series = (null != params.inputSeries ? params.inputSeries : "")
						referenceInstance.chapter = (null != params.inputChapter ? params.inputChapter : "")
						referenceInstance.genre = (null != params.inputGenre ? params.inputGenre : "")
						referenceInstance.notes = (null != params.inputNotes ? params.inputNotes : "")
						
						referenceInstance.type = TypeDocument.findByNomenclatura(params.selectType.toString())
						referenceInstance.criterion = Criterion.findByNomenclatura(params.selectCriterion.toString())
						referenceInstance.language = Language.findByCode(params.selectLanguage.toString())
						
						// Keywords
						referenceInstance.keywords.clear()
						if (!params.inputKeys.toString().equals(""))
						{
							String[] arrayElements = params.inputKeys.toString().trim().toLowerCase().split(";")
							for(String e : arrayElements)
							{
								if (!(e.trim().equals("") || e.trim().equals(" ")))
								{
									referenceInstance.addToKeywords(e.trim())
								}
							}
						}
						
						// Websites
						referenceInstance.websites.clear()
						if (!params.inputWebs.toString().equals(""))
						{
							String[] arrayElements = params.inputWebs.toString().trim().toLowerCase().split(";")
							for(String e : arrayElements)
							{
								if (!(e.trim().equals("") || e.trim().equals(" ")))
								{
									referenceInstance.addToWebsites(e.trim())
								}
							}
						}
						
						// Tags
						referenceInstance.tags.clear()
						if (!params.inputTags.toString().equals(""))
						{
							String[] arrayElements = params.inputTags.toString().trim().toLowerCase().split(";")
							for(String e : arrayElements)
							{
								if (!(e.trim().equals("") || e.trim().equals(" ")))
								{
									referenceInstance.addToTags(e.trim())
								}
							}
						}
						
						referenceInstance.save(failOnError: true)
						
						// Authors
						Set<Long> ids = new HashSet<Long>()
						
						for(AuthorReference ar : referenceInstance.authorsRefs)
						{
							ids.add(ar.id)
						}
						
						for(Long i : ids)
						{
							AuthorReference.executeUpdate("delete AuthorReference ar where ar.id = :arid", [arid:i])
						}
						
						referenceInstance.authorsRefs.clear()
						referenceInstance.save(failOnError: true)
	
						if (!params.inputAuthors.toString().equals(""))
						{
							Set<Author> authors = new HashSet<Author>()
							String[] arrayElements = params.inputAuthors.toString().trim().split(";")
							
							for(String e : arrayElements)
							{
								if (!(e.trim().equals("") || e.trim().equals(" ")))
								{
									def author = Author.findByDisplay_nameIlike(e.trim())
									
									// Si no existe, lo creamos
									if(null == author)
									{
										String[] arrayDisplayName = e.trim().split(" ")
										if(arrayDisplayName.size() < 2)
										{
											// error
										}
										else
										{
											String nombre = ""
											String apellidos = ""
											
											nombre = arrayDisplayName[0].toString().trim().substring(0,1).toUpperCase() + arrayDisplayName[0].toString().trim().substring(1).toLowerCase()
											
											for(int i=1; i<arrayDisplayName.size(); i++)
											{
												apellidos += arrayDisplayName[i].toString().trim().substring(0,1).toUpperCase() + arrayDisplayName[i].toString().trim().substring(1).toLowerCase() + " "
											}
											
											author = new Author(forename: nombre, surname: apellidos)
											author.save(failOnError: true)
										}
									}
									
									authors.add(author)
								}
							}
							
							for(Author author : authors)
							{
								author.addToAuthorsRefs(reference: referenceInstance).save(failOnError: true)
							}
							
							// Citation Key
							referenceInstance.citation_key = toolService.getCitationKey(referenceInstance, authors)
							
							// Atributos específicos
							def slrInstance = referenceInstance.search.slr
							for(SpecificAttribute attribute : slrInstance.specAttributes)
							{
								def attributeReference = SpecificAttributeReference.findByReferenceAndAttribute(referenceInstance,attribute)
								def value = (params["att"+attribute.id.toString()] == null ? "" : params["att"+attribute.id.toString()].toString())
								attributeReference.value = value;
								attributeReference.save(failOnError: true)
							}
						}
						
						referenceInstance.save(failOnError: true)					
						
						// Guardar cambios en mendeley
						boolean isSynchro = mendeleyToolService.saveReferenceMendeley(referenceInstance, userInstance)
						
						redirect(controller: 'reference', action: 'show', params: [idmend: idmend, success: true, isSynchro: isSynchro])
					}
				}
			}
		}
	}
	
	@Transactional
	def delete()
	{
		String idmend = (null == params.idmendReference ? "" : params.idmendReference)
		
		if (idmend.equals(""))+
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def referenceInstance = Reference.findByIdmend(idmend)
			
			if (null == referenceInstance)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				String guidSlr = referenceInstance.search.slr.guid
				
				// Borramos las referencias con los autores
				AuthorReference.deleteAll(AuthorReference.findAllByReference(referenceInstance))
				
				// Borramos las referencias con los atributos especificos
				SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReference(referenceInstance))
				
				referenceInstance.delete flush: true
				
				redirect(controller: 'slr', action: 'references', params: [guid: guidSlr])
			}
		}		
	}
	
	def exportReferenceToBibTex()
	{
		def idmend = params.idmend.toString();
		def referenceInstance = Reference.findByIdmendLike(idmend)
		
		if(referenceInstance == null)
		{
			redirect(controller: 'slr', action: 'myList')
		}
		else
		{
			def file = exportService.exportToBibTex(referenceInstance)
			
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
	
	@Transactional
	def sychronizeReferenceMend()
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin || !params.idmend || params.idmend.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def referenceInstance = Reference.findByIdmend(params.idmend.toString())
			
			if(referenceInstance == null)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				def userLogin = User.get(springSecurityService.principal.id)
				mendeleyToolService.updateReferenceFromMendeley(referenceInstance, userLogin)
				
				redirect(controller: 'reference', action: 'show',
					params: [idmend: referenceInstance.idmend])
			}
		}
	}
}
