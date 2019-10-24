package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

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
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * Adapter for favorite list
 */
public class FavoriteAdapter extends BaseAdapter {

    ArrayList<Favorite> favorites,favoritesAll;
    private Context context;

    /**
     * Constructor
     * @param mcontext
     * @param arrayList
     */
    public FavoriteAdapter(Context mcontext, ArrayList<Favorite> arrayList){
        context = mcontext;
        favorites = arrayList;
        favoritesAll = new ArrayList<>(favorites);
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        try {
            int size = favorites.size();
            return size;
        }catch (NullPointerException ex) {
            return 0;
        }
    }

    /**
     * Get item
     * @param position
     * @return
     */
    @Override
    public Favorite getItem(int position) {
        return favorites.get(position);
    }

    /**
     * Get item ID
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Favorite favorite = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favorite_item,parent,false);
        }

        // Lookup view for data population
        ImageView pic = (ImageView) convertView.findViewById(R.id.picture);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        final ImageView heartBtn = (ImageView) convertView.findViewById(R.id.heart_btn);

        // Populate the data into the template view using the data object
        title.setText(favorite.getTitle());
        time.setText(favorite.getTime());

        Glide.with(pic.getContext())
                .load(favorite.getPic())
                .into(pic);

        /**
         * Set on click listener of heart button
         */
        heartBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * When heart button is clicked, remove recipe from favorite list
             * @param view
             */
            @Override
            public void onClick(View view) {
                heartBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                UserDBHelper userDBHelper = new UserDBHelper(context);
                User c = userDBHelper.getThisUser();

                c.getFavorites().remove(favorites.get(position).getTitle());
                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());

                // Save favorites to firebase
                new FirebaseDatabaseHelper("user").updateUser(c.getKey(),  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataisLoaded(List<User> users, List<String> keys) {}

                    @Override
                    public void DataIsInserted(User user, String key) {}

                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {}
                });

                // Save favorites to local db
                userDBHelper.deleteAll();
                userDBHelper.insertData(c.getKey(), c.getName(), c.getEmail(), c.getInventoryStr(), c.getShoppingStr(), c.getShoppingcheckStr(), c.getCompletedStr(), c.getCompletedDateStr(), c.getFavoriteStr());
            }
        });

        return convertView;
    }

    /**
     * Getting filter
     * @return
     */
    public Filter getFilter(){
        return customFilter;
    }

    /**
     * Function for filtering favorite list from Search query text
     */
    private Filter customFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Favorite> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                Toast.makeText(context,"NOTHING FOUND",Toast.LENGTH_LONG);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Favorite item: favorites){
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
         * Publish result of filtered list
         * @param constraint
         * @param results
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            favorites.clear();
            if(results.count!=0) {
                favorites.addAll((ArrayList<Favorite>) results.values);
                Log.v("publishResults", Integer.toString(favorites.size()));
                notifyDataSetChanged();
            }else {
                favorites.addAll(favoritesAll);
                notifyDataSetChanged();
            }
        }
    };

}
