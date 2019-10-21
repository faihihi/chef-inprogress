package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

public class FavoriteAdapter extends BaseAdapter {

    ArrayList<Favorite> favorites,favoritesAll;
    private Context context;

    public FavoriteAdapter(Context mcontext, ArrayList<Favorite> arrayList){
        context = mcontext;
        favorites = arrayList;
        favoritesAll = new ArrayList<>(favorites);
    }

    @Override
    public int getCount() {
        try {
            int size = favorites.size();
            return size;
        }catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Favorite getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

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

        new FavoriteAdapter.DownloadImageTask((ImageView) convertView.findViewById(R.id.picture))
                .execute(favorite.getPic());

        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heartBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                UserDBHelper userDBHelper = new UserDBHelper(context);
                User c = userDBHelper.getThisUser();

                c.getFavorites().remove(favorites.get(position).getTitle());
                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());

                // Save favorites to firebase
                new FirebaseDatabaseHelper("user").updateUser("1",  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataisLoaded(List<User> users, List<String> keys) {}

                    @Override
                    public void DataIsInserted() {}

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

    public Filter getFilter(){
        return customFilter;
    }

    private Filter customFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Favorite> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                //filteredList.addAll(arrayList);
                Toast.makeText(context,"NOTHING FOUND",Toast.LENGTH_LONG);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Favorite item: favorites){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            Log.v("list",filteredList.get(0).getTitle());
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            Log.v("value",Integer.toString(results.count));

            return results;
        }

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
