package es.uca.pdf;

import java.util.Map;
import java.util.TreeMap;

public class PrimaryStudyHelper {
	
	private String title;
	private String type;
	private int year;
	private String publisher;
	private String citationKey;
	
	Map<String, String> specAttributes = new TreeMap<String, String>();
	
	public PrimaryStudyHelper()
	{
	}

	public PrimaryStudyHelper(String title, String type, int year,
			String publisher, String citationKey, Map<String, String> specAttributes) {
		
		this.title = title;
		this.type = type;
		this.year = year;
		this.publisher = publisher;
		this.citationKey = citationKey;
		this.specAttributes = specAttributes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCitationKey() {
		return citationKey;
	}

	public void setCitationKey(String citationKey) {
		this.citationKey = citationKey;
	}

	public Map<String, String> getSpecAttributes() {
		return specAttributes;
	}

	public void setSpecAttributes(Map<String, String> specAttributes) {
		this.specAttributes = specAttributes;
	}
}
