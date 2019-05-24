package com.example.curriculumtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.example.curriculumtracker.R.layout.layout_courseitem;

public class CourseAlertCRVAdapter extends RecyclerView.Adapter<CourseAlertCRVAdapter.CourseAlertViewHolder> {

    public static final String TAG = "CourseAlertCRVAdapter";
    private Context mContext;
    private Cursor mCursor;
    private OnTaskClickListener mListener;


    public static final int DIALOG_ID_DELETE = 1;

    private AlertDialog mDialog = null;

    interface OnTaskClickListener{
        void showSetAlertDialog(View v);
    }

    public CourseAlertCRVAdapter(Cursor cursor, Context context, OnTaskClickListener listener){
        this.mCursor = cursor;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CourseAlertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout_courseitem, viewGroup, false);
        return new CourseAlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseAlertViewHolder holder, final int position) {

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
        holder.status.setVisibility(View.GONE);
        holder.statusTitle.setVisibility(View.GONE);
        holder.term.setVisibility(View.GONE);
        holder.staticTerm.setVisibility(View.GONE);

        if((mCursor == null) || (mCursor.getCount() == 0)){
            holder.title.setText("Course Title");
        } else {
            if (!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("couldn't move cursor into position: " + position);
            }
            holder.startText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_START)));
            holder.endText.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_END)));
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));

            //Sends intent to SetCourseAlertActivity
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);
                    Intent intent = new Intent(mContext, SetCourseAlertActivity.class);
                    intent.putExtra("start_dialog", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_START)));
                    intent.putExtra("end_dialog", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_END)));
                    mContext.startActivity(intent);

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
            return 1;
        } else {
            return mCursor.getCount();
        }
    }

    Cursor swapCursor(Cursor newCursor){
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

    static class CourseAlertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ConstraintLayout parentLayout = null;
        TextView title = null;
        TextView endTitle = null;
        TextView startTitle = null;
        TextView statusTitle = null;
        TextView endText = null;
        TextView startText = null;
        TextView status = null;
        TextView staticTerm = null;
        TextView term = null;
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
        AlertDialog mDialog = null;
        View messageView = null;

        OnCourseListener mOnCourseListener;


        public CourseAlertViewHolder(View itemView) {
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.layout_courseitem_CL);
            this.title = itemView.findViewById(R.id.layout_courseitem_title);
            this.endTitle = itemView.findViewById(R.id.layout_courseitem_start);
            this.startTitle = itemView.findViewById(R.id.layout_courseitem_end);
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
            this.term = itemView.findViewById(R.id.layout_courseitem_termTV);
            this.staticTerm = itemView.findViewById(R.id.layout_courseitem_statictermTV);
            this.messageView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_coursealert, null, false);

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
