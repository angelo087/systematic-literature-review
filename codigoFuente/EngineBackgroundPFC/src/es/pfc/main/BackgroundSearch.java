package es.pfc.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import es.pfc.commons.ComponentSearch;
import es.pfc.commons.OperatorSearch;
import es.pfc.commons.SearchTermParam;
import es.pfc.engine.EngineSearch;
import es.pfc.engine.EngineSearchACM;
import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.services.MendeleyService;

public class BackgroundSearch {

	private MendeleyService mendeleyService = null;
	private boolean opACM;
	private boolean opIEEE;
	private boolean opSCIENCE;
	private boolean opSPRINGER;
	private String nameSLR;
	private int tammax;
	private List<String> tags = new ArrayList<String>();
	private int start_year;
	private int end_year;
	private List<SearchTermParam> searchsTerms;
	
	public static void main(String[] args) {
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		
		String 	client_id = "1044", 
				client_secret = "5qQ6zm5iYpvUehj4", 
				redirect_url = "http://localhost:8090/SLR/indexMendeley/",
				access_token  = "asdsadasd",
				refresh_token = "asdasdaaaaaaa";
		
		String email = "angel.gonzatoro@gmail.com", password = "angel.gonzatoro";
		//String terminos = "spem";
		String nameSLR = "SLR1: spem study";
		
		//String operator = "any"; //all,any or none
		int start_year = 2010, end_year = 2012;
		int tammax = 1;
		
		SearchTermParam stp01 = new SearchTermParam(ComponentSearch.ANYFIELD, OperatorSearch.ALL, "word");
		//SearchTermParam stp02 = new SearchTermParam(ComponentSearch.AUTHOR, OperatorSearch.NONE, "toro");
		//SearchTermParam stp03 = new SearchTermParam(ComponentSearch.TITLE, OperatorSearch.ANY, "of");
		List<SearchTermParam> searchsTerms = new ArrayList<SearchTermParam>();
		searchsTerms.add(stp01); //searchsTerms.add(stp02); searchsTerms.add(stp03);
		
		List<String> tags = new ArrayList<String>();
		tags.add("cr_included"); tags.add("met_met1_yes"); tags.add("met_met2_35"); tags.add("met_met3_ingles");
		
		boolean opACM = true,
				opIEEE = true,
				opSCIENCE = false,
				opSPRINGER = false;
		
		try
		{
			BackgroundSearch backgroundSearch = new BackgroundSearch(client_id, client_secret, access_token, refresh_token, redirect_url, email, password, opACM, opIEEE, opSCIENCE, opSPRINGER, nameSLR, tammax, tags, start_year, end_year, searchsTerms);
			backgroundSearch.startSearchs();
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPCION: " + ex.getMessage());
		}
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecucion en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecucion en milisegundos
	}

	public BackgroundSearch(String ci, String cs, String at, String rt, String ru, String email, String pass,
			boolean opACM, boolean opIEEE, boolean opSCIENCE, boolean opSPRINGER,
			String nameSLR, int tammax, List<String> tags, int start_year, int end_year,
			List<SearchTermParam> searchsTerms) throws Exception {

		//this.mendeleyService = new MendeleyService(ci,cs,ru,email,pass,at,rt);
		this.mendeleyService = new MendeleyService(ci,cs,ru,email,pass);
		this.opACM = opACM;
		this.opIEEE = opIEEE;
		this.opSCIENCE = opSCIENCE;
		this.opSPRINGER = opSPRINGER;
		this.nameSLR = nameSLR;
		this.tammax = tammax;
		this.tags = tags;
		this.start_year = start_year;
		this.end_year = end_year;
		this.searchsTerms = searchsTerms;
	}
	
	public void startSearchs() throws FailingHttpStatusCodeException, MalformedURLException, IOException, MendeleyException
	{
		System.out.println("COMIENZA PROCESO BUSQUEDA");
		
		Thread tACM = new Thread(), tIEEE = new Thread();
		
		opIEEE = false;
		
		if(opACM)
		{
			tACM = new Thread(new EngineSearchACM(mendeleyService, nameSLR, tammax, tags, start_year, end_year, searchsTerms));
			tACM.start();
		}
		
		if(opIEEE)
		{
			tIEEE = new Thread(new EngineSearchACM(mendeleyService, nameSLR, tammax, tags, start_year, end_year, searchsTerms));
			tIEEE.start();
		}
		
		while(tACM.isAlive() || tIEEE.isAlive())
		{
			// Wait finish process
		}
		
		System.out.println("PROCESO BUSQUEDA FINALIZADO.");
	}
}
