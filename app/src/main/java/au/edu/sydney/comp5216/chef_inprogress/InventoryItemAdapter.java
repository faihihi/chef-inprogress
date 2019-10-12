package au.edu.sydney.comp5216.chef_inprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InventoryItemAdapter extends ArrayAdapter<InventoryItem> {
    public InventoryItemAdapter(Context context, ArrayList<InventoryItem> items) {
        super(context, 0, items);
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
        InventoryItem item = getItem(position);

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

}
