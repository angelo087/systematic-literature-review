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
			procesoSegundoPlano(usuarioInstance.username)
		}
		
		return true;
	}
	
	void procesoSegundoPlano(String username)
	{
		List<String> titles = getTitlesBooks(username);
		
		if(titles.size() > 0)
		{
			for(String title : titles)
			{
				Book book = new Book(name: title)
				book.save(flush: true)
			}
		}
	}
	
	List<String> getTitlesBooks(String username)
	{
		List<String> titles = new ArrayList<String>();
		
		for(int i=0; i<3; i++)
		{
			titles.add(UUID.randomUUID().toString())
			Thread.sleep(10000);
			println username + ": Insertado Referencia " + (i+1) + " de 3."
		}
		
		return titles;
	}
	
	/*boolean insertSearchsBackground(String guidSlr, String terminos, String operator, List<EngineSearch> engines, String component, String minYear, String maxYear, String maxTotal)
	{
		def usuarioInstance = User.get(springSecurityService.currentUser.id)
		
		List<TaskEngineSearch> taskEngines = new ArrayList<TaskEngineSearch>();
		
		for(EngineSearch engine : engines)
		{
			TaskEngineSearch eng = new TaskEngineSearch();
			eng.setName(engine.name);
			eng.setUrl(engine.url);
			taskEngines.add(eng);
		}
		
		SchedulerFactory schedFact = new StdSchedulerFactory();
		Scheduler sched = schedFact.getScheduler();
		
		String name = UUID.randomUUID().toString();
		String group = UUID.randomUUID().toString();
		Gson gson = new Gson();
		
		JobKey jobKey = new JobKey("Job-"+name, "JGroup-"+group);
		JobDetail job = newJob(TaskBackgroundJob.class)
		.usingJobData("username", usuarioInstance.username.toString())
		.usingJobData("guidSlr", guidSlr)
		.usingJobData("terminos", terminos)
		.usingJobData("operator", operator)
		.usingJobData("component", component)
		.usingJobData("minYear", minYear)
		.usingJobData("maxYear", maxYear)
		.usingJobData("maxTotal", maxTotal)
		.usingJobData("engines", gson.toJson(taskEngines))
		.withIdentity(jobKey)
		.build();
		
		Trigger trigger = newTrigger()
		.withIdentity("Trigger-"+name, "TGroup-"+group)
		.startNow()
		.build();
		
		sched.getListenerManager().addJobListener(
			new TaskBackgroundJobListener(), KeyMatcher.keyEquals(jobKey)
		);
	
		boolean flag = sched.checkExists(trigger.getKey())
		
		while(flag)
		{
			sched.interrupt(jobKey)
			name = UUID.randomUUID().toString();
			group = UUID.randomUUID().toString();
			trigger = newTrigger()
			.withIdentity("Trigger-"+name, "TGroup-"+group)
			.startNow()
			.build();
			flag = sched.checkExists(trigger.getKey())
		}
		
		sched.start();
		sched.scheduleJob(job, trigger);
		
		println (new Date()) + ": Sali de MendeleyService()"
		
		return true;
	}*/
	
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
