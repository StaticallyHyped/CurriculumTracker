package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.curriculumtracker.R.id.layout_courseitem_Btnedit;

public class LayoutCourseitemActivity extends AppCompatActivity {

    public static final String TAG = "LayoutCourseItemActivity";
    private ConstraintLayout layout;
    private ImageButton edit;
    private ImageButton delete;
    private TextView start;
    private TextView end;
    private TextView note;
    private TextView title;
    private TextView mentName;
    private TextView mentEmail;
    private TextView mentPhone;
    private TextView status;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_courseitem);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.layout_courseitem_CL);
        edit = findViewById(layout_courseitem_Btnedit);
        delete = findViewById(R.id.layout_courseitem_Btndelete);
        title = findViewById(R.id.layout_courseitem_title);
        start = findViewById(R.id.layout_courseitem_tvstart);
        end = findViewById(R.id.layout_courseitem_tvend);
        note = findViewById(R.id.layout_courseitem_notecontentTV);
        mentName = findViewById(R.id.layout_courseitem_mentorname);
        mentPhone = findViewById(R.id.layout_courseitem_mentorphone);
        mentEmail = findViewById(R.id.layout_courseitem_mentoremail);
        status = findViewById(R.id.layout_courseitem_status);
        getIncomingIntent();
    }

    private void getIncomingIntent() {

        courseId = getIntent().getStringExtra("course_id");

        if (getIntent().hasExtra("course_title")){
            String courseTitle = getIntent().getStringExtra("course_title");
            title.setText(courseTitle);
        }
        if (getIntent().hasExtra("course_start")) {
            String courseStart = getIntent().getStringExtra("course_start");
            start.setText(courseStart);
        }
        if (getIntent().hasExtra("course_end")){
            String courseEnd = getIntent().getStringExtra("course_end");
            end.setText(courseEnd);
        }
        if (getIntent().hasExtra("course_note")) {
            String courseNote = getIntent().getStringExtra("course_note");
            note.setText(courseNote);
        }
        if (getIntent().hasExtra("course_mentname")) {
            String mntName = getIntent().getStringExtra("course_mentname");
            mentName.setText(mntName);
        }
        if (getIntent().hasExtra("course_mentemail")) {
            String mntEmail = getIntent().getStringExtra("course_mentemail");
            mentEmail.setText(mntEmail);
        }
        if (getIntent().hasExtra("course_mentphone")) {
            String mntPhone = getIntent().getStringExtra("course_mentphone");
            mentPhone.setText(mntPhone);
        }
        if (getIntent().hasExtra("course_status")) {
            String status = getIntent().getStringExtra("course_status");
            this.status.setText(status);
        }
    }

    public void goToEditCourse (View v) {
        Intent intent = new Intent(this, AddcourseActivity.class);
        boolean edit = true;
        intent.putExtra("bEdit", edit);
        intent.putExtra("course_id", courseId);
        intent.putExtra("title", title.getText());
        intent.putExtra("start", start.getText());
        intent.putExtra("end", end.getText());
        intent.putExtra("note", note.getText());
        intent.putExtra("mentname", mentName.getText());
        intent.putExtra("mentemail", mentEmail.getText());
        intent.putExtra("mentphone", mentPhone.getText());
        intent.putExtra("status", status.getText());
        startActivity(intent);

    }

    public void backToCourseList(View v) {
        startActivity(new Intent(LayoutCourseitemActivity.this, CoursesActivity.class));
    }

    public void deleteCourseItem(View v) {
        final long delCourseId = Long.parseLong(courseId);
        Context context = v.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        getContentResolver().delete(CoursesContract.buildCourseURI(delCourseId), null, null);
        backToCourseList(v);
    }

}
