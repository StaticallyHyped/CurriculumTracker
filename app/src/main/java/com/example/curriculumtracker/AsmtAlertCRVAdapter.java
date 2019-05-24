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

import static com.example.curriculumtracker.R.layout.layout_assessmentitem;

public class AsmtAlertCRVAdapter extends RecyclerView.Adapter<AsmtAlertCRVAdapter.AsmtAlertViewHolder> {

    public static final String TAG = "AsmtAlertCRVAdapter";
    private Context mContext;
    private Cursor mCursor;
    private CourseAlertCRVAdapter.OnTaskClickListener mListener;

    public AsmtAlertCRVAdapter(Cursor cursor, Context context, CourseAlertCRVAdapter.OnTaskClickListener listener){
        this.mCursor = cursor;
        this.mContext = context;
        this.mListener = listener;
    }


    @NonNull
    @Override
    public AsmtAlertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout_assessmentitem, viewGroup, false);
        return new AsmtAlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsmtAlertCRVAdapter.AsmtAlertViewHolder holder, final int position) {
        holder.shareBtn.setVisibility(View.GONE);
        holder.deleteBtn.setVisibility(View.GONE);
        holder.editBtn.setVisibility(View.GONE);

        if ((mCursor == null) || mCursor.getCount() == 0){
            holder.title.setText("Assessment Title");
        } else {
            if (!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("couldn't move cursor into position");
            }
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TITLE)));
            holder.date.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_DATE)));
            holder.type.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TYPE)));
            holder.courseTv.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_COURSE)));
            holder.staticCourseTv.setText("In Course:");
            holder.staticDate.setText("Due Date:");
            holder.staticType.setText("Assessment Type:");

            //sends intent to SetAsmtAlertActivity
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);
                    Intent intent = new Intent(mContext, SetAsmtAlertActivity.class);
                    intent.putExtra("start_dialog", mCursor.getString(mCursor.getColumnIndex
                            (AssessmentsContract.Columns.ASMTS_DATE)));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if ((mCursor == null) || (mCursor.getCount() == 0)){
            return 1;
        } else{
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
            Log.d(TAG, "swapCursor: not null: cursor number: " + newCursor);
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    static class AsmtAlertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ConstraintLayout parentLayout = null;
        TextView title, date, staticDate, staticType, type, courseTv, staticCourseTv;
        ImageButton editBtn, deleteBtn, shareBtn;
        OnAsmtListener mOnAsmtListener;

        public AsmtAlertViewHolder(View itemView){
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.layout_asmtitem_CL);
            this.staticDate = itemView.findViewById(R.id.layout_asmntitem_date);
            this.staticType = itemView.findViewById(R.id.layout_asmntitem_type);
            this.title = itemView.findViewById(R.id.layout_asmntitem_title);
            this.date = itemView.findViewById(R.id.layout_assessmentitem_dateTV);
            this.deleteBtn = itemView.findViewById(R.id.layout_asmntitem_buttondelete);
            this.editBtn = itemView.findViewById(R.id.layout_asmntitem_buttonedit);
            this.type = itemView.findViewById(R.id.layout_assesmentitem_typeTV);
            this.shareBtn = itemView.findViewById(R.id.layout_assessmentitem_sharebutton);
            this.courseTv = itemView.findViewById(R.id.layout_asmtitem_courseTV);
            this.staticCourseTv = itemView.findViewById(R.id.layout_asmtitem_staticcourseTV);

        }

        @Override
        public void onClick(View v) {
            mOnAsmtListener.onCourseClick(getAdapterPosition());
        }
    }
    public interface OnAsmtListener {
        void onCourseClick(int position);
    }
}
