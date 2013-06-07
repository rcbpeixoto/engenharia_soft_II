package br.fbv.cryptosvault.model.persistence.repositories;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.fbv.cryptosvault.model.exception.UserNotFoundException;
import br.fbv.cryptosvault.model.objects.User;
import br.fbv.cryptosvault.model.persistence.DbHelper;

/**
 * Project: Cryptos Vault Class: UserRepository REVISION HISTORY Date          Developer       Comment ----------    -------------------------------------------- ---------------------------------------------- 08/11/2011    "Erivan Nogueira" <erivan.spe@gmail.com>     Initial draft 12/11/2011	 "Rogerio Peixoto" <rcbpeixoto@gmail.com>	  Change for singleton ----------    -------------------------------------------- ----------------------------------------------
 */
public class UserRepository {
	
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static UserRepository instance;
	
	public static final String TABLE_USER_NAME = "user";
	
	private SQLiteDatabase database;
	
	private UserRepository(Context context){
		DbHelper dbHelper = new DbHelper(context);
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Metodo do singleton
	 * @author Rogerio Peixoto
	 * @param context
	 * @return
	 */
	public static UserRepository getInstance(Context context){
		if (instance == null) {
			instance = new UserRepository(context);
		}
		return instance;
	}
	
	/**
	 * Insere o usuario passado
	 * 
	 * @author Erivan
	 * @param user
	 * @return id do usuario inserido
	 */
	public long insert(User user){
		long id = 0;
		
		ContentValues content = new ContentValues();
		content.put(User.EMAIL, user.getEmail());
		content.put(User.PASSWORD, user.getPassword());
		
		id = this.database.insert(UserRepository.TABLE_USER_NAME, null, content);	
		
		return id;
	}
	
	/**
	 * Retorna o usuario cadastrado
	 * @author Rogério Peixoto
	 * @return user 
	 */
	public User getUser() throws UserNotFoundException{
		
		User user = new User();
		
		Cursor cursor = this.database.query(TABLE_USER_NAME, 
				new String[]{User.ID, User.EMAIL, User.PASSWORD}, null, null, null, null, null);
		
		if (cursor != null ) {
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				
				user.setId(cursor.getLong(0));
				user.setEmail(cursor.getString(1));
				user.setPassword(cursor.getString(2));
			}else {
				throw new UserNotFoundException();
			}
		}
		if(cursor!= null){
			cursor.close();
		}
		
		return user;
	}
	
	/**
	 * Retorna todos os usuarios cadastrados no banco
	 * 
	 * @author Erivan
	 * @return ArrayList com on usuários
	 */
	public ArrayList<User> allUsers(){
		ArrayList<User> list = new ArrayList<User>();
		
		Cursor cursor = this.database.query(UserRepository.TABLE_USER_NAME, 
				new String[]{User.ID, User.EMAIL, User.PASSWORD}, null, null, null, null, null);
		
		int count = cursor.getCount();
		
		if(cursor != null || count > 0){
			cursor.moveToFirst();
			
			boolean out = false;
			while(!out){
				User user = new User();
				
				user.setId(cursor.getLong(0));
				user.setEmail(cursor.getString(1));
				user.setPassword(cursor.getString(2));
				
				list.add(user);
				
				if(cursor.isLast()){
					out = true;					
				}else{
					cursor.moveToNext();
				}				
				
			}			
			
		}
		if(cursor!= null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * Busca pelo usuario cujo email 
	 * corresponda ao email informado;
	 * 
	 * @author Erivan
	 * @param user
	 * @return Usuario encontrado
	 */
	public User selectUser(String email) throws InvalidObjectException{
		User userFound = new User();
		
			String where = this.mountWhereClause(new User(email, ""));
		
			//executa pesquisa
			Cursor cursor = this.database.query(UserRepository.TABLE_USER_NAME, new String[]{User.ID, User.EMAIL, User.PASSWORD}, 
					where, null, null, null, null);
			
			userFound.setId(cursor.getLong(0));
			userFound.setEmail(cursor.getString(1));
			userFound.setPassword(cursor.getString(2));
		
			if(cursor!= null){
				cursor.close();
			}
		return userFound;
	}
	
	/**
	 * Busca pelo usuario correspondente ao id informado
	 * 
	 * @author Erivan
	 * @param id
	 * @return Usuario encontrado
	 */
	public User selectUser(long id) throws InvalidObjectException{
		User userFound = null;
		String query = "SELECT "
				+ User.ID + ", "
				+ User.EMAIL + ", "
				+ User.PASSWORD + " ";
		
		String where = this.mountWhereClause(new User(id));
		
		if(where != null && !where.equals("")){
			query = query + where;
			Cursor cursor = this.database.rawQuery(query, null);
			
			if(cursor != null && cursor.getCount() > 0){
				//Inicia userFound com os valores encontrados;
				userFound = new User(cursor.getLong(0), 
						cursor.getString(1), 
						cursor.getString(2));				
			}
			if(cursor!= null){
				cursor.close();
			}
		}
		return userFound;
	}
	
	/**
	 * Atualiza o usuario informado
	 * 
	 * @author Erivan
	 * @param user
	 * @return Numero de registros afetados
	 */
	public int update(User user){
		String where = this.mountWhereClause(user);
		
		ContentValues content = new ContentValues();
		
		content.put(User.EMAIL, user.getEmail());
		content.put(User.PASSWORD, user.getPassword());
		
		return this.database.update(TABLE_USER_NAME, content, where, null);
	}
	
	/**
	 * Exclui todos os registros da tabela usuario
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int deleteAll(){
		int ret = this.database.delete(TABLE_USER_NAME, null, null);
		return ret;
	}
	
	/**
	 * Exclui o usuario passado
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int delete(User user){
		
		int ret = 0;
		String where = this.mountWhereClause(user);		
		
		if(where != null && where.equals("")){
			ret = this.database.delete(TABLE_USER_NAME, where, null);
		}
		return ret;
	}
	
	private String mountWhereClause(User user){
		String where = "";
		
		if(user.getId() != 0){
			
			if(user.getEmail() != null){
				where = where + User.EMAIL + "=" + user.getEmail().trim() 
						+ "," + User.ID + "=" + user.getId();
			}else{
				where = where + User.ID + "=" + user.getId();
			}
		}else{
			if(user.getEmail() != null){
				where = where + User.EMAIL + "=" + user.getEmail().trim();
			}
		}
		return where;
	}
	
	/**
	 * Fhecha a conexao com o banco
	 * 
	 * @author Erivan
	 */
	public void closeDatabase(){
		this.database.close();
	}
}
