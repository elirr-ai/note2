package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivityTimelyAlarm extends Activity {

	TextView tv,tvbottom,tvlatlon;
	Button btactivate,btcancel, settime;
	Button every1min, every30mins, every1hour, every4hours,cancelrepeatalarm;
	ImageView iv10,iv10L;
	Context context=this;
	Calendar calendar=Calendar.getInstance();
	boolean	every1minFlag=false,
					every30minsFlag=false,
					every1hourFlag=false,
					every4hoursFlag=false;
	
	final static String MYPREFERNCES="MyPrefs";
	
	final static String alarmOnOfStatus="alarmOnOfStatus";
	final static String alarmTimeHour="alarmTimeHour";
	final static String alarmTimeMinutes="alarmTimeMinutes";
	final static String NotifOnOfStatus="NotifOnOfStatus";
	final static String ALARMSHAREDPERF="ALARMSHAREDPERF";
	final static String ALARMNOTEPOSITION="ALARMNOTEPOSITION";
	final static String ALARMNOTENOTE="ALARMNOTENOTE";
	final static String ALARMNOTENOTEHM="ALARMNOTENOTEHM";
	
	Note note=null;	
	int position;
	int random;
	ArrayList<Note>  alarmSet, alarmSetOne ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		random=getRandom();	
		setContentView(R.layout.activity_main_activity_time_picker);
		note = (Note) getIntent().getSerializableExtra("noteObject");
		position=(Integer) getIntent().getIntExtra("position_marter", 0);

		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		alarmSet = new ArrayList<Note>();	
		
		final int	cHour=calendar.get(Calendar.HOUR);
		final int	cMinute=calendar.get(Calendar.MINUTE);
		
		settime=(Button)findViewById(R.id.settime);
		tv=(TextView)findViewById(R.id.tv);
		tvbottom=(TextView)findViewById(R.id.tvbottom);
		tvbottom.setText(note.getMemo_header());
		tvlatlon=(TextView)findViewById(R.id.tvbottom1);
		tvlatlon.setText(note.getDate()+"    "+note.getPriority()+"    "+position);
		btactivate=(Button)findViewById(R.id.bt);
		btcancel=(Button)findViewById(R.id.btcancel);

		
		iv10=(ImageView)findViewById(R.id.alm1290);
		iv10L=(ImageView)findViewById(R.id.alm1291);
		iv10L.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(NotifOnOfStatus, !sp.getBoolean(NotifOnOfStatus, false)).commit();
		showNotifStatus(sp.getBoolean(NotifOnOfStatus, false));
		
	}
});
	
		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));
		showNotifStatus(sp.getBoolean(NotifOnOfStatus, false));

		every1min=(Button)findViewById(R.id.repeat1minute);
		every1min.setVisibility(View.INVISIBLE);
		
		every30mins=(Button)findViewById(R.id.repeat30mins);
		every30mins.setVisibility(View.INVISIBLE);
		
		every1hour=(Button)findViewById(R.id.repeat1hours);
		every1hour.setVisibility(View.INVISIBLE);
		
		every4hours=(Button)findViewById(R.id.repeat4hours);
		every4hours.setVisibility(View.INVISIBLE);
		
		cancelrepeatalarm=(Button)findViewById(R.id.cancelrepeatalarm);	
		cancelrepeatalarm.setVisibility(View.INVISIBLE);
		
		AnalogClock simpleAnalogClock = (AnalogClock)findViewById(R.id.analog_clock); // inititate a analog clock
		simpleAnalogClock.setBackgroundColor(Color.YELLOW); 
		simpleAnalogClock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				  Toast.makeText(getApplicationContext(), 
//						    "clk  ", 
//						    Toast.LENGTH_LONG).show();			
				TimePickerDialog timePickerDialog = new TimePickerDialog(context,
//						android.R.style.Theme_Holo_Light_Dialog,
						3,
						new TimePickerDialog.OnTimeSetListener() {
			
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						tv.setText("Hour: "+hourOfDay+" Minute: "+minute);
						calendar.set(Calendar.HOUR, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);
						
						SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt(alarmTimeHour, hourOfDay).commit();// check for null when read
						editor.putInt(alarmTimeMinutes, minute).commit();// check for null when read
					
						}
				},cHour,
						cMinute,
						android.text.format.DateFormat.is24HourFormat(context));
				timePickerDialog.setTitle("Choose hour and minute:");
				timePickerDialog.setIcon(R.drawable.alarmclock128);
				
				timePickerDialog.show();
			}
		});
		
		
		
		tv.setText("Hour: "+sp.getInt(alarmTimeHour, 0)+
				" Minute: "+sp.getInt(alarmTimeMinutes, 0));
		
		settime.setOnClickListener(new OnClickListener() {		
			
			@Override
			public void onClick(View v) {
	
				TimePickerDialog timePickerDialog = new TimePickerDialog(context,
//						android.R.style.Theme_Holo_Light_Dialog,
						3,
						new TimePickerDialog.OnTimeSetListener() {
			
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						tv.setText("Alarm Hour: "+hourOfDay+" Minute: "+minute);
						calendar.set(Calendar.HOUR, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						calendar.set(Calendar.SECOND, 0);
						
						SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();
						editor.putInt(alarmTimeHour, hourOfDay).commit();// check for null when read
						editor.putInt(alarmTimeMinutes, minute).commit();// check for null when read
	
						
						}
				},cHour, cMinute,
						android.text.format.DateFormat.is24HourFormat(context));
				timePickerDialog.setTitle("Choose hour and minute:");
				timePickerDialog.setIcon(R.drawable.alarmclock128);
				
				timePickerDialog.show();
				}
			});

		btactivate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manageList();
				setAlarm();
				addAlarmLogFile();
			}
		});
		
		btcancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancelAlarm();
				removeAlarmLogFile();
			}
		});
		
	}


	private void setAlarm(){
		
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);						  
				Toast.makeText(getApplicationContext(), 
				    "CODE "+random,  Toast.LENGTH_LONG).show();
		
		AlarmManager alarmManager =(AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent(context, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				random, intent, PendingIntent.FLAG_ONE_SHOT);
				
		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));
