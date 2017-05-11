package background.pfc.commons;

import java.util.ArrayList;
import java.util.List;

public class StaticData {

	private String guid;
	private List<Reference> referencesEngineSearch = new ArrayList<Reference>();
	private int contMax	= 0;
	private int contHilos = 0;
	
	public StaticData(String guid) {
		this.guid = guid;
		referencesEngineSearch = new ArrayList<Reference>();
		contMax	= 0;
		contHilos = 0;
	}
	
	public StaticData(String guid, List<Reference> referencesEngineSearch,
			int contMax, int contHilos) {
		this.guid = guid;
		this.referencesEngineSearch = referencesEngineSearch;
		this.contMax = contMax;
		this.contHilos = contHilos;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public List<Reference> getReferencesEngineSearch() {
		return referencesEngineSearch;
	}

	public void setReferencesEngineSearch(List<Reference> referencesEngineSearch) {
		this.referencesEngineSearch = referencesEngineSearch;
	}

	public int getContMax() {
		return contMax;
	}

	public void setContMax(int contMax) {
		this.contMax = contMax;
	}

	public int getContHilos() {
		return contHilos;
	}

	public void setContHilos(int contHilos) {
		this.contHilos = contHilos;
	}
	
	public void increaseContMax() {
		contMax++;
	}
	
	public void decreaseContMax() {
		if(contMax != 0)
		{
			contMax--;
		}
	}
	
	public void increaseContHilos() {
		contHilos++;
	}
	
	public void decreaseContHilos() {
		if (contHilos != 0)
		{
			contHilos--;
		}
	}
}
