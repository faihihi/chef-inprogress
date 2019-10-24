package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;

/**
 * ShoppingList adapter for shopping list
 */
public class ShoppinglistAdapter extends BaseAdapter {

    ArrayList<ShoppinglistItem> shoppinglistItems;
    Context context;

    /**
     * Constructor
     * @param c
     * @param shoppinglist
     */
    ShoppinglistAdapter(Context c, ArrayList<ShoppinglistItem> shoppinglist){
        context = c;
        shoppinglistItems = shoppinglist;
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        return shoppinglistItems.size();
    }

    /**
     * Get item
     * @param position
     * @return
     */
    @Override
    public ShoppinglistItem getItem(int position) {
        return shoppinglistItems.get(position);
    }

    /**
     * Get item ID
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Check if list is checked
     * @param position
     * @return
     */
    public boolean isChecked(int position){
        return shoppinglistItems.get(position).checked;
    }

    /**
     * Get view of each shopping list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShoppinglistItem shoppinglistItem = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shoppinglist_item,parent,false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.rowCheckBox);
        TextView textView = convertView.findViewById(R.id.shoppingName);

        textView.setText(shoppinglistItem.getName());
        checkBox.setChecked(shoppinglistItem.checked);

        checkBox.setOnClickListener(new View.OnClickListener() {
            /**
             * If item is checked, save state
             * @param v
             */
            @Override
            public void onClick(View v) {
                boolean newState = !shoppinglistItem.isChecked();
                shoppinglistItem.checked = newState;
            }
        });

        checkBox.setChecked(isChecked(position));

        return convertView;
    }
}