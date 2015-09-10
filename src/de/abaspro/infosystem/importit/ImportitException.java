package de.abaspro.infosystem.importit;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ImportitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 66964017367429334L;

	public ImportitException() {
		super();

	}

	public ImportitException(String message) {
		super(message);

	}
	/**
	 * 
	 * nimmt eine urspr�ngliche Exception und wird hinter die Message geh�ngt
	 * so gehen beim rethrow keine Information verloren 
	 * @param message Text f�r eigene Exeption
	 * @param e
	 */
	public ImportitException(String message , Exception e) {
		super(message + "\n" + e.getMessage() + "\n" + ExceptionUtils.getStackTrace(e));

	}


}
