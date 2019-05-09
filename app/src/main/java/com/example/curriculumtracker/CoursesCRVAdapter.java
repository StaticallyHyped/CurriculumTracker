package com.example.curriculumtracker;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class CoursesCRVAdapter extends RecyclerView.Adapter<CoursesCRVAdapter.CourseViewHolder> {

    public static final String TAG = "CoursesCRVAdapter";

    private Cursor mCursor;

    public CoursesCRVAdapter(Cursor cursor){
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: starts");

        // make sure to use the LAYOUT for the item here, not the activity for the recyclerview
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_courseitem, viewGroup, false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");
        //System.out.println("this is mCursor: " + mCursor.getCount());
        if((mCursor == null) || (mCursor.getCount() == 0)){
            Log.d(TAG, "onBindViewHolder: mCursor is NULL");
            holder.title.setText("Course Title");
            //holder.startTitle.setText("DO NOT START");

        } else {
            Log.d(TAG, "onBindViewHolder: inside else statement");
            if(!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("couldn't move cursor into position: " + position);
            }
            System.out.println("ON BIND VIEW HOLDER" + CoursesContract.Columns.COURSE_START);

            holder.endText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_START)));
            holder.startText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_END)));
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
            holder.note.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_NOTE)));
            //holder.statusSpinner TODO set up code to fill in spinner values using table
        }
        Log.d(TAG, "onBindViewHolder: exits");
    }

    @Override
    public int getItemCount() { //This is actually really important. It returns how many items are returned in the
        //recyclerview. If you leave the default to 0, you will not get any info
        Log.d(TAG, "getItemCount: starts");
        if((mCursor == null) || (mCursor.getCount() == 0)){
            Log.d(TAG, "getItemCount: inside get item count: Item Count == 0 or null");
            return 1;
        } else {
            Log.d(TAG, "getItemCount: in else: " + mCursor.getCount());
            return mCursor.getCount();
        }

    }

    Cursor swapCursor(Cursor newCursor){
        Log.d(TAG, "swapCursor: starts");
        if (newCursor == mCursor){
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "CourseViewHolder (subclass in CoursesCRVAdapter";

        TextView title = null;
        TextView endTitle = null;
        TextView startTitle = null;
        TextView statusTitle = null;
        TextView endText = null;
        TextView startText = null;
        Spinner statusSpinner = null;
        ImageButton editBtn = null;
        ImageButton deleteBtn = null;
        TextInputEditText note = null;

        public CourseViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "CourseViewHolder: starts");

            this.title = itemView.findViewById(R.id.layout_courseitem_title);
            this.endTitle = itemView.findViewById(R.id.layout_courseitem_start);
            this.startTitle = itemView.findViewById(R.id.layout_courseitem_end);
            this.statusTitle = itemView.findViewById(R.id.layout_courseitem_status);
            this.endText = itemView.findViewById(R.id.layout_courseitem_tvend);
            this.startText = itemView.findViewById(R.id.layout_courseitem_tvstart);
            this.statusSpinner = itemView.findViewById(R.id.layout_courseitem_statusspinner);
            this.editBtn = itemView.findViewById(R.id.layout_courseitem_buttonedit);
            this.deleteBtn = itemView.findViewById(R.id.layout_courseitem_buttondelete);
            this.note = itemView.findViewById(R.id.layout_courseitem_noteTI);
        }

    }
}
