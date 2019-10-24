package au.edu.sydney.comp5216.chef_inprogress;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Recipe class
 */
public class Recipe implements Parcelable {
    private int id;
    private String title;
    private String imgpath;

    // Tags ex. vegetarian, gluten-free
    private ArrayList<String> tags;

    // Nutrition intake in gram
    private int protein;
    private int fat;
    private int carb;

    // Details
    private String timeTaken;
    private int servings;

    // Ingredients
    private ArrayList<String> ingredientFB;
    private ArrayList<Ingredients> ingredientsList;

    // Instruction
    private ArrayList<String> instructions;

    // User favorite flat
    private boolean userFavorite;

    public Recipe(){}

    /**
     * Default constructor of recipe
     * @param title
     * @param imgpath
     * @param tags
     * @param protein
     * @param fat
     * @param carbs
     * @param timeTaken
     * @param servings
     * @param ingredientFB
     * @param instructions
     */
    public Recipe(String title, String imgpath, ArrayList<String> tags, int protein, int fat, int carbs, String timeTaken, int servings, ArrayList<String> ingredientFB, ArrayList<String> instructions) {
        this.title = title;
        this.imgpath = imgpath;
        this.tags = tags;
        this.protein = protein;
        this.fat = fat;
        this.carb = carbs;
        this.timeTaken = timeTaken;
        this.servings = servings;
        this.ingredientFB = ingredientFB;
        this.ingredientsList = setIngredientsString(ingredientFB);
        this.instructions = instructions;
    }

    /**
     * Constructor for passing intent as parcel
     * @param in
     */
    protected Recipe(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imgpath = in.readString();
        tags = in.createStringArrayList();
        protein = in.readInt();
        fat = in.readInt();
        carb = in.readInt();
        timeTaken = in.readString();
        servings = in.readInt();
        ingredientFB = in.createStringArrayList();
        ingredientsList = in.createTypedArrayList(Ingredients.CREATOR);
        instructions = in.createStringArrayList();
        userFavorite = in.readByte() != 0;
    }

    /**
     * Parcelable methods
     */
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

    /**
     * For setting instruction string from arrayList
     * @param instructions
     * @return
     */
    public ArrayList<Ingredients> setInstructionsString(ArrayList<String> instructions) {
        ArrayList<Ingredients> list = new ArrayList<>();
        for(int i=0;i<instructions.size();i++){
            int num = i + 1;
            String step = "STEP " + num;
            Ingredients ing = new Ingredients(step, instructions.get(i));
            list.add(ing);
        }
        return list;
    }

    /**
     * For setting ingredients string from arraylist
     * @param ingredients
     * @return
     */
    public ArrayList<Ingredients> setIngredientsString(ArrayList<String> ingredients){
        ArrayList<Ingredients> list = new ArrayList<>();
        for(String word : ingredients){
            String[] temp = word.split("-");
            list.add(new Ingredients(temp[0], temp[1]));
        }
        this.ingredientsList = ingredientsList;
        return list;
    }

    /** GETTERS **/
    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    public int getCarb() {
        return carb;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public int getServings() {
        return servings;
    }

    public ArrayList<String> getIngredientFB() {
        return ingredientFB;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public boolean getUserFavorite(){
        return userFavorite;
    }

    /** SETTERS **/
    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setIngredientFB(ArrayList<String> ingredientFB) {
        this.ingredientFB = ingredientFB;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public void setUserFavorite(boolean userFavorite) {
        this.userFavorite = userFavorite;
    }

    /**
     * Parcelable method
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write to parcel
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imgpath);
        parcel.writeStringList(tags);
        parcel.writeInt(protein);
        parcel.writeInt(fat);
        parcel.writeInt(carb);
        parcel.writeString(timeTaken);
        parcel.writeInt(servings);
        parcel.writeStringList(ingredientFB);
        parcel.writeTypedList(ingredientsList);
        parcel.writeStringList(instructions);
        parcel.writeByte((byte) (userFavorite ? 1 : 0));
    }
}

