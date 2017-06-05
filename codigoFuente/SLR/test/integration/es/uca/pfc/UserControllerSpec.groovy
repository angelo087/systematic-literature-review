package es.uca.pfc



import grails.test.mixin.TestFor
import spock.lang.*

/**
 *
 */
@TestFor(UserController)
class UserControllerSpec extends Specification {

	User user
	User admin
	User superAdmin
	
    def setup() {
		// Create roles
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def superRole = Role.findByAuthority('ROLE_SUPER') ?: new Role(authority: 'ROLE_SUPER').save(failOnError: true)
		
		// Create profiles
		def profile01 = new UserProfile(
			first_name: 'Paco',
			last_name: 'Jimenez',
			display_name: 'Paco Jimenez',
			url_foto: 'http://fotos00.laopiniondemurcia.es/fotos/noticias/318x200/2011-11-02_IMG_2011-11-02_21:57:56_00902murmu.jpg')
		
		def mendProfile01 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		superAdmin = User.findByUsername('superadmin@uca.es') ?: new User(
			username: 'superadmin@uca.es',
			password: 'superadmin',
			userProfile: profile01,
			userMendeley: mendProfile01).save(failOnError: true)
		
		if (!superAdmin.authorities.contains(superRole)) {
			UserRole.create superAdmin, superRole
		}
    }

    def cleanup() {
    }
	
	void "test user list by super administrator"() {
		given:
			controller.springSecurityService.reauthenticate(superAdmin.username, superAdmin.password)
		when:
			def model = controller.list()
		then:
			model.userListInstance != null
			model.userListInstance.size() > 1
	}
}
