package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EngineSearch)
class EngineSearchSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		EngineSearch.withNewSession {
			EngineSearch.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create engine"() {
		given: 
			EngineSearch engine = Stub()	
		when:
			engine.save()
		then:
			!engine.errors.hasFieldErrors("name")
			!engine.errors.hasFieldErrors("display_name")
			!engine.errors.hasFieldErrors("url")
    }
	
	void "test update engine"() {
		given:
			EngineSearch engine = Stub()
			engine.save()
		when:
			engine.display_name >> "other display_name"
			engine.save()
		then:
			!engine.errors.hasFieldErrors("name")
			!engine.errors.hasFieldErrors("display_name")
			!engine.errors.hasFieldErrors("url")
			engine.display_name == "other display_name"
	}
	
	void "test delete engine"() {
		given:
			EngineSearch engine = Stub()
			engine.save(flush: true)
		when:
			engine.delete()
		then:
			EngineSearch.list().size() == 0
	}
}
