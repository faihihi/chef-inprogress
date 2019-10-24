package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;

/**
 * ImageGrid adapter for recipe image list view
 */
public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Recipe> recipes;

    /**
     * Constructor
     * @param context
     * @param recipes
     */
    public ImageGridAdapter(Context context, ArrayList<Recipe> recipes){
        this.mContext = context;
        this.recipes = recipes;
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        return recipes.size();
    }

    /**
     * Get item
     * @param i
     * @return
     */
    @Override
    public Recipe getItem(int i) {
        return recipes.get(i);
    }

    /**
     * Get item ID
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Get view of each recipe
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe recipe = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.recipe_image_list, null);
        }

        // Lookup view for data population
        ImageView image = (ImageView) convertView.findViewById(R.id.calendar_recipeImage);
        TextView name = (TextView) convertView.findViewById(R.id.calendar_recipeName);

        // Populate the data into the template view using the data object
        name.setText(recipe.getTitle());
        Glide.with(image.getContext())
                .load(recipe.getImgpath())
                .into(image);

        // Return the completed view to render on screen
        return convertView;
    }

}
