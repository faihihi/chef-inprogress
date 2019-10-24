package au.edu.sydney.comp5216.chef_inprogress.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;

/**
 * TagAdapter for displaying tags list
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {
    private ArrayList<String> tags;
    private Context context;
    private LayoutInflater mInflater;

    /**
     * Provide a reference to the views for each data item
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(View v) {
            super(v);

            textView = v.findViewById(R.id.tag);
        }
    }

    /**
     * Provide a suitable constructor
     * @param context
     * @param tags
     */
    public TagAdapter(Context context, ArrayList<String> tags) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.tags = tags;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(tags.get(position));
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return
     */
    @Override
    public int getItemCount() {
        return tags.size();
    }

}
