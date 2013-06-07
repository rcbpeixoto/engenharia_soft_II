package br.fbv.cryptosvault.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.util.Constants;
import br.fbv.cryptosvault.model.util.Util;

public class ConfigurationActivity extends Activity implements
		OnCheckedChangeListener {

	private CheckBox cbxRunOnBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.configuration_screen);

		cbxRunOnBackground = (CheckBox) findViewById(R.id.cbxNotification);
		cbxRunOnBackground.setChecked(getRunOnBackground());
		cbxRunOnBackground.setOnCheckedChangeListener(this);
	}
	
	private SharedPreferences getPreferences() {
		return getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);		
	}
	
	private boolean getRunOnBackground() {
		return getPreferences().getBoolean(Constants.RUN_ON_BOOT_PREFERENCE, true);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		boolean runOnBackground = isChecked;
		Editor editor = getPreferences().edit();
		editor.putBoolean(Constants.RUN_ON_BOOT_PREFERENCE, runOnBackground);
		editor.commit();
		if (runOnBackground) {
			Util.startService(this);
		} else {
			Util.stopService(this);
		}
	}
}
