package br.fbv.cryptosvault.model.exception;

public class InvalidEmailException extends Exception {
	
	private static final long serialVersionUID = 3875075540598429100L;

	public InvalidEmailException(){
		super("Invalid Email");
	}

}
