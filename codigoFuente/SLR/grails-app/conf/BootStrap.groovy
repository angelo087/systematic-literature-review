import java.util.Date;

import es.uca.pfc.Author
import es.uca.pfc.Education
import es.uca.pfc.EngineSearch
import es.uca.pfc.Notification
import es.uca.pfc.Role
import es.uca.pfc.Search
import es.uca.pfc.Slr
import es.uca.pfc.User
import es.uca.pfc.UserRole
import es.uca.pfc.UserProfile

class BootStrap {

    def init = { servletContext ->
		
		println "Creamos Roles..."
		def userRole =  Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
		def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def superRole = Role.findByAuthority('ROLE_SUPER') ?: new Role(authority: 'ROLE_SUPER').save(failOnError: true)
		
		println "Creamos Usuarios de prueba..."
		
		def profile01 = new UserProfile(
			first_name: 'Francisco',
			last_name: 'Jimenez',
			display_name: 'Francisco Jimenez',
			url_foto: 'http://fotos00.laopiniondemurcia.es/fotos/noticias/318x200/2011-11-02_IMG_2011-11-02_21:57:56_00902murmu.jpg')
		
		def user01 = User.findByUsername('pacoji@pacoji.com') ?: new User(
			username: 'pacoji@pacoji.com',
			password: 'pacoji',
			userProfile: profile01).save(failOnError: true)

		if (!user01.authorities.contains(userRole)) {
			UserRole.create user01, userRole
		}
		
		def profile02 = new UserProfile(
			first_name: 'Morgan',
			last_name: 'Freeman',
			url_foto: 'http://www.moviepilot.de/files/images/0486/8182/Morgan_Freeman.jpg')
		
		def user02 = User.findByUsername('morgan@freeman.com') ?: new User(
			username: 'morgan@freeman.com',
			password: 'morgan',
			userProfile: profile02).save(failOnError: true)

		if (!user02.authorities.contains(userRole)) {
			UserRole.create user02, userRole
		}
		
		def profile03 = new UserProfile(
			first_name: 'Abraham',
			last_name: 'Lincoln',
			url_foto: 'http://www.biografiasyvidas.com/biografia/l/fotos/lincoln_abraham_2.jpg')
		
		def user03 = User.findByUsername('abraham@lincoln.com') ?: new User(
			username: 'abraham@lincoln.com',
			password: 'abraham',
			userProfile: profile03).save(failOnError: true)

		if (!user03.authorities.contains(userRole)) {
			UserRole.create user03, userRole
		}
		
		def profile04 = new UserProfile(
			first_name: 'Susana',
			last_name: 'Guash',
			url_foto: 'http://www.telebasura.net/wp-content/2013/11/susana-guasch.jpg')
		
		def user04 = User.findByUsername('susana@guash.com') ?: new User(
			username: 'susana@guash.com',
			password: 'susana',
			userProfile: profile04).save(failOnError: true)

		if (!user04.authorities.contains(userRole)) {
			UserRole.create user04, userRole
		}
		
		def profile05 = new UserProfile(
			first_name: 'Bruce',
			last_name: 'Wayne',
			url_foto: 'http://vignette3.wikia.nocookie.net/batman/images/4/49/1929848-bruce_wayne.jpg/revision/latest?cb=20140625152104&path-prefix=es')
		
		def user05 = User.findByUsername('bruce@wayne.com') ?: new User(
			username: 'bruce@wayne.com',
			password: 'bruce',
			userProfile: profile05).save(failOnError: true)

		if (!user05.authorities.contains(userRole)) {
			UserRole.create user05, userRole
		}
		
		def profile06 = new UserProfile(
			first_name: 'Jack',
			last_name: 'Sparrow',
			url_foto: 'http://www.electric949.com/wp-content/uploads/2015/07/Captain-Jack-captain-jack-sparrow-14117613-1242-900.jpg')
		
		def user06 = User.findByUsername('jack@sparrow.com') ?: new User(
			username: 'jack@sparrow.com',
			password: 'jack',
			userProfile: profile06).save(failOnError: true)

		if (!user06.authorities.contains(userRole)) {
			UserRole.create user06, userRole
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
		user01.userProfile.addToEducations(new Education(degree: 'Degree 10', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 11', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 12', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 13', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 14', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 15', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 16', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 17', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 18', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 19', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 20', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 21', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		user01.userProfile.addToEducations(new Education(degree: 'Degree 22', institution: 'Institution 1', website: 'website 1', start_date: new Date(), end_date: new Date()))
		
		println "Creamos los autores"
		def author01 = new Author(forename: 'Angel', surname: 'Gonzalez')
		def author02 = new Author(forename: 'Aradia', surname: 'Rocha')
		def author03 = new Author(forename: 'Will', surname: 'Smith')
		def author04 = new Author(forename: 'Natalia', surname: 'Verbeke')
		
		println "Creamos los engines"
		def engine01 = new EngineSearch(name: 'ACM')
		def engine02 = new EngineSearch(name: 'IEEE')
		def engine03 = new EngineSearch(name: 'Science Direct')
		def engine04 = new EngineSearch(name: 'Springer')

		println "Creamos las busquedas"
		def search01 = new Search(terminos: "terminos 1", engine: engine01, fecha: new Date(), operator: "AND", startYear: "2010", endYear: "2012", components: "component1", maxTotal: 5)
		def search02 = new Search(terminos: "terminos 2", engine: engine01, fecha: new Date(), operator: "OR", startYear: "2010", endYear: "2012", components: "component1", maxTotal: 10)
		def search03 = new Search(terminos: "terminos 3", engine: engine01, fecha: new Date(), operator: "OR", startYear: "2010", endYear: "2012", components: "component1", maxTotal: 10)
		def search04 = new Search(terminos: "terminos 4", engine: engine01, fecha: new Date(), operator: "AND", startYear: "2008", endYear: "2012", components: "component1", maxTotal: 10)
		def search05 = new Search(terminos: "terminos 5", engine: engine01, fecha: new Date(), operator: "AND", startYear: "2008", endYear: "2012", components: "component1", maxTotal: 10)
		def search06 = new Search(terminos: "terminos 6", engine: engine02, fecha: new Date(), operator: "AND", startYear: "2007", endYear: "2012", components: "component2", maxTotal: 20)
		def search07 = new Search(terminos: "terminos 7", engine: engine02, fecha: new Date(), operator: "NOT", startYear: "2012", endYear: "2013", components: "component2", maxTotal: 10)
		def search08 = new Search(terminos: "terminos 8", engine: engine02, fecha: new Date(), operator: "NOT", startYear: "2012", endYear: "2013", components: "component2", maxTotal: 50)
		def search09 = new Search(terminos: "terminos 9", engine: engine03, fecha: new Date(), operator: "OR", startYear: "2013", endYear: "2015", components: "component2", maxTotal: 10)
		def search10 = new Search(terminos: "terminos 10", engine: engine03, fecha: new Date(), operator: "OR", startYear: "2012", endYear: "2014", components: "component3", maxTotal: 10)
		def search11 = new Search(terminos: "terminos 11", engine: engine03, fecha: new Date(), operator: "AND", startYear: "2011", endYear: "2013", components: "component3", maxTotal: 10)
		def search12 = new Search(terminos: "terminos 12", engine: engine04, fecha: new Date(), operator: "AND", startYear: "2011", endYear: "2015", components: "component2", maxTotal: 30)
		def search13 = new Search(terminos: "terminos 13", engine: engine04, fecha: new Date(), operator: "AND", startYear: "2010", endYear: "2012", components: "component3", maxTotal: 10)
		def search14 = new Search(terminos: "terminos 14", engine: engine04, fecha: new Date(), operator: "AND", startYear: "2009", endYear: "2011", components: "component3", maxTotal: 8)

				
		println "Creamos los SLR..."
		def slr01 = new Slr(title: 'Titulo 1', justification: 'Justificacion 1')
		def slr02 = new Slr(title: 'Titulo 2', justification: 'Justificacion 2')
		def slr03 = new Slr(title: 'Titulo 3', justification: 'Justificacion 3')
		def slr04 = new Slr(title: 'Titulo 4', justification: 'Justificacion 4')
		def slr05 = new Slr(title: 'Titulo 5', justification: 'Justificacion 5')
		def slr06 = new Slr(title: 'Titulo 6', justification: 'Justificacion 5')
		def slr07 = new Slr(title: 'Titulo 7', justification: 'Justificacion 7')
		def slr08 = new Slr(title: 'Titulo 8', justification: 'Justificacion 8')
		def slr09 = new Slr(title: 'Titulo 9', justification: 'Justificacion 9')
		def slr10 = new Slr(title: 'Titulo 10', justification: 'Justificacion 10')
		def slr11 = new Slr(title: 'Titulo 11', justification: 'Justificacion 11')
		def slr12 = new Slr(title: 'Titulo 12', justification: 'Justificacion 12')
		def slr13 = new Slr(title: 'Titulo 13', justification: 'Justificacion 13')
		def slr14 = new Slr(title: 'Titulo 14', justification: 'Justificacion 14')
		def slr15 = new Slr(title: 'Titulo 15', justification: 'Justificacion 15')
		def slr16 = new Slr(title: 'Titulo 16', justification: 'Justificacion 16')
		def slr17 = new Slr(title: 'Titulo 17', justification: 'Justificacion 17')
		def slr18 = new Slr(title: 'Titulo 18', justification: 'Justificacion 18')
		
		println "Insertamos las busquedas en el SLR01"
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
		
		println "Asignamos los Slr al usuario 01: Paco"
		user01.userProfile.addToSlrs(slr01)
		user01.userProfile.addToSlrs(slr02)
		user01.userProfile.addToSlrs(slr03)
		user01.userProfile.addToSlrs(slr04)
		user01.userProfile.addToSlrs(slr05)
		user01.userProfile.addToSlrs(slr06)
		user01.userProfile.addToSlrs(slr07)
		user01.userProfile.addToSlrs(slr08)
		user01.userProfile.addToSlrs(slr09)
		user01.userProfile.addToSlrs(slr10)
		user01.userProfile.addToSlrs(slr11)
		user01.userProfile.addToSlrs(slr12)
		user01.userProfile.addToSlrs(slr13)
		user01.userProfile.addToSlrs(slr14)
		user01.userProfile.addToSlrs(slr15)
		user01.userProfile.addToSlrs(slr16)
		user01.userProfile.addToSlrs(slr17)
		user01.userProfile.addToSlrs(slr18)
		
		user01.save(failOnError: true)
		
		println "Creamos las amistades"
		user01.userProfile.addToFriends(profile02)
		user01.userProfile.addToFriends(profile03)
		user01.userProfile.addToFriends(profile04)
		user01.userProfile.addToFriends(profile05)
		//user01.userProfile.addToRequests(profile06)

		user02.userProfile.addToFriends(profile01)
		user03.userProfile.addToFriends(profile01)
		user04.userProfile.addToFriends(profile01)
		user05.userProfile.addToFriends(profile01)
		//user06.userProfile.addToFriends(profile01)
		
    }
    def destroy = {
    }
}
