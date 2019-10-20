package au.edu.sydney.comp5216.chef_inprogress;

import android.app.Application;

import au.edu.sydney.comp5216.chef_inprogress.ui.add.CategoryPagerAdapter;

public class GlobalVariables extends Application {

    CategoryPagerAdapter viewPagerAdapter;

    User currentUser;
    String currentUserKey;

    public void setCurrentUserKey(String currentUserKey) {
        this.currentUserKey = currentUserKey;
    }

    public String getCurrentUserKey() {
        return currentUserKey;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public CategoryPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public void setViewPagerAdapter(CategoryPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }
}
