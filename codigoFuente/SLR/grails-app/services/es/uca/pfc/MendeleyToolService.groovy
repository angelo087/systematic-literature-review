package es.uca.pfc

import java.lang.annotation.Documented;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

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
import es.uca.pfc.TaskSearch
import grails.transaction.Transactional;

import org.quartz.impl.matchers.KeyMatcher;

import background.pfc.enums.TypeEngineSearch;
import background.pfc.main.BackgroundSearchMendeley
import background.pfc.tests.BackgroundSearchMendeleyTest;

import com.google.gson.Gson;

@Transactional
class MendeleyToolService {
	
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
	
	boolean insertSearchsBackground(String guidTaskSearch, String nameSlr, String guidSlr, List<String> terminos, List<SearchOperator> operators, 
		List<SearchComponent> components, String minYear, String maxYear, String maxTotal, Map<String, Boolean> engines)
	{
		println "insertSearchsBackground"
		
		increaseProgressBar(0, guidTaskSearch)
		boolean isSuccess = true
		def userInstance = User.get(springSecurityService.currentUser.id)
		String emailMend = userInstance.userMendeley.email_mend
		String passMend = decodePasswordMendeley(userInstance.userMendeley.pass_mend)
		
		runAsync {
			try
			{
				// Obtenemos referencias para mendeley
				increaseProgressBar(25, guidTaskSearch, null)
				List<background.pfc.commons.Reference> referencesMend = createSearchInMendeley(emailMend, passMend, nameSlr, terminos,
					operators, components, minYear, maxYear, maxTotal, engines)
								
				// Creamos busqueda para el SLR
				increaseProgressBar(50, guidTaskSearch, null)
				Search searchInstance = createSearchForSlr(terminos, operators, components,
															minYear, maxYear, maxTotal, engines)
				
				// Insertamos las referencias en la busqueda
				increaseProgressBar(75, guidTaskSearch, null)
				createSearchFromMendeley(emailMend, searchInstance, guidSlr, referencesMend)
				
				// Finalizamos proceso
				increaseProgressBar(100, guidTaskSearch, null)
			}
			catch(Exception ex)
			{
				isSuccess = false
				println "ERROR => " + ex.getMessage()
				increaseProgressBar(-100, guidTaskSearch, ex.getMessage())
			}
			sendSearchNotification(emailMend, guidSlr, isSuccess)
			sendSearchLogger(emailMend, guidSlr, isSuccess)
		}
		
		return isSuccess;
	}
	
	void increaseProgressBar(int percen, String guidTaskSearch, String strException)
	{
		def taskSearchInstance = TaskSearch.findByGuidLike(guidTaskSearch)
		
		if (taskSearchInstance != null)
		{
			def state = ""
			def hasErrors = false
			def percentage = percen
			
			if (percentage < 0)
			{
				percentage = 0
				state = "Ha habido un fallo en el proceso de busqueda."
				hasErrors = true;
			}
			else if(percentage >= 0 && percentage < 25)
			{
				state = "Conectando con los engines..."
			}
			else if (percentage >= 25 && percentage < 50)
			{
				state = "Realizando busqueda en los engines..."
			}
			else if (percentage >= 50 && percentage < 75)
			{
				state = "Recopilando las referencias..."
			}
			else if (percentage >= 75 && percentage < 100)
			{
				state = "Guardando referencias en SLR..."
			}
			else if (percentage == 100)
			{
				state = "Finalizado!"
			}

			taskSearchInstance.percentage = percentage
			taskSearchInstance.state = state
			taskSearchInstance.hasErrors = hasErrors
			taskSearchInstance.endDate = new Date()
			if(strException != null)
			{
				taskSearchInstance.strException = strException
			}
			taskSearchInstance.save(failOnError: true, flush: true)
		}
	}
	
