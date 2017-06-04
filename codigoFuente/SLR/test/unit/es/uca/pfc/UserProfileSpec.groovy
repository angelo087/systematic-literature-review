package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserProfile)
class UserProfileSpec extends Specification {

    def setup() {
    }

    def cleanup() {
		UserProfile.withNewSession {
			UserProfile.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create userProfile"() {
		given: 
			UserProfile userProfile = Stub()	
		when:
			userProfile.save()
		then:
			!userProfile.errors.hasFieldErrors("user")
			!userProfile.errors.hasFieldErrors("slrs")
			!userProfile.errors.hasFieldErrors("first_name")
			!userProfile.errors.hasFieldErrors("last_name")
			!userProfile.errors.hasFieldErrors("idmend")
			
    }
	
	void "test update userProfile"() {
		given:
			UserProfile userProfile = Stub()
			userProfile.save()
		when:
			userProfile.first_name >> 'other first_name'
			userProfile.save()
		then:
			!userProfile.errors.hasFieldErrors("user")
			!userProfile.errors.hasFieldErrors("slrs")
			!userProfile.errors.hasFieldErrors("first_name")
			!userProfile.errors.hasFieldErrors("last_name")
			!userProfile.errors.hasFieldErrors("idmend")
			userProfile.first_name.equals("other first_name")
	}
	
	void "test delete userProfile"() {
		given:
			UserProfile userProfile = Stub()
			userProfile.save(flush: true)
		when:
			userProfile.delete()
		then:
			UserProfile.list().size() == 0
	}
}
