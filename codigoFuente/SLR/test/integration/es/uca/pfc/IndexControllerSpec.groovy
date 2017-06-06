package es.uca.pfc



import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

import spock.lang.*
import grails.test.mixin.TestFor

/**
 *
 */
@TestFor(IndexController)
class IndexControllerSpec extends Specification {

	User user
	
	def logoutUser() {
		if(controller.springSecurityService != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
		}
	}
	
    def setup() {
		// Create roles
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def profileUser = new UserProfile(
			first_name: 'User',
			last_name: 'No Admin No Super',
			display_name: 'User No Admin No Super',
			url_foto: 'http://www.free-icons-download.net/images/user-icon-44709.png')
		def mendProfileUser = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		user = User.findByUsername('user@uca.es') ?: new User(
			username: 'user@uca.es',
			password: 'userpassword',
			userProfile: profileUser,
			userMendeley: mendProfileUser).save(failOnError: true)
		
		if (!user.authorities.contains(userRole)) {
			UserRole.create user, userRole
		}
    }

    def cleanup() {
    }

    void "test go to main menu by user with login"() {
		given:
			controller.springSecurityService.reauthenticate(user.username, user.password)
		when:
			def model = controller.menu()
		then:
			model != null
			model.loggerListInstance != null
			model.userProfileInstance != null
			model.userProfileInstance == user.userProfile
			model.lastUsersRegistered != null
			model.lastSlrCreated != null
			model.totalSLR >= 0
			model.totalUsers >= 0
			model.totalUsersOnline >= 0
			model.totalRefsIncluded >= 0
			model.totalTaskSearchs >= 0
    }
	
	void "test go to main menu by user without login"() {
		given:
			logoutUser()
		when:
			def model = controller.menu()
		then:
			model == null
	}
	
	void "get loggers of an user"() {
		given:
			controller.springSecurityService.reauthenticate(user.username, user.password)
		when:
			def model = controller.loggers()
		then:
			model.allLoggers != null
			model.myLoggers != null
			model.friendsLogers != null
	}
	
	void "get faqs"() {
		given:
			controller.springSecurityService.reauthenticate(user.username, user.password)
		when:
			def model = controller.faqs()
		then:
			model.faqListInstance != null
		
	}
}
