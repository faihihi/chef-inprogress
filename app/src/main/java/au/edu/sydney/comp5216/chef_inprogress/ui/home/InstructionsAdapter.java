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
 * InstructionsAdapter for displaying list of instructions in RecipeDetails
 */
public class InstructionsAdapter extends BaseAdapter {

    ArrayList<Ingredients> ingredients;
    private Context context;

    /**
     * Constructor
     * @param mcontext
     * @param arrayList
     */
    public InstructionsAdapter(Context mcontext, ArrayList<Ingredients> arrayList){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.instructions_list,parent,false);
        }

        // Lookup view for data population
        TextView step = (TextView) convertView.findViewById(R.id.step);
        TextView instruction = (TextView) convertView.findViewById(R.id.instructions);

        // Populate the data into the template view using the data object
        step.setText(ingredient.getIngredientsName());
        instruction.setText(ingredient.getDescription());

        return convertView;
    }
}
