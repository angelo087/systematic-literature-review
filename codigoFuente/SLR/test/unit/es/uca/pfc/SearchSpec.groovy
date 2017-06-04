package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Search)
class SearchSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		Search.withNewSession {
			Search.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create search"() {
		given: 
			Search search = Stub()	
		when:
			search.save()
		then:
			!search.errors.hasFieldErrors("slr")
			!search.errors.hasFieldErrors("references")
			!search.errors.hasFieldErrors("engines")
			!search.errors.hasFieldErrors("termParams")
			!search.errors.hasFieldErrors("startYear")
			!search.errors.hasFieldErrors("endYear")
			!search.errors.hasFieldErrors("maxTotal")
    }
	
	void "test update search"() {
		given:
			Search search = Stub()
			search.save()
		when:
			SearchTermParam termParam = Stub()
			search.termParams >> termParam
			search.save()
		then:
			!search.errors.hasFieldErrors("slr")
			!search.errors.hasFieldErrors("references")
			!search.errors.hasFieldErrors("engines")
			!search.errors.hasFieldErrors("termParams")
			!search.errors.hasFieldErrors("startYear")
			!search.errors.hasFieldErrors("endYear")
			!search.errors.hasFieldErrors("maxTotal")
	}
	
	void "test delete search"() {
		given:
			Search search = Stub()
			search.save(flush: true)
		when:
			search.delete()
		then:
			Search.list().size() == 0
	}
}
