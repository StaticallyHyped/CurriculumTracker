package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

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

    private String mTitle, mStart, mEnd, mStatus, mNote, mMentName, mMentPhone, mMentEmail;


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
        bEdit = false;

        String [] statusList = new String[]{
        "Not Started", "In Progress", "Complete"
        };

        ArrayAdapter<String> spinnerAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusList);
        spinnerAA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSP.setAdapter(spinnerAA);

        getBooleanIntent();

        if (bEdit){
            Log.d(TAG, "onCreate: EDIT MODE");
            mMode = AddOrEdit.EDIT;
            getIncomingIntent();
            setStartCal();
            setEndCal();

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
            statusSP.setSelection(0);
        }

        statusSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mStatus = statusSP.getSelectedItem().toString();
                        break;
                    case 1:
                        mStatus = statusSP.getSelectedItem().toString();
                        break;
                    case 2:
                        mStatus = statusSP.getSelectedItem().toString();
                        break;
                    default:
                        throw new IllegalArgumentException("did not successfully retrieve status spinner info");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                startDate = (month + 1) + "/" + dayOfMonth + "/" + year;

            }
        });
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
                        values.put(CoursesContract.Columns.COURSE_STATUS, mStatus);


                        //TODO add status spinner if block here
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
                            values.put(CoursesContract.Columns.COURSE_MENTOR_EMAIL, mentorPhone.getText().toString());
                            values.put(CoursesContract.Columns.COURSE_STATUS, mStatus);
                            //TODO add status spinner values.put
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
