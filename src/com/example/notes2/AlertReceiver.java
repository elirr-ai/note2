package com.example.notes2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlertReceiver extends BroadcastReceiver {
	
	SharedPreferences sp=null;
//	boolean flag1min=false;
//	boolean flag30min=false;
//	boolean flag1hour=false;
//	boolean flag4hour=false;		
	
//	private final static String degC="\u2103";
//	private final static char degree = '\u00B0';
	
	final static String MYPREFERNCES="MyPrefs";
//	final static String every1minFlagString="every1minFlagString";
//	final static String every30minsFlagString="every30minsFlagString";
//	final static String every1hourFlagString="every1hourFlagString";
//	final static String every4hoursFlagString="every4hoursFlagString";
	final static String alarmOnOfStatus="alarmOnOfStatus";
	final static String NotifOnOfStatus="NotifOnOfStatus";

	
	@Override
	public void onReceive(Context context, Intent intent) {

//		c=context;
		sp = context.getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);		
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(alarmOnOfStatus, false).commit();

		
		
		//		flag1min=sp.getBoolean(every1minFlagString, false);
//		flag30min=sp.getBoolean(every30minsFlagString, false);
//		flag1hour=sp.getBoolean(every1hourFlagString, false);
//		flag4hour=sp.getBoolean(every4hoursFlagString, false);		
	
		String bdf=null;
		String bd=sp.getString("ALARMNOTEBODY", "");
		if (bd.length()>30) bdf=bd.substring(0,29);
		else bdf=bd;

		String[] st =new String[]{ 
				sp.getString("ALARMNOTEDATE", ""),
				sp.getString("ALARMNOTEPRI", ""),
				sp.getString("ALARMNOTEHEADER", "")	,
				bdf ,
				String.valueOf(sp.getInt("ALARMNOTEINDEX", 0))	};
			
		
		
		addNotification(context,st);
//////		Set12nnAlarm(c);
		
		
//Toast.makeText(context, "value is  :  "+rec+" "+lat+" "+lon,
//		Toast.LENGTH_SHORT).show();

	}
	private void addNotification(Context c, String string[]) {	
		Intent myIntent = new Intent(c, PostIntentActivityTimelyAlarm.class);
		myIntent.putExtra("postpass", string[0]+" "+string[1]);
	     PendingIntent pendingIntent11 = PendingIntent.getActivity(
	            c, 
	            7890, 
	            myIntent, 
	            Intent.FLAG_ACTIVITY_NEW_TASK);
	     		int idx=Integer.valueOf(sp.getString("ALARMNOTEPRI", ""));
	     		int i=c.getResources().getIdentifier("ic_launcher", "drawable", c.getPackageName());
	     		
	     		try {
					if (idx==1) i=c.getResources().getIdentifier("ucritical", "drawable", c.getPackageName());
					if (idx==2) i=c.getResources().getIdentifier("uimportant", "drawable", c.getPackageName());
					if (idx==3) i=c.getResources().getIdentifier("uusual", "drawable", c.getPackageName());
					if (idx==4) i=c.getResources().getIdentifier("ucasual", "drawable", c.getPackageName());
					if (idx==5) i=c.getResources().getIdentifier("ulow", "drawable", c.getPackageName());
				} catch (Exception e) {
					e.printStackTrace();
				}
	     			     		
	     	 Notification.Builder builder =
	         new Notification.Builder(c)
	         .setSmallIcon(i)	         
	         .setContentTitle(" "+string[2]+" ("+string[4]+")"     )
	         .setContentText(""+string[0])
	         .setSubText(" "+string[3])
	         .setContentIntent(pendingIntent11)
//	         .setSound(Emergency_sound_uri)  //This sets the sound to play
	         ; 	
	      NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
	      manager.notify(Integer.valueOf(string[4]), builder.build());
	   }
	
/*
public void Set12nnAlarm(Context context) {

    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    Intent intent12 = new Intent(context, AlertReceiver.class);
	PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
			1, intent12, 0);
    Calendar calendar = Calendar.getInstance();    // every day at 9 am
    calendar.setTimeInMillis(System.currentTimeMillis());

    // if it's after or equal 9 am schedule for next day
 //   if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 9) {
        Log.e("TAG", "Alarm will schedule for next day!");
        if (flag1min) calendar.add(Calendar.MINUTE, 1); // add, not set!
        if (flag30min) calendar.add(Calendar.MINUTE, 30); // add, not set!
 
        if (android.text.format.DateFormat.is24HourFormat(context)){
            if (flag1hour) calendar.add(Calendar.HOUR_OF_DAY, 1); // add, not set!
            if (flag4hour) calendar.add(Calendar.HOUR_OF_DAY, 4); // add, not set!   
        }
        else {
        if (flag1hour) calendar.add(Calendar.HOUR, 1); // add, not set!
        if (flag4hour) calendar.add(Calendar.HOUR, 4); // add, not set!
        }
        
		if (!flag1min && !flag30min &&  !flag1hour  && !flag4hour){
			flag4hour=true;
			calendar.add(Calendar.HOUR, 4); // add, not set!
		}              
        
//        calendar.add(Calendar.MINUTE, 2); // add, not set!
//    }
//    else{
//        Log.e("TAG", "Alarm will schedule for today!");
//    }
//    calendar.set(Calendar.HOUR_OF_DAY, 9);
//    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
	intent12.putExtra("rec", String.valueOf(System.currentTimeMillis()));
//	PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//			1, intent, 0);
//	long time=System.currentTimeMillis()+6000;
	am.setExact(AlarmManager.RTC_WAKEUP,
			calendar.getTimeInMillis(), pendingIntent);	
	}
*/		
}

//String lat="32.8184";  haifa
//String lon="34.9885";
