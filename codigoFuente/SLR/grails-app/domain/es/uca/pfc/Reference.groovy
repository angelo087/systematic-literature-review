package es.uca.pfc

import java.util.List;
import java.util.Map;

import es.uca.pfc.TypeDocument;

class Reference implements Comparator<Reference> {

	static hasMany = [keywords: String, authorsRefs: AuthorReference, websites: String, tags: String, specificAttributes: SpecificAttributeReference]
	static belongsTo = [search: Search, type: TypeDocument, language: Language, criterion: Criterion]
	
    String idmend = ""; // Identificador en mendeley
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
	
	// Metadatos especificos que será una pareja de clave-valor (nombre atributo, valor)
	//Map<String, String> specificAttributes = new HashMap<String, String>();
	
	static constraints = {
		criterion(nullable: true)
	}
	
	/*static mapping = {
		authorsRefs(cascade: "all-delete-orphan")
	}*/
	
	// Indicamos que la justificacion es un texto
	static mapping = {
		bibtex type: 'text'
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
	}
	
	def beforeUpdate = {
		last_modified = new Date()
	}
	
	def beforeDelete = {
		if(search.slr.totalReferences > 0) {
			search.slr.totalReferences--;
		}
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
