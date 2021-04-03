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
	
	final static String MYPREFERNCES="MyPrefs";
	final static String alarmOnOfStatus="alarmOnOfStatus";
	final static String NotifOnOfStatus="NotifOnOfStatus";
	final static String ALARMSHAREDPERF="ALARMSHAREDPERF";
	final static String ALARMNOTEPOSITION="ALARMNOTEPOSITION";
//	final static String ALARMNOTENOTE="ALARMNOTENOTE";
	final static String ALARMNOTENOTEHM="ALARMNOTENOTEHM";
	final static String ALARMNOTEPOSTALARMACT="ALARMNOTEPOSTALARMACT";

	
	Calendar calendar=Calendar.getInstance();
	int position;
	long timeL;
	ArrayList<AlarmPostActivityHolder1> alx;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		timeL=System.currentTimeMillis();
		sp = context.getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);		
		editor = sp.edit();

		
		if (sp.contains(ALARMNOTEPOSTALARMACT)){
			editor.remove(ALARMNOTEPOSTALARMACT).commit();
			}		
				
		editor.putBoolean(alarmOnOfStatus, false).commit();
//		  ArrayList<Note>  alarmSet1 = new ArrayList<Note>();				  
//		  try {
//			  alarmSet1 = (ArrayList<Note>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTE,
//					  ObjectSerializer.serialize(new ArrayList<Note>())));
//			  } catch (IOException e) {
//			    e.printStackTrace();
//			  } 
		  
			 alx = new ArrayList<AlarmPostActivityHolder1>();
			  try {
	  alx = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTEHM,
			  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
		Note nt=null;
		int j=0;
		for (int i=0;i<alx.size();i++){
			long l=alx.get(i).getTimeMillis();
			if (Math.abs(l-timeL) <5*1000){
				nt=alx.get(i).getNotee(); j=i;break;
			}
			
			
		}
			  
			  
			  
			  
			  
		position=sp.getInt(ALARMNOTEPOSITION, 0);
		addNotification(context,nt, j);
//		addNotification(context,alarmSet1);
	
//////		Set12nnAlarm(c);
		
		
//Toast.makeText(context, "value is  :  "+rec+" "+lat+" "+lon,
//		Toast.LENGTH_SHORT).show();

	}///////////////////////////////////
	private void addNotification(Context c, Note nt, int j) {	
	    if (nt!=null){	    	
	    	Intent myIntent = new Intent(c, PostIntentActivityTimelyAlarm.class);	 
//	    	int	 ptr=0;
	    	int tm = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
	    	int	idx=Integer.valueOf(nt.getPriority());
	    	int i=c.getResources().getIdentifier("ic_launcher", "drawable", c.getPackageName());
//		index=addIntent(alarmSet1, tm, ptr);
		
		myIntent.putExtra("postprir", ""+nt.getPriority());
		myIntent.putExtra("postheader", ""+nt.getMemo_header());
		myIntent.putExtra("postbody", ""+nt.getMemoBody());
		myIntent.putExtra("postdate", ""+nt.getDate());

	     PendingIntent pendingIntent11 = PendingIntent.getActivity(
	            c, (tm/2+10), myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

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
				String bd=nt.getMemoBody();
					if (bd.length()>40) bdf=bd.substring(0,39);
					else bdf=bd;

	     	 Notification.Builder builder =
	         new Notification.Builder(c)
	         .setSmallIcon(i)	         
	         .setContentTitle(" "+nt.getMemo_header()+
	        		 " ("+nt.getPriority()+")  index " + tm    )
	         .setContentText(""+nt.getDate()+" "+String.valueOf(tm))
	         .setSubText(" "+bdf+
	        		 "  "+position)
	         .setContentIntent(pendingIntent11)
//	         .setSound(Emergency_sound_uri)  //This sets the sound to play
	         ; 	
//	     	alarmSet1.remove(ptr); 
	     	alx.remove(j); 
//	     	try {
//	    	    editor.putString(ALARMNOTENOTE, ObjectSerializer.serialize(alarmSet1)).commit();
//	    	  } catch (IOException e) {
//	    	    e.printStackTrace();
//	    	  }
			try {
				editor.putString(ALARMNOTENOTEHM, ObjectSerializer.serialize(alx)).commit();
				  } catch (IOException e) {
				    e.printStackTrace();
				  }	     	
	     	
	     	
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
	@SuppressWarnings("unchecked")
	private int addIntent(ArrayList<Note> alarmSet1, int tm, int ptr) {
		Note note=alarmSet1.get(ptr);
		ArrayList<AlarmPostActivityHolder1>	alarmNT = new ArrayList<AlarmPostActivityHolder1>();	
		if (!sp.contains(ALARMNOTEPOSTALARMACT)){
			alarmNT.add(new AlarmPostActivityHolder1(note, tm, alarmNT.size() ));
		
		  try {
			    editor.putString(ALARMNOTEPOSTALARMACT, ObjectSerializer.serialize(alarmNT));
			  } catch (IOException e) {
			    e.printStackTrace();
			  }
			  editor.commit();
//			  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
		}		
		else {			
			  try {
	  alarmNT = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sp.getString(ALARMNOTEPOSTALARMACT,
			  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
alarmNT.add(new AlarmPostActivityHolder1(note, tm,alarmNT.size()));
try {
	    editor.putString(ALARMNOTEPOSTALARMACT, ObjectSerializer.serialize(alarmNT));
	  } catch (IOException e) {
	    e.printStackTrace();
	  }
	  editor.commit();
	
		}
		return alarmNT.size();
	}
*/
	
	
	
	
	
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
