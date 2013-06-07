package br.fbv.cryptosvault.core;

import android.content.Context;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import br.fbv.cryptosvault.model.security.FileEncrypter;

public class SafeFileAdapter extends FileAdapter {
	
	private Context context;

	public SafeFileAdapter(Context context,
			OnCheckedChangeListener onItemCheckedChangeListener) {
		super(context, onItemCheckedChangeListener);
	}

	@Override
	protected void setItemName(TextView itemNameText, FileItem item) {
		String name = FileEncrypter.getInstance().getDecryptFileName(item.getFile().getName());
		itemNameText.setText(name);
	}
}
