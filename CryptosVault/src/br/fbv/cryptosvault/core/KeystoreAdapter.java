package br.fbv.cryptosvault.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import br.fbv.cryptosvault.R;
import br.fbv.cryptosvault.model.objects.KeyStore;

public class KeystoreAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<KeyStore> keystoreList;
	
	public KeystoreAdapter(Context context, ArrayList<KeyStore> keystoreList){
		super();
		this.context = context;
		this.keystoreList = keystoreList;
	}
	
    public ArrayList<KeyStore> getItems() {
        return keystoreList;
    }

    public void setItems(ArrayList<KeyStore> keystoreList) {
        this.keystoreList = keystoreList;
    }
	
	@Override
	public int getCount() {
		return keystoreList.size();
	}

	@Override
	public Object getItem(int position) {
		return keystoreList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return keystoreList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_keystore_item, null);
		}
		KeyStore keystore = keystoreList.get(position);

        TextView keystoreDescription = (TextView) convertView.findViewById(R.id.keystoreDescription);
        keystoreDescription.setText(keystoreList.get(position).getDescription());
        
        TextView keystoreExpDate = (TextView) convertView.findViewById(R.id.keystoreExpirationDate);
        
        Date expDateString = keystoreList.get(position).getExpireDate();
        keystoreExpDate.setText(expDateString.toString());

		return convertView;
	}

}
