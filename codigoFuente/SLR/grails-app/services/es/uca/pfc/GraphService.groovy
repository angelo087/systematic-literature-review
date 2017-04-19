package es.uca.pfc

import grails.transaction.Transactional
import java.text.SimpleDateFormat

@Transactional
class GraphService {

	def toolService
	
    def serviceMethod() {

    }
	
	private String labelCriterionIncluded = "Criterion Included"
	private String labelCriterionNoIncluded = "Criterion No Included"
	
	List<Integer> getMinAndMaxYear(Slr slrInstance)
	{
		List<Integer> years = new ArrayList<Integer>();
		
		int min = 9999, max = 0;
		
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				int yearAux = Integer.parseInt(reference.year)
				if (yearAux < min)
				{
					min = yearAux;
				}
				if(yearAux > max)
				{
					max = yearAux;
				}
			}
		}
		years.add(min); years.add(max);
		
		return years;
	}
	
	Map<String,Map<String,Integer>> inicialiceMapYearsByTag(int minYear, int maxYear, Set<String> labels)
	{
		Map<String,Map<String,Integer>> mapYears = new TreeMap<String,Map<String,Integer>>()
						
		for(int y = minYear; y <= maxYear; y++)
		{		
			Map<String, Integer> mapAux = new TreeMap<String, Integer>();
			
			for(String l : labels)
			{
				mapAux.put(l, 0)
			}
			mapYears.put(Integer.toString(y), mapAux)
		}
		
		return mapYears
	}
	
	List<String> queryComboChart(Slr slrInstance, int minYear, int maxYear, Map<String, Map<String,Integer>> referencesByYear)
	{
		Map<String, Map<String,Integer>> referencesByRangeYear = new TreeMap<String, Map<String,Integer>>()
		String query = "['Year', ";
		int intervalo = 1;
		if(maxYear - minYear > 40)
		{
			intervalo = 10;
		}
		else if(maxYear - minYear > 20)
		{
			intervalo = 5;
		}
		else if(maxYear - minYear > 10)
		{
			intervalo = 2;
		}
				
		int endYear = 0;
		boolean noFirst = false;
		String label = ""
		for(int i=minYear; i<=maxYear; i=i+intervalo)
		{
			label = "[" + ((i==minYear)?minYear:i+1) + "," + (i + intervalo > maxYear ? maxYear : i + intervalo) + "]"
			referencesByRangeYear.put(label, new TreeMap<String, Integer>())
			endYear = i;
		}
		
		if(endYear != maxYear)
		{
			if(endYear+1 != maxYear)
			{
				label = "[" + (endYear+1) + "," + maxYear + "]"
			}
			else
			{
				label = Integer.toString(endYear)
			}
			referencesByRangeYear.put(label, new TreeMap<String,Integer>())
		}
		
		
		Iterator iter = referencesByYear.keySet().iterator();
		String criterios = ""
		int totalCriterios = 0
		while(iter.hasNext())
		{
			criterios = ""
			String year = iter.next();
			String keyRangeYears = getKeyRangeByYear(year, referencesByRangeYear)
			if(null == referencesByRangeYear.get(keyRangeYears))
			{
				referencesByRangeYear.put(keyRangeYears,new TreeMap<String,Integer>())
			}
			
			Iterator iter2 = referencesByYear.get(year).keySet().iterator()
			totalCriterios = 0
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				totalCriterios++
				criterios += "'"+crit + "',"
				int total = referencesByYear.get(year).get(crit)
				if(referencesByRangeYear.get(keyRangeYears).containsKey(crit))
				{
					total += referencesByRangeYear.get(keyRangeYears).get(crit)
				}
				referencesByRangeYear.get(keyRangeYears).put(crit,total)
			}
		}
		query += criterios + "'Average'],"

		Iterator iter3 = referencesByRangeYear.keySet().iterator();
		int totalSuma = 0
		while(iter3.hasNext())
		{
			String year = iter3.next()
			String queryAux = "['" + year + "', "
			Iterator iter4 = referencesByRangeYear.get(year).keySet().iterator()
			totalSuma = 0
			while(iter4.hasNext())
			{
				String crit = iter4.next();
				queryAux += referencesByRangeYear.get(year).get(crit) + ", "
				totalSuma += referencesByRangeYear.get(year).get(crit) 
			}
			queryAux += ((double)totalSuma/totalCriterios) + "],"
			query += queryAux
		}
		query = query.substring(0, query.lastIndexOf(","))
		
		List<String> results = new ArrayList<String>()
		results.add(query)
		results.add(Integer.toString(totalCriterios))
		return results;
	}
	
	String getKeyRangeByYear(String keyYear, Map<String, Map<String,Integer>> referencesByRangeYear)
	{
		String keyRange = ""
		
		Iterator iter = referencesByRangeYear.keySet().iterator()
		while(iter.hasNext())
		{
			keyRange = iter.next();

			String[] years = keyRange.replaceAll("\\[", "").replaceAll("\\]","").split(",")

			int minYear = 0
			int maxYear = 0
			int year = Integer.parseInt(keyYear)

			if(years.length == 1)
			{
				minYear = year
				maxYear = year
			}
			else
			{
				minYear = Integer.parseInt(years[0])
				maxYear = Integer.parseInt(years[1])
			}
			
			if(minYear <= year && year <= maxYear)
			{
				break;
			}
		}
		
		return keyRange
	}
	
	String chartTotal5LastSlrCreated(UserProfile userInstance)
	{
		String queryChart = "[\"SLR\", \"Total SLR\", { role: \"style\" }], ";
		
		Calendar cal = Calendar.getInstance();
		Date currentDate = cal.getTime();
		
		for(int i=4; i >= 0; i--)
		{
			Date dateLabel = toolService.addMonthToDate(currentDate, 0 - i);
			String strLabelMonth = new SimpleDateFormat("MMM").format(dateLabel).toUpperCase();
			String strLabelYear  = new SimpleDateFormat("YYYY").format(dateLabel);
			
			//queryChart += "[\"" + strAuxMonth + " " + strAuxYear + "\", 5, \"\"], "
			int totalSlr = 0
			
			def slrListInstance = Slr.findAllByUserProfile(userInstance)
			
			for(Slr slrInstance : slrListInstance)
			{
				String strMonthDateSubmit = new SimpleDateFormat("MMM").format(slrInstance.submitDate).toUpperCase();
				String strYearDateSubmit = new SimpleDateFormat("YYYY").format(slrInstance.submitDate).toUpperCase();
				
				if (strLabelMonth.equals(strMonthDateSubmit) && strLabelYear.equals(strYearDateSubmit))
				{
					totalSlr++;
				}
			}
			
			queryChart += "[\"" + strLabelMonth + " " + strLabelYear + "\", " + totalSlr + ", \"\"], "
		}
		
		queryChart = queryChart.substring(0, queryChart.length()-1)
		
		return queryChart;
	}
	
	List<String> chartsByTag(Slr slrInstance)
	{
		List<String> queriesCharts = new ArrayList<String>()
		List<Reference> references = new ArrayList<Reference>()

		// Range Years
		List<Integer> years = getMinAndMaxYear(slrInstance)
		int minYear = years.get(0)
		int maxYear = years.get(1)
		String showTextEvery = "1"
		
		if(maxYear - minYear > 20)
		{
			showTextEvery = Integer.toString(5)
		}
		else if(maxYear - minYear > 10)
		{
			showTextEvery = Integer.toString(2)
		}

		// For tab Search
		Set<String> searchs = new HashSet<String>(); searchs.add(labelCriterionIncluded); searchs.add(labelCriterionNoIncluded);
		Map<String, Integer> referencesBySearch = new TreeMap<String, Integer>();
		referencesBySearch.put(labelCriterionIncluded,0); referencesBySearch.put(labelCriterionNoIncluded,0);
		
		for(Search search : slrInstance.searchs)
		{
			if(hasReferenceIncludes(search))
			{
				referencesBySearch.put(labelCriterionIncluded, referencesBySearch.get(labelCriterionIncluded)+1)
			}
			else
			{
				referencesBySearch.put(labelCriterionNoIncluded, referencesBySearch.get(labelCriterionNoIncluded)+1)
			}
			references.addAll(search.references)
		}
		
		Map<String, Map<String,Integer>> referencesSearchByYear = inicialiceMapYearsByTag(minYear, maxYear, searchs)
		String querySearch1 = "[\"Search\", \"Total\", { role: \"style\" } ],"
		
		Set<String> criterions = new HashSet<String>()
		Set<String> engines = new HashSet<String>()
		Set<String> departments = new HashSet<String>()
		Set<String> types = new HashSet<String>()
		Set<String> languages = new HashSet<String>()

		for(Reference reference : references)
		{
			criterions.add(reference.criterion.name)
			engines.add(reference.engine.name)
			String depart = reference.department.toString().toLowerCase().trim()
			departments.add(depart == "" ? "--" : reference.department.toString().toLowerCase().trim())
			types.add(reference.type.nombre)
			languages.add(reference.language.name)
		}
		
		String queryCriterion1 = "[\"Criterion\", \"Total\", { role: \"style\" } ],"
		String queryCriterion2 = "['Year', "
		String queryEngine1 = "[\"Engine\", \"Total\", { role: \"style\" } ],"
		String queryEngine2 = "['Year', "
		String queryDepartment1 = "[\"Department\", \"Total\", { role: \"style\" } ],"
		String queryDepartment2 = "['Year', "
		String queryType1 = "[\"Type\", \"Total\", { role: \"style\" } ],"
		String queryType2 = "['Year', "
		String queryLanguage1 = "[\"Language\", \"Total\", { role: \"style\" } ],"
		String queryLanguage2 = "['Year', "
		
		Map<String, Integer> referencesByCriterion = new TreeMap<String, Integer>()
		Map<String, Map<String,Integer>> referencesCriterionByYear = inicialiceMapYearsByTag(minYear, maxYear, criterions)
		Map<String, Integer> referencesByEngine = new TreeMap<String, Integer>()
		Map<String, Map<String,Integer>> referencesEngineByYear = inicialiceMapYearsByTag(minYear, maxYear, engines)
		Map<String, Integer> referencesByDepartment = new TreeMap<String, Integer>()
		Map<String, Map<String,Integer>> referencesDepartmentByYear = inicialiceMapYearsByTag(minYear, maxYear, departments)
		Map<String, Integer> referencesByType = new TreeMap<String, Integer>()
		Map<String, Map<String,Integer>> referencesTypeByYear = inicialiceMapYearsByTag(minYear, maxYear, types)
		Map<String, Integer> referencesByLanguage = new TreeMap<String, Integer>()
		Map<String, Map<String,Integer>> referencesLanguageByYear = inicialiceMapYearsByTag(minYear, maxYear, languages)

		for(Reference reference : references)
		{
			String yearReference = reference.year
			
			// Criterions
			int total = 1;
			String nameLabel = reference.criterion.name
			if(referencesByCriterion.containsKey(nameLabel))
			{
				total = referencesByCriterion.get(nameLabel) + 1
			}
			referencesByCriterion.put(nameLabel, total)

			total = 1;
			Map<String,Integer> mapAux = referencesCriterionByYear.get(yearReference);
			if(mapAux == null)
			{
				mapAux = new TreeMap<String,Integer>()
			}
			else if(mapAux != null && mapAux.containsKey(nameLabel))
			{
				total = mapAux.get(nameLabel) + 1
			}
			mapAux.put(nameLabel, total)
			referencesCriterionByYear.put(yearReference, mapAux)
			
			// Engines
			total = 1;
			nameLabel = reference.engine.name
			if(referencesByEngine.containsKey(nameLabel))
			{
				total = referencesByEngine.get(nameLabel) + 1
			}
			referencesByEngine.put(nameLabel, total)

			total = 1;
			mapAux = referencesEngineByYear.get(yearReference);
			if(mapAux == null)
			{
				mapAux = new TreeMap<String,Integer>()
			}
			else if(mapAux != null && mapAux.containsKey(nameLabel))
			{
				total = mapAux.get(nameLabel) + 1
			}
			mapAux.put(nameLabel, total)
			referencesEngineByYear.put(yearReference, mapAux)
			
			// Departments
			total = 1;
			nameLabel = reference.department.toString().toLowerCase().trim()
			nameLabel = (nameLabel == "" ? "--" : nameLabel)
			if(referencesByDepartment.containsKey(nameLabel))
			{
				total = referencesByDepartment.get(nameLabel) + 1
			}
			referencesByDepartment.put(nameLabel, total)
			
			total = 1;
			mapAux = referencesDepartmentByYear.get(yearReference);
			if(mapAux == null)
			{
				mapAux = new TreeMap<String,Integer>()
			}
			else if(mapAux != null && mapAux.containsKey(nameLabel))
			{
				total = mapAux.get(nameLabel) + 1
			}
			mapAux.put(nameLabel, total)
			referencesDepartmentByYear.put(yearReference, mapAux)
			
			// Types
			total = 1;
			nameLabel = reference.type.nombre
			if(referencesByType.containsKey(nameLabel))
			{
				total = referencesByType.get(nameLabel) + 1
			}
			referencesByType.put(nameLabel, total)
			
			total = 1;
			mapAux = referencesTypeByYear.get(yearReference);
			if(mapAux == null)
			{
				mapAux = new TreeMap<String,Integer>()
			}
			else if(mapAux != null && mapAux.containsKey(nameLabel))
			{
				total = mapAux.get(nameLabel) + 1
			}
			mapAux.put(nameLabel, total)
			referencesTypeByYear.put(yearReference, mapAux)
			
			// Languages
			total = 1;
			nameLabel = reference.language.name
			if(referencesByLanguage.containsKey(nameLabel))
			{
				total = referencesByLanguage.get(nameLabel) + 1
			}
			referencesByLanguage.put(nameLabel, total)
			
			total = 1;
			mapAux = referencesLanguageByYear.get(yearReference);
			if(mapAux == null)
			{
				mapAux = new TreeMap<String,Integer>()
			}
			else if(mapAux != null && mapAux.containsKey(nameLabel))
			{
				total = mapAux.get(nameLabel) + 1
			}
			mapAux.put(nameLabel, total)
			referencesLanguageByYear.put(yearReference, mapAux)
		}
		
		// Criterions
		Iterator iter = referencesByCriterion.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			queryCriterion1 += "[\"" + key + "\", " + referencesByCriterion.get(key) + ", \"\"],"
		}
		queryCriterion1 = queryCriterion1.substring(0, queryCriterion1.lastIndexOf(","))
		
		iter = referencesByCriterion.keySet().iterator()
		while(iter.hasNext())
		{
			String key = iter.next()
			queryCriterion2 += "'"+key+"',"
		}
		queryCriterion2 = queryCriterion2.substring(0, queryCriterion2.lastIndexOf(",")) + "],"
		
		iter = referencesCriterionByYear.keySet().iterator();
		while(iter.hasNext())
		{
			String year = iter.next()
			String queryAux = "['" + year + "', "
			Iterator iter2 = referencesCriterionByYear.get(year).keySet().iterator()
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				queryAux += referencesCriterionByYear.get(year).get(crit) + ", "
			}
			queryAux = queryAux.substring(0, queryAux.lastIndexOf(",")) + "],"
			queryCriterion2 += queryAux
		}
		queryCriterion2 = queryCriterion2.substring(0, queryCriterion2.lastIndexOf(","))
		
		List<String> resultsComboCriterion = queryComboChart(slrInstance, minYear, maxYear, referencesCriterionByYear)
		
		// Engine
		iter = referencesByEngine.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			queryEngine1 += "[\"" + key + "\", " + referencesByEngine.get(key) + ", \"\"],"
		}
		queryEngine1 = queryEngine1.substring(0, queryEngine1.lastIndexOf(","))
		
		iter = referencesByEngine.keySet().iterator()
		while(iter.hasNext())
		{
			String key = iter.next()
			queryEngine2 += "'"+key+"',"
		}
		queryEngine2 = queryEngine2.substring(0, queryEngine2.lastIndexOf(",")) + "],"
		
		iter = referencesEngineByYear.keySet().iterator();
		while(iter.hasNext())
		{
			String year = iter.next()
			String queryAux = "['" + year + "', "
			Iterator iter2 = referencesEngineByYear.get(year).keySet().iterator()
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				queryAux += referencesEngineByYear.get(year).get(crit) + ", "
			}
			queryAux = queryAux.substring(0, queryAux.lastIndexOf(",")) + "],"
			queryEngine2 += queryAux
		}
		queryEngine2 = queryEngine2.substring(0, queryEngine2.lastIndexOf(","))
		
		List<String> resultsComboEngine = queryComboChart(slrInstance, minYear, maxYear, referencesEngineByYear)
		
		// Department
		iter = referencesByDepartment.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			queryDepartment1 += "[\"" + key + "\", " + referencesByDepartment.get(key) + ", \"\"],"
		}
		queryDepartment1 = queryDepartment1.substring(0, queryDepartment1.lastIndexOf(","))
		
		iter = referencesByDepartment.keySet().iterator()
		while(iter.hasNext())
		{
			String key = iter.next()
			queryDepartment2 += "'"+key+"',"
		}
		queryDepartment2 = queryDepartment2.substring(0, queryDepartment2.lastIndexOf(",")) + "],"
		
		iter = referencesDepartmentByYear.keySet().iterator();
		while(iter.hasNext())
		{
			String year = iter.next()
			String queryAux = "['" + year + "', "
			Iterator iter2 = referencesDepartmentByYear.get(year).keySet().iterator()
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				queryAux += referencesDepartmentByYear.get(year).get(crit) + ", "
			}
			queryAux = queryAux.substring(0, queryAux.lastIndexOf(",")) + "],"
			queryDepartment2 += queryAux
		}
		queryDepartment2 = queryDepartment2.substring(0, queryDepartment2.lastIndexOf(","))
		
		List<String> resultsComboDepartment = queryComboChart(slrInstance, minYear, maxYear, referencesDepartmentByYear)
		
		// Type
		iter = referencesByType.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			queryType1 += "[\"" + key + "\", " + referencesByType.get(key) + ", \"\"],"
		}
		queryType1 = queryType1.substring(0, queryType1.lastIndexOf(","))
		
		iter = referencesByType.keySet().iterator()
		while(iter.hasNext())
		{
			String key = iter.next()
			queryType2 += "'"+key+"',"
		}
		queryType2 = queryType2.substring(0, queryType2.lastIndexOf(",")) + "],"
		
		iter = referencesTypeByYear.keySet().iterator();
		while(iter.hasNext())
		{
			String year = iter.next()
			String queryAux = "['" + year + "', "
			Iterator iter2 = referencesTypeByYear.get(year).keySet().iterator()
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				queryAux += referencesTypeByYear.get(year).get(crit) + ", "
			}
			queryAux = queryAux.substring(0, queryAux.lastIndexOf(",")) + "],"
			queryType2 += queryAux
		}
		queryType2 = queryType2.substring(0, queryType2.lastIndexOf(","))
		
		List<String> resultsComboType = queryComboChart(slrInstance, minYear, maxYear, referencesTypeByYear)
		
		// Language
		iter = referencesByLanguage.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			queryLanguage1 += "[\"" + key + "\", " + referencesByLanguage.get(key) + ", \"\"],"
		}
		queryLanguage1 = queryLanguage1.substring(0, queryLanguage1.lastIndexOf(","))
		
		iter = referencesByLanguage.keySet().iterator()
		while(iter.hasNext())
		{
			String key = iter.next()
			queryLanguage2 += "'"+key+"',"
		}
		queryLanguage2 = queryLanguage2.substring(0, queryLanguage2.lastIndexOf(",")) + "],"
		
		iter = referencesLanguageByYear.keySet().iterator();
		while(iter.hasNext())
		{
			String year = iter.next()
			String queryAux = "['" + year + "', "
			Iterator iter2 = referencesLanguageByYear.get(year).keySet().iterator()
			while(iter2.hasNext())
			{
				String crit = iter2.next();
				queryAux += referencesLanguageByYear.get(year).get(crit) + ", "
			}
			queryAux = queryAux.substring(0, queryAux.lastIndexOf(",")) + "],"
			queryLanguage2 += queryAux
		}
		queryLanguage2 = queryLanguage2.substring(0, queryLanguage2.lastIndexOf(","))
		
		List<String> resultsComboLanguage = queryComboChart(slrInstance, minYear, maxYear, referencesLanguageByYear)
		
		// Search
		iter = referencesBySearch.keySet().iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			
			querySearch1 += "[\"" + key + "\", " + referencesBySearch.get(key) + ", \"\"],"
		}
		querySearch1 = querySearch1.substring(0, querySearch1.lastIndexOf(","))
		

		// Return queries
		queriesCharts.add(showTextEvery)
		queriesCharts.add(queryCriterion1)
		queriesCharts.add(queryCriterion2)
		queriesCharts.add(resultsComboCriterion.get(0))
		queriesCharts.add(resultsComboCriterion.get(1))
		queriesCharts.add(queryEngine1)
		queriesCharts.add(queryEngine2)
		queriesCharts.add(resultsComboEngine.get(0))
		queriesCharts.add(resultsComboEngine.get(1))
		queriesCharts.add(queryDepartment1)
		queriesCharts.add(queryDepartment2)
		queriesCharts.add(resultsComboDepartment.get(0))
		queriesCharts.add(resultsComboDepartment.get(1))
		queriesCharts.add(queryType1)
		queriesCharts.add(queryType2)
		queriesCharts.add(resultsComboType.get(0))
		queriesCharts.add(resultsComboType.get(1))
		queriesCharts.add(queryLanguage1)
		queriesCharts.add(queryLanguage2)
		queriesCharts.add(resultsComboLanguage.get(0))
		queriesCharts.add(resultsComboLanguage.get(1))
		queriesCharts.add(querySearch1)
		
		return queriesCharts
	}
	
	boolean hasReferenceIncludes(Search search)
	{
		boolean hasIncluded = false;
		
		for(Reference reference : search.references)
		{
			if(reference.criterion.name.toLowerCase().equals("included"))
			{
				hasIncluded = true;
				break;
			}
		}
		
		return hasIncluded;
	}
}
