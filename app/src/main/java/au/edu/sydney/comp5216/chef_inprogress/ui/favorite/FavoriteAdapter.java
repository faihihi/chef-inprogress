package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;

public class FavoriteAdapter extends ArrayAdapter<Favorite> implements Filterable {

    ArrayList<Favorite> arrayList,arrayList2;
    CustomFilter customFilter;

    public FavoriteAdapter(@NonNull Context context, ArrayList<Favorite> favorites) {
        super(context, 0,favorites);
        this.arrayList2 = favorites;
        this.arrayList = favorites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Favorite favorite = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_item,parent,false);
        }

        // Lookup view for data population
        ImageView pic = (ImageView) convertView.findViewById(R.id.picture);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        // Populate the data into the template view using the data object
        title.setText(favorite.getTitle());
        time.setText(favorite.getTime());

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.picture))
                .execute(favorite.getPic());

        return convertView;
    }

    @Override
    public Filter getFilter(){
        if(customFilter == null){
            customFilter = new CustomFilter();
        }
        return customFilter;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
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


    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString();

                ArrayList<Favorite> filters = new ArrayList<>();

                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getTitle().contains(constraint)) {
                        Favorite favorite = new Favorite(arrayList.get(i).getTitle(), arrayList.get(i).getTime(), arrayList.get(i).getPic());
                        filters.add(favorite);
                    }
                }

                results.count = filters.size();
                results.values = filters;
            }else {
                results.count = 0;
                results.values = null;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList2 = (ArrayList<Favorite>) results.values;
            notifyDataSetChanged();
        }
    }
}
