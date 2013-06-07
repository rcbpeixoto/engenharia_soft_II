package br.fbv.cryptosvault.model.exception;


/**
 * Project: Cryptos Vault
 * Class: UserNotFoundException
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 12/11/2011	 "Rogério Peixoto" <rcbpeixoto@gmail.com>	  Initial Draft
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class UserNotFoundException extends Exception {
	
	private static final long serialVersionUID = 156783033434630886L;

	public UserNotFoundException(){
		super("Usuário não cadastrado");
	}
}
