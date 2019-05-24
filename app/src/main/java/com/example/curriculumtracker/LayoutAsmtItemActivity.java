package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;


public class LayoutAsmtItemActivity extends AppCompatActivity {

    public static final String TAG = "LayoutAsmtItemActivity";
    private TextView dueDate, type, title, staticType, staticDate, staticCourseTV, courseTv;
    private ImageButton editBtn, deleteBtn;
    private String asmtId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_assessmentitem);
        /*Toolbar toolbar = findViewById(R.id.layout_asmtitem_toolbar);
        setSupportActionBar(toolbar);*/

        dueDate = findViewById(R.id.layout_assessmentitem_dateTV);
        title = findViewById(R.id.layout_asmntitem_title);
        type = findViewById(R.id.layout_assesmentitem_typeTV);
        staticDate = findViewById(R.id.layout_asmntitem_date);
        staticType = findViewById(R.id.layout_asmntitem_type);
        editBtn = findViewById(R.id.layout_asmntitem_buttonedit);
        deleteBtn = findViewById(R.id.layout_asmntitem_buttondelete);
        staticDate.setText("Assessment Date");
        staticType.setText("Assessment Type");
        courseTv = findViewById(R.id.layout_asmtitem_courseTV);

        getIncomingIntent();
    }


    //gets intent from AmtsCRVAdapter, sets text values for layout items
    public void getIncomingIntent (){

        asmtId = getIntent().getStringExtra("asmt_id");

        if (getIntent().hasExtra("asmt_title")){
            String asmtTitle = getIntent().getStringExtra("asmt_title");
            title.setText(asmtTitle);
        }
        if (getIntent().hasExtra("asmt_date")){
            String asmtDate = getIntent().getStringExtra("asmt_date");
            dueDate.setText(asmtDate);
        }
        if (getIntent().hasExtra("asmt_type")){
            String asmtType = getIntent().getStringExtra("asmt_type");
            type.setText(asmtType);
        }
        if (getIntent().hasExtra("asmt_course")){
            String asmtCourse = getIntent().getStringExtra("asmt_course");
            courseTv.setText(asmtCourse);
            Log.d(TAG, "getIncomingIntent: IN LAYOUT, asmtCourse" + asmtCourse);
        }

    }

    //Creates intent for AddAsmtActivity, to be used when EDITING an assessment
    public void goToEditAssessment(View view){
        Intent intent = new Intent(this, AddAsmtActivity.class);
        boolean edit = true;

        intent.putExtra("bEdit", edit);
        intent.putExtra("asmt_id", asmtId);
        intent.putExtra("title", title.getText());
        intent.putExtra("asmt_date", dueDate.getText());
        intent.putExtra("type", type.getText());
        intent.putExtra("asmt_course", courseTv.getText());


        startActivity(intent);    }

    public void backToAssessmentsList (View view) {
        startActivity(new Intent(LayoutAsmtItemActivity.this, AssessmentsActivity.class));
    }

    public void deleteAsmtItem(View v) {
        Log.d(TAG, "onClick: asmtId string" + asmtId);
        final long delAsmtId = Long.parseLong(asmtId);

        Context context = v.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        getContentResolver().delete(AssessmentsContract.buildAssessmentsURI(delAsmtId), null, null);

        backToAssessmentsList(v);
    }

    public void shareAsmtItem(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Title: " + title.getText());
        intent.putExtra(Intent.EXTRA_TEXT, ("Due Date: " + dueDate.getText()) + "\n" +
        "Type of Assessment: " + type.getText() + "\n" + "For Course: " + courseTv.getText());

        startActivity(Intent.createChooser(intent, "Share using"));
    }




}
