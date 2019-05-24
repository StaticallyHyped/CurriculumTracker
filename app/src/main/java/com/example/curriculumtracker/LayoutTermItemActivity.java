package com.example.curriculumtracker;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LayoutTermItemActivity extends AppCompatActivity {

    private TextView start, end, title;
    private ImageButton edit, delete;
    private String termId;
    private Cursor mCursor;
    private String query;
    private String termTitle;
    private ArrayList<String> courseArrayList = new ArrayList<>();
    private boolean checkCourseTerm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_termitem);

        /*Toolbar toolbar = findViewById(R.id.layout_termitem_toolbar);
        setSupportActionBar(toolbar);*/

        title = findViewById(R.id.layout_termitem_title);
        start = findViewById(R.id.layout_termitem_tvstart);
        end = findViewById(R.id.layout_termitem_tvend);
        edit = findViewById(R.id.layout_termitem_buttonedit);
        delete = findViewById(R.id.layout_termitem_buttondelete);

        getIncomingIntent();

    }


    //gets intent from TermsCRVAdapter, sets text values for layout items
    private void getIncomingIntent(){
        termId = getIntent().getStringExtra("term_id");

        if (getIntent().hasExtra("term_title")){
            termTitle = getIntent().getStringExtra("term_title");
            title.setText(termTitle);
        }
        if (getIntent().hasExtra("term_start")){
            String termStart = getIntent().getStringExtra("term_start");
            start.setText(termStart);
        }
        if (getIntent().hasExtra("term_end")) {
            String termEnd = getIntent().getStringExtra("term_end");
            end.setText(termEnd);
        }
    }
    //creates intent to populate fields in Add/Edit terms activity
    public void goToEditTerm(View v){
        Intent intent = new Intent(this, AddtermActivity.class);
        boolean edit = true;

        intent.putExtra("bEdit", edit);
        intent.putExtra("term_id", termId);
        intent.putExtra("title", title.getText());
        intent.putExtra("start", start.getText());
        intent.putExtra("end", end.getText());

        startActivity(intent);

    }


    public void deleteTermItem(View v) {
        final long delTermId = Long.parseLong(termId);
        Context context = v.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        getCourseList();
        if (!checkCourseTerm) {
            getContentResolver().delete(TermsContract.buildTermURI(delTermId), null, null);
            startActivity(new Intent(this, TermsActivity.class));
        } else {
            Toast.makeText(this, "This Term contains one or more courses.\n" +
                    "Please delete the associated courses before proceeding.", Toast.LENGTH_LONG).show();
        }

    }

    public void getCourseList(){
        query = "SELECT Term FROM Courses;";
        AppDatabase db = AppDatabase.getInstance(this);
        String looper;

        mCursor = db.getReadableDatabase().rawQuery(query, null);
//        int id[] = new int[mCursor.getCount()];
        if (mCursor.getCount() > 0 ){
            mCursor.moveToFirst();
            do{
//                id[position] = mCursor.getInt(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE));
                looper = mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TERM));
                if (looper.equals(termTitle)){
                    checkCourseTerm = true;
                    break;
                } else {
                    checkCourseTerm = false;
                }

            } while (mCursor.moveToNext());
            mCursor.close();
        }
    }


}