	Search createSearchForSlr(List<String> terminos, List<SearchOperator> operators,
		List<SearchComponent> components, String minYear, String maxYear, String maxTotal, Map<String, Boolean> engines)
	{
		Search searchInstance = new Search(startYear: minYear, endYear: maxYear, maxTotal: Integer.parseInt(maxTotal));
		
		// Insertamos los engines
		for(Map.Entry<String, Boolean> entry : engines.entrySet())
		{
			if(entry.getValue())
			{
				def engineInstance = EngineSearch.findByNameIlike(entry.getKey())
				
				if(engineInstance != null)
				{
					searchInstance.addToEngines(engineInstance);
				}
			}
		}
		
		// Insertamos los SearchTermParams
		for(int i = 0; i < terminos.size(); i++)
		{
			def searchTermParam = new SearchTermParam(terminos: terminos.get(i), 
				component: components.get(i), operator: operators.get(i))
			
			if (searchTermParam != null)
			{
				searchInstance.addToTermParams(searchTermParam)
			}
		}
				
		return searchInstance;
	}
	
	void createSearchFromMendeley(String emailMend, Search searchInstance, String guidSlr, 
		List<background.pfc.commons.Reference> referencesMend)
	{
		User userInstance = User.findByUsernameLike(emailMend)
		
		if (userInstance == null)
		{
			throw new Exception("User is null");
		}
		
		// Cargamos los datos de mendeley
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
			userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
			userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
		
		for(background.pfc.commons.Reference ref : referencesMend)
		{
			Reference reference = convertReferenceMendToReference(ref, userInstance)
			searchInstance.addToReferences(reference)
		}
		
		Slr slrInstance = Slr.findByGuidLike(guidSlr)
		
		slrInstance.addToSearchs(searchInstance)
		
		slrInstance.save(failOnError: true)
		
		// Insertamos referencias con autores
		insertAuthorsToReferences(searchInstance, userInstance)
	}
	
	void insertAuthorsToReferences(Search searchInstance, User userInstance)
	{
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
			userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
			userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
		
		DocumentService documentService = new DocumentService(mendeleyService);
		
		for(Reference ref : searchInstance.references)
		{
			AuthorReference.deleteAll(AuthorReference.findAllByReference(ref))
			ref.save(flush: true)
			List<Author> authors = new ArrayList<Author>()
			
			Document docMend = documentService.getDocument(ref.idmend)
			
			if(docMend != null && docMend.getAuthors() != null && docMend.getAuthors().size() > 0)
			{
				for(mendeley.pfc.schemas.Person person : docMend.getAuthors())
				{
					if ((person.getForename() != null && !person.getForename().equals("")) ||
						(person.getSurname() != null && !person.getSurname().equals("")))
					{
						List<String> names = toolService.extractForenameAndSurnameAuthor(person.getForename(), person.getSurname())
						String forenamePerson = names.get(0)
						String surnamePerson = names.get(1)
						
						def authorInstance = Author.findByForenameIlikeAndSurnameIlike(forenamePerson, surnamePerson)
						
						if (authorInstance == null)
						{
							authorInstance = new Author(forename: forenamePerson, surname: surnamePerson).save(failOnError: true)
						}
						
						authors.add(authorInstance)
						//authorInstance.addToAuthorsRefs(reference: ref).save(failOnError: true)
					}
				}
				
				for(Author author : authors)
				{
					ref.addToAuthorsRefs(author: author).save(failOnError: true, flush: true)
				}
			}
		}
	}
	
