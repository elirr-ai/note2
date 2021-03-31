package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AlertReceiver extends BroadcastReceiver {
	
	SharedPreferences sp=null;
	SharedPreferences.Editor editor=null;
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
	final static String ALARMSHAREDPERF="ALARMSHAREDPERF";
	final static String ALARMNOTEPOSITION="ALARMNOTEPOSITION";

	Calendar calendar=Calendar.getInstance();
	int position;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);		
		editor = sp.edit();
		editor.putBoolean(alarmOnOfStatus, false).commit();
		  ArrayList<Note>  alarmSet1 = new ArrayList<Note>();		
		  
		  try {
			  alarmSet1 = (ArrayList<Note>) ObjectSerializer.deserialize(sp.getString("ALARMNOTENOTE",
					  ObjectSerializer.serialize(new ArrayList<Note>())));
			  } catch (IOException e) {
			    e.printStackTrace();
			  } 
		position=sp.getInt(ALARMNOTEPOSITION, 0);
		addNotification(context,alarmSet1);
	
//////		Set12nnAlarm(c);
		
		
//Toast.makeText(context, "value is  :  "+rec+" "+lat+" "+lon,
//		Toast.LENGTH_SHORT).show();

	}///////////////////////////////////
	private void addNotification(Context c, ArrayList<Note> alarmSet1) {	
		int	 ptr=0;
		int tm = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
		Intent myIntent = new Intent(c, PostIntentActivityTimelyAlarm.class);
		
//		sp = c.getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);		
//		SharedPreferences.Editor editor8 = sp.edit();
		editor.putString("postpasspostpass", ""+tm).commit();
		
		
		myIntent.putExtra("postpass", ""+tm);
	     PendingIntent pendingIntent11 = PendingIntent.getActivity(
	            c, 
	    		tm, 
	            myIntent, 
	            Intent.FLAG_ACTIVITY_NEW_TASK);
	     
	     if (alarmSet1!=null && !alarmSet1.isEmpty() && alarmSet1.size()>0){	    	 
	  
	     		int	idx=Integer.valueOf(alarmSet1.get(ptr).getPriority());
	     		int i=c.getResources().getIdentifier("ic_launcher", "drawable", c.getPackageName());
	     		
	     		try {
					if (idx==1) i=c.getResources().getIdentifier("ucritical", "drawable", c.getPackageName());
					if (idx==2) i=c.getResources().getIdentifier("uimportant", "drawable", c.getPackageName());
					if (idx==3) i=c.getResources().getIdentifier("uusual", "drawable", c.getPackageName());
					if (idx==4) i=c.getResources().getIdentifier("ucasual", "drawable", c.getPackageName());
					if (idx==5) i=c.getResources().getIdentifier("ulow", "drawable", c.getPackageName());
				} catch (Exception e) {
					Toast.makeText(c, "ERR 11 "+e.getMessage(),Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
	     		
				String bdf=null;
				String bd=alarmSet1.get(ptr).getMemoBody();
					if (bd.length()>40) bdf=bd.substring(0,39);
					else bdf=bd;

	     	 Notification.Builder builder =
	         new Notification.Builder(c)
	         .setSmallIcon(i)	         
	         .setContentTitle(" "+alarmSet1.get(ptr).getMemo_header()+
	        		 " ("+alarmSet1.get(ptr).getPriority()+")"     )
	         .setContentText(""+alarmSet1.get(ptr).getDate()+" "+String.valueOf(tm))
	         .setSubText(" "+bdf+
	        		 "  "+position)
	         .setContentIntent(pendingIntent11)
//	         .setSound(Emergency_sound_uri)  //This sets the sound to play
	         ; 	
	     	alarmSet1.remove(ptr); 	 

	     	try {
	    	    editor.putString("ALARMNOTENOTE", ObjectSerializer.serialize(alarmSet1));
	    	  } catch (IOException e) {
	    	    e.printStackTrace();
	    	  }
	    	  editor.commit();
	    	  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
	     	 
	      try {
			NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
			  manager.notify(tm, builder.build());
		} catch (Exception e) {
			Toast.makeText(c, "ERR 12 "+e.getMessage(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
				}
			}
	     else {
				Toast.makeText(c, "ERR 81- 1 alarm 1 note ",Toast.LENGTH_LONG).show();
	     	}
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
