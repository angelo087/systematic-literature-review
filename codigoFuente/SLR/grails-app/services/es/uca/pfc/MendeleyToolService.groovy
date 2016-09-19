package es.uca.pfc

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.UUID;

import mendeley.pfc.schemas.Document
import mendeley.pfc.schemas.Folder
import mendeley.pfc.schemas.Identifier
import mendeley.pfc.schemas.Person
import mendeley.pfc.schemas.Profile
import mendeley.pfc.services.DocumentService
import mendeley.pfc.services.FolderService
import mendeley.pfc.services.MendeleyService
import mendeley.pfc.services.ProfileService;
import mendeley.pfc.commons.MendeleyException;

import org.apache.poi.util.StringUtil;
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.Trigger
import org.quartz.impl.StdSchedulerFactory

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;
import es.uca.pfc.encode.EncodeDecodeMendeley;
import es.uca.pfc.task.TaskEngineSearch
import es.uca.pfc.task.TaskSearch
import grails.transaction.Transactional;

import org.quartz.impl.matchers.KeyMatcher;

import com.google.gson.Gson;

import grails.util.Holders

@Transactional
class MendeleyToolService /*implements IMendeleyService*/ {
	
	def springSecurityService
	def toolService
		
	String encodePasswordMendeley(String password)
	{
		return EncodeDecodeMendeley.encodePasswordMendeley(password);
	}
	
	String decodePasswordMendeley(String password)
	{
		return EncodeDecodeMendeley.decodePasswordMendeley(password);
	}
	
	boolean insertSearchsBackground(Slr slrInstance, List<String> terminos, List<SearchOperator> operators, 
		List<SearchComponent> components, String minYear, String maxYear, String maxTotal, List<EngineSearch> engines)
	{
		println "insertSearchsBackground"
		def userInstance = User.get(springSecurityService.currentUser.id)
		
		runAsync {
			createSearch(userInstance, slrInstance, terminos, operators, components, minYear, maxYear, maxTotal, engines)
		}
		
		return true;
	}	
	
	@Transactional
	void sendSearchNotification(User userInstance, Slr slrInstance, boolean success)
	{
		String asunto = "Nuevas búsquedas"
		String txt = ""
		if (!success)
		{
			txt = "Ha habido problemas en la realización de búsquedas en el SLR " + slrInstance.title
		}
		else
		{
			txt = "Se han realizado nuevas búsquedas para el SLR " + slrInstance.title
		}
		
		UserProfile userProfile = UserProfile.lock(1)

		userProfile.addToNotifications(new NotificationSlr(tipo: "search", asunto: asunto, texto: txt, slr: slrInstance))
		userProfile.save(failOnError: true)
		println "Notificación enviada"
	}
	
