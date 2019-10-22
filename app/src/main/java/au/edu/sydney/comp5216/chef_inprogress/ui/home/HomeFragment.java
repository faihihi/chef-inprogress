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

public class HomeFragment extends Fragment {
    private InventoryDBHelper inventoryDBHelper;
    private UserDBHelper userDBHelper;

    private ArrayList<Recipe> recipeArrayList, userRecipe, intentList;
    private ArrayList<Inventory> userInventory;

    HomeAdapter arrayAdapter;
    EditText searchTXT;
    TextView pageTitle;
    private ListView listView;

    private ArrayList<String> favorites, inventoryAL;
    private String loggedInEmail;
    boolean finished = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        inventoryDBHelper = new InventoryDBHelper(getContext());
        userDBHelper = new UserDBHelper(getContext());


        userInventory = new ArrayList<>();
        userInventory = inventoryDBHelper.getItemsInUserInventory();
//        for(Inventory item: userInventory){
//            Log.d("USER INVENTORYYY", item.getItemName());
//        }

        userRecipe = new ArrayList<>();
        recipeArrayList = new ArrayList<>();

        arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);

        listView = root.findViewById(R.id.recipeList);

        // Uncomment after
        new FirebaseRecipeDBHelper().getAllRecipe(new FirebaseRecipeDBHelper.DataStatus() {
            @Override
            public void RecipeisLoaded(List<Recipe> recipes, List<String> keys) {
                for (Recipe recipe : recipes) {
                    recipeArrayList.add(recipe);
                    arrayAdapter.notifyDataSetChanged();
                }

                userRecipe = getUserRecipes();
//                userRecipe = getUserRecipe();

                if (userRecipe.size() > 0) {
                    arrayAdapter = new HomeAdapter(getContext(), userRecipe);
                    listView.setAdapter(arrayAdapter);
                    intentList = userRecipe;
                    pageTitle.setText("Here's your Recipes");
                } else {
                    arrayAdapter = new HomeAdapter(getContext(), recipeArrayList);
                    intentList = recipeArrayList;
                    pageTitle.setText("Recommended Recipes");
                }
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

    private ArrayList<Recipe> getUserRecipes(){
        ArrayList<Recipe> result = new ArrayList<>();

        User thisUser = userDBHelper.getThisUser();
        ArrayList<String> inventory = thisUser.getInventory();

        for (Recipe recipe : recipeArrayList) {
            ArrayList<Ingredients> recipeIngredients = new ArrayList<>();
            recipeIngredients = recipe.setIngredientsString(recipe.getIngredientFB());

            int recipeIngredients_total = recipeIngredients.size();
            int count = 0;
            for (Ingredients recipeIng : recipeIngredients) {
                String[] words = recipeIng.getIngredientsName().split("\\W+");
                for (int i = 0; i < words.length; i++) {
                    for(String userIng : inventory){
                        if (words[i].equalsIgnoreCase(userIng)) {
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

    private ArrayList<Recipe> getUserRecipe() {
        ArrayList<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipeArrayList) {
            ArrayList<Ingredients> recipeIngredients = new ArrayList<>();
            recipeIngredients = recipe.setIngredientsString(recipe.getIngredientFB());

            int recipeIngredients_total = recipeIngredients.size();
            int count = 0;
            for (Ingredients recipeIng : recipeIngredients) {
                String[] words = recipeIng.getIngredientsName().split("\\W+");
                for (int i = 0; i < words.length; i++) {

                    for (Inventory item : userInventory) {
                        if (words[i].equalsIgnoreCase(item.getItemName())) {
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