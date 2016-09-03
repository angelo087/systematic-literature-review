package mendeley.pfc.schemas;

public class Position 
{
	String page = "1";
	CoordinatesTopLeft top_left = new CoordinatesTopLeft();
	CoordinatesBottomRight bottom_right = new CoordinatesBottomRight();
	
	public String getPage() { return this.page; }
	public CoordinatesTopLeft getCoordinatesTopLeft() { return this.top_left; }
	public CoordinatesBottomRight getCoordinatesBottomLeft() { return this.bottom_right; }
	
	public void setPage(String page) { this.page = page; }
	public void setCoordinatesTopLeft(CoordinatesTopLeft top_left) { this.top_left = top_left; }
	public void setCoordinatesBottomRight(CoordinatesBottomRight bottom_right) { this.bottom_right = bottom_right; }
}
