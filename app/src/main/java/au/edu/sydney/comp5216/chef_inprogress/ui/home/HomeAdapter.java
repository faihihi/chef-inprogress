package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * Home adapter for recipe listview
 */
public class HomeAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes,recipesAll;
    private Context context;

    /**
     * Constructor
     * @param mcontext
     * @param arrayList
     */
    public HomeAdapter(Context mcontext, ArrayList<Recipe> arrayList){
        context = mcontext;
        recipes = arrayList;
        recipesAll = new ArrayList<>(recipes);
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        try {
            int size = recipes.size();
            return size;
        }catch (NullPointerException ex) {
            return 0;
        }
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

        // Setting display for recipe that is marked as favorite
        if(favorites != null){
            for(String fav: favorites){
                if(recipes.get(position).getTitle().equalsIgnoreCase(fav)){
                    // Favorite already exist
                    checkIfAlreadyFav = true;
                }
            }
        }

        final ImageView heartBtn = (ImageView) convertView.findViewById(R.id.heart_btn);
        if(checkIfAlreadyFav){
            heartBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        // Populate the data into the template view using the data object
        title.setText(recipe.getTitle());
        time.setText(recipe.getTimeTaken());

        Glide.with(pic.getContext())
                .load(recipe.getImgpath())
                .into(pic);

        heartBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * When heart button is clicked, add recipe to user's favorite list
             * @param view
             */
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

                // Change heart display
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

    /**
     * Get filture
     * @return
     */
    public Filter getFilter(){
        return customFilter;
    }

    private Filter customFilter = new Filter() {
        /**
         * Function for filtering recipe list when search bar is entered
         * @param constraint
         * @return
         */
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

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        /**
         * Publish filtered list result
         * @param constraint
         * @param results
         */
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

}
