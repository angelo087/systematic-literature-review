package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

public class Annotation 
{
	String id;
	String created;
	String last_modified;
	Color color = new Color();
	String text;
	String privacy_level;
	String document_id; 
	String profile_id;
	String filehash;
	List<Position> positions = new ArrayList<Position>();
	
	public void setId(String id) { this.id = id; }
	public void setCreated(String created) { this.created = created; }
	public void setColor(Color color) { this.color = color; }
	public void setText(String text) { this.text = text; }
	public void setPrivacyLevel(String privacy_level) { this.privacy_level = privacy_level; }
	public void setDocument(String document_id) { this.document_id = document_id; }
	public void setDocument(Document document) { this.document_id = document.getId(); }
	public void setProfile(String profile_id) { this.profile_id = profile_id; }
	public void setProfile(Profile profile) { this.profile_id = profile.getId(); }
	public void setFileHash(String filehash) { this.filehash = filehash; }
	public void setPositions(List<Position> positions) { this.positions = positions; }
	
	public String getId() { return this.id; }
	public String getCreated() { return this.created; }
	public Color getColor() { return this.color; }
	public String getText() { return this.text; }
	public String getPrivacyLevel() { return this.privacy_level; }
	public String getDocument() { return this.document_id; }
	public String getProfile() { return this.profile_id; }
	public String getFileHash() { return this.filehash; }
	public List<Position> getPositions() { return this.positions; }
}
