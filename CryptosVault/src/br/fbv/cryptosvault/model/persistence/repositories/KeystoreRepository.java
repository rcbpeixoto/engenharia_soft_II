package br.fbv.cryptosvault.model.persistence.repositories;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.fbv.cryptosvault.model.objects.KeyStore;
import br.fbv.cryptosvault.model.persistence.DbHelper;

/**
 * Project: Cryptos Vault Class: UserRepository REVISION HISTORY Date          Developer       Comment ----------    -------------------------------------------- ---------------------------------------------- 12/11/2011    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft ----------    -------------------------------------------- ----------------------------------------------
 */

public class KeystoreRepository {
	
	public static final String TABLE_KEYSTORE_NAME = "keystore";

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static KeystoreRepository instance;
	
	private Context context;
	
	private SQLiteDatabase database;
	
	private KeystoreRepository(Context context){
		DbHelper dbHelper = new DbHelper(context);
		this.context = context;
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Metodo do singleton
	 * 
	 * @author Erivan 
	 * @param context
	 * @return Instancia de KeystoreRepository
	 */
	public static KeystoreRepository getInstance(Context context){
		if(instance == null){
			instance = new KeystoreRepository(context);
		}
		return instance;
	}
	
	/**
	 * Insere o objeto KeyStore informado
	 * 
	 * @author Erivan
	 * @param keyStore
	 * @return id registro inserido
	 */
	public long insert(KeyStore keyStore){
		long id = 0;
		
		ContentValues content = new ContentValues();
		
		content.put(KeyStore.PASSWORD, keyStore.getPassword());
		content.put(KeyStore.DESCRIPTION, keyStore.getDescription());
		content.put(KeyStore.CREATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(keyStore.getCreationDate()));
		content.put(KeyStore.EXPIRATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(keyStore.getExpireDate()));
		content.put(KeyStore.USER_ID, keyStore.getUserId());
		
		id = this.database.insert(KeystoreRepository.TABLE_KEYSTORE_NAME, null, content);	
		
		return id;
	}
	
	public void fillKeystoreForTest() {
		for (int i = 0; i < 50; i++) {
			ContentValues content = new ContentValues();
			double matRandom = (Math.random() * 100000000);
			String matString = Double.toString(matRandom);
			content.put(KeyStore.PASSWORD, matRandom);
			content.put(KeyStore.DESCRIPTION, "TESTE DE KEYSTORE");
			content.put(KeyStore.CREATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(new Date()));
			content.put(KeyStore.EXPIRATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(new Date()));
			content.put(KeyStore.USER_ID, 0);
			this.database.insert(KeystoreRepository.TABLE_KEYSTORE_NAME, null, content);
		}
	}
	
	/**
	 * Retorna todos registros da tabela keystore cadastrados no banco
	 * 
	 * @author Erivan
	 * @return ArrayList com os registros
	 */
	public ArrayList<KeyStore> allKeyStore(){
		ArrayList<KeyStore> list = new ArrayList<KeyStore>();
		
		String query = "SELECT * FROM " + TABLE_KEYSTORE_NAME;
		
		Cursor cursor;
		
		try {
			cursor = this.database.rawQuery(query, null);		
		} catch(Exception e){
			this.database.close();
			DbHelper dbHelper = new DbHelper(context);
			database = dbHelper.getWritableDatabase();
			cursor = this.database.rawQuery(query, null);
		}
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			
			boolean out = false;
			while(!out){
				KeyStore keyStore = new KeyStore();
				
				keyStore.setId(cursor.getLong(0));
				keyStore.setUserId(cursor.getLong(1));
				keyStore.setDescription(cursor.getString(2));
				
				try {
					keyStore.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(3)));
					keyStore.setExpireDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(4)));
				} catch (ParseException e) {
					//Um erro ocorreu na convers�o de string do banco
					e.printStackTrace();
				}
				
				keyStore.setPassword(cursor.getBlob(5));
				
				list.add(keyStore);
				
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
	 * Busca o KeyStore com o id do parametro informado, 
	 * caso o id seja 0 procura pelos KeyStore com UserId igual ao do keyStore passado;
	 * 
	 * @author Erivan
	 * @param keyStore
	 * @return Usu�rio encontrado
	 */
	public ArrayList<KeyStore> selectKeyStore(KeyStore keyStore) throws InvalidObjectException{
		ArrayList<KeyStore> list = new ArrayList<KeyStore>();
		
		String where = this.mountWhereClause(keyStore);
		
		String query = "SELECT "
				+ KeyStore.ID + ", "
				+ KeyStore.USER_ID + ", "
				+ KeyStore.DESCRIPTION + ", "
				+ KeyStore.CREATE_DATE + ", "
				+ KeyStore.EXPIRATE_DATE + ", "
				+ KeyStore.PASSWORD + ", "
				+ " FROM " + TABLE_KEYSTORE_NAME;
		if(where != null && !where.equals("")){
			query = query + " " + where;
		}
		
		Cursor cursor = this.database.rawQuery(query, null);
		
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			
			boolean out = false;
			while(!out){
				KeyStore key = new KeyStore();
				
				key.setId(cursor.getLong(0));
				key.setUserId(cursor.getLong(1));
				key.setDescription(cursor.getString(2));
				
				try {
					key.setCreationDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(3)));
					key.setExpireDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(4)));
				} catch (ParseException e) {
					//Um erro ocorreu na convers�o de string do banco
					e.printStackTrace();
				}
				
				key.setPassword(cursor.getBlob(5));
				
				list.add(key);
				
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
	 * Modifica os campos do registro do banco de acordo com o objeto passado
	 * @author Erivan
	 * @param keyStore
	 * @return Quantidade de registros modificados
	 */
	public int update(KeyStore keyStore){
		String where = this.mountWhereClause(keyStore);
		
		ContentValues content = new ContentValues();
		
		content.put(KeyStore.USER_ID, keyStore.getUserId());
		content.put(KeyStore.PASSWORD, keyStore.getPassword());
		content.put(KeyStore.DESCRIPTION, keyStore.getDescription());
		content.put(KeyStore.CREATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(keyStore.getCreationDate()));
		content.put(KeyStore.EXPIRATE_DATE, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(keyStore.getExpireDate()));
		
		return this.database.update(TABLE_KEYSTORE_NAME, content, where, null);
	}
	
	/**
	 * Exclui todos os registros da tabela keyStore
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int deleteAll(){
		int ret = this.database.delete(TABLE_KEYSTORE_NAME, null, null);
		return ret;
	}
	
	/**
	 * Exclui o keystore passado
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int delete(KeyStore keyStore){
		String where = this.mountWhereClause(keyStore);
		int ret = 0;
				
		if(where != null && where.equals("")){
			ret = this.database.delete(TABLE_KEYSTORE_NAME, where, null);
		}
		return ret;
	}
	
	/**
	 * Monta uma clausula where para o objeto especificado
	 * 
	 * @author Erivan
	 * @param keyStore
	 * @return where
	 */
	private String mountWhereClause(KeyStore keyStore){
		String where = "";		
		if(keyStore != null){
			if(keyStore.getId() != 0){
				where = "WHERE " + KeyStore.ID + "=" + keyStore.getId();
			}else{
				if(keyStore.getUserId() != 0){
					where = "WHERE " + KeyStore.USER_ID +"="+ keyStore.getUserId();
				}
			}
		}else{
			throw new NullPointerException("KeyStore null, parametro incorreto");
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
