package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SearchComponent)
class SearchComponentSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		SearchComponent.withNewSession {
			SearchComponent.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create searchComponent"() {
		given: 
			SearchComponent searchComponent = Stub()	
		when:
			searchComponent.save()
		then:
			!searchComponent.errors.hasFieldErrors("name")
			!searchComponent.errors.hasFieldErrors("value")
    }
	
	void "test update searchComponent"() {
		given:
			SearchComponent searchComponent = Stub()
			searchComponent.save()
		when:
			searchComponent.value >> "other value"
			searchComponent.save()
		then:
			!searchComponent.errors.hasFieldErrors("name")
			!searchComponent.errors.hasFieldErrors("value")
			searchComponent.value == "other value"
	}
	
	void "test delete searchComponent"() {
		given:
			SearchComponent searchComponent = Stub()
			searchComponent.save(flush: true)
		when:
			searchComponent.delete()
		then:
			SearchComponent.list().size() == 0
	}
}
