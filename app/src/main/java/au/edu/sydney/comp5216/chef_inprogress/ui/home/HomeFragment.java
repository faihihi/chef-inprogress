package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.Array;
import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.RecipeDBHelper;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecipeDBHelper recipeDBHelper;
    private ArrayList<Recipe> recipeArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        recipeDBHelper = new RecipeDBHelper(getContext());

        recipeArrayList = new ArrayList<>();
        recipeArrayList = recipeDBHelper.getAllData();



        Log.d("test recipe tag", recipeArrayList.get(0).getTags()[0]);
        Log.d("INSTRUCTIONS", recipeArrayList.get(0).getInstructions()[0]);
        Log.d("INGREDIENTS", recipeArrayList.get(0).getIngredientsListString());

        Log.d("Ingredients LIST", recipeArrayList.get(0).getIngredientsList().get(0).getDescription());

        return root;
    }
}