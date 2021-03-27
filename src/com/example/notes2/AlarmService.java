package com.example.notes2;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmService extends IntentService {
	   public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
	      	
	private NotificationManager alarmNotificationManager;
    private SharedPreferences sharedpreferencesAlarm;
    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
    	 sharedpreferencesAlarm = getSharedPreferences(MyPREFERENCESALARM, Context.MODE_PRIVATE);
         
    	 
    	String header=sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", "");
    	String body=sharedpreferencesAlarm.getString("memo_text", "");
    	//MediaPlayer  mp=MediaPlayer.create(getBaseContext(), R.raw.tada);
    	MediaPlayer  mp=MediaPlayer.create(getBaseContext(), R.raw.martindennytheenchantedsea);
		mp.start();	
        
    	sendNotification(body,header);
    }

    private void sendNotification(String body, String header) {
        Log.d("AlarmService", "Preparing to send notification...: " + body);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

    //    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
   //            new Intent(this, AlarmActivity.class), 0);

         PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, showAlarmMessage.class), 0);

        
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle(header).setSmallIcon(R.drawable.notifalarm)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}