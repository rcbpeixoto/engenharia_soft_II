package br.fbv.cryptosvault.core;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.util.Constants;
import br.fbv.cryptosvault.view.LoginActivity;
import br.fbv.cryptosvault.view.TabHostActivity;

/**
 * Project: Cryptos Vault Class: Notification Service REVISION HISTORY Date          Developer       							  Comment ----------    -------------------------------------------- ---------------------------------------------- 30/10/2011    "Rogerio Peixoto" <rcbpeixoto@gmail.com>     Initial Draft ----------    -------------------------------------------- ----------------------------------------------
 */
public class NotificationService extends Service {
	
	private int NOTIFICATION_KEY = R.string.app_name;
	
	private static Timer timer = null;
	private static TimerTask timerTask = null;
	/**
	 * @uml.property  name="refreshReceiver"
	 * @uml.associationEnd  
	 */
	private RefreshReceiver refreshReceiver;
	private NotificationManager notificationManager;
	
	public class NotificationServiceBinder extends Binder {
		NotificationService getService() {
			return NotificationService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	public NotificationService(){
		refreshReceiver = new RefreshReceiver() {
			@Override
			public void refresh() {
				showNotification();
			}
		};
	}
	/**
	 * Responsavel pela criação do serviço que atualizara a notificacao do cryptos
	 * @date 30/10/2011
	 * @author Rogerio Peixoto
	 * 
	 */
	@Override
	public void onCreate() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		if(timer != null) {
			timer.cancel();
			timer.purge();
		}
		
		timerTask = new TimerTask() {
			@Override
			public void run() {
				showNotification();
			}
		};
		
		showNotification();
		
		timer = new Timer();
		timer.schedule(timerTask, 0, Constants.NOTIFICATION_REFRESH_INTERVAL);
		
		IntentFilter refreshFilter = new IntentFilter(RefreshReceiver.REFRESH_ACTION);
		registerReceiver(refreshReceiver, refreshFilter);
	}
	
	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = null;
		timerTask = null;
		notificationManager.cancel(NOTIFICATION_KEY);
		unregisterReceiver(refreshReceiver);
	}
	
	/**
	 * Monta a notificacao permanente a ser exibida
	 * @author Rogerio Peixoto
	 */
	public void showNotification() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.ic_lock_lock, null, System.currentTimeMillis());
		Intent it = new Intent(this, TabHostActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, it, 0);
		
		notification.setLatestEventInfo(this, getApplicationContext().getText(R.string.app_name) , null, contentIntent);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		
		notificationManager.notify(NOTIFICATION_KEY, notification);
	}

}
