package com.example.curriculumtracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddtermActivity extends AppCompatActivity {

    public static final String TAG = "AddTermActivity";

    private enum AddOrEdit {EDIT, ADD}
    private AddOrEdit mMode;
    private Button cancelBtn;
    private Button saveBtn;
    private CalendarView startCal;
    private CalendarView endCal;

    private String startDate;
    private String endDate;
    private long termId;
    private String mTitle, mStart, mEnd, addTitle, title;
    boolean bEdit;
    private EditText titleTE;

    public AddtermActivity() {
        Log.d(TAG, "AddtermActivity: constructor called");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addterm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_addterm_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add a Term");

        cancelBtn = findViewById(R.id.activity_addterm_btndelete);
        saveBtn = findViewById(R.id.activity_addterm_btnsave);
        startCal = findViewById(R.id.activity_addterm_startcal);
        endCal = findViewById(R.id.activity_addterm_endcal);
        titleTE = findViewById(R.id.content_addterm_titleET);
        bEdit = false;

        getBooleanIntent();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date today = Calendar.getInstance().getTime();
        String setDefaultDate = df.format(today);

        if (bEdit) {
            mMode = AddOrEdit.EDIT;
            getIncomingIntent();
            setStartCal();
            setEndCal();
            toolbar.setTitle("Edit a Term");
            title = mTitle;
            startDate = mStart;
            endDate = mEnd;
        } else {
            title = titleTE.getText().toString();
            mMode = AddOrEdit.ADD;
            toolbar.setTitle("Add a Term");
            startDate = setDefaultDate;
            endDate = setDefaultDate;

        }

        startCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startDate = (month +1) + "/" + dayOfMonth + "/" + year;
            }
        });

        endCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                endDate = (month +1) + "/" + dayOfMonth + "/" + year;
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: setOnClick listener starts");
                Context testContext = v.getContext();
                ContentResolver contentResolver = testContext.getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        title = titleTE.getText().toString();
                        Log.d(TAG, "onClick: YOU'RE UPDATING/EDITING A TERM");
                        if (!title.equals(mTitle)) {
                            values.put(TermsContract.Columns.TERMS_TITLE, titleTE.getText().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        if (!startDate.equals(mStart)){
                            values.put(TermsContract.Columns.TERMS_STARTDATE, startDate);
                        }
                        if (!endDate.equals(mEnd)){
                            values.put(TermsContract.Columns.TERMS_ENDDATE, endDate);
                        }
                        if (values.size() != 0){
                            contentResolver.update(TermsContract.buildTermURI(termId), values, null, null);
                            saveGoToTermsActivity(v);
                        }
                        break;
                    case ADD:
                        title = titleTE.getText().toString();
                        Log.d(TAG, "onClick: TITLE = " + title);
                        if (title.length() > 0) {
                            values.put(TermsContract.Columns.TERMS_TITLE, titleTE.getText().toString());
                            values.put(TermsContract.Columns.TERMS_STARTDATE, startDate);
                            values.put(TermsContract.Columns.TERMS_ENDDATE, endDate);
                            contentResolver.insert(TermsContract.CONTENT_URI, values);
                            Log.d(TAG, "onClick: in ADD: " + title + startDate + endDate);
                            saveGoToTermsActivity(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.backToMain:
                Intent intent1 = new Intent(this, MainActivity.class);
                Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
                startActivity(intent1);
                break;
            case R.id.alertCourseStart:
                Intent intent = new Intent(this, SetCourseAlertActivity.class);
                intent.putExtra("start_alert", "start");
                Log.d(TAG, "onOptionsItemSelected: alertCourseStart " + item.getItemId());
                startActivity(intent);
                break;
            case R.id.alertCourseEnd:
                Intent intent2 = new Intent(this, SetCourseAlertActivity.class);
                intent2.putExtra("start_alert", "end");
                startActivity(intent2);
                break;
            case R.id.alertAsmtDue:
                startActivity(new Intent(this, SetAsmtAlertActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIncomingIntent() {
        String stringId = getIntent().getStringExtra("term_id");
        termId = Long.parseLong(stringId);

        if (getIntent().hasExtra("title")){
            mTitle = getIntent().getStringExtra("title");
            titleTE.setText(mTitle);
        }
        if (getIntent().hasExtra("start")){
            mStart = getIntent().getStringExtra("start");
        }
        if (getIntent().hasExtra("end")){
            mEnd = getIntent().getStringExtra("end");
        }

    }

    private void getBooleanIntent(){
        if (getIntent().hasExtra("bEdit")){
            bEdit = true;
        }
    }

    public void saveGoToTermsActivity(View v){
        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddtermActivity.this, TermsActivity.class));
    }

    public void cancelBackToTermsActivity(View v){
        Toast.makeText(getApplicationContext(), "Process Cancelled", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddtermActivity.this, TermsActivity.class));
    }

    private void setStartCal(){
        String date = mStart;
        String parts [] = date.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.YEAR, year);
        long timeInMillies = calendar.getTimeInMillis();
        startCal.setDate(timeInMillies);
    }

    private void setEndCal(){
        String date = mEnd;
        String parts [] = date.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.YEAR, year);
        long timeInMillies = calendar.getTimeInMillis();
        endCal.setDate(timeInMillies);

    }

}
