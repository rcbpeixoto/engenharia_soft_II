package br.fbv.cryptosvault.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Project: Cryptos Vault
 * Class: DbHelper
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 30/10/2011    "Rogerio Peixoto" <rcbpeixoto@gmail.com>     Initial Draft
 * 31/10/2011	 "Erivan Nogueira" <erivan.spe@gmail.com>	  Insercao do script de de criacao do DB
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class DbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "cryptosVaultDb";
	
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * Script de criacao do banco
	 */
	private static final String[] SCRIPT_CREATE_DATABASE = {"CREATE TABLE IF NOT EXISTS [user] ("
			+ "usr_email VARCHAR(45), "
			+ "usr_password BLOB, "
			+ "usr_id INTEGER NOT NULL ON CONFLICT FAIL PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT ); "
			,

			"CREATE TABLE IF NOT EXISTS [keystore] ("
			+ "[kst_password] BLOB, "
			+ "[kst_create_date] TIMESTAMP NOT NULL ON CONFLICT FAIL, "
			+ "[kst_exp_date] TIMESTAMP DEFAULT (null), "
			+ "[kst_label] VARCHAR(150), "
			+ "[usr_id] INTEGER NOT NULL REFERENCES "
			+ "[user]([usr_id]) ON DELETE NO ACTION ON UPDATE NO ACTION, "
			+ "[kst_id] INTEGER NOT NULL ON CONFLICT FAIL PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT); "
			,

			"CREATE TABLE IF NOT EXISTS [message] ("
			+ "[msg_type] INTEGER NOT NULL ON CONFLICT FAIL,"
			+ "[msg_content] VARCHAR(400) DEFAULT (null), "
			+ "[msg_date] TIMESTAMP NOT NULL ON CONFLICT FAIL, "
			+ "[msg_from] VARCHAR(45) NOT NULL ON CONFLICT FAIL,"
			+ "[usr_id] INTEGER REFERENCES [user]([usr_id]) ON DELETE NO ACTION ON UPDATE NO ACTION, "
			+ "[msg_id] INTEGER NOT NULL ON CONFLICT FAIL PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT, "
			+ "[msg_indlida] INTEGER NOT NULL ON CONFLICT FAIL);"
			
			};

	public DbHelper(Context context, String databaseName, CursorFactory factory,
			int version) {
		super(context, databaseName, factory, version);
	}
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < SCRIPT_CREATE_DATABASE.length; i++) {
			db.execSQL(SCRIPT_CREATE_DATABASE[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	@Override
	public synchronized void close() {
		super.close();
	}


}
