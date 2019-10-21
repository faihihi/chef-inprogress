package au.edu.sydney.comp5216.chef_inprogress;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRecipeDBHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUser;

    private List<Recipe> recipes = new ArrayList<>();

    public interface DataStatus{
        void RecipeisLoaded(List<Recipe> recipes, List<String> keys);
        void DataIsUpdated();
    }

    public FirebaseRecipeDBHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUser = mDatabase.getReference("recipe");
    }


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
