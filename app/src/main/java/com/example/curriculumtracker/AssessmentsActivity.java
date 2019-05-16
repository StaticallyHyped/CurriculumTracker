package com.example.curriculumtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class AssessmentsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "AssessmentsActivity";
    public static final int LOADER_ID = 0;

    private AsmtsCRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        RecyclerView recyclerView = findViewById(R.id.activity_assessmentsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AsmtsCRVAdapter(null, this);
        recyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        Log.d(TAG, "onCreateLoader: starts");
        String [] projection = {AssessmentsContract.Columns._ID, AssessmentsContract.Columns.ASMTS_TITLE, AssessmentsContract.Columns.ASMTS_TYPE,
        AssessmentsContract.Columns.ASMTS_DATE};

        switch(id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        AssessmentsContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

                default:
                    throw new IllegalArgumentException(TAG + ".onCreateLoader did not find LOADER ID");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: starts");
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
    }

    public void goToAddAsmts(View view){
        startActivity(new Intent(AssessmentsActivity.this, AddAsmtActivity.class));
    }
}
