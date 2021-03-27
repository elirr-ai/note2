package com.example.notes2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmClk3_MainActivity extends Activity implements View.OnClickListener {
	
	PendingIntent pi;
	Intent intent;
	private AlarmManagerBroadcastReceiver alarm;
    private TimePicker alarmTimePicker;
   private Button alarmOff,alarmSet,alarmClr;
    private TextView alarmTextView,tv22_;
    private CalendarView cv;
    Context context=this;
    AlarmManager amgr;
    SharedPreferences sharedpreferences;
    int mYear,mMonth,mday,mHour,mMinute;
    boolean calanderTouched=false,timeTouched=false;
    
    public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
    SharedPreferences sharedpreferencesAlarm;
    
    final public static String ONE_TIME = "onetime";
	final public static String PLAY = "play";
	public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String Key = "key1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clk3__main);
        alarm = new AlarmManagerBroadcastReceiver();
        amgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        tv22_ = (TextView) findViewById(R.id.tv22);
        cv = (CalendarView) findViewById(R.id.simpleCalendarView);
       
        alarmOff = (Button) findViewById(R.id.alarmOff);
        alarmSet = (Button) findViewById(R.id.alarmSet);
        alarmClr = (Button) findViewById(R.id.alarmClr);
        
        
        alarmOff.setOnClickListener(this);
        alarmSet.setOnClickListener(this);
        alarmClr.setOnClickListener(this);
              
        cv.setFocusedMonthDateColor(Color.MAGENTA); // set the red color for the dates of  focused month
        cv.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        cv.setSelectedWeekBackgroundColor(Color.YELLOW); // red color for the selected week's background
        cv.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line
        cv.setBackgroundColor(Color.YELLOW);
        cv.setWeekNumberColor(Color.RED);
        cv.setSelectedWeekBackgroundColor(Color.GREEN);  
        cv.setSelectedDateVerticalBar(getResources().getDrawable(R.drawable.vbar));     
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferencesAlarm = getSharedPreferences(MyPREFERENCESALARM, Context.MODE_PRIVATE);
                    
    alarmTextView.setText(sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", ""));
    if (sharedpreferencesAlarm.getString("memo_text", "").length()>20){
    tv22_.setText(sharedpreferencesAlarm.getString("memo_text", "").substring(0, 20));
    } 
    else {
        tv22_.setText(sharedpreferencesAlarm.getString("memo_text", ""));
   	
    }
if (getKey()==0){  // 0=undefined
	setKey(1);  // 1=1st entry set alarm, -1 entry from notof to clear alarm
}
Toast.makeText(getApplicationContext(), "key= "+getKey(), Toast.LENGTH_LONG).show();
if (getKey()==1){
	alarmOff.setEnabled(false);
	alarmSet.setEnabled(true);
	alarmSet.setBackgroundColor(Color.GREEN);
}
if (getKey()==-1){
	alarmOff.setEnabled(true);
	alarmSet.setEnabled(false);
	alarmOff.setBackgroundColor(Color.RED);
}

alarmTimePicker.setIs24HourView(true);


String st=getDateCommas(System.currentTimeMillis());
String[] st1=st.split(",");
mYear=Integer.valueOf(st1[0]);
mMonth=Integer.valueOf(st1[1]);
mday=Integer.valueOf(st1[2]);
mHour=Integer.valueOf(st1[3]);
mMinute=Integer.valueOf(st1[4]);
//alarmTextView.setText("---"+"  "+st);	

// perform setOnDateChangeListener event on CalendarView
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            // display the selected date by using a toast
        	calanderTouched=true;
        	mYear=year;
        	mMonth=month;
        	mday=dayOfMonth;  
        	Toast.makeText(getApplicationContext(), mday + "/" + mMonth + "/" + mYear, Toast.LENGTH_LONG).show();
        }
    });       
      
        alarmTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		timeTouched=true;
		mMinute=minute;
		mHour=hourOfDay;
    	Toast.makeText(getApplicationContext(), mHour + ":" + mMinute , Toast.LENGTH_LONG).show();
				
			}
		});  
        
        
        
        
        
        
	}  // end of onCreate

//    public void startRepeatingTimer(View view) {
//    	Context context = this.getApplicationContext();
//    	if(alarm != null){
//    		alarm.SetAlarm(context);
//    	}else{
//    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
//    	}
//    }
    
