package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Employment.
 */
public class Employment
{
	private String institution;
	private String position;
	private String degree;
	private String start_date;
	private String end_date;
	private String website;
	private List<String> classes = new ArrayList<String>();
	private boolean is_main_employment = false;
	
	public Employment()
	{
		
	}
	
	public String getInstitution() { return institution; }
	public String getPosition() { return position; }
	public String getDegree() { return degree; }
	public String getStartDate() { return start_date; }
	public String getEndDate() { return end_date; }
	public String getWebSite() { return website; }
	public List<String> getClasses() { return classes; }
	public boolean isMainEmployment() { return is_main_employment; }
	
	public void setInstitution(String institution) { this.institution = institution; }
	public void setPosition(String position) { this.position = position; }
	public void setDegree(String degree) { this.degree = degree; }
	public void setStartDate(String start_date) { this.start_date = start_date; }
	public void setEndDate(String end_date) { this.end_date = end_date; }
	public void setWebSite(String website) { this.website = website; }
	public void setClasses(List<String> classes) { this.classes = classes; }
	public void setMainEmployment(boolean is_main_employment) { this.is_main_employment = is_main_employment; }	
	
	public String toString()
	{
		return "Employment [institution: " + institution + 
				", position: " + position + 
				", degree: " + degree + 
				", startDate: " + start_date +
				", endDate: " + end_date +
				", website: " + website + 
				", classes: " + classes + 
				", mainEmployment: " + is_main_employment + "]";
	}
}
