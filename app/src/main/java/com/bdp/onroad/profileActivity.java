package com.bdp.onroad;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class profileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView userName;
    TextView email;
    ImageView profilePic;
    EditText edittext;
    Long millis;
    SharedPreferences prefs;
    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            millis = myCalendar.getTimeInMillis();
            updateLabel();
            prefs.edit().putLong("time", millis).apply();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        userName = findViewById(R.id.userName);
        profilePic = findViewById(R.id.profile_image);
        email = findViewById(R.id.email);
        email.setText(firebaseUser.getEmail());
        userName.setText(firebaseUser.getDisplayName());
        edittext=findViewById(R.id.Birthday);
        prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Date myDate = new Date(prefs.getLong("time", 0));
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(myDate);
        edittext.setText(dateString);
        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(profileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext.setText(sdf.format(myCalendar.getTime()));
    }
    }

