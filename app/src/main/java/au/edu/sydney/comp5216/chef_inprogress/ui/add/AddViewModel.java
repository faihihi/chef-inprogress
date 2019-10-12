package au.edu.sydney.comp5216.chef_inprogress.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.InventoryItem;
import au.edu.sydney.comp5216.chef_inprogress.R;

public class AddViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<InventoryItem>> inventoryList;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Add fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<InventoryItem>> getInventoryList(){
        if(inventoryList == null){
            inventoryList = new MutableLiveData<>();
            loadInventoryList();
        }
        return inventoryList;
    }

    private void loadInventoryList(){
        List<InventoryItem> inventoryItemList = new ArrayList<>();

        inventoryItemList.add(new InventoryItem("Apple", "Fruit", R.drawable.apple));
        inventoryItemList.add(new InventoryItem("Fish","Meat",R.drawable.fish));

        inventoryList.setValue(inventoryItemList);
    }
}