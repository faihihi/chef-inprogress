package au.edu.sydney.comp5216.chef_inprogress;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    private InventoryDBHelper inventoryDBHelper;
    private UserDBHelper userDBHelper;

    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

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
        userDBHelper = new UserDBHelper(MainActivity.this);
        userDBHelper.deleteAll();

        new FirebaseDatabaseHelper("user").getUserInfo(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataisLoaded(List<User> users, List<String> keys) {
                int idx = 0;
                String key = "";
                for(int i=0;i<users.size();i++){
                    if(users.get(i).getEmail().equalsIgnoreCase(fbUser.getEmail())){
                        idx = i;
                        key = keys.get(i);
                    }
                }
                User c = users.get(idx);
                User newUser = new User (key, c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());

                userDBHelper.insertData(newUser.getKey(), newUser.getName(), newUser.getEmail(), newUser.getInventoryStr(), newUser.getShoppingStr(), newUser.getShoppingcheckStr(), newUser.getCompletedStr(), newUser.getCompletedDateStr(), newUser.getFavoriteStr());

                ArrayList<String> inventory = newUser.getInventory();
                for(String item: inventory){
                    inventoryDBHelper.saveToUserInventoryWithTitle(item);
                }
            }

            @Override
            public void DataIsInserted(User user, String key) {

            }

            @Override
            public void DataIsUpdated() {
            }

            @Override
            public void DataIsDeleted() {
            }
        });



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
            inventoryDBHelper.insertData("Tomato", "fruitveg", R.drawable.tomato, 0);
            inventoryDBHelper.insertData("Grapes", "fruitveg", R.drawable.grapes, 0);
            inventoryDBHelper.insertData("Peas", "fruitveg", R.drawable.peas, 0);
            inventoryDBHelper.insertData("Lime", "fruitveg", R.drawable.lime, 0);

            inventoryDBHelper.insertData("Cheese","grocery",R.drawable.cheese,0);
            inventoryDBHelper.insertData("Salt","grocery",R.drawable.salt,0);
            inventoryDBHelper.insertData("Mustard","grocery",R.drawable.mustard,0);
            inventoryDBHelper.insertData("Ketchup","grocery",R.drawable.ketchup,0);
            inventoryDBHelper.insertData("Bread","grocery",R.drawable.bread,0);
            inventoryDBHelper.insertData("Baguette","grocery",R.drawable.baguette,0);
            inventoryDBHelper.insertData("Pasta","grocery",R.drawable.pasta,0);
            inventoryDBHelper.insertData("Oil","grocery",R.drawable.olive_oil,0);
            inventoryDBHelper.insertData("Pepper","grocery",R.drawable.pepper,0);
            inventoryDBHelper.insertData("Honey","grocery",R.drawable.honey,0);

            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }


}
