package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.api.CommonStatusCodes;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryAdapter;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.OcrCaptureActivity;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * GridFragment is started when the AddFragment is loaded
 * This fragment contains all functions related to Gridview in the Adding menu
 */
public class GridFragment extends Fragment {
    CategoryPagerAdapter categoryPagerAdapter;
    private MultiChoiceModeListener myModeListener;
    private ActionMode currentMode;

    private ArrayList<Inventory> inventoryList, displayList, selectedItem, scannedListResult, allInventory;
    private InventoryAdapter itemsAdapter;
    private InventoryDBHelper inventoryDBHelper;
    private UserDBHelper userDBHelper;
    private ArrayList<Integer> selectedPositions;

    private GridView gridView;
    private FloatingActionButton save_fab, close_fab;
    private SearchView search_bar;
    private ImageView scan_btn;

    private boolean firstOpen = false;
    private String category;
    private int currentCategory;
    private boolean allfragments;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "GridFragment";

    /**
     * Create view when fragment is started
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2, container, false);

        categoryPagerAdapter = ((GlobalVariables) getActivity().getApplication()).getViewPagerAdapter();

        if (!firstOpen) {
            Bundle args = getArguments();
            category = args.getString("category", "");
        }

        inventoryDBHelper = new InventoryDBHelper(getContext());
        inventoryList = new ArrayList<>();

        allInventory = new ArrayList<>();
        allInventory = inventoryDBHelper.getAllData();

        // Get all ingredients items that is not in user's inventory
        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();
        ArrayList<String> userItems = c.getInventory();
        for(Inventory item : allInventory){
            boolean exist = false;
            for(String userItem: userItems){
                if(userItem.equalsIgnoreCase(item.getItemName())){
                    exist = true;
                }
            }
            if(!exist){
                inventoryList.add(item);
            }
        }

        displayList = new ArrayList<>();
        selectedItem = new ArrayList<>();
        selectedPositions = new ArrayList<>();
        scannedListResult = new ArrayList<>();

        setDisplayList(category);

        // Initialize the custom adapter and connect listView with adapter
        itemsAdapter = new InventoryAdapter(getContext(), displayList);
        gridView = (GridView) view.findViewById(R.id.inventoryGridView);

        gridView.setAdapter(itemsAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        myModeListener = new MultiChoiceModeListener();
        gridView.setMultiChoiceModeListener(myModeListener);

        save_fab = (FloatingActionButton) view.findViewById(R.id.save_fab);
        close_fab = (FloatingActionButton) view.findViewById(R.id.close_fab);

        /**
         * Set on click listener for save floating button
         */
        save_fab.setOnClickListener(new View.OnClickListener() {
            /**
             * When save floating button is clicked, save items to user's inventory
             * @param view
             */
            @Override
            public void onClick(View view) {

                StyleableToast.makeText(getContext(), "Saved items to Inventory", Toast.LENGTH_SHORT).show();

                UserDBHelper userDBHelper = new UserDBHelper(getContext());
                User c = userDBHelper.getThisUser();

                // Update display of items
                Log.d("SELECTEDItem", selectedItem.get(0).getItemName());
                for(Inventory item: selectedItem){
                    displayList.remove(item);
                    itemsAdapter.notifyDataSetChanged();

                    c.getInventory().add(item.getItemName());
                }

                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());
                // Save items to firebase
                new FirebaseDatabaseHelper("user").updateUser(c.getKey(),  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataisLoaded(List<User> users, List<String> keys) {}

                    @Override
                    public void DataIsInserted(User user, String key) {}


                    @Override
                    public void DataIsUpdated() {}

                    @Override
                    public void DataIsDeleted() {}
                });

                // Save items to local db
                userDBHelper.deleteAll();
                userDBHelper.insertData(c.getKey(), c.getName(), c.getEmail(), c.getInventoryStr(), c.getShoppingStr(), c.getShoppingcheckStr(), c.getCompletedStr(), c.getCompletedDateStr(), c.getFavoriteStr());


                for (Integer position : selectedPositions) {
                    inventoryDBHelper.saveToUserInventory(position);
                }

                // Update display
                for (int i = 0; i < gridView.getChildCount(); i++) {
                    ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.grid_selector);
                    iv.setVisibility(View.INVISIBLE);
                }

