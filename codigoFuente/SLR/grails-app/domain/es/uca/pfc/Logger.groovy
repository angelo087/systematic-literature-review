package es.uca.pfc

import java.util.Date;

class Logger {

    static belongsTo = [profile: UserProfile]
	
	String tipo = 'bienvenida'
	/*UserProfile friend
	UserProfile friendFriend
	Slr slr
	Date fecha = new Date()
	*/
    static constraints = {
		/*tipo(inList:['bienvenida','crear','buscar','seguir','fr-bienvenida','fr-crear','fr-buscar','fr-seguir'],display:false,blank:false)
		friend(display: false, blank: true, nullable: true)
		friendFriend(display: false, blank: true, nullable: true)
		slr(display:false, blank: true, nullable: true)*/
    }
}
