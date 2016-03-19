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
		
    /*def serviceMethod() {

    }*/
	
	boolean insertSearchsBackground(String guidSlr, String terminos, String operator, List<EngineSearch> engines, String component, String minYear, String maxYear, String maxTotal)
	{
		def usuarioInstance = User.get(springSecurityService.currentUser.id)
		
		runAsync {
			// Proceso en segundo plano
			//procesoSegundoPlano(usuarioInstance.username)
			crearBusquedas(usuarioInstance.username, guidSlr, terminos, operator, engines, component, minYear, maxYear, maxTotal)
		}
		
		return true;
	}
	
	void crearBusquedas(String username, String guidSlr, String terminos, String operator, List<EngineSearch> engines, String component, String minYear, String maxYear, String maxTotal)
	{
		SearchComponent searchComponent = SearchComponent.findByValueLike(component)
		SearchOperator searchOperator = SearchOperator.findByValueLike(operator)
		Slr slrInstance = Slr.findByGuidLike(guidSlr)
		def langES = Language.findByCodeLike('ES')
		def langEN = Language.findByCodeLike('EN')
		def langFR = Language.findByCodeLike('FR')
		def type01 = TypeDocument.findByNombreLike('Journal')
		def type02 = TypeDocument.findByNombreLike('Book')
		def author01 = Author.findByForenameLikeAndSurnameLike('Angel','Gonzalez')
		def author02 = Author.findByForenameLikeAndSurnameLike('Aradia','Rocha')
		
		Random rnd = new Random()
		
		println "TOTAL ENGINES: " + engines.size()
		println "TOTAL REFS/BUSQ: " + maxTotal
		
		for(EngineSearch engine : engines)
		{
			Search searchInstance = new Search(terminos: terminos, startYear: minYear, endYear: maxYear, maxTotal: maxTotal,
												engine: engine, component: searchComponent, operator: searchOperator,
												slr: slrInstance)
			for(int i = 0; i<maxTotal; i++)
			{
				println "Engine " + engine.name + ": Insertando referencia " + (i+1) + " de " + maxTotal + "."
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
															bibtex: 'Bibtex '+idMend)
				
				//searchInstance.addToReferences(referenceInstance).save(failOnError: true)
				searchInstance.addToReferences(referenceInstance)
				
				if(author.id == author01.id)
				{
					author01.addToAuthorsRefs(reference: referenceInstance).save(failOnError: true)
				}
				else
				{
					author02.addToAuthorsRefs(reference: referenceInstance).save(failOnError: true)
				}
			} // for i reference
			searchInstance.save(failOnError: true)
		} //for-engines
		
		println "PROCESO DE BUSQUEDAS COMPLETADO"
	}
	
	public static void convertTaskSearchsToSearchs(List<TaskSearch> taskSearchs, String username)
	{
		println username + ": convertTaskSearchsToSearchs"
		/*println (new Date()) + "USUARIO: " + username
		for(TaskSearch taskSearch : taskSearchs)
		{
			println "ENGINE: " + taskSearch.getEngine().getName()
			println "Se han encontrado " + taskSearch.getReferences().size() + " referencias."
		}*/
		for(int i=0; i<taskSearchs.size(); i++)
		{
			//Book book = new Book(name: UUID.randomUUID().toString())
			Book book = new Book(name: username+":::"+i)
			book.save(flush: true)
		}
	}
}
