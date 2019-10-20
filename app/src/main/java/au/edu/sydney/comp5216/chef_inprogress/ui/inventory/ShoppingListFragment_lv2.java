package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

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

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.GlobalVariables;
import au.edu.sydney.comp5216.chef_inprogress.R;
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
    List<String> fbShoppingList;
    List<Integer> fbShoppingCheck;
    boolean justUpdated;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shoppinglist_lv2, container, false);

        editText = (EditText) view.findViewById(R.id.addText);
        imageButton = view.findViewById(R.id.addButton);
        saveButton = view.findViewById(R.id.shopping_save);
        listView = (ListView) view.findViewById(R.id.shoppingListview);

        justUpdated = false;

        new FirebaseDatabaseHelper().getUserInfo(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataisLoaded(List<User> users, List<String> keys) {
                currentUser = users.get(0);
//                ((GlobalVariables) getActivity().getApplication()).setCurrentUser(currentUser);
//                ((GlobalVariables) getActivity().getApplication()).setCurrentUserKey("1");
                fbShoppingList = currentUser.getShoppingList();
                fbShoppingCheck = currentUser.getShoppinglistcheck();

                if(!justUpdated){
                    for(int i=0;i<fbShoppingList.size();i++){
                        if(fbShoppingCheck.get(i) == 0){ // list false
                            shoppinglist.add(new ShoppinglistItem(false, fbShoppingList.get(i)));
                        } else{
                            shoppinglist.add(new ShoppinglistItem(true, fbShoppingList.get(i)));
                        }
                    }
                    shoppinglistAdapter = new ShoppinglistAdapter(getContext(),shoppinglist);
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
                fbShoppingList = new ArrayList<>();
                fbShoppingCheck = new ArrayList<>();
                for(ShoppinglistItem item : shoppinglist){
                    if(item.isChecked()){
                        fbShoppingCheck.add(1);
                    } else {
                        fbShoppingCheck.add(0);
                    }

                    fbShoppingList.add(item.getName());
                }
                currentUser.setShoppingList(fbShoppingList);
                currentUser.setShoppinglistcheck(fbShoppingCheck);
                justUpdated = true;

                new FirebaseDatabaseHelper().updateUser("1",  currentUser, new FirebaseDatabaseHelper.DataStatus() {
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
        });

//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        return view;
    }
}