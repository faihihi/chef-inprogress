package au.edu.sydney.comp5216.chef_inprogress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {
    /**
     * initialize the databasehelper
     * @param context
     */
    public UserDBHelper (Context context) {
        super(context,"chefinprogress_user.db",null,1);
    }

    /**
     * create the database and state all the columns
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (ID INTEGER PRIMARY KEY AUTOINCREMENT, FBKEY STRING, EMAIL TEXT, INVENTORY TEXT, SHOPPINGLIST TEXT, SHOPPINGCHECK TEXT, COMPLETED TEXT, FAVORITES TEXT)");

    }

    /**
     * update the database if exists
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * insert new rows when new data generated
     * @param fbkey
     * @param email
     * @param inventory
     * @param shopping
     * @param completed
     * @param favorites
     * @return
     */
    public boolean insertData(String fbkey, String email, String inventory, String shopping, String shoppingcheck, String completed, String favorites){
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS user");
//        onCreate(db);

        ContentValues contentValues = new ContentValues();
        contentValues.put("FBKEY",fbkey);
        contentValues.put("EMAIL",email);
        contentValues.put("INVENTORY",inventory);
        contentValues.put("SHOPPINGLIST", shopping);
        contentValues.put("SHOPPINGCHECK", shoppingcheck);
        contentValues.put("COMPLETED", completed);
        contentValues.put("FAVORITES", favorites);

        long result = db.insert("user",null,contentValues);
        db.close();

        if(result == -1) return false;
        else return true;
    }

    /**
     * to retrieve all the data
     * @return
     */
    public User getThisUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user",null);

        User user = new User();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String key = cursor.getString(1);
            String email = cursor.getString(2);
            String inventory = cursor.getString(3);
            String shopping = cursor.getString(4);
            String shoppingcheck = cursor.getString(5);
            String completed = cursor.getString(6);
            String favorites = cursor.getString(7);

            user = new User(key, email, inventory, shopping, shoppingcheck, completed, favorites);

        }

        db.close();
        return user;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ "user");
    }
}
