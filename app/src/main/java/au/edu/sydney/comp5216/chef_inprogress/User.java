package au.edu.sydney.comp5216.chef_inprogress;

import java.util.ArrayList;

/**
 * User class
 */
public class User {
    private String key;
    private String email;
    private String name;

    private ArrayList<Inventory> userInventory;
    private ArrayList<String> inventory;

    private ArrayList<String> shoppinglist;
    private ArrayList<Integer> shoppinglistcheck;

    // Recipes that user have made
    private ArrayList<String> completedrecipe;
    private ArrayList<String> completedDate;

    private ArrayList<String> favorites;

    // For local database
    private String inventoryStr;
    private String shoppingStr;
    private String shoppingcheckStr;
    private String completedStr;
    private String completedDateStr;
    private String favoriteStr;

    private static String strSeparator = "__,__";

    /**
     * void constructor
     */
    public User(){}

    /**
     * Constructor for dealing with device local database
     * @param key
     * @param name
     * @param email
     * @param inventoryStr
     * @param shoppingStr
     * @param shoppingcheckStr
     * @param completedStr
     * @param completedDateStr
     * @param favoriteStr
     */
    public User(String key, String name, String email, String inventoryStr, String shoppingStr, String shoppingcheckStr, String completedStr, String completedDateStr, String favoriteStr){
        this.key = key;
        this.name = name;
        this.email = email;
        this.inventoryStr = inventoryStr;
        this.inventory = convertStringToArray(inventoryStr);

        this.shoppingStr = shoppingStr;
        this.shoppinglist = convertStringToArray(shoppingStr);
        this.shoppingcheckStr = shoppingcheckStr;
        this.shoppinglistcheck = convertStringToIntArray(shoppingcheckStr);

        this.completedStr = completedStr;
        this.completedrecipe = convertStringToArray(completedStr);
        this.completedDateStr = completedDateStr;
        this.completedDate = convertStringToArray(completedDateStr);

        this.favoriteStr = favoriteStr;
        this.favorites = convertStringToArray(favoriteStr);
    }

    /**
     * Constructor for firebase: when retrieving data
     * @param key
     * @param name
     * @param email
     * @param inventory
     * @param shoppinglist
     * @param shoppinglistcheck
     * @param completedrecipe
     * @param completedDate
     * @param favorites
     */
    public User(String key, String name, String email, ArrayList<String> inventory, ArrayList<String> shoppinglist, ArrayList<Integer> shoppinglistcheck, ArrayList<String> completedrecipe, ArrayList<String> completedDate, ArrayList<String> favorites) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.inventory = inventory;
        this.inventoryStr = convertArrayToString(inventory);

        this.shoppinglist = shoppinglist;
        this.shoppinglistcheck = shoppinglistcheck;
        this.shoppingStr = convertArrayToString(shoppinglist);
        this.shoppingcheckStr = convertIntArrayToString(shoppinglistcheck);

        this.completedrecipe = completedrecipe;
        this.completedStr = convertArrayToString(completedrecipe);
        this.completedDate = completedDate;
        this.completedDateStr = convertArrayToString(completedDate);

        this.favorites = favorites;
        this.favoriteStr = convertArrayToString(favorites);
    }

    /**
     * Constructor for firebase: when posting data
     * @param name
     * @param email
     * @param inventory
     * @param shoppinglist
     * @param shoppinglistcheck
     * @param completedrecipe
     * @param completedDate
     * @param favorites
     */
    public User(String name, String email, ArrayList<String> inventory, ArrayList<String> shoppinglist, ArrayList<Integer> shoppinglistcheck, ArrayList<String> completedrecipe, ArrayList<String> completedDate, ArrayList<String> favorites){
        this.name = name;
        this.email = email;
        this.inventory = inventory;
        this.shoppinglist = shoppinglist;
        this.shoppinglistcheck = shoppinglistcheck;
        this.completedrecipe = completedrecipe;
        this.completedDate = completedDate;
        this.favorites = favorites;
    }

    /**
     * Constructor for firebase: when account is first created
     * @param name
     * @param email
     */
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    /**
     * Method of converting array list of String to String
     * @param array
     * @return
     */
    public static String convertArrayToString(ArrayList<String> array){
        String str = "";
        for (int i = 0;i<array.size(); i++) {
            str = str+array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    /**
     * Method for converting Array List of Integer to String
     * @param array
     * @return
     */
    public static String convertIntArrayToString(ArrayList<Integer> array){
        String str = "";
        for (int i = 0;i<array.size(); i++) {
            str = str+array.get(i);
            // Do not append comma at the end of last element
            if(i<array.size()-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    /**
     * Method for converting String to ArrayList of String
     * @param str
     * @return
     */
    public static ArrayList<String> convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        ArrayList<String> result = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            result.add(arr[i]);
        }
        return result;
    }

    /**
     * Method for converting String to ArrayList of Integer
     * @param str
     * @return
     */
    public static ArrayList<Integer> convertStringToIntArray(String str){
        String[] arr = str.split(strSeparator);
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            result.add(Integer.parseInt(arr[i]));
        }
        return result;
    }


    /** GETTERS **/
    public String getKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Inventory> getUserInventory() {
        return userInventory;
    }

    public ArrayList<String> getInventory() {
        return inventory;
    }

    public ArrayList<String> getShoppinglist() {
        return shoppinglist;
    }

    public ArrayList<Integer> getShoppinglistcheck() {
        return shoppinglistcheck;
    }

    public ArrayList<String> getCompletedrecipe() {
        return completedrecipe;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public String getInventoryStr() {
        return inventoryStr;
    }

    public String getShoppingStr() {
        return shoppingStr;
    }

    public String getShoppingcheckStr() {
        return shoppingcheckStr;
    }

    public String getCompletedStr() {
        return completedStr;
    }

    public String getFavoriteStr() {
        return favoriteStr;
    }

    public static String getStrSeparator() {
        return strSeparator;
    }

    public ArrayList<String> getCompletedDate() {
        return completedDate;
    }

    public String getName() {
        return name;
    }

    public String getCompletedDateStr() {
        return completedDateStr;
    }

    /** SETTERS **/
    public void setKey(String key) {
        this.key = key;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserInventory(ArrayList<Inventory> userInventory) {
        this.userInventory = userInventory;
    }

    public void setInventory(ArrayList<String> inventory) {
        this.inventory = inventory;
    }

    public void setShoppinglist(ArrayList<String> shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    public void setShoppinglistcheck(ArrayList<Integer> shoppinglistcheck) {
        this.shoppinglistcheck = shoppinglistcheck;
    }

    public void setCompletedrecipe(ArrayList<String> completedrecipe) {
        this.completedrecipe = completedrecipe;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.favorites = favorites;
    }

    public void setInventoryStr(String inventoryStr) {
        this.inventoryStr = inventoryStr;
    }

    public void setShoppingStr(String shoppingStr) {
        this.shoppingStr = shoppingStr;
    }

    public void setShoppingcheckStr(String shoppingcheckStr) {
        this.shoppingcheckStr = shoppingcheckStr;
    }

    public void setCompletedStr(String completedStr) {
        this.completedStr = completedStr;
    }

    public void setFavoriteStr(String favoriteStr) {
        this.favoriteStr = favoriteStr;
    }

    public static void setStrSeparator(String strSeparator) {
        User.strSeparator = strSeparator;
    }

    public void setCompletedDate(ArrayList<String> completedDate) {
        this.completedDate = completedDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompletedDateStr(String completedDateStr) {
        this.completedDateStr = completedDateStr;
    }
}
