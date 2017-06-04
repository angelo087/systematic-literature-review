package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Language)
class LanguageSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		Language.withNewSession {
			Language.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create language"() {
		given: 
			Language language = Stub()	
		when:
			language.save()
		then:
			!language.errors.hasFieldErrors("name")
			!language.errors.hasFieldErrors("country")
			!language.errors.hasFieldErrors("code")
    }
	
	void "test update language"() {
		given:
			Language language = Stub()
			language.save()
		when:
			language.code >> "other code"
			language.save()
		then:
			!language.errors.hasFieldErrors("name")
			!language.errors.hasFieldErrors("country")
			!language.errors.hasFieldErrors("code")
			language.code == "other code"
	}
	
	void "test delete language"() {
		given:
			Language language = Stub()
			language.save(flush: true)
		when:
			language.delete()
		then:
			Language.list().size() == 0
	}
}
