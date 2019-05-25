package com.example.curriculumtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.security.InvalidParameterException;

public class CoursesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        CoursesCRVAdapter.OnCourseListener {

    public static final String TAG = "CoursesActivity";
    public static final int LOADER_ID = 0;
    private CoursesCRVAdapter mAdapter;
    public Button goToAddCourseBtn;

    public CoursesActivity(){
        //empty constructor

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.activity_courses_toolbar);
        setSupportActionBar(toolbar);
        goToAddCourseBtn = findViewById(R.id.activity_courses_btnadd);

        RecyclerView recyclerView = findViewById(R.id.activity_coursesRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CoursesCRVAdapter(null, this);
        recyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

    }
    public void goToAddCourse (View v) {
        startActivity(new Intent(CoursesActivity.this, AddcourseActivity.class));

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(TAG, "onCreateLoader: start, id = " + id);
        String [] projection = {CoursesContract.Columns._ID, CoursesContract.Columns.COURSE_TITLE,
        CoursesContract.Columns.COURSE_START, CoursesContract.Columns.COURSE_END, CoursesContract.Columns.COURSE_STATUS,
                CoursesContract.Columns.COURSE_NOTE, CoursesContract.Columns.COURSE_TERM, CoursesContract.Columns.COURSE_MENTOR_NAME,
        CoursesContract.Columns.COURSE_MENTOR_PHONE, CoursesContract.Columns.COURSE_MENTOR_EMAIL, CoursesContract.Columns.COURSE_SECOND_MENTOR_NAME,
                CoursesContract.Columns.COURSE_SECOND_MENTOR_PHONE, CoursesContract.Columns.COURSE_SECOND_MENTOR_EMAIL};

        switch (id){
            case LOADER_ID:
                Log.d(TAG, "onCreateLoader: inside the createloader method with loader id" + LOADER_ID);
                return new CursorLoader(this,
                CoursesContract.CONTENT_URI,
                projection,
                null,
                null,
                null);

            default:
                    throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id");
        }
    }

    private void courseDetailViewRequest(Course course) {

    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: start");
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();
    }

    public void onLoaderReset(Loader<Cursor> loader){
        mAdapter.swapCursor(null);
    }

    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(this, LayoutCourseitemActivity.class);
        //intent.putExtra("selected_course", mAdapter.getmCursor(position));


    }
}
