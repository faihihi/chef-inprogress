package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

public class RecipeDetails extends AppCompatActivity {
    private ImageView image;
    private TextView title, protein, fat, carbs, time_serves;
    private LinearLayout ingredients_lv, instructions_lv;

    private IngredientsAdapter ingredientsAdapter;
    private InstructionsAdapter instructionsAdapter;

    private ExtendedFloatingActionButton complete_btn;

    private String recipeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        image = (ImageView) findViewById(R.id.recipe_image);

        title = (TextView) findViewById(R.id.recipe_title);
        protein = (TextView) findViewById(R.id.protein_value);
        fat = (TextView) findViewById(R.id.fat_value);
        carbs = (TextView) findViewById(R.id.carbs_value);
        time_serves = (TextView) findViewById(R.id.time_serves);
        complete_btn = (ExtendedFloatingActionButton) findViewById(R.id.complete_btn);

        Intent intent = getIntent();
        recipeTitle = intent.getStringExtra("title");
        title.setText(recipeTitle);
        new DownloadImageTask((ImageView) image).execute(intent.getStringExtra("image"));

        protein.setText(String.valueOf(intent.getIntExtra("protein", 0)) + " g");
        fat.setText(String.valueOf(intent.getIntExtra("fat", 0)) + " g");
        carbs.setText(String.valueOf(intent.getIntExtra("carbs", 0)) + " g");

        String timeserves = intent.getStringExtra("time") + " | " + String.valueOf(intent.getIntExtra("serves", 0) + " servings");
        time_serves.setText(timeserves);

        ArrayList<String> ingredientsList = intent.getStringArrayListExtra("ingredients");
        Recipe ins = new Recipe();
        ArrayList<Ingredients> ingredients = ins.setIngredientsString(ingredientsList);
        ingredientsAdapter = new IngredientsAdapter(this, ingredients);

        ingredients_lv = (LinearLayout)findViewById(R.id.ingredients_ll);
        for (int i = 0; i < ingredientsAdapter.getCount(); i++) {
            View view = ingredientsAdapter.getView(i, null, ingredients_lv);
            ingredients_lv.addView(view);
        }

        // Get instructions
        ArrayList<String> instructionsList = intent.getStringArrayListExtra("instructions");
//        ins = new Recipe();
        ArrayList<Ingredients> instructions = new ArrayList<>();
        instructions = ins.setInstructionsString(instructionsList);
        for(Ingredients i : instructions){
            Log.d("Check arraylist", i.getDescription());
        }
        instructionsAdapter = new InstructionsAdapter(this, instructions);

        instructions_lv = (LinearLayout)findViewById(R.id.instructions_ll);
        for (int i = 0; i < instructionsAdapter.getCount(); i++) {
            View view = instructionsAdapter.getView(i, null, instructions_lv);
            instructions_lv.addView(view);
        }

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDBHelper userDBHelper = new UserDBHelper(getApplicationContext());
                User c = userDBHelper.getThisUser();

                c.getCompletedrecipe().add(recipeTitle);

                long millis=System.currentTimeMillis();
                Date now = new Date(millis);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String strDate = dateFormat.format(now);
                c.getCompletedDate().add(strDate);

                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());

                // Save favorites to firebase
                new FirebaseDatabaseHelper("user").updateUser(c.getKey(),  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataisLoaded(List<User> users, List<String> keys) {}

                    @Override
                    public void DataIsInserted(User user, String key) {

                    }


                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {}
                });

                // Save favorites to local db
                userDBHelper.deleteAll();
                userDBHelper.insertData(c.getKey(), c.getName(), c.getEmail(), c.getInventoryStr(), c.getShoppingStr(), c.getShoppingcheckStr(), c.getCompletedStr(), c.getCompletedDateStr(), c.getFavoriteStr());

                StyleableToast.makeText(getApplicationContext(),"Recipe completion successfully logged", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap foodPic = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                foodPic = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                e.printStackTrace();
            }
            return foodPic;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }
    }

}