	Reference convertReferenceMendToReference(background.pfc.commons.Reference refMend, User userInstance)
	{
		Reference reference = null;
		
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
			userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
			userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
		
		DocumentService documentService = new DocumentService(mendeleyService);
		Document documentMend = documentService.getDocument(refMend.getIdMendeley())
		
		if (documentMend == null)
		{
			throw new Exception("Document with id=" + refMend.getIdMendeley() + " did not found.")
		}
		else
		{
			reference = new Reference(
				idmend: refMend.getIdMendeley()
				);
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			reference.title = toolService.getStringIfNotNull(documentMend.getTitle())
			
			if (documentMend.getCreated() != null)
			{
				try
				{
					reference.created = df.format(documentMend.getCreated());
				}
				catch(Exception ex) { reference.created = new Date(); }
			}
			if (documentMend.getLastModified() != null)
			{
				try
				{
					reference.last_modified = df.format(documentMend.getLastModified());
				}
				catch(Exception ex) { reference.last_modified = new Date() }
			}
			
			reference.docAbstract = toolService.getStringIfNotNull(documentMend.getAbstract());
			reference.source = toolService.getStringIfNotNull(documentMend.getSource());
			
			if (documentMend.getYear() != null)
			{
				reference.year = Integer.toString(documentMend.getYear())
			}
			
			reference.pages = toolService.getStringIfNotNull(documentMend.getPages());
			reference.volume = toolService.getStringIfNotNull(documentMend.getVolume());
			reference.issue = toolService.getStringIfNotNull(documentMend.getIssue());
			reference.publisher = toolService.getStringIfNotNull(documentMend.getPublisher());
			reference.city = toolService.getStringIfNotNull(documentMend.getCity());
			reference.institution = toolService.getStringIfNotNull(documentMend.getInstitution());
			reference.series = toolService.getStringIfNotNull(documentMend.getSeries());
			reference.chapter = toolService.getStringIfNotNull(documentMend.getChapter());
			reference.citation_key = toolService.getStringIfNotNull(documentMend.getCitationKey());
			reference.source_type = toolService.getStringIfNotNull(documentMend.getSourceType());
			reference.genre = toolService.getStringIfNotNull(documentMend.getGenre());
			reference.country = toolService.getStringIfNotNull(documentMend.getCountry());
			reference.department = toolService.getStringIfNotNull(documentMend.getDepartment());
			if(documentMend.getIdentifiers() != null)
			{
				reference.arxiv = toolService.getStringIfNotNull(documentMend.getIdentifiers().getArxiv());
				reference.doi = toolService.getStringIfNotNull(documentMend.getIdentifiers().getDoi());
				reference.isbn = toolService.getStringIfNotNull(documentMend.getIdentifiers().getIsbn());
				reference.issn = toolService.getStringIfNotNull(documentMend.getIdentifiers().getIssn());
				reference.pmid = toolService.getStringIfNotNull(documentMend.getIdentifiers().getPmid());
				reference.scopus = toolService.getStringIfNotNull(documentMend.getIdentifiers().getScopus());
			}

			reference.month = toolService.getStringIfNotNull(documentMend.getMonthName("en"));
			reference.day = toolService.getStringIfNotNull(Integer.toString(documentMend.getDay()));
			reference.file_attached = documentMend.getFileAttached()
			reference.bibtex = toolService.getStringIfNotNull(documentMend.getBibtex());
			
			def typeDocument = es.uca.pfc.TypeDocument.findByNomenclaturaIlike("%" + documentMend.getType().getKey() + "%")
			if(typeDocument != null)
			{
				reference.type = typeDocument
			}
			else
			{
				reference.type = es.uca.pfc.TypeDocument.findByNomenclaturaIlike("journal")
			}
			
			if(documentMend.getKeywords() != null && documentMend.getKeywords().size() > 0)
			{
				for(String k : documentMend.getKeywords())
				{
					reference.addToKeywords(k)
				}
			}
			
			if(documentMend.getWebsites() != null && documentMend.getWebsites().size() > 0)
			{
				for(String w : documentMend.getWebsites())
				{
					reference.addToWebsites(w)
				}
			}
			
			if(documentMend.getTags() != null && documentMend.getTags().size() > 0)
			{
				for(String t : documentMend.getTags())
				{
					reference.addToTags(t)
				}
			}
			
			String lang = "english"
			if(documentMend.getLanguage() != null && !documentMend.getLanguage().equals(""))
			{
				lang = documentMend.getLanguage()
			}
			
			def languageInstance = Language.findByNameIlike("%" + lang + "%")
			if(languageInstance != null)
			{
				reference.language = languageInstance;
			}
			else
			{
				reference.language = Language.findByName('english');
			}
			
			if (refMend.getTypeEngineSearch() != null)
			{
				def engineSearch = EngineSearch.findByNameIlike("%" + refMend.getTypeEngineSearch().getKey() + "%")
				
				if (engineSearch != null)
				{
					reference.engine = engineSearch;
				}
				else
				{
					reference.engine = EngineSearch.findByName('OTHER')
				}
			}
			
			// Criterios y atributos especificos se insertan en Reference.beforeInsert
		}
		
		return reference;
	}
	