//    public void cancelRepeatingTimer(View view){
//    	Context context = this.getApplicationContext();
//    	if(alarm != null){
//    		alarm.CancelAlarm(context);
// 
//    	}else{
//    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
//    	}
//    }
    
//    public void onetimeTimer(View view){
//    	Context context = this.getApplicationContext();
//    	if(alarm != null){
//    		alarm.setOnetimeTimer(context,0);
//    	}else{
//    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
//    	}
//    	finish();
//}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_clk3__main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		String sta;
		if (id == R.id.action_settings) {
			Calendar calendar = Calendar.getInstance();
//
//calendar.set(mYear, mMonth, mday,mHour, mMinute, 0);
//Toast.makeText(context, st1[0], Toast.LENGTH_SHORT).show();		
//long startTimeA = calendar.getTimeInMillis();
//String styzzz=getDateCommas(startTimeA);
	
//		}	
//			calendar.setTimeInMillis(System.currentTimeMillis());

			if (timeTouched || calanderTouched){
			
calendar.set(mYear, mMonth, mday,mHour, mMinute, 0);
long startTime = calendar.getTimeInMillis();
sta=getDateCommas(startTime);
//			alarmTextView.setText("***"+startTime+"  "+sta);
			}		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if (id==alarmSet.getId()){
		   		if(alarm != null){
		    		setOnetimeTimer(context,calc_time_alarm());
		    	}else{
		    		Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		    	}
		 setKey(-1);  		
		   		
		   		
		}
		if (id==alarmOff.getId()){
	   		Toast.makeText(context, "Alarm off", Toast.LENGTH_SHORT).show();
//	   		CancelAlarm222(context);
   		setKey(1);
        startActivity(new Intent(this, MainActivity.class));
	   	 System.exit(0);
//	   		ActivityManager amg = (ActivityManager) context
//	                .getSystemService(Context.ACTIVITY_SERVICE);
//	amg.killBackgroundProcesses(getPackageName());
		}
		if (id==alarmClr.getId()){
	   		Toast.makeText(context, "Alarm CLR!!!!!", Toast.LENGTH_SHORT).show();
	   		CancelAlarm222(context);

		}		
		
		
		
	}

	private long calc_time_alarm() {
		
		int h=alarmTimePicker.getCurrentHour();
		int m=alarmTimePicker.getCurrentMinute();

		Calendar calendar = Calendar.getInstance();
		int tz1=(TimeZone.getDefault().getRawOffset()/1000)/3600;//time zone offset=2h for IL

		long startTime = calendar.getTimeInMillis();
		long l=cv.getDate();

		long l2=System.currentTimeMillis();
		long millis=l2;
		int hh = (int) (((millis / 1000) / 3600)%24);
		int mm = (int) (((millis / 1000) / 60) % 60);
		int ss = (int) ((millis / 1000) % 60);

		hh+=tz1;
		if (hh>23) hh-=24;
		int Delta=(h-hh)*3600*1000+(m-mm)*60*1000;
//		tv22_.setText("SYS Time "+l2+" H "+h+"M "+m);///  "+startTime);

				   		Toast.makeText(context, "Alarm set "+h+" "+m+" long "+l ,
				   				Toast.LENGTH_SHORT).show();
						return (l2+Delta);	

		
	}
	
	
    public void CancelAlarm222(Context context) {
        intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(PLAY, Boolean.FALSE);
        pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        amgr.cancel(pi);
        Toast.makeText(context, "canceldeddddd",
        		Toast.LENGTH_LONG).show();
 
    }
    
    public void CancelAlarm11(Context context) {
    	System.exit(0);
//        Toast.makeText(context, "canceldeddddd",Toast.LENGTH_LONG).show();
    }
    
    
    
    public void setOnetimeTimer(Context context, long ll){
        intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra(PLAY, Boolean.TRUE);
        pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        amgr.set(AlarmManager.RTC_WAKEUP, ll, pi);
    }
	
	private void setKey (int f){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Key, f).commit();
    	}
	
		private int getKey(){
		return (sharedpreferences.getInt(Key, 0));
		}	
	
	private String getDateCommas(long millis){
		Date date = new Date(millis);
		String formattedDate=new SimpleDateFormat("yyyy,MM,dd,kk,mm").format(date);
		return formattedDate;
	}
	
	
	
	
	
}
