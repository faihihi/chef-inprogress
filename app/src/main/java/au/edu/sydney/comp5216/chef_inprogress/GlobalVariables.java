package au.edu.sydney.comp5216.chef_inprogress;

import android.app.Application;

import au.edu.sydney.comp5216.chef_inprogress.ui.add.CategoryPagerAdapter;

public class GlobalVariables extends Application {

    CategoryPagerAdapter viewPagerAdapter;


    public CategoryPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public void setViewPagerAdapter(CategoryPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }
}
