package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Profile.
 */
public class Profile
{
	public static final String ME = "me";
	
	private String id;
	private String first_name;
	private String last_name;
	private Location location = new Location();
	private String display_name;
	private String email;
	private String research_interests;
	private List<Education> education = new ArrayList<Education>();
	private List<Employment> employment = new ArrayList<Employment>();
	private String academic_status;
	private String link;
	private Discipline discipline = new Discipline();
	private List<Discipline> disciplines = new ArrayList<Discipline>();
	private List<Image> photos = new ArrayList<Image>();
	private boolean verified = false;
	private String created;
	private String biography;
	private boolean marketing = false;
	
	public String getId() { return id; }
	public String getFirstName() { return first_name; }
	public String getLastName() { return last_name; }
	public Location getLocation() { return location; }
	public String getDisplayName() { return display_name; }
	public String getEmail() { return email; }
	public String getResearchInterests() { return research_interests; }
	public List<Education> getEducation() { return education; }
	public List<Employment> getEmployment() { return employment; }
	public String getAcademicStatus() { return academic_status; }
	public String getLink() { return link; }
	public Discipline getDiscipline() { return discipline; }
	public List<Discipline> getDisciplines() { return disciplines; }
	public Image getPhoto()
	{
		if(this.photos.size() == 0)
			return null;
		else
			return photos.get(0);
	}
	public List<Image> getPhotos() { return photos; }
	public boolean isVerified() { return verified; }
	public String getCreated() { return created; }
	public String getBiography() { return biography; }
	public boolean isMarketing() { return marketing; }
	
	public void setId(String id) { this.id = id; }
	public void setFirstName(String first_name) { this.first_name = first_name; }
	public void setLastName(String last_name) { this.last_name = last_name; }
	public void setLocation(Location location) { this.location = location; }
	public void setDisplayName(String display_name) { this.display_name = display_name; }
	public void setEmail(String email) { this.email = email; }
	public void setResearchInterests(String research_interests) { this.research_interests = research_interests; }
	public void setEducation(List<Education> education) { this.education = education; }
	public void setEmployment(List<Employment> employment) { this.employment = employment; }
	public void setAcademicStatus(String academic_status) { this.academic_status = academic_status; }
	public void setLink(String link) { this.link = link; }
	public void setDiscipline(Discipline discipline) { this.discipline = discipline; }
	public void setDisciplines(List<Discipline> disciplines) { this.disciplines = disciplines; }
	public void setPhotos(List<Image> photos) { this.photos = photos; }
	public void setVerified(boolean verified) { this.verified = verified; }
	public void setCreated(String created) { this.created = created; }
	public void setBiography(String biography) { this.biography = biography; }	
	public void setMarketing(boolean marketing) { this.marketing = marketing; }
	
	@Override
	public String toString()
	{
		return "Id: " + id + ", first_name: " + first_name + ", last_name: " + last_name
				+ ", location: " + location + ", display_name: " + display_name +
				", email: " + email + ", researchs: " + research_interests + ", education: " +
				education + ", employment: " + employment + ", academic_status: " +	academic_status
				+ ", link: " + link + ", discipline: " + discipline + ", disciplines: " + disciplines
				+ ", photos: " + photos + ", created: " + created + ", biography: " + biography;
	}
}

