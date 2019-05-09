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
import android.widget.Button;
import android.widget.Toast;

import java.security.InvalidParameterException;

public class TermsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "TermsActivity";

    public static final int LOADER_ID = 0;

    private TermsCRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        String testTwo = getIntent().getStringExtra("noteRV");
        Toast.makeText(getApplicationContext(), testTwo, Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.activity_termlistRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TermsCRVAdapter(null);
        recyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

    }

    public void goToAddTerm(View v){
        Button goToAddTermBtn = findViewById(R.id.activity_terms_newtermbtn);
        startActivity(new Intent(TermsActivity.this, AddtermActivity.class));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        Log.d(TAG, "onCreateLoader: starts");
        String [] projection = {TermsContract.Columns._ID, TermsContract.Columns.TERMS_TITLE,
                TermsContract.Columns.TERMS_STARTDATE, TermsContract.Columns.TERMS_ENDDATE};
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        TermsContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

                default:
                    throw new InvalidParameterException(TAG + ".onCreateLoader method called with invalid loader id");

        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: onLoadStart");
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
