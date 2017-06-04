package es.uca.pfc

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UserMendeley)
class UserMendeleySpec extends Specification {

    def setup() {
    }

    def cleanup() {
		UserMendeley.withNewSession {
			UserMendeley.findAll().each { it.delete(flush: true) }
		}
    }

    void "test create userMendeley"() {
		given: 
			UserMendeley userMendeley = Stub()	
		when:
			userMendeley.save()
		then:
			!userMendeley.errors.hasFieldErrors("email_mend")
			!userMendeley.errors.hasFieldErrors("access_token")
			!userMendeley.errors.hasFieldErrors("token_type")
			!userMendeley.errors.hasFieldErrors("refresh_token")
    }
	
	void "test update userMendeley"() {
		given:
			UserMendeley userMendeley = Stub()
			userMendeley.save()
		when:
			userMendeley.access_token >> 'otherAccessToken'
			userMendeley.save()
		then:
			!userMendeley.errors.hasFieldErrors("email_mend")
			!userMendeley.errors.hasFieldErrors("access_token")
			!userMendeley.errors.hasFieldErrors("token_type")
			!userMendeley.errors.hasFieldErrors("refresh_token")
			userMendeley.access_token.equals("otherAccessToken")
	}
	
	void "test delete userMendeley"() {
		given:
			UserMendeley userMendeley = Stub()
			userMendeley.save(flush: true)
		when:
			userMendeley.delete()
		then:
			UserMendeley.list().size() == 0
	}
}
