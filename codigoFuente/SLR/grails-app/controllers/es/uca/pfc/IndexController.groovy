package es.uca.pfc

class IndexController {

	def springSecurityService
	
    def index() 
	{
		// Si no est� logado, redirigimos a la p�gina principal.
		if(springSecurityService.isLoggedIn())
		{
			redirect(controller: "index", action: "menu")
			return
		}
	}
	
	def menu()
	{
		// Si no est� logado, redirigimos a la p�gina principal.
		if(!springSecurityService.isLoggedIn())
		{
			redirect(controller: 'index', action: 'index')
			return
		}
	}
	
	def menu2() { }
	
	def index2() { }
}
