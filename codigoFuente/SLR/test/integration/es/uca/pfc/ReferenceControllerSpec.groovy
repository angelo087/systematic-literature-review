package es.uca.pfc


import grails.test.mixin.TestFor
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import spock.lang.*

/**
 *
 */
@TestFor(ReferenceController)
class ReferenceControllerSpec extends Specification {

    User user
	
	String idmend01 = 'mend01'
	String idmend02 = 'mend02'
	
	def logoutUser() {
		if(controller.springSecurityService != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
		}
	}
	
    def setup() {
		// Create rol
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
		
		//user.userProfile.addToSlrs(new Slr(title: 'Titulo 1', justification: 'Justificacion 1', userProfile: userInstance.userProfile, idmend: 'asdsad')).save(failOnError: true, flush: true)		
		def engine01 = EngineSearch.findByName('ACM') ?: new EngineSearch(name: 'ACM', display_name: 'ACM Digital Library', image: 'acm.jpeg', text: 'ACM').save(failOnError: true)
        def engine02 = EngineSearch.findByName('IEEE') ?: new EngineSearch(name: 'IEEE', display_name: 'IEEE Computer Society', image: 'ieee.jpeg', text: 'IEEE').save(failOnError: true)
        def type01 = TypeDocument.findByNomenclatura('journal') ?: new TypeDocument(nombre: 'Journal', nomenclatura: 'journal').save(failOnError: true)
        def type02 = TypeDocument.findByNomenclatura('book') ?: new TypeDocument(nombre: 'Book', nomenclatura: 'book').save(failOnError: true)
        def langEN = Language.findByName('english') ?: new Language(name: 'english', code: 'EN', image: 'flag-EN.PNG').save(failOnError: true)
		def opALL = SearchOperator.findByValue('all') ?: new SearchOperator(name: 'ALL', value: 'all').save(failOnError: true)
		def comp01 = SearchComponent.findByValue('full-text') ?: new SearchComponent(name: 'Full-text', value: 'full-text').save(failOnError: true)
		
		Slr slr01 = new Slr(title: 'Titulo 1', justification: 'Justificacion 1', userProfile: user.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		def question01 = new ResearchQuestion(enunciado: '¿Pregunta 1?')
		slr01.addToQuestions(question01)
		def terms01 = new SearchTermParam(terminos: "terminos 1", component: comp01, operator: opALL)
		
		def search01 = new Search(startYear: "2010", endYear: "2012", maxTotal: 5)
		search01.addToEngines(engine01); search01.addToTermParams(terms01)
		def reference01 = new Reference(idmend : idmend01, engine: engine01, title : 'Reference 1', type : type01, docAbstract : 'Abstract 1', source : 'Source 1', year : '1987',    keywords: ["key1", "key2", "key3"], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1', 'tag2', 'tag3'], citation_key : 'citationkey1', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 1')
		def reference02 = new Reference(idmend : idmend02, engine: engine02, title : 'Reference 2', type : type02, docAbstract : 'Abstract 2', source : 'Source 2', year : '1987',    keywords: ['key5', 'key3', 'key2'], pages : '1', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Madrid', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag2', 'tag3'], citation_key : 'citationkey2', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 2')
		search01.addToReferences(reference01)
		search01.addToReferences(reference02)
		slr01.addToSearchs(search01)
    }
	
	def cleanup() {
		logoutUser()
	}

    void "show a reference"() {
		given:
			controller.springSecurityService.reauthenticate(user.username, user.password)
			controller.params.idmend = idmend01
		when:
			def model = controller.show()
		then:
			model.referenceInstance != null
    }
}
