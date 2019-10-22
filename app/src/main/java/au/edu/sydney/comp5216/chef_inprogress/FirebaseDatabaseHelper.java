package au.edu.sydney.comp5216.chef_inprogress;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class FirebaseDatabaseHelper implements Continuation<Void, Task<User>> {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUser;

    private List<User> users = new ArrayList<>();

    Semaphore semaphore;

    @Override
    public Task<User> then(@NonNull Task<Void> task) throws Exception {
        final TaskCompletionSource<User> tcs = new TaskCompletionSource();
        mReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onCancelled(DatabaseError error) {
                tcs.setException(error.toException());
            }

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tcs.setResult(snapshot.getValue(User.class));
            }

        });

        return tcs.getTask();
    }

    public interface DataStatus{
        void DataisLoaded(List<User> users, List<String> keys);
        void DataIsInserted(User user, String key);
        void DataIsUpdated();
        void DataIsDeleted();
    }
    public FirebaseDatabaseHelper(String table){
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceUser = mDatabase.getReference(table);
        semaphore = new Semaphore(0);
    }

    public void getUserInfo(final DataStatus dataStatus){
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataisLoaded(users, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNewUser(final User user, final DataStatus dataStatus){
        final String key = mReferenceUser.push().getKey();
        mReferenceUser.child(key).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted(user, key);
                    }
                });
    }

    public void updateUser(String key, User user, final DataStatus dataStatus){
        mReferenceUser.child(key).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteUser(String key, final DataStatus dataStatus){
        mReferenceUser.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}
