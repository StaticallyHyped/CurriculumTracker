package com.example.curriculumtracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;



public class AppProvider extends ContentProvider {

     public static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final String CONTENT_AUTHORITY = "com.example.curriculumtracker.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int TERMS = 100;
    public static final int TERMS_ID = 101;

    public static final int ASMTS = 200;
    public static final int ASMTS_ID = 201;

    public static final int COURSES = 300;
    public static final int COURSES_ID = 301;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, TermsContract.TERMS_TABLE_NAME, TERMS);
        matcher.addURI(CONTENT_AUTHORITY, TermsContract.TERMS_TABLE_NAME + "/#", TERMS_ID);

        matcher.addURI(CONTENT_AUTHORITY, AssessmentsContract.ASMTS_TABLE_NAME, ASMTS);
        matcher.addURI(CONTENT_AUTHORITY, AssessmentsContract.ASMTS_TABLE_NAME + "/#", ASMTS_ID);

        matcher.addURI(CONTENT_AUTHORITY, CoursesContract.COURSES_TABLE_NAME, COURSES);
        matcher.addURI(CONTENT_AUTHORITY, CoursesContract.COURSES_TABLE_NAME + "/#", COURSES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);

        final int match = sUriMatcher.match(uri);

        Log.d(TAG, "query: match is: " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        //using the switch statement to choose different blocks of code, depending on the result of the uri
        //when the uri matches times or tasks or durations, the table is chosen which to query from
        switch(match) {
            case TERMS:
            queryBuilder.setTables(TermsContract.TERMS_TABLE_NAME);
            break;

            case TERMS_ID:
                queryBuilder.setTables(TermsContract.TERMS_TABLE_NAME);
                long termId = TermsContract.getTermId(uri);
                queryBuilder.appendWhere(TermsContract.Columns._ID + " = " + termId);
                break;

            case COURSES:
                queryBuilder.setTables(CoursesContract.COURSES_TABLE_NAME);
                break;

            case COURSES_ID:
                queryBuilder.setTables(CoursesContract.COURSES_TABLE_NAME);
                long courseId = CoursesContract.getCourseId(uri);
                queryBuilder.appendWhere(CoursesContract.Columns._ID + " = " + courseId);
                break;

            case ASMTS:
                queryBuilder.setTables(AssessmentsContract.ASMTS_TABLE_NAME);
                break;

            case ASMTS_ID:
                queryBuilder.setTables(AssessmentsContract.ASMTS_TABLE_NAME);
                long assessmentsId = AssessmentsContract.getAssessmentsId(uri);
                queryBuilder.appendWhere(AssessmentsContract.Columns._ID + " = " + assessmentsId);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TERMS:
                return TermsContract.CONTENT_TYPE;
            case TERMS_ID:
                return TermsContract.CONTENT_ITEM_TYPE;
            case COURSES:
                return CoursesContract.CONTENT_TYPE;
            case COURSES_ID:
                return CoursesContract.CONTENT_ITEM_TYPE;
            case ASMTS:
                return AssessmentsContract.CONTENT_TYPE;
            case ASMTS_ID:
                return AssessmentsContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "entering insert, called with uri: " + uri);
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch(match) {
            case TERMS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TermsContract.TERMS_TABLE_NAME, null, values);
                if(recordId >= 0) {
                    returnUri = TermsContract.buildTermURI(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri);
        }
        if (recordId > 0){
            getContext().getContentResolver().notifyChange(uri, null); //updates Resolver if changes occur in db
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "entering update, called with uri: " + uri);
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case TERMS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TermsContract.TERMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case TERMS_ID:
                db = mOpenHelper.getWritableDatabase();
                long termId = TermsContract.getTermId(uri);
                selectionCriteria = TermsContract.Columns._ID + " = " + termId;

                if((selection != null) && (selection.length() > 0)){
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(TermsContract.TERMS_TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Uknown uri: " + uri);
        }
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri, null); //updates Resolver if changes occur in db
        }
        return count;
    }
}
