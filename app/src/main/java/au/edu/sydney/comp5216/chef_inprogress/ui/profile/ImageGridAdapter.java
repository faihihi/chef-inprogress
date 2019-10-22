package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;

public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Recipe> recipes;

    public ImageGridAdapter(Context context, ArrayList<Recipe> recipes){
        this.mContext = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Recipe getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recipe recipe = getItem(position);
        Log.d("CHECKKKKK", recipe.getTitle());
        Log.d("CHECK imgpath", recipe.getImgpath());

        // Check if an existing view is being reused, otherwise inflate the view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.recipe_image_list, null);
        }

        // Lookup view for data population
//        ImageView image = (ImageView) convertView.findViewById(R.id.calendarView);
        TextView name = (TextView) convertView.findViewById(R.id.calendar_recipeName);

        // Populate the data into the template view using the data object
//        image.setImageResource(item.getIcon());
        name.setText(recipe.getTitle());

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.calendar_recipeImage))
                .execute(recipe.getImgpath());

        // Return the completed view to render on screen
        return convertView;
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
