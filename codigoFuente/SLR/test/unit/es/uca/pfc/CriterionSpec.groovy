package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Criterion)
class CriterionSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		Criterion.withNewSession {
			Criterion.findAll().each { it.delete(flush: true) }
		}
    }
	
    void "test create criterion"() {
		given: 
			Criterion criterion = Stub()	
		when:
			criterion.save()
		then:
			!criterion.errors.hasFieldErrors("name")
			!criterion.errors.hasFieldErrors("description")
			!criterion.errors.hasFieldErrors("slr")
    }
	
	void "test update criterion"() {
		given:
			Criterion criterion = Stub()
			criterion.save()
		when:
			criterion.description >> "other description"
			criterion.save()
		then:
			!criterion.errors.hasFieldErrors("name")
			!criterion.errors.hasFieldErrors("description")
			!criterion.errors.hasFieldErrors("slr")
			criterion.description == "other description"
	}
	
	void "test delete criterion"() {
		given:
			Criterion criterion = Stub()
			criterion.save()
		when:
			criterion.delete()
		then:
			Criterion.list().size() == 0
	}
}
