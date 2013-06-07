package br.fbv.cryptosvault.model.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.fbv.cryptosvault.core.NotificationService;
import br.fbv.cryptosvault.model.objects.Message;
import br.fbv.cryptosvault.model.security.Crypter;
/**
 * Project: Cryptos Vault
 * Class: Util
 * 
 * REVISION HISTORY
 * 
 * Date          Developer       							  Comment
 * ----------    -------------------------------------------- ----------------------------------------------
 * ##/##/####    "Erivan Nogueira" <erivan.spe@gmail.com>     Initial draft
 * 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>     Code Revision
 * 13/11/2011	 "Rogerio Peixoto" <rcbpeixoto@gmail.com>	  setTextViewFont() adicionado  
 * ----------    -------------------------------------------- ----------------------------------------------
 */
public class Util {
	
    private static final InputFilter noInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start,
                int end, Spanned dest, int dstart, int dend) {
            return "";
        }
    };
	
	/**
	 * Inclui delimitadores que identificam os limmites do conteudo em codigo
	 * morse
	 * 
	 * @author Erivan
	 * @param message
	 * @return String
	 */
	public static String formatCryptosVaultCodeMensageToSend(String message) {
		return "%&" + message.trim() + "%&";
	}

	/**
	 * Busca os delimitadores de codigo gerado pelo aplicativo CriptosVault na mensagem e valida o conteudo
	 * entre os delimitadores;
	 * 
	 * @author Erivan
	 * @param message
	 * @return String
	 */
	public static Message searchCryptosVaultCodeInMessage(String message) {
		
		int firstInd = message.indexOf("%&");
		int lastInd = message.indexOf("%&", firstInd+2);
		
		String messageFound = null;
		Message retMessage = null;
		
		if(firstInd != -1 && lastInd != -1){
			messageFound = message.substring(firstInd+2, lastInd);
		}
		
		if(messageFound != null && !messageFound.equals("")){
			retMessage = new Message();
			retMessage.setContent(messageFound);
			if(MorseCode.validateMorseCode(messageFound)){
				retMessage.setType(Constants.MESSAGE_TYPE_MORSE);
			}else{
				retMessage.setType(Constants.MESSAGE_TYPE_ENCRYPTED);
			}
			
		}
		return retMessage;
	}
	
	/**
	 * Envia por SMS a String informada para o número passado por parametro
	 * 
	 * @author Erivan
	 * @param number
	 * @param message
	 */
	public static void sendSMS(String number, String message) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(number, null, message, null, null);
	}
	
	/**
	 * Extrai conteúdo de SMS recebido
	 * 
	 * @author Erivan
	 * @param intent
	 * @return SmsMessage[]
	 */
	public static SmsMessage[] getIntentContent(Intent intent) {

		Object[] pdusExtras = (Object[]) intent.getSerializableExtra("pdus");

		SmsMessage[] message = new SmsMessage[pdusExtras.length];

		for (int i = 0; i < pdusExtras.length; i++) {
			message[i] = SmsMessage.createFromPdu((byte[]) pdusExtras[i]);
		}
		return message;
	}
	
	/**
	 * Inicia o servico que mostra a notificacao permanente de itens no cofre 
	 * ao ligar o telefone caso o usuario tenha configurado a aplicacao desta forma
	 * @author Rogerio Peixoto
	 * @param context
	 */
	public static void startServiceWhenNecessary(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
		boolean runOnBoot = prefs.getBoolean(Constants.RUN_ON_BOOT_PREFERENCE, true);
		if(runOnBoot) {
			startService(context);
		}
	}
	
	/**
	 * Inicia o servico de notificacao
	 * @author Rogerio Peixoto
	 * @param context
	 */
	public static void startService(Context context) {
		Intent it = new Intent(context, NotificationService.class);
		context.startService(it);
	}	
	
	/**
	 * Para o servico de notificacao
	 * @param context
	 * @author Rogerio Peixoto
	 */
	public static void stopService(Context context) {
		Intent it = new Intent(context, NotificationService.class);
		context.stopService(it);
	}
	
	/**
	 * Mostra notificacoes na tela
	 * @author Rogerio Peixoto
	 */
	public static Toast makeToast(Context ctx, String text, Toast toast) {
    	if (toast == null){
    		toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
    	} else {
        	toast.cancel();
    		toast.setText(text);
    	}
    	toast.show();
    	return toast;
	}
	
	/**
	 * Muda a fonte de um TextView
	 * @author Rogerio Peixoto
	 * @param font
	 * @param textViewResourceId
	 * @param activity
	 */
	public static void setViewFont(String font, View view, Activity activity) {
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
		
		if (view instanceof EditText) {
			((EditText) view).setTypeface(tf);
		}
		if (view instanceof TextView)
			((TextView) view).setTypeface(tf);
	    }
	/**
	 * Desabilita mostrar o teclado virtual em um input
	 * @param view
	 */
    public static void disableShowVirtualKeyboard(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.requestFocus();
                if (view instanceof EditText) {
                    EditText edit = (EditText) view;
                    edit.setSelection(edit.getText().length());
                }
                return true;
            }
        });
    }
    
    /**
     * Desabilita a visibilidade dos caracteres digitados
     * @param view
     */
    public static void disableUserInputs(View view) {
        if (view instanceof EditText) {
            EditText edit = (EditText) view;
            edit.setFilters(new InputFilter[] { noInputFilter });
        }
    }
    
    /** 
     * Mostrar mensagem em caixa de dialogo
     * 
     * @author Rogerio Peixoto
     * @date 14/11/2011
     */
	public static void showDialog(int titleResID, int icoResnID, int msgResID, int btnLabeReslID, Context ctx){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
		.setTitle(ctx.getString(titleResID))
		.setIcon(icoResnID)
		.setMessage(ctx.getString(msgResID));
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Encripta a menságem passada 
	 * 
	 * @author Erivan
	 * @param message
	 * @return Mensagem encriptada e pronta pra envio
	 */
	public static String encriptMessage(String message){
		String encMessage = null;
		// Cria instância do gerador de chaves
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance("RC4");
			// Inicializa gerador de chaves (128bit) e gera chave
			keyGenerator.init(128);
			SecretKey key = keyGenerator.generateKey();			
			Crypter crypt = new Crypter(key);			
			encMessage = crypt.encrypt(message);
				
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encMessage;
	}
	
	/**
	 * Encripta a menságem passada e inclui flags 
	 * que identificam uma menságem de CryptosVault.
	 * 
	 * @author Erivan
	 * @param message
	 * @return Mensagem encriptada e pronta pra envio
	 */
	public static String encriptAndPrepareMessageToSend(String message){
		String encMessage = null;
		
		encMessage = Util.encriptMessage(message);
		
		return Util.formatCryptosVaultCodeMensageToSend(encMessage);
	}
	
	/**
	 * Desencripta a mensagem passada
	 * 
	 * @author Erivan
	 * @param encriptMessage
	 * @return Mensagem desemcriptada
	 */
	public static String decriptMessage(String encriptMessage){
		String encMessage = null;
		// Cria instância do gerador de chaves
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance("RC4");
			// Inicializa gerador de chaves (128bit) e gera chave
			keyGenerator.init(128);
			SecretKey key = keyGenerator.generateKey();			
			Crypter crypt = new Crypter(key);			
			encMessage = crypt.decrypt(encriptMessage);
				
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encMessage;
	}
	
}
