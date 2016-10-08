package es.uca.pfc

import grails.transaction.Transactional;

class UserController {

	// Servicios utilizados
	def springSecurityService
	def toolService
	def mendeleyToolService

	def index() { }

	def show()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Si no existe el guid, redirigimos a index
			if(!params.guid || params.guid.equals(""))
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				def userProfileInstance = UserProfile.findByGuid(params.guid)
				def isSynchro = params.isSynchro

				// Si es nulo, redirigimos a index
				if(userProfileInstance == null)
				{
					redirect(controller: 'index', action: 'index')
				}
				else
				{
					if(params.guidNotif != null)
					{
						def notification = Notification.findByGuidLike(params.guidNotif.toString())
						notification.leido = true;
						notification.save(failOnError: true, flush: true)
					}

					def userInstance = userProfileInstance.user
					def isMyProfile = false
					def isMyFriend  = 'N' // S: 'Si', N: 'No', P: 'Pendiente'
					def userLogin = User.get(springSecurityService.principal.id)

					// Comprobamos si es nuestro perfil de usuario o no.
					if(userLogin.id == userInstance.id)
					{
						isMyProfile = true;
					}

					//def relationFriend2 = userProfileInstance.friends.contains(userLogin.userProfile)

					// Son amigos
					if (userLogin.userProfile.friends.contains(userProfileInstance) && !isMyProfile)
					{
						isMyFriend = 'S';
					}

					def lastTime = toolService.getTimeString(userProfileInstance.ultimaConexion)

					respond userInstance, model:[isMyProfile: isMyProfile, profileInstance: userProfileInstance, lastTime: lastTime, isMyFriend: isMyFriend, isSynchro: isSynchro]
				}
			}
		}
	}


	@Transactional
	def addFollower()
	{
		def guidFriend = params.guid.toString();
		def userLogin = User.get(springSecurityService.principal.id)

		// Si no existe guid o no hay usuario logado
		if (guidFriend.equals("") || guidFriend == null || userLogin == null)
		{
			redirect(controller: 'index', action: 'menu')
		}
		else
		{
			def friendProfileInstance = UserProfile.findByGuid(guidFriend)
			// Si la persona existe o es la misma que esta logada
			if (friendProfileInstance == null || friendProfileInstance.id == userLogin.userProfile.id)
			{
				redirect(controller: 'index', action: 'menu')
			}
			else
			{
				// Comprobamos las relaciones de amistades y solicitudes entre ambas personas
				if(!userLogin.userProfile.friends.contains(friendProfileInstance))
				{
					userLogin.userProfile.addToFriends(friendProfileInstance).save(failOnError: true)
					// Creamos loggers entre ellos
					toolService.createLoggersBetweenUsers(userLogin.userProfile, friendProfileInstance)
				}
				redirect(controller: 'user', action: 'show', params: [guid: guidFriend])
			}
		}
	}

	@Transactional
	def removeFollower()
	{
		def guidFriend = params.guid.toString();
		def userLogin = User.get(springSecurityService.principal.id)

		// Si no existe guid o no hay usuario logado
		if (guidFriend.equals("") || guidFriend == null || userLogin == null)
		{
			redirect(controller: 'index', action: 'menu')
		}
		else
		{
			def friendProfileInstance = UserProfile.findByGuid(guidFriend)
			// Si la persona existe o es la misma que esta logada
			if (friendProfileInstance == null || friendProfileInstance.id == userLogin.userProfile.id)
			{
				redirect(controller: 'index', action: 'menu')
			}
			else
			{
				// Comprobamos las relaciones de amistades y solicitudes entre ambas personas
				if(userLogin.userProfile.friends.contains(friendProfileInstance))
				{
					userLogin.userProfile.removeFromFriends(friendProfileInstance).save(failOnError: true)
					// ELiminamos loggers
					toolService.removeLoggersBetweenUsers(userLogin.userProfile, friendProfileInstance)
					userLogin.userProfile.save(failOnError: true)
				}
				redirect(controller: 'user', action: 'show', params: [guid: guidFriend])
			}
		}
	}


	@Transactional
	def synchronizeUserProfile()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			// Si no existe el guid, redirigimos a index
			if(!params.guid || params.guid.equals(""))
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				def userProfileInstance = UserProfile.findByGuid(params.guid)

				// Si es nulo, redirigimos a index
				if(userProfileInstance == null)
				{
					redirect(controller: 'index', action: 'index')
				}
				else
				{
					boolean isSynchro = mendeleyToolService.synchronizeProfile(userProfileInstance.user)
					redirect(controller: 'user', action: 'show', params: [guid: userProfileInstance.guid, isSynchro: isSynchro])
				}
			}
		}
	}

	def list()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: "index", action: "index")
		}
		else
		{
			def userLogin = User.get(springSecurityService.principal.id)
			def userListInstance = User.list()
			String roleUserLogin = (userLogin.authorities.any { it.authority == "ROLE_SUPER" } ? "S" : (userLogin.authorities.any { it.authority == "ROLE_ADMIN" } ? "A" : "U"))

			[userListInstance: userListInstance, userLogin: userLogin, roleUserLogin: roleUserLogin]
		}
	}

	def enabledUser()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: "index", action: "index")
		}
		else
		{
			def userProfileInstance = UserProfile.findByGuidLike(params.guidUserEnabled.toString())
			def userLogin = User.get(springSecurityService.principal.id)

			if(userProfileInstance == null)
			{
				redirect(controller: "index", action: "index")
			}
			else
			{
				def userInstance = userProfileInstance.user
				if(toolService.canEnabledDisabled(userLogin, userInstance))
				{
					userInstance.enabled = true;
					userInstance.save(failOnError: true, flush: true)
				}
				redirect(controller: "user", action: "list")
			}
		}
	}

	def disabledUser()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: "index", action: "index")
		}
		else
		{
			def userProfileInstance = UserProfile.findByGuidLike(params.guidUserDisabled.toString())
			def userLogin = User.get(springSecurityService.principal.id)

			if(userProfileInstance == null)
			{
				redirect(controller: "index", action: "index")
			}
			else
			{
				def userInstance = userProfileInstance.user

				if(toolService.canEnabledDisabled(userLogin, userInstance))
				{
					userInstance.enabled = false;
					userInstance.save(failOnError: true, flush: true)
				}
				redirect(controller: "user", action: "list")
			}
		}
	}

	def changeToAdminRole()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: "index", action: "index")
		}
		else
		{
			def userProfileInstance = UserProfile.findByGuidLike(params.guidUserRoleAdmin.toString())
			def userLogin = User.get(springSecurityService.principal.id)

			if(userProfileInstance == null)
			{
				redirect(controller: "index", action: "index")
			}
			else
			{
				def userInstance = userProfileInstance.user

				if(userInstance.authorities.any { it.authority != "ROLE_ADMIN" } && toolService.canChangeRole(userLogin, userInstance))
				{
					UserRole.remove userInstance, Role.findByAuthority('ROLE_USER')
					UserRole.create userInstance, Role.findByAuthority('ROLE_ADMIN')
					userInstance.save(failOnError: true, flush: true)
				}
				redirect(controller: "user", action: "list")
			}
		}
	}

	def changeToUserRole()
	{
		def isLogin = springSecurityService.loggedIn

		if(!isLogin)
		{
			redirect(controller: "index", action: "index")
		}
		else
		{
			def userProfileInstance = UserProfile.findByGuidLike(params.guidUserRoleUser.toString())
			def userLogin = User.get(springSecurityService.principal.id)

			if(userProfileInstance == null)
			{
				redirect(controller: "index", action: "index")
			}
			else
			{
				def userInstance = userProfileInstance.user

				if(userInstance.authorities.any { it.authority != "ROLE_USER" } && toolService.canChangeRole(userLogin, userInstance))
				{
					UserRole.remove userInstance, Role.findByAuthority('ROLE_ADMIN')
					UserRole.create userInstance, Role.findByAuthority('ROLE_USER')
					userInstance.save(failOnError: true, flush: true)
				}
				redirect(controller: "user", action: "list")
			}
		}
	}
}
