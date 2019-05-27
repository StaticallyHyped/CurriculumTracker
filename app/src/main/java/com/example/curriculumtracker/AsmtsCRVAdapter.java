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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class AsmtsCRVAdapter extends RecyclerView.Adapter<AsmtsCRVAdapter.AsmtViewHolder> {
    public static final String TAG = "AsmtsCRVAdapter";
    private Cursor mCursor;
    private Context mContext;

    public ArrayList<String> courseArray;


    public AsmtsCRVAdapter(Cursor cursor, Context context){
        this.mCursor = cursor;
        this.mContext = context;

    }
    @NonNull
    @Override
    public AsmtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_assessmentitem,
                viewGroup, false);

        return new AsmtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AsmtViewHolder holder, final int position) {
        holder.editBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);

        holder.staticCourseTV.setVisibility(View.GONE);
        holder.courseTV.setVisibility(View.GONE);
       holder.date.setVisibility(View.GONE);

        if ((mCursor == null) || (mCursor.getCount() == 0)){
            holder.title.setText("Nothing Here. Add an assessment using the button below.");

        } else {
            if (!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("could not move cursor into position");
            }
            try {
                String test = mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE));
                courseArray.add(test);
            } catch (Exception e){
                e.printStackTrace();
            }

            holder.staticType.setVisibility(View.VISIBLE);
            holder.staticDate.setVisibility(View.VISIBLE);
            holder.staticCourseTV.setVisibility(View.VISIBLE);
            holder.type.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.courseTV.setVisibility(View.VISIBLE);
            holder.title.setText("Assessment Title");
            holder.staticDate.setText("Assessment Date:");
            holder.staticType.setText("Assessment Type:");
            try {
                holder.type.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TYPE)));
                holder.title.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TITLE)));
                holder.date.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_DATE)));
                holder.courseTV.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_COURSE)));
            } catch (Exception e){
                e.printStackTrace();
            }

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LayoutAsmtItemActivity.class);
                    Log.d(TAG, "onClick: INSIDE ON CLICK, Asmt title index" + mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TITLE)));
                    mCursor.moveToPosition(position);
                    try {
                        intent.putExtra("asmt_course", mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
                        Log.d(TAG, "onClick: in try: " + mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
                    }
                    catch (Exception e){
                        Log.d(TAG, "onClick: in catch: " + mCursor.getString(mCursor.getColumnIndex(CoursesContract.Columns.COURSE_TITLE)));
                        e.printStackTrace();
                    }
                    intent.putExtra("asmt_id", mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns._ID)));
                    intent.putExtra("asmt_title", mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TITLE)));
                    intent.putExtra("asmt_date", mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_DATE)));
                    intent.putExtra("asmt_type", mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TYPE)));
                    intent.putExtra("asmt_course", mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_COURSE)));

                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if ((mCursor == null) || (mCursor.getCount()==0)){
            return 1;

        } else {
            Log.d(TAG, "getItemCount: mCursor count" + mCursor.getCount());
            return mCursor.getCount();
        }
    }

    Cursor swapCursor(Cursor newCursor){
        Log.d(TAG, "swapCursor: starts");
        if (newCursor == mCursor){
            Log.d(TAG, "swapCursor: newCursor == mCursor, returned null");
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null) {
            Log.d(TAG, "swapCursor: newCursor not null");
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    static class AsmtViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parentLayout;
        TextView title = null;
        TextView date = null;
        TextView staticDate = null;
        TextView staticType = null;
        ImageButton editBtn = null;
        ImageButton deleteBtn = null;
        TextView type = null;
        ImageButton shareBtn = null;
        TextView staticCourseTV = null;
        TextView courseTV = null;


        public AsmtViewHolder(View itemView) {
            super(itemView);
            this.staticDate = itemView.findViewById(R.id.layout_asmntitem_date);
            this.staticType = itemView.findViewById(R.id.layout_asmntitem_type);
            this.title = itemView.findViewById(R.id.layout_asmntitem_title);
            this.date = itemView.findViewById(R.id.layout_assessmentitem_dateTV);
            this.deleteBtn = itemView.findViewById(R.id.layout_asmntitem_buttondelete);
            this.editBtn = itemView.findViewById(R.id.layout_asmntitem_buttonedit);
            this.type = itemView.findViewById(R.id.layout_assesmentitem_typeTV);
            this.parentLayout = itemView.findViewById(R.id.layout_asmtitem_CL);

            this.staticCourseTV = itemView.findViewById(R.id.layout_asmtitem_staticcourseTV);
            this.courseTV = itemView.findViewById(R.id.layout_asmtitem_courseTV);
        }
    }
}
