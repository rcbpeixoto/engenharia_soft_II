package br.fbv.cryptosvault.model.util;

/**
 * Project: Cryptos Vault
 * Class: Constants
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 30/10/2011    "Rogerio Peixoto" <rcbpeixoto@gmail.com>     Initial Draft
 * 12/11/2011	 "Erivan Nogueira" <erivan.spe@gmail.com>	  Message Constants
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class Constants {
	
	/*	Application Constants  */
	
	public static final String SHARED_PREFERENCES_NAME = "CRYPTOS_VAULT_PREFERENCES";
	public static final String RUN_ON_BOOT_PREFERENCE = "RUN_ON_BOOT";
	public static final int NOTIFICATION_REFRESH_INTERVAL = 2500;
	
	/* Message Constants */
	
	public static final int MESSAGE_TYPE_MORSE = 1;
	public static final int MESSAGE_TYPE_ENCRYPTED = 2;
	public static final int MESSAGE_IC_READ = 1;
	public static final int MESSAGE_IC_NOT_READ = 2;
	
	/* Activities Constants */
	
	public static final String DROID_SANS_FONT = "fonts/DROIDSANS.TTF";
	public static final String ZERO_THREE_FONT = "fonts/ZEROTHREE.TTF";
	public static final int DELAY = 3000;
	public static final int PASSWORD_MAX_LENGTH = 8;
}
