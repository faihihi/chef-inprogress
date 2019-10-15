package au.edu.sydney.comp5216.chef_inprogress;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class InventoryItemAdapter extends ArrayAdapter<Inventory> implements Filterable {
    ArrayList<Inventory> inventory;
    ArrayList<Inventory> filterList;
    CustomFilter filter;

    private Context context;


    public InventoryItemAdapter(Context context, ArrayList<Inventory> items) {
        super(context, 0, items);
        this.context = context;
        this.inventory = items;
        this.filterList = inventory;
    }

    /**
     * Get view using Item object
     * @param convertView
     * @param parent
     * @param position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Inventory item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_list, parent, false);
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
