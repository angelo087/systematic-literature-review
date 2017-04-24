package es.uca.pfc

class IndexController {

	def springSecurityService
	def toolService
	def graphService
	def maxPerPage = 10
	
    def index() 
	{
		// Si no esta logado, redirigimos a la pagina principal
		if(springSecurityService.isLoggedIn())
		{
			redirect(controller: "index", action: "menu")
			return
		}
	}
	
	def loadNotifications()
	{
		def isLogin = springSecurityService.isLoggedIn()
		
		if (isLogin)
		{
			def userInstance = User.get(springSecurityService.principal.id)
			
			def notifications = Notification.findAllByProfileAndLeido(userInstance.userProfile, false, [sort: 'fecha', order: 'desc'])	
			
			for(Notification not : notifications)
			{
				not.fechaString = toolService.getTimeString(not.fecha)
				not.save(failOnError: true, flush: true)
			}
				
			Map model = [notificationList: notifications]
			render(template: 'notificationsUser', model: model)
		}
	}
	
	def menu()
	{		
		// Si no esta logado, redirigimos a la pagina principal.
		if(!springSecurityService.isLoggedIn())
		{
			redirect(controller: 'index', action: 'index')
			return
		}
		
		// Perfil usuario
		def userProfileInstance = User.get(springSecurityService.principal.id).userProfile
		
		// Logers usuario
		def loggerListInstance = Logger.findAllByProfile(userProfileInstance,[sort: 'submitDate', order: 'desc'])
		
		loggerListInstance = toolService.updateTimeStringLogger(loggerListInstance)
		
		// Usuarios conectados
		List<User> usersOnline = toolService.getUsersOnline()
		def totalUsersOnline = 0
		
		def lastUsersRegistered = UserProfile.list(max: 10, sort: 'fechaRegistro', order: 'desc')
		for(UserProfile profile : lastUsersRegistered)
		{
			profile.isOnline = usersOnline.contains(profile.user)
			profile.save(failOnError: true, flush: true)
			
			if(profile.isOnline)
			{
				totalUsersOnline++
			}
		}
		
		// Ultimos SLR's creados
		def lastSlrCreated = Slr.list(max: 5, sort: 'submitDate', order: 'desc')
		
		lastSlrCreated = toolService.updateTimeStringSlr(lastSlrCreated)
		
		// Estadisticas
		def totalSLR = Slr.list().size()
		def totalUsers = User.list().size()
		
		// Grafica slr's creados en los ultimos 5 meses
		String queryChartIndex = graphService.chartTotal5LastSlrCreated(userProfileInstance)
		
		// Total referencias incluidas
		int totalRefsIncluded = 0
		for(Slr slrInstance : userProfileInstance.slrs)
		{
			for(Search searchInstance : slrInstance.searchs)
			{
				for(Reference referenceInstance : searchInstance.references)
				{
					if(referenceInstance.criterion.name.toLowerCase().equals("included"))
					{
						totalRefsIncluded++
					}
				}
			}
		}
		
		// Total task searchs
		/*def totalTaskSearchs = 0
		def taskSearchList = TaskSearch.list(sort: 'submitDate', order: 'desc')
		List<String> guidsSlr = new ArrayList<String>()
		for(Slr slrInstance : userProfileInstance.slrs)
		{
			guidsSlr.add(slrInstance.guid)
		}
		for(TaskSearch t : taskSearchList)
		{
			if(guidsSlr.contains(t.guidSlr) && !t.hasErrors && t.percentage != 100)
			{
				totalTaskSearchs++
			}
		}*/
		def totalTaskSearchs = toolService.getNotCompletedTaskSearchFromUser(userProfileInstance.user).size()
		
		[loggerListInstance: loggerListInstance, userProfileInstance: userProfileInstance, lastUsersRegistered: lastUsersRegistered,
			lastSlrCreated: lastSlrCreated, totalSLR: totalSLR, totalUsers: totalUsers, totalUsersOnline: totalUsersOnline,
			queryChartIndex: queryChartIndex, totalRefsIncluded: totalRefsIncluded, totalTaskSearchs: totalTaskSearchs]
	}
	
	def menu2() { }
	
	def index2() { }
	
