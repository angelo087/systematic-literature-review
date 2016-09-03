package mendeley.pfc.schemas;

import java.util.ArrayList;
import java.util.List;

public class Library 
{
	int limit = -1;
	List<String> document_ids = new ArrayList<String>();
	
	public Library(String resourceResponse)
	{
		if(!resourceResponse.equals("[]"))
		{
			String[] ids = resourceResponse.split(",");
			
			for(String id : ids)
			{
				document_ids.add(id.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "")
								 .replaceAll("\"", "").replaceAll("id:", ""));
			}
		}
	}
	
	public Library(String resourceResponse, int limit)
	{
		this.limit = limit;
		
		String[] ids = resourceResponse.split(",");
		
		for(String id : ids)
		{
			document_ids.add(id.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "")
							 .replaceAll("\"", "").replaceAll("id:", ""));
		}
	}
	
	public List<String> getDocumentsIds() { return document_ids; }
	
	public void setDocumentsIds(List<String> document_ids) { this.document_ids = document_ids; }
	
	public int getLimit() { return limit; }
	
	public void setLimit(int limit) { this.limit = limit; }
}
