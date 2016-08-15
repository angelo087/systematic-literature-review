package es.uca.pfc

class UserMendeley {

	static belongsTo = [user: User]
	
	String email_mend
	String pass_mend
	String access_token = "";
	String token_type = "";
	String expires_in = "";
	String refresh_token = "";
	
    static constraints = {
    }
}
