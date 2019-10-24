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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseRecipeDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * HomeFragment is started when home menu is selected
 * It is also a default fragment that is launched when MainActivity run
 */
public class HomeFragment extends Fragment {
    private InventoryDBHelper inventoryDBHelper;
    private UserDBHelper userDBHelper;

    private ArrayList<Recipe> recipeArrayList, userRecipe, intentList;
    private ArrayList<Inventory> userInventory;

    private HomeAdapter arrayAdapter;
    private EditText searchTXT;
    private TextView pageTitle;
    private ListView listView;
    private Button toggle;

    /**
     * Create view for Home
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        inventoryDBHelper = new InventoryDBHelper(getContext());
        userDBHelper = new UserDBHelper(getContext());

        userInventory = new ArrayList<>();
        userInventory = inventoryDBHelper.getItemsInUserInventory();

        userRecipe = new ArrayList<>();
        recipeArrayList = new ArrayList<>();

        arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);

        listView = root.findViewById(R.id.recipeList);
        toggle = (Button) root.findViewById(R.id.toggle_btn);

        // Get all recipes from Firebase
        new FirebaseRecipeDBHelper().getAllRecipe(new FirebaseRecipeDBHelper.DataStatus() {
            @Override
            public void RecipeisLoaded(List<Recipe> recipes, List<String> keys) {
                for (Recipe recipe : recipes) {
                    recipeArrayList.add(recipe);
                    arrayAdapter.notifyDataSetChanged();
                }

                userRecipe = getUserRecipes();

                // Get recipes that users' ingredients can make
                // Set display for "Recipes for you" if the recipes exist
                if (userRecipe.size() > 0) {
                    toggle.setVisibility(View.VISIBLE);
                    arrayAdapter = new HomeAdapter(getContext(), userRecipe);
                    listView.setAdapter(arrayAdapter);
                    intentList = userRecipe;
                    pageTitle.setText("Recipes for you");
                } else {
                    arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);
                    intentList = recipeArrayList;
                    pageTitle.setText("All Recipes");
                }

                toggle.setOnClickListener(new View.OnClickListener() {
                    /**
                     * When toggle button is clicked, switch view between Recipe for You and All Recipes
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        if(pageTitle.getText().equals("Recipes for you")){
                            arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);
                            intentList = recipeArrayList;
                            pageTitle.setText("All Recipes");
                            listView.setAdapter(arrayAdapter);
                        } else if(pageTitle.getText().equals("All Recipes")){
                            arrayAdapter = new HomeAdapter(getContext(), userRecipe);
                            listView.setAdapter(arrayAdapter);
                            intentList = userRecipe;
                            pageTitle.setText("Recipes for you");
                            listView.setAdapter(arrayAdapter);
                        }
                    }
                });
            }

            @Override
            public void DataIsUpdated() {

            }
        });

        pageTitle = (TextView) root.findViewById(R.id.title);
        intentList = new ArrayList<>();

        listView.setTextFilterEnabled(true);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * When item from recipe list is clicked, set intent and start RecipeDetails activity
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        RecipeDetails.class);
                intent.putExtra("title", intentList.get(i).getTitle());
                intent.putExtra("image", intentList.get(i).getImgpath());
                intent.putExtra("tags", intentList.get(i).getTags());
                intent.putExtra("protein", intentList.get(i).getProtein());
                intent.putExtra("fat", intentList.get(i).getFat());
                intent.putExtra("carbs", intentList.get(i).getCarb());
                intent.putExtra("time", intentList.get(i).getTimeTaken());
                intent.putExtra("serves", intentList.get(i).getServings());

                intent.putExtra("ingredients", intentList.get(i).getIngredientFB());
                intent.putExtra("instructions", intentList.get(i).getInstructions());
                getActivity().startActivity(intent);
            }
        });

        searchTXT = root.findViewById(R.id.searchTxt);
        searchTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * When search text is changed, display filtered list
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HomeFragment.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return root;
    }

    /**
     * Function for getting user recipes
     * If user's inventory contains all ingredients needed for that particular recipe
     * Add that recipe to the user recipe list
     * @return
     */
    private ArrayList<Recipe> getUserRecipes(){
        ArrayList<Recipe> result = new ArrayList<>();

        User thisUser = userDBHelper.getThisUser();
        ArrayList<String> inventory = thisUser.getInventory();
        if(inventory == null){
            return result;
        }

        for (Recipe recipe : recipeArrayList) {
            Log.d("RECIPE NAME", recipe.getTitle());
            ArrayList<Ingredients> recipeIngredients = new ArrayList<>();
            recipeIngredients = recipe.setIngredientsString(recipe.getIngredientFB());

            int recipeIngredients_total = recipeIngredients.size();
            int count = 0;
            for (Ingredients recipeIng : recipeIngredients) {
                String[] words = recipeIng.getIngredientsName().split("\\W+");
                for (int i = 0; i < words.length; i++) {
                    for(String userIng : inventory){
                        if (words[i].equalsIgnoreCase(userIng)) {
                            Log.d("MATCH ing", userIng);
                            count++;
                        }
                    }
                }
            }
            if (recipeIngredients_total == count) {
                result.add(recipe);
            }
        }

        return result;

    }


}