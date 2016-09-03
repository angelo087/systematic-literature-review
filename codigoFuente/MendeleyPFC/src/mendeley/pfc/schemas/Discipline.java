package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Discipline.
 */
public class Discipline 
{
	/** The name. */
	private String name;
		
	private List<String> subdisciplines = new ArrayList<String>();
	
	public List<String> getSubdisciplines() { return subdisciplines; }
	public void setSubdisciplines(List<String> subdisciplines) { this.subdisciplines = subdisciplines; }
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Discipline [name=" + name + ", subdisciplines=" + subdisciplines + "]";
	}
}
