package au.edu.sydney.comp5216.chef_inprogress;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String key;
    private String email;

    private ArrayList<Inventory> userInventory;
    private List<String> inventory;

    private ArrayList<String> userShoppingList;
    private List<String> shoppinglist;
    private List<Integer> shoppinglistcheck;

    // Recipes that user have made
    private ArrayList<String> completedRecipeList;
    private List<String> completedrecipe;

    public User(){}

    public User(String email, List<String> inventory, List<String> shoppinglist, List<Integer> shoppinglistcheck, List<String> completedrecipe) {
        this.email = email;
        this.inventory = inventory;
        this.shoppinglist = shoppinglist;
        this.shoppinglistcheck = shoppinglistcheck;
        this.completedrecipe = completedrecipe;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Inventory> getUserInventory() {
        return userInventory;
    }

    public void setUserInventory(ArrayList<Inventory> userInventory) {
        this.userInventory = userInventory;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<String> getUserShoppingList() {
        return userShoppingList;
    }

    public void setUserShoppingList(ArrayList<String> userShoppingList) {
        this.userShoppingList = userShoppingList;
    }

    public List<String> getShoppingList() {
        return shoppinglist;
    }

    public void setShoppinglistcheck(List<Integer> shoppingCheck) {
        this.shoppinglistcheck = shoppingCheck;
    }

    public List<Integer> getShoppinglistcheck() {
        return shoppinglistcheck;
    }

    public void setShoppingList(List<String> shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    public ArrayList<String> getCompletedRecipeList() {
        return completedRecipeList;
    }

    public void setCompletedRecipeList(ArrayList<String> completedRecipeList) {
        this.completedRecipeList = completedRecipeList;
    }

    public List<String> getCompletedrecipe() {
        return completedrecipe;
    }

    public void setCompletedrecipe(List<String> completedrecipe) {
        this.completedrecipe = completedrecipe;
    }
}
