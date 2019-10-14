package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.ViewPagerAdapter;

public class GridFragment extends Fragment {
    MultiChoiceModeListener modeListenerGridFragment;
    CategoryPagerAdapter categoryPagerAdapter;
    int currentCategory;

    private ArrayList<InventoryItem> inventoryList, displayList;
    private ArrayAdapter<InventoryItem> itemsAdapter;
    private InventoryDBHelper inventoryDBHelper;

    private GridView gridView;
    private ArrayList<Integer> selectedPositions;
    private ViewPagerAdapter viewPagerAdapter;

    private boolean firstOpen = false;
    private String category;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2, container, false);

        categoryPagerAdapter = ((GlobalVariables) getActivity().getApplication()).getViewPagerAdapter();
        currentCategory = ((GlobalVariables) getActivity().getApplication()).getCurrentCategory();

        if (!firstOpen) {
            Bundle args = getArguments();
            category = args.getString("category", "");
        }

        inventoryDBHelper = new InventoryDBHelper(getContext());

        inventoryList = new ArrayList<>();
        inventoryList = inventoryDBHelper.getAllData();

        displayList = new ArrayList<>();

        selectedPositions = new ArrayList<>();

        setDisplayList(category);


        // Initialize the custom adapter and connect listView with adapter
        itemsAdapter = new InventoryItemAdapter(getContext(), displayList);
        gridView = (GridView) view.findViewById(R.id.inventoryGridView);


        gridView.setAdapter(itemsAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        modeListenerGridFragment = new MultiChoiceModeListener();
        gridView.setMultiChoiceModeListener(modeListenerGridFragment);


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


            mode.setTitle("Select Items");
            mode.setSubtitle("1 item selected");
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;

        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_save:
                    Toast.makeText(getContext(), "Saved items to Inventory", Toast.LENGTH_SHORT).show();


                    for (Integer position : selectedPositions) {
                        inventoryDBHelper.saveToUserInventory(position);
                    }

                    for (int i = 0; i < gridView.getChildCount(); i++) {
                        ImageView iv = (ImageView) gridView.getChildAt(i).findViewById(R.id.grid_selector);
                        iv.setVisibility(View.INVISIBLE);
                    }
                    mode.finish();

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
                    ((GlobalVariables) getActivity().getApplication()).setCurrentCategory(-1);
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

            if (checked) {
                selectedPositions.add(displayList.get(position).getId());
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.VISIBLE);
            } else {
                for (int i = 0; i < selectedPositions.size(); i++) {
                    if (selectedPositions.get(i) == position) {
                        selectedPositions.remove(i);
                    }
                }
                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setDisplayList(String category) {
        Log.d("Set Display List", category);
        switch (category) {
            case "meat":
                currentCategory = 0;
                for (InventoryItem i : inventoryList) {
                    if (i.getCategory().equals("meat")) {
                        displayList.add(i);
                    }
                }
                break;
            case "fruitveg":
                currentCategory = 1;
                for (InventoryItem i : inventoryList) {
                    if (i.getCategory().equals("fruitveg")) {
                        displayList.add(i);
                    }
                }
                break;
            case "grocery":
                currentCategory = 2;
                for (InventoryItem i : inventoryList) {
                    if (i.getCategory().equals("grocery")) {
                        displayList.add(i);
                    }
                }
                break;
            default:
                break;
        }
    }


}
