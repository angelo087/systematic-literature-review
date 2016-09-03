/*
 * Copyright 2011 Nabeel Mukhtar 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */

package mendeley.pfc.schemas;


/**
 * The Class Identifiers.
 */
public class Identifier
{
	private String arxiv;
	private String doi;
	private String isbn;
	private String issn;
	private String pmid;
	private String scopus;
	
	public String getArxiv() { return arxiv; }
	
	public String getDoi() { return doi; }
	
	public String getIsbn() { return isbn; }
	
	public String getIssn() { return issn; }
	
	public String getPmid() { return pmid; }
	
	public String getScopus() { return scopus; }
		
	
	public void setArxiv(String arxiv) { this.arxiv = arxiv; }
	
	public void setDoi(String doi) { this.doi = doi; }
	
	public void setIsbn(String isbn) { this.isbn = isbn; }
	
	public void setIssn(String issn) { this.issn = issn; }
	
	public void setPmid(String pmid) { this.pmid = pmid; }
	
	public void setScopus(String scopus) { this.scopus = scopus; }
	
	public Identifier() {}

	public String toString()
	{
		return "Identifier={arxiv: " + this.arxiv + 
				", doi: " + this.doi + ", isbn: " + this.isbn + 
				", issn: " + this.issn + ", pmid: " + this.pmid + 
				", scopus: " + this.scopus + "}";
	}

}
