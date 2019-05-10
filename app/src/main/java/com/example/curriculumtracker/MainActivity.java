package com.example.curriculumtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnGoToTermlist = findViewById(R.id.main_buttonTermList);
        Button btnGoToAsmtList = findViewById(R.id.main_buttonAsmntList);


        /*AppDatabase appDatabase = AppDatabase.getInstance(this);
        final SQLiteDatabase db = appDatabase.getReadableDatabase();*/
       /* btnGoToTermlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermsActivity.class));
            }
        });*/

    }

    public void goToTermlist(View v) {

        Intent intent = new Intent(MainActivity.this, TermsActivity.class);
        //intent.putExtra("noteRV", "test");

        startActivity(intent);

    }

    public void goToAsmtList(View v) {
        startActivity(new Intent(MainActivity.this, AssessmentsActivity.class));
    }

    public void goToCourseList (View v) {
        startActivity(new Intent(MainActivity.this, CoursesActivity.class));
    }


}
