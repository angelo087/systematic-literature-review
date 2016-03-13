package es.uca.pfc.jobs;

import java.lang.reflect.Type;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.uca.pfc.MendeleyService;
import es.uca.pfc.task.TaskSearch;

public class TaskBackgroundJobListener implements JobListener {

	public static final String LISTENER_NAME = "dummyJobListenerName";
	
	@Override
	public String getName() {
		return LISTENER_NAME;
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		System.out.println("jobExecutionVetoed");
	}

	// Run this if job is about to be executed.
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " is going to start...");
	}

	//Run this after job has been executed
	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {

		if (null != jobException)
		{
			System.out.println("Exception: " + jobException.getMessage());
		}
		else
		{
			JobDataMap dataMap = context.getMergedJobDataMap();

			Gson gson = new Gson();
			Type typeListSearch = new TypeToken<List<TaskSearch>>(){}.getType();
			List<TaskSearch> taskSearchs = gson.fromJson(dataMap.getString("taskSearchs"), typeListSearch);
			String username = dataMap.getString("username");
			
			MendeleyService.convertTaskSearchsToSearchs(taskSearchs, username);
		}
	}
}
