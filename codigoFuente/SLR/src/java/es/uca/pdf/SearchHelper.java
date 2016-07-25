package es.uca.pdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchHelper {

	/*private String source;
	private String terms;
	private String scope;
	private String operator;
	private int results;
	private int primaryStudies;
	private Date date;*/
	private String engines;
	private String terms;
	private int results;
	private int primaryStudies;
	private Date date;
	
	public SearchHelper() {}
	
	public SearchHelper(String engines, String terms, int results, int primaryStudies, Date date) {
		this.engines = engines;
		this.terms = terms;
		this.results = results;
		this.primaryStudies = primaryStudies;
		this.date = date;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public int getResults() {
		return results;
	}
	public void setResults(int results) {
		this.results = results;
	}
	public int getPrimaryStudies() {
		return primaryStudies;
	}
	public void setPrimaryStudies(int primaryStudies) {
		this.primaryStudies = primaryStudies;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getEngines() {
		return engines;
	}

	public void setEngines(String engines) {
		this.engines = engines;
	}
}
