package br.fbv.cryptosvault.view;

import br.fbv.cryptosvault.R;
import android.app.Activity;
import android.os.Bundle;

public class MessageActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_screen);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
