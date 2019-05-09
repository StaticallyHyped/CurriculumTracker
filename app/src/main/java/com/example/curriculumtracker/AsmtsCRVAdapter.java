package com.example.curriculumtracker;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class AsmtsCRVAdapter extends RecyclerView.Adapter<AsmtsCRVAdapter.AsmtViewHolder> {
    // do I try to inflate a fragment or a view in my AsmtsCRVAdapter for my notesRV?
    // can I include a loader in here? Do I need to?
    public static final String TAG = "AsmtsCRVAdapter";
    private Cursor mCursor;


    public AsmtsCRVAdapter(Cursor cursor){
        this.mCursor = cursor;

    }
    @NonNull
    @Override
    public AsmtViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_assessmentitem,
                viewGroup, false);
        return new AsmtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AsmtViewHolder holder, int position) {
        if ((mCursor == null) || (mCursor.getCount() == 0)){
            holder.title.setText("Assessment Title");
            holder.staticDate.setText("Assessment Date");
            holder.staticType.setText("Assessment Type");
        } else {
            if (!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("could not move cursor into position");
            }
            holder.title.setText("Assessment Title");
            holder.staticDate.setText("Assessment Date");
            holder.staticType.setText("Assessment Type");
            System.out.println("ASSESSMENTS TYPE: " + AssessmentsContract.Columns.ASMTS_TYPE);
            holder.type.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TYPE)));
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_TITLE)));
            holder.date.setText(mCursor.getString(mCursor.getColumnIndex(AssessmentsContract.Columns.ASMTS_DATE)));
        }
    }


    @Override
    public int getItemCount() {
        if ((mCursor == null) || (mCursor.getCount()==0)){
            return 1;

        } else {
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

    static class AsmtViewHolder extends RecyclerView.ViewHolder {

        TextView title = null;
        TextView date = null;
        TextView staticDate = null;
        TextView staticType = null;
        ImageButton editBtn = null;
        ImageButton deleteBtn = null;
        TextView type = null;


        public AsmtViewHolder(View itemView) {
            super(itemView);
            this.staticDate = itemView.findViewById(R.id.layout_asmntitem_date);
            this.staticType = itemView.findViewById(R.id.layout_asmntitem_type);
            this.title = itemView.findViewById(R.id.layout_asmntitem_title);
            this.date = itemView.findViewById(R.id.layout_assessmentitem_dateTV);
            this.deleteBtn = itemView.findViewById(R.id.layout_asmntitem_buttondelete);
            this.editBtn = itemView.findViewById(R.id.layout_asmntitem_buttonedit);
            this.type = itemView.findViewById(R.id.layout_assesmentitem_typeTV);

        }
    }
}
