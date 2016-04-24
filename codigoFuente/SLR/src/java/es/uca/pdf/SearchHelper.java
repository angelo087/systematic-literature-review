package es.uca.pdf;

import java.util.Date;

public class SearchHelper {

	private String source;
	private String terms;
	private String scope;
	private String operator;
	private int results;
	private int primaryStudies;
	private Date date;
	
	public SearchHelper() {}
	
	public SearchHelper(String source, String terms, String scope, String operator, int results,
			int primaryStudies, Date date) {
		this.source = source;
		this.terms = terms;
		this.scope = scope;
		this.results = results;
		this.primaryStudies = primaryStudies;
		this.date = date;
		this.operator = operator;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
