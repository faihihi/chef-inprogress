package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class AddFragment extends Fragment {
    CategoryPagerAdapter categoryPagerAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        viewPager = root.findViewById(R.id.addViewPager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.addTabs);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager){
        categoryPagerAdapter = new CategoryPagerAdapter(getFragmentManager());
        categoryPagerAdapter = new CategoryPagerAdapter(getChildFragmentManager());

        Bundle args = new Bundle();
        args.putString("category", "meat");
        GridFragment f = new GridFragment();
        f.setArguments(args);
        categoryPagerAdapter.addFragment(f,"MEAT", 0);


        args = new Bundle();
        args.putString("category", "fruitveg");
        f = new GridFragment();
        f.setArguments(args);
        categoryPagerAdapter.addFragment(f,"FRUITS & VEG", 1);

        args = new Bundle();
        args.putString("category", "grocery");
        f = new GridFragment();
        f.setArguments(args);
        categoryPagerAdapter.addFragment(f,"GROCERY", 2);

        ((GlobalVariables) getActivity().getApplication()).setViewPagerAdapter(categoryPagerAdapter);
        viewPager.setAdapter(categoryPagerAdapter);
    }

}