package es.uca.pfc

import java.util.Date;

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
import es.uca.pfc.jobs.IMendeleyService
import es.uca.pfc.jobs.TaskBackgroundJob;
import es.uca.pfc.jobs.TaskBackgroundJobListener
import es.uca.pfc.task.TaskEngineSearch
import es.uca.pfc.task.TaskSearch
import grails.transaction.Transactional;

import org.quartz.impl.matchers.KeyMatcher;

import com.google.gson.Gson;

@Transactional
class MendeleyService /*implements IMendeleyService*/ {
	
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
	
	public static void convertTaskSearchsToSearchs(List<TaskSearch> taskSearchs, String username)
	{
		
	}
}
