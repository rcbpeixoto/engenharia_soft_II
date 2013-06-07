package br.fbv.cryptosvault.view;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.core.SoundManager;
import br.fbv.cryptosvault.model.account.AccountManager;
import br.fbv.cryptosvault.model.exception.InvalidPasswordException;
import br.fbv.cryptosvault.model.exception.UserNotFoundException;
import br.fbv.cryptosvault.model.util.Constants;
import br.fbv.cryptosvault.model.util.Util;

/**
 * Project: Cryptos Vault Class: LoginActivity REVISION HISTORY Date          Developer       Comment ----------    -------------------------------------------- ---------------------------------------------- 30/10/2011    "Rogério Peixoto" <rcbpeixoto@gmail.com>  	  InitialDraft ----------    -------------------------------------------- ----------------------------------------------
 */
public class LoginActivity extends Activity{
	
    private static final InputFilter noInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start,
                int end, Spanned dest, int dstart, int dend) {
            return "";
        }
    };
	
    private static Toast toast;
	private EditText edtPassword;
	/**
	 * @uml.property  name="accountManager"
	 * @uml.associationEnd  
	 */
	private AccountManager accountManager;
	/**
	 * @uml.property  name="soundManager"
	 * @uml.associationEnd  
	 */
	private static SoundManager soundManager;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		super.onCreate(savedInstanceState);
		
		soundManager = SoundManager.getInstance(this);
		accountManager = AccountManager.getInstance(this);
		
		setContentView(R.layout.login_screen);
		edtPassword = (EditText) findViewById(R.id.EdtLoginPassword);
		
		configPassKeyboard(this);
		Util.disableShowVirtualKeyboard(edtPassword);
		Util.disableUserInputs(edtPassword);
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		edtPassword = null;
		accountManager = null;
	}
	
	public void tryLogin(String password){
		try {
			accountManager.login(password);
			setResult(RESULT_OK);
			finish();
		} catch (InvalidPasswordException e) {
			Util.showDialog(R.string.error_title, 
							R.drawable.cancel, 
							R.string.incorrect_password, 
							R.string.ok_label, 
							this);
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Ativa linha do teclado para tratamento de clicks
     * @param row
     * @param activity
     */
	private void setKeyboardRowClick(ViewGroup row, final Activity activity) {
		for (int i = 0; i < row.getChildCount(); i++) {
			row.getChildAt(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					
					View currentFocus = activity.getCurrentFocus();
					
					if (currentFocus != null && currentFocus instanceof EditText) {
						EditText edit = (EditText) currentFocus;
						
						if (edit.getInputType() == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD) {

							if (view.getId() == R.id.btn_cancel_password) {
								edit.setText("");
							} else if (view.getId() == R.id.btn_confirm_password) {
								tryLogin(edit.getText().toString());
							} else {

								soundManager.stopSounds();
								soundManager.playSound(SoundManager.DIGIT);

								if (edit.getText().length() < Constants.PASSWORD_MAX_LENGTH) {
									edit.setFilters(new InputFilter[] {});
									String key = (String) view.getTag();
									edit.getText().append(key);
									edit.setFilters(new InputFilter[] { noInputFilter });
								}
								if (edit.getText().length() == Constants.PASSWORD_MAX_LENGTH) {
									View nextFocus = currentFocus
											.focusSearch(View.FOCUS_DOWN);
									if (nextFocus != null) {
										nextFocus.requestFocus();
									}
								}
							}
						}
					}
				}
			});
		}
	}
	
	public void configPassKeyboard(Activity activity) {
		ViewGroup keyboardRow1 = (ViewGroup) activity.findViewById(R.id.keypadRow1);
		setKeyboardRowClick(keyboardRow1, activity);
		ViewGroup keyboardRow2 = (ViewGroup) activity.findViewById(R.id.keypadRow2);
		setKeyboardRowClick(keyboardRow2, activity);
		ViewGroup keyboardRow3 = (ViewGroup) activity.findViewById(R.id.keypadRow3);
		setKeyboardRowClick(keyboardRow3, activity);
		ViewGroup keyboardRow4 = (ViewGroup) activity.findViewById(R.id.keypadRow4);
		setKeyboardRowClick(keyboardRow4, activity);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == R.id.about_menu_item) {
			intent = new Intent(this, AboutActivity.class);
		}
		else if (item.getItemId() == R.id.exit_menu_item){
			Util.stopService(this);
			onBackPressed();
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
