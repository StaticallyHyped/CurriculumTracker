package com.example.curriculumtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DialogCoursealertActivity extends AppCompatActivity {

    public static final String TAG = "DialogCourseAlertActivity";
    private AlertDialog mDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showSetAlertDialog();
    }

    /*@SuppressLint("SetTextTl8n")
    public void showSetAlertDialog(){
        Log.d(TAG, "showSetAlertDialog: BEFORE SETTING LISTENER VALUE");


        @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.dialog_coursealert, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Curriculum Tracker");

        builder.setView(messageView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        TextView promptTv = messageView.findViewById(R.id.dialog_coursealert_promptTv);
        promptTv.setText("HOLY SHIT THIS ACTUALLY WORKING");
        mDialog.show();

    }*/
}
