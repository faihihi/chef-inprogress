package au.edu.sydney.comp5216.chef_inprogress;

public class InventoryItem {
    private int id;
    private String itemName;
    private String category;
    private int icon;
    private int userInventory; // 1 true, 0 false

    public InventoryItem(String itemName, String category, int icon){
        this.itemName = itemName;
        this.category = category;
        this.icon = icon;
        this.userInventory = 0;
    }

    public InventoryItem(int id, String itemName, String category, int icon, int userInventory){
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.icon = icon;
        this.userInventory = userInventory;
    }

    public void setUserInventory(int userInventory) {
        this.userInventory = userInventory;
    }

    public int getId() {
        return id;
    }

    public String getItemName(){return itemName;}
    public String getCategory(){return category;}
    public int getIcon() {return icon;}

    public int getUserInventory() {
        return userInventory;
    }
}
