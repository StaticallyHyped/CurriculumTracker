package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddcourseActivity extends AppCompatActivity {

    public static final String TAG = "AddcourseActivity";

    private EditText title;
    private Spinner statusSP;
    private EditText note;
    private EditText mentorName;
    private EditText mentorPhone;
    private EditText mentorEmail;
    private CalendarView startCal;
    private CalendarView endCal;
    private Button saveBtn;
    private Button cancelBtn;
    private String startDate;
    private String endDate;
    private boolean bEdit;
    private long courseId;
    private Spinner termSP;

    private String mTitle, mStart, mEnd, mStatus, mNote, mMentName, mMentPhone, mMentEmail, mTerm, term1, term2, term3, term4;


    private enum AddOrEdit {EDIT, ADD}
    private AddOrEdit mMode;



    public AddcourseActivity() {
        Log.d(TAG, "AddcourseActivity: constructor called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.activity_addcourse_titleET);
        statusSP = findViewById(R.id.activity_addcourse_statusSP);
        note = findViewById(R.id.activity_addcourse_noteET);
        startCal = findViewById(R.id.activity_addcourse_startCal);
        endCal = findViewById(R.id.activity_addcourse_endCal);
        mentorName = findViewById(R.id.activity_addcourse_mentnameTI);
        mentorEmail = findViewById(R.id.activity_addcourse_mentemail);
        mentorPhone = findViewById(R.id.activity_addcourse_mentphoneET);
        saveBtn = findViewById(R.id.activity_addcourse_saveBtn);
        cancelBtn = findViewById(R.id.activity_addcourse_cancelBtn);
        termSP = findViewById(R.id.layout_addcourse_termSP);
        bEdit = false;
        ActionBar ab = getSupportActionBar();
        ab.setTitle("THIS IS A TOOLBAR TITLE");

        String [] statusList = new String[]{
        "Not Started", "In Progress", "Complete"
        };

        ArrayAdapter<String> spinnerAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
        spinnerAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSP.setAdapter(spinnerAA);

        String [] termArray = new String [] {
                "Term 1", "Term 2", "Term 3", "Term 4"
        };
        ArrayAdapter termArrayListAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, termArray);
        termArrayListAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSP.setAdapter(termArrayListAA);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date today = Calendar.getInstance().getTime();
        String setDefaultDate = df.format(today);

        getBooleanIntent();
        Log.d(TAG, "onCreate: " + bEdit);
        if (bEdit){
            Log.d(TAG, "onCreate: EDIT MODE");
            mMode = AddOrEdit.EDIT;
            toolbar.setTitle("Edit a Course");
            getIncomingIntent();
            getTermSelection();
            setStartCal();
            setEndCal();
            startDate = mStart;
            endDate = mEnd;

            switch (mStatus) {
                case "Not Started":
                    statusSP.setSelection(0);
                    break;
                case "In Progress":
                    statusSP.setSelection(1);
                    break;
                case "Completed":
                    statusSP.setSelection(2);
                    break;
                default:
                    statusSP.setSelection(0);
            }

        } else {
            Log.d(TAG, "onCreate: NOW IN ADD MODE");
            mMode = AddOrEdit.ADD;
            toolbar.setTitle("Add a Course");
            termSP.setSelection(0);
            statusSP.setSelection(0);
            mStatus = statusSP.getSelectedItem().toString();
            startDate = setDefaultDate;
            endDate = setDefaultDate;
        }

        statusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStatus = statusSP.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            termSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mTerm = termSP.getSelectedItem().toString();
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                termSP.setSelection(0);
                mTerm = termSP.getSelectedItem().toString();
            }
        });
        startDate = mStart;
        startCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startDate = (month + 1) + "/" + dayOfMonth + "/" + year;

            }
        });
        endDate = mEnd;
        endCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                endDate = (month + 1) + "/" + dayOfMonth + "/" + year;

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (!title.getText().toString().equals(mTitle)){
                            values.put(CoursesContract.Columns.COURSE_TITLE, title.getText().toString());
                        }
                        if (!note.getText().toString().equals(mNote)){
                            values.put(CoursesContract.Columns.COURSE_NOTE, note.getText().toString());
                        }

                        if (!startDate.equals(mStart)){
                            values.put(CoursesContract.Columns.COURSE_START, startDate);
                        }
                        if (!endDate.equals(mEnd)){
                            values.put(CoursesContract.Columns.COURSE_END, endDate);
                        }
                        if (!mentorName.getText().toString().equals(mMentName)){
                            values.put(CoursesContract.Columns.COURSE_MENTOR_NAME, mentorName.getText().toString());
                        }
                        if (!mentorEmail.getText().toString().equals(mMentEmail)){
                            values.put(CoursesContract.Columns.COURSE_MENTOR_EMAIL, mentorEmail.getText().toString());
                        }
                        if (!mentorPhone.getText().toString().equals(mMentPhone)){
                            values.put(CoursesContract.Columns.COURSE_MENTOR_PHONE, mentorPhone.getText().toString());
                        }
                        if (!termSP.getSelectedItem().toString().equals(mTerm)){
                            values.put(CoursesContract.Columns.COURSE_TERM, termSP.getSelectedItem().toString());
                        }
                        values.put(CoursesContract.Columns.COURSE_STATUS, mStatus);


                        if (values.size() !=0 ){
                            contentResolver.update(CoursesContract.buildCourseURI(courseId), values, null, null);
                            saveGoToCoursesActivity(v);
                        }
                        break;
                    case ADD:
                        if (title.length() > 0){
                            values.put(CoursesContract.Columns.COURSE_TITLE, title.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_NOTE, note.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_START, startDate);
                            values.put(CoursesContract.Columns.COURSE_END, endDate);
                            values.put(CoursesContract.Columns.COURSE_MENTOR_NAME, mentorName.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_MENTOR_PHONE, mentorPhone.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_MENTOR_EMAIL, mentorEmail.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_STATUS, mStatus);
                            values.put(CoursesContract.Columns.COURSE_TERM, mTerm);
                            contentResolver.insert(CoursesContract.CONTENT_URI, values);
                            saveGoToCoursesActivity(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Course Title cannot be empty", Toast.LENGTH_SHORT).show();
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

    public void saveGoToCoursesActivity (View v) {
        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddcourseActivity.this, CoursesActivity.class));
    }
    public void cancelBackToCoursesActivity (View v){
        Toast.makeText(getApplicationContext(), "Process Cancelled", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddcourseActivity.this, CoursesActivity.class));
    }

    private void getBooleanIntent(){
        if (getIntent().hasExtra("bEdit")){
            bEdit = true;
        }
    }
    //gets intent from LayoutCourseitemActivity
    private void getIncomingIntent(){

        String stringId = getIntent().getStringExtra("course_id");
        courseId = Long.parseLong(stringId);

        if (getIntent().hasExtra("title")){
            mTitle = getIntent().getStringExtra("title");
            title.setText(mTitle);
        }
        if (getIntent().hasExtra("start")){
            mStart = getIntent().getStringExtra("start");

        }
        if (getIntent().hasExtra("end")){
            mEnd = getIntent().getStringExtra("end");
        }
        if (getIntent().hasExtra("status")){
            mStatus = getIntent().getStringExtra("status");
        }
        if (getIntent().hasExtra("note")){
            mNote = getIntent().getStringExtra("note");
            note.setText(mNote);
        }
        if (getIntent().hasExtra("mentname")){
            mMentName = getIntent().getStringExtra("mentname");
            mentorName.setText(mMentName);
        }
        if (getIntent().hasExtra("mentemail")){
            mMentEmail = getIntent().getStringExtra("mentemail");
            mentorEmail.setText(mMentEmail);
        }
        if (getIntent().hasExtra("mentphone")){
            mMentPhone = getIntent().getStringExtra("mentphone");
            mentorPhone.setText(mMentPhone);
        }
        if (getIntent().hasExtra("term")){
            mTerm = getIntent().getStringExtra("term");
        }
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

    private void getTermSelection(){
        term1 = "Term 1";
        term2 = "Term 2";
        term3 = "Term 3";
        term4 = "Term 4";
        if (term1.equals(mTerm)) {
            termSP.setSelection(0);

        } else if (term2.equals(mTerm)) {
            termSP.setSelection(1);

        } else if (term3.equals(mTerm)) {
            termSP.setSelection(2);

        } else if (term4.equals(mTerm)) {
            termSP.setSelection(3);

        } else {
            termSP.setSelection(0);
        }

    }


}
