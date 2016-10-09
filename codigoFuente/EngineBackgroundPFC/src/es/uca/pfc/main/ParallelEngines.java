package es.uca.pfc.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.services.MendeleyService;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import es.uca.pfc.engine.EngineScience;
import es.uca.pfc.engine.EngineSearch;

public class ParallelEngines {
	private MendeleyService mendeleyService = null;
	private boolean opACM;
	private boolean opIEEE;
	private boolean opSCIENCE;
	private boolean opSPRINGER;
	private String nameSLR;
	private String terminos;
	private int tammax;
	private List<String> tags = new ArrayList<String>();
	private String operator;
	private int start_year;
	private int end_year;
	private String components;
	
	public static void main(String[] args) 
	{
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		/*String 	client_id = "1044", 
				client_secret = "IuV68GS1j5MkoPTp", 
				redirect_url = "http://localhost:8090/Systematic_Literature_Review",
				access_token  = "MSwxNDIyNTQ5Mjk3NjA4LDI0OTc0NDIxLDEwNDQsYWxsLCxmalRaSFhWclJxVUpIX20tRDFBWXRKWTlsNDQ",
				refresh_token = "MSwyNDk3NDQyMSwxMDQ0LGFsbCxWMjRqVzZXaG5RQ29CQVNCenFsTEJRQUplVkE";*/
		
		String 	client_id = "1044", 
				client_secret = "pLG4CUrdi2DtPb6l", 
				redirect_url = "http://default-environment-bvwspgmbk4.elasticbeanstalk.com/",
				access_token  = "MSwxNDIyNTQ5Mjk3NjA4LDI0OTc0NDIxLDEwNDQsYWxsLCxmalRaSFhWclJxVUpIX20tRDFBWXRKWTlsNDQ",
				refresh_token = "MSwyNDk3NDQyMSwxMDQ0LGFsbCxWMjRqVzZXaG5RQ29CQVNCenFsTEJRQUplVkE";
		
		String email = "alpha_snake087@hotmail.com", password = "serpiente2";
		String terminos = "spem";
		String nameSLR = "SLR1: spem study";
		
		String operator = "any"; //all,any or none
		int start_year = 2010, end_year = 2012;
		int tammax = 12;
		String components = ""; //full-text, abstract, review or ""
		
		List<String> tags = new ArrayList<String>();
		tags.add("cr_included"); tags.add("met_met1_yes"); tags.add("met_met2_35"); tags.add("met_met3_ingles");
		
		boolean opACM = false,
				opIEEE = false,
				opSCIENCE = true,
				opSPRINGER = false;
		
		try
		{
			ParallelEngines parallelengines = new ParallelEngines(client_id, client_secret, access_token, refresh_token, redirect_url, email, password, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components, opACM, opIEEE, opSCIENCE, opSPRINGER);
			//parallelengines.startSearchs();
			System.out.println("OK");
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPCION: " + ex.getMessage());
		}
		

		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecuci�n en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecuci�n en milisegundos
	}
	
	public ParallelEngines(String ci, String cs, String at, String rt, String ru, String email, String pass, String nameSLR, String terms, int tammax, List<String> tags, String operator, int start_year, int end_year, String comps, boolean opACM, boolean opIEEE, boolean opScience, boolean opSpringer) throws Exception
	{
		this.mendeleyService = new MendeleyService(ci, cs, ru, email, pass);
		this.nameSLR = nameSLR;
		this.terminos = terms;
		this.tammax = tammax;
		this.tags = tags;
		this.operator = operator;
		this.start_year = start_year;
		this.end_year = end_year;
		this.components = comps;
		this.opACM = opACM;
		this.opIEEE = opIEEE;
		this.opSCIENCE = opScience;
		this.opSPRINGER = opSpringer;
	}
	
	public void startSearchs() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException, MendeleyException
	{
		System.out.println("COMIENZA PROCESO BUSQUEDA");
		
		Thread tACM = new Thread(), tIEEE = new Thread();
		
		if(opACM)
		{
			tACM = new Thread(new EngineSearch("ACM", mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components));
			tACM.start();
		}
		
		if(opIEEE)
		{
			tIEEE = new Thread(new EngineSearch("IEEE", mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components));
			tIEEE.start();
		}
		
		if(opSCIENCE)
		{
			EngineScience engineScience = new EngineScience(mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components);
			
			engineScience.startSearchs();
		}
		
		while(tACM.isAlive() || tIEEE.isAlive())
		{
			
		}
		
		System.out.println("PROCESO BUSQUEDA FINALIZADO.");
	}
	
	/*public void startSearchs() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
	{
		System.out.println("COMIENZA PROCESO BUSQUEDA");
		
		Thread tACM = new Thread(), tIEEE = new Thread();
		
		if(opACM)
		{
			EngineACM engineACM = new EngineACM(mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components);
			engineACM.startSearchs();
		}
		
		if(opIEEE)
		{
			EngineIEEE engineIEEE = new EngineIEEE(mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components);
			engineIEEE.startSearchs();
		}
		
		System.out.println("PROCESO BUSQUEDA FINALIZADO.");
	}*/

}
