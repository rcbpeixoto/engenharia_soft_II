package br.fbv.cryptosvault.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Project: Cryptos Vault
 * Class: RefreshReceiver
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 30/10/2011    "Rogerio Peixoto" <rcbpeixoto@gmail.com>     Initial Draft
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public abstract class RefreshReceiver extends BroadcastReceiver {
	
	public static final String REFRESH_ACTION = "br.fbv.cryptosvault.REFRESH";
	
	public abstract void refresh();
	
	public RefreshReceiver(){
		super();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(REFRESH_ACTION)) {
			refresh();
		}
	}

}
