package com.example.curriculumtracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.Calendar;

public class SetAsmtAlertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
AsmtAlertCRVAdapter.OnAsmtListener{

    public static final String TAG = "SetAsmtAlertActivity";
    public static final int COURSE_ALERT_ID = 555;
    public static final int LOADER_ID = 0;
    private AsmtAlertCRVAdapter mAdapter;
    private AlertDialog mDialog = null;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "prime_note_channel";
    private String dueDate;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_asmt_alert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Set an Assessment Alert");

        RecyclerView recyclerView = findViewById(R.id.content_asmtalert_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AsmtAlertCRVAdapter(null, this, null);
        recyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        incomingIntent();
        if (dueDate != null){
            showSetAlertDialog();
        } else {
            Log.d(TAG, "onCreate: test is null");
        }
        createNotificationChannel();

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.backToMain:
                Intent intent1 = new Intent(this, MainActivity.class);
                Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
                startActivity(intent1);
                break;
            case R.id.alertCourseStart:
                Intent intent = new Intent(this, SetCourseAlertActivity.class);
                intent.putExtra("start_alert", "start");
                Log.d(TAG, "onOptionsItemSelected: alertCourseStart " + item.getItemId());
                startActivity(intent);
                break;
            case R.id.alertCourseEnd:
                Intent intent2 = new Intent(this, SetCourseAlertActivity.class);
                intent2.putExtra("start_alert", "end");
                startActivity(intent2);
                break;
            case R.id.alertAsmtDue:
                startActivity(new Intent(this, SetAsmtAlertActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNotificationChannel(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        String [] projection = {AssessmentsContract.Columns._ID, AssessmentsContract.Columns.ASMTS_TITLE,
                AssessmentsContract.Columns.ASMTS_DATE, AssessmentsContract.Columns.ASMTS_TYPE,
                AssessmentsContract.Columns.ASMTS_COURSE};

        switch (id){
            case LOADER_ID:
                return new CursorLoader(this,
                        AssessmentsContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid id");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
    // gets intent from AssessmentsAlertCRVAdapter
    // gives us our start date
    private void incomingIntent(){

        if (getIntent().hasExtra("start_dialog")){
            dueDate = getIntent().getStringExtra("start_dialog");

        }
    }

    @SuppressLint("SetTextTl8n")
    public void showSetAlertDialog(){
        Log.d(TAG, "showSetAlertDialog: BEFORE SETTING LISTENER VALUE");

        String start = dueDate;
        String parts [] = start.split("/");

        final long twentyFourHours = (24*60*60*1000);
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        final long dueDateInMillies = calendar.getTimeInMillis();
        final long timeDif = dueDateInMillies - twentyFourHours;

        Intent notifyIntent = new Intent(this, MyReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        @SuppressLint("InflateParams") View messageView = getLayoutInflater().inflate(R.layout.dialog_coursealert, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set a reminder?");


        builder.setView(messageView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDialog != null && mDialog.isShowing()) {
                    if (timeDif > twentyFourHours){
                        alarmManager.set(AlarmManager.RTC_WAKEUP, (dueDateInMillies - twentyFourHours), notifyPendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, 5, notifyPendingIntent);
                    }
                    Toast.makeText(getApplicationContext(), "Save successful. You will be alerted 24 hours before the assessment due date", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                });

        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        TextView promptTv = messageView.findViewById(R.id.dialog_coursealert_promptTv);
        promptTv.setText("Set up a reminder to alert you 24 hours before the assessment due date?");
        mDialog.show();

    }

    @Override
    public void onCourseClick(int position) {

    }
}
