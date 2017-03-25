package mendeley.pfc.schemas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mendeley.pfc.commons.TypeDocument;

import com.google.gson.annotations.SerializedName;

/**
 * The Class Document.
 */
public class Document 
{	
	private String id;
	private String title;
	//private String type;
	@SerializedName("type")
	private TypeDocument type;
	private String created;
	private String last_modified;
	private String docAbstract;
	private String source;
	private int year;
	private String pages;
	private String volume;
	private String issue;
	private String publisher;
	private String city;
	private String institution;
	private String series;
	private String chapter;
	private String citation_key;
	private String source_type;
	private String language;
	private String genre;
	private String country;
	private String department;
	private int month;
	private int day;
	private List<Person> authors = new ArrayList<Person>();
	private List<String> keywords = new ArrayList<String>();
	private List<String> websites = new ArrayList<String>();
	private List<Person> editors = new ArrayList<Person>();
	private List<String> tags = new ArrayList<String>();
	private Identifier identifiers = new Identifier();
	private boolean file_attached;
	private String bibtex;
	
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id; }
	
	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }
	
	public TypeDocument getType() { return type; }
	public void setType(TypeDocument type)
	{
		this.type = type;
	}
	
	/*public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	public void setType(TypeDocument type)
	{
		this.type = type.getValue();
	}*/
	
	public String getCreated() { return created; }
	public void setCreated(String created) { this.created = created; }
	
	public String getLastModified() { return last_modified; }
	public void setLastModified(String last_modified) { this.last_modified = last_modified; }
	
	public String getAbstract() { return docAbstract; }
	public void setAbstract(String docAbstract) { this.docAbstract = docAbstract; }
	
	public String getSource() { return source; }
	public void setSource(String source) { this.source = source; }
	
	public int getYear() { return year; }
	public void setYear(int year) { this.year = year; }
	public void setYear(String year) { this.year = Integer.parseInt(year); }
	
	public List<Person> getAuthors() { return authors; }
	public void setAuthors(List<Person> authors) { this.authors = authors; }
	public String getAuthorsString() 
	{
		String autores = "";
		
		for(Person p : authors)
		{
			autores += p.getForename() + " " + p.getSurname() + ", ";
		}
		autores = autores.trim().substring(0, autores.length()-2);
		return autores;
	}
	
	public List<String> getKeywords() { return keywords; }
	public void setKeywords(List<String> keywords) { this.keywords = keywords; }
	public void setKeywords(String keyworkds)
	{
		String[] keys = keyworkds.split(",");
		for(String k : keys)
		{
			this.keywords.add(k);
		}
	}
	
	public String getPages() { return pages; }
	public void setPages(String pages) { this.pages = pages; }
	
	public String getVolume() { return volume; }
	public void setVolume(String volume) { this.volume = volume; }
	
	public String getIssue() { return issue; }
	public void setIssue(String issue) { this.issue = issue; }
	
	public List<String> getWebsites() { return websites; }
	public void setWebsites(List<String> websites) { this.websites = websites; }
	public void setWebsites(String websites)
	{
		String[] webs = websites.split(",");
		
		for(String w : webs)
		{
			this.websites.add(w);
		}
	}
	
	public String getPublisher() { return publisher; }
	public void setPublisher(String publisher) { this.publisher = publisher; }
	
	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	
	public String getInstitution() { return institution; }
	public void setInstitution(String instutition) { this.institution = instutition; }
	
	public String getSeries() { return series; }
	public void setSeries(String series) { this.series = series; }
	
	public String getChapter() { return chapter; }
	public void setChapter(String chapter) { this.chapter = chapter; }
	
	public List<Person> getEditors() { return editors; }
	public void setEditors(List<Person> editors) { this.editors = editors; }
	public String getEditorsString() 
	{
		String editores = "";
		int size = editors.size();
		int cont = 0;
		
		for(Person p : this.editors)
		{
			if(cont != (size-1))
				editores = editores + p.toString2() + ", ";
			else
				editores = editores + p.toString2();
			
			cont++;
		}
		
		return editores;
	}
	
	public List<String> getTags() { return tags; }
	public void setTags(List<String> tags) { this.tags = tags; }
	public void setTags(String tags)
	{
		String[] tgs = tags.split(",");
		
		for(String t : tgs)
		{
			this.tags.add(t.trim());
		}
	}
	
	public String getCitationKey() { return citation_key; }
	public void setCitationKey(String citation_key) { this.citation_key = citation_key; }
	
	public String getSourceType() { return source_type; }
	public void setSourceType(String source) { this.source_type = source; }
	
	public String getLanguage() { return language; }
	public void setLanguage(String language) { this.language = language; }
	
	public String getGenre() { return genre; }
	public void setGenre(String genre) { this.genre = genre; }
	
	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }
	
	public String getDepartment() { return department; }
	public void setDepartment(String department) { this.department = department; }
	
	public Identifier getIdentifiers() { return identifiers; }
	public void setIdentifiers(Identifier identifiers) { this.identifiers = identifiers; }
		
	public int getMonth() { return month; }
	public String getMonthName(String language)
	{
		String[] months;
		if(language.toLowerCase().equals("es"))
		{
			months = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		}
		else
		{
			months = new String[]{"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
		}
		
		if(this.month > 11 || this.month < 0)
		{
			return months[0];
		}
		else
		{
			return months[this.month];
		}
	}
	public void setMonth(int month)	{ this.month = month; }
	
	public int getDay() { return day; }
	public void setDay(int day) { this.day = day; }
	
	public boolean getFileAttached() { return file_attached; }
	public void setFileAttached(boolean file_attached) { this.file_attached = file_attached; }
	
	public String getBibtex() { return bibtex; }
	public void setBibtex(String bibtex) { this.bibtex = bibtex; }
	
	public Document()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = Calendar.getInstance().getTime();
		created = df.format(date);
		last_modified = created;
	}
	
	public Document(String label)
	{
		
	}
	
	@Override
	public String toString() {
		return "Document [authors=" + authors + ", city=" + city
				+ ", documentAbstract=" + docAbstract + ", doi=" + identifiers.getDoi()
				+ ", editors=" + editors + ", id=" + id + ", issue=" + issue
				+ ", keywords=" + keywords + ", mendeleyUrl=" + (websites.size() == 0 ? "" : websites.get(0))
				+ ", pages=" + pages + ", publicationOutlet="
				+ ", publisher=" + publisher + ", tags="
				+ tags + ", title=" + title + ", type=" + type + ", url=" + (websites.size() == 0 ? "" : websites.get(1))
				+ ", volume=" + volume + ", year=" + year
				+ "]";
	}
}
