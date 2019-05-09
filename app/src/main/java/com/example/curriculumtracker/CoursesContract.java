package com.example.curriculumtracker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY;
import static com.example.curriculumtracker.AppProvider.CONTENT_AUTHORITY_URI;

public class CoursesContract {
    static final String COURSES_TABLE_NAME = "Courses";

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String COURSE_TITLE = "Title";
        public static final String COURSE_START = "Start";
        public static final String COURSE_END = "Complete";
        public static final String COURSE_STATUS = "Status";
        public static final String COURSE_NOTE = "Note";
        public static final String COURSE_MENTOR_NAME = "MentorName";
        public static final String COURSE_MENTOR_PHONE = "MentorPhone";
        public static final String COURSE_MENTOR_EMAIL = "MentorEmail";

        private Columns() {
            //private constructor to prevent instantiation
        }
    }
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, COURSES_TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + COURSES_TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + COURSES_TABLE_NAME;

    static Uri buildCourseURI (long courseId) {
        return ContentUris.withAppendedId(CONTENT_URI, courseId);
    }
    static long getCourseId(Uri uri){
        return ContentUris.parseId(uri);
    }

}
