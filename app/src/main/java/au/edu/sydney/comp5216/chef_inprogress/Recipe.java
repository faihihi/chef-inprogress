package au.edu.sydney.comp5216.chef_inprogress;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;

public class Recipe implements Parcelable {
    private int id;
    private String title;
    private String imgpath;

    // Tags ex. vegetarian, gluten-free
    private String[] tags;
    private String tagsString;

    // Nutrition intake in gram
    private int protein;
    private int fat;
    private int carbs;

    // Details
    private String timeTaken;
    private int servings;

    // Ingredients
    private ArrayList<Ingredients> ingredientsList;
    private String ingredientsListString;

    // Instruction
    private String[] instructions;
    private String instructionsString;
    private ArrayList<Ingredients> instructionsList;

    private static String strSeparator = "__,__";

    public Recipe(){}

    public Recipe(String title){
        this.title = title;
    }

    public Recipe(String title, String imgpath, String[] tags, int protein, int fat, int carbs, String timeTaken, int servings, ArrayList<Ingredients> ingredientsList, String[] instructions){
        this.title = title;
        this.imgpath = imgpath;
        this.tags = tags;
        this.tagsString = convertArrayToString(tags);
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.timeTaken = timeTaken;
        this.servings = servings;
        this.ingredientsList = ingredientsList;
        this.ingredientsListString = getJSONStringIngredient(ingredientsList);
        this.instructions = instructions;
        this.instructionsString = convertArrayToString(instructions);
    }

    public Recipe(String title, String imgpath, String tags, int protein, int fat, int carbs, String timeTaken, int servings, String ingredientsList, String instructions){
        this.title = title;
        this.imgpath = imgpath;
        this.tagsString = tags;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.timeTaken = timeTaken;
        this.servings = servings;
        this.ingredientsListString = ingredientsList;
        this.instructionsString = instructions;

        this.tags = convertStringToArray(tagsString);
        this.ingredientsList = convertJSONtoList(ingredientsListString);
        this.instructions = convertStringToArray(instructions);
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imgpath = in.readString();
        tags = in.createStringArray();
        tagsString = in.readString();
        protein = in.readInt();
        fat = in.readInt();
        carbs = in.readInt();
        timeTaken = in.readString();
        servings = in.readInt();
        ingredientsList = in.createTypedArrayList(Ingredients.CREATOR);
        ingredientsListString = in.readString();
        instructions = in.createStringArray();
        instructionsString = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    public ArrayList<Ingredients> convertJSONtoList(String str){
        ArrayList<Ingredients> list = new ArrayList<>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(str);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = null;
                jsonObj = jsonArr.getJSONObject(i);
                Ingredients item = new Ingredients(jsonObj.getString("ingredientsName"),jsonObj.getString("description"));

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public String getJSONString(ArrayList<String> strList){
        Gson gson = new Gson();
        String str = gson.toJson(strList);

        return str;
    }

    public String getJSONStringIngredient(ArrayList<Ingredients> strList){
        Gson gson = new Gson();
        String str = gson.toJson(strList);

        return str;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
        tagsString = convertArrayToString(tags);
    }

    public void setNutritions(int protien, int fat, int carbs){
        this.protein = protien;
        this.fat = fat;
        this.carbs = carbs;
    }

    public void setDetails(String timeTaken, int servings){
        this.timeTaken = timeTaken;
        this.servings = servings;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
        this.ingredientsListString = getJSONStringIngredient(ingredientsList);
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
        this.instructionsString = convertArrayToString(instructions);
    }

    public void setIngredientsListString(String ingredientsListString) {
        this.ingredientsListString = ingredientsListString;
    }

    public void setInstructionsString(String instructionsString) {
        this.instructionsString = instructionsString;
        this.instructions = convertStringToArray(instructionsString);

        ArrayList<Ingredients> list = new ArrayList<>();
        for(int i=0;i<instructions.length;i++){
            int num = i + 1;
            String step = "STEP " + num;
            Ingredients ing = new Ingredients(step, instructions[i]);
            list.add(ing);
        }
        this.instructionsList = list;
    }

    public ArrayList<Ingredients> getInstructionsList() {
        return instructionsList;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImgpath() {
        return imgpath;
    }

    public String[] getTags() {
        return tags;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public int getServings() {
        return servings;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public String getIngredientsListString() {
        return ingredientsListString;
    }

    public String getInstructionsString() {
        return instructionsString;
    }

    public String getTagsString() {
        return tagsString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imgpath);
        parcel.writeStringArray(tags);
        parcel.writeString(tagsString);
        parcel.writeInt(protein);
        parcel.writeInt(fat);
        parcel.writeInt(carbs);
        parcel.writeString(timeTaken);
        parcel.writeInt(servings);
        parcel.writeTypedList(ingredientsList);
        parcel.writeString(ingredientsListString);
        parcel.writeStringArray(instructions);
        parcel.writeString(instructionsString);
    }
}

