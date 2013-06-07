package br.fbv.cryptosvault.model.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Project: Cryptos Vault Class: KeyStore REVISION HISTORY Date          Developer       							  Comment ----------    -------------------------------------------- ---------------------------------------------- ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>     Initial draft ----------    -------------------------------------------- ----------------------------------------------
 */
public class KeyStore implements Serializable {
	
	private static final long serialVersionUID = 7455963665242566167L;

	/**
	 * Nomes das colunas no banco
	 */
	public static final String ID = "kst_id";
	
	public static final String PASSWORD = "kst_password";
	
	public static final String CREATE_DATE = "kst_create_date";
	
	public static final String EXPIRATE_DATE = "kst_exp_date";
	
	public static final String DESCRIPTION = "kst_label";
	
	public static final String USER_ID = "usr_id";
	
	/**
	 * Atributos
	 * @uml.property  name="id"
	 */
	private long id;
	
	/**
	 * @uml.property  name="userId"
	 */
	private long userId;
	
	/**
	 * @uml.property  name="password"
	 */
	private byte[] password;
	
	private Date creationDate;
	
	private Date expireDate;
	
	private String description;
	

	/**
	 * COnstrutor vazio
	 */
	public KeyStore(){
		
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


	/**
	 * @return
	 * @uml.property  name="userId"
	 */
	public long getUserId() {
		return userId;
	}


	/**
	 * @param userId
	 * @uml.property  name="userId"
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}


	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public byte[] getPassword() {
		return password;
	}


	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
}