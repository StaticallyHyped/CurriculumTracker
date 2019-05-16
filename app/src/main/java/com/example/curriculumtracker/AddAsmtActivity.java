package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private String mTitle, mDate, mType, addTitle, date, type;
    private Boolean bEdit;

    public AddAsmtActivity(){
        Log.d(TAG, "AddAsmtActivity: constructor called");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addasmt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleTv = findViewById(R.id.content_add_asmtText);
        cal = findViewById(R.id.content_add_asmtCV);
        rbGroup = findViewById(R.id.content_add_asmt_RG);
        save = findViewById(R.id.content_add_asmt_savebtn);
        cancel = findViewById(R.id.content_add_asmt_cancelbtn);
        rbGroup = findViewById(R.id.content_add_asmt_RG);
        rbOA = findViewById(R.id.content_add_asmt_rbOA);
        rbPA = findViewById(R.id.content_add_asmt_rbPA);
        bEdit = false;

        getBooleanIntent();
        if (bEdit){
            mMode = AddOrEdit.EDIT;
            getIncomingIntent();
            setDate();

        } else {
            mMode = AddOrEdit.ADD;
            rbPA.setChecked(true);
            type = "PA";
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
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month + 1) + "/" + dayOfMonth + "/" + year;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
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
                            contentResolver.insert(AssessmentsContract.CONTENT_URI, values);
                            saveGoToAsmtsActivity(v);
                        } else {
                            Toast.makeText(getApplicationContext(), "Term Title cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        break;
                        default:
                            throw new IllegalArgumentException("neither ADD nor EDIT");
                }
            }
        });

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
            mDate = "5/16/2019";
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

}
