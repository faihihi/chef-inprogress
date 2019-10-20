package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.Array;
import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.RecipeDBHelper;

public class HomeFragment extends Fragment {
    private RecipeDBHelper recipeDBHelper;
    private InventoryDBHelper inventoryDBHelper;

    private ArrayList<Recipe> recipeArrayList, userRecipe, intentList;
    private ArrayList<Inventory> userInventory;

    HomeAdapter arrayAdapter;
    EditText searchTXT;
    TextView pageTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recipeDBHelper = new RecipeDBHelper(getContext());
        inventoryDBHelper = new InventoryDBHelper(getContext());

        recipeArrayList = new ArrayList<>();
        recipeArrayList = recipeDBHelper.getAllData();

        userInventory = new ArrayList<>();
        userInventory = inventoryDBHelper.getItemsInUserInventory();

        userRecipe = new ArrayList<>();
        userRecipe = getUserRecipe();

        pageTitle = (TextView) root.findViewById(R.id.title);
        intentList = new ArrayList<>();
        // User has ingredients for the recipe in the database
        if(userRecipe.size() > 0){
            arrayAdapter = new HomeAdapter(getContext(), userRecipe);
            intentList = userRecipe;
            pageTitle.setText("Here's your Recipes");
        } else{
            arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);
            intentList = recipeArrayList;
            pageTitle.setText("Recommended Recipes");
        }

//        arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);
        ListView listView = root.findViewById(R.id.recipeList);

        listView.setTextFilterEnabled(true);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Check POSITION", String.valueOf(i));
                Intent intent = new Intent(getActivity().getBaseContext(),
                        RecipeDetails.class);
                intent.putExtra("title", intentList.get(i).getTitle());
                intent.putExtra("image", intentList.get(i).getImgpath());
                intent.putExtra("protein", intentList.get(i).getProtein());
                intent.putExtra("fat", intentList.get(i).getFat());
                intent.putExtra("carbs", intentList.get(i).getCarbs());
                intent.putExtra("time", intentList.get(i).getTimeTaken());
                intent.putExtra("serves", intentList.get(i).getServings());
                intent.putExtra("ingredients", intentList.get(i).getIngredientsList());
                intent.putExtra("instructions", intentList.get(i).getInstructionsString());
                getActivity().startActivity(intent);
            }
        });

        searchTXT = root.findViewById(R.id.searchTxt);
        searchTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HomeFragment.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
//                filter(s.toString());
            }
        });



        return root;
    }

    private ArrayList<Recipe> getUserRecipe() {
        ArrayList<Recipe> result = new ArrayList<>();

        for(Recipe recipe : recipeArrayList){
            ArrayList<Ingredients> recipeIngredients = new ArrayList<>();
            recipeIngredients = recipe.getIngredientsList();

            int recipeIngredients_total = recipeIngredients.size();
            int count = 0;
            for(Ingredients recipeIng : recipeIngredients){
                String[] words = recipeIng.getIngredientsName().split("\\W+");
                for(int i=0;i<words.length;i++){
                    for(Inventory item: userInventory){
                        if(words[i].equalsIgnoreCase(item.getItemName())){
                            count++;
                        }
                    }
                }
            }
            if(recipeIngredients_total == count){
                result.add(recipe);
            }
        }


        return result;
    }
}