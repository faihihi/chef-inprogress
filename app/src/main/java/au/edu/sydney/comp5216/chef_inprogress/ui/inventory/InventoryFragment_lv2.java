package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class InventoryFragment_lv2 extends Fragment {
    private ArrayList<InventoryItem> inventoryList;
    private ArrayAdapter<InventoryItem> itemsAdapter;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2,container,false);

        inventoryList = new ArrayList<>();

        inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));
        inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));
        inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));
        inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));
        inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));

        // Initialize the custom adapter and connect listView with adapter
        itemsAdapter = new InventoryItemAdapter(getContext(), inventoryList);
        GridView gridView = (GridView) view.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(itemsAdapter);

        gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete the item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //remove item from shopping list
                                itemsAdapter.notifyDataSetChanged();
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

        return view;
    }
}
