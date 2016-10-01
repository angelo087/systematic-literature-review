// Place your Spring DSL code here
/*beans = {
}*/

import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

import es.uca.pfc.MendeleyToolService;
import es.uca.pfc.authentication.CustomAuthenticationProvider

beans = {
	
	customAuthenticationProvider(CustomAuthenticationProvider) 
	{
		mendeleyToolService = ref('mendeleyToolService')
		springSecurityService = ref('springSecurityService')
	}

	sessionRegistry(SessionRegistryImpl)

	sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
			maximumSessions = -1
	}

	concurrentSessionFilter(ConcurrentSessionFilter){
			sessionRegistry = sessionRegistry
			expiredUrl = '/login/concurrentSession'
	}
}
