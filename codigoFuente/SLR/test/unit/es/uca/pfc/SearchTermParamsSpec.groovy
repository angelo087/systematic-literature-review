package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SearchTermParam)
class SearchTermParamsSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		SearchTermParam.withNewSession {
			SearchTermParam.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create searchTermParam"() {
		given: 
			SearchTermParam searchTermParam = Stub()	
		when:
			searchTermParam.save()
		then:
			!searchTermParam.errors.hasFieldErrors("terminos")
			!searchTermParam.errors.hasFieldErrors("component")
			!searchTermParam.errors.hasFieldErrors("operator")
			!searchTermParam.errors.hasFieldErrors("search")
    }
	
	void "test update searchTermParam"() {
		given:
			SearchTermParam searchTermParam = Stub()
			searchTermParam.save()
		when:
			searchTermParam.terminos >> 'Other terminos'
			searchTermParam.save()
		then:
			!searchTermParam.errors.hasFieldErrors("terminos")
			!searchTermParam.errors.hasFieldErrors("component")
			!searchTermParam.errors.hasFieldErrors("operator")
			!searchTermParam.errors.hasFieldErrors("search")
	}
	
	void "test delete searchTermParam"() {
		given:
			SearchTermParam searchTermParam = Stub()
			searchTermParam.save(flush: true)
		when:
			searchTermParam.delete()
		then:
			SearchTermParam.list().size() == 0
	}
}
