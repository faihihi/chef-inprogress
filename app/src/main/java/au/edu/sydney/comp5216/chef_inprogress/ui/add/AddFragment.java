package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private ArrayList<InventoryItem> inventoryList;
    private ArrayAdapter<InventoryItem> itemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel =
                ViewModelProviders.of(this).get(AddViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        final TextView textView = root.findViewById(R.id.text_add);
        final TextView itemView = root.findViewById(R.id.item);

        addViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        addViewModel.getInventoryList().observe(this, new Observer() {
            @Override
            public void onChanged(Object inventoryList) {

            }
        });

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
        GridView gridView = (GridView) root.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(itemsAdapter);

        return root;
    }
}