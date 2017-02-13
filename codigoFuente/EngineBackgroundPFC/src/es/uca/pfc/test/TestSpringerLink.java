package es.uca.pfc.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uca.pfc.commons.SearchTermParam;
import es.uca.pfc.enums.ComponentSearch;
import es.uca.pfc.enums.OperatorSearch;
import es.uca.pfc.enums.TypeEngineSearch;

public class TestSpringerLink {

	public static String CODE_API = "c8c8ee4b2c20f0046806762317d0d6e2";
	public static String web = "http://api.springer.com/meta/v1/pam";
	public static String start_year = "2010";
	public static String end_year = "2012";
	public static Map<TypeEngineSearch, String> apiKeysEngine = new HashMap<TypeEngineSearch, String>();
	public static int TAM_DEF = 10;
	
	public static void main(String[] args) {
		
		SearchTermParam stp01 = new SearchTermParam(ComponentSearch.ANYFIELD, OperatorSearch.ALL, "word");
		SearchTermParam stp02 = new SearchTermParam(ComponentSearch.AUTHOR, OperatorSearch.NONE, "toro");
		List<SearchTermParam> searchsTerms = new ArrayList<SearchTermParam>();
		searchsTerms.add(stp01); searchsTerms.add(stp02);
		
		apiKeysEngine.put(TypeEngineSearch.SPRINGER, CODE_API);
		
		System.out.println("Web => " + createQuerySpringer(searchsTerms));
		
	}
	
	private static String createQuerySpringer(List<SearchTermParam> searchsTerms)
	{
		String query = web;
		
		// Insertamos la api-key y el tipo de formato a obtener (xml)
		String apiKeySpringer = (String) apiKeysEngine.get(TypeEngineSearch.SPRINGER);
		query += "?api_key=" + apiKeySpringer;
		
		if(searchsTerms.size() > 0)
		{
			query += "&q=((";
			
			List<SearchTermParam> noneParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> andParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> orParameters = new ArrayList<SearchTermParam>();
			
			for(SearchTermParam stp : searchsTerms)
			{
				if (OperatorSearch.ALL == stp.getOperatorSearch()) {
					andParameters.add(stp);
				} else if (OperatorSearch.ANY == stp.getOperatorSearch()) {
					orParameters.add(stp);
				} else {
					noneParameters.add(stp);
				}
			}
			
			// Introducimos los years de comienzo y fin
			int yearStart = Integer.parseInt(start_year);
			int yearEnd = Integer.parseInt(end_year);
			
			for(int y = yearStart; y <= yearEnd; y++)
			{
				query += "year:" + y;
				if (y != yearEnd)
				{
					query += " OR ";
				}
			}
			query += ")";
			
			// Construimos la query
			String subquery = "";
			
			// Parameters AND
			for(SearchTermParam stp : andParameters) 
			{
				subquery += convertParametersSpringerLink(stp);
				
				query += " AND " + subquery;
			}
			
			// Parameters OR
			subquery = "";
			if (orParameters.size() > 0)
			{
				for(SearchTermParam stp : orParameters)
				{
					subquery += convertParametersSpringerLink(stp);
					
					query += " OR " + subquery;
				}
			}
			
			// Parameters NOT
			subquery = "";
			if (noneParameters.size() > 0)
			{
				for (SearchTermParam stp : noneParameters)
				{
					subquery += convertParametersSpringerLink(stp);
					query += "-(" + subquery + ")";
				}
			}
			
			query += ")"; // fin param "q"
			
			// Indicamos numero de resultados a obtener
			query += "&p="+TAM_DEF; //maximo es 100
			
			// Indicamos posicion por donde se empieza (index)
			query += "&s=1";   // comienza desde 1
		}
		
		return query;
	}
	
	private static String convertParametersSpringerLink(SearchTermParam stp)
	{
		return "";
	}
	
}
