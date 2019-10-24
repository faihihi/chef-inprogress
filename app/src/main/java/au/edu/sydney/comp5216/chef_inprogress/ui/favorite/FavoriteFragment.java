package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseRecipeDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;


/**
 * FavoriteFragment is started when user click on Favorite menu
 */
public class FavoriteFragment extends Fragment{
    private UserDBHelper userDBHelper;

    private ArrayList<Favorite> arrayList;
    private ArrayList<Recipe> favoriteList;
    private ArrayList<String> titleList;
    FavoriteAdapter arrayAdapter;

    EditText searchTXT;

    /**
     * Create view for Favorite Fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        searchTXT = root.findViewById(R.id.searchTxt);

        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();
        titleList = c.getFavorites();

        arrayList = new ArrayList<>();
        favoriteList = new ArrayList<>();

        // Get recipes data from Firebase
        new FirebaseRecipeDBHelper().getAllRecipe(new FirebaseRecipeDBHelper.DataStatus() {
            @Override
            public void RecipeisLoaded(List<Recipe> recipes, List<String> keys) {
                for (Recipe recipe : recipes) {
                    for(String title: titleList){
                        if(recipe.getTitle().equalsIgnoreCase(title)){
                            arrayList.add(new Favorite(recipe.getTitle(), recipe.getTimeTaken(), recipe.getImgpath()));
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void DataIsUpdated() { }
        });

        // Set favorite adapter on list view
        arrayAdapter = new FavoriteAdapter(getContext(), arrayList);
        ListView listView = root.findViewById(R.id.favouriteList);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(arrayAdapter);

        /**
         * Set text changed listener on search edittext bar
         */
        searchTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * When text is changed, display filtered list
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FavoriteFragment.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //filter(s.toString());
            }
        });

        return root;
    }

}
