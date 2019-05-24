package com.example.curriculumtracker;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddAsmtActivity extends AppCompatActivity {

    public static final String TAG = "AddAsmtActivity";

    private enum AddOrEdit {EDIT, ADD}
    private AddOrEdit mMode;
    private EditText titleTv;
    private RadioGroup rbGroup;
    private RadioButton rbOA;
    private RadioButton rbPA;
    private Button save, cancel;
    private CalendarView cal;
    private long asmtId;
    private String mTitle, mDate, mType, addTitle, date, type, mCourse;
    private Boolean bEdit;
    private TextView staticCourseTV;
    private Spinner courseSP;
    private String mCourseList;
    public Cursor mCursor;
    public ArrayList<String> courseArrayList = new ArrayList<>();

    public AddAsmtActivity(){
        //zero arg constructor, takes no parameter

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addasmt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Curriculum Tracker");

        titleTv = findViewById(R.id.content_add_asmtText);
        cal = findViewById(R.id.content_add_asmtCV);
        rbGroup = findViewById(R.id.content_add_asmt_RG);
        save = findViewById(R.id.content_add_asmt_savebtn);
        cancel = findViewById(R.id.content_add_asmt_cancelbtn);
        rbGroup = findViewById(R.id.content_add_asmt_RG);
        rbOA = findViewById(R.id.content_add_asmt_rbOA);
        rbPA = findViewById(R.id.content_add_asmt_rbPA);
        courseSP = findViewById(R.id.content_addsmt_courseSP);
        bEdit = false;

        ArrayAdapter<String> courseALAdapter = new ArrayAdapter<>(AddAsmtActivity.this, android.R.layout.simple_spinner_dropdown_item, courseArrayList);
        courseSP.setAdapter(courseALAdapter);

        getCourseList();

        courseALAdapter.notifyDataSetChanged();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date today = Calendar.getInstance().getTime();
        String setDefaultDate = df.format(today);


        getBooleanIntent();
        if (bEdit){
            mMode = AddOrEdit.EDIT;
            getIncomingIntent();
            setDate();
            toolbar.setTitle("Edit an Assessment");
            mCourseList = mCourse;

        } else {
            mMode = AddOrEdit.ADD;
            //courseSP.setSelection(1);
            rbPA.setChecked(true);
            toolbar.setTitle("Add an Assessment");
            type = "PA";
            courseSP.setSelection(0, true);
            mCourseList = courseSP.getSelectedItem().toString();
            date = setDefaultDate;
        }
        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.content_add_asmt_rbOA:
                        type = "OA";
                        break;
                    case R.id.content_add_asmt_rbPA:
                        type = "PA";
                        break;
                        default:
                            throw new IllegalArgumentException("unable to find asmt type");
                }
            }
        });
        courseSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCourseList = courseSP.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month + 1) + "/" + dayOfMonth + "/" + year;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues values = new ContentValues();

                switch (mMode){
                    case EDIT:
                        if (!titleTv.getText().toString().equals(mTitle)) {
                            values.put(AssessmentsContract.Columns.ASMTS_TITLE, titleTv.getText().toString());
                        }
                        if (!date.equals(mDate)){
                            values.put(AssessmentsContract.Columns.ASMTS_DATE, date);
                        }
                        if (!type.equals(mType)){
                            values.put(AssessmentsContract.Columns.ASMTS_TYPE, type);
                        }
                        if (!mCourse.equals(mCourseList)){
                            values.put(AssessmentsContract.Columns.ASMTS_COURSE, mCourseList);
                        }
                        if (values.size() != 0){
                            contentResolver.update(AssessmentsContract.buildAssessmentsURI(asmtId), values, null, null);
                            saveGoToAsmtsActivity(v);
                        }
                        break;

                    case ADD:
                        addTitle = titleTv.getText().toString();
                        if (addTitle.length() > 0) {
                            values.put(AssessmentsContract.Columns.ASMTS_TITLE, addTitle);
                            values.put(AssessmentsContract.Columns.ASMTS_DATE, date);
                            values.put(AssessmentsContract.Columns.ASMTS_TYPE, type);
                            values.put(AssessmentsContract.Columns.ASMTS_COURSE, mCourseList);
                            contentResolver.insert(AssessmentsContract.CONTENT_URI, values);
                            saveGoToAsmtsActivity(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        break;
                        default:
                            throw new IllegalArgumentException("neither ADD nor EDIT");
                }
            }
        });
    }

    //inflating the options menu
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
    //get intent from LayoutAsmtItemActivity
    private void getIncomingIntent(){
        String stringId = getIntent().getStringExtra("asmt_id");
        asmtId = Long.parseLong(stringId);

        if (getIntent().hasExtra("title")){
            mTitle = getIntent().getStringExtra("title");
            titleTv.setText(mTitle);
        }
        if (getIntent().hasExtra("asmt_date")){
            mDate = getIntent().getStringExtra("asmt_date");
        } else {
            mDate = "5/30/2019";
        }
        if (getIntent().hasExtra("type")){
            mType = getIntent().getStringExtra("type");
            if (mType.equals("PA")){
                type = mType;
                rbPA.setChecked(true);
            }else {
                type = mType;
                rbOA.setChecked(true);
            }
        } else {
            mType = "OA";
        }
        if (getIntent().hasExtra("asmt_course")){
            mCourse = getIntent().getStringExtra("asmt_course");
            int position = 0;
            for (String s : courseArrayList)
                if (s.equals(mCourse)) {
                    position = courseArrayList.indexOf(mCourse);
                }
            courseSP.setSelection(position);

        } else {
            courseSP.setSelection(0);
        }
    }

    private void getBooleanIntent(){
        if (getIntent().hasExtra("bEdit")){
            bEdit = true;
        }
    }
    public void saveGoToAsmtsActivity(View v){
        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddAsmtActivity.this, AssessmentsActivity.class));
    }

    public void cancelBackToAsmtsActivity(View v) {
        Toast.makeText(getApplicationContext(),"Process Cancelled", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddAsmtActivity.this, AssessmentsActivity.class));
    }

    public void setDate (){
        String date = mDate;
        String parts[] = date.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.YEAR, year);
        long timeInMillis = calendar.getTimeInMillis();
        cal.setDate(timeInMillis);
    }

    public void getCourseList(){
        int position = 0;
        String query = "SELECT Title FROM Courses;";
        AppDatabase db = AppDatabase.getInstance(this);

        mCursor = db.getReadableDatabase().rawQuery(query, null);
        int id[] = new int[mCursor.getCount()];
        if (mCursor.getCount() > 0 ){
            mCursor.moveToFirst();
            do{
                id[position] = mCursor.getInt(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE));
                courseArrayList.add(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
                position ++;
            } while (mCursor.moveToNext());
            mCursor.close();
        }

    }

    public int getItemCount() {
        if ((mCursor == null) || (mCursor.getCount()==0)){
            return 1;

        } else {
            Log.d(TAG, "getItemCount: mCursor count" + mCursor.getCount());
            return mCursor.getCount();
        }
    }

}
