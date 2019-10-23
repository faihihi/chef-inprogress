package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.edu.sydney.comp5216.chef_inprogress.FirebaseRecipeDBHelper;
import au.edu.sydney.comp5216.chef_inprogress.Login;
import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.Recipe;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

public class ProfileFragment extends Fragment {
    private CalendarView calendarView;
    private TextView username, email, protein, fat, carbs;
    private ImageView logout;

    private UserDBHelper userDBHelper;

    private int protein_sum, fat_sum, carbs_sum;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getContext(), Login.class));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();

        username = (TextView) root.findViewById(R.id.user_name);
        email = (TextView) root.findViewById(R.id.email);
        protein = (TextView) root.findViewById(R.id.protein_value);
        fat = (TextView) root.findViewById(R.id.fat_value);
        carbs = (TextView) root.findViewById(R.id.carbs_value);


        logout = (ImageView) root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        username.setText(c.getName());
        email.setText(c.getEmail());

        final ArrayList<String> completed = c.getCompletedrecipe();
        final ArrayList<String> completedDate = c.getCompletedDate();

        getMonthlyIntake(completed, completedDate);

        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int month = i1 + 1;
                String date = i + "/" + month + "/" + i2;

                ArrayList<String> recipesMadeOnThisDate = getRecipesMadeOnThisDate(completed, completedDate, date);

                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("recipes", recipesMadeOnThisDate);

                startActivity(intent);
            }
        });

        return root;
    }

    public void getMonthlyIntake(ArrayList<String> completed, ArrayList<String> completedDate){
        int idx = 0;
        protein_sum = 0;
        fat_sum = 0;
        carbs_sum = 0;
        for(String dateStr: completedDate){
            boolean dateInThisMonth = false;
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);

                //Create 2 instances of Calendar
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                //set the given date in one of the instance and current date in the other
                cal1.setTime(date);
                cal2.setTime(new Date());

                //now compare the dates using methods on Calendar
                if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                        // the date falls in current month
                        dateInThisMonth = true;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(dateInThisMonth){
//                completed.get(idx)

                // Query for nutrition of the recipe
                new FirebaseRecipeDBHelper().getRecipeByTitle(completed.get(idx), new FirebaseRecipeDBHelper.DataStatus() {
                    @Override
                    public void RecipeisLoaded(List<Recipe> recipes, List<String> keys) {
                        for (Recipe recipe : recipes) {
                            protein_sum = protein_sum + recipe.getProtein();
                            fat_sum = fat_sum + recipe.getFat();
                            carbs_sum = carbs_sum + recipe.getCarb();

                            protein.setText(String.valueOf(protein_sum) + " g");
                            fat.setText(String.valueOf(fat_sum) + " g");
                            carbs.setText(String.valueOf(carbs_sum) + " g");
                        }
                    }

                    @Override
                    public void DataIsUpdated() {

                    }
                });

            }

            idx++;
        }
    }

    public ArrayList<String> getRecipesMadeOnThisDate(ArrayList<String> completed, ArrayList<String> completedDate, String selecteddate){
//        final ArrayList<Recipe> recipesMadeOnThisDate = new ArrayList<>();
        ArrayList<String> recipesMadeOnThisDate = new ArrayList<>();
        int idx = 0;
        for(String dateStr: completedDate){
            boolean madeOnThisDate = false;
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(dateStr);
                Date selectedDate = new SimpleDateFormat("yyyy/MM/dd").parse(selecteddate);

                //Create 2 instances of Calendar
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                //set the given date in one of the instance and current date in the other
                cal1.setTime(date);
                cal2.setTime(selectedDate);

                //now compare the dates using methods on Calendar
                if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                        recipesMadeOnThisDate.add(completed.get(idx));
                        madeOnThisDate = true;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            idx++;
        }
        return recipesMadeOnThisDate;
    }

}
