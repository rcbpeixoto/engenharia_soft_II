package br.fbv.cryptosvault.core;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import br.fbv.cryptosvault.model.objects.Message;
import br.fbv.cryptosvault.model.persistence.repositories.MessageRepository;
import br.fbv.cryptosvault.model.util.Constants;
import br.fbv.cryptosvault.model.util.Util;
/**
 * Project: Cryptos Vault
 * Class: SMSBroadcastReceiver
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>   	  Initial draft
 * 30/10/2011    "Rogerio Peixoto" <rcbpeixoto@gmail.com>   	  Code Revision
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SmsMessage sms[] = Util.getIntentContent(intent);
		Message message = Util.searchCryptosVaultCodeInMessage(sms[0].getMessageBody().trim());
		
		if(message != null){
			MessageRepository repository = MessageRepository.getInstance(context);
			List<Message> list = repository.allMessages();
			if(list != null){
				message.setFrom(sms[0].getOriginatingAddress());
				message.setReceivedDate(new Date(System.currentTimeMillis()));
				message.setIcRead(Constants.MESSAGE_IC_NOT_READ);
				
				repository = MessageRepository.getInstance(context);
				repository.insert(message);
			}
			
		}
	}
}
