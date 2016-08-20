package es.uca.pfc

import java.util.Date;

import mendeley.pfc.schemas.Profile
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
	
	void synchronizeProfile(User userInstance)
	{
		try
		{
			String clientId = Holders.getGrailsApplication().config.clientId
			String clientSecret = Holders.getGrailsApplication().config.clientSecret
			String redirectUri = Holders.getGrailsApplication().config.redirectUri
			
			MendeleyService mendeleyService = new MendeleyService(clientId, clientSecret, redirectUri, 
				userInstance.userMendeley.email_mend, userInstance.userMendeley.pass_mend, 
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
		}
		catch(MendeleyException ex)
		{
			println "Hubo un problema de sincronizacion."
		}
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
		
		userProfile.educations.clear()
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
}
