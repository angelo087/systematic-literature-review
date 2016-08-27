import java.util.List;
import java.util.Date;

import es.uca.pfc.Author
import es.uca.pfc.AuthorReference
import es.uca.pfc.Book
import es.uca.pfc.Criterion
import es.uca.pfc.Education
import es.uca.pfc.EngineSearch
import es.uca.pfc.FAQ
import es.uca.pfc.Language
import es.uca.pfc.Logger
import es.uca.pfc.LoggerFriend
import es.uca.pfc.LoggerSlr
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
		
		println "Creamos los componentes de busquedas..."
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
		
		println "Creamos los operators de busquedas..."
		def opALL = SearchOperator.findByValue('all') ?: new SearchOperator(name: 'ALL', value: 'all').save(failOnError: true)
		def opANY  = SearchOperator.findByValue('any') ?: new SearchOperator(name: 'ANY', value: 'any').save(failOnError: true)
		def opNONE = SearchOperator.findByValue('none') ?: new SearchOperator(name: 'NONE', value: 'none').save(failOnError: true)
		
		println "Creamos los idiomas..."
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
		
		println "Creamos los tipos de documentos..."
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
		def type16 = TypeDocument.findByNomenclatura('television_boradcast') ?: new TypeDocument(nombre: 'Television Broadcast', nomenclatura: 'television_broadcast').save(failOnError: true)
		def type17 = TypeDocument.findByNomenclatura('encyclopedia_article') ?: new TypeDocument(nombre: 'Encyclopedia Article', nomenclatura: 'encyclopedia_article').save(failOnError: true)
		def type18 = TypeDocument.findByNomenclatura('case') ?: new TypeDocument(nombre: 'Case', nomenclatura: 'case').save(failOnError: true)
		def type19 = TypeDocument.findByNomenclatura('film') ?: new TypeDocument(nombre: 'Film', nomenclatura: 'film').save(failOnError: true)
		def type20 = TypeDocument.findByNomenclatura('bill') ?: new TypeDocument(nombre: 'Bill', nomenclatura: 'bill').save(failOnError: true)
		
		println "Creamos Roles..."
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def superRole = Role.findByAuthority('ROLE_SUPER') ?: new Role(authority: 'ROLE_SUPER').save(failOnError: true)
		
		println "Creamos los engines"
		def engine01 = EngineSearch.findByName('ACM') ?: new EngineSearch(name: 'ACM', display_name: 'ACM Digital Library', image: 'acm.jpeg', text: 'ACM').save(failOnError: true)
		def engine02 = EngineSearch.findByName('IEEE') ?: new EngineSearch(name: 'IEEE', display_name: 'IEEE Computer Society', image: 'ieee.jpeg', text: 'IEEE').save(failOnError: true)
		def engine03 = EngineSearch.findByName('SCIENCE') ?: new EngineSearch(name: 'SCIENCE', display_name: 'Science Direct', image: 'science.jpeg', text: 'SCIENCE').save(failOnError: true)
		def engine04 = EngineSearch.findByName('SPRINGER') ?: new EngineSearch(name: 'SPRINGER', display_name: 'Springer Link', image: 'springer.jpeg', text: 'SPRINGER').save(failOnError: true)
		
		println "Creamos Usuarios de prueba..."
		
		def profile01 = new UserProfile(
			first_name: 'Francisco',
			last_name: 'Jimenez',
			display_name: 'Francisco Jimenez',
			url_foto: 'http://fotos00.laopiniondemurcia.es/fotos/noticias/318x200/2011-11-02_IMG_2011-11-02_21:57:56_00902murmu.jpg')
		
		def mendProfile01 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def user01 = User.findByUsername('pacoji@pacoji.com') ?: new User(
			username: 'pacoji@pacoji.com',
			password: 'pacoji',
			userProfile: profile01,
			userMendeley: mendProfile01).save(failOnError: true)

		if (!user01.authorities.contains(userRole)) {
			UserRole.create user01, userRole
		}
		
		def profile02 = new UserProfile(
			first_name: 'Morgan',
			last_name: 'Freeman',
			url_foto: 'http://www.moviepilot.de/files/images/0486/8182/Morgan_Freeman.jpg')
		
		def mendProfile02 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def user02 = User.findByUsername('morgan@freeman.com') ?: new User(
			username: 'morgan@freeman.com',
			password: 'morgan',
			userProfile: profile02,
			userMendeley: mendProfile02).save(failOnError: true)

		if (!user02.authorities.contains(userRole)) {
			UserRole.create user02, userRole
		}
		
		def profile03 = new UserProfile(
			first_name: 'Abraham',
			last_name: 'Lincoln',
			url_foto: 'http://www.biografiasyvidas.com/biografia/l/fotos/lincoln_abraham_2.jpg')
		
		def mendProfile03 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)
		
		def user03 = User.findByUsername('abraham@lincoln.com') ?: new User(
			username: 'abraham@lincoln.com',
			password: 'abraham',
			userProfile: profile03,
			userMendeley: mendProfile03).save(failOnError: true)

		if (!user03.authorities.contains(userRole)) {
			UserRole.create user03, userRole
		}
		
		def profile04 = new UserProfile(
			first_name: 'Susana',
			last_name: 'Guash',
			url_foto: 'http://www.telebasura.net/wp-content/2013/11/susana-guasch.jpg')

		def mendProfile04 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)

		def user04 = User.findByUsername('susana@guash.com') ?: new User(
			username: 'susana@guash.com',
			password: 'susana',
			userProfile: profile04,
			userMendeley: mendProfile04).save(failOnError: true)
			
		if (!user04.authorities.contains(userRole)) {
			UserRole.create user04, userRole
		}
		
		def profile05 = new UserProfile(
			first_name: 'Bruce',
			last_name: 'Wayne',
			url_foto: 'http://vignette3.wikia.nocookie.net/batman/images/4/49/1929848-bruce_wayne.jpg/revision/latest?cb=20140625152104&path-prefix=es')
		
		def mendProfile05 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)

		def user05 = User.findByUsername('bruce@wayne.com') ?: new User(
			username: 'bruce@wayne.com',
			password: 'bruce',
			userProfile: profile05,
			userMendeley: mendProfile05).save(failOnError: true)

		if (!user05.authorities.contains(userRole)) {
			UserRole.create user05, userRole
		}
		
		def profile06 = new UserProfile(
			first_name: 'Jack',
			last_name: 'Sparrow',
			url_foto: 'http://www.electric949.com/wp-content/uploads/2015/07/Captain-Jack-captain-jack-sparrow-14117613-1242-900.jpg')
		
		def mendProfile06 = new UserMendeley(
			email_mend: 'email',
			pass_mend: 'pass',
			access_token: 'accesstoken',
			token_type: 'tokentype',
			expires_in: 'expiresin',
			refresh_token: 'refreshtoken'
			)

		def user06 = User.findByUsername('jack@sparrow.com') ?: new User(
			username: 'jack@sparrow.com',
			password: 'jack',
			userProfile: profile06,
			userMendeley: mendProfile06).save(failOnError: true)
			
		def profile07 = new UserProfile(
			first_name: 'Monkey',
			last_name: 'D. Luffy',
			url_foto: 'http://vignette2.wikia.nocookie.net/onepiece/images/6/61/Estatua_de_cera_de_Luffy.png/revision/latest?cb=20121231203632&path-prefix=es')
		
		def mendProfile07 = new UserMendeley(
			email_mend: 'angel.gonzatoro@gmail.com',
			pass_mend: 'angel.gonzatoro',
			access_token: 'sad76sa7d7sa8d6',
			token_type: 'sad908sd980vfsd',
			expires_in: '3600',
			refresh_token: 'sdasñdusd9aod'
			)

		def user07 = User.findByUsername('angel.gonzatoro@gmail.com') ?: new User(
			username: 'angel.gonzatoro@gmail.com',
			password: 'angel.gonzatoro',
			userProfile: profile07,
			userMendeley: mendProfile07).save(failOnError: true)

		if (!user07.authorities.contains(userRole)) {
			UserRole.create user07, userRole
		}
		
		println "Creamos las Estudios..."
		user01.userProfile.addToEducations(new Education(degree: 'Degree 1', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 2', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 3', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 4', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 5', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 6', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 7', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 8', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 9', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))		
		
		println "Creamos las notificaciones..."
		//user01.userProfile.addToNotifications(new Notification(asunto: "Hola", texto: "hola", tipo: "search"))
		
		user01.save(failOnError: true)
				
		println "Creamos las amistades"
		user01.userProfile.addToFriends(profile03)
		user03.userProfile.addToFriends(profile01)
		user02.userProfile.addToFriends(profile04)
		user04.userProfile.addToFriends(profile02)
		
		user01.userProfile.addToLoggers(new LoggerFriend(friendProfile: user03.userProfile, tipo: 'seguir')).save(failOnError: true)
		user04.userProfile.addToLoggers(new LoggerFriend(friendProfile: user02.userProfile, tipo: 'seguir')).save(failOnError: true)
		user02.userProfile.addToLoggers(new LoggerFriend(friendProfile: user04.userProfile, tipo: 'seguir')).save(failOnError: true)
		user03.userProfile.addToLoggers(new LoggerFriend(friendProfile: user01.userProfile, tipo: 'seguir')).save(failOnError: true)
		
		/*user01.userProfile.addToFriends(profile02)
		user01.userProfile.addToFriends(profile03)
		user01.userProfile.addToFriends(profile04)
		user01.userProfile.addToFriends(profile05)

		user02.userProfile.addToFriends(profile01)
		user03.userProfile.addToFriends(profile01)
		user04.userProfile.addToFriends(profile01)
		user05.userProfile.addToFriends(profile01)
		
		user01.userProfile.addToLoggers(new LoggerFriend(friendProfile: user02.userProfile, tipo: 'seguir')).save(failOnError: true)
		user01.userProfile.addToLoggers(new LoggerFriend(friendProfile: user03.userProfile, tipo: 'seguir')).save(failOnError: true)
		user01.userProfile.addToLoggers(new LoggerFriend(friendProfile: user04.userProfile, tipo: 'seguir')).save(failOnError: true)
		user01.userProfile.addToLoggers(new LoggerFriend(friendProfile: user05.userProfile, tipo: 'seguir')).save(failOnError: true)
		
		user02.userProfile.addToLoggers(new LoggerFriend(friendProfile: user01.userProfile, tipo: 'seguir')).save(failOnError: true)
		user03.userProfile.addToLoggers(new LoggerFriend(friendProfile: user01.userProfile, tipo: 'seguir')).save(failOnError: true)
		user04.userProfile.addToLoggers(new LoggerFriend(friendProfile: user01.userProfile, tipo: 'seguir')).save(failOnError: true)
		user05.userProfile.addToLoggers(new LoggerFriend(friendProfile: user01.userProfile, tipo: 'seguir')).save(failOnError: true)*/
		
		println "Creamos los SLR..."
		Slr slr01 = new Slr(title: 'Titulo 1', justification: 'Justificacion 1', userProfile: user07.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr02 = new Slr(title: 'Titulo 2', justification: 'Justificacion 2', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr03 = new Slr(title: 'Titulo 3', justification: 'Justificacion 3', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr04 = new Slr(title: 'Titulo 4', justification: 'Justificacion 4', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr05 = new Slr(title: 'Titulo 5', justification: 'Justificacion 5', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr06 = new Slr(title: 'Titulo 6', justification: 'Justificacion 5', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr07 = new Slr(title: 'Titulo 7', justification: 'Justificacion 7', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr08 = new Slr(title: 'Titulo 8', justification: 'Justificacion 8', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr09 = new Slr(title: 'Titulo 9', justification: 'Justificacion 9', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr10 = new Slr(title: 'Titulo 10', justification: 'Justificacion 10', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr11 = new Slr(title: 'Titulo 11', justification: 'Justificacion 11', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr12 = new Slr(title: 'Titulo 12', justification: 'Justificacion 12', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr13 = new Slr(title: 'Titulo 13', justification: 'Justificacion 13', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr14 = new Slr(title: 'Titulo 14', justification: 'Justificacion 14', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr15 = new Slr(title: 'Titulo 15', justification: 'Justificacion 15', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr16 = new Slr(title: 'Titulo 16', justification: 'Justificacion 16', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr17 = new Slr(title: 'Titulo 17', justification: 'Justificacion 17', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr18 = new Slr(title: 'Titulo 18', justification: 'Justificacion 18', userProfile: user01.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr19 = new Slr(title: 'Titulo 19', justification: 'Justificacion 19', userProfile: user02.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
		Slr slr20 = new Slr(title: 'Titulo 20', justification: 'Justificacion 20', userProfile: user07.userProfile, idmend: 'asdsad').save(failOnError: true, flush: true)
				
		println "Insertamos los criterios..."
		def criterion02 = new Criterion(name: 'language', description: 'Language diferent to english', nomenclatura: 'cr_language')
		def criterion03 = new Criterion(name: 'version', description: 'Version diferent to 1.3', nomenclatura: 'cr_version')
		def criterion04 = new Criterion(name: 'metamodel', description: 'Metamodel no present', nomenclatura: 'cr_metamodel')
		
		slr01.addToCriterions(criterion02)
		slr01.addToCriterions(criterion03)
		slr01.addToCriterions(criterion04)
		
		println "Insertamos los atributos especificos..."
		def specAttribute01 = new SpecificAttribute(name: "specAttribute01", tipo: "number")
		def specAttribute02 = new SpecificAttribute(name: "specAttribute02", tipo: "string")
		def specAttribute03 = new SpecificAttributeMultipleValue(name: "specAttribute03", tipo: "list", options: ['uno','dos','tres'], optionDefault: 'uno')

		slr01.addToSpecAttributes(specAttribute01)
		slr01.addToSpecAttributes(specAttribute02)
		slr01.addToSpecAttributes(specAttribute03)
		
		println "Insertamos las preguntas de investigacion...."
		def question01 = new ResearchQuestion(enunciado: '�Pregunta 1?')
		def question02 = new ResearchQuestion(enunciado: '�Pregunta 2?')
		def question03 = new ResearchQuestion(enunciado: '�Pregunta 3?')
		
		slr01.addToQuestions(question01)
		slr01.addToQuestions(question02)
		slr01.addToQuestions(question03)
		
		println "Creamos las busquedas..."
		def terms01 = new SearchTermParam(terminos: "terminos 1", component: comp01, operator: opALL)
		def terms02 = new SearchTermParam(terminos: "terminos 2", component: comp02, operator: opANY)
		def terms03 = new SearchTermParam(terminos: "terminos 3", component: comp03, operator: opNONE)
		def terms04 = new SearchTermParam(terminos: "terminos 4", component: comp04, operator: opALL)
		def terms05 = new SearchTermParam(terminos: "terminos 5", component: comp05, operator: opALL)
		def terms06 = new SearchTermParam(terminos: "terminos 6", component: comp06, operator: opALL)
		def terms07 = new SearchTermParam(terminos: "terminos 7", component: comp07, operator: opALL)
		def terms08 = new SearchTermParam(terminos: "terminos 8", component: comp08, operator: opALL)
		def terms09 = new SearchTermParam(terminos: "terminos 9", component: comp09, operator: opANY)
		def terms10 = new SearchTermParam(terminos: "terminos 10", component: comp10, operator: opANY)
		def terms11 = new SearchTermParam(terminos: "terminos 11", component: comp01, operator: opANY)
		def terms12 = new SearchTermParam(terminos: "terminos 12", component: comp02, operator: opALL)
		def terms13 = new SearchTermParam(terminos: "terminos 13", component: comp03, operator: opALL)
		def terms14 = new SearchTermParam(terminos: "terminos 14", component: comp04, operator: opNONE)
		def terms15 = new SearchTermParam(terminos: "terminos 15", component: comp05, operator: opALL)
		def terms16 = new SearchTermParam(terminos: "terminos 16", component: comp06, operator: opALL)
		def terms17 = new SearchTermParam(terminos: "terminos 17", component: comp07, operator: opNONE)
		def terms18 = new SearchTermParam(terminos: "terminos 18", component: comp08, operator: opALL)
		def terms19 = new SearchTermParam(terminos: "terminos 19", component: comp09, operator: opALL)
		def terms20 = new SearchTermParam(terminos: "terminos 20", component: comp10, operator: opANY)
		def terms21 = new SearchTermParam(terminos: "terminos 21", component: comp01, operator: opANY)
		def terms22 = new SearchTermParam(terminos: "terminos 22", component: comp02, operator: opNONE)
		def terms23 = new SearchTermParam(terminos: "terminos 23", component: comp03, operator: opALL)
		def terms24 = new SearchTermParam(terminos: "terminos 24", component: comp04, operator: opALL)
		def terms25 = new SearchTermParam(terminos: "terminos 25", component: comp05, operator: opANY)
		def terms26 = new SearchTermParam(terminos: "terminos 26", component: comp06, operator: opALL)
		def terms27 = new SearchTermParam(terminos: "terminos 27", component: comp07, operator: opALL)
		def terms28 = new SearchTermParam(terminos: "terminos 28", component: comp08, operator: opNONE)
		def terms29 = new SearchTermParam(terminos: "terminos 29", component: comp09, operator: opALL)
		def terms30 = new SearchTermParam(terminos: "terminos 30", component: comp10, operator: opALL)
				
		def search01 = new Search(startYear: "2010", endYear: "2012", maxTotal: 5)
		def search02 = new Search(startYear: "2010", endYear: "2012", maxTotal: 10)
		def search03 = new Search(startYear: "2010", endYear: "2012", maxTotal: 10)
		def search04 = new Search(startYear: "2008", endYear: "2012", maxTotal: 10)
		def search05 = new Search(startYear: "2008", endYear: "2012", maxTotal: 10)
		def search06 = new Search(startYear: "2007", endYear: "2012", maxTotal: 20)
		def search07 = new Search(startYear: "2012", endYear: "2013", maxTotal: 10)
		def search08 = new Search(startYear: "2012", endYear: "2013", maxTotal: 50)
		def search09 = new Search(startYear: "2013", endYear: "2015", maxTotal: 10)
		def search10 = new Search(startYear: "2012", endYear: "2014", maxTotal: 10)
		def search11 = new Search(startYear: "2011", endYear: "2013", maxTotal: 10)
		def search12 = new Search(startYear: "2011", endYear: "2015", maxTotal: 30)
		def search13 = new Search(startYear: "2010", endYear: "2012", maxTotal: 10)
		def search14 = new Search(startYear: "2009", endYear: "2011", maxTotal: 8)
		def search15 = new Search(startYear: "2009", endYear: "2011", maxTotal: 8)
		
		search01.addToEngines(engine01); search01.addToTermParams(terms01)
		search01.addToEngines(engine04); search01.addToTermParams(terms02)
		search02.addToEngines(engine02); search02.addToTermParams(terms03)
		search02.addToEngines(engine03); search02.addToTermParams(terms04)
		search03.addToEngines(engine03); search03.addToTermParams(terms05)
		search03.addToEngines(engine02); search03.addToTermParams(terms06)
		search04.addToEngines(engine01); search04.addToTermParams(terms07)
		search04.addToEngines(engine01); search04.addToTermParams(terms08)
		search05.addToEngines(engine02); search05.addToTermParams(terms09)
		search05.addToEngines(engine04); search05.addToTermParams(terms10)
		search06.addToEngines(engine03); search06.addToTermParams(terms11)
		search06.addToEngines(engine04); search06.addToTermParams(terms12)
		search07.addToEngines(engine01); search07.addToTermParams(terms13)
		search07.addToEngines(engine02); search07.addToTermParams(terms14)
		search08.addToEngines(engine01); search08.addToTermParams(terms15)
		search08.addToEngines(engine03); search08.addToTermParams(terms16)
		search09.addToEngines(engine03); search09.addToTermParams(terms17)
		search09.addToEngines(engine04); search09.addToTermParams(terms18)
		search10.addToEngines(engine01); search10.addToTermParams(terms19)
		search10.addToEngines(engine02); search10.addToTermParams(terms20)
		search11.addToEngines(engine02); search11.addToTermParams(terms21)
		search11.addToEngines(engine01); search11.addToTermParams(terms22)
		search12.addToEngines(engine02); search12.addToTermParams(terms23)
		search12.addToEngines(engine04); search12.addToTermParams(terms24)
		search13.addToEngines(engine01); search13.addToTermParams(terms25)
		search13.addToEngines(engine03); search13.addToTermParams(terms26)
		search14.addToEngines(engine02); search14.addToTermParams(terms27)
		search14.addToEngines(engine01); search14.addToTermParams(terms28)
		search15.addToEngines(engine01); search15.addToTermParams(terms29)
		search15.addToEngines(engine02); search15.addToTermParams(terms30)
		search15.addToEngines(engine03); 
	
		println "Creamos las referencias..."
		def reference01 = new Reference(idmend : 'mend01', engine: engine01, title : 'Reference 1', type : type01, docAbstract : 'Abstract 1', source : 'Source 1', year : '1987',    keywords: ["key1", "key2", "key3"], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1', 'tag2', 'tag3'], citation_key : 'citationkey1', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 1')
		def reference02 = new Reference(idmend : 'mend02', engine: engine02, title : 'Reference 2', type : type02, docAbstract : 'Abstract 2', source : 'Source 2', year : '1987',    keywords: ['key5', 'key3', 'key2'], pages : '1', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Madrid', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag2', 'tag3'], citation_key : 'citationkey2', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 2')
		def reference03 = new Reference(idmend : 'mend03', engine: engine03, title : 'Reference 3', type : type03, docAbstract : 'Abstract 3', source : 'Source 3', year : '1987',    keywords: ['key5', 'key2', 'key3'], pages : '2', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Teruel', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag3'], citation_key : 'citationkey3', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 3')
		def reference04 = new Reference(idmend : 'mend04', engine: engine04, title : 'Reference 4', type : type04, docAbstract : 'Abstract 4', source : 'Source 4', year : '1987',    keywords: ['key6', 'key2', 'key3'], pages : '3', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi4', city : 'Avila', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey4', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 4')
		def reference05 = new Reference(idmend : 'mend05', engine: engine01, title : 'Reference 5', type : type05, docAbstract : 'Abstract 5', source : 'Source 5', year : '1987',    keywords: ['key1', 'key2', 'key3'], pages : '4', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi5', city : 'Zaragoza', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey5', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'EEUU', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 5')
		def reference06 = new Reference(idmend : 'mend06', engine: engine02, title : 'Reference 6', type : type01, docAbstract : 'Abstract 6', source : 'Source 6', year : '1987',    keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi6', city : 'Badajoz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey6', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'EEUU', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 6')
		def reference07 = new Reference(idmend : 'mend07', engine: engine03, title : 'Reference 7', type : type01, docAbstract : 'Abstract 7', source : 'Source 7', year : '1989',    keywords: ['key1', 'key6', 'key7'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Barcelona', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey7', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'EEUU', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 7')
		def reference08 = new Reference(idmend : 'mend08', engine: engine04, title : 'Reference 8', type : type02, docAbstract : 'Abstract 8', source : 'Source 8', year : '1988',    keywords: ['key4', 'key2', 'key5'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Madrid', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey8', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'EEUU', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 8')
		def reference09 = new Reference(idmend : 'mend09', engine: engine01, title : 'Reference 9', type : type03, docAbstract : 'Abstract 9', source : 'Source 9', year : '2000',    keywords: ['key10', 'key3', 'key5'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Cadiz', institution : 'Institution 3', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey9', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'EEUU', department : 'Literature', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 9')
		def reference10 = new Reference(idmend : 'mend10', engine: engine02, title : 'Reference 10', type : type04, docAbstract : 'Abstract 10', source : 'Source 10', year : '2000', keywords: ['key4', 'key2', 'key7'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi1', city : 'Cadiz', institution : 'Institution 3', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey10', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'Spain', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 10')
		def reference11 = new Reference(idmend : 'mend11', engine: engine03, title : 'Reference 11', type : type05, docAbstract : 'Abstract 11', source : 'Source 11', year : '2001', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Cadiz', institution : 'Institution 4', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey11', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'Spain', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 11')
		def reference12 = new Reference(idmend : 'mend12', engine: engine04, title : 'Reference 12', type : type05, docAbstract : 'Abstract 12', source : 'Source 12', year : '2002', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Cadiz', institution : 'Institution 4', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey12', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'Spain', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 12')
		def reference13 = new Reference(idmend : 'mend13', engine: engine01, title : 'Reference 13', type : type02, docAbstract : 'Abstract 13', source : 'Source 13', year : '2001', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Vigo', institution : 'Institution 4', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey13', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'Spain', department : 'Math', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 13')
		def reference14 = new Reference(idmend : 'mend14', engine: engine02, title : 'Reference 14', type : type02, docAbstract : 'Abstract 14', source : 'Source 14', year : '1999', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Salamanca', institution : 'Institution 4', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey14', source_type : 'Source Type 1', language : langES, genre : 'Genre 1', country : 'Spain', department : 'Math', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 14')
		def reference15 = new Reference(idmend : 'mend15', engine: engine03, title : 'Reference 15', type : type02, docAbstract : 'Abstract 15', source : 'Source 15', year : '1998', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Cadiz', institution : 'Institution 5', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey15', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'Spain', department : 'Math', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 15')
		def reference16 = new Reference(idmend : 'mend16', engine: engine04, title : 'Reference 16', type : type01, docAbstract : 'Abstract 16', source : 'Source 16', year : '2012', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi2', city : 'Cadiz', institution : 'Institution 5', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey16', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Math', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 16')
		def reference17 = new Reference(idmend : 'mend17', engine: engine01, title : 'Reference 17', type : type03, docAbstract : 'Abstract 17', source : 'Source 17', year : '2000', keywords: ['key5', 'key2', 'key6'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 5', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey17', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Math', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 17')
		def reference18 = new Reference(idmend : 'mend18', engine: engine02, title : 'Reference 18', type : type03, docAbstract : 'Abstract 18', source : 'Source 18', year : '2004', keywords: ['key1', 'key2', 'key4'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 5', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey18', source_type : 'Source Type 1', language : langIT, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 18')
		def reference19 = new Reference(idmend : 'mend19', engine: engine03, title : 'Reference 19', type : type04, docAbstract : 'Abstract 19', source : 'Source 19', year : '1999', keywords: ['key1', 'key4', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey19', source_type : 'Source Type 1', language : langIT, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 19')
		def reference20 = new Reference(idmend : 'mend20', engine: engine04, title : 'Reference 20', type : type05, docAbstract : 'Abstract 20', source : 'Source 20', year : '2000', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey20', source_type : 'Source Type 1', language : langIT, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 20')
		def reference21 = new Reference(idmend : 'mend21', engine: engine01, title : 'Reference 21', type : type06, docAbstract : 'Abstract 21', source : 'Source 21', year : '2002', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey21', source_type : 'Source Type 1', language : langIT, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 21')
		def reference22 = new Reference(idmend : 'mend22', engine: engine02, title : 'Reference 22', type : type02, docAbstract : 'Abstract 22', source : 'Source 22', year : '2014', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi3', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey22', source_type : 'Source Type 1', language : langIT, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 22')
		def reference23 = new Reference(idmend : 'mend23', engine: engine03, title : 'Reference 23', type : type02, docAbstract : 'Abstract 23', source : 'Source 23', year : '2003', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi4', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey23', source_type : 'Source Type 1', language : langPO, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 23')
		def reference24 = new Reference(idmend : 'mend24', engine: engine04, title : 'Reference 24', type : type06, docAbstract : 'Abstract 24', source : 'Source 24', year : '1995', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi4', city : 'Cadiz', institution : 'Institution 1', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey24', source_type : 'Source Type 1', language : langPO, genre : 'Genre 1', country : 'Italy', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 24')
		def reference25 = new Reference(idmend : 'mend25', engine: engine01, title : 'Reference 25', type : type08, docAbstract : 'Abstract 25', source : 'Source 25', year : '1994', keywords: ['key1', 'key2', 'key3'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi4', city : 'Cadiz', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey25', source_type : 'Source Type 1', language : langPO, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 25')
		def reference26 = new Reference(idmend : 'mend26', engine: engine02, title : 'Reference 26', type : type04, docAbstract : 'Abstract 26', source : 'Source 26', year : '1987', keywords: ['key1', 'key4', 'key6'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi4', city : 'Cadiz', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag1'], citation_key : 'citationkey26', source_type : 'Source Type 1', language : langPO, genre : 'Genre 1', country : 'France', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 26')
		def reference27 = new Reference(idmend : 'mend27', engine: engine02, title : 'Reference 27', type : type02, docAbstract : 'Abstract 27', source : 'Source 27', year : '1989', keywords: ['key1', 'key2', 'key7'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi5', city : 'Cadiz', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag1', 'tag2', 'tag3'], citation_key : 'citationkey27', source_type : 'Source Type 1', language : langPO, genre : 'Genre 1', country : 'France', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 27')
		def reference28 = new Reference(idmend : 'mend28', engine: engine02, title : 'Reference 28', type : type02, docAbstract : 'Abstract 28', source : 'Source 28', year : '1990', keywords: ['key1', 'key2', 'key8'], pages : '12', volume : '2', issue : '1', websites: ['web1', 'web2'], publisher : 'publi5', city : 'Cadiz', institution : 'Institution 2', series : 'Series 1', chapter : '1', tags: ['tag1', 'tag2', 'tag3'], citation_key : 'citationkey28', source_type : 'Source Type 1', language : langEN, genre : 'Genre 1', country : 'England', department : 'Science', arxiv : 'a1', doi : 'd1', isbn : 'isb1', issn : 'issn1', pmid : 'p1', scopus : 's1', notes : 'n1', month : 'January', day : '1', bibtex: 'Bibtex 28')
		
		println "Insertamos las referencias en las busquedas..."
		search01.addToReferences(reference01)
		search01.addToReferences(reference04)
		search02.addToReferences(reference02)
		search02.addToReferences(reference03)
		search03.addToReferences(reference06)
		search03.addToReferences(reference07)
		search04.addToReferences(reference05)
		search04.addToReferences(reference08)
		search05.addToReferences(reference10)
		search05.addToReferences(reference12)
		search06.addToReferences(reference15)
		search06.addToReferences(reference16)
		search07.addToReferences(reference13)
		search07.addToReferences(reference14)
		search08.addToReferences(reference09)
		search08.addToReferences(reference11)
		search09.addToReferences(reference19)
		search09.addToReferences(reference20)
		search10.addToReferences(reference18)
		search10.addToReferences(reference21)
		search11.addToReferences(reference17)
		search11.addToReferences(reference22)
		search12.addToReferences(reference24)
		search12.addToReferences(reference26)
		search13.addToReferences(reference23)
		search13.addToReferences(reference27)
		search14.addToReferences(reference25)
		search14.addToReferences(reference28)
		
		println "Insertamos las busquedas en los slr's"
		slr01.addToSearchs(search01)
		slr01.addToSearchs(search02)
		slr01.addToSearchs(search03)
		slr01.addToSearchs(search04)
		slr01.addToSearchs(search05)
		slr01.addToSearchs(search06)
		slr01.addToSearchs(search07)
		slr01.addToSearchs(search08)
		slr01.addToSearchs(search09)
		slr01.addToSearchs(search10)
		slr01.addToSearchs(search11)
		slr01.addToSearchs(search12)
		slr01.addToSearchs(search13)
		slr01.addToSearchs(search14)
		slr19.addToSearchs(search15)
		
		user01.userProfile.addToLoggers(new LoggerSlr(slr: slr01, isSearch: true, tipo: 'buscar')).save(failOnError: true)
		user02.userProfile.addToLoggers(new LoggerSlr(slr: slr19, isSearch: true, tipo: 'buscar')).save(failOnError: true)
		
		println "Creamos autores para las referencias..."
		def author01 = new Author(forename: 'Angel', surname: 'Gonzalez').save(failOnError: true)
		def author02 = new Author(forename: 'Aradia', surname: 'Rocha').save(failOnError: true)
		def author03 = new Author(forename: 'Will', surname: 'Smith').save(failOnError: true)
		def author04 = new Author(forename: 'Natalia', surname: 'Verbeke').save(failOnError: true)
		
		println "Establecemos relacion entre autores y referencias"
		author01.addToAuthorsRefs(reference: reference01).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference03).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference05).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference08).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference15).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference19).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference23).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference24).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference25).save(failOnError: true)
		author01.addToAuthorsRefs(reference: reference26).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference01).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference02).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference04).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference07).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference09).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference16).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference20).save(failOnError: true)
		author02.addToAuthorsRefs(reference: reference27).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference02).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference03).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference06).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference10).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference17).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference21).save(failOnError: true)
		author03.addToAuthorsRefs(reference: reference28).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference04).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference05).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference11).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference12).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference13).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference14).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference18).save(failOnError: true)
		author04.addToAuthorsRefs(reference: reference22).save(failOnError: true)
		
		println "Creamos FAQ's"
		def faq01 = new FAQ(enunciado: '�Esto es una pregunta 1?', respuesta: 'Efectivamente, esto es una respuesta 1').save(failOnError: true)
		def faq02 = new FAQ(enunciado: '�Esto es una pregunta 2?', respuesta: 'Efectivamente, esto es una respuesta 2').save(failOnError: true)
		def faq03 = new FAQ(enunciado: '�Esto es una pregunta 3?', respuesta: 'Efectivamente, esto es una respuesta 3').save(failOnError: true)
																   
		def book01 = new Book(name: 'Titulo 1').save(failOnError: true)
		def book02 = new Book(name: 'Titulo 2').save(failOnError: true)
		def book03 = new Book(name: 'Titulo 3').save(failOnError: true)
		def book04 = new Book(name: 'Titulo 4').save(failOnError: true)
		def book05 = new Book(name: 'Titulo 5').save(failOnError: true)
		def book06 = new Book(name: 'Titulo 6').save(failOnError: true)
		def book07 = new Book(name: 'Titulo 7').save(failOnError: true)
		def book08 = new Book(name: 'Titulo 8').save(failOnError: true)
		def book09 = new Book(name: 'Titulo 9').save(failOnError: true)
		def book10 = new Book(name: 'Titulo 10').save(failOnError: true)
		
    }
    def destroy = {
    }
}
