package es.pfc.test;

import java.util.ArrayList;
import java.util.List;

import es.pfc.commons.ComponentSearch;
import es.pfc.commons.OperatorSearch;
import es.pfc.commons.SearchTermParam;

public class Test {

	public static void main(String[] args)
	{
		SearchTermParam stp01 = new SearchTermParam(ComponentSearch.REVIEW, OperatorSearch.ALL, "word");
		SearchTermParam stp02 = new SearchTermParam(ComponentSearch.FULLTEXT, OperatorSearch.ANY, "word");
		List<SearchTermParam> searchsTerms = new ArrayList<SearchTermParam>();
		searchsTerms.add(stp01); searchsTerms.add(stp02);
		
		String minYear = "1999";
		String maxYear = "2010";
		
		System.out.println(QueryIEEE(searchsTerms, minYear, maxYear));
	}
	
	public static String QueryIEEE(List<SearchTermParam> searchsTerms, String minYear, String maxYear)
	{
		String query = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp";
		
		if(searchsTerms.size() > 0)
		{
			query += "?";
			List<SearchTermParam> othersParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> noneParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> andParameters = new ArrayList<SearchTermParam>();
			List<SearchTermParam> orParameters = new ArrayList<SearchTermParam>();
			
			for(SearchTermParam stp : searchsTerms)
			{
				if(stp.getOperatorSearch() == OperatorSearch.NONE)
				{
					othersParameters.add(stp);
				}
				else
				{
					String subquery = "";
					switch(stp.getComponentSearch())
					{
						case ABSTRACT:
							subquery = "ab=\"" + stp.getTerminos().trim() + "\"";
							break;
						case TITLE:
							subquery = "ti=\"" + stp.getTerminos().trim() + "\"";
							break;
						case AUTHOR:
							subquery = "au=\"" + stp.getTerminos().trim() + "\"";
							break;
						case PUBLISHER:
							subquery = "jn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case ISBN:
							subquery = "isbn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case ISSN:
							subquery = "issn=\"" + stp.getTerminos().trim() + "\"";
							break;
						case DOI:
							subquery = "doi=\"" + stp.getTerminos().trim() + "\"";
							break;
						default:
							othersParameters.add(stp);
					}
					
					if(!subquery.equals(""))
					{
						query += subquery + "&";
					}
					
					query = query.trim();
				}
			}
			
			// Restriccion para el rango de years
			if (query.charAt(query.length()-1) != '&')
			{
				query += "&";
			}
			query += "pys=" + minYear;
			query += "&pye=" + maxYear;
			
			// Restriccion total a mostrar
			query += "&hc=25";
			//query += "&rs=1";
			
			// QueryText
			if (othersParameters.size() > 0)
			{
				if (query.charAt(query.length()-1) != '&')
				{
					query += "&";
				}
				
				query += "querytext=(";
				
				for(SearchTermParam param : othersParameters) {
					if (param.getOperatorSearch() == OperatorSearch.ALL) 
					{
						andParameters.add(param);
					} 
					else if (param.getOperatorSearch() == OperatorSearch.ANY) 
					{
						orParameters.add(param);
					}
					else if (param.getOperatorSearch() == OperatorSearch.NONE)
					{
						noneParameters.add(param);
					}
				}
				
				String firstTerm = "";
				if(andParameters.size() > 0)
				{
					firstTerm = andParameters.get(0).getTerminos();
					andParameters.remove(0);
				}
				else
				{
					if (orParameters.size() > 0)
					{
						firstTerm = orParameters.get(0).getTerminos();
						orParameters.remove(0);
					}
					else
					{
						firstTerm = "article";
					}
				}
				
				query += firstTerm;
				
				// Resto de parametros AND
				if (andParameters.size() > 0)
				{
					for (SearchTermParam param : andParameters) {
						query += " AND " + param.getTerminos();
					}
				}
				
				// Resto de parametros OR
				if (orParameters.size() > 0)
				{
					for (SearchTermParam param : orParameters) {
						query += " OR " + param.getTerminos();
					}
				}
				
				// Resto de parametros NOT
				if (noneParameters.size() > 0)
				{
					for (SearchTermParam param : noneParameters) {
						query += " NOT " + param.getTerminos();
					}
				}
				
				query += ")";
			}
			else if (query.charAt(query.length()-1) == '&') 
			{
				query = query.substring(0, query.length()-1);
			}
			query = query.trim() + "&rs=1";
		}
		
		return query;
	}
	
}
