package au.edu.sydney.comp5216.chef_inprogress;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    private InventoryDBHelper inventoryDBHelper;
    private RecipeDBHelper recipeDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_inventory, R.id.navigation_add, R.id.navigation_favorite, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        inventoryDBHelper = new InventoryDBHelper(MainActivity.this);
        recipeDBHelper = new RecipeDBHelper(MainActivity.this);

        // Save all the recipes to the database
        String title = "Chickpea, lemon and rocket pesto pasta";
        String imgpath = "https://img.taste.com.au/MUDQMCwh/w720-h480-cfill-q80/taste/2016/11/chickpea-lemon-and-rocket-pesto-pasta-91973-1.jpeg";
        String[] tags = {"vegetarian", "capable cook", "family dinner"};

        int protein = 18;
        int fat = 15;
        int carb = 66;
        String time = "20 mins";
        int serves = 4;
        ArrayList<Ingredients> ingredients = new ArrayList<>();
        ingredients.add(new Ingredients("wholegrain spagetti", "300g"));
        ingredients.add(new Ingredients("chickpeas, rinsed, drained", "400g"));
        ingredients.add(new Ingredients("baby rocket leaves", "120g"));
        ingredients.add(new Ingredients("fresh basil leaves", "1/2 cup"));

        String[] instructions = {
                "Cook the pasta in a large saucepan of boiling salted water following the packet directions or until al dente. Drain, reserving 1/4 cup cooking liquid. Return pasta to saucepan.",
                "Meanwhile process chickpeas, rocket, basil, pecorino, garlic, lemon juice and water until finely chopped. Season. Add oil in a slow, steady stream until well combined. Reserve 1/4 cup pesto mixture.",
                "Add the cooking water, peppers and remaining pesto to the pasta. Toss well to combine. Divide the pasta mixture among serving bowls. Top each with a spoonful of reserved pesto. Sprinkle with extra pecorino and micro basil."
        };

        Recipe recipe = new Recipe(title, imgpath, tags, protein, fat, carb, time, serves, ingredients, instructions);

        recipeDBHelper.deleteAll();
        recipeDBHelper.insertData(title, imgpath, recipe.getTagsString(), protein, fat, carb, time, serves, recipe.getIngredientsListString(), recipe.getInstructionsString());


        prefs = getSharedPreferences("au.edu.sydney.comp5216.chef_inprogress", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Log.d("Check first run", "YESSSSS RAN!!");
            // Do first run stuff here then set 'firstrun' as false

            // Save all the inventory items to database
            inventoryDBHelper.insertData("Fish","meat",R.drawable.fish,0);
            inventoryDBHelper.insertData("Beef","meat",R.drawable.steak,0);
            inventoryDBHelper.insertData("Chicken","meat",R.drawable.meat,0);

            inventoryDBHelper.insertData("Apple", "fruitveg", R.drawable.apple, 0);
            inventoryDBHelper.insertData("Carrot", "fruitveg", R.drawable.carrot, 0);

            inventoryDBHelper.insertData("Cheese","grocery",R.drawable.cheese,0);
            inventoryDBHelper.insertData("Salt","grocery",R.drawable.salt,0);
            inventoryDBHelper.insertData("Mustard","grocery",R.drawable.mustard,0);
            inventoryDBHelper.insertData("Ketchup","grocery",R.drawable.ketchup,0);
            inventoryDBHelper.insertData("Bread","grocery",R.drawable.bread,0);
            inventoryDBHelper.insertData("Baguette","grocery",R.drawable.baguette,0);
            inventoryDBHelper.insertData("Pasta","grocery",R.drawable.pasta,0);



            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }


}
