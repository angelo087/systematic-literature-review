package es.uca.pfc

import java.util.Date;

import org.apache.tools.ant.types.resources.selectors.InstanceOf;

class UserProfile {
	
	static belongsTo = [user: User]
	static hasMany = [slrs:Slr, notifications: Notification, loggers: Logger, friends: UserProfile, requests: UserProfile, 
					  educations: Education]
	
	String first_name = ""
	String last_name = ""
	String display_name = ""
	String url_foto = ""
	String guid = UUID.randomUUID().toString();
	String idmend = ""
	String research_interests = ""
	String academic_status = ""
	String link = ""
	String created = ""
	String biography = ""
	String codeBotonEnlace = ""
	String locationName = ""
	String locationLatitude = ""
	String locationLongitude = ""
	String discipline = ""
	String webMendeleyProfile = ""
	
	Date ultimaConexion = new Date()
	Date fechaRegistro  = new Date()
	
	String lastGuidTaskSearch = "";

    static constraints = {
    }
	
	//static mappedBy = [loggers: 'profile', friends: 'profile']
	//static mappedBy = [loggers: 'profile']
	
	def beforeInsert = {
		display_name = first_name + ' ' + last_name;
		println display_name + ': ' + guid
		
		addToLoggers(new Logger(tipo: 'bienvenida'))
	}
	
	boolean equals(Object obj)
	{
		return (this.id == ((UserProfile)obj).id);
	}
}
