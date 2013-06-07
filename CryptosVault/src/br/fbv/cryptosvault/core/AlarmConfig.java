package br.fbv.cryptosvault.core;

/**
 * Project: Cryptos Vault
 * Class: Constants
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * 30/10/2011    "Erivan Sousa" <erivan.spe@gmail.com>     Initial Draft
 * ----------    -------------------------------------------- ----------------------------------------------
 */

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmConfig {

	private Context context;

	private AlarmManager alarm;

	/**
	 * Construtor
	 *  @author Erivan
	 *  @param context
	 */
	public AlarmConfig(Context context) {
		this.context = context;
		this.alarm = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
	}

	/**
	 * Agenda o alarme para a data passada por parametro, lancando a intent
	 * especificada;
	 * 
	 * @author Erivan
	 * @param date
	 * @param intent
	 */
	public void alarmSchedule(Date date, Intent intent) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, 0);
		
		this.alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);		
	}

}
