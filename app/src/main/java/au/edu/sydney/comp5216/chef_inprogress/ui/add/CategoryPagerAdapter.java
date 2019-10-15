package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public CategoryPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title, int position) {
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
    }

    public void setFragment(Fragment fragment, String title, int position) {
        mFragmentList.set(position, fragment);
        mFragmentTitleList.set(position, title);
    }

    public void removeFragment(Fragment fragment, int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}