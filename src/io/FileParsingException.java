package io;

/**
 * File Parsing Exception
 * @author ZYL
 *
 */
public class FileParsingException extends Exception {

	private static final long serialVersionUID = 5613635213616260928L;

	private String errorMsg;
	
	public FileParsingException(String e) {
		errorMsg = e;
	}
	
	public String toString() {
		return errorMsg;
	}
	
}