//  debug  /////////////		
		alarmManager.setExact(AlarmManager.RTC_WAKEUP,
				 calendar.getTimeInMillis(), pendingIntent);				
	}
	
	@SuppressWarnings("unchecked")
	private void cancelAlarm(){	
		int rr=0;
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		ArrayList<AlarmPostActivityHolder1> alx = new ArrayList<AlarmPostActivityHolder1>();
		try{
		alx = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTEHM,
				  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
		  } catch (IOException e) {
		    e.printStackTrace();
			Toast.makeText(getApplicationContext(), 
				    "ERROR 568 CANCEL",  Toast.LENGTH_LONG).show();

		  } 
				  for (int i=0;i<alx.size();i++){
					if (note.getMemo_header().equals(alx.get(i).getNotee().getMemo_header())){	  
						rr=alx.get(i).getRandom();
						break;					  
					  }
				  }
	
		AlarmManager alarmManager =
				(AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent(context, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				rr, intent, PendingIntent.FLAG_ONE_SHOT);
		alarmManager.cancel(pendingIntent);		
		editor.putBoolean(alarmOnOfStatus, false).commit();
		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainalarmtime, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.notif) {
			SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(NotifOnOfStatus, !sp.getBoolean(NotifOnOfStatus, false)).commit();
			showNotifStatus(sp.getBoolean(NotifOnOfStatus, false));
			return true;
		}
		
		if (id == R.id.showSP) {
SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
StringBuilder sb=new StringBuilder();

sb.append(alarmSet.get(0).getMemo_header()+" "+alarmSet.get(0).getPriority());
String s=sb.toString();
Toast.makeText(getApplicationContext(), 
	    "status\n "+
		  s, 
	    Toast.LENGTH_LONG).show();	
			return true;
		}
	
		if (id == R.id.clearSP) {
			SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
//		note = (Note) getIntent().getSerializableExtra("noteObject");
editor.remove("noteObject").commit();		
editor.remove(ALARMNOTENOTE).commit();	
editor.remove(ALARMNOTENOTEHM).commit();
Toast.makeText(getApplicationContext(), 
	    "status\n "+  "done", Toast.LENGTH_LONG).show();	

			return true;
		}
		
		if (id == R.id.debug2) {
			  try {
	SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
	  alarmSet = (ArrayList<Note>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTE,
			  ObjectSerializer.serialize(new ArrayList<Note>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
			Toast.makeText(getApplicationContext(), 
					"CODE "+alarmSet.size(), 
					Toast.LENGTH_LONG).show();								
			return true;
		}
		
		if (id == R.id.debug1) {
			int r123=getRandom();
			Toast.makeText(getApplicationContext(), 
					"CODE "+r123, 
					Toast.LENGTH_LONG).show();								
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void showAlarmStatus(boolean boolean1) {
		if (boolean1) {
			iv10.setImageDrawable(getResources().getDrawable(R.drawable.alarmon64x64));
		}
		else {
			iv10.setImageDrawable(getResources().getDrawable(R.drawable.alarmoff64x64));		
		}		
	}
	
	private void showNotifStatus(boolean boolean1) {
		if (boolean1) {
			iv10L.setImageDrawable(getResources().getDrawable(R.drawable.notifsoundon));
		}
		else {
			iv10L.setImageDrawable(getResources().getDrawable(R.drawable.notifsoundoff));		
		}				
	}


	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("alm1000", "ABAB");
		setResult(RESULT_OK, intent);
		finish();
		super.onBackPressed();
	}
	
	private int getRandom(){
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());		
		return random.nextInt(999999)+1;
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void manageList(){
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		if (!sp.contains(ALARMNOTENOTE)){
			alarmSet.add(note); 
			try {
			    editor.putString(ALARMNOTENOTE, ObjectSerializer.serialize(alarmSet)).commit();
			  } catch (IOException e) {
			    e.printStackTrace();
			  }
			  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
		}		
		else {			
			  try {
	  alarmSet = (ArrayList<Note>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTE,
			  ObjectSerializer.serialize(new ArrayList<Note>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
alarmSet.add(note);
try {
	    editor.putString(ALARMNOTENOTE, ObjectSerializer.serialize(alarmSet)).commit();
	  } catch (IOException e) {
	    e.printStackTrace();
	  }
	  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
}
	}
	
	
	@SuppressWarnings("unchecked")
	private void addAlarmLogFile(){
		ArrayList<AlarmPostActivityHolder1> alx = new ArrayList<AlarmPostActivityHolder1>();
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		String timeH=""+sp.getInt(alarmTimeHour, 0);
		if (timeH.length()<2) timeH="0"+timeH;
		String timeM=""+sp.getInt(alarmTimeMinutes, 0);
		if (timeM.length()<2) timeM="0"+timeM;	
		String timeHM=timeH+":"+timeM;								
				
		if (!sp.contains(ALARMNOTENOTEHM)){
			alx.add(new AlarmPostActivityHolder1(note, 0, 0, timeHM, random, calendar.getTimeInMillis())); 
			try {
			    editor.putString(ALARMNOTENOTEHM, ObjectSerializer.serialize(alx)).commit();
			  } catch (IOException e) {
			    e.printStackTrace();
			  }
			  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
		}		
		else {			
			  try {
	  alx = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTEHM,
			  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
		alx.add(new AlarmPostActivityHolder1(note, 0, 0, timeHM, random, calendar.getTimeInMillis())); 
			try {
				editor.putString(ALARMNOTENOTEHM, ObjectSerializer.serialize(alx)).commit();
				  } catch (IOException e) {
				    e.printStackTrace();
				  }

	  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
		}		
	}
	
	
	
	//////////////////////
	@SuppressWarnings("unchecked")
	private void removeAlarmLogFile(){
		ArrayList<AlarmPostActivityHolder1> alx = new ArrayList<AlarmPostActivityHolder1>();
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();						
				
		if (!sp.contains(ALARMNOTENOTEHM)){
			  Toast.makeText(getApplicationContext(), 
		    "ERROR 237: nothing to remove  ", 
		    Toast.LENGTH_LONG).show();	
			}		
		else {			
			  try {
	  alx = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sp.getString(ALARMNOTENOTEHM,
			  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
			  for (int i=0;i<alx.size();i++){
//				  if (random==alx.get(i).getRandom()){
				if (note.getMemo_header().equals(alx.get(i).getNotee().getMemo_header())){	  
					  alx.remove(i);
						try {
							editor.putString(ALARMNOTENOTEHM, ObjectSerializer.serialize(alx)).commit();
							  } catch (IOException e) {
							    e.printStackTrace();
							  }
					  break;					  
				  }
			  }
			  
			  
//		alx.add(new AlarmPostActivityHolder1(note, 0, 0, timeHM, random, calendar.getTimeInMillis())); 


	  editor.putInt(ALARMNOTEPOSITION, position).commit();// saved to sp			
		}		
	}
	
	
	/////////////////////////
	
	
}
