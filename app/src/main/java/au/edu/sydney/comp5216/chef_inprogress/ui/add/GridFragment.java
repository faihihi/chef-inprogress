package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;

import com.google.android.gms.common.api.CommonStatusCodes;

import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.Inventory;
import au.edu.sydney.comp5216.chef_inprogress.InventoryAdapter;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.OcrCaptureActivity;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class GridFragment extends Fragment {
    CategoryPagerAdapter categoryPagerAdapter;
    private MultiChoiceModeListener myModeListener;
    private ActionMode currentMode;

    private ArrayList<Inventory> inventoryList, displayList, selectedItem;
    private InventoryAdapter itemsAdapter;
    private InventoryDBHelper inventoryDBHelper;
    private ArrayList<Integer> selectedPositions;

    private GridView gridView;
    private FloatingActionButton save_fab, close_fab;
    private SearchView search_bar;
    private ImageView scan_btn;

    private boolean firstOpen = false;
    private String category;
    private int currentCategory;
    private boolean allfragments;

    // Variables for OCR reader
    private TextView statusMessage;
    private TextView textValue;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "GridFragment";

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
        inventoryList = inventoryDBHelper.getItemsNotInUserInventory();

        displayList = new ArrayList<>();
        selectedItem = new ArrayList<>();
        selectedPositions = new ArrayList<>();

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

        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StyleableToast.makeText(getContext(), "Saved items to Inventory", Toast.LENGTH_SHORT).show();

                for(Inventory item: selectedItem){
                    displayList.remove(item);
                    itemsAdapter.notifyDataSetChanged();
                }
                for (Integer position : selectedPositions) {
                    inventoryDBHelper.saveToUserInventory(position);
                }

                for (int i = 0; i < gridView.getChildCount(); i++) {
                    ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.grid_selector);
                    iv.setVisibility(View.INVISIBLE);
                }

                setAllFragments();
                myModeListener.onDestroyActionMode(currentMode);
            }
        });

        close_fab.setOnClickListener(new View.OnClickListener() {
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

            @Override
            public boolean onQueryTextChange(String query) {
                ((InventoryAdapter) gridView.getAdapter()).getFilter().filter(query);
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        });

        scan_btn = (ImageView) view.findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
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

    //multi select mode codes
    private class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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

            mode.setTitle("Add Ingredients to Inventory");
            mode.setSubtitle("1 item selected");
            currentMode = mode;

            save_fab.setVisibility(View.VISIBLE);
            close_fab.setVisibility(View.VISIBLE);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            currentMode = mode;
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

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
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
//                    statusMessage.setText(R.string.ocr_success);
//                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
//                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.ocr_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
