package background.pfc.commons;

import java.util.ArrayList;
import java.util.List;

public class PoolReferences {

	public static List<StaticData> staticDatas = new ArrayList<StaticData>();
	
	public static void addStaticData(String guidStaticData)
	{
		synchronized (staticDatas) {
			if(getStaticData(guidStaticData) == null)
			{
				StaticData staticData = new StaticData(guidStaticData);
				staticDatas.add(staticData);
			}
		}
	}
	
	public static void removeStaticData(String guidStaticData)
	{
		synchronized (staticDatas) {
			StaticData staticDataToRemove = getStaticData(guidStaticData);
			if(staticDataToRemove != null)
			{
				staticDatas.remove(staticDataToRemove);
			}
		}
	}
	
	public static StaticData getStaticData(String guidStaticData)
	{
		StaticData staticData = null;
		
		synchronized (staticDatas) {
			
			for(StaticData sd : staticDatas)
			{
				if (sd.getGuid().equals(guidStaticData))
				{
					staticData = sd;
					break;
				}
			}
		}
		
		return staticData;
	}

	public static void increaseContMax(String guidStaticData)
	{
		StaticData staticData = getStaticData(guidStaticData);
		
		staticData.increaseContMax();
	}
	
	public static void decreaseContMax(String guidStaticData)
	{
		StaticData staticData = getStaticData(guidStaticData);
		
		staticData.decreaseContMax();
	}
	public static void increaseContHilos(String guidStaticData)
	{
		StaticData staticData = getStaticData(guidStaticData);
		
		staticData.increaseContHilos();
	}
	
	public static void decreaseContHilos(String guidStaticData)
	{
		StaticData staticData = getStaticData(guidStaticData);
		
		staticData.decreaseContHilos();
	}
	
	public static boolean addReferenceToEngineSearch(String guidStaticData, Reference reference)
	{
		boolean ok = false;
		
		synchronized(getStaticData(guidStaticData).getReferencesEngineSearch())
		{
			try
			{
				getStaticData(guidStaticData).getReferencesEngineSearch().add(reference);
				ok = true;
			}
			catch(Exception ex)
			{
				ok = false;
			}
		}
		return ok;
	}
}
