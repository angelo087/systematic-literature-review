package mendeley.pfc.commons;

public class MendeleyUrl 
{
	// FOLDERS
	public static final String FOLDER_LIST_ALL = "https://api.mendeley.com/folders?limit={limit}";
	public static final String FOLDER_CREATE   = "https://api.mendeley.com/folders/";
	public static final String FOLDER_DELETE   = "https://api.mendeley.com/folders/{id}";
	public static final String FOLDER_LIST_DOCUMENTS = "https://api.mendeley.com/folders/{id}/documents?limit={limit}";
	public static final String FOLDER_ADD_DOCUMENT = "https://api.mendeley.com/folders/{id}/documents";
	public static final String FOLDER_REMOVE_DOCUMENT = "https://api.mendeley.com/folders/{id}/documents/{document_id}";
	public static final String FOLDER_CREATE_SUBFOLDER = "https://api.mendeley.com/folders/";
	public static final String FOLDER_UPDATE           = "https://api.mendeley.com/folders/{id}";
	
	// DOCUMENTS
	public static final String DOCUMENT_DETAILS = "https://api.mendeley.com/documents/{id}?view=all";
	public static final String DOCUMENT_BIBTEXT = "https://api.mendeley.com/documents/{id}?view=bib";
	public static final String DOCUMENT_LIST_ALL = "https://api.mendeley.com/documents";
	public static final String DOCUMENT_LIST_ALL_BIBTEXT = "https://api.mendeley.com/documents?view=bib";
	public static final String DOCUMENT_UPDATE  = "https://api.mendeley.com/documents/{id}";
	public static final String DOCUMENT_CREATE  = "https://api.mendeley.com/documents";
	public static final String DOCUMENT_DELETE  = "https://api.mendeley.com/documents/{id}";
	
	// PROFILES
	public static final String PROFILE_DETAILS = "https://api.mendeley.com/profiles/{id}";
	public static final String PROFILE_CURRENT_DETAILS = "https://api.mendeley.com/profiles/me";
	public static final String PROFILE_UPDATE = "https://api.mendeley.com/profiles/me";
	
	// ANNOTATIONS
	public static final String ANNOTATION_LIST_ALL = "https://api.mendeley.com/annotations";
	public static final String ANNOTATION_CREATE = "https://api.mendeley.com/annotations";
	public static final String ANNOTATION_UPDATE = "https://api.mendeley.com/annotations/{id}";
	public static final String ANNOTATION_DELETE = "https://api.mendeley.com/annotations/{id}";
	
	// CATALOG DOCUMENT
	public static final String CATALOG_DETAILS_ALL = "https://api.mendeley.com/catalog/{id}?view=all";
	public static final String CATALOG_DETAILS_BY_DOI = "https://api.mendeley.com/catalog?doi={id}";
	public static final String CATALOG_DETAILS_BY_ISBN = "https://api.mendeley.com/catalog?isbn={id}?view=all";
	public static final String CATALOG_DETAILS_BY_ISSN = "https://api.mendeley.com/catalog?issn={id}?view=all";
	public static final String CATALOG_DETAILS_BY_ARXIV = "https://api.mendeley.com/catalog?arxiv={id}?view=all";
	public static final String CATALOG_DETAILS_BY_PMID = "https://api.mendeley.com/catalog?pmid={id}?view=all";
}
