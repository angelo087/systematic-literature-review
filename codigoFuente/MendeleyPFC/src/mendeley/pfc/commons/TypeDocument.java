package mendeley.pfc.commons;

public enum TypeDocument 
{
	JOURNAL("journal"), BOOK("book"), GENERIC("generic"), BOOK_SECTION("book_section"), CONFERENCE("conference_proceedings"), WORKING("working_paper"), 
	REPORT("report"), WEB("web_page"), THESIS("thesis"), MAGAZINE("magazine_article"), STATUTE("statute"), PATENT("patent"), NEWSPAPER("newspaper_article"),
	COMPUTER("computer_program"), HEARING("hearing"), TELEVISION("television_broadcast"), ENCYCLOPEDIA("encyclopedia_article"), CASE("case"), FILM("film"), BILL("bill");
	
	private final String key;
	
	TypeDocument(String key)
	{
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static TypeDocument fromKey(String key)
	{
		for(TypeDocument type : TypeDocument.values()) {
            if(type.getKey().equals(key)) {
                 return type;
            }
       }
       return null;
	}
}
