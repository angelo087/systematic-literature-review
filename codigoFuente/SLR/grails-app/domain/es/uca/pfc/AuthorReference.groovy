package es.uca.pfc

class AuthorReference {

	Author author
	Reference reference
	static belongsTo = [author: Author, reference: Reference]
	
    static constraints = {
    }
}
