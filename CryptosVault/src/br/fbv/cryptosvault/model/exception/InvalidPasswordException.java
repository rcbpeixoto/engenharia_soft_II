package br.fbv.cryptosvault.model.exception;


public class InvalidPasswordException extends Exception {
	
	private static final long serialVersionUID = 7986005834622955905L;

	public InvalidPasswordException(){
		super("Invalid Password!");
	}
}
