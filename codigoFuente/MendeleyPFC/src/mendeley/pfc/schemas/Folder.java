package mendeley.pfc.schemas;

public class Folder 
{
	// Atributos
	private String id;
	private String name;
	private String parent_id;
	private String group_id;
	private String created; //Fecha de creacion
	private String modified; //Fecha de modificacion
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParent() {
		return parent_id;
	}
	
	public void setParent(String parent) {
		this.parent_id = parent;
	}
	
	public String getCreatedDate()
	{
		return this.created;
	}
	
	public void setCreatedDate(String date)
	{
		this.created = date;
	}
	
	public String getModifiedDate()
	{
		return modified;
	}
	
	public void setModifiedDate(String date)
	{
		this.modified = date;
	}
	
	public String getGroup()
	{
		return this.group_id;
	}
	
	public void setGroup(String group)
	{
		this.group_id = group;
	}

	public boolean isSubFolder()
	{
		if(parent_id == null || parent_id.equals(""))
		{
			return false;
		}
		return true;
	}
}
