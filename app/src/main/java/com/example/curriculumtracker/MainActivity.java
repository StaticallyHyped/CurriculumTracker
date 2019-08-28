package com.example.curriculumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnGoToTermlist = findViewById(R.id.main_buttonTermList);
        Button btnGoToAsmtList = findViewById(R.id.main_buttonAsmntList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(this, SetCourseAlertActivity.class);
        switch (item.getItemId()){
            case R.id.alertCourseStart:
                intent.putExtra("start_alert", "start");
                startActivity(intent);
                break;
            case R.id.alertCourseEnd:
                intent.putExtra("start_alert", "end");
                startActivity(intent);
                break;
            case R.id.alertAsmtDue:
                startActivity(new Intent(this, SetAsmtAlertActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToTermlist(View v) {

        Intent intent = new Intent(MainActivity.this, TermsActivity.class);

        startActivity(intent);

    }

    public void goToAsmtList(View v) {

        startActivity(new Intent(MainActivity.this, AssessmentsActivity.class));
    }

    public void goToCourseList (View v) {
        startActivity(new Intent(MainActivity.this, CoursesActivity.class));
    }


}