	List<background.pfc.commons.Reference> createSearchInMendeley(String emailMend, String passMend, String nameSlr, List<String> terminos, List<SearchOperator> operators, 
		List<SearchComponent> components, String minYear, String maxYear, String maxTotal, Map<String, Boolean> engines)
	{
		List<background.pfc.commons.Reference> referencesImported = new ArrayList<background.pfc.commons.Reference>();
		
		// Obtenemos datos de api Mendeley
		def mendeleyApi = MendeleyApi.list().first()
		
		// Obtenemos map de los engines donde buscar
		Map<TypeEngineSearch, Boolean> optionsEngine = new HashMap<TypeEngineSearch, Boolean>();
		for(Map.Entry<String, Boolean> entry : engines.entrySet())
		{
			optionsEngine.put(TypeEngineSearch.fromKey(entry.getKey().toLowerCase()), entry.getValue())
		}
		
		// Obtenemos los api engines
		def engineSearchList = EngineSearch.findAllByNameNotEqual('OTHER', [sort: 'name', order: 'asc'])
		Map<TypeEngineSearch, String> apiKeysEngine = new HashMap<TypeEngineSearch, String>();
		for(EngineSearch engine : engineSearchList)
		{
			String apiKeyValue = ( engine.apiKey == null ? "" : engine.apiKey )
			apiKeysEngine.put(TypeEngineSearch.fromKey(engine.name.toLowerCase()), apiKeyValue)
		}
		
		List<background.pfc.commons.SearchTermParam> termsMendeley = new ArrayList<background.pfc.commons.SearchTermParam>();
		List<es.uca.pfc.SearchTermParam> termsSLR = new ArrayList<es.uca.pfc.SearchTermParam>();
		int index = 0
		for(String termino : terminos)
		{
			background.pfc.commons.SearchTermParam termMendeley = new background.pfc.commons.SearchTermParam();
			
			termMendeley = new background.pfc.commons.SearchTermParam();
			termMendeley.setTerminos(termino)
			termMendeley.setOperatorSearch(background.pfc.enums.OperatorSearch.fromKey(operators.get(index).value.toString()))
			termMendeley.setComponentSearch(background.pfc.enums.ComponentSearch.fromKey(components.get(index).value.toString()))
			
			termsMendeley.add(termMendeley)
			
			index++;
		}

		//try
		//{
			BackgroundSearchMendeley backgroundMendeley = new BackgroundSearchMendeley(
				mendeleyApi.clientId,
				mendeleyApi.clientSecret,
				"token", "token",
				mendeleyApi.redirectUri,
				emailMend,
				passMend,
				optionsEngine,
				nameSlr,
				Integer.parseInt(maxTotal),
				new ArrayList<String>(),
				Integer.parseInt(minYear),
				Integer.parseInt(maxYear),
				termsMendeley,
				apiKeysEngine,
				mendeleyApi.totalHilos, mendeleyApi.totalTries)
			
			backgroundMendeley.startSearchs()
			
			referencesImported = backgroundMendeley.getReferences()
		//}
		//catch(Exception ex)
		//{
		//	println "Hay algun error => " + ex.getMessage()
		//}
		
		return referencesImported;
	}
	
	@Transactional
	void sendSearchLogger(String emailMend, String guidSlr, boolean success)
	{
		Slr slrInstance = Slr.findByGuidLike(guidSlr)
		try
		{
			if (success)
			{
				User userInstance = User.findByUsernameIlike(emailMend)
				
				if (userInstance != null)
				{
					userInstance.userProfile.addToLoggers(new LoggerSlr(slr: slrInstance, isSearch: true, tipo: 'buscar')).save(failOnError: true)
				}
			}
		}
		catch(Exception ex)
		{
			println "Logger NO creado para " + slrInstance.title			
		}
	}
	
	@Transactional
	void sendSearchNotification(String emailMend, String guidSlr, boolean success)
	{
		Slr slrInstance = Slr.findByGuidLike(guidSlr)
		try
		{
			String asunto = "Nuevas búsquedas"
			String txt = ""
			if (!success)
			{
				asunto = "Error en búsquedas"
				txt = "Ha habido problemas en la realización de búsquedas en " + slrInstance.title
			}
			else
			{
				txt = "Se han realizado nuevas búsquedas para " + slrInstance.title
			}
	
			User userInstance = User.findByUsernameIlike(emailMend)
			
			if (userInstance != null)
			{
				UserProfile userProfile = userInstance.userProfile
				userProfile.addToNotifications(new NotificationSlr(tipo: "search", asunto: asunto, texto: txt, slr: slrInstance, leido: false))
				userProfile.save(failOnError: true)
				println "Notificacion enviada para " + slrInstance.title
			}
			else
			{
				println "Notificacion NO enviada para " + slrInstance.title
			}
		}
		catch(Exception ex)
		{
			println "Notificacion NO enviada para " + slrInstance.title
		}
	}
	
