package com.example.curriculumtracker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY;
import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY_URI;

public class AssessmentsContract {

    static final String ASMTS_TABLE_NAME = "Assessments";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String ASMTS_TITLE = "Title";
        public static final String ASMTS_TYPE = "Type";
        public static final String ASMTS_DATE = "Date";



        private Columns() {
            //private constructor to prevent instantiation
        }
    }
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, ASMTS_TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + ASMTS_TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + ASMTS_TABLE_NAME;

    static Uri buildAssessmentsURI (long asmtsId) {
        return ContentUris.withAppendedId(CONTENT_URI, asmtsId);
    }
    static long getAssessmentsId(Uri uri){
        return ContentUris.parseId(uri);
    }
}
