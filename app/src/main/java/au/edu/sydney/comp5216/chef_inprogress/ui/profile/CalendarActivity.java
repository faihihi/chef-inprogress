package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseRecipeDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;

import static android.view.View.VISIBLE;

public class CalendarActivity extends Activity {
    private TextView date, proteins, fat, carbs;
    private GridView gridView;

    private ArrayList<String> recipesMade;
    private ArrayList<Recipe> recipesList;
    private ImageGridAdapter imageGridAdapter;

    int protein_sum, fat_sum, carbs_sum;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_modal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*.8));

        date = findViewById(R.id.date);
        proteins = findViewById(R.id.protein_intake);
        fat = findViewById(R.id.fat_intake);
        carbs = findViewById(R.id.carbs_intake);

        Intent intent = getIntent();
        date.setText(intent.getStringExtra("date"));
        recipesMade = intent.getStringArrayListExtra("recipes");
        for(String r: recipesMade){
            Log.d("MADEEE", r);
        }

        recipesList = new ArrayList<>();
        protein_sum = 0;
        fat_sum = 0;
        carbs_sum = 0;
        for(final String recipeTitle: recipesMade){
            // Query for recipe by title
            new FirebaseRecipeDBHelper().getRecipeByTitle(recipeTitle, new FirebaseRecipeDBHelper.DataStatus() {
                @Override
                public void RecipeisLoaded(List<Recipe> recipes, List<String> keys) {
                    for (Recipe recipe : recipes) {
                        Log.d("recipeeeeeee", recipe.getTitle());
                        recipesList.add(recipe);
                        imageGridAdapter.notifyDataSetChanged();

                        protein_sum = protein_sum + recipe.getProtein();
                        fat_sum = fat_sum + recipe.getFat();
                        carbs_sum = carbs_sum + recipe.getCarb();

                        proteins.setText(String.valueOf(protein_sum) + " g");
                        fat.setText(String.valueOf(fat_sum) + " g");
                        carbs.setText(String.valueOf(carbs_sum) + " g");
                    }
                }

                @Override
                public void DataIsUpdated() {

                }
            });

        }

        gridView = (GridView) findViewById(R.id.recipe_images);

        if(recipesMade.size() > 0){
            gridView.setVisibility(VISIBLE);
            imageGridAdapter = new ImageGridAdapter(this, recipesList);
            gridView.setAdapter(imageGridAdapter);

        } else{
            gridView.setVisibility(View.GONE);
            TextView msg = findViewById(R.id.nodish_msg);
            msg.setVisibility(VISIBLE);
        }

    }
}
