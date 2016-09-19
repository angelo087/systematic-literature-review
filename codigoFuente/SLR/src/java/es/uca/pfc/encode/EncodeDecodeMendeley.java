package es.uca.pfc.encode;

import java.util.UUID;

public class EncodeDecodeMendeley {
	
	private static final int NUMCHAR = 5; // impar
	
	public static String encodePasswordMendeley(String password)
	{
		String encPassAux = password.substring(Integer.parseInt(Long.toString(Math.round(password.length()/2)))) +
							password.substring(0, Integer.parseInt(Long.toString(Math.round(password.length()/2))));
		
		String encPass = "";
		
		for(int i = 0; i < encPassAux.length(); i++)
		{
			encPass += UUID.randomUUID().toString().substring(0, NUMCHAR) + encPassAux.charAt(i);
		}
		
		encPass = encPass.substring(Integer.parseInt(Long.toString(Math.round(encPass.length()/2)))) +
				encPass.substring(0, Integer.parseInt(Long.toString(Math.round(encPass.length()/2))));
		
		return encPass;
	}
	
	public static String decodePasswordMendeley(String password)
	{
		String decPassAux = password.substring(Integer.parseInt(Long.toString(Math.round(password.length()/2)))) +
				password.substring(0, Integer.parseInt(Long.toString(Math.round(password.length()/2))));
		
		String decPass = "";
		int cont = 0;
		for(int i = 0; i < decPassAux.length(); i++)
		{
			if(cont != NUMCHAR)
			{
				cont++;
			}
			else
			{
				decPass += decPassAux.charAt(i);
				cont = 0;
			}
		}
				
		if (decPass.length() % 2 != 0) // impar
		{
			decPass = decPass.substring(Integer.parseInt(Long.toString(Math.round(decPass.length()/2)+1))) +
					decPass.substring(0, Integer.parseInt(Long.toString(Math.round(decPass.length()/2)+1)));
		}
		else
		{
			decPass = decPass.substring(Integer.parseInt(Long.toString(Math.round(decPass.length()/2)))) +
					decPass.substring(0, Integer.parseInt(Long.toString(Math.round(decPass.length()/2))));
		}
		
		return decPass;
	}
}
