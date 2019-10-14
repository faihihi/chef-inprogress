package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.InventoryDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.InventoryItemAdapter;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.ui.inventory.ViewPagerAdapter;

public class GridFragment extends Fragment {
    private ArrayList<InventoryItem> inventoryList, displayList;
    private ArrayAdapter<InventoryItem> itemsAdapter;
    private InventoryDBHelper inventoryDBHelper;

    private GridView gridView;
    private ViewPagerAdapter viewPagerAdapter;

    private boolean firstOpen = false;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_lv2,container,false);

        Log.d("onCreateView", "GridFragment is started");
        if(!firstOpen){
            Bundle args = getArguments();
            category = args.getString("category", "");
            Log.d("CHECK category", category);
        }

        inventoryDBHelper = new InventoryDBHelper(getContext());

        inventoryList = new ArrayList<>();
        inventoryList = inventoryDBHelper.getAllData();

        displayList = new ArrayList<>();

        switch(category){
            case "meat":
                for(InventoryItem i : inventoryList){
                    Log.d("Check list", i.getCategory());
                    if(i.getCategory().equals("meat")){
                        displayList.add(i);
                    }
                }
//                inventoryList.add(new InventoryItem("Fish","Meat",R.drawable.fish));
                break;
            case "fruitveg":
                for(InventoryItem i : inventoryList){
                    if(i.getCategory().equals("fruitveg")){
                        displayList.add(i);
                    }
                }
//                inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
//                inventoryList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
                break;
            case "grocery":
                for(InventoryItem i : inventoryList){
                    if(i.getCategory().equals("grocery")){
                        displayList.add(i);
                    }
                }
                break;
            default:
                break;
        }


        // Initialize the custom adapter and connect listView with adapter
        itemsAdapter = new InventoryItemAdapter(getContext(), displayList);
        gridView = (GridView) view.findViewById(R.id.inventoryGridView);


        gridView.setAdapter(itemsAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new MultiChoiceModeListener());
//        gridView.setDrawSelectorOnTop(true);
//        gridView.setSelector(getResources().getDrawable(R.drawable.success));

//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                Log.d("ITEM LONG", "LONG CLICKED!");
////                Toast.makeText(getActivity(), String.valueOf(imageAdapter.mThumbIds[position]), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
//                Log.d("Item", "IS CLICKED!");
////                ImageView iv = (ImageView) getActivity().findViewById(R.id.grid_selector);
////                iv.setVisibility(View.VISIBLE);
////                if(BaseActivity.isinint) { // check if any app cares for the result
////                    int ImageResourse=imageAdapter.mThumbIds[position];
////                    Uri path = Uri.parse("android.resource://dragonflymobile.stickers.lifestickers/" + ImageResourse);
////
////                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND, path); //Create a new intent. First parameter means that you want to send the file. The second parameter is the URI pointing to a file on the sd card. (openprev has the datatype File)
////
////                    (getActivity()).setResult(getActivity().RESULT_OK, shareIntent); //set the file/intent as result
////                    (getActivity()).finish(); //close your application and get back to the requesting application like GMail and WhatsApp
////                    return; //do not execute code below, not important
////                } else {
//////                    Intent intent = new Intent(getActivity(), PreviewActivity.class);
//////                    intent.putExtra("Image Int", imageAdapter.mThumbIds[position]);
//////                    startActivity(intent);
////                }
//            }
//        });



        return view;
    }

    //multi select mode codes
    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            int selectCount = gridView.getCheckedItemCount();
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }

            if(checked){
                View tv = (View) gridView.getChildAt(position);
//                tv.setBackgroundColor(Color.RED);

                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.VISIBLE);
            }else{
                View tv = (View) gridView.getChildAt(position);
//                tv.setBackgroundColor(Color.TRANSPARENT);

                ImageView iv = (ImageView) gridView.getChildAt(position).findViewById(R.id.grid_selector);
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }



}
