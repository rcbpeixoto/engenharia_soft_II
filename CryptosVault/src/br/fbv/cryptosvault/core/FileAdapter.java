package br.fbv.cryptosvault.core;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.fbv.cryptosvault.R;

/**
 * @author    Rogério
 */
public class FileAdapter extends BaseAdapter {

    private Context                                context;
    private List<FileItem>                         items;
    private CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener;

    public FileAdapter(Context context, CompoundButton.OnCheckedChangeListener onItemCheckedChangeListener) {
        super();
        this.context = context;
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    public List<FileItem> getItems() {
        return items;
    }

    public void setItems(List<FileItem> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_file_item, null);
        }

        FileItem item = items.get(position);

        CheckBox itemCheckBox = (CheckBox) convertView.findViewById(R.id.itemCheckbox);
        itemCheckBox.setTag(item);
        itemCheckBox.setChecked(item.isSelected());
        itemCheckBox.setOnCheckedChangeListener(onItemCheckedChangeListener);
        itemCheckBox.setVisibility(View.GONE);

        ImageView itemThumb = (ImageView) convertView.findViewById(R.id.itemThumb);
        itemThumb.setVisibility(View.VISIBLE);

        if (item.isBackToParent()) {
            // Back to parent icon.
            itemThumb.setImageResource(R.drawable.folder_back);
        } else if (item.getFile().isDirectory()) {
            // Directory icon.
            itemThumb.setImageResource(R.drawable.folder);
        } else {
            itemThumb.setVisibility(View.GONE);
            itemCheckBox.setVisibility(View.VISIBLE);
        }

        TextView itemNameText = (TextView) convertView.findViewById(R.id.itemNameText);
        setItemName(itemNameText, item);

        return convertView;
    }

    protected void setItemName(TextView itemNameText, FileItem item) {
        itemNameText.setText(item.getFile().getName());
    }

}
