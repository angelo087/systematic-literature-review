package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Slr)
class SlrSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		Slr.withNewSession {
			Slr.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create slr"() {
		given: 
			Slr slr = Stub()	
		when:
			slr.save()
		then:
			!slr.errors.hasFieldErrors("userProfile")
			!slr.errors.hasFieldErrors("searchs")
			!slr.errors.hasFieldErrors("criterions")
			!slr.errors.hasFieldErrors("questions")
			!slr.errors.hasFieldErrors("justification")
			!slr.errors.hasFieldErrors("title")
			!slr.errors.hasFieldErrors("specAttributes")
    }
	
	void "test update slr"() {
		given:
			Slr slr = Stub()
			slr.save()
		when:
			slr.title >> 'Other title'
			slr.save()
		then:
			!slr.errors.hasFieldErrors("userProfile")
			!slr.errors.hasFieldErrors("searchs")
			!slr.errors.hasFieldErrors("criterions")
			!slr.errors.hasFieldErrors("questions")
			!slr.errors.hasFieldErrors("justification")
			!slr.errors.hasFieldErrors("title")
			!slr.errors.hasFieldErrors("specAttributes")
			slr.title.equals("Other title")
	}
	
	void "test delete slr"() {
		given:
			Slr slr = Stub()
			slr.save(flush: true)
		when:
			slr.delete()
		then:
			Slr.list().size() == 0
	}
}
