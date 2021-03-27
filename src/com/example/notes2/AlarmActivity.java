package com.example.notes2;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;


public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView,tv22_;    
    
    String memo_header,memo_position,memo_text_;
    
    public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
    SharedPreferences sharedpreferencesAlarm;
    public static AlarmActivity instance() {
    return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        
        tv22_ = (TextView) findViewById(R.id.tv22);
        
        
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        
        sharedpreferencesAlarm = getSharedPreferences(MyPREFERENCESALARM, Context.MODE_PRIVATE);
        
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        
        //Bundle extras = getIntent().getExtras();
        //final String header = extras.getString("_header");
                
        memo_header = sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", "");
        memo_text_ = sharedpreferencesAlarm.getString("memo_text", "");
                        
        tv22_.setText(memo_text_);
                
        setAlarmText("Alarm for memo:  "+memo_header);
 //Toast.makeText(AlarmActivity.this,"Alarm for memo:  "+memo_header+"  memo position   "+memo_position,Toast.LENGTH_LONG).show();
        
    }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            //setAlarmText("Alarm for memo:  "+memo_header+"\nposition   "+memo_position);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            //setAlarmText("Alarm for memo:  "+memo_header+"  memo position   "+memo_position);
            Log.d("MyActivity", "Alarm Off");
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}
