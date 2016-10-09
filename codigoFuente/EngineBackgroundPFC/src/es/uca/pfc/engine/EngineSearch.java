package es.uca.pfc.engine;

import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import mendeley.pfc.commons.MendeleyException;
import mendeley.pfc.services.MendeleyService;

public class EngineSearch implements Runnable
{
	private String engine;
	private MendeleyService mendeleyService = null;
	private String nameSLR;
	private String terminos;
	private int tammax;
	private List<String> tags;
	private String operator;
	private int start_year;
	private int end_year;
	private String components;
	
	public EngineSearch(String engine, MendeleyService mendeleyService, String nameSLR, String terminos, int tammax, List<String> tags, String operator, int start_year, int end_year, String components)
	{
		this.engine = engine;
		this.mendeleyService = mendeleyService;
		this.nameSLR = nameSLR;
		this.terminos = terminos;
		this.tammax = tammax;
		this.tags = tags;
		this.operator = operator;
		this.start_year = start_year;
		this.end_year = end_year;
		this.components = components;
	}
	
	public void run()
	{
		try 
		{
			if(this.engine.equals("ACM"))
			{
				long TInicioACM, TFinACM, tiempoACM;
				TInicioACM = System.currentTimeMillis();
				EngineACM engineACM = new EngineACM(mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components);
				engineACM.startSearchs();
				TFinACM = System.currentTimeMillis();
				tiempoACM = (TFinACM - TInicioACM)/1000; //Calculamos los milisegundos de diferencia
				System.out.println("RESULTADOS ACM: " + EngineACM.references.size() + " referencias en " + tiempoACM + " segundos.");
			}
			else if(this.engine.equals("IEEE"))
			{
				long TInicioIEEE, TFinIEEE, tiempoIEEE;
				TInicioIEEE = System.currentTimeMillis();
				EngineIEEE engineIEEE = new EngineIEEE(mendeleyService, nameSLR, terminos, tammax, tags, operator, start_year, end_year, components);
				engineIEEE.startSearchs();
				TFinIEEE = System.currentTimeMillis();
				tiempoIEEE = (TFinIEEE - TInicioIEEE)/1000;
				System.out.println("RESULTADOS IEEE: " + EngineIEEE.references.size() + " referencias en " + tiempoIEEE + " segundos.");
			}
		}
		catch (FailingHttpStatusCodeException | InterruptedException | IOException | MendeleyException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
