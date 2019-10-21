package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.ui.add.GridFragment;

public class ShoppingListFragment_lv2 extends Fragment {

    private ArrayList<ShoppinglistItem> shoppinglist = new ArrayList<>();
    private ShoppinglistAdapter shoppinglistAdapter;

    ListView listView;
    EditText editText;
    ImageButton imageButton;
    Button saveButton;

    User currentUser;
    String userKey;
    ArrayList<String> fbShoppingList;
    ArrayList<Integer> fbShoppingCheck;
    boolean justUpdated;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shoppinglist_lv2, container, false);

        editText = (EditText) view.findViewById(R.id.addText);
        imageButton = view.findViewById(R.id.addButton);
        saveButton = view.findViewById(R.id.shopping_save);
        listView = (ListView) view.findViewById(R.id.shoppingListview);

        justUpdated = false;

        new FirebaseDatabaseHelper("user").getUserInfo(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataisLoaded(List<User> users, List<String> keys) {
                currentUser = users.get(0);
//                ((GlobalVariables) getActivity().getApplication()).setCurrentUser(currentUser);
//                ((GlobalVariables) getActivity().getApplication()).setCurrentUserKey("1");
                fbShoppingList = currentUser.getShoppinglist();
                fbShoppingCheck = currentUser.getShoppinglistcheck();

                if(!justUpdated){
                    for(int i=1;i<fbShoppingList.size();i++){
                        if(fbShoppingCheck.get(i) == 0){ // list false
                            shoppinglist.add(new ShoppinglistItem(false, fbShoppingList.get(i)));
                        } else{
                            shoppinglist.add(new ShoppinglistItem(true, fbShoppingList.get(i)));
                        }
                    }

                    shoppinglistAdapter = new ShoppinglistAdapter(getContext(),shoppinglist);
//                listView.setAdapter(null);
                    listView.setAdapter(shoppinglistAdapter);

                }


            }

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() { }

            @Override
            public void DataIsDeleted() {}
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toAddString = editText.getText().toString();
                if(toAddString != null && toAddString.length()>0){
                    shoppinglist.add(new ShoppinglistItem(false,toAddString));
                    editText.setText("");
                    shoppinglistAdapter.notifyDataSetChanged();


                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Save Shopping List")
                        .setMessage("Do you want to save your shopping list? Note that checked list will be removed once saved.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //remove item from shopping list
                                fbShoppingList = new ArrayList<>();
                                fbShoppingCheck = new ArrayList<>();

                                fbShoppingList.add("dummy");
                                fbShoppingCheck.add(0);

                                ArrayList<ShoppinglistItem> temp = new ArrayList<ShoppinglistItem>(shoppinglist);
                                for(ShoppinglistItem item : temp){
                                    if(!item.isChecked()){
//                                        shoppinglist.remove(item);
//                                        shoppinglistAdapter.notifyDataSetChanged();

                                        fbShoppingCheck.add(0);
                                        fbShoppingList.add(item.getName());
                                    }

                                }
                                shoppinglistAdapter.notifyDataSetChanged();
                                currentUser.setShoppinglist(fbShoppingList);
                                currentUser.setShoppinglistcheck(fbShoppingCheck);
                                justUpdated = true;

                                Gson gson = new Gson();
                                String str = gson.toJson(currentUser);

                                gson = new GsonBuilder().setPrettyPrinting().create();
                                JsonParser jp = new JsonParser();
                                JsonElement je = jp.parse(str);
                                String prettyJsonString = gson.toJson(je);
                                Log.d("CORRECT FORMAT", prettyJsonString);

                                new FirebaseDatabaseHelper("user").updateUser("1",  currentUser, new FirebaseDatabaseHelper.DataStatus() {
                                    @Override
                                    public void DataisLoaded(List<User> users, List<String> keys) {}

                                    @Override
                                    public void DataIsInserted() {}

                                    @Override
                                    public void DataIsUpdated() {}

                                    @Override
                                    public void DataIsDeleted() {}
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create().show();
            }
        });


        return view;
    }
}