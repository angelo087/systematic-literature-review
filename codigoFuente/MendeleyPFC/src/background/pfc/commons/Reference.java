package background.pfc.commons;

import background.pfc.enums.TypeEngineSearch;
import background.pfc.enums.TypeReferenceId;

/**
 * Clase Reference, es la encargada de recoger cada una de las referencias que son importadas
 * a Mendeley junto con su identificador correspondiente en Mendeley.
 * 
 * @author agonzatoro
 *
 */
public class Reference {
	
	/** url.*/
	private String codeReference;
	/** idMendeley.*/
	private String idMendeley = "";
	/** notesCont.*/
	private String notesCont = "";
	/** typeEngineSearch.*/
	private TypeEngineSearch typeEngineSearch;
	/** idFolderEngine.*/
	private String idFolderEngine = "";
	/** typeReferenceId.*/
	private TypeReferenceId typeReferenceId;
	
	/**
	 * Constructor de la clase Reference.
	 * 
	 * @param url String
	 * @param idmendeley String
	 * @param notesCont String
	 * @param typeEngineSearch TypeEngineSearch
	 */
	public Reference(String codeReference, String idmendeley, String notesCont, TypeEngineSearch typeEngineSearch)
	{
		this.codeReference = codeReference;
		this.idMendeley = idmendeley;
		this.typeEngineSearch = typeEngineSearch;
		this.notesCont = notesCont;
	}
	
	/**
	 * Constructor de la clase Reference.
	 * 
	 * @param url String
	 * @param notesCont String
	 * @param typeEngineSearch TypeEngineSearch
	 */
	public Reference(String codeReference, String notesCont, TypeEngineSearch typeEngineSearch)
	{
		this.codeReference = codeReference;
		this.idMendeley = "";
		this.typeEngineSearch = typeEngineSearch;
		this.notesCont = notesCont;
	}
	
	/**
	 * M�todo que devuelve el identificador de la referencia en Mendeley.
	 * 
	 * @return String
	 */
	public String getIdMendeley() { return idMendeley; }
	
	/**
	 * M�todo que devuelve el codeReference de la referencia.
	 * 
	 * @return String
	 */
	public String getCodeReference() { return codeReference; }
	
	/**
	 * M�todo que devuelve el TypeEngineSearch de la referencia.
	 * @return String
	 */
	public TypeEngineSearch getTypeEngineSearch() { return typeEngineSearch; }

	/**
	 * M�todo que devuelve las notas asociadas a la referencia.
	 * 
	 * @return String
	 */
	public String getNotesCont() {
		return notesCont;
	}

	/**
	 * M�todo que establece las notas asociadas a la referencia.
	 * 
	 * @param notesCont String
	 */
	public void setNotesCont(String notesCont) {
		this.notesCont = notesCont;
	}

	/**
	 * M�todo que establece la url de donde se importa la referencia.
	 * 
	 * @param url String
	 */
	public void setCodeReference(String codeReference) {
		this.codeReference = codeReference;
	}

	/**
	 * M�todo que establece el identificador de Mendeley de la referencia importada.
	 * 
	 * @param idMendeley String
	 */
	public void setIdMendeley(String idMendeley) {
		this.idMendeley = idMendeley;
	}

	/**
	 * M�todo que establce el TypeEngineSearch asociado a la referencia importada.
	 * 
	 * @param typeEngineSearch TypeEngineSearch
	 */
	public void setTypeEngineSearch(TypeEngineSearch typeEngineSearch) {
		this.typeEngineSearch = typeEngineSearch;
	}

	/**
	 * M�todo que devuelve el identificador de la carpeta donde est� contenida la referencia.
	 * 
	 * @return String
	 */
	public String getIdFolderEngine() {
		return idFolderEngine;
	}

	/**
	 * M�todo que establece el identificador de la carpeta donde est� contenida la referencia.
	 * 
	 * @param idFolderEngine String
	 */
	public void setIdFolderEngine(String idFolderEngine) {
		this.idFolderEngine = idFolderEngine;
	}

	public TypeReferenceId getTypeReferenceId() {
		return typeReferenceId;
	}

	public void setTypeReferenceId(TypeReferenceId typeReferenceId) {
		this.typeReferenceId = typeReferenceId;
	}	
}