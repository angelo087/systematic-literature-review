package mendeley.pfc.schemas;

public class Location 
{
	private String name;
	private String latitude;
	private String longitude;
	
	public Location() {}
	
	public String getName() { return name; }
	public String getLatitude() { return latitude; }
	public String getLongitude() { return longitude; }
	
	public void setName(String name) { this.name = name; }
	public void setLatitude(String latitude) { this.latitude = latitude; }
	public void setLongitude(String longitude) { this.longitude = longitude; }
	
	public String toString()
	{
		return "Location [ name: " + name + 
				", latitude: " + latitude + 
				", longitude: " + longitude + "]";
	}
}
