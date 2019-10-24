package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseDatabaseHelper;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

/**
 * ShoppingListFragment_lv2 is loaded when Shopping List tab is selected
 */
public class ShoppingListFragment_lv2 extends Fragment {

    private ArrayList<ShoppinglistItem> shoppinglist = new ArrayList<>();
    private ShoppinglistAdapter shoppinglistAdapter;

    private ListView listView;
    private EditText editText;
    private ImageButton imageButton;
    private Button saveButton;

    private ArrayList<String> fbShoppingList;
    private ArrayList<Integer> fbShoppingCheck;

    private UserDBHelper userDBHelper;

    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;

    /**
     * Create view of shopping list
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shoppinglist_lv2, container, false);

        editText = (EditText) view.findViewById(R.id.addText);
        imageButton = view.findViewById(R.id.addButton);
        saveButton = view.findViewById(R.id.shopping_save);
        listView = (ListView) view.findViewById(R.id.shoppingListview);

        // Get user's shopping list
        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();
        fbShoppingList = c.getShoppinglist();
        fbShoppingCheck = c.getShoppinglistcheck();
        for(int i=1;i<fbShoppingList.size();i++){
            if(fbShoppingCheck.get(i) == 0){ // list false
                shoppinglist.add(new ShoppinglistItem(false, fbShoppingList.get(i)));
            } else{
                shoppinglist.add(new ShoppinglistItem(true, fbShoppingList.get(i)));
            }
        }
        shoppinglistAdapter = new ShoppinglistAdapter(getContext(),shoppinglist);
        listView.setAdapter(shoppinglistAdapter);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        imageButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When add button is clicked, add new shopping list to the list
             * Update UI
             * @param v
             */
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
            /**
             * When save button is clicked, display alert dialog to confirm if user wants to save shopping list to Firebase
             * @param view
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Save Shopping List")
                        .setMessage("Do you want to save your shopping list? Note that checked list will be removed once saved.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            /**
                             * If user click YES, add current list to local database and Firebase
                             * @param dialog
                             * @param which
                             */
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
                                        fbShoppingCheck.add(0);
                                        fbShoppingList.add(item.getName());
                                    }
                                }
                                shoppinglistAdapter.notifyDataSetChanged();

                                UserDBHelper userDBHelper = new UserDBHelper(getContext());
                                User c = userDBHelper.getThisUser();
                                User currentUser = new User(c.getName(), c.getEmail(), c.getInventory(), c.getShoppinglist(), c.getShoppinglistcheck(), c.getCompletedrecipe(), c.getCompletedDate(), c.getFavorites());
                                currentUser.setShoppinglist(fbShoppingList);
                                currentUser.setShoppinglistcheck(fbShoppingCheck);

                                // Update shopping list to firebase
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
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /**
                             * If user click NO, do nothing
                             * @param dialog
                             * @param which
                             */
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