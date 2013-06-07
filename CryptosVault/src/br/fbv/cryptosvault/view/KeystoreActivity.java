package br.fbv.cryptosvault.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.core.KeystoreAdapter;
import br.fbv.cryptosvault.model.persistence.repositories.KeystoreRepository;

/**
 * @author  Rogério
 */
public class KeystoreActivity extends Activity implements OnClickListener,
		OnItemLongClickListener {

	/**
	 * @uml.property  name="keystoreRepository"
	 * @uml.associationEnd  
	 */
	private KeystoreRepository keystoreRepository;
	/**
	 * @uml.property  name="keystoreAdapter"
	 * @uml.associationEnd  
	 */
	private KeystoreAdapter keystoreAdapter;
	private ListView keystoreListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keystore_screen);

		keystoreRepository = KeystoreRepository.getInstance(this);

		keystoreAdapter = new KeystoreAdapter(this, keystoreRepository.allKeyStore());
		
//		keystoreRepository.fillKeystoreForTest();

		keystoreListView = (ListView) findViewById(R.id.keystoreListView);
		keystoreListView.setAdapter(keystoreAdapter);

		findViewById(R.id.add_key_btn).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		keystoreRepository.closeDatabase();
		keystoreRepository = null;
		keystoreAdapter = null;
		super.onDestroy();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.keystore_context_menu, menu);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			Dialog dialog = new Dialog(this);

			dialog.setContentView(R.layout.keystore_edit_dialog);
			dialog.setTitle("Adicione sua Senha:");

//			EditText description = (EditText) dialog.findViewById(R.id.edit_description_keystore);
//			Button btnAddKeystore = (Button) dialog.findViewById(R.id.add_keystore_btn_dialog);
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add_key_btn) {
			showDialog(0);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		openContextMenu(view);
		return true;
	}
}
