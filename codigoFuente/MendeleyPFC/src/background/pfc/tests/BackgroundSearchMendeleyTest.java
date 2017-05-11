package background.pfc.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import background.pfc.commons.SearchTermParam;
import background.pfc.enums.ComponentSearch;
import background.pfc.enums.OperatorSearch;
import background.pfc.enums.TypeEngineSearch;
import background.pfc.main.BackgroundSearchMendeley;

public class BackgroundSearchMendeleyTest {
	
	public static void main(String[] args)
	{
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();

		String 	client_id = "1044", 
				client_secret = "N1krjxVLCiflzZTt", 
				redirect_url = "http://45.76.94.16:8080/SLR/indexMendeley/",
				access_token  = "asdsadasd",
				refresh_token = "asdasdaaaaaaa";
		
		String email = "angel.gonzatoro@gmail.com", password = "Number98*";
		String nameSLR = "SLR1: spem study";
		
		int total_hilos = 5;
		int total_tries = 5;
		int tammax = 8;
		
		int start_year = 2010, end_year = 2017;
		
		SearchTermParam stp01 = new SearchTermParam(ComponentSearch.ANYFIELD, OperatorSearch.ALL, "word");
		SearchTermParam stp02 = new SearchTermParam(ComponentSearch.AUTHOR, OperatorSearch.NONE, "toro");
		//SearchTermParam stp03 = new SearchTermParam(ComponentSearch.TITLE, OperatorSearch.ANY, "of");
		List<SearchTermParam> searchsTerms = new ArrayList<SearchTermParam>();
		searchsTerms.add(stp01); searchsTerms.add(stp02); //searchsTerms.add(stp03);
		
		List<String> tags = new ArrayList<String>();
		tags.add("cr_included"); tags.add("met_met1_yes"); tags.add("met_met2_35"); tags.add("met_met3_ingles");
		
		Map<TypeEngineSearch, Boolean> optionsEngine = new HashMap<TypeEngineSearch, Boolean>();
		optionsEngine.put(TypeEngineSearch.ACM, true);
		optionsEngine.put(TypeEngineSearch.IEEE, true);
		optionsEngine.put(TypeEngineSearch.SCIENCE, false);
		optionsEngine.put(TypeEngineSearch.SPRINGER, false);
		
		Map<TypeEngineSearch,String> apiKeysEngine = new HashMap<TypeEngineSearch, String>();
		apiKeysEngine.put(TypeEngineSearch.SCIENCE, "80aa542193705ce36ebfe094078b9aa3");
		apiKeysEngine.put(TypeEngineSearch.SPRINGER, "c8c8ee4b2c20f0046806762317d0d6e2");
		
		String guidStaticData = UUID.randomUUID().toString();
		
		try
		{
			System.out.println("GUID STATIC DATA => " + guidStaticData);
			BackgroundSearchMendeley backgroundSearch = new BackgroundSearchMendeley(client_id, client_secret, access_token, 
					refresh_token, redirect_url, email, password, optionsEngine, nameSLR, 
					tammax, tags, start_year, end_year, searchsTerms, apiKeysEngine, total_hilos, total_tries, guidStaticData);
			backgroundSearch.startSearchs();
			System.out.println("Total encontrados: " + backgroundSearch.getTotalReferences());
		}
		catch(Exception ex)
		{
			System.out.println("EXCEPCION: " + ex.getMessage());
		}
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecucion en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecucion en milisegundos
	}
}
