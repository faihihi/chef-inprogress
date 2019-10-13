package au.edu.sydney.comp5216.chef_inprogress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecipeDBHelper extends SQLiteOpenHelper {
    /**
     * initialize the databasehelper
     * @param context
     */
    public RecipeDBHelper (Context context) {
        super(context,"chefrecipe.db",null,1);
    }

    /**
     * create the database and state all the columns
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE recipe (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, IMGPATH TEXT, TAGS TEXT, PROTEIN INTEGER, " +
                "FAT INTEGER, CARB INTEGER, TIME TEXT, SERVES INTEGER, INGREDIENTS TEXT, INSTRUCTIONS TEXT)");

    }

    /**
     * update the database if exists
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe");
        onCreate(db);
    }

    /**
     * insert new rows when new data generated
     * @param title
     * @param imgpath
     * @param tags
     * @param protein
     * @param fat
     * @param carb
     * @param time
     * @param serves
     * @param ingredients
     * @param instructions
     * @return
     */
    public boolean insertData(String title, String imgpath, String tags, int protein, int fat, int carb, String time, int serves, String ingredients, String instructions){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("TITLE", title);
        contentValues.put("IMGPATH",imgpath);
        contentValues.put("TAGS",tags);
        contentValues.put("PROTEIN",protein);
        contentValues.put("FAT",fat);
        contentValues.put("CARB",carb);
        contentValues.put("TIME",time);
        contentValues.put("SERVES",serves);
        contentValues.put("INGREDIENTS",ingredients);
        contentValues.put("INSTRUCTIONS",instructions);

        Log.d("INSERTTT", tags);

        long result = db.insert("recipe",null,contentValues);

        if(result == -1) return false;
        else return true;
    }

    /**
     * to retrieve all the data
     * @return
     */
    public ArrayList<Recipe> getAllData(){
        ArrayList<Recipe> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipe",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String imgpath = cursor.getString(2);
            String tags = cursor.getString(3);
            int protein = cursor.getInt(4);
            int fat = cursor.getInt(5);
            int carb = cursor.getInt(6);
            String time = cursor.getString(7);
            int serves = cursor.getInt(8);
            String ingredients = cursor.getString(9);
            String instructions= cursor.getString(10);

            Recipe item = new Recipe(title, imgpath, tags, protein, fat, carb, time, serves, ingredients, instructions);

//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<String>>() {}.getType();
//            ArrayList<String> tagsList = gson.fromJson(tags, type);
//            ArrayList<String> instructionsList = gson.fromJson(instructions, type);
//
//            type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
//            ArrayList<Ingredients> ingredientsList = gson.fromJson(ingredients, type);
//
//            Recipe item = new Recipe(title, imgpath, tagsList, protein, fat, carb, time, serves, ingredientsList, instructionsList);
//            Recipe item = new Recipe(title, imgpath, tagsList, protein, fat, carb, time, serves, ingredientsList, instructionsList);


            arrayList.add(item);
        }

        return arrayList;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ "recipe");
    }
}
