package mendeley.pfc.schemas;

public class Person 
{
	String first_name = "",last_name = "";
    
    public Person(String first_name, String last_name)
    {
    	this.first_name = first_name;
    	this.last_name = last_name;
    }

    public String getForename() {
            return first_name;
    }

    public void setForename(String first_name) {
            this.first_name = first_name;
    }

    public String getSurname() {
            return last_name;
    }

    public void setSurname(String last_name) {
            this.last_name = last_name;
    }
    
    public String toString() {
    	return this.last_name+","+this.first_name;
    }
    
    public String toString2() {
    	return this.first_name + " " + this.last_name;
    }
}
