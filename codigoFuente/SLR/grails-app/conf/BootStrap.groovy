import java.util.List;
import java.util.Date;

import es.uca.pfc.Author
import es.uca.pfc.AuthorReference
import es.uca.pfc.Criterion
import es.uca.pfc.Education
import es.uca.pfc.EngineSearch
import es.uca.pfc.FAQ
import es.uca.pfc.Language
import es.uca.pfc.Logger
import es.uca.pfc.LoggerFriend
import es.uca.pfc.LoggerSlr
import es.uca.pfc.MendeleyApi
import es.uca.pfc.Notification
import es.uca.pfc.Reference
import es.uca.pfc.ResearchQuestion
import es.uca.pfc.Role
import es.uca.pfc.Search
import es.uca.pfc.SearchComponent
import es.uca.pfc.SearchOperator
import es.uca.pfc.SearchTermParam
import es.uca.pfc.Slr
import es.uca.pfc.SpecificAttribute
import es.uca.pfc.SpecificAttributeMultipleValue
import es.uca.pfc.TypeDocument
import es.uca.pfc.User
import es.uca.pfc.UserMendeley
import es.uca.pfc.UserRole
import es.uca.pfc.UserProfile

class BootStrap {

    def init = { servletContext ->
		
		log.info "Creamos los componentes de busquedas..."
		
		def comp01 = SearchComponent.findByValue('full-text') ?: new SearchComponent(name: 'Full-text', value: 'full-text').save(failOnError: true)
		def comp02 = SearchComponent.findByValue('abstract') ?: new SearchComponent(name: 'Abstract', value: 'abstract').save(failOnError: true)
		def comp03 = SearchComponent.findByValue('review') ?: new SearchComponent(name: 'Review', value: 'review').save(failOnError: true)
		def comp04 = SearchComponent.findByValue('title') ?: new SearchComponent(name: 'Title', value: 'title').save(failOnError: true)
		def comp05 = SearchComponent.findByValue('author') ?: new SearchComponent(name: 'Author', value: 'author').save(failOnError: true)
		def comp06 = SearchComponent.findByValue('any-field') ?: new SearchComponent(name: 'Any Field', value: 'any-field').save(failOnError: true)
		def comp07 = SearchComponent.findByValue('publisher') ?: new SearchComponent(name: 'Publisher', value: 'publisher').save(failOnError: true)
		def comp08 = SearchComponent.findByValue('isbn') ?: new SearchComponent(name: 'ISBN', value: 'isbn').save(failOnError: true)
		def comp09 = SearchComponent.findByValue('issn') ?: new SearchComponent(name: 'ISSN', value: 'issn').save(failOnError: true)
		def comp10 = SearchComponent.findByValue('doi') ?: new SearchComponent(name: 'DOI', value: 'doi').save(failOnError: true)
		def comp11 = SearchComponent.findByValue('keywords') ?: new SearchComponent(name: 'Keywords', value: 'keywords').save(failOnError: true)
		
		log.info "Creamos los operators de busquedas..."
		def opALL = SearchOperator.findByValue('all') ?: new SearchOperator(name: 'ALL', value: 'all').save(failOnError: true)
		def opANY  = SearchOperator.findByValue('any') ?: new SearchOperator(name: 'ANY', value: 'any').save(failOnError: true)
		def opNONE = SearchOperator.findByValue('none') ?: new SearchOperator(name: 'NONE', value: 'none').save(failOnError: true)
		
		log.info "Creamos los idiomas..."
		def langES = Language.findByName('spanish') ?: new Language(name: 'spanish', code: 'ES', image: 'flag-ES.PNG').save(failOnError: true)
		def langEN = Language.findByName('english') ?: new Language(name: 'english', code: 'EN', image: 'flag-EN.PNG').save(failOnError: true)
		def langDE = Language.findByName('german') ?: new Language(name: 'german', code: 'DE', image: 'flag-DE.PNG').save(failOnError: true)
		def langCH = Language.findByName('chinese') ?: new Language(name: 'chinese', code: 'CH', image: 'flag-CH.PNG').save(failOnError: true)
		def langFR = Language.findByName('french') ?: new Language(name: 'french', code: 'FR', image: 'flag-FR.PNG').save(failOnError: true)
		def langGR = Language.findByName('greek') ?: new Language(name: 'greek', code: 'GR', image: 'flag-GR.PNG').save(failOnError: true)
		def langHO = Language.findByName('dutch') ?: new Language(name: 'dutch', code: 'HO', image: 'flag-HO.PNG').save(failOnError: true)
		def langIT = Language.findByName('italian') ?: new Language(name: 'italian', code: 'IT', image: 'flag-IT.PNG').save(failOnError: true)
		def langJP = Language.findByName('japanese') ?: new Language(name: 'japanese', code: 'JP', image: 'flag-JP.PNG').save(failOnError: true)
		def langPO = Language.findByName('portuguese') ?: new Language(name: 'portuguese', code: 'PO', image: 'flag-PO.PNG').save(failOnError: true)
		def langRU = Language.findByName('russian') ?: new Language(name: 'russian', code: 'RU', image: 'flag-RU.PNG').save(failOnError: true)
		def langOTHER = Language.findByName('other') ?: new Language(name: 'other', code: 'OTHER', image: 'flag-OTHER.PNG').save(failOnError: true)
		
		log.info "Creamos los tipos de documentos..."
		def type01 = TypeDocument.findByNomenclatura('journal') ?: new TypeDocument(nombre: 'Journal', nomenclatura: 'journal').save(failOnError: true)
		def type02 = TypeDocument.findByNomenclatura('book') ?: new TypeDocument(nombre: 'Book', nomenclatura: 'book').save(failOnError: true)
		def type03 = TypeDocument.findByNomenclatura('generic') ?: new TypeDocument(nombre: 'Generic', nomenclatura: 'generic').save(failOnError: true)
		def type04 = TypeDocument.findByNomenclatura('book_section') ?: new TypeDocument(nombre: 'Book Section', nomenclatura: 'book_section').save(failOnError: true)
		def type05 = TypeDocument.findByNomenclatura('conference_proceedings') ?: new TypeDocument(nombre: 'Conference Proceedings', nomenclatura: 'conference_proceedings').save(failOnError: true)
		def type06 = TypeDocument.findByNomenclatura('working_paper') ?: new TypeDocument(nombre: 'Working Paper', nomenclatura: 'working_paper').save(failOnError: true)
		def type07 = TypeDocument.findByNomenclatura('report') ?: new TypeDocument(nombre: 'Report', nomenclatura: 'report').save(failOnError: true)
		def type08 = TypeDocument.findByNomenclatura('web_page') ?: new TypeDocument(nombre: 'Web Page', nomenclatura: 'web_page').save(failOnError: true)
		def type09 = TypeDocument.findByNomenclatura('thesis') ?: new TypeDocument(nombre: 'Thesis', nomenclatura: 'thesis').save(failOnError: true)
		def type10 = TypeDocument.findByNomenclatura('magazine_article') ?: new TypeDocument(nombre: 'Magazine Article', nomenclatura: 'magazine_article').save(failOnError: true)
		def type11 = TypeDocument.findByNomenclatura('statute') ?: new TypeDocument(nombre: 'Statute', nomenclatura: 'statute').save(failOnError: true)
		def type12 = TypeDocument.findByNomenclatura('patent') ?: new TypeDocument(nombre: 'Patent', nomenclatura: 'patent').save(failOnError: true)
		def type13 = TypeDocument.findByNomenclatura('newspaper_article') ?: new TypeDocument(nombre: 'Newspaper Article', nomenclatura: 'newspaper_article').save(failOnError: true)
		def type14 = TypeDocument.findByNomenclatura('computer_program') ?: new TypeDocument(nombre: 'Computer Program', nomenclatura: 'computer_program').save(failOnError: true)
		def type15 = TypeDocument.findByNomenclatura('hearing') ?: new TypeDocument(nombre: 'Hearing', nomenclatura: 'hearing').save(failOnError: true)
		def type16 = TypeDocument.findByNomenclatura('television_broadcast') ?: new TypeDocument(nombre: 'Television Broadcast', nomenclatura: 'television_broadcast').save(failOnError: true)
		def type17 = TypeDocument.findByNomenclatura('encyclopedia_article') ?: new TypeDocument(nombre: 'Encyclopedia Article', nomenclatura: 'encyclopedia_article').save(failOnError: true)
		def type18 = TypeDocument.findByNomenclatura('case') ?: new TypeDocument(nombre: 'Case', nomenclatura: 'case').save(failOnError: true)
		def type19 = TypeDocument.findByNomenclatura('film') ?: new TypeDocument(nombre: 'Film', nomenclatura: 'film').save(failOnError: true)
		def type20 = TypeDocument.findByNomenclatura('bill') ?: new TypeDocument(nombre: 'Bill', nomenclatura: 'bill').save(failOnError: true)
		
		log.info "Creamos Roles..."
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def superRole = Role.findByAuthority('ROLE_SUPER') ?: new Role(authority: 'ROLE_SUPER').save(failOnError: true)
		
		log.info "Creamos los engines"
		def engine01 = EngineSearch.findByName('ACM') ?: new EngineSearch(name: 'ACM', display_name: 'ACM Digital Library', image: 'acm.jpeg', text: 'ACM').save(failOnError: true)
		def engine02 = EngineSearch.findByName('IEEE') ?: new EngineSearch(name: 'IEEE', display_name: 'IEEE Computer Society', image: 'ieee.jpeg', text: 'IEEE').save(failOnError: true)
		def engine03 = EngineSearch.findByName('SCIENCE') ?: new EngineSearch(name: 'SCIENCE', display_name: 'Science Direct', image: 'science.jpeg', text: 'SCIENCE', apiKey: '80aa542193705ce36ebfe094078b9aa3').save(failOnError: true)
		def engine04 = EngineSearch.findByName('SPRINGER') ?: new EngineSearch(name: 'SPRINGER', display_name: 'Springer Link', image: 'springer.jpeg', text: 'SPRINGER', apiKey: 'c8c8ee4b2c20f0046806762317d0d6e2').save(failOnError: true)
		def engine05 = EngineSearch.findByName('OTHER') ?: new EngineSearch(name: 'OTHER', display_name: 'Other engines', image: 'other.png', text: 'OTHER').save(failOnError: true)
		
		log.info "Creamos las credenciales para Mendeley..."
		def mendeleyApi = MendeleyApi.findByClientId('1044') ?: 
			new MendeleyApi(clientId: '1044', clientSecret: 'N1krjxVLCiflzZTt', 
							redirectUri: 'http://45.76.94.16:8080/SLR/indexMendeley/', 
							totalHilos: 3, totalTries: 2).save(failOnError: true)
		
		/*def mendeleyApi = MendeleyApi.findByClientId('4347') ?:
			new MendeleyApi(clientId: '4347', clientSecret: 'XJZuvpCKISNcnLBo',
						redirectUri: 'http://localhost:8080/SLR/indexMendeley/',
						totalHilos: 3, totalTries: 2).save(failOnError: true)
		*/				
		log.info "Creamos Usuarios de prueba..."
		
		/*def profile01 = new UserProfile(
			first_name: 'Paco',
			last_name: 'Jimenez',
			display_name: 'Paco Jimenez',
			url_foto: 'http://fotos00.laopiniondemurcia.es/fotos/noticias/318x200/2011-11-02_IMG_2011-11-02_21:57:56_00902murmu.jpg')
		
		def mendProfile01 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def user01 = User.findByUsername('alpha_snake087@hotmail.com') ?: new User(
			username: 'alpha_snake087@hotmail.com',
			password: 'alpha_snake087',
			userProfile: profile01,
			userMendeley: mendProfile01).save(failOnError: true)

		if (!user01.authorities.contains(userRole)) {
			//UserRole.create user01, adminRole
			UserRole.create user01, userRole
		}*/
				
		def profile02 = new UserProfile(
			first_name: 'Monkey',
			last_name: 'D. Luffy',
			url_foto: 'http://vignette2.wikia.nocookie.net/onepiece/images/6/61/Estatua_de_cera_de_Luffy.png/revision/latest?cb=20121231203632&path-prefix=es')
		
		def mendProfile02 = new UserMendeley(
			email_mend: 'angel.gonzatoro@gmail.com',
			pass_mend: 'angel.gonzatoro',
			access_token: 'sad76sa7d7sa8d6',
			token_type: 'sad908sd980vfsd',
			expires_in: '3600',
			refresh_token: 'sdasñdusd9aod'
			)

		def user02 = User.findByUsername('angel.gonzatoro@gmail.com') ?: new User(
			username: 'angel.gonzatoro@gmail.com',
			password: 'angel.gonzatoro',
			userProfile: profile02,
			userMendeley: mendProfile02).save(failOnError: true)

		if (!user02.authorities.contains(userRole)) {
			//UserRole.create user02, userRole
			UserRole.create user02, superRole
		}
		user02.save(failOnError: true)
		
		log.info "FIN BOOTSRAP"
    }
    def destroy = {
    }
}
