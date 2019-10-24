package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import au.edu.sydney.comp5216.chef_inprogress.R;

/**
 * InventoryFragment is started when Inventory menu is clicked
 */
public class InventoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Create view of user's inventory
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inventory, container, false);

        viewPager = root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    /**
     * Set up view pager for showing Inventory and Shopping List tabs
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new InventoryFragment_lv2(),"Inventory");
        adapter.addFragment(new ShoppingListFragment_lv2(),"Shopping List");
        viewPager.setAdapter(adapter);
    }

}