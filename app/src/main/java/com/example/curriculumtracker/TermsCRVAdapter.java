package com.example.curriculumtracker;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class TermsCRVAdapter extends RecyclerView.Adapter<TermsCRVAdapter.TermViewHolder> {

    public static final String TAG = "TermsCRVAdapter";

    private Cursor mCursor;

    public TermsCRVAdapter (Cursor cursor) {
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: start");

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_termitem, viewGroup, false);

        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");

        if((mCursor == null) || (mCursor.getCount()==0)) {
            holder.title.setText("Term Title");
        } else {
            if (!mCursor.moveToPosition(position)){
                throw new IllegalArgumentException("could move cursor into position");
            }
            holder.title.setText(mCursor.getString(mCursor.getColumnIndex(TermsContract.Columns.TERMS_TITLE)));
            holder.endDate.setText(mCursor.getString(mCursor.getColumnIndex(TermsContract.Columns.TERMS_ENDDATE)));
            holder.startDate.setText(mCursor.getString(mCursor.getColumnIndex(TermsContract.Columns.TERMS_STARTDATE)));
        }

    }


    @Override
    public int getItemCount() {

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

    static class TermViewHolder extends RecyclerView.ViewHolder {
        public static final String TAG = "TermView Holder";

        TextView title = null;
        TextView endStaticText = null;
        TextView startStaticText = null;
        TextView endDate = null;
        TextView startDate = null;
        ImageButton editBtn = null;
        ImageButton deleteBtn = null;

        public TermViewHolder(View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.layout_termitem_title);
            this.endDate = itemView.findViewById(R.id.layout_termitem_tvend);
            this.startDate = itemView.findViewById(R.id.layout_termitem_tvstart);
            this.endStaticText = itemView.findViewById(R.id.layout_termitem_endtext);
            this.startStaticText = itemView.findViewById(R.id.layout_termitem_starttext);
            this.editBtn = itemView.findViewById(R.id.layout_termitem_buttonedit);
            this.deleteBtn = itemView.findViewById(R.id.layout_termitem_buttondelete);
        }
    }
}
