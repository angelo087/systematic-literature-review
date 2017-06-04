package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Reference)
class ReferenceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		Reference.withNewSession {
			Reference.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create reference"() {
		given: 
			Reference reference = Stub()	
		when:
			reference.save()
		then:
			!reference.errors.hasFieldErrors("idmend")
			!reference.errors.hasFieldErrors("title")
			!reference.errors.hasFieldErrors("search")
			!reference.errors.hasFieldErrors("criterion")
			!reference.errors.hasFieldErrors("engine")
			!reference.errors.hasFieldErrors("citation_key")
    }
	
	void "test update reference"() {
		given:
			Reference reference = Stub()
			reference.save()
		when:
			reference.title >> "other title"
			reference.save()
		then:
			!reference.errors.hasFieldErrors("idmend")
			!reference.errors.hasFieldErrors("title")
			!reference.errors.hasFieldErrors("search")
			!reference.errors.hasFieldErrors("criterion")
			!reference.errors.hasFieldErrors("engine")
			!reference.errors.hasFieldErrors("citation_key")
			reference.title == "other title"
	}
	
	void "test delete reference"() {
		given:
			Reference reference = Stub()
			reference.save(flush: true)
		when:
			reference.delete()
		then:
			Reference.list().size() == 0
	}
}
