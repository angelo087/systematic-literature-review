package es.uca.pfc

import java.util.Comparator;
import java.util.Date;

class Logger {

    static belongsTo = [profile: UserProfile]
	
	String tipo = 'bienvenida'
	Date submitDate = new Date()
	String timeString = ''
	//UserProfile userProfile
	
    static constraints = {
		tipo(inList:['bienvenida','crear','buscar','seguir','fr-bienvenida','fr-crear','fr-buscar','fr-seguir'],display:false,blank:false)
    }
	
	@Override
	public boolean equals(Object obj)
	{
		if (null == obj) { return false; }
		if(getClass() != obj.getClass()) { return false; }
		
		final Logger other = (Logger) obj;
		
		if (this.id != other.id) { return false; }
		
		return true;
	}
}
