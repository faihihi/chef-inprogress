package au.edu.sydney.comp5216.chef_inprogress;

public class InventoryItem {
    private String itemName;
    private String category;
    private int icon;

    public InventoryItem(String itemName, String category, int icon){
        this.itemName = itemName;
        this.category = category;
        this.icon = icon;
    }

    public String getItemName(){return itemName;}
    public String getCategory(){return category;}
    public int getIcon() {return icon;}
}
