package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView term;
    private String courseId;
    private String mCourseTerm;
    public Cursor mCursor;
    public ArrayList<String> courseArrayList = new ArrayList<>();
    private String query, looper, testIntent, courseTitle, secondMentName,
            secondMentPhone, secondMentEmail;
    private TextView secondMentNameTV, secondMentPhoneTV, secondMentEmailTV;
    private boolean checkAsmtCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_courseitem);
        /*Toolbar toolbar = findViewById(R.id.layout_courseitem_toolbar);
        setSupportActionBar(toolbar);*/

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
        term = findViewById(R.id.layout_courseitem_termTV);
        secondMentNameTV = findViewById(R.id.layout_courseitem_mentName2);
        secondMentPhoneTV = findViewById(R.id.layout_courseitem_mentPhone2);
        secondMentEmailTV = findViewById(R.id.layout_courseitem_mentEmail2);

        getIncomingIntent();

    }


    // gets intent from CoursesCRVAdapter
    private void getIncomingIntent() {
        testIntent = getIntent().getStringExtra("start_dialog");
        courseId = getIntent().getStringExtra("course_id");

        if (getIntent().hasExtra("course_title")){
            courseTitle = getIntent().getStringExtra("course_title");
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
        if (getIntent().hasExtra("course_term")) {
            String mTerm = getIntent().getStringExtra("course_term");
            term.setText(mTerm);
        }
        if (getIntent().hasExtra("term_title")){
            String mTermTitle = getIntent().getStringExtra("term_title");
            Log.d(TAG, "getIncomingIntent: TERM TITLE PASSED" + mTermTitle);
        }
        if (getIntent().hasExtra("course_second_mentname")){
            secondMentName = getIntent().getStringExtra("course_second_mentname");
            Log.d(TAG, "getIncomingIntent: secondmentnname" + secondMentName);
            secondMentNameTV.setText(secondMentName);
        }
        if (getIntent().hasExtra("course_second_mentphone")){
            secondMentPhone = getIntent().getStringExtra("course_second_mentphone");
            Log.d(TAG, "getIncomingIntent: secondmentphone" + secondMentPhone);
            secondMentPhoneTV.setText(secondMentPhone);
        }
        if (getIntent().hasExtra("course_second_mentemail")){
            secondMentEmail = getIntent().getStringExtra("course_second_mentemail");
            Log.d(TAG, "getIncomingIntent: secondmentemail" + secondMentEmail);
            secondMentEmailTV.setText(secondMentEmail);
        }
    }
//  sends intent to AddcourseActivity
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
        intent.putExtra("term", term.getText());
        intent.putExtra("second_mentname", secondMentNameTV.getText());
        intent.putExtra("second_mentphone", secondMentPhoneTV.getText());
        intent.putExtra("second_mentemail", secondMentEmailTV.getText());
        startActivity(intent);

    }


    public void backToCourseList(View v) {
        startActivity(new Intent(LayoutCourseitemActivity.this, CoursesActivity.class));
    }

    public void deleteCourseItem(View v) {

        final long delCourseId = Long.parseLong(courseId);
        Context context = v.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        getCourseList();
        if (!checkAsmtCourse){
            getContentResolver().delete(CoursesContract.buildCourseURI(delCourseId), null, null);
        } else {
            Toast.makeText(this, "This course contains one or more assessments.\n" +
                    "Please delete the associated assessments before proceeding", Toast.LENGTH_LONG).show();
        }
        backToCourseList(v);
    }

    public void getCourseList(){
        query = "SELECT Course FROM Assessments;";
        AppDatabase db = AppDatabase.getInstance(this);
        mCursor = db.getReadableDatabase().rawQuery(query, null);
        if (mCursor.getCount() > 0 ){
            mCursor.moveToFirst();
            do{
                looper = mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_COURSE));
                if (looper.equals(courseTitle)){
                    checkAsmtCourse = true;
                    break;
                } else {
                    checkAsmtCourse = false;
                }

            } while (mCursor.moveToNext());
            mCursor.close();
        }

    }


}
