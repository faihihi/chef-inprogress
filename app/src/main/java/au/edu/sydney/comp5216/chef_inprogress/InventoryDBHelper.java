package au.edu.sydney.comp5216.chef_inprogress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDBHelper extends SQLiteOpenHelper {

    /**
     * initialize the databasehelper
     * @param context
     */
    public InventoryDBHelper (Context context) {
        super(context,"chefinprogress.db",null,1);
    }

    /**
     * create the database and state all the columns
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE inventoryItem (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEMNAME TEXT, CATEGORY TEXT, ICON INTEGER, USERINVENTORY INTEGER)");

    }

    /**
     * update the database if exists
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS inventoryItem");
        onCreate(db);
    }

    /**
     * insert new rows when new data generated
     * @param itemname
     * @param category
     * @param icon
     * @return
     */
    public boolean insertData(String itemname, String category, int icon, int userInventory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEMNAME",itemname);
        contentValues.put("CATEGORY",category);
        contentValues.put("ICON",icon);
        contentValues.put("USERINVENTORY", userInventory);

        long result = db.insert("inventoryItem",null,contentValues);

        if(result == -1) return false;
        else return true;
    }

    /**
     * to retrieve all the data
     * @return
     */
    public ArrayList<Inventory> getAllData(){
        ArrayList<Inventory> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM inventoryItem",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String itemName = cursor.getString(1);
            String category = cursor.getString(2);
            int icon = cursor.getInt(3);
            int userInventory = cursor.getInt(4);

            Inventory item = new Inventory(id, itemName, category, icon, userInventory);

            arrayList.add(item);
        }

        return arrayList;
    }

    public void saveToUserInventory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERINVENTORY", 1);

        db.update("inventoryItem", contentValues, "ID="+id, null);
    }

    public void saveToUserInventoryWithTitle(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERINVENTORY", 1);

        db.update("inventoryItem", contentValues, "ITEMNAME='"+title+"'", null);
    }


    public void removeFromUserInventory(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERINVENTORY", 0);

        db.update("inventoryItem", contentValues, "ID="+id, null);
    }

    public ArrayList<Inventory> getItemsNotInUserInventory(){
        ArrayList<Inventory> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM inventoryItem",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String itemName = cursor.getString(1);
            String category = cursor.getString(2);
            int icon = cursor.getInt(3);
            int userInventory = cursor.getInt(4);

            if(userInventory == 0){
                Inventory item = new Inventory(id, itemName, category, icon, userInventory);
                arrayList.add(item);
            }
        }

        return arrayList;

    }

    public ArrayList<Inventory> getItemsInUserInventory(){
        ArrayList<Inventory> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM inventoryItem",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String itemName = cursor.getString(1);
            String category = cursor.getString(2);
            int icon = cursor.getInt(3);
            int userInventory = cursor.getInt(4);

            if(userInventory == 1){
                Inventory item = new Inventory(id, itemName, category, icon, userInventory);
                arrayList.add(item);
            }
        }

        return arrayList;

    }
}
