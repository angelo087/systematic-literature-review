package es.uca.pfc

class EngineSearch {
	
	String name = ""			//nombre
	String display_name = ""	
	String url = ""				//url
	String image = ""			//imagen a mostrar
	String text = ""			//texto a mostrar
	boolean checkDefault  = false	//activado por defecto o no
	boolean status = true		//indica si se puede realizar busquedas o no
	String apiKey = ""
	
    static constraints = {
		apiKey(blank:true, nullable: true)
    }
}
