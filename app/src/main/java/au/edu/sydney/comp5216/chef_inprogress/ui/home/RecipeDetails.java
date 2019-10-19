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

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;

public class RecipeDetails extends AppCompatActivity {
    ImageView image;
    TextView title, protein, fat, carbs, time_serves;
    LinearLayout ingredients_lv, instructions_lv;

    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

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

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        new DownloadImageTask((ImageView) image).execute(intent.getStringExtra("image"));

        protein.setText(String.valueOf(intent.getIntExtra("protein", 0)) + " g");
        fat.setText(String.valueOf(intent.getIntExtra("fat", 0)) + " g");
        carbs.setText(String.valueOf(intent.getIntExtra("carbs", 0)) + " g");

        String timeserves = intent.getStringExtra("time") + " | " + String.valueOf(intent.getIntExtra("serves", 0) + " servings");
        time_serves.setText(timeserves);

        ArrayList<Ingredients> ingredients = intent.getParcelableArrayListExtra("ingredients");
        ingredientsAdapter = new IngredientsAdapter(this, ingredients);

        ingredients_lv = (LinearLayout)findViewById(R.id.ingredients_ll);
        for (int i = 0; i < ingredientsAdapter.getCount(); i++) {
            View view = ingredientsAdapter.getView(i, null, ingredients_lv);
            ingredients_lv.addView(view);
        }

        // Get instructions
        String instructionsString = intent.getStringExtra("instructions");
        Recipe ins = new Recipe();
        ins.setInstructionsString(instructionsString);
        ArrayList<Ingredients> instructions = new ArrayList<>();
        instructions = ins.getInstructionsList();
        for(Ingredients i : instructions){
            Log.d("Check arraylist", i.getDescription());
        }
        instructionsAdapter = new InstructionsAdapter(this, instructions);

        instructions_lv = (LinearLayout)findViewById(R.id.instructions_ll);
        for (int i = 0; i < instructionsAdapter.getCount(); i++) {
            View view = instructionsAdapter.getView(i, null, instructions_lv);
            instructions_lv.addView(view);
        }



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
