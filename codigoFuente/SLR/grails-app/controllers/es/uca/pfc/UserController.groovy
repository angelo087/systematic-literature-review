package es.uca.pfc

class UserController {
	
	// Servicios utilizados
	def springSecurityService
	def toolService
	
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
				
				// Si es nulo, redirigimos a index
				if(userProfileInstance == null)
				{
					redirect(controller: 'index', action: 'index')
				}
				else
				{
					def userInstance = userProfileInstance.user			
					def isMyProfile = false
					def isMyFriend  = 'N' // S: 'Si', N: 'No', P: 'Pendiente'
					def userLogin = User.get(springSecurityService.principal.id)
					
					// Comprobamos si es nuestro perfil de usuario o no.
					if(userLogin.id == userInstance.id)
					{
						isMyProfile = true;
					}
					
					def relationFriend1 = userLogin.userProfile.friends.contains(userProfileInstance)
					def relationFriend2 = userProfileInstance.friends.contains(userLogin.userProfile)
					def relationPendent = userLogin.userProfile.requests.contains(userProfileInstance)
					
					// Son amigos
					if (relationFriend1 && relationFriend2)
					{
						isMyFriend = 'S';
					}
					else if (relationPendent) // Pendientes de amistad
					{
						isMyFriend = 'P';
					}
					
					def lastTime = toolService.getTimeString(userProfileInstance.ultimaConexion)
					
					respond userInstance, model:[isMyProfile: isMyProfile, profileInstance: userProfileInstance, lastTime: lastTime, isMyFriend: isMyFriend]
				}
			}
		}
	}
	
	def addRequestFriends()
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
			
			// Si la persona existe o es la misma que está logada
			if (friendProfileInstance == null || friendProfileInstance.id == userLogin.userProfile.id)
			{
				redirect(controller: 'index', action: 'menu')
			}
			else
			{
				// Comprobamos las relaciones de amistades y solicitudes entre ambas personas
				def relation1 = userLogin.userProfile.friends.contains(friendProfileInstance)
				def relation2 = friendProfileInstance.friends.contains(userLogin.userProfile)
				def request1  = userLogin.userProfile.requests.contains(friendProfileInstance)
				def request2  = friendProfileInstance.requests.contains(userLogin.userProfile)
				
				if (!((!relation1 && !relation2) || (!request1)))
				{
					// ELiminamos las relaciones entre ellos por seguridad
					userLogin.userProfile.removeFromRequests(friendProfileInstance).save(failOnError: true)
					friendProfileInstance.removeFromRequests(userLogin.userProfile).save(failOnError: true)
					userLogin.userProfile.removeFromFriends(friendProfileInstance).save(failOnError: true)
					friendProfileInstance.removeFromFriends(userLogin.userProfile).save(failOnError: true)
					
					redirect(controller: 'index', action: 'menu')
				}
				else
				{
					if (request2)
					{
						// Ambos serán amigos y se eliminaran de sus request
						userLogin.userProfile.removeFromRequests(friendProfileInstance).save(failOnError: true)
						friendProfileInstance.removeFromRequests(userLogin.userProfile).save(failOnError: true)
						userLogin.userProfile.addToFriends(friendProfileInstance).save(failOnError: true)
						friendProfileInstance.addToFriends(userLogin.userProfile).save(failOnError: true)
					}
					else
					{
						// Se enviará una solicitud de amistad
						userLogin.userProfile.addToRequests(friendProfileInstance).save(failOnError: true)
					}
					redirect(controller: 'user', action: 'show', params: [guid: guidFriend])
				}				
			}
		}
	}
	
	def removeRequestFriends()
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
			
			// Si la persona existe o es la misma que está logada
			if (friendProfileInstance == null || friendProfileInstance.id == userLogin.userProfile.id)
			{
				redirect(controller: 'index', action: 'menu')
			}
			else
			{
				userLogin.userProfile.removeFromFriends(friendProfileInstance).save(failOnError: true)
				friendProfileInstance.removeFromFriends(userLogin.userProfile).save(failOnError: true)
				userLogin.userProfile.removeFromRequests(friendProfileInstance).save(failOnError: true)
				friendProfileInstance.removeFromRequests(userLogin.userProfile).save(failOnError: true)
				
				redirect(controller: 'user', action: 'show', params: [guid: guidFriend])
			}
		}
	}
}
