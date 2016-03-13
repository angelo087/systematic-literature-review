package es.uca.pfc.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskReference {

	private List<String> keywords = new ArrayList<String>();
	private List<String> authors = new ArrayList<String>();
	private List<String> websites = new ArrayList<String>();
	private List<String> tags = new ArrayList<String>();

	private String typeDocument;
	private String language;
	private String idmend = "";
	private String title = "";
	private Date created = new Date();
	private Date last_modified = new Date();
	private String docAbstract = "";
	private String source = "";
	private String year = "";
	private String pages = "";
	private String volume = "";
	private String issue = "";
	private String publisher = "";
	private String city = "";
	private String institution = "";
	private String series = "";
	private String chapter = "";
	private String citation_key = "";
	private String source_type = "";
	private String genre = "";
	private String country = "";
	private String department = "";
	private String arxiv = "";
	private String doi = "";
	private String isbn = "";
	private String issn = "";
	private String pmid = "";
	private String scopus = "";
	private String notes = "";
	private String month = "";
	private String day = "";
	private boolean file_attached = false;
	private String bibtex = "";

	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	public List<String> getWebsites() {
		return websites;
	}
	public void setWebsites(List<String> websites) {
		this.websites = websites;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getTypeDocument() {
		return typeDocument;
	}
	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getIdmend() {
		return idmend;
	}
	public void setIdmend(String idmend) {
		this.idmend = idmend;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(Date last_modified) {
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
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
	public String getArxiv() {
		return arxiv;
	}
	public void setArxiv(String arxiv) {
		this.arxiv = arxiv;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getIssn() {
		return issn;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getScopus() {
		return scopus;
	}
	public void setScopus(String scopus) {
		this.scopus = scopus;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public boolean isFile_attached() {
		return file_attached;
	}
	public void setFile_attached(boolean file_attached) {
		this.file_attached = file_attached;
	}
	public String getBibtex() {
		return bibtex;
	}
	public void setBibtex(String bibtex) {
		this.bibtex = bibtex;
	}
}
