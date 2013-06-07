package br.fbv.cryptosvault.model.persistence.repositories;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.fbv.cryptosvault.model.objects.Message;
import br.fbv.cryptosvault.model.persistence.DbHelper;

/**
 * Project: Cryptos Vault Class: MessageRepository REVISION HISTORY Date          Developer       Comment ----------    -------------------------------------------- ---------------------------------------------- 12/11/2011    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft ----------    -------------------------------------------- ----------------------------------------------
 */

public class MessageRepository {
	
	public static final String TABLE_MESSAGE_NAME = "message";

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static MessageRepository instance;
	
	private SQLiteDatabase database;
	
	/**
	 * Construtor privado usado na criacao da instancia da classe;
	 * 
	 * @author Erivan
	 * @param context
	 */
	private MessageRepository(Context context){
		DbHelper dbHelper = new DbHelper(context);
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Metodo do singleton
	 * 
	 * @author Erivan 
	 * @param context
	 * @return Instancia de MessageRepository
	 */
	public static MessageRepository getInstance(Context context){
		if(instance == null){
			instance = new MessageRepository(context);
		}
		return instance;
	}
	
	/**
	 * Insere o objeto KeyStore informado
	 * 
	 * @author Erivan
	 * @param message
	 * @return id registro inserido
	 */
	public long insert(Message message){
		long id = 0;
		
		ContentValues content = new ContentValues();
		
		content.put(Message.USER_ID, message.getUserId());
		content.put(Message.CONTENT, message.getContent());
		content.put(Message.MESSAGE_DATE_RECEIVED, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(message.getReceivedDate()));
		content.put(Message.MESSAGE_FROM, message.getFrom());
		content.put(Message.INDICATOR_READ, message.getIcRead());
		content.put(Message.MESSAGE_TYPE, message.getType());
		
		id = this.database.insert(TABLE_MESSAGE_NAME, null, content);	
		
		return id;
	}
	
	/**
	 * Retorna todos registros da tabela message cadastrados no banco
	 * 
	 * @author Erivan
	 * @return ArrayList com os registros
	 */
	public ArrayList<Message> allMessages(){
		ArrayList<Message> list = new ArrayList<Message>();
		
		String query = "SELECT "
				+ Message.ID + ", "
				+ Message.USER_ID + ", "
				+ Message.CONTENT + ", "
				+ Message.MESSAGE_FROM + ", "
				+ Message.MESSAGE_DATE_RECEIVED + ", "
				+ Message.MESSAGE_TYPE + ", "
				+ Message.INDICATOR_READ + ", "
				+ " FROM " + TABLE_MESSAGE_NAME;
		
		Cursor cursor = this.database.rawQuery(query, null);		
		
		
		
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			
			boolean out = false;
			while(!out){
				Message message = new Message();
				
				message.setId(cursor.getLong(0));
				message.setUserId(cursor.getLong(1));
				message.setContent(cursor.getString(2));
				message.setFrom(cursor.getString(3));
				try {
					message.setReceivedDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(4)));
				} catch (ParseException e) {
					//Um erro ocorreu na conversao de string do banco
					e.printStackTrace();
				}
				message.setType(cursor.getLong(5));
				message.setIcRead(cursor.getLong(6));
				
				list.add(message);
				
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
	 * Busca o Message com o id do parametro informado, 
	 * caso o id seja 0 procura pelos Message com UserId igual ao do Message passado;
	 * 
	 * @author Erivan
	 * @param message
	 * @return Mensagem encontrada
	 */
	public ArrayList<Message> selectMessage(Message message) throws InvalidObjectException{
		ArrayList<Message> list = new ArrayList<Message>();
		
		String where = this.mountWhereClause(message);
		
		String query = "SELECT "
				+ Message.ID + ", "
				+ Message.USER_ID + ", "
				+ Message.CONTENT + ", "
				+ Message.MESSAGE_FROM + ", "
				+ Message.MESSAGE_DATE_RECEIVED + ", "
				+ Message.MESSAGE_TYPE + ", "
				+ Message.INDICATOR_READ + ", "
				+ " FROM " + TABLE_MESSAGE_NAME;
		
		if(where != null && !where.equals("")){
			query = query + " " + where;
		}
		
		Cursor cursor = this.database.rawQuery(query, null);
		
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			
			boolean out = false;
			while(!out){
				Message messageReturn = new Message();
				
				messageReturn.setId(cursor.getLong(0));
				messageReturn.setUserId(cursor.getLong(1));
				messageReturn.setContent(cursor.getString(2));
				messageReturn.setFrom(cursor.getString(3));
				try {
					messageReturn.setReceivedDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").parse(cursor.getString(4)));
				} catch (ParseException e) {
					//Um erro ocorreu na conversao de string do banco
					e.printStackTrace();
				}
				messageReturn.setType(cursor.getLong(5));
				messageReturn.setIcRead(cursor.getLong(6));
				
				list.add(messageReturn);
				
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
	 * @param message
	 * @return Quantidade de registros modificados
	 */
	public int update(Message message){
		String where = this.mountWhereClause(message);
		
		ContentValues content = new ContentValues();
		
		content.put(Message.USER_ID, message.getUserId());
		content.put(Message.CONTENT, message.getContent());
		content.put(Message.MESSAGE_FROM, message.getFrom());
		content.put(Message.MESSAGE_DATE_RECEIVED, new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(message.getReceivedDate()));
		content.put(Message.MESSAGE_TYPE, message.getType());
		content.put(Message.INDICATOR_READ, message.getIcRead());
		
		return this.database.update(TABLE_MESSAGE_NAME, content, where, null);
	}
	
	/**
	 * Exclui todos os registros da tabela message
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int deleteAll(){
		int ret = this.database.delete(TABLE_MESSAGE_NAME, null, null);
		return ret;
	}
	
	/**
	 * Exclui a mensagem passado
	 * 
	 * @author Erivan
	 * @return Numero de linhas afetadas
	 */
	public int delete(Message message){
		String where = this.mountWhereClause(message);
		int ret = 0;
				
		if(where != null && where.equals("")){
			ret = this.database.delete(TABLE_MESSAGE_NAME, where, null);
		}
		return ret;
	}
	
	/**
	 * Monta uma clausula where para o objeto especificado
	 * 
	 * @author Erivan
	 * @param message
	 * @return where
	 */
	private String mountWhereClause(Message message){
		String where = "";
		boolean previous = false;
		if(message != null){
			if(message.getId() != 0){
				where = "WHERE " + Message.ID + "=" + message.getId();
			}else{
				//Inclui id do usuario na clausula where caso tenha sido setada no objeto message
				if(message.getUserId() != 0){
					where = "WHERE " + Message.USER_ID +"="+ message.getUserId();
					previous = true;
				}
				//Inclui o remetente na clausula where caso tenha sido setado no objeto message
				if(message.getFrom() != null && !message.getFrom().equals("")){
					if(previous){
						where = where + ", " + Message.MESSAGE_FROM + "=" + message.getFrom();
					}else{
						where = "WHERE " + Message.MESSAGE_FROM +"="+ message.getFrom();
						previous = true;
					}
				}
				//Inclui tipo da mensagem na clausula where caso tenha sido setado no objeto message
				if(message.getType() != 0){
					if(previous){
						where = where + ", " + Message.MESSAGE_TYPE + "=" + message.getType();
					}else{
						where = "WHERE " + Message.MESSAGE_TYPE +"="+ message.getType();
						previous = true;
					}
				}
				//Inclui indicador de mensagem lida na clausula where caso tenha sido setado no objeto message
				if(message.getIcRead() != 0){
					if(previous){
						where = where + ", " + Message.INDICATOR_READ + "=" + message.getIcRead();
					}else{
						where = "WHERE " + Message.INDICATOR_READ +"="+ message.getIcRead();
						previous = true;
					}
				}
				//Inclui data de recebimento da mensagem na clausula where caso tenha sido setado no objeto message
				if(message.getReceivedDate() != null){
					if(previous){
						where = where + ", " + Message.MESSAGE_DATE_RECEIVED + "=" 
								+ new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(message.getReceivedDate());
					}else{
						where = "WHERE " + Message.MESSAGE_DATE_RECEIVED +"="
								+ new SimpleDateFormat("dd/MM/yyyy HH:mm:SS").format(message.getReceivedDate());
						previous = true;
					}
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
