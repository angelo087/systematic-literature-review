package es.uca.pfc

class MendeleyApi {

	String clientId;
	String clientSecret;
	String redirectUri;
	int totalHilos;			// Total hilos para descargar referencias en paralelo.
	int totalTries;			// Total intentos para realizar login.
	
    static constraints = {
    }
}