	boolean synchronizeProfile(User userInstance)
	{
		boolean isSynchro = false;
		try
		{
			MendeleyApi mendeleyApi = MendeleyApi.list().first()
			String clientId = mendeleyApi.clientId
			String clientSecret = mendeleyApi.clientSecret
			String redirectUri = mendeleyApi.redirectUri
			
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
		userProfile.first_name = toolService.getStringIfNotNull(profile.getFirstName())
		userProfile.last_name = toolService.getStringIfNotNull(profile.getLastName())
		userProfile.display_name = toolService.getStringIfNotNull(profile.getDisplayName())
		userProfile.url_foto = (profile.getPhoto() == null ? "unknown_user.png" : (profile.getPhoto().getUrl() == null ? "unknown_user.png" : profile.getPhoto().getUrl().toString()))
		userProfile.idmend = toolService.getStringIfNotNull(profile.getId())
		userProfile.research_interests = toolService.getStringIfNotNull(profile.getResearchInterests())
		userProfile.academic_status = toolService.getStringIfNotNull(profile.getAcademicStatus())
		userProfile.link = toolService.getStringIfNotNull(profile.getLink())
		userProfile.created = toolService.getStringIfNotNull(profile.getCreated())
		userProfile.biography = toolService.getStringIfNotNull(profile.getBiography())
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
			MendeleyApi mendeleyApi = MendeleyApi.list().first()
			String clientId = mendeleyApi.clientId
			String clientSecret = mendeleyApi.clientSecret
			String redirectUri = mendeleyApi.redirectUri
			
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
			MendeleyApi mendeleyApi = MendeleyApi.list().first()
			String clientId = mendeleyApi.clientId
			String clientSecret = mendeleyApi.clientSecret
			String redirectUri = mendeleyApi.redirectUri
			
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
					deleteSlr(slrInstance, userInstance)
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

							if(document == null || document.getId() == null)
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

					// Borramos la referencia de search
					Search searchInstance = reference.search
					searchInstance.references.remove(reference)
					
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
		
		println "FIN SINCRONIZACION"

		return isSynchro;
	}
	
	boolean updateReferenceFromMendeley(Reference reference, User userInstance)
	{
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		boolean isOnMendeley = true
		
		MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
			userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
			userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
		
		// Actualizamos los tokens del usuario
		userInstance.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken();
		userInstance.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken();
		userInstance.save(failOnError: true, flush: true)
		
		DocumentService documentService = new DocumentService(mendeleyService)
		Document document = documentService.getDocument(reference.idmend.toString())
		
		if (document != null && document.getId() != null && !document.getId().equals(""))
		{
			updateReferenceFromMendeley(reference, documentService)
		}
		else
		{
			isOnMendeley = false
			// Borramos las referencias con los autores
			AuthorReference.deleteAll(AuthorReference.findAllByReference(reference))
			
			// Borramos las referencias con los atributos especificos
			SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReference(reference))
			
			reference.delete flush: true
		}
		
		return isOnMendeley
	}
	
