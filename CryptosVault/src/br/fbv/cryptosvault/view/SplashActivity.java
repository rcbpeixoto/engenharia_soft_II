package br.fbv.cryptosvault.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.util.Constants;
import br.fbv.cryptosvault.model.util.Util;

/**
 * Project: Cryptos Vault
 * Class: SplashActivity
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 12/11/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>     Code Revision
 * ----------    -------------------------------------------- ----------------------------------------------
 */

public class SplashActivity extends Activity implements Runnable{
	
	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			this.toast = Util.makeToast(this, getString(R.string.sd_mounted), this.toast);
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			this.toast = Util.makeToast(this, getString(R.string.sd_mounted), this.toast);
		}
		else if (Environment.MEDIA_UNMOUNTED.equals(state)){
			this.toast = Util.makeToast(this, getString(R.string.sd_mounted), this.toast);
		}
		
		Handler handler = new Handler();
		handler.postDelayed(this, Constants.DELAY);
	}
	
	@Override
	public void run() {
		finish();
	}
}
