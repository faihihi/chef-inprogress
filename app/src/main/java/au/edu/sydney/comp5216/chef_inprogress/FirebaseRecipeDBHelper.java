package au.edu.sydney.comp5216.chef_inprogress;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Firebase Recipe Database Helper for getting Recipe data from Firebase
 */
public class FirebaseRecipeDBHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUser;

    private List<Recipe> recipes = new ArrayList<>();

    /**
     * DataStatus interface for setting callback functions
     */
    public interface DataStatus{
        void RecipeisLoaded(List<Recipe> recipes, List<String> keys);
        void DataIsUpdated();
    }

    /**
     * Constructor
     */
    public FirebaseRecipeDBHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUser = mDatabase.getReference("recipe");
    }


    /**
     * Get all recipes from firebase
     * @param dataStatus
     */
    public void getAllRecipe(final DataStatus dataStatus){
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Recipe recipe = keyNode.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                dataStatus.RecipeisLoaded(recipes, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Get recipe by title from firebase
     * @param title
     * @param dataStatus
     */
    public void getRecipeByTitle(String title, final DataStatus dataStatus) {
        Query query = mReferenceUser.orderByChild("title").equalTo(title);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Recipe recipe = keyNode.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                // create new call back function and pass sum instead
                dataStatus.RecipeisLoaded(recipes, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Update recipe on firebase
     * @param key
     * @param user
     * @param dataStatus
     */
    public void updateRecipe(String key, User user, final DataStatus dataStatus){
        mReferenceUser.child(key).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

}
