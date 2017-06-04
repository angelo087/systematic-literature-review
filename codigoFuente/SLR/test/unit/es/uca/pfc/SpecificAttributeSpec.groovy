package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SpecificAttribute)
class SpecificAttributeSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		SpecificAttribute.withNewSession {
			SpecificAttribute.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create specificAttribute"() {
		given: 
			SpecificAttribute specificAttribute = Stub()	
		when:
			specificAttribute.save()
		then:
			!specificAttribute.errors.hasFieldErrors("userProfile")
			!specificAttribute.errors.hasFieldErrors("searchs")
			!specificAttribute.errors.hasFieldErrors("attributesReferences")
			!specificAttribute.errors.hasFieldErrors("slr")
    }
	
	void "test update specificAttribute"() {
		given:
			SpecificAttribute specificAttribute = Stub()
			specificAttribute.save()
		when:
			specificAttribute.tipo >> 'list'
			specificAttribute.save()
		then:
			!specificAttribute.errors.hasFieldErrors("userProfile")
			!specificAttribute.errors.hasFieldErrors("searchs")
			!specificAttribute.errors.hasFieldErrors("attributesReferences")
			!specificAttribute.errors.hasFieldErrors("slr")
			specificAttribute.tipo.equals("list")
	}
	
	void "test delete specificAttribute"() {
		given:
			SpecificAttribute specificAttribute = Stub()
			specificAttribute.save(flush: true)
		when:
			specificAttribute.delete()
		then:
			SpecificAttribute.list().size() == 0
	}
}
