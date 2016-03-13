package es.uca.pfc.jobs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.uca.pfc.task.TaskEngineSearch;
import es.uca.pfc.task.TaskReference;
import es.uca.pfc.task.TaskSearch;

public class TaskBackgroundJob implements Job
{
	public TaskBackgroundJob() {
		
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		//JobKey key = context.getJobDetail().getKey();		
		JobDataMap dataMap = context.getMergedJobDataMap();
		
	    Gson gson = new Gson();
		Type typeListEngine = new TypeToken<List<TaskEngineSearch>>(){}.getType();
		List<TaskEngineSearch> engines = gson.fromJson(dataMap.getString("engines"), typeListEngine);
		String guidSlr = dataMap.getString("guidSlr");
		String terminos = dataMap.getString("terminos");
		String operator = dataMap.getString("operator");
		String component = dataMap.getString("component");
		String minYear = dataMap.getString("minYear");
		String maxYear = dataMap.getString("maxYear");
		String maxTotal = dataMap.getString("maxTotal");
		String username = dataMap.getString("username");

		List<TaskSearch> taskSearchs = getTaskSearchs(engines, guidSlr, terminos, operator, component, minYear, maxYear, maxTotal);
		
		context.getMergedJobDataMap().put("taskSearchs", gson.toJson(taskSearchs));
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<TaskSearch> getTaskSearchs(List<TaskEngineSearch> engines, String guidSlr, String terminos, String operator, String component, String minYear, String maxYear, String maxTotal)
	{
		List<TaskSearch> taskSearchs = new ArrayList<TaskSearch>();
		
		for(TaskEngineSearch engine : engines)
		{
			TaskSearch taskSearch = new TaskSearch();
			taskSearch.setEngine(engine);
			taskSearch.setComponent(component);
			taskSearch.setEndYear(maxYear);
			taskSearch.setGuidSlr(guidSlr);
			taskSearch.setMaxTotal(Integer.parseInt(maxTotal));
			taskSearch.setOperator(operator);
			taskSearch.setStartYear(minYear);
			taskSearch.setTerminos(terminos);
			
			getTaskReferences(taskSearch);
			
			taskSearchs.add(taskSearch);
		}
		
		return taskSearchs;
	}
	
	private void getTaskReferences(TaskSearch taskSearch)
	{
		List<TaskReference> taskReferences = new ArrayList<TaskReference>();
		
		for(int i=0; i<taskSearch.getMaxTotal(); i++)
		{
			TaskReference reference = new TaskReference();
			
			reference.setTypeDocument("journal");
			reference.setLanguage("english");
			reference.setIdmend(UUID.randomUUID().toString());
			reference.setTitle("Title"+UUID.randomUUID().toString());
			reference.setCitation_key("CK"+UUID.randomUUID().toString());
			reference.setBibtex(UUID.randomUUID().toString());
			taskReferences.add(reference);
		}
		taskSearch.setReferences(taskReferences);
	}
}
