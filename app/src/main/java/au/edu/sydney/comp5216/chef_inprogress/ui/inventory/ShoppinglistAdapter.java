package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;

public class ShoppinglistAdapter extends BaseAdapter {

    ArrayList<ShoppinglistItem> shoppinglistItems;
    Context context;

    ShoppinglistAdapter(Context c, ArrayList<ShoppinglistItem> shoppinglist){
        context = c;
        shoppinglistItems = shoppinglist;
    }

    @Override
    public int getCount() {
        return shoppinglistItems.size();
    }

    @Override
    public ShoppinglistItem getItem(int position) {
        return shoppinglistItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isChecked(int position){
        return shoppinglistItems.get(position).checked;
    }

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
            @Override
            public void onClick(View v) {
                boolean newState = !shoppinglistItem.isChecked();
                shoppinglistItem.checked = newState;

            }
        });

        checkBox.setChecked(isChecked(position));

        textView.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View v) {
                Log.v("TESSSST","RUNNING");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete the item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //remove item from shopping list
                                //shoppinglistAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create().show();
                return true;

            }
        });

        return convertView;
    }
}