	void createSearch(User userInstance, Slr slrInstance, List<String> terminos, List<SearchOperator> operators, 
		List<SearchComponent> components, String minYear, String maxYear, String maxTotal, List<EngineSearch> engines)
	{
		println "crearBusquedas"
		def langES = Language.findByCodeLike('ES')
		def langEN = Language.findByCodeLike('EN')
		def langFR = Language.findByCodeLike('FR')
		def type01 = TypeDocument.findByNombreLike('Journal')
		def type02 = TypeDocument.findByNombreLike('Book')
		def author01 = Author.findByForenameLikeAndSurnameLike('Angel','Gonzalez')
		def author02 = Author.findByForenameLikeAndSurnameLike('Aradia','Rocha')
		
		Random rnd = new Random()
		Search searchInstance = new Search(startYear: minYear, endYear: maxYear, maxTotal: maxTotal, slr: slrInstance)
		
		for(int i=0; i<terminos.size(); i++)
		{
			SearchTermParam term = new SearchTermParam( terminos: terminos.get(i),
														component: components.get(i),
														operator: operators.get(i))
			searchInstance.addToTermParams(term)
		}
		
		for(EngineSearch engine : engines)
		{
			searchInstance.addToEngines(engine)
			
			for(int i=0; i < Integer.parseInt(maxTotal); i++)
			{
				String idMend = UUID.randomUUID().toString()
				def type
				def lang
				def author
				switch ((int)(rnd.nextDouble()*2+1))
				{
					case 2:
						type = type02;
						author = author02
						break;
					default:
						type = type01;
						author = author01
				}
				switch ((int)(rnd.nextDouble()*3+1))
				{
					case 1:
						lang = langEN;
						break;
					case 2:
						lang = langES;
						break;
					default:
						lang = langFR;
				}
				
				String year = ((int) (rnd.nextDouble()*(Integer.parseInt(maxYear) - Integer.parseInt(minYear) + 1) + Integer.parseInt(minYear))).toString()
				
				Reference referenceInstance = new Reference(idmend : idMend,
															title : 'Reference ' + idMend,
															type : type,
															docAbstract : 'Abstract ' + idMend,
															source : 'Source ' + idMend,
															year : year,
															publisher : 'publi'+idMend,
															city : 'Cadiz',
															institution : 'Institution '+idMend,
															series : 'Series '+idMend,
															citation_key : 'citationkey'+idMend,
															language : lang,
															bibtex: 'Bibtex '+idMend,
															engine: engine)
				
				searchInstance.addToReferences(referenceInstance)
				if(author.id == author01.id)
				{
					author01.addToAuthorsRefs(reference: referenceInstance).save(failOnError: true)
				}
				else
				{
					author02.addToAuthorsRefs(reference: referenceInstance).save(failOnError: true)
				}
				searchInstance.save(failOnError: true)
			} // for i reference
		} // for engine
		searchInstance.save(failOnError: true)		
		println "PROCESO DE BUSQUEDAS COMPLETADO"
		sendSearchNotification(userInstance, slrInstance, true)
	}
	
	boolean synchronizeProfile(User userInstance)
	{
		boolean isSynchro = false;
		try
		{
			String clientId = Holders.getGrailsApplication().config.clientId
			String clientSecret = Holders.getGrailsApplication().config.clientSecret
			String redirectUri = Holders.getGrailsApplication().config.redirectUri
			
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri, 
				userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend), 
				userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);

			// Actualizamos los tokens del usuario
			userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
			userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
			userInstance.save(failOnError: true, flush: true)
			
			
			ProfileService profileService = new ProfileService(mendeleyService);
			Profile profile = profileService.getCurrentProfile();
			UserProfile currentProfile = userInstance.userProfile
			
			convertProfileMendeleyToProfileSlr(profile,currentProfile)
			
