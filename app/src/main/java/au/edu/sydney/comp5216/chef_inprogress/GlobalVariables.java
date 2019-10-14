package au.edu.sydney.comp5216.chef_inprogress;

import android.app.Application;
import android.view.ActionMode;

import au.edu.sydney.comp5216.chef_inprogress.ui.add.CategoryPagerAdapter;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.ViewPagerAdapter;

public class GlobalVariables extends Application {
    private ActionMode currentMode;

    CategoryPagerAdapter viewPagerAdapter;
    private int currentCategory;

    public ActionMode getCurrentMode() {
        return currentMode;
    }

    public CategoryPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public int getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentMode(ActionMode currentMode) {
        this.currentMode = currentMode;
    }

    public void setViewPagerAdapter(CategoryPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }

    public void setCurrentCategory(int currentCategory) {
        this.currentCategory = currentCategory;
    }
}
