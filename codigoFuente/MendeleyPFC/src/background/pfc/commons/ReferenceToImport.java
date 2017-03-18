package background.pfc.commons;

import background.pfc.enums.TypeReferenceId;

public class ReferenceToImport {

	private String title;
	private TypeReferenceId typeReferenceId;
	private String id;
	
	public ReferenceToImport(String title, TypeReferenceId typeReferenceId,
			String id) {
		
		this.title = title;
		this.typeReferenceId = typeReferenceId;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TypeReferenceId getTypeReferenceId() {
		return typeReferenceId;
	}

	public void setTypeReferenceId(TypeReferenceId typeReferenceId) {
		this.typeReferenceId = typeReferenceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
