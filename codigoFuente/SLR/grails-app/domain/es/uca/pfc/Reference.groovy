package es.uca.pfc

import java.util.List;
import java.util.Map;

import es.uca.pfc.TypeDocument;

class Reference implements Comparator<Reference> {

	static hasMany = [keywords: String, authorsRefs: AuthorReference, websites: String, tags: String, specificAttributes: SpecificAttributeReference]
	static belongsTo = [search: Search, type: TypeDocument, language: Language, criterion: Criterion, engine: EngineSearch]
	
	String idmend = "";
	String title = "";
	Date created = new Date();
	Date last_modified = new Date();
	String docAbstract = "";
	String source = "";
	String year = "";
	String pages = "";
	String volume = "";
	String issue = "";
	String publisher = "";
	String city = "";
	String institution = "";
	String series = "";
	String chapter = "";
	String citation_key = "";
	String source_type = "";
	String genre = "";
	String country = "";
	String department = "";
	String arxiv = "";
	String doi = "";
	String isbn = "";
	String issn = "";
	String pmid = "";
	String scopus = "";
	String notes = "";
	String month = "";
	String day = "";
	boolean file_attached = false;
	String bibtex = ""
	
	// Metadatos especificos que ser� una pareja de clave-valor (nombre atributo, valor)
	//Map<String, String> specificAttributes = new HashMap<String, String>();
	
	static constraints = {
		criterion(nullable: true)
	}
	
	/*static mapping = {
		authorsRefs(cascade: "all-delete-orphan")
	}*/
	
	// Indicamos que bibtex es un texto largo
	static mapping = {
		title type: 'text'
		bibtex type: 'text'
		docAbstract type: 'text'
	}
	
	def beforeInsert = {
		
		// Aumentamos el numero de referencias en el Slr
		search.slr.totalReferences++;
		
		// Insertamos el criterio Included
		criterion = Criterion.findByNomenclaturaAndSlr("cr_included", search.slr)
		
		// Insertamos los atributos especificos del Slr
		for(SpecificAttribute attribute : search.slr.specAttributes)
		{
			if (attribute.tipo == "list" && attribute instanceof SpecificAttributeMultipleValue)
			{
				addToSpecificAttributes(attribute: attribute, value: attribute.optionDefault)
			}
			else
			{
				addToSpecificAttributes(attribute: attribute)
			}
		}
		
		//applyEncode();
	}
	
	def beforeUpdate = {
		last_modified = new Date()
		//applyEncode();
	}
	
	def beforeDelete = {
		if(search.slr.totalReferences > 0) {
			search.slr.totalReferences--;
		}
	}
	
	void applyEncode()
	{
		title = (title == null ? '' : new String(title.getBytes("ISO-8859-1"), "UTF-8"))
		docAbstract = (docAbstract == null ? '' : new String(docAbstract.getBytes("ISO-8859-1"), "UTF-8"))
		source = (source == null ? '' : new String(source.getBytes("ISO-8859-1"), "UTF-8"))
		year = (year == null ? '' : new String(year.getBytes("ISO-8859-1"), "UTF-8"))
		pages = (pages == null ? '' : new String(pages.getBytes("ISO-8859-1"), "UTF-8"))
		volume = (volume == null ? '' : new String(volume.getBytes("ISO-8859-1"), "UTF-8"))
		issue = (issue == null ? '' : new String(issue.getBytes("ISO-8859-1"), "UTF-8"))
		publisher = (publisher == null ? '' : new String(publisher.getBytes("ISO-8859-1"), "UTF-8"))
		city = (city == null ? '' : new String(city.getBytes("ISO-8859-1"), "UTF-8"))
		institution = (institution == null ? '' : new String(institution.getBytes("ISO-8859-1"), "UTF-8"))
		series = (series == null ? '' : new String(series.getBytes("ISO-8859-1"), "UTF-8"))
		chapter = (chapter == null ? '' : new String(chapter.getBytes("ISO-8859-1"), "UTF-8"))
		source_type = (source_type == null ? '' : new String(source_type.getBytes("ISO-8859-1"), "UTF-8"))
		genre = (genre == null ? '' : new String(genre.getBytes("ISO-8859-1"), "UTF-8"))
		country = (country == null ? '' : new String(country.getBytes("ISO-8859-1"), "UTF-8"))
		department = (department == null ? '' : new String(department.getBytes("ISO-8859-1"), "UTF-8"))
		arxiv = (arxiv == null ? '' : new String(arxiv.getBytes("ISO-8859-1"), "UTF-8"))
		doi = (doi == null ? '' : new String(doi.getBytes("ISO-8859-1"), "UTF-8"))
		isbn = (isbn == null ? '' : new String(isbn.getBytes("ISO-8859-1"), "UTF-8"))
		issn = (issn == null ? '' : new String(issn.getBytes("ISO-8859-1"), "UTF-8"))
		pmid = (pmid == null ? '' : new String(pmid.getBytes("ISO-8859-1"), "UTF-8"))
		scopus = (scopus == null ? '' : new String(scopus.getBytes("ISO-8859-1"), "UTF-8"))
		notes = (notes == null ? '' : new String(notes.getBytes("ISO-8859-1"), "UTF-8"))
		month = (month == null ? '' : new String(month.getBytes("ISO-8859-1"), "UTF-8"))
		day = (day == null ? '' : new String(day.getBytes("ISO-8859-1"), "UTF-8"))
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (null == obj) { return false; }
		if(getClass() != obj.getClass()) { return false; }
		
		final Reference other = (Reference) obj;
		
		if (this.id != other.id) { return false; }
		if (!Objects.equals(this.idmend, obj.idmend)) { return false; }
		
		return true;
	}

	@Override
	public int compare(Reference r1, Reference r2) {
		return r1.title.compareTo(r2.title)
	}
}
