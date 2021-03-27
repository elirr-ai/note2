package com.example.notes2;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	Ringtone ringtone;
    SharedPreferences sharedpreferencesAlarm;
    final public static String ONE_TIME = "onetime";
	final public static String PLAY = "play";
    public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
    

    
	@Override
	public void onReceive(Context context, Intent intent) {
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
         Bundle extras = intent.getExtras();
         StringBuilder msgStr = new StringBuilder();
         Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Acquire the lock
         wl.acquire();
     
           if (alarmUri == null)
             {
                 alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
             }    
            
         if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
        	 msgStr.append("One time Timer : ");
             if(extras != null && extras.getBoolean(PLAY, Boolean.FALSE)){
                 Toast.makeText(context,
              		 "onr time PLAY= "+String.valueOf(extras.getBoolean(PLAY, Boolean.FALSE)),
               		 Toast.LENGTH_LONG).show();
                 
             ringtone = RingtoneManager.getRingtone(context, alarmUri);
             ringtone.play();
             
             sharedpreferencesAlarm = context.getSharedPreferences(MyPREFERENCESALARM, Context.MODE_PRIVATE);
//alarmTextView.setText(sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", ""));

//tv22_.setText(sharedpreferencesAlarm.getString("memo_text", ""));
          
             
             
             
             
             
Notification(context, sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", ""),
		sharedpreferencesAlarm.getString("memo_text", "")	
		);
              }
             if(extras != null && !extras.getBoolean(PLAY, Boolean.FALSE)){
                 Toast.makeText(context,
              		 "onr time PLAY= "+String.valueOf(extras.getBoolean(PLAY, Boolean.FALSE)),
               		 Toast.LENGTH_LONG).show();
    
             ringtone = RingtoneManager.getRingtone(context, alarmUri);
             ringtone.stop();
             ringtone=null;
              }           
             
         }
         
         
         
         Format formatter = new SimpleDateFormat("hh:mm:ss a");
         msgStr.append(formatter.format(new Date()));

         Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
         
         //Release the lock
         wl.release();
         
	}  // end of onReciev
	
//	public void SetAlarm(Context context)    {
//        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//        intent.putExtra(ONE_TIME, Boolean.FALSE);
//        intent.putExtra(PLAY, Boolean.TRUE);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 10, intent, 0);
//        //After after 30 seconds
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
//    }

//    public void CancelAlarm(Context context) {
//        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//        intent.putExtra(PLAY, Boolean.FALSE);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 10, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//        Toast.makeText(context, "canceldeddddd",
//        		Toast.LENGTH_LONG).show();
// 
//    }
    
//    public void CancelAlarm11(Context context) {
//    	System.exit(0);
//        Toast.makeText(context, "canceldeddddd",Toast.LENGTH_LONG).show();
//    }
    
    
    
//    public void setOnetimeTimer(Context context, long ll){
//    	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//        intent.putExtra(ONE_TIME, Boolean.TRUE);
//        intent.putExtra(PLAY, Boolean.TRUE);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
//        am.set(AlarmManager.RTC_WAKEUP, ll, pi);
//    }
    
	public void Notification(Context context, String hdr, String msg) {
		// Set Notification Title
//		String strtitle = "iiii";
		// Open NotificationView Class on Notification Click
		Intent intent = new Intent(context, AlarmClk3_MainActivity.class);
		// Send data to NotificationView Class
		intent.putExtra("title", hdr);
		intent.putExtra("text", msg);
		// Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
 
		// Create Notification using NotificationCompat.Builder
		android.app.Notification.Builder builder = new android.app.Notification.Builder(
				context)
				// Set Icon
				.setSmallIcon(R.drawable.alarm1)
				// Set Ticker Message
				.setTicker(msg)
				// Set Title
				.setContentTitle(hdr) 
				// Set Text
				.setContentText(msg)
				// Add an Action Button below Notification
				.addAction(R.drawable.alarm1, "Action Button", pIntent)
				// Set PendingIntent into Notification
				.setContentIntent(pIntent)
				// Dismiss Notification
				.setAutoCancel(true);
 
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());
 
	}
    
    
}