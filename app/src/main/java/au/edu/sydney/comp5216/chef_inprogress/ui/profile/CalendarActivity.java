package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.TextView;

import au.edu.sydney.comp5216.chef_inprogress.R;

public class CalendarActivity extends Activity {
    TextView date, proteins, fat, carbs;
    GridView gridView;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_modal);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*.8));

        date = findViewById(R.id.date);
        proteins = findViewById(R.id.protein_intake);
        fat = findViewById(R.id.fat_intake);
        carbs = findViewById(R.id.carbs_intake);

        Intent intent = getIntent();
        date.setText(intent.getStringExtra("date"));
    }
}
