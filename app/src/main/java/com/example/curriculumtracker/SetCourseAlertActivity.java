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

public class SetCourseAlertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
CourseAlertCRVAdapter.OnCourseListener {

    public static final String TAG = "SetCourseAlertActivity";
    public static final int LOADER_ID = 0;
    public static final int COURSE_ALERT_ID = 555;
    private CourseAlertCRVAdapter mAdapter;
    private AlertDialog mDialog = null;
    private String startDate, endDate;
    private static final int NOTIFICATION_ID = 0;
    public static final String PRIMARY_CHANNEL_ID = "prime_note_channel";
    private static String type;


    private NotificationManager mNotificationManager;
   // NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_course_alert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.content_coursealert_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseAlertCRVAdapter(null, this, null);
        recyclerView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        startOrEndIntent();
        incomingIntent();
        if (type.equals("start")){
            toolbar.setTitle("Set A Course Start Alert");
        } else {
            toolbar.setTitle("Set A Course End Alert");
        }

        if (startDate != null){

            showSetAlertDialog();
        } else {
            Log.d(TAG, "onCreate: startDate is null");
        }
        createNotificationChannel();
//TODO add functionality to .setText for Term asssociated with a course
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
                            "Course Alert Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Reminds the user their course will be beginning or ending within 24 hours");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String [] projection = {CoursesContract.Columns._ID, CoursesContract.Columns.COURSE_TITLE,
        CoursesContract.Columns.COURSE_START, CoursesContract.Columns.COURSE_END};

        switch (id){
            case LOADER_ID:
                return new CursorLoader(this,
                        CoursesContract.CONTENT_URI,
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

    private void startOrEndIntent(){
        if (getIntent().hasExtra("start_alert")){
            type = getIntent().getStringExtra("start_alert");
            Log.d(TAG, "startOrEndIntent: TYPE = " + type);
        } else {
            Log.d(TAG, "startOrEndIntent: TYPE IS NULL");
        }
        
    }
    // gets intent from CourseAlertCRVAdapter
    //gets start date
    private void incomingIntent(){
        if (getIntent().hasExtra("start_dialog")){
            startDate = getIntent().getStringExtra("start_dialog");

        }
        if (getIntent().hasExtra("end_dialog")) {
            endDate = getIntent().getStringExtra("end_dialog");
        }
    }

    @SuppressLint("SetTextTl8n")
    public void showSetAlertDialog(){

        String date;
        if (type.equals("start")){
            date = startDate;
        } else {
            date = endDate;
        }

        String parts [] = date.split("/");

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
                        Toast.makeText(getApplicationContext(), "Save successful. You will be alerted 24 hours before the " + type + " of your course.", Toast.LENGTH_SHORT).show();
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
            promptTv.setText("Set up a reminder to alert you when your course is 24 hours from " + type + "ing?");
            mDialog.show();

    }

    @Override
    public void onCourseClick(int position) {

        Log.d(TAG, "onCourseClick: YOU'VE CLICKED A COURSE");
        Intent intent = new Intent(this, LayoutCourseitemActivity.class);
    }

}