	void updateReferenceFromMendeley(Reference reference, DocumentService documentService)
	{
		Document document = documentService.getDocument(reference.idmend.toString())
		
		reference.idmend = document.getId()
		reference.title = toolService.getStringIfNotNull(document.getTitle())
		reference.docAbstract = toolService.getStringIfNotNull(document.getAbstract())
		reference.source = toolService.getStringIfNotNull(document.getSource())
		reference.pages = toolService.getStringIfNotNull(document.getPages())
		reference.volume = toolService.getStringIfNotNull(document.getVolume())
		reference.issue = toolService.getStringIfNotNull(document.getIssue())
		reference.publisher = toolService.getStringIfNotNull(document.getPublisher())
		reference.city = toolService.getStringIfNotNull(document.getCity())
		reference.institution = toolService.getStringIfNotNull(document.getInstitution())
		reference.series = toolService.getStringIfNotNull(document.getSeries())
		reference.chapter = toolService.getStringIfNotNull(document.getChapter())
		reference.citation_key = toolService.getStringIfNotNull(document.getCitationKey())
		reference.source_type = toolService.getStringIfNotNull(document.getSourceType())
		reference.genre = toolService.getStringIfNotNull(document.getGenre())
		reference.country = toolService.getStringIfNotNull(document.getCountry())
		reference.department = toolService.getStringIfNotNull(document.getDepartment())
		
		if(document.getIdentifiers() != null)
		{
			reference.arxiv = toolService.getStringIfNotNull(document.getIdentifiers().getArxiv())
			reference.doi = toolService.getStringIfNotNull(document.getIdentifiers().getDoi())
			reference.isbn = toolService.getStringIfNotNull(document.getIdentifiers().getIsbn())
			reference.issn = toolService.getStringIfNotNull(document.getIdentifiers().getIssn())
			reference.pmid = toolService.getStringIfNotNull(document.getIdentifiers().getPmid())
			reference.scopus = toolService.getStringIfNotNull(document.getIdentifiers().getScopus())
		}

		reference.month = toolService.getStringIfNotNull(document.getMonthName("en"))
		reference.day = toolService.getStringIfNotNull(Integer.toString(document.getDay()))
		reference.file_attached = document.getFileAttached()
		reference.bibtex = toolService.getStringIfNotNull(document.getBibtex())
		
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
			def typeRef = TypeDocument.findByNomenclaturaLike("%"+document.getType().getKey().toLowerCase()+"%")
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
		reference.authorsRefs.removeAll(AuthorReference.findAllByReference(reference))
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
		
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
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
		
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
		try
		{
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				emailMend, passMend);
			ProfileService profileService = new ProfileService(mendeleyService)			
			Profile profile = profileService.getCurrentProfile();
			
			// Perfil de Mendeley
			UserProfile userProfileInstance = getUserProfileFromMendeley(profile)
			UserMendeley userMendeleyInstance = new UserMendeley(
																	email_mend: emailMend,
																	pass_mend: encodePasswordMendeley(passMend),
																	access_token: mendeleyService.getTokenResponse().getAccessToken(),
																	token_type: mendeleyService.getTokenResponse().getTokenType(),
																	expires_in: mendeleyService.getTokenResponse().getExpiresIn(),
																	refresh_token: mendeleyService.getTokenResponse().getRefreshToken())
			// User Settings
			//UserSetting userSettingInstance = new UserSetting(langWeb: Language.findByNameIlike('english').code.toLowerCase(), autoSynchro: false)
			
			// Save User
			userInstance = new User(
									username: emailMend,
									password: passMend,
									enabled: true,
									userProfile: userProfileInstance,
									userMendeley: userMendeleyInstance/*,
									userSetting: userSettingInstance*/
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
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
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
	
	boolean deleteSlr(Slr slrInstance, User userInstance)
	{
		boolean isDeleted = false;
		
		MendeleyApi mendeleyApi = MendeleyApi.list().first()
		String clientId = mendeleyApi.clientId
		String clientSecret = mendeleyApi.clientSecret
		String redirectUri = mendeleyApi.redirectUri
		
		try
		{
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri,
				userInstance.userMendeley.email_mend, decodePasswordMendeley(userInstance.userMendeley.pass_mend),
				userInstance.userMendeley.access_token, userInstance.userMendeley.refresh_token);
			
			FolderService folderService = new FolderService(mendeleyService);
			DocumentService documentService = new DocumentService(mendeleyService);
			
			Folder folder = folderService.getFolderById(slrInstance.idmend)
			
			// Borramos los documentos del SLR
			folderService.deleteAllDocument(folder)
			//documentService.deleteDocumentsFromFolder(folder);
			
			// Borramos la carpeta con sus subcarpetas
			folderService.deleteFolder(folder)
			isDeleted = true;
		}
		catch(Exception ex)
		{
			isDeleted = false;
		}
		
		return isDeleted;
	}
}
