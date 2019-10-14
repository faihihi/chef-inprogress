package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.InventoryFragment_lv2;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.ShoppingListFragment_lv2;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.ViewPagerAdapter;

public class AddFragment extends Fragment {
    private ArrayList<InventoryItem> inventoryList;
    private ArrayAdapter<InventoryItem> itemsAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private InventoryDBHelper inventoryDBHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

//        inventoryDBHelper = new InventoryDBHelper(getContext());
//
////        inventoryList = new ArrayList<>();
////        inventoryList = inventoryDBHelper.getAllData();

        viewPager = root.findViewById(R.id.addViewPager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.addTabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle args = new Bundle();
        args.putString("category", "meat");
        GridFragment f = new GridFragment();
        f.setArguments(args);
        adapter.addFragment(f,"MEAT");


        args = new Bundle();
        args.putString("category", "fruitveg");
        f = new GridFragment();
        f.setArguments(args);
        adapter.addFragment(f,"FRUITS & VEG");

        args = new Bundle();
        args.putString("category", "grocery");
        f = new GridFragment();
        f.setArguments(args);
        adapter.addFragment(f,"GROCERY");

        viewPager.setAdapter(adapter);
    }
}