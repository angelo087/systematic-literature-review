package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SearchOperator)
class SearchOperatorSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		SearchOperator.withNewSession {
			SearchOperator.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create searchOperator"() {
		given: 
			SearchOperator searchOperator = Stub()	
		when:
			searchOperator.save()
		then:
			!searchOperator.errors.hasFieldErrors("name")
			!searchOperator.errors.hasFieldErrors("value")
    }
	
	void "test update searchOperator"() {
		given:
			SearchOperator searchOperator = Stub()
			searchOperator.save()
		when:
			searchOperator.value >> "other value"
			searchOperator.save()
		then:
			!searchOperator.errors.hasFieldErrors("name")
			!searchOperator.errors.hasFieldErrors("value")
			searchOperator.value == "other value"
	}
	
	void "test delete searchOperator"() {
		given:
			SearchOperator searchOperator = Stub()
			searchOperator.save(flush: true)
		when:
			searchOperator.delete()
		then:
			SearchOperator.list().size() == 0
	}
}
