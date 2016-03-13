package es.uca.pfc

import es.uca.pfc.Reference;

class Author {
	
	static hasMany = [authorsRefs: AuthorReference]
	
	String forename = ""
	String surname = ""
	String display_name = ""
	String shortname = ""
	String role = 'author' //author or editor
	
    static constraints = {
		role(inList:['author','editor'], display: false, blank: false)
    }
	
	@Override
	int hashCode() {
		display_name?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof Author && other.display_name == display_name)
	}
	
	def beforeInsert = {
		display_name = forename + " " + surname
		shortname = forename.charAt(0).toString() + ". " + surname
	}
}
