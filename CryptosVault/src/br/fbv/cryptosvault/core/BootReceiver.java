package br.fbv.cryptosvault.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import br.fbv.cryptosvault.model.util.Util;

/**
 * Project: Cryptos Vault
 * Class: BootReceiver
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>     Initial Draft
 * ----------    -------------------------------------------- ----------------------------------------------
 */

public class BootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent it) {
		Util.startServiceWhenNecessary(context);
	}

}
