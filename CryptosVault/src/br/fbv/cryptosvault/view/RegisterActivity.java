package br.fbv.cryptosvault.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.account.AccountManager;
import br.fbv.cryptosvault.model.exception.InvalidEmailException;
import br.fbv.cryptosvault.model.exception.InvalidPasswordException;
import br.fbv.cryptosvault.model.util.Util;

/**
 * @author  Rogério
 */
public class RegisterActivity extends Activity implements OnClickListener {
	
	/**
	 * @uml.property  name="accountManager"
	 * @uml.associationEnd  
	 */
	private AccountManager accountManager;
	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnRegister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		accountManager = AccountManager.getInstance(this);
		setContentView(R.layout.register_screen);
		
		edtEmail = (EditText) findViewById(R.id.EdtRegisterEmail);
		edtPassword = (EditText) findViewById(R.id.EdtRegisterPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		btnRegister.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		accountManager = null;
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister) {
			try {
				accountManager.registerAccount(edtEmail.getText().toString(), edtPassword.getText().toString());
				setResult(RESULT_OK);
				finish();
			} catch (InvalidEmailException e) {
				Util.showDialog(R.string.error_title,
								R.drawable.error_icon, 
								R.string.invalid_email, 
								R.string.ok_label, 
								this);
				e.printStackTrace();
			} catch (InvalidPasswordException e) {
				Util.showDialog(R.string.error_title,
						R.drawable.error_icon, 
						R.string.invalid_password, 
						R.string.ok_label, 
						this);
				e.printStackTrace();
			}
		}
	}
}
