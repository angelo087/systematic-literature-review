package es.uca.pfc.jobs;

import java.util.List;

import es.uca.pfc.EngineSearch;
import es.uca.pfc.task.TaskSearch;

public interface IMendeleyService {
	
	public boolean insertSearchsBackground(String guidSlr, String terminos, String operator, List<EngineSearch> engines, String component, String minYear, String maxYear, String maxTotal);
	public void convertTaskSearchsToSearchs(List<TaskSearch> taskSearchs);
}
