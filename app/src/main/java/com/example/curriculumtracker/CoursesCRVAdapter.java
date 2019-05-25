package com.example.curriculumtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class CoursesCRVAdapter extends RecyclerView.Adapter<CoursesCRVAdapter.CourseViewHolder> {

    public static final String TAG = "CoursesCRVAdapter";
    private Context mContext;
    private Cursor mCursor;


    public CoursesCRVAdapter(Cursor cursor, Context context){
        this.mCursor = cursor;
        this.mContext = context;
    }
    
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: starts");

        // make sure to use the LAYOUT for the item here, not the activity for the recyclerview
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_courseitem, viewGroup, false);

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, final int position) {
        holder.note.setVisibility(View.GONE);
        holder.courseMentInfo.setVisibility(View.GONE);
        holder.mentEmail.setVisibility(View.GONE);
        holder.mentPhone.setVisibility(View.GONE);
        holder.mentName.setVisibility(View.GONE);
        holder.staticMentName.setVisibility(View.GONE);
        holder.staticMentPhone.setVisibility(View.GONE);
        holder.staticMentEmail.setVisibility(View.GONE);
        holder.staticNoteTitle.setVisibility(View.GONE);
        holder.editBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);
        holder.termTV.setVisibility(View.GONE);
        holder.staticTermTV.setVisibility(View.GONE);
        holder.staticSecondMentName.setVisibility(View.GONE);
        holder.staticSecondMentEmail.setVisibility(View.GONE);
        holder.staticSecondMentPhone.setVisibility(View.GONE);
        holder.secondMentName.setVisibility(View.GONE);
        holder.secondMentPhone.setVisibility(View.GONE);
        holder.secondMentEmail.setVisibility(View.GONE);
        holder.secondMentHeader.setVisibility(View.GONE);


        if((mCursor == null) || (mCursor.getCount() == 0)){
            Log.d(TAG, "onBindViewHolder: mCursor is NULL");
            //holder.startTitle.setText("This is only a test");
            holder.title.setVisibility(View.GONE);
            holder.startText.setVisibility(View.GONE);
            holder.endText.setVisibility(View.GONE);
            holder.endTitle.setVisibility(View.GONE);
            holder.startTitle.setVisibility(View.GONE);

        } else {
            Log.d(TAG, "onBindViewHolder: MCURSOR VALUES: " + mCursor.getCount());

            if(!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("couldn't move cursor into position: " + position);
            }
            holder.title.setVisibility(View.VISIBLE);
            holder.startText.setVisibility(View.VISIBLE);
            holder.endText.setVisibility(View.VISIBLE);
            holder.endTitle.setVisibility(View.VISIBLE);
            holder.startTitle.setVisibility(View.VISIBLE);

            holder.startText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_START)));
            holder.endText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_END)));
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
            holder.note.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_NOTE)));
            holder.status.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_STATUS)));



            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, LayoutCourseitemActivity.class);
                    mCursor.moveToPosition(position);
                    intent.putExtra("course_id", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns._ID)));
                    intent.putExtra("course_title", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
                    intent.putExtra("course_start", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_START)));
                    intent.putExtra("course_end", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_END)));
                    intent.putExtra("course_note", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_NOTE)));
                    intent.putExtra("course_status", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_STATUS)));

                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_NAME)) != null){
                        intent.putExtra("course_mentname", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_NAME)));
                    } else {
                        intent.putExtra("course_mentname", "");
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_EMAIL)) != null){
                        intent.putExtra("course_mentemail", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_EMAIL)));
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_PHONE)) != null){
                        intent.putExtra("course_mentphone", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_MENTOR_PHONE)));
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TERM)) != null){
                        intent.putExtra("course_term", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TERM)));
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_NAME)) !=null){
                        intent.putExtra("course_second_mentname", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_NAME)));
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_PHONE)) !=null){
                        intent.putExtra("course_second_mentphone", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_PHONE)));
                    }
                    if (mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_EMAIL)) !=null){
                        intent.putExtra("course_second_mentemail", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_SECOND_MENTOR_EMAIL)));
                    }

                    mContext.startActivity(intent);
                    Log.d(TAG, "onClick: exits");
                }
            });

        }
        Log.d(TAG, "onBindViewHolder: exits");
    }

    @Override
    public int getItemCount() {
        //If you leave the default to 0, you will not get any info
        if((mCursor == null) || (mCursor.getCount() == 0)){
            Log.d(TAG, "getItemCount: IS NULL IN getitemcount");
            return 1;
        } else {
            Log.d(TAG, "getItemCount: in else" + mCursor.getCount());
            return mCursor.getCount();
        }
    }

    Cursor swapCursor(Cursor newCursor){
        Log.d(TAG, "swapCursor: cursor number:" + newCursor);
        if (newCursor == mCursor){
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null) {
            Log.d(TAG, "swapCursor: not null: cursor number: " + newCursor);
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }


    static class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = "CourseViewHolder (subclass in CoursesCRVAdapter";

        ConstraintLayout parentLayout = null;
        TextView title = null;
        TextView endTitle = null;
        TextView startTitle = null;
        TextView statusTitle = null;
        TextView endText = null;
        TextView startText = null;
        TextView status = null;
        ImageButton editBtn = null;
        ImageButton deleteBtn = null;
        TextView note = null;
        TextView staticNoteTitle = null;
        TextView courseMentInfo = null;
        TextView staticMentName = null;
        TextView staticMentPhone = null;
        TextView staticMentEmail = null;
        TextView mentName = null;
        TextView mentEmail = null;
        TextView mentPhone = null;
        TextView staticTermTV = null;
        TextView termTV = null;
        OnCourseListener mOnCourseListener;
        TextView staticSecondMentName, staticSecondMentPhone, staticSecondMentEmail, secondMentName,
                secondMentPhone, secondMentEmail, secondMentHeader;

        public CourseViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "CourseViewHolder: starts");
            this.parentLayout = itemView.findViewById(R.id.layout_courseitem_CL);
            this.title = itemView.findViewById(R.id.layout_courseitem_title);
            this.startTitle = itemView.findViewById(R.id.layout_courseitem_start);
            this.endTitle = itemView.findViewById(R.id.layout_courseitem_end);
            this.statusTitle = itemView.findViewById(R.id.layout_courseitem_statusTV);
            this.endText = itemView.findViewById(R.id.layout_courseitem_tvend);
            this.startText = itemView.findViewById(R.id.layout_courseitem_tvstart);
            this.status = itemView.findViewById(R.id.layout_courseitem_status);
            this.editBtn = itemView.findViewById(R.id.layout_courseitem_Btnedit);
            this.deleteBtn = itemView.findViewById(R.id.layout_courseitem_Btndelete);
            this.note = itemView.findViewById(R.id.layout_courseitem_notecontentTV);
            this.staticNoteTitle = itemView.findViewById(R.id.layout_courseitem_notesTV);
            this.courseMentInfo = itemView.findViewById(R.id.layout_courseitem_mentinfo);
            this.staticMentName = itemView.findViewById(R.id.layout_courseitem_staticmname);
            this.staticMentEmail = itemView.findViewById(R.id.layout_courseitem_staticmemail);
            this.staticMentPhone = itemView.findViewById(R.id.layout_courseitem_staticmphone);
            this.mentName = itemView.findViewById(R.id.layout_courseitem_mentorname);
            this.mentEmail = itemView.findViewById(R.id.layout_courseitem_mentoremail);
            this.mentPhone = itemView.findViewById(R.id.layout_courseitem_mentorphone);
            this.staticTermTV = itemView.findViewById(R.id.layout_courseitem_statictermTV);
            this.termTV = itemView.findViewById(R.id.layout_courseitem_termTV);
            this.staticSecondMentName = itemView.findViewById(R.id.layout_courseitem_staticmentName2);
            this.staticSecondMentPhone = itemView.findViewById(R.id.layout_courseitem_staticMentphone2);
            this.staticSecondMentEmail = itemView.findViewById(R.id.layout_courseitem_staticMentemail2);
            this.secondMentName = itemView.findViewById(R.id.layout_courseitem_mentName2);
            this.secondMentPhone = itemView.findViewById(R.id.layout_courseitem_mentPhone2);
            this.secondMentEmail = itemView.findViewById(R.id.layout_courseitem_mentEmail2);
            this.secondMentHeader = itemView.findViewById(R.id.layout_courseitem_secondmentheader);

//
        }

        @Override
        public void onClick(View v) {
            mOnCourseListener.onCourseClick(getAdapterPosition());
        }
    }

    public interface OnCourseListener {
        void onCourseClick(int position);
    }
}
