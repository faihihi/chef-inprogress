package au.edu.sydney.comp5216.chef_inprogress;

import android.app.Application;

import au.edu.sydney.comp5216.chef_inprogress.ui.add.CategoryPagerAdapter;

/**
 * Application class for storing Global variables
 */
public class GlobalVariables extends Application {

    CategoryPagerAdapter viewPagerAdapter;

    /**
     * Getting view pager adapter
     * @return
     */
    public CategoryPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    /**
     * Setting view pager adapter
     * @param viewPagerAdapter
     */
    public void setViewPagerAdapter(CategoryPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }
}
