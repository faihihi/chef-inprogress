package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

public class HomeAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes,recipesAll;
    private Context context;

    public HomeAdapter(Context mcontext, ArrayList<Recipe> arrayList){
        context = mcontext;
        recipes = arrayList;
        recipesAll = new ArrayList<>(recipes);
    }



    @Override
    public int getCount() {
        try {
            int size = recipes.size();
            return size;
        }catch (NullPointerException ex) {
            return 0;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Recipe recipe = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.home_item,parent,false);
        }

        // Lookup view for data population
        ImageView pic = (ImageView) convertView.findViewById(R.id.picture);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        UserDBHelper userDBHelper = new UserDBHelper(context);
        User c = userDBHelper.getThisUser();


        ArrayList<String> favorites = c.getFavorites();
        boolean checkIfAlreadyFav = false;
        for(String fav: favorites){
            if(recipes.get(position).getTitle().equalsIgnoreCase(fav)){
                // Favorite already exist
                checkIfAlreadyFav = true;
            }
        }
        final ImageView heartBtn = (ImageView) convertView.findViewById(R.id.heart_btn);
        if(checkIfAlreadyFav){
            heartBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        // Populate the data into the template view using the data object
        title.setText(recipe.getTitle());
        time.setText(recipe.getTimeTaken());

        new HomeAdapter.DownloadImageTask((ImageView) convertView.findViewById(R.id.picture))
                .execute(recipe.getImgpath());

        heartBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserDBHelper userDBHelper = new UserDBHelper(context);
                User c = userDBHelper.getThisUser();

                ArrayList<String> favorites = c.getFavorites();
                boolean checkIfAlreadyFav = false;
                for(String fav: favorites){
                    if(recipes.get(position).getTitle().equalsIgnoreCase(fav)){
                        // Favorite already exist
                        checkIfAlreadyFav = true;
                    }
                }

                if(!checkIfAlreadyFav){
                    heartBtn.setImageResource(R.drawable.ic_favorite_black_24dp);

                    c.getFavorites().add(recipes.get(position).getTitle());
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

                }
            }
        });

        return convertView;
    }

    public Filter getFilter(){
        return customFilter;
    }

    private Filter customFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipe> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                //filteredList.addAll(arrayList);
                Toast.makeText(context,"NOTHING FOUND",Toast.LENGTH_LONG);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Recipe item: recipes){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

//            Log.v("list",filteredList.get(0).getTitle());
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
//            Log.v("value",Integer.toString(results.count));

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipes.clear();
            if(results.count!=0) {
                recipes.addAll((ArrayList<Recipe>) results.values);
                Log.v("publishResults", Integer.toString(recipes.size()));
                notifyDataSetChanged();
            }else {
                recipes.addAll(recipesAll);
                notifyDataSetChanged();
            }
        }
    };

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
