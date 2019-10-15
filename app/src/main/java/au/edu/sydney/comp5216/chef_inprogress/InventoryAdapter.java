package au.edu.sydney.comp5216.chef_inprogress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends BaseAdapter implements Filterable {

    ArrayList<Inventory> inventory;
    ArrayList<Inventory> filterList;
    CustomFilter filter;

    private Context context;

    public InventoryAdapter(Context context, ArrayList<Inventory> items) {
        this.context = context;
        this.inventory = items;
        this.filterList = inventory;
    }

    @Override
    public int getCount() {
        try {
            int size = inventory.size();
            return size;
        }catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Inventory getItem(int i) {
        return inventory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Inventory item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.inventory_list, null);
        }

        // Lookup view for data population
        ImageView icon = (ImageView) convertView.findViewById(R.id.inventoryItemImg);
        TextView name = (TextView) convertView.findViewById(R.id.inventoryItemName);

        // Populate the data into the template view using the data object
        icon.setImageResource(item.getIcon());
        name.setText(item.getItemName());

        // Return the completed view to render on screen
        return convertView;
    }

    public Filter getFilter(){
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<Inventory> filters = new ArrayList<>();

                // Filtering
                for(int i=0;i< filterList.size();i++){
                    if(filterList.get(i).getItemName().toUpperCase().contains(constraint)){
//                        Inventory item = new Inventory(filterList.get(i).getId(), filterList.get(i).getItemName(), filterList.get(i).getCategory(), filterList.get(i).getIcon(), filterList.get(i).getUserInventory());
                        filters.add(filterList.get(i));
                    }
                }

                results.count = filters.size();
                results.values = filters;
            } else{
                results.count = filterList.size();
                results.values = filterList;
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            inventory = (ArrayList<Inventory>) results.values;
            notifyDataSetChanged();
        }
    }

}
