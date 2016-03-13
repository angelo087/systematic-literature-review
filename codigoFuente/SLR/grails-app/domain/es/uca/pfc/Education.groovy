package es.uca.pfc

import java.util.Date;

class Education {

    static belongsTo = [profile: UserProfile]
	
	String degree;
	String institution;
	String website;
	Date start_date;
	Date end_date;

    static constraints = {
    }
}
