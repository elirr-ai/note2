package com.example.notes2;

import java.util.Calendar;

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

////	final static int CODE=1;
	
	final static String MYPREFERNCES="MyPrefs";
	
	final static String every1minFlagString="every1minFlagString";
	final static String every30minsFlagString="every30minsFlagString";
	final static String every1hourFlagString="every1hourFlagString";
	final static String every4hoursFlagString="every4hoursFlagString";
	
	final static String alarmOnOfStatus="alarmOnOfStatus";
	final static String alarmTimeHour="alarmTimeHour";
	final static String alarmTimeMinutes="alarmTimeMinutes";
	final static String NotifOnOfStatus="NotifOnOfStatus";

	Note note=null;	
	int position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_time_picker);
		note = (Note) getIntent().getSerializableExtra("noteObject");
		position=(Integer) getIntent().getIntExtra("position", 0);
		String memodate=note.getDate();
		String memoPriority=note.getPriority();		

		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		
		final int	cHour=calendar.get(Calendar.HOUR);
		final int	cMinute=calendar.get(Calendar.MINUTE);
		
		settime=(Button)findViewById(R.id.settime);
		tv=(TextView)findViewById(R.id.tv);
		tvbottom=(TextView)findViewById(R.id.tvbottom);
		tvlatlon=(TextView)findViewById(R.id.tvbottom1);
		tvlatlon.setText(memodate+"  "+memoPriority+"  "+position);
		btactivate=(Button)findViewById(R.id.bt);
		btcancel=(Button)findViewById(R.id.btcancel);

		
		iv10=(ImageView)findViewById(R.id.alm1290);
		iv10L=(ImageView)findViewById(R.id.alm1291);

		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));
		showNotifStatus(sp.getBoolean(NotifOnOfStatus, false));

		every1min=(Button)findViewById(R.id.repeat1minute);
		every1min.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFlags(true, false, false, false);
				showFlags();
				showTVBOTTOM();
			}
		});
		
		every30mins=(Button)findViewById(R.id.repeat30mins);
		every30mins.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFlags(false, true, false, false);
				showFlags();
				showTVBOTTOM();
			}
		});
		
		every1hour=(Button)findViewById(R.id.repeat1hours);
		every1hour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFlags(false, false,true, false);
				showFlags();
				showTVBOTTOM();
			}
		});
		
		every4hours=(Button)findViewById(R.id.repeat4hours);
		every4hours.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFlags(false, false, false, true);
				showFlags();
				showTVBOTTOM();
			}
		});
		
		cancelrepeatalarm=(Button)findViewById(R.id.cancelrepeatalarm);	
		cancelrepeatalarm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setFlags(false, false, false, true);
				showFlags();
				showTVBOTTOM();
				
			}
		});
		
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
				setAlarm();
			}
		});
		
		btcancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancelAlarm();
			}
		});
		
		getFlags();
		showFlags();
		showTVBOTTOM();

	}


	private void setAlarm(){
		
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(alarmOnOfStatus, true).commit();
		editor.putString("ALARMNOTEDATE", note.getDate()).commit();
		editor.putString("ALARMNOTEPRI", note.getPriority()).commit();
		editor.putString("ALARMNOTEHEADER", note.getMemo_header()).commit();			
		editor.putString("ALARMNOTEBODY", note.getMemoBody()).commit();
		editor.putInt("ALARMNOTEINDEX", position).commit();	
//		int code=Integer.valueOf(note.getMemoID().substring(
//				note.getMemoID().length()-7,
//				note.getMemoID().length()-1)     );
		
		  Toast.makeText(getApplicationContext(), 
				    "CODE "+position, 
				    Toast.LENGTH_LONG).show();
		
		AlarmManager alarmManager =(AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent(context, AlertReceiver.class);
		intent.putExtra("rec", String.valueOf(System.currentTimeMillis()));
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				position, intent, 0);
		
		alarmManager.setExact(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), pendingIntent);		
		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));
