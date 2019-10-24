package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for tabeview of category
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    /**
     * Constructor for the adapter
     * @param manager
     */
    public CategoryPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**
     * Get item
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    /**
     * Adding fragment to the tabview
     * @param fragment
     * @param title
     * @param position
     */
    public void addFragment(Fragment fragment, String title, int position) {
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
    }

    /**
     * Setting fragment on the tabview
     * @param fragment
     * @param title
     * @param position
     */
    public void setFragment(Fragment fragment, String title, int position) {
        mFragmentList.set(position, fragment);
        mFragmentTitleList.set(position, title);
    }

    /**
     * Remove fragment from the tabview
     * @param fragment
     * @param position
     */
    public void removeFragment(Fragment fragment, int position) {
        mFragmentList.remove(position);
        mFragmentTitleList.remove(position);
    }


    /**
     * Get page title of the fragment
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