			userInstance.save(failOnError: true, flush: true)
			isSynchro = true
		}
		catch(MendeleyException ex)
		{
			println "EXCEPTION MendeleyToolService.synchronizeProfile() => Hubo un problema de sincronizacion."
		}
		
		return isSynchro
	}
	
	void convertProfileMendeleyToProfileSlr(Profile profile, UserProfile userProfile)
	{
		userProfile.first_name = (profile.getFirstName() == null ? "" : profile.getFirstName().toString())
		userProfile.last_name = (profile.getLastName() == null ? "" : profile.getLastName().toString())
		userProfile.display_name = (profile.getDisplayName() == null ? "" : profile.getDisplayName().toString())
		userProfile.url_foto = (profile.getPhoto() == null ? "unknown_user.png" : (profile.getPhoto().getUrl() == null ? "unknown_user.png" : profile.getPhoto().getUrl().toString()))
		userProfile.idmend = (profile.getId() == null ? "" : profile.getId().toString())
		userProfile.research_interests = (profile.getResearchInterests() == null ? "" : profile.getResearchInterests().toString())
		userProfile.academic_status = (profile.getAcademicStatus() == null ? "" : profile.getAcademicStatus().toString())
		userProfile.link = (profile.getLink() == null ? "" : profile.getLink().toString())
		userProfile.created = (profile.getCreated() == null ? "" : profile.getCreated().toString())
		userProfile.biography = (profile.getBiography() == null ? "" : profile.getBiography().toString())
		userProfile.codeBotonEnlace = ""
		userProfile.locationName = (profile.getLocation() == null ? "" : profile.getLocation().getName() == null ? "" : profile.getLocation().getName().toString())
		userProfile.locationLatitude = (profile.getLocation() == null ? "" : profile.getLocation().getLatitude() == null ? "" : profile.getLocation().getLatitude().toString())
		userProfile.locationLongitude = (profile.getLocation() == null ? "" : profile.getLocation().getLongitude()== null ? "" : profile.getLocation().getLongitude().toString())
		userProfile.discipline = (profile.getDiscipline() == null ? "" : profile.getDiscipline().toString())
		
		Education.deleteAll(Education.findAllByProfile(userProfile))
		
		userProfile.save(failOnError: true, flush: true)
		if (profile.getEducation().size() > 0)
		{
			for(mendeley.pfc.schemas.Education ed : profile.getEducation())
			{
				es.uca.pfc.Education education = convertEducationMendeleyToEducationSlr(ed)
				userProfile.addToEducations(education)
			}
		}
	}
	
	es.uca.pfc.Education convertEducationMendeleyToEducationSlr(mendeley.pfc.schemas.Education educationMend)
	{
		es.uca.pfc.Education education = new Education(
				degree: (educationMend.getDegree() == null ? '' : educationMend.getDegree()),
				institution: (educationMend.getInstitution() == null ? '' : educationMend.getInstitution()),
				website: (educationMend.getWebsite() == null ? '' : educationMend.getWebsite()),
				start_date: (educationMend.getStartDate() == null ? new Date() : educationMend.getStartDate()),
				end_date: (educationMend.getEndDate() == null ? new Date() : educationMend.getEndDate())
				)
		return education;
	}
	
	boolean createSlrMendeley(User userInstance, Slr slrInstance)
	{
		String titleSlr = slrInstance.title
		boolean isCreated = false
		try
		{
			String clientId = Holders.getGrailsApplication().config.clientId
			String clientSecret = Holders.getGrailsApplication().config.clientSecret
			String redirectUri = Holders.getGrailsApplication().config.redirectUri
			
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
				userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
			
			// Actualizamos los tokens del usuario
			userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
			userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
			userInstance.save(failOnError: true, flush: true)
			
			FolderService folderService = new FolderService(mendeleyService)
			Folder folder = folderService.getFolderByName(titleSlr)
			
			// Creamos la carpeta si no existe
			if (folder == null)
			{
				folder = folderService.createFolder(titleSlr)
				slrInstance.idmend = folder.getId()
			}
			
			// Comprobamos que las subcarpetas (por engine) se encuentran
			for(EngineSearch engine : EngineSearch.list())
			{
				if(folderService.getSubFolder(folder.getId(), engine.name.toString().toLowerCase().trim()) == null)
				{
					folderService.createSubFolder(engine.name.toString().toLowerCase().trim(), folder)
				}
			}
			isCreated = true
		}
		catch(Exception ex)
		{
			println "EXCEPTION MendeleyToolService.createSlrMendeley() => " + ex.getMessage()
		}
		
		return isCreated
	}
	
	boolean synchronizeSlrList(User userInstance)
	{
		List<Slr> listSlrs = new ArrayList<Slr>()
		listSlrs.addAll(userInstance.userProfile.slrs)
		return synchronizeSlrList(userInstance, listSlrs)
	}
	
	boolean synchronizeSlr(User userInstance, Slr slrInstance)
	{
		List<Slr> listSlrs = new ArrayList<Slr>()
		
		listSlrs.add(slrInstance)
		
		return synchronizeSlrList(userInstance, listSlrs)
	}
	
	boolean synchronizeSlrList(User userInstance, List<Slr> listSlrs)
	{
		boolean isSynchro = false;
		try
		{
			String clientId = Holders.getGrailsApplication().config.clientId
			String clientSecret = Holders.getGrailsApplication().config.clientSecret
			String redirectUri = Holders.getGrailsApplication().config.redirectUri
			
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
				userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
			
			// Actualizamos los tokens del usuario
			userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
			userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
			userInstance.save(failOnError: true, flush: true)
			
			FolderService folderService = new FolderService(mendeleyService)
			DocumentService documentService = new DocumentService(mendeleyService)
			List<Folder> folders = folderService.getAllFolders()
			List<Long> idsSlrDrop = new ArrayList<Long>()
			List<Long> idsSlrUpdate = new ArrayList<Long>()
			
			// Comprobamos cuáles deberán eliminarse y cuáles actualizarse
			for(Slr slr : listSlrs)
			{
				Folder folder = folderService.getFolderById(slr.idmend)
				
				if (folder == null)
				{
					idsSlrDrop.add(slr.id)
				}
				else
				{
					idsSlrUpdate.add(slr.id)
				}
			}

			// Eliminamos Slrs
			if (idsSlrDrop.size() > 0)
			{
				for(Long idSlr : idsSlrDrop)
				{
					Slr slrInstance = Slr.get(idSlr)
					userInstance.userProfile.removeFromSlrs(slrInstance)
					toolService.deleteSlr(slrInstance)
				}
			}

			// Actualizamos Slrs
			if (idsSlrUpdate.size() > 0)
			{
				def engines = EngineSearch.list()
				List<Reference> referencesDrop = new ArrayList<Reference>()
				List<Reference> referencesUpdate = new ArrayList<Reference>()

				for(Long idSlr : idsSlrUpdate)
				{
					Slr slrInstance = Slr.get(idSlr)
					Folder folder = folderService.getFolderById(slrInstance.idmend)
					
					// Verificamos que tiene todos las subcarpetas correspondientes
					for(EngineSearch engine : engines)
					{
						if (folderService.getSubFolder(folder.getId(), engine.name.toString().toLowerCase().trim()) == null)
						{
							println slrInstance.idmend
							println folder.getId() + " => Creamos el folder " + engine.name.toString().toLowerCase().trim() + " para el folder " + folder.getName()
							folderService.createSubFolder(engine.name.toString().toLowerCase().trim(), folder)
						}
					}
					
					// Actualizamos el nombre
					slrInstance.title = folder.getName().toString()
					slrInstance.save(failOnError: true, flush: true)
					
					// Obtenemos las referencias del slr y comprobamos si existen dichas referencias
					for(Search search : slrInstance.searchs)
					{
						for(Reference reference : search.references)
						{
							Document document = documentService.getDocument(reference.idmend)
							
							if(document == null)
							{
								referencesDrop.add(reference.id)
							}
							else
							{
								referencesUpdate.add(reference.id)
							}
						}
					}
				} // fin-for idsSlrUpdate

				// Eliminamos referencias no incluidas
				for(Long idRef : referencesDrop)
				{
					Reference reference = Reference.get(idRef)
					
					// Borramos las referencias con los autores
					AuthorReference.deleteAll(AuthorReference.findAllByReference(reference))
					
					// Borramos las referencias con los atributos especificos
					SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReference(reference))
					
					reference.delete flush: true
				}

				// Actualizamos referencias
				for(Long idRef : referencesUpdate)
				{
					Reference reference = Reference.get(idRef)
					
					// Actualizamos
					updateReferenceFromMendeley(reference, documentService)
				}
			}
			isSynchro = true;
		}
		catch(Exception ex)
		{
			isSynchro = false;
			println "EXCEPCION MendeleyToolService.synchronizeSlrList() => " + ex.getMessage()
		}

		return isSynchro;
	}
	
	void updateReferenceFromMendeley(Reference reference, User userInstance)
	{
		String clientId = Holders.getGrailsApplication().config.clientId
		String clientSecret = Holders.getGrailsApplication().config.clientSecret
		String redirectUri = Holders.getGrailsApplication().config.redirectUri
		
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
			userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
			userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
		
		// Actualizamos los tokens del usuario
		userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
		userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
		userInstance.save(failOnError: true, flush: true)
		
		DocumentService documentService = new DocumentService(mendeleyService)
		
		updateReferenceFromMendeley(reference, documentService)
	}
	
	void updateReferenceFromMendeley(Reference reference, DocumentService documentService)
	{
		Document document = documentService.getDocument(reference.idmend.toString())
		
		reference.idmend = document.getId()
		reference.title = (document.getTitle() == null ? "" : document.getTitle());
		reference.docAbstract = (document.getAbstract() == null ? "" : document.getAbstract())
		reference.source = (document.getSource() == null ? "" : document.getSource())
		reference.pages = (document.getPages() == null ? "" : document.getPages())
		reference.volume = (document.getVolume() == null ? "" : document.getVolume())
		reference.issue = (document.getIssue() == null ? "" : document.getIssue())
		reference.publisher = (document.getPublisher() == null ? "" : document.getPublisher())
		reference.city = (document.getCity() == null ? "" : document.getCity())
		reference.institution = (document.getInstitution() == null ? "" : document.getInstitution())
		reference.series = (document.getSeries() == null ? "" : document.getSeries())
		reference.chapter = (document.getChapter() == null ? "" : document.getChapter())
		reference.citation_key = (document.getCitationKey() == null ? "" : document.getCitationKey())
		reference.source_type = (document.getSourceType() == null ? "" : document.getSourceType())
		reference.genre = (document.getGenre() == null ? "" : document.getGenre())
		reference.country = (document.getCountry() == null ? "" : document.getCountry())
		reference.department = (document.getDepartment() == null ? "" : document.getDepartment())
		reference.arxiv = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getArxiv() == null ? "" : document.getIdentifiers().getArxiv()))
		reference.doi = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getDoi() == null ? "" : document.getIdentifiers().getDoi()))
		reference.isbn = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getIsbn() == null ? "" : document.getIdentifiers().getIsbn()))
		reference.issn = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getIssn() == null ? "" : document.getIdentifiers().getIssn()))
		reference.pmid = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getPmid() == null ? "" : document.getIdentifiers().getPmid()))
		reference.scopus = (document.getIdentifiers() == null ? "" : (document.getIdentifiers().getScopus() == null ? "" : document.getIdentifiers().getScopus()))
		reference.month = (document.getMonthName("") == null ? "" : document.getMonthName(""))
		reference.day = (document.getDay() == null ? "" : document.getDay())
		reference.file_attached = (document.getFileAttached() == null ? false : document.getFileAttached())
		reference.bibtex = documentService.getBibtex(document)
		
		// Actualizamos año y dia
		Calendar now = Calendar.getInstance();
		int currentYear = now.get(Calendar.YEAR);
		int numYear = currentYear
				
		try
		{
			numYear = document.getYear()
			
			if (numYear < 1980 || numYear > currentYear)
			{
				numYear = currentYear
			}
		}
		catch(NumberFormatException ex)
		{
			numYear = currentYear
		}
		reference.year = Integer.toString(numYear)
		
		// keywords
		reference.keywords.clear();
		if(document.getKeywords().size() > 0)
		{
			for(String k : document.getKeywords())
			{
				reference.addToKeywords(k)
			}
		}

		// websites
		reference.websites.clear();
		if(document.getWebsites().size() > 0)
		{
			for(String w : document.getWebsites())
			{
				reference.addToWebsites(w)
			}
		}
		
		// tags
		reference.tags.clear();
		if(document.getTags().size() > 0)
		{
			for(String w : document.getTags())
			{
				reference.addToTags(w)
			}
		}
				
		// type
		if (document.getType() == null)
		{
			reference.type = TypeDocument.findByNomenclatura('journal')
		}
		else
		{
			def typeRef = TypeDocument.findByNomenclaturaLike(document.getType().getKey().toLowerCase())
			reference.type = (typeRef == null ? TypeDocument.findByNomenclatura('journal') : typeRef)
		}
		
		// language
		if (document.getLanguage() == null)
		{
			reference.language = Language.findByName('english')
		}
		else
		{
			def langRef = Language.findByNameLike(document.getLanguage().toLowerCase())
			reference.language = (langRef == null ? Language.findByName('english') : langRef)
		}
		
		// Engine
		Folder folEngine = documentService.getFolder(document);
		if(folEngine == null)
		{
			reference.engine = EngineSearch.findByName('ACM')
		}
		else
		{
			def engSearch = EngineSearch.findByNameIlike(folEngine.getName())
			reference.engine = (engSearch == null ? EngineSearch.findByName('ACM') : engSearch)
		}
		
		// Autores
		AuthorReference.deleteAll(AuthorReference.findAllByReference(reference))
		reference.save(failOnError: true, flush: true)
		
		if (document.getAuthors().size() > 0 || document.getEditors().size() > 0)
		{
			List<Person> authorsMend = (document.getAuthors().size() > 0 ? document.getAuthors() : document.getEditors());

			for(Person person : authorsMend)
			{
				def author = Author.findByForenameIlikeAndSurnameIlike(person.getForename(), person.getSurname())
				
				if (author == null) // Lo creamos
				{
					author = new Author(forename: person.getForename(), surname: person.getSurname()).save(failOnError: true)
				}
				
				author.addToAuthorsRefs(reference: reference).save(failOnError: true)
			}
		}

		reference.save(failOnError: true, flush: true)
	}
	
	boolean isRegisteredMendeley(String emailMend, String passMend)
	{
		return getTokenResponseMendeley(emailMend, passMend) != null;
	}
	
	MendeleyService getTokenResponseMendeley(String emailMend, String passMend)
	{
		MendeleyService mendeleyService = null;
		
		String clientId = Holders.getGrailsApplication().config.clientId
		String clientSecret = Holders.getGrailsApplication().config.clientSecret
		String redirectUri = Holders.getGrailsApplication().config.redirectUri
		
		try
		{
			mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				emailMend, passMend);
		}
		catch(Exception ex)
		{
			mendeleyService = null;
		}
		
		return mendeleyService;
	}
	
	User getUserFromMendeley(String emailMend, String passMend)
	{
		User userInstance = null;
		
		String clientId = Holders.getGrailsApplication().config.clientId
		String clientSecret = Holders.getGrailsApplication().config.clientSecret
		String redirectUri = Holders.getGrailsApplication().config.redirectUri
		
		try
		{
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				emailMend, passMend);
			ProfileService profileService = new ProfileService(mendeleyService)			
			Profile profile = profileService.getCurrentProfile();
			
			UserProfile userProfileInstance = getUserProfileFromMendeley(profile)
			UserMendeley userMendeleyInstance = new UserMendeley(
																	email_mend: emailMend,
																	pass_mend: encodePasswordMendeley(passMend),
																	access_token: mendeleyService.getTokenResponse().getAccessToken(),
																	token_type: mendeleyService.getTokenResponse().getTokenType(),
																	expires_in: mendeleyService.getTokenResponse().getExpiresIn(),
																	refresh_token: mendeleyService.getTokenResponse().getRefreshToken())
			userInstance = new User(
									username: emailMend,
									password: passMend,
									enabled: true,
									userProfile: userProfileInstance,
									userMendeley: userMendeleyInstance
									)
		}
		catch(Exception ex)
		{
			println "ERROR: MendeleyToolService.getUserProfileFromMendeley() -> " + ex.getMessage()
		}
		
		return userInstance;
	}
	
	UserProfile getUserProfileFromMendeley(Profile profile)
	{
		UserProfile userProfile = new UserProfile(
			first_name: (profile.getFirstName() == null ? "" : profile.getFirstName().toString()),
			last_name: (profile.getLastName() == null ? "" : profile.getLastName().toString()),
			display_name: (profile.getDisplayName() == null ? "" : profile.getDisplayName().toString()),
			url_foto: (profile.getPhoto() == null ? "unknown_user.png" : (profile.getPhoto().getUrl() == null ? "unknown_user.png" : profile.getPhoto().getUrl().toString())),
			idmend: (profile.getId() == null ? "" : profile.getId().toString()),
			research_interests: (profile.getResearchInterests() == null ? "" : profile.getResearchInterests().toString()),
			academic_status: (profile.getAcademicStatus() == null ? "" : profile.getAcademicStatus().toString()),
			link: (profile.getLink() == null ? "" : profile.getLink().toString()),
			created: (profile.getCreated() == null ? "" : profile.getCreated().toString()),
			biography: (profile.getBiography() == null ? "" : profile.getBiography().toString()),
			codeBotonEnlace: "",
			locationName: (profile.getLocation() == null ? "" : profile.getLocation().getName() == null ? "" : profile.getLocation().getName().toString()),
			locationLatitude: (profile.getLocation() == null ? "" : profile.getLocation().getLatitude() == null ? "" : profile.getLocation().getLatitude().toString()),
			locationLongitude: (profile.getLocation() == null ? "" : profile.getLocation().getLongitude()== null ? "" : profile.getLocation().getLongitude().toString()),
			discipline: (profile.getDiscipline() == null ? "" : profile.getDiscipline().toString())
			)
		
		if(profile.getEducation() != null && profile.getEducation().size() > 0)
		{
			for(mendeley.pfc.schemas.Education ed : profile.getEducation())
			{
				es.uca.pfc.Education education = convertEducationMendeleyToEducationSlr(ed)
				userProfile.addToEducations(education)
			}
		}
		
		return userProfile
	}
	
	boolean isChangePasswordMendeley(String email, String password)
	{
		return isRegisteredMendeley(email, password)
	}
	
	boolean saveReferenceMendeley(Reference reference, User userInstance)
	{
		boolean isSaved = false;
		String clientId = Holders.getGrailsApplication().config.clientId
		String clientSecret = Holders.getGrailsApplication().config.clientSecret
		String redirectUri = Holders.getGrailsApplication().config.redirectUri
		
		try
		{
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend));
			
			// Actualizamos los tokens del usuario
			userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
			userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
			userInstance.save(failOnError: true, flush: true)
			
			DocumentService documentService = new DocumentService(mendeleyService)
			
			Document docOld = documentService.getDocument(reference.idmend.toString())
			Document document = new Document()
			
			document.setTitle(reference.title == null ? "" : reference.title)
			document.setAbstract(reference.docAbstract)
			document.setSource(reference.source)
			document.setPages(reference.pages)
			document.setVolume(reference.volume)
			document.setIssue(reference.issue)
			document.setPublisher(reference.publisher)
			document.setCity(reference.city)
			document.setInstitution(reference.institution)
			document.setSeries(reference.series)
			document.setChapter(reference.chapter)
			document.setCitationKey(reference.citation_key)
			document.setSourceType(reference.source_type)
			document.setGenre(reference.genre)
			document.setCountry(reference.country)
			document.setDepartment(reference.department)
			
			Identifier identifier = new Identifier()
			identifier.setArxiv(reference.arxiv)
			identifier.setDoi(reference.doi)
			identifier.setIsbn(reference.isbn)
			identifier.setIssn(reference.issn)
			identifier.setPmid(reference.pmid)
			identifier.setScopus(reference.scopus)
			document.setIdentifiers(identifier)
			
			//document.setMonth(Integer.parseInt(reference.month))
			document.setDay(Integer.parseInt(reference.day))
			document.setFileAttached(reference.file_attached)
			document.setYear(reference.year)
			
			//Keywords
			document.setKeywords(reference.keywords.toList())
			
			//Websites
			document.setWebsites(reference.websites.toList())
			
			//Tags
			document.setTags(reference.tags.toList())
			
			//Type
			document.setType(mendeley.pfc.commons.TypeDocument.fromKey(reference.type.nomenclatura))
			
			//Language
			document.setLanguage(reference.language.name)
			
			//Engine
			// ...
			
			//Autores
			List<Person> authors = new ArrayList<Person>()
			for(AuthorReference ar : reference.authorsRefs)
			{
				Person person = new Person(ar.author.forename, ar.author.surname)
				authors.add(person)
			}
			document.setAuthors(authors)
			
			documentService.updateDocument(docOld, document)
			
			isSaved = true;
		}
		catch(Exception ex)
		{
			println "ERROR: MendeleyToolService.saveReferenceMendeley() -> " + ex.getMessage()
		}
		
		return isSaved;
	}
}
