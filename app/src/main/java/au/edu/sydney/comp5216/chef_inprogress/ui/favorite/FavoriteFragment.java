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

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.FirebaseRecipeDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;


public class FavoriteFragment extends Fragment{
    private UserDBHelper userDBHelper;

    private ArrayList<Favorite> arrayList;
    private ArrayList<Recipe> favoriteList;
    private ArrayList<String> titleList;
    FavoriteAdapter arrayAdapter;

    EditText searchTXT;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        searchTXT = root.findViewById(R.id.searchTxt);

        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();
        titleList = c.getFavorites();

        arrayList = new ArrayList<>();
        favoriteList = new ArrayList<>();

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
            public void DataIsUpdated() {

            }
        });

        arrayAdapter = new FavoriteAdapter(getContext(), arrayList);
        ListView listView = root.findViewById(R.id.favouriteList);

        listView.setTextFilterEnabled(true);

        listView.setAdapter(arrayAdapter);

        searchTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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
