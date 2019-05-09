package com.example.curriculumtracker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY_URI;
import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY;

public class TermsContract {

    static final String TERMS_TABLE_NAME = "Terms";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TERMS_TITLE = "Title";
        public static final String TERMS_STARTDATE = "Start_Date";
        public static final String TERMS_ENDDATE = "End_Date";



        private Columns() {
            //private constructor to prevent instantiation
        }
    }
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TERMS_TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TERMS_TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TERMS_TABLE_NAME;

    static Uri buildTermURI (long termId) {
        return ContentUris.withAppendedId(CONTENT_URI, termId);
    }
    static long getTermId(Uri uri){
        return ContentUris.parseId(uri);
    }
}