//		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//				 calendar.getTimeInMillis(), 0, pendingIntent);		
	}
	
	private void cancelAlarm(){
//		int code=Integer.valueOf(note.getMemoID().substring(
//				note.getMemoID().length()-7,
//				note.getMemoID().length()-1)     );
		
		AlarmManager alarmManager =
				(AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent=new Intent(context, AlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				position, intent, 0);
		alarmManager.cancel(pendingIntent);
		
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(alarmOnOfStatus, false).commit();
		showAlarmStatus(sp.getBoolean(alarmOnOfStatus, false));

	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainalarmtime, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
	String s="";
//	String hour,min,alarmStat;
	if (sp.getBoolean(every1minFlagString, false))s="Every 1 min";
	else 	if (sp.getBoolean(every30minsFlagString, false))s="Every 30 min";
	else if (sp.getBoolean(every1hourFlagString, false))s="Every 1 hour";
	else if (sp.getBoolean(every4hoursFlagString, false))s="Every 4 hour";
	int mins=sp.getInt(alarmTimeMinutes, 0);
	int hrs=sp.getInt(alarmTimeHour, 0);
	boolean onOff=sp.getBoolean(alarmOnOfStatus, false);
	  Toast.makeText(getApplicationContext(), 
    "status\n "+
	  hrs+":"+mins+"*"+s+"    "+onOff, 
    Toast.LENGTH_LONG).show();	
	
	
			
			return true;
		}
		
		if (id == R.id.notif) {
			SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
	editor.putBoolean(NotifOnOfStatus, !sp.getBoolean(NotifOnOfStatus, false)).commit();
			showNotifStatus(sp.getBoolean(NotifOnOfStatus, false));
			return true;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	
//	private void showTV(){
//		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);		
//		tv.setText(""+
//				sp.getBoolean(every1minFlagString, false)+ "/"+
//				sp.getBoolean(every30minsFlagString, false)+"/"+
//				sp.getBoolean(every1hourFlagString, false)+"/"+
//				sp.getBoolean(every4hoursFlagString, false)+"/");		
//	}
	
	private void showTVBOTTOM(){
	
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
if (sp.getBoolean(every1minFlagString, false)){
	tvbottom.setText(""+"1 minute repeat");
}
else if (sp.getBoolean(every30minsFlagString, false)){
	tvbottom.setText(""+"30 minute repeat");
}
else if (sp.getBoolean(every1hourFlagString, false)){
	tvbottom.setText(""+"1 hour repeat");
}		
else if (sp.getBoolean(every4hoursFlagString, false)){
	tvbottom.setText(""+"4 hours repeat");
}
	
	}
	
	private void setFlags(boolean b, boolean c, boolean d, boolean e) {
		every1minFlag=b;
		every30minsFlag=c;
		every1hourFlag=d;
		every4hoursFlag=e;	
		
		if (!b && !c && !d && !e){
			every4hoursFlag=true;	
			}		
		
SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(every1minFlagString, every1minFlag).commit();
		editor.putBoolean(every30minsFlagString, every30minsFlag).commit();
		editor.putBoolean(every1hourFlagString, every1hourFlag).commit();
		editor.putBoolean(every4hoursFlagString, every4hoursFlag).commit();
		
	}
	
	private void getFlags(){
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);
		every1minFlag=sp.getBoolean(every1minFlagString, false);
		every30minsFlag=sp.getBoolean(every30minsFlagString, false);
		every1hourFlag=sp.getBoolean(every1hourFlagString, false);
		every4hoursFlag=sp.getBoolean(every4hoursFlagString, false);		
		
	}
	private void showFlags(){
		every1min.setBackgroundColor(Color.WHITE);
		every30mins.setBackgroundColor(Color.WHITE);
		every1hour.setBackgroundColor(Color.WHITE);
		every4hours.setBackgroundColor(Color.WHITE);		
		
		if (every1minFlag) every1min.setBackgroundColor(Color.GREEN);
		if (every30minsFlag) every30mins.setBackgroundColor(Color.GREEN);
		if (every1hourFlag) every1hour.setBackgroundColor(Color.GREEN);
		if (every4hoursFlag) every4hours.setBackgroundColor(Color.GREEN);
		
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
	
	
	
	
}
