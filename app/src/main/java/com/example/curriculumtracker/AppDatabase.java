package com.example.curriculumtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppDatabase extends SQLiteOpenHelper {

    public static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "CurriculumTracker.db";
    public static final int DATABASE_VERSION = 1;

    // Implement AppDatabase using singleton design pattern. Create one instance

    private static AppDatabase instance = null;

    private AppDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor working");

    }

    static AppDatabase getInstance(Context context) {
        if(instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        Log.d(TAG, "onCreate: DOES THIS EVEN WORKKKKKK???????????");
        String termSQL;

        termSQL = "CREATE TABLE " + TermsContract.TERMS_TABLE_NAME + " ("
                + TermsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + TermsContract.Columns.TERMS_TITLE + " TEXT NOT NULL, "
                + TermsContract.Columns.TERMS_STARTDATE + " TEXT, "
                + TermsContract.Columns.TERMS_ENDDATE + " TEXT);";
        Log.d(TAG, termSQL);
        db.execSQL(termSQL);

        String asmtSQL;
        asmtSQL = "CREATE TABLE " + AssessmentsContract.ASMTS_TABLE_NAME + " ("
                + AssessmentsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + AssessmentsContract.Columns.ASMTS_TITLE + " TEXT NOT NULL, "
                + AssessmentsContract.Columns.ASMTS_DATE + " TEXT, "
                + AssessmentsContract.Columns.ASMTS_TYPE + " TEXT);";

        Log.d(TAG, asmtSQL);
        db.execSQL(asmtSQL);

        String courseSQL;
        courseSQL = "CREATE TABLE " + CoursesContract.COURSES_TABLE_NAME + " ("
                + CoursesContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + CoursesContract.Columns.COURSE_TITLE + " TEXT NOT NULL, "
                + CoursesContract.Columns.COURSE_START + " TEXT, "
                + CoursesContract.Columns.COURSE_END + " TEXT, "
                + CoursesContract.Columns.COURSE_STATUS + " TEXT, "
                + CoursesContract.Columns.COURSE_NOTE + " TEXT, "
                + CoursesContract.Columns.COURSE_MENTOR_NAME + " TEXT, "
                + CoursesContract.Columns.COURSE_MENTOR_PHONE + " TEXT, "
                + CoursesContract.Columns.COURSE_MENTOR_EMAIL + " TEXT);";


        Log.d(TAG, courseSQL);
        db.execSQL(courseSQL);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch(oldVersion) {
            case 1:
                // upgrade logic from version 1
                break;
                //add another case when upgrading to new db version
            default:
                throw new IllegalStateException("onUpgrade() with unknown new version");
        }
    }


}
