package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

/**
 * Shopping List Item class
 */
public class ShoppinglistItem {
    boolean checked;
    String name;

    /**
     * Constructor
     * @param b
     * @param n
     */
    public ShoppinglistItem(boolean b, String n){
        checked = b;
        name = n;
    }

    /**
     * Get checked state
     * @return
     */
    public boolean isChecked(){
        return checked;
    }

    /**
     * Get name of shopping list
     * @return
     */
    public String getName() {
        return name;
    }

}