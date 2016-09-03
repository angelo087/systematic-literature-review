package mendeley.pfc.searchs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.commons.ParamSearch;
import mendeley.pfc.commons.TypeComponentSearch;
import mendeley.pfc.commons.TypeEngine;
import mendeley.pfc.commons.TypeOperatorSearch;
import mendeley.pfc.engines.EngineSearch;
import mendeley.pfc.engines.EngineSearchACM;
import mendeley.pfc.engines.EngineSearchIEEE;
import mendeley.pfc.services.MendeleyService;

public class ParallelEngines {

	private MendeleyService mendeleyService = null;
	private String nameSLR;
	private List<ParamSearch> paramsSearchs = new ArrayList<ParamSearch>();
	private int tammax;
	private List<String> tags = new ArrayList<String>();
	private String operator;
	private int start_year;
	private int end_year;
	private String components;
	private List<TypeEngine> typeEngines = new ArrayList<TypeEngine>();
	
	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "FJRrPdYqo08P01rn";
	public static void main(String[] args) {

		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		
		String email = "alpha_snake087@hotmail.com";
		String pass  = "serpiente2";
		String terminos = "spem";
		String nameSLR = "SLR10: osos polares";
		String operator = "any"; //all,any or none
		int start_year = 2010, end_year = 2012;
		int tammax = 10;
		String components = ""; //full-text, abstract, review or ""
		List<String> tags = new ArrayList<String>();
		tags.add("cr_included"); tags.add("met_met1_yes"); tags.add("met_met2_35"); tags.add("met_met3_ingles");
		
		List<TypeEngine> typesEngines = new ArrayList<TypeEngine>();
		typesEngines.add(TypeEngine.ACM); typesEngines.add(TypeEngine.IEEE);
		
		ParamSearch p01 = new ParamSearch(TypeOperatorSearch.ALL, TypeComponentSearch.ANY, "science");
		ParamSearch p02 = new ParamSearch(TypeOperatorSearch.ANY, TypeComponentSearch.ABSTRACT, "bla bla 2");
		List<ParamSearch> paramSearchs = new ArrayList<ParamSearch>();
		paramSearchs.add(p01); //paramSearchs.add(p02); 
		
		try
		{
			MendeleyService mendeleyService = new MendeleyService(APP_ID, APP_CODE_SECRET, APP_URL, email, pass);
			ParallelEngines parallelEngines = new ParallelEngines(mendeleyService, nameSLR, paramSearchs, tammax, tags, operator, start_year, end_year, components, typesEngines);
			parallelEngines.startSearchs();
		}
		catch(Exception ex)
		{
			System.err.println("ERROR: ParallelEngines -> " + ex.getMessage());
		}
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecucion en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecucion en segundos
	}

	public ParallelEngines(MendeleyService mendeleyService, String nameSLR,
			List<ParamSearch> paramSearchs, int tammax, List<String> tags, String operator,
			int start_year, int end_year, String components,
			List<TypeEngine> typeEngines) {
		this.mendeleyService = mendeleyService;
		this.nameSLR = nameSLR;
		this.paramsSearchs = paramSearchs;
		this.tammax = tammax;
		this.tags = tags;
		this.operator = operator;
		this.start_year = start_year;
		this.end_year = end_year;
		this.components = components;
		this.typeEngines = typeEngines;
	}
	
	public void startSearchs() throws HttpException, MendeleyException, IOException, InterruptedException
	{
		System.out.println("COMIENZA PROCESO BUSQUEDA");
		
		Map<Thread,Boolean> threads = new HashMap<Thread,Boolean>(); //Map con el hilo y boolean que controla si ha finalizado
		
		for(TypeEngine tEngine : typeEngines)
		{
			EngineSearch engineSearch = null;
			
			if(TypeEngine.ACM == tEngine)
			{
				engineSearch = new EngineSearchACM(mendeleyService, nameSLR, paramsSearchs, tammax, tags, operator, start_year, end_year, components);
			}
			else if(TypeEngine.IEEE == tEngine)
			{
				engineSearch = new EngineSearchIEEE(mendeleyService, nameSLR, paramsSearchs, tammax, tags, operator, start_year, end_year, components);
			}
			else if(TypeEngine.SCIENCE == tEngine)
			{
				
			}
			else if(TypeEngine.SPRINGER == tEngine)
			{
				
			}
			
			if(engineSearch != null)
			{
				Thread t = new Thread(engineSearch);
				t.start();
				threads.put(t,false);
			}
		}

		runSearchs(threads);

		System.out.println("PROCESO BUSQUEDA FINALIZADO.");
	}
	
	private void runSearchs(Map<Thread,Boolean> threads) throws InterruptedException
	{
		int totalEngines = threads.size();
		int finEngines   = 0;
		double porcentaje = 0;
		
		int totalRefs = tammax * totalEngines;
		int encontrados = 0;
		
		while(totalEngines != finEngines || porcentaje < 100)
		{
			if(totalEngines != finEngines)
			{
				Iterator it = threads.keySet().iterator();
				
				while(it.hasNext())
				{
					Thread t = (Thread) it.next();
					
					if(!threads.get(t) && !t.isAlive())
					{
						threads.put(t, true);
						finEngines++;
					}
				}
				Thread.sleep(10000);
				encontrados = EngineSearch.totalEncontrados;
				porcentaje = (encontrados * 100) / totalRefs;
			}
			else if(porcentaje < 100)
			{
				porcentaje = 100;
			}
			System.out.println("Porcentaje.........." + porcentaje + "% (" + encontrados + " de " + totalRefs + ").");
		}
		System.out.println("Porcentaje.........." + porcentaje + "% (" + encontrados + " de " + totalRefs + ").");
	}

	public MendeleyService getMendeleyService() {
		return mendeleyService;
	}

	public void setMendeleyService(MendeleyService mendeleyService) {
		this.mendeleyService = mendeleyService;
	}

	public String getNameSLR() {
		return nameSLR;
	}

	public void setNameSLR(String nameSLR) {
		this.nameSLR = nameSLR;
	}

	public int getTammax() {
		return tammax;
	}

	public void setTammax(int tammax) {
		this.tammax = tammax;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getStart_year() {
		return start_year;
	}

	public void setStart_year(int start_year) {
		this.start_year = start_year;
	}

	public int getEnd_year() {
		return end_year;
	}

	public void setEnd_year(int end_year) {
		this.end_year = end_year;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public List<TypeEngine> getTypeEngines() {
		return typeEngines;
	}

	public void setTypeEngines(List<TypeEngine> typeEngines) {
		this.typeEngines = typeEngines;
	}

	public List<ParamSearch> getParamsSearchs() {
		return paramsSearchs;
	}

	public void setParamsSearchs(List<ParamSearch> paramsSearchs) {
		this.paramsSearchs = paramsSearchs;
	}
}
