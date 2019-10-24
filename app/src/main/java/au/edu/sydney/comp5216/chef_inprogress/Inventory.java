package au.edu.sydney.comp5216.chef_inprogress;

/**
 * Inventory class for ingredients information
 */
public class Inventory {
    private int id;
    private String itemName;
    private String category;
    private int icon;
    private int userInventory; // 1 true, 0 false

    /**
     * Constructor without ID and userInventory data
     * @param itemName
     * @param category
     * @param icon
     */
    public Inventory(String itemName, String category, int icon){
        this.itemName = itemName;
        this.category = category;
        this.icon = icon;
        this.userInventory = 0;
    }

    /**
     * Constructor
     * @param id
     * @param itemName
     * @param category
     * @param icon
     * @param userInventory
     */
    public Inventory(int id, String itemName, String category, int icon, int userInventory){
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.icon = icon;
        this.userInventory = userInventory;
    }

    /**
     * Setting user inventory
     * @param userInventory
     */
    public void setUserInventory(int userInventory) {
        this.userInventory = userInventory;
    }

    /** GETTERS **/
    public int getId() {return id; }
    public String getItemName(){return itemName;}
    public String getItemNameWithS(){ return itemName + "s";}
    public String getItemNameWithES(){return itemName + "es";}
    public String getCategory(){return category;}
    public int getIcon() {return icon;}
    public int getUserInventory() { return userInventory; }
}
