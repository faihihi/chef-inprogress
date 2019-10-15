package au.edu.sydney.comp5216.chef_inprogress.ui.inventory;

public class ShoppinglistItem {
    boolean checked;
    String name;

    ShoppinglistItem(boolean b, String n){
        checked = b;
        name = n;
    }

    public boolean isChecked(){
        return checked;
    }

    public String getName() {
        return name;
    }
}