                setAllFragments();
                myModeListener.onDestroyActionMode(currentMode);
            }
        });

        /**
         * Set on click listener for closing button
         */
        close_fab.setOnClickListener(new View.OnClickListener() {
            /**
             * When clicked, destroy multiple selection action mode
             * @param view
             */
            @Override
            public void onClick(View view) {
                myModeListener.onDestroyActionMode(currentMode);
            }
        });


        search_bar = (SearchView) view.findViewById(R.id.searchView);
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * When query is submitted
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            /**
             * When query text on search bar is changed
             * Get filtered list and update display
             * @param query
             * @return
             */
            @Override
            public boolean onQueryTextChange(String query) {
                ((InventoryAdapter) gridView.getAdapter()).getFilter().filter(query);
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        });

        scan_btn = (ImageView) view.findViewById(R.id.scan_btn);
        scan_btn.setVisibility(View.VISIBLE);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            /**
             * When camera button is clicked, start OCR Activity (Google text recognition API)
             * @param view
             */
            @Override
            public void onClick(View view) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        return view;
    }

    /**
     * Multiple selection mode listener class for Gridview
     */
    private class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        /**
         * When ingredients items are long clicked, the listener activate and this function is ran
         * @param mode
         * @param menu
         * @return
         */
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Update display to restrict user from switching to another category tab
            switch (currentCategory) {
                case 0:
                    categoryPagerAdapter.removeFragment(new GridFragment(), 2);
                    categoryPagerAdapter.removeFragment(new GridFragment(), 1);
                    break;
                case 1:
                    categoryPagerAdapter.removeFragment(new GridFragment(), 0);
                    categoryPagerAdapter.removeFragment(new GridFragment(), 1);
                    break;
                case 2:
                    categoryPagerAdapter.removeFragment(new GridFragment(), 0);
                    categoryPagerAdapter.removeFragment(new GridFragment(), 0);
                    break;
                default:
                    break;
            }
            categoryPagerAdapter.notifyDataSetChanged();
            allfragments = false;

            // Display mode on action bar
            mode.setTitle("Add Ingredients to Inventory");
            mode.setSubtitle("1 item selected");
            currentMode = mode;

            save_fab.setVisibility(View.VISIBLE);
            close_fab.setVisibility(View.VISIBLE);
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
         * When item is clicked
         * @param mode
         * @param item
         * @return
         */
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        /**
         * When action mode is destroyed, update UI and kill mode
         * @param mode
         */
        public void onDestroyActionMode(ActionMode mode) {
            for (int i = 0; i < gridView.getChildCount(); i++) {
                ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.grid_selector);
                iv.setVisibility(View.INVISIBLE);
            }

            save_fab.setVisibility(View.INVISIBLE);
            close_fab.setVisibility(View.INVISIBLE);
            setAllFragments();

            selectedPositions = new ArrayList<>();
            currentMode = mode;
            mode.finish();
        }

        /**
         * When item is clicked, if the item is not selected, save selected items to list
         * If item is already selected, remove items from the list
         * And update UI
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

            if (checked) {
                selectedItem.add(displayList.get(position));
                selectedPositions.add(displayList.get(position).getId());
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.VISIBLE);
            } else {
                selectedItem.remove(displayList.get(position));

                for (int i = 0; i < selectedPositions.size(); i++) {
                    if (selectedPositions.get(i) == position) {
                        selectedPositions.remove(i);
                    }
                }
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.INVISIBLE);
            }

            currentMode = mode;
        }
    }

    /**
     * Function for setting displaying list of each category tab
     * @param category
     */
    private void setDisplayList(String category) {
        switch (category) {
            case "meat":
                currentCategory = 0;
                for (Inventory i : inventoryList) {
                    if (i.getCategory().equals("meat")) {
                        displayList.add(i);
                    }
                }
                break;
            case "fruitveg":
                currentCategory = 1;
                for (Inventory i : inventoryList) {
                    if (i.getCategory().equals("fruitveg")) {
                        displayList.add(i);
                    }
                }
                break;
            case "grocery":
                currentCategory = 2;
                for (Inventory i : inventoryList) {
                    if (i.getCategory().equals("grocery")) {
                        displayList.add(i);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Function for setting pager adapter for each category tab
     */
    private void setAllFragments(){
        if(!allfragments){
            categoryPagerAdapter.removeFragment(new GridFragment(), 0);

            Bundle args = new Bundle();
            args.putString("category", "meat");
            GridFragment f = new GridFragment();
            f.setArguments(args);
            categoryPagerAdapter.addFragment(f, "MEAT", 0);


            args = new Bundle();
            args.putString("category", "fruitveg");
            f = new GridFragment();
            f.setArguments(args);
            categoryPagerAdapter.addFragment(f, "FRUITS & VEG", 1);

            args = new Bundle();
            args.putString("category", "grocery");
            f = new GridFragment();
            f.setArguments(args);
            categoryPagerAdapter.addFragment(f, "GROCERY", 2);

            categoryPagerAdapter.notifyDataSetChanged();

            allfragments = true;
        }
    }

    /**
     * After the scanning recipes with camera activity is finished, this onActivityResult is called
     *
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    // Get scanned strings
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    scannedListResult = checkIfItemIsAvailable(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }

            // Find scanned strings that match with ingredients in database
            if(scannedListResult.size() > 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ArrayList<String> matchedList = new ArrayList<>();
                for(Inventory item : scannedListResult){
                    matchedList.add(item.getItemName());
                }

                String[] matchedListArr = new String[matchedList.size()];
                for(int i=0;i<matchedListArr.length;i++){
                    matchedListArr[i] = matchedList.get(i);
                    Log.d("Added MATCHED", matchedListArr[i]);
                }

                // Create dialog box for confirming if user wants to add all these ingredients
                builder.setTitle("Would you like to add these ingredients to your inventory?")
                        .setItems(matchedListArr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){

                                UserDBHelper userDBHelper = new UserDBHelper(getContext());
                                User c = userDBHelper.getThisUser();
                                for(Inventory item: scannedListResult){
                                    inventoryDBHelper.saveToUserInventory(item.getId());

                                    displayList.remove(item);
                                    itemsAdapter.notifyDataSetChanged();

                                    c.getInventory().add(item.getItemName());
                                }

                                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());
                                // Save items to firebase
                                new FirebaseDatabaseHelper("user").updateUser(c.getKey(),  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                                    @Override
                                    public void DataisLoaded(List<User> users, List<String> keys) {}

                                    @Override
                                    public void DataIsInserted(User user, String key) { }

                                    @Override
                                    public void DataIsUpdated() {
                                        StyleableToast.makeText(getContext(), "Saved items to Inventory", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void DataIsDeleted() {}
                                });

                                // Save items to local db
                                userDBHelper.deleteAll();
                                userDBHelper.insertData(c.getKey(), c.getName(), c.getEmail(), c.getInventoryStr(), c.getShoppingStr(), c.getShoppingcheckStr(), c.getCompletedStr(), c.getCompletedDateStr(), c.getFavoriteStr());


                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        });
                builder.create().show();

            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Ingredients Not Found")
                        .setMessage("Sorry, we do not have these ingredients in our Database :(")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        });
                builder.create().show();

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Function for matching strings of scanned list and ingredients in the database
     * @param text
     * @return
     */
    private ArrayList<Inventory> checkIfItemIsAvailable(String text){
        ArrayList<Inventory> scannedList = new ArrayList<>();

        for(String word: text.split("\\W+")){
            for(Inventory item: inventoryList){
                if((word.equalsIgnoreCase(item.getItemName())) ||
                        (word.equalsIgnoreCase(item.getItemNameWithS())) ||
                        (word.equalsIgnoreCase(item.getItemNameWithES()))){
                    scannedList.add(item);
                }
            }
        }
        return scannedList;
    }
}
