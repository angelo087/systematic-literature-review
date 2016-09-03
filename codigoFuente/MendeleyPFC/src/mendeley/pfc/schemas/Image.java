package mendeley.pfc.schemas;

public class Image 
{
	private int width;
	private int height;
	private boolean original = false;
	private String url;
	
	public Image() {}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public boolean isOriginal() { return original; }
	public String getUrl() { return url; }
	
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public void setOriginal(boolean original) { this.original = original; }
	public void setUrl(String url) { this.url = url; }
	
	public String toString()
	{
		return "Image[width=" + width + ", height=" + height + ", original=" + original + ", url=" + url + "]";
	}
}
