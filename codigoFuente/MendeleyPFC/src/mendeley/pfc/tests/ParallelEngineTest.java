package mendeley.pfc.tests;

public class ParallelEngineTest {

	public static final String APP_EMAIL = "angel.gonzatoro@gmail.com";
	public static final String APP_PASS  = "angel.gonzatoro";
	public static final String APP_ID = "1044";
	public static final String APP_NAME = "Systematic_Literature_Review";
	public static final String APP_URL = "http://localhost:8090/SLR/indexMendeley/";
	public static final String APP_CODE_SECRET = "FJRrPdYqo08P01rn";
	
	public static void main(String[] args)
	{
		String email = "alpha_snake087@hotmail.com";
		String pass  = "serpiente2";
		String accessToken = "MSwxNDY2MDE4ODAzNzY1LDI0OTc0NDIxLDEwNDQsYWxsLCwsNWU2My01MmRkNTQ1NTU1MTgyYWQzYzk1NTUxMTQ1NjhlNjk1NmU0LDAxNGM4OGRlLTkyNjktMzdjZC1iZjA4LTM0MTZiZTA0MTVhOSxTMzhiLVZjSko5SXFncV9IMUlLOXhaYzZ0U2M";
		String refreshToken = "MSwyNDk3NDQyMSwxMDQ0LGFsbCw1ZTYzLTUyZGQ1NDU1NTUxODJhZDNjOTU1NTExNDU2OGU2OTU2ZTQsNzIyNy03YzZiYzM1NDU1MTk1MjJjMzcxLWE1OGIyMDY1Zjk1MTRjODMsMTQ2NjAxNTIwMzc2MywwMTRjODhkZS05MjY5LTM3Y2QtYmYwOC0zNDE2YmUwNDE1YTksQkhNdi1iWnA3cWlXd2NuZDhrUEVZRUpjVlo0";
		
		String terminos = "spem";
		String nameSLR = "SLR1: spem study";
		String operator = "any"; //all,any or none
		int start_year = 2010, end_year = 2012;
		int tammax = 12;
		String components = ""; //full-text, abstract, review or ""
		
		boolean opACM = false,
				opIEEE = false,
				opSCIENCE = true,
				opSPRINGER = false;
		
		long TInicio, TFin, tiempo;
		TInicio = System.currentTimeMillis();
		
		// Proceso
		
		TFin = System.currentTimeMillis();
		tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
		System.out.println("Tiempo de ejecucion en segundos: " + tiempo/1000); //Mostramos en pantalla el tiempo de ejecucion en milisegundos
	}
}
