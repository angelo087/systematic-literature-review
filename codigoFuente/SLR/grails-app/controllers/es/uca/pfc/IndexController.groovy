package es.uca.pfc

class IndexController {

	def springSecurityService
	
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
	}
	
	def menu2() { }
	
	def index2() { }
}
