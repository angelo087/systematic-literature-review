package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.commons.TypeDocument;

import com.google.gson.annotations.SerializedName;

public class Catalog {
	
	private String id;
	private String title;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TypeDocument getType() {
		return type;
	}
	public void setType(TypeDocument type) {
		this.type = type;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}
	public String getDocAbstract() {
		return docAbstract;
	}
	public void setDocAbstract(String docAbstract) {
		this.docAbstract = docAbstract;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	public String getCitation_key() {
		return citation_key;
	}
	public void setCitation_key(String citation_key) {
		this.citation_key = citation_key;
	}
	public String getSource_type() {
		return source_type;
	}
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public List<Person> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Person> authors) {
		this.authors = authors;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public List<String> getWebsites() {
		return websites;
	}
	public void setWebsites(List<String> websites) {
		this.websites = websites;
	}
	public List<Person> getEditors() {
		return editors;
	}
	public void setEditors(List<Person> editors) {
		this.editors = editors;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public Identifier getIdentifiers() {
		return identifiers;
	}
	public void setIdentifiers(Identifier identifiers) {
		this.identifiers = identifiers;
	}
	public boolean isFile_attached() {
		return file_attached;
	}
	public void setFile_attached(boolean file_attached) {
		this.file_attached = file_attached;
	}
	
	public Document convertToDocument()
	{
		Document document = new Document();
		
		document.setTitle(title);
		document.setType(type);
		document.setCreated(created);
		document.setLastModified(last_modified);
		document.setAbstract(docAbstract);
		document.setSource(source);
		document.setYear(year);
		document.setPages(pages);
		document.setVolume(volume);
		document.setIssue(issue);
		document.setPublisher(publisher);
		document.setCity(city);
		document.setInstitution(institution);
		document.setSeries(series);
		document.setChapter(chapter);
		document.setCitationKey(citation_key);
		document.setSourceType(source_type);
		document.setLanguage(language);
		document.setGenre(genre);
		document.setCountry(country);
		document.setDepartment(department);
		document.setMonth(month);
		document.setDay(day);
		document.setAuthors(authors);
		document.setKeywords(keywords);
		document.setWebsites(websites);
		document.setEditors(editors);
		document.setTags(tags);
		document.setIdentifiers(identifiers);
		document.setFileAttached(file_attached);
		
		return document;
	}
}
