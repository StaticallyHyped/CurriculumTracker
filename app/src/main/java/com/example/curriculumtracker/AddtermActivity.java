package com.example.curriculumtracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddtermActivity extends AppCompatActivity {

    public static final String TAG = "AddTermActivity";

    private enum AddOrEdit {EDIT, ADD}
    private AddOrEdit mMode;
    private Button cancelBtn;
    private Button saveBtn;
    private CalendarView startCal;
    private CalendarView endCal;
    private EditText title;
    private String startDate;
    private String endDate;


    public AddtermActivity() {
        Log.d(TAG, "AddtermActivity: constructor called");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addterm);
        cancelBtn = findViewById(R.id.activity_addterm_btndelete);
        saveBtn = findViewById(R.id.activity_addterm_btnsave);
        startCal = findViewById(R.id.activity_addterm_startcal);
        endCal = findViewById(R.id.activity_addterm_endcal);
        title = findViewById(R.id.activity_addterm_titleet);

        Bundle arguments = savedInstanceState;


//        Context context = getApplicationContext();
//        term = (Term) arguments.getSerializable(Term.class.getSimpleName());
//        term = (Term) context.getSerializable(Term.class.getSimpleName());
        final Term term;

        if (arguments != null) {
            Log.d(TAG, "onCreate: arguments not null, found a term" );
            term = (Term) arguments.getSerializable(Term.class.getSimpleName());

            if (term != null) {
                title.setText(term.getTitle());
                mMode = AddOrEdit.EDIT;
            } else {
                Log.d(TAG, "onCreate: TERM NOT NULL, FRAGMENTEDITMODE?");
                mMode = AddOrEdit.ADD;
            }

        } else {
            Log.d(TAG, "onCreate: TERM WAS NULL, ENTER EDIT MODE");
            term = null;
            mMode = AddOrEdit.ADD;
        }
        startCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String startMonth = Integer.toString(month + 1);
                String startDay = Integer.toString(dayOfMonth);
                String startYear = Integer.toString(year);
                startDate = (startMonth + 1) + "/" + startDay + "/" + startYear;
                Log.d(TAG, "onSelectedDayChange: start date" + startDate);

            }
        });

        endCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                endDate = month + "/" + dayOfMonth + "/" + year;
                Log.d(TAG, "onSelectedDayChange: end date" + endDate);

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String startSimpleDate = sdf.format(new Date(startCal.getDate()));
        Log.d(TAG, "onCreate: start date in SDF:" + startSimpleDate);

        SimpleDateFormat endingSDF = new SimpleDateFormat("dd/MM/yyyy");
        String endSimpleDate = endingSDF.format(new Date(endCal.getDate()));
        Log.d(TAG, "onCreate: end date in SDF:" + endSimpleDate);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: setOnClick listener starts");
                Context testContext = v.getContext();
                ContentResolver contentResolver = testContext.getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (term == null){
                            break;
                        }
                        if (!title.getText().toString().equals(term.getTitle())) {
                            values.put(TermsContract.Columns.TERMS_TITLE, title.getText().toString());
                            Log.d(TAG, "onClick: title: " + title);
                        }
                        if (!startDate.equals(term.getStart())){
                            values.put(TermsContract.Columns.TERMS_STARTDATE, startDate);
                        }
                        if (!endDate.equals(term.getEnd())){
                            values.put(TermsContract.Columns.TERMS_ENDDATE, endDate);
                        }
                        if (values.size() != 0){
                            contentResolver.update(TermsContract.buildTermURI(term.getId()), values, null, null);
                            saveGoToTermsActivity(v);
                        }

                    case ADD:
                        if (title.length() > 0) {
                            values.put(TermsContract.Columns.TERMS_TITLE, title.getText().toString());
                            values.put(TermsContract.Columns.TERMS_STARTDATE, startDate);
                            values.put(TermsContract.Columns.TERMS_ENDDATE, endDate);
                            contentResolver.insert(TermsContract.CONTENT_URI, values);
                            Log.d(TAG, "onClick: in ADD: " + title + startDate + endDate);
                            saveGoToTermsActivity(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Term Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    public void saveGoToTermsActivity(View v){
        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        cancelBackToTermsActivity(v);
    }

    public void cancelBackToTermsActivity(View v){
        Toast.makeText(getApplicationContext(), "Process Cancelled", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddtermActivity.this, TermsActivity.class));

    }

}
