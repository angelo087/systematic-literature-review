package es.uca.pfc

class SearcherController {

	def springSecurityService
	def MAX_TOTAL = 12;
	
    def index() 
	{
		def isLogin = springSecurityService.loggedIn
		
		if(!isLogin)
		{
			redirect(controller:'index', action: 'index')
		}
		else
		{
			String query = params.query == null ? "" : params.query
			
			query = query.trim()
			
			// Buscamos usuarios
			def userList = UserProfile.findAllByDisplay_nameIlike("%"+query+"%", [sort: 'display_name', order: 'asc'])
			def totalUserList = userList.size()
			userList = UserProfile.findAllByDisplay_nameIlike("%"+query+"%", [sort: 'display_name', order: 'asc', max: MAX_TOTAL])
			
			// Buscamos SLR's
			def slrList = Slr.findAllByTitleIlike("%"+query+"%", [sort: 'title', order: 'asc'])
			def totalSlrList = slrList.size()
			slrList = Slr.findAllByTitleIlike("%"+query+"%", [sort: 'title', order: 'asc', max: MAX_TOTAL])
			
			[query: query, userList: userList, slrList: slrList, 
				totalUserList: totalUserList, totalSlrList: totalSlrList]
		}
	}
	
	def search()
	{
		redirect(controller: 'searcher', action: 'index', params: params)
	}
}
