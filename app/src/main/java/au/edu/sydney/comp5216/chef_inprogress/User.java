package au.edu.sydney.comp5216.chef_inprogress;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;

    private ArrayList<Inventory> userInventory;
    private ArrayList<String> userShoppingList;

    // Recipes that user have made
    private ArrayList<String> completedRecipeList;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void setUserInventory(ArrayList<Inventory> userInventory) {
        this.userInventory = userInventory;
    }

    public void setCompletedRecipeList(ArrayList<String> completedRecipeList) {
        this.completedRecipeList = completedRecipeList;
    }

    public void setUserShoppingList(ArrayList<String> userShoppingList) {
        this.userShoppingList = userShoppingList;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Inventory> getUserInventory() {
        return userInventory;
    }

    public ArrayList<String> getCompletedRecipeList() {
        return completedRecipeList;
    }

    public ArrayList<String> getUserShoppingList() {
        return userShoppingList;
    }

}
