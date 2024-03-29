package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryAdapter;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * InventoryFragment_lv2 is loaded when "Inventory" tab is selected
 */
public class InventoryFragment_lv2 extends Fragment {
    ArrayList<Inventory> userInventoryList = new ArrayList<>();
    private ArrayList<Inventory> selectedItem, allInventory;
    private InventoryAdapter userItemsAdapter;

    private MultiChoiceModeListener myModeListener;
    private ActionMode currentMode;

    GridView gridView;
    private FloatingActionButton save_fab, close_fab;
    private SearchView search_bar;

    private InventoryDBHelper inventoryDBHelper;
    private UserDBHelper userDBHelper;
    private ArrayList<Integer> selectedPositions;

    /**
     * Create user's inventory list view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2,container,false);

        // Get user inventory list
        inventoryDBHelper = new InventoryDBHelper(getContext());
        allInventory = new ArrayList<>();
        allInventory = inventoryDBHelper.getAllData();

        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();
        ArrayList<String> userItems = c.getInventory();
        if(userItems != null){
            for(Inventory item : allInventory){
                boolean exist = false;
                for(String userItem: userItems){
                    if(userItem.equalsIgnoreCase(item.getItemName())){
                        exist = true;
                    }
                }
                if(exist){
                    userInventoryList.add(item);
                }
            }
        }


        // Initialize the custom adapter and connect listView with adapter
        userItemsAdapter = new InventoryAdapter(getContext(), userInventoryList);
        gridView = (GridView) view.findViewById(R.id.inventoryGridView);
        gridView.setAdapter(userItemsAdapter);

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        myModeListener = new MultiChoiceModeListener();
        gridView.setMultiChoiceModeListener(myModeListener);

        selectedItem = new ArrayList<>();
        selectedPositions = new ArrayList<>();

        save_fab = (FloatingActionButton) view.findViewById(R.id.remove_fab);
        close_fab = (FloatingActionButton) view.findViewById(R.id.close_fab);

        save_fab.setOnClickListener(new View.OnClickListener() {
            /**
             * When save button is clicked, remove selected items from user's inventory list
             * @param view
             */
            @Override
            public void onClick(View view) {
                StyleableToast.makeText(getContext(), "Remove from Inventory", Toast.LENGTH_SHORT).show();

                UserDBHelper userDBHelper = new UserDBHelper(getContext());
                User c = userDBHelper.getThisUser();

                for(Inventory item: selectedItem){
                    userInventoryList.remove(item);
                    userItemsAdapter.notifyDataSetChanged();

                    c.getInventory().remove(item.getItemName());
                }

                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());
                // Save items to firebase
                new FirebaseDatabaseHelper("user").updateUser(c.getKey(),  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataisLoaded(List<User> users, List<String> keys) {}

                    @Override
                    public void DataIsInserted(User user, String key) { }

                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {}
                });

                // Save items to local db
                userDBHelper.deleteAll();
                userDBHelper.insertData(c.getKey(), c.getName(), c.getEmail(), c.getInventoryStr(), c.getShoppingStr(), c.getShoppingcheckStr(), c.getCompletedStr(), c.getCompletedDateStr(), c.getFavoriteStr());

                for (Integer position : selectedPositions) {
                    inventoryDBHelper.removeFromUserInventory(position);
                }

                for (int i = 0; i < gridView.getChildCount(); i++) {
                    ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.grid_selector);
                    iv.setVisibility(View.INVISIBLE);
                }

                myModeListener.onDestroyActionMode(currentMode);
            }
        });

        close_fab.setOnClickListener(new View.OnClickListener() {
            /**
             * When close button is clicked, kill multiple choice mode listener
             * @param view
             */
            @Override
            public void onClick(View view) {
                myModeListener.onDestroyActionMode(currentMode);
            }
        });


        search_bar = (SearchView) view.findViewById(R.id.searchView);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            /**
             * When search bar text change, display filtered list
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextChange(String query) {
                ((InventoryAdapter) gridView.getAdapter()).getFilter().filter(query);
                userItemsAdapter.notifyDataSetChanged();
                return false;
            }
        });


        return view;
    }

    /**
     * MultiChoiceModeListener class for inventory gridview
     */
    private class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        /**
         * When item is long clicked, multiple choice mode is triggered
         * Update UI
         * @param mode
         * @param menu
         * @return
         */
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Remove Ingredients");
            mode.setSubtitle("1 item selected");

            save_fab.setVisibility(View.VISIBLE);
            close_fab.setVisibility(View.VISIBLE);

            currentMode = mode;
            return true;
        }

        /**
         * onPrepareActionMode
         * @param mode
         * @param menu
         * @return
         */
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            currentMode = mode;
            return true;
        }

        /**
         * onActionItemClicked
         * @param mode
         * @param item
         * @return
         */
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        /**
         * When action mode is destroyed, update UI and make selected list null
         * @param mode
         */
        public void onDestroyActionMode(ActionMode mode) {
            for (int i = 0; i < gridView.getChildCount(); i++) {
                ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.remove_selector);
                iv.setVisibility(View.INVISIBLE);
            }

            save_fab.setVisibility(View.INVISIBLE);
            close_fab.setVisibility(View.INVISIBLE);

            selectedPositions = new ArrayList<>();
            currentMode = mode;
            mode.finish();
        }

        /**
         * When item is selected, update UI
         * If item is already selected, remove the item from the list
         * If item is not selected, add item to the selected arrayList
         * @param mode
         * @param position
         * @param id
         * @param checked
         */
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
                selectedItem.add(userInventoryList.get(position));
                selectedPositions.add(userInventoryList.get(position).getId());
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.remove_selector);
                iv.setVisibility(View.VISIBLE);
            }else{
                selectedItem.remove(userInventoryList.get(position));
                for(int i=0;i<selectedPositions.size();i++){
                    if(selectedPositions.get(i) == position){
                        selectedPositions.remove(i);
                    }
                }

                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.remove_selector);
                iv.setVisibility(View.INVISIBLE);
            }
            currentMode = mode;
        }
    }
}
