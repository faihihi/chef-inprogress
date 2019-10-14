package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class InventoryFragment_lv2 extends Fragment {
    ArrayList<InventoryItem> userInventoryList = new ArrayList<>();
    ArrayAdapter<InventoryItem> userItemsAdapter;

    GridView gridView;

    private InventoryDBHelper inventoryDBHelper;
    private ArrayList<Integer> selectedPositions, selectedAdapterPositions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2,container,false);

        inventoryDBHelper = new InventoryDBHelper(getContext());
        userInventoryList = inventoryDBHelper.getItemsInUserInventory();

        // Initialize the custom adapter and connect listView with adapter
        userItemsAdapter = new InventoryItemAdapter(getContext(), userInventoryList);
        gridView = (GridView) view.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(userItemsAdapter);

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());

        selectedPositions = new ArrayList<>();
        selectedAdapterPositions = new ArrayList<>();

        return view;
    }

    //multi select mode codes
    private class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("1 item selected");
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()){
                case R.id.action_save:
                    Toast.makeText(getContext(), "Deleted item(s) from Inventory",Toast.LENGTH_SHORT).show();
                    for(Integer position: selectedPositions){
                        inventoryDBHelper.removeFromUserInventory(position);
                    }

                    int count = 0;
                    for(Integer position: selectedAdapterPositions){
                        position = position - count;
                        if(position < 0){
                            position = 0;
                        }
                        userItemsAdapter.remove(userInventoryList.get(position));
                        count++;
                        userItemsAdapter.notifyDataSetChanged();
                    }

                    for(int i=0;i<gridView.getChildCount();i++){
                        ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.remove_selector);
                        iv.setVisibility(View.INVISIBLE);
                    }
                    mode.finish();

                    break;
                case R.id.action_delete:
                    break;
                default:
                    break;
            }

            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            int selectCount = gridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("1 item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }

            if(checked){
                selectedPositions.add(userInventoryList.get(position).getId());
                selectedAdapterPositions.add(position);
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.remove_selector);
                iv.setVisibility(View.VISIBLE);
            }else{
                for(int i=0;i<selectedPositions.size();i++){
                    if(selectedPositions.get(i) == position){
                        selectedPositions.remove(i);
                    }
                }

                for(int i=0;i<selectedAdapterPositions.size();i++){
                    if(selectedAdapterPositions.get(i) == position){
                        selectedAdapterPositions.remove(i);
                    }
                }

                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.remove_selector);
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }
}
