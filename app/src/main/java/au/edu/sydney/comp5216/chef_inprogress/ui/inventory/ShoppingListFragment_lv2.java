package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
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


        return view;
    }
}