	def loggers()
	{
		// Si no est� logado, redirigimos a la pagina principal.
		if(!springSecurityService.isLoggedIn())
		{
			redirect(controller: 'index', action: 'index')
			return
		}
		def userProfileInstance = User.get(springSecurityService.principal.id).userProfile
		
		List<Logger> allLoggers = new ArrayList<Logger>()
		List<Logger> myLoggers = new ArrayList<Logger>()
		List<Logger> friendsLoggers = new ArrayList<Logger>()
		
		// Todos los loggers
		allLoggers.addAll(Logger.findAllByProfile(userProfileInstance, [sort: 'submitDate', order: 'desc']))
		int totalAllLoggers = allLoggers.size()
		
		// Mis Loggers
		myLoggers.addAll(Logger.findAllByProfileAndTipoInList(userProfileInstance,['bienvenida','crear','buscar','seguir'],[sort: 'submitDate', order: 'desc']))
		int totalMyLoggers = myLoggers.size()
		
		// Loggers de amistades
		friendsLoggers.addAll(Logger.findAllByProfileAndTipoInList(userProfileInstance,['fr-bienvenida','fr-crear','fr-buscar','fr-seguir'],[sort: 'submitDate', order: 'desc']))
		int totalFriendsLoggers = friendsLoggers.size()

		def totalPagesAllLoggers = Math.round(totalAllLoggers / maxPerPage);
		def totalPagesMyLoggers = Math.round(totalMyLoggers / maxPerPage);
		def totalPagesFriendsLoggers = Math.round(totalFriendsLoggers / maxPerPage);
		def pageAllLoggers = 1
		def pageMyLoggers = 1
		def pageFriendsLoggers = 1
		
		def currentOnglet = (params.currentOnglet.toString().equals("") || params.currentOnglet.toString().equals("null") || params.currentOnglet.toString().equals(null)) ? "all" :  params.currentOnglet.toString() //all, my or friend		
		def page = (params.page.toString().equals("") || params.page.toString().equals("null") || params.page.toString().equals(null)) ? 1 : Integer.parseInt(params.page.toString())

		if (currentOnglet == 'my')
		{
			pageMyLoggers = page
		}
		else if (currentOnglet == 'friend')
		{
			pageFriendsLoggers = page
		}
		else //all or other
		{
			currentOnglet = 'all'
			pageAllLoggers = page
		}

		def offsetAllLoggers = (pageAllLoggers * maxPerPage) - maxPerPage
		def offsetMyLoggers = (pageMyLoggers * maxPerPage) - maxPerPage
		def offsetFriendsLoggers = (pageFriendsLoggers * maxPerPage) - maxPerPage
		
		allLoggers.clear()
		myLoggers.clear()
		friendsLoggers.clear()
		allLoggers.addAll(Logger.findAllByProfile(userProfileInstance, [sort: 'submitDate', order: 'desc', offset: offsetAllLoggers, max: maxPerPage]))
		myLoggers.addAll(Logger.findAllByProfileAndTipoInList(userProfileInstance,['bienvenida','crear','buscar','seguir'],[sort: 'submitDate', order: 'desc', offset: offsetMyLoggers, max: maxPerPage]))
		friendsLoggers.addAll(Logger.findAllByProfileAndTipoInList(userProfileInstance,['fr-bienvenida','fr-crear','fr-buscar','fr-seguir'],[sort: 'submitDate', order: 'desc', offset: offsetFriendsLoggers, max: maxPerPage]))
		
		[
			profileInstance: userProfileInstance,
			allLoggers: allLoggers,
			myLoggers: myLoggers,
			friendsLogers: friendsLoggers,
			totalAllLoggers: totalAllLoggers,
			totalMyLoggers: totalMyLoggers,
			totalFriendsLoggers: totalFriendsLoggers,
			currentOnglet: currentOnglet,
			pageAllLoggers: pageAllLoggers,
			pageMyLoggers: pageMyLoggers,
			pageFriendsLoggers: pageFriendsLoggers,
			totalAllLoggers: totalAllLoggers,
			totalMyLoggers: totalMyLoggers,
			totalFriendsLoggers: totalFriendsLoggers
		]
		
	}
	
	def faqs()
	{
		def userProfileInstance = null
		def isLogin = springSecurityService.isLoggedIn()
		if(isLogin)
		{
			userProfileInstance = User.get(springSecurityService.principal.id).userProfile
		}
		
		[
			faqListInstance: FAQ.list(),
			userProfileInstance: userProfileInstance,
			isLogin: isLogin
		]
	}
	
	// Para Mendeley
	def indexMendeley()
	{
		
	}
	
	def loadTaskSearchs()
	{
		def isLogin = springSecurityService.isLoggedIn()
		
		if (!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def userInstance = User.get(springSecurityService.principal.id)
			def taskSearchUser = toolService.getAllTaskSearchFromUser(userInstance)
			
			Map model = [taskSearchList: taskSearchUser]
			render(template: 'taskSearchList', model: model)
		}
	}
	
	def loadTaskSearchsHead()
	{
		def isLogin = springSecurityService.isLoggedIn()
		
		if (!isLogin)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def userInstance = User.get(springSecurityService.principal.id)
			def taskSearchUser = toolService.getAllTaskSearchFromUser(userInstance)
			
			Map model = [taskSearchListHead: taskSearchUser]
			render(template: 'taskSearchListHead', model: model)
		}
	}
	
	def taskSearchs()
	{
		def isLogin = springSecurityService.isLoggedIn()
		
		if (!isLogin)
		{
			redirect(controller: 'index', action: 'index')			
		}
		else
		{
			def userInstance = User.get(springSecurityService.principal.id)
			def taskSearchUser = toolService.getAllTaskSearchFromUser(userInstance)
			
			[taskSearchList: taskSearchUser, dateNow: new Date()]
		}
	}
}
