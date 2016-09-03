package mendeley.pfc.commons;

public class MendeleyException extends Exception {
	public MendeleyException() { super(); }
	public MendeleyException(String message) { super(message); }
	public MendeleyException(String message, Throwable cause) { super(message, cause); }
	public MendeleyException(Throwable cause) { super(cause); }
}
