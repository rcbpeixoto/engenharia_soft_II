package br.fbv.cryptosvault.model.objects;

import java.io.Serializable;

/**
 * Project: Cryptos Vault Class: User REVISION HISTORY Date          Developer       							  Comment ----------    -------------------------------------------- ---------------------------------------------- ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>     Initial draft 12/11/2011	 "Rogério Peixoto" <rcbpeixoto@gmail.com>	  Remoção de login inclusão de email ----------    -------------------------------------------- ----------------------------------------------
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 5354257293165811555L;

	/**
	 * Nomes das colunas no banco
	 */
	public static final String ID = "usr_id";
	
	public static final String EMAIL= "usr_email";
	
	public static final String PASSWORD = "usr_password";
	
	/**
	 * Atributos de usuario
	 * @uml.property  name="id"
	 */
	private long id;
	
	private String email;
	
	private String password;
	
	/**
	 * Construtor vazio
	 * 
	 * @author Erivan
	 */
	public User(){
		this.id = 0;
		this.email = "";
		this.password = "";
	}
	
	/**
	 * Contrutor com um parametro, informando o id
	 * 
	 * @author Erivan
	 * @param id
	 */
	public User(long id){
		this.setId(id);
		this.setEmail("");
		this.setPassword("");
	}
	
	/**
	 * Construtor com dois parametros
	 * 
	 * @author Erivan
	 * @param email
	 * @param password
	 */
	public User(String email, String password){
		setEmail(email);
		setPassword(password);
	}

	/**
	 * Construtor com tres parametros
	 * 
	 * @author Erivan
	 * @param login
	 * @param password
	 */
	public User(long id, String login,String password){
		setId(id);
		setEmail(email);
		setPassword(password);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public boolean isReal() {
        return !("".equals(password) || "".equals(email));
    }
	
}