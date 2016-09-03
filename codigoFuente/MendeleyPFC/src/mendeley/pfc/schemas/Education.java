package mendeley.pfc.schemas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Education.
 */
public class Education 
{
	/** The degree. */
	private String degree;
	
	/** The institution. */
	private String institution;
	
	/** The website. */
	private String website;
	
	/** The start date. */
	private String start_date;
	
	/** The end date. */
	private String end_date;
	
	/**
	 * Gets the degree.
	 * 
	 * @return the degree
	 */
	public String getDegree() {
		return degree;
	}
	
	/**
	 * Sets the degree.
	 * 
	 * @param degree the new degree
	 */
	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	/**
	 * Gets the institution.
	 * 
	 * @return the institution
	 */
	public String getInstitution() {
		return institution;
	}
	
	/**
	 * Sets the institution.
	 * 
	 * @param institution the new institution
	 */
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	/**
	 * Gets the website.
	 * 
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * Sets the website.
	 * 
	 * @param website the new website
	 */
	public void setWebsite(String website) {
		this.website = website;
	}
	
	/**
	 * Gets the start date.
	 * 
	 * @return the start date
	 * @throws ParseException 
	 */
	public Date getStartDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = format.parse(start_date);
		
		return fecha;
	}
	
	/**
	 * Sets the start date.
	 * 
	 * @param start_date the new start date
	 */
	public void setStartDate(String start_date) {
		this.start_date = start_date;
	}
	
	/**
	 * Gets the end date.
	 * 
	 * @return the end date
	 * @throws ParseException 
	 */
	public Date getEndDate() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date fecha = format.parse(end_date);
		return fecha;
	}
	
	/**
	 * Sets the end date.
	 * 
	 * @param end_date the new end date
	 */
	public void setEndDate(String end_date) {
		this.end_date = end_date;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Education [degree=" + degree + ", end_date=" + end_date
				+ ", institution=" + institution 
				+ ", start_date=" + start_date + ", website=" + website + "]";
	}
}
