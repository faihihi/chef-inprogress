package au.edu.sydney.comp5216.chef_inprogress.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.edu.sydney.comp5216.chef_inprogress.R;
import au.edu.sydney.comp5216.chef_inprogress.User;
import au.edu.sydney.comp5216.chef_inprogress.UserDBHelper;

public class ProfileFragment extends Fragment {
    private CalendarView calendarView;
    private TextView username, email, protein, fat, carbs;

    private UserDBHelper userDBHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        userDBHelper = new UserDBHelper(getContext());
        User c = userDBHelper.getThisUser();

        username = (TextView) root.findViewById(R.id.user_name);
        email = (TextView) root.findViewById(R.id.email);
        protein = (TextView) root.findViewById(R.id.protein_value);
        fat = (TextView) root.findViewById(R.id.fat_value);
        carbs = (TextView) root.findViewById(R.id.carbs_value);

        username.setText(c.getName());
        email.setText(c.getEmail());

        ArrayList<String> completed = c.getCompletedrecipe();
        ArrayList<String> completedDate = c.getCompletedDate();

        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i + "/" + i1 + "/" + i2;
                Log.d("SELECTED DATE", date);

                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("date", date);

                startActivity(intent);
            }
        });

        return root;
    }

    public int getMonthlyIntake(ArrayList<String> completed, ArrayList<String> completedDate){
        boolean dateInThisMonth = false;
        int idx = 0;
        for(String dateStr: completedDate){
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

            }

            idx++;
        }



        return 0;
    }

}
