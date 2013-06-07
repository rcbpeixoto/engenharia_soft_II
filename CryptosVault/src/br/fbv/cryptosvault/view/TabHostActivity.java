package br.fbv.cryptosvault.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.account.AccountManager;
import br.fbv.cryptosvault.model.util.Util;

/**
 * @author  Rogério
 */
public class TabHostActivity extends TabActivity  {

	private static final int OPEN = 0;
	private static final int LOGIN = 1;
	private static final int NEW_USER = 2;
	private static final int CLOSE = 3;

	/**
	 * @uml.property  name="accountManager"
	 * @uml.associationEnd  
	 */
	private AccountManager accountManager;
	private TabHost tabHost;
	private Menu mainMenu;
	
	private int NOTIFICATION_KEY = R.string.app_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		accountManager = AccountManager.getInstance(this);

		Intent intentActivitiesForResult;
		intentActivitiesForResult = new Intent(this, SplashActivity.class);
		startActivityForResult(intentActivitiesForResult, OPEN);

		Util.startServiceWhenNecessary(this);

		tabHost = getTabHost();
		
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(null, getResources().getDrawable(R.drawable.ic_lock_lock))
				.setContent(new Intent(this, FilesActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(null, getResources().getDrawable(R.drawable.ic_menu_start_conversation))
				.setContent(new Intent(this, MessageActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator(null, getResources().getDrawable(R.drawable.ic_menu_login))
				.setContent(new Intent(this, KeystoreActivity.class)));
		
		mainMenu = (Menu) findViewById(R.menu.tabhost_menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Intent intent;

		switch (requestCode) {

		case OPEN:
			if (accountManager.isFirstAccess()) {
				intent = new Intent(this, RegisterActivity.class);
				startActivityForResult(intent, NEW_USER);
			} else {
				intent = new Intent(this, LoginActivity.class);
				startActivityForResult(intent, LOGIN);
			}
			break;
		case LOGIN:
			if (resultCode == RESULT_OK) {
			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
			break;
		case NEW_USER:
			if (resultCode == RESULT_OK) {

			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
			break;
		case CLOSE:
			if (resultCode == CLOSE) {
				finish();
			}
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tabhost_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		accountManager = null;
		tabHost = null;
		super.onDestroy();
	}
	
}
