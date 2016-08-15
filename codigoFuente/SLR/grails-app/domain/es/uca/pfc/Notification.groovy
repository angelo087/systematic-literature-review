package es.uca.pfc

import java.util.Date;

class Notification {

    static belongsTo = [profile: UserProfile]
	
	Date fecha = new Date()
	String fechaString = ""
	String asunto = ""
	String texto = ""
	String tipo = "slr" // slr-friend-search
	boolean leido = false
	String guid = UUID.randomUUID().toString();
	
    static constraints = {
    }
	
	static mapping = {
		texto type: 'text'
	}
	
	def beforeInsert = {
		fechaString = getTimeString(fecha)
	}
	
	String getTimeString(Date date)
	{
		String timeToString = "";
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal1.setTime(date);
		cal2.setTime(new Date());
		
		def milis1 = cal1.getTimeInMillis();
		def milis2 = cal2.getTimeInMillis();
		
		def diff = milis2 - milis1;
		
		// Calculamos la diferencia en segundos
		def diffSeconds = diff / 1000;
		if(diffSeconds < 60)
		{
			timeToString = "Hace " + Math.round(diffSeconds) + " segundos.";
		}
		else
		{
			def diffMinutes = diff / (60 * 1000);
			if(diffMinutes < 60)
			{
				timeToString = "Hace " + Math.round(diffMinutes) + " minutos.";
			}
			else
			{
				def diffHours = diff / (60 * 60 * 1000);
				if(diffHours < 24)
				{
					timeToString = "Hace " + Math.round(diffHours) + " horas.";
				}
				else
				{
					def diffDays = diff / (24 * 60 * 60 * 1000);
					if(diffDays < 30)
					{
						timeToString = "Hace " + Math.round(diffDays) + " dias.";
					}
					else
					{
						def diffMonths = diff / (30 * 24 * 60 * 60 * 1000);
						if(diffMonths <= 6)
						{
							timeToString = "Hace " + Math.round(diffMonths) + " meses.";
						}
						else
						{
							timeToString = "Hace más de 6 meses";
						}
					}
				}
			}
		}
		
		return timeToString;
	}
}
