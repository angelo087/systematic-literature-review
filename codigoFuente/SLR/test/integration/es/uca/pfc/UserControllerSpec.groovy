package es.uca.pfc



import grails.test.mixin.TestFor
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import spock.lang.*

/**
 *
 */
@TestFor(UserController)
class UserControllerSpec extends Specification {

	User user
	User admin
	User superAdmin
	
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
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
        def superRole = Role.findByAuthority('ROLE_SUPER') ?: new Role(authority: 'ROLE_SUPER').save(failOnError: true)
		
		// Create profiles
		def profileSuperAdmin = new UserProfile(
			first_name: 'Super Admin',
			last_name: 'User',
			display_name: 'Super Admin User',
			url_foto: 'http://www.free-icons-download.net/images/user-icon-44709.png')
		
		def profileAdmin = new UserProfile(
			first_name: 'Admin',
			last_name: 'User',
			display_name: 'Admin User',
			url_foto: 'http://www.free-icons-download.net/images/user-icon-44709.png')
		
		def profileUser = new UserProfile(
			first_name: 'User',
			last_name: 'No Admin No Super',
			display_name: 'User No Admin No Super',
			url_foto: 'http://www.free-icons-download.net/images/user-icon-44709.png')
		
		def mendProfileSuperAdmin = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def mendProfileAdmin = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def mendProfileUser = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		superAdmin = User.findByUsername('superadmin@uca.es') ?: new User(
			username: 'superadmin@uca.es',
			password: 'superadminpassword',
			userProfile: profileSuperAdmin,
			userMendeley: mendProfileSuperAdmin).save(failOnError: true)
		
		if (!superAdmin.authorities.contains(superRole)) {
			UserRole.create superAdmin, superRole
		}
		
		admin = User.findByUsername('admin@uca.es') ?: new User(
			username: 'admin@uca.es',
			password: 'adminpassword',
			userProfile: profileAdmin,
			userMendeley: mendProfileAdmin).save(failOnError: true)
		
		if (!admin.authorities.contains(adminRole)) {
			UserRole.create admin, adminRole
		}
		
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
		logoutUser()
    }
	
	void "test user list by super administrator"() {
		given:
			controller.springSecurityService.reauthenticate(superAdmin.username, superAdmin.password)
		when:
			def model = controller.list()
		then:
			model.userListInstance != null
			model.userListInstance.size() >= 1
	}
	
	void "test user list by administrator"() {
		given:
			controller.springSecurityService.reauthenticate(admin.username, admin.password)
		when:
			def model = controller.list()
		then:
			model.userListInstance != null
			model.userListInstance.size() >= 1
	}
	
	void "test user list by user"() {
		given:
			controller.springSecurityService.reauthenticate(user.username, user.password)
		when:
			def model = controller.list()
		then:
			model == null
	}
}
