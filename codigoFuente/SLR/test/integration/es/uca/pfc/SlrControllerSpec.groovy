package es.uca.pfc

import grails.test.mixin.TestFor
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import spock.lang.*
import grails.plugin.springsecurity.SpringSecurityService

/**
 *
 */
@TestFor(SlrController)
class SlrControllerSpec extends Specification {

	User userInstance
	
	def logoutUser() {
		if(controller.springSecurityService != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
		}
	}
	
    def setup() {
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		
		def userProfile = new UserProfile(
            first_name: 'Paco',
            last_name: 'Jimenez',
            display_name: 'Paco Jimenez',
            url_foto: 'http://fotos00.laopiniondemurcia.es/fotos/noticias/318x200/2011-11-02_IMG_2011-11-02_21:57:56_00902murmu.jpg')
		
		def mendProfile = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		userInstance = User.findByUsername('useremail@domain.es') ?: new User(
			username: 'useremail@domain.es',
			password: 'passuseremail',
			userProfile: userProfile,
			userMendeley: mendProfile).save(failOnError: true)
		
		if (!userInstance.authorities.contains(userRole)) {
			UserRole.create userInstance, userRole
		}
		
		userInstance.userProfile.addToSlrs(new Slr(title: 'Titulo 1', justification: 'Justificacion 1', userProfile: userInstance.userProfile, idmend: 'asdsad')).save(failOnError: true, flush: true)
		userInstance.userProfile.addToSlrs(new Slr(title: 'Titulo 2', justification: 'Justificacion 2', userProfile: userInstance.userProfile, idmend: 'asdsad')).save(failOnError: true, flush: true)
		userInstance.userProfile.addToSlrs(new Slr(title: 'Titulo 3', justification: 'Justificacion 3', userProfile: userInstance.userProfile, idmend: 'asdsad')).save(failOnError: true, flush: true)
    }

    def cleanup() {
		logoutUser()
    }
	
	void "list all SLR"() {
		given:
			controller.springSecurityService.reauthenticate(userInstance.username, 
															userInstance.password)
		when:
			def model = controller.myList()
		then:
			model.slrListInstance.size() == 3
    }
}
