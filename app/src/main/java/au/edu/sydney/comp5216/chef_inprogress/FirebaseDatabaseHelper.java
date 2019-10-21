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

//    public Task<User> execute() {
//        return Task.<Void>forResult(null)
//                .then(new GetUser())
//                .then(new GetCourse());
//    }
//
//    public static Task<TResult> forResult (TResult result){
//
//    }

    public interface DataStatus{
        void DataisLoaded(List<User> users, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public interface OnGetDataListener {
        public void onStart();
        public void onSuccess(List<User> users, List<String> keys);
        public void onFailed(DatabaseError databaseError);
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

    public void getUserInfoTest(final DataStatus dataStatus) throws InterruptedException{
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
                semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        semaphore.acquire();
    }

    public void getUserInfoSync(String child, final OnGetDataListener listener){
        listener.onStart();
        FirebaseDatabase.getInstance().getReference().child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
//                dataStatus.DataisLoaded(users, keys);
                listener.onSuccess(users, keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    private void mCheckInforInServer(String child) {
        new FirebaseDatabaseHelper("user").getUserInfoSync(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess(List<User> users, List<String> keys) {

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });

    }

    public void addNewUser(User user, final DataStatus dataStatus){
        String key = mReferenceUser.push().getKey();
        mReferenceUser.child(key).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
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

    public void deleteShoppingList(String key, final DataStatus dataStatus){
        mReferenceUser.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}
