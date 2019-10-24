package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.Ingredients;
import au.edu.sydney.comp5216.chef_inprogress.R;

/**
 * IngredientsAdapter for displaying ingredients list in Recipe Details Page
 */
public class IngredientsAdapter extends BaseAdapter {

    ArrayList<Ingredients> ingredients;
    private Context context;

    /**
     * Constructor
     * @param mcontext
     * @param arrayList
     */
    public IngredientsAdapter(Context mcontext, ArrayList<Ingredients> arrayList){
        context = mcontext;
        ingredients = arrayList;
    }

    /**
     * Get count
     * @return
     */
    @Override
    public int getCount() {
        try {
            int size = ingredients.size();
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
    public Ingredients getItem(int i) {
        return ingredients.get(i);
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
     * Get view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ingredients ingredient = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ingredient_list,parent,false);
        }

        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.ingredient_name);
        TextView quantity = (TextView) convertView.findViewById(R.id.ingredient_quantity);

        // Populate the data into the template view using the data object
        name.setText(ingredient.getIngredientsName());
        quantity.setText(ingredient.getDescription());

        return convertView;
    }

}
