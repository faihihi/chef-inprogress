package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import au.edu.sydney.comp5216.chef_inprogress.R;


public class FavoriteFragment extends Fragment{

    private ArrayList<Favorite> arrayList;
    FavoriteAdapter arrayAdapter;

    EditText searchTXT;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        searchTXT = root.findViewById(R.id.searchTxt);


        arrayList = new ArrayList<>();

        arrayList.add(new Favorite("Fried chicken","11 min","https://food.fnr.sndimg.com/content/dam/images/food/fullset/2012/11/2/0/DV1510H_fried-chicken-recipe-10_s4x3.jpg.rend.hgtvcom.826.620.suffix/1568222255998.jpeg"));
        arrayList.add(new Favorite("Salad","5 min","https://www.diabetesfoodhub.org/system/thumbs/system/images/recipes/931-diabetic-powerhouse-kale-salad_designed-for-one_071118_3547183137.jpg"));
        arrayList.add(new Favorite("Pizza","30 min","https://www.biggerbolderbaking.com/wp-content/uploads/2019/07/15-Minute-Pizza-WS-Thumbnail.png"));
        arrayList.add(new Favorite("Burger","15 min","https://media1.s-nbcnews.com/j/newscms/2019_21/2870431/190524-classic-american-cheeseburger-ew-207p_d9270c5c545b30ea094084c7f2342eb4.fit-760w.jpg"));

        arrayAdapter = new FavoriteAdapter(getContext(), arrayList);
        ListView listView = root.findViewById(R.id.favouriteList);

        listView.setTextFilterEnabled(true);

        listView.setAdapter(arrayAdapter);

        searchTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FavoriteFragment.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //filter(s.toString());
            }
        });

        return root;
    }

}
