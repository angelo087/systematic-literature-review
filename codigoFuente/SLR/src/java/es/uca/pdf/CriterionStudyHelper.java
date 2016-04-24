package es.uca.pdf;

public class CriterionStudyHelper {
	
	private String name;
	private String description;
	private int studies;
	private double frecuency;
	
	public CriterionStudyHelper()
	{
		
	}
	
	public CriterionStudyHelper(String name, String description, int studies, double frecuency) {
		this.name = name;
		this.description = description;
		this.studies = studies;
		this.frecuency = frecuency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStudies() {
		return studies;
	}

	public void setStudies(int studies) {
		this.studies = studies;
	}

	public double getFrecuency() {
		return frecuency;
	}

	public void setFrecuency(double frecuency) {
		this.frecuency = frecuency;
	}	
}
