package es.uca.pfc

class IndexController {

	def springSecurityService
	def toolService
	def maxPerPage = 10
	
    def index() 
	{
		// Si no está logado, redirigimos a la página principal.
		if(springSecurityService.isLoggedIn())
		{
			redirect(controller: "index", action: "menu")
			return
		}
	}
	
	def menu()
	{
		// Si no está logado, redirigimos a la página principal.
		if(!springSecurityService.isLoggedIn())
		{
			redirect(controller: 'index', action: 'index')
			return
		}
		def userProfileInstance = User.get(springSecurityService.principal.id).userProfile
		
		def loggerListInstance = Logger.findAllByProfile(userProfileInstance,[sort: 'submitDate', order: 'desc'])
		
		loggerListInstance = toolService.updateTimeStringLogger(loggerListInstance)
		
		[loggerListInstance: loggerListInstance, userProfileInstance: userProfileInstance]
	}
	
	def menu2() { }
	
	def index2() { }
	
	def loggers()
	{
		// Si no está logado, redirigimos a la página principal.
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
}
