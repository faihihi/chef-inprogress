package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class ShoppingListFragment_lv2 extends Fragment {


    private ArrayList<ShoppinglistItem> shoppinglist = new ArrayList<>();
    private ShoppinglistAdapter shoppinglistAdapter;

    ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoppinglist_lv2, container, false);


        shoppinglist.add(new ShoppinglistItem(false,"Apple"));
        shoppinglist.add(new ShoppinglistItem(true,"Banana"));
        shoppinglist.add(new ShoppinglistItem(false,"Cling Wrap"));


        // Initialize the custom adapter and connect listView with adapter
        //itemsAdapter = new InventoryItemAdapter(getContext(), inventoryList);

        shoppinglistAdapter = new ShoppinglistAdapter(getContext(),shoppinglist);
        listView = (ListView) view.findViewById(R.id.shoppingListview);
        listView.setAdapter(shoppinglistAdapter);

        setupListviewListener();

        //GridView gridView = (GridView) view.findViewById(R.id.shoppingGridView);
        //gridView.setAdapter(itemsAdapter);

//
//        listView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Delete the item")
//                        .setMessage("Do you want to delete this item?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //remove item from shopping list
//                                itemsAdapter.notifyDataSetChanged();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//
//                builder.create().show();
//                return true;
//            }
//        });

        return view;
    }

    private void setupListviewListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("TESSSST","RUNNING");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete the item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //remove item from shopping list
                                shoppinglistAdapter.notifyDataSetChanged();
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
    }
}
