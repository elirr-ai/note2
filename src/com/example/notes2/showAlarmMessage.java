package com.example.notes2;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;



public class showAlarmMessage extends Activity {

		String memo_header,memo_position,memo_text_;
    	public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
		SharedPreferences sharedpreferencesAlarm;
		TextView textView1;
		private static final String DNAME = "memo_files";
		public static final String MyPREFERENCES = "MyPrefs" ; 
		
	    
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shownotif);
		
		textView1=(TextView)findViewById(R.id.textView1);
		
		
		sharedpreferencesAlarm = getSharedPreferences(MyPREFERENCESALARM, Context.MODE_PRIVATE);
		 memo_header = sharedpreferencesAlarm.getString("AlarmPerfPositionHeader", "");
	        memo_text_ = sharedpreferencesAlarm.getString("memo_text", "");
	        textView1.setText(
	        		"+Headr: \nPosition: "+
	        		
	        		memo_header+"\n\n"+memo_text_);
//	        Intent intent = new Intent();
//	        intent.setClass(getBaseContext(), AlarmService.class); 
//	        getBaseContext().stopService(intent);

	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
switch (id) {
		
		
			
		case R.id.Back:
		String[] a=memo_header.split(" ");
			
			Toast.makeText(getBaseContext(),"Exiting...  "+a[2],Toast.LENGTH_SHORT).show();
			NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			nMgr.cancelAll();
	     // Explicit Intent by specifying its class name
	        Intent i = new Intent(showAlarmMessage.this, ActivityTwo.class);
	        i.putExtra("Value1", DNAME);  //directory name
      	    i.putExtra("Value2", MyPREFERENCES);
      	    i.putExtra("Value3", a[2]);  //file name
      	   // Starts TargetActivity
      	  finishAffinity();
	        startActivity(i);
			
				break;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
	        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
	            try{
	                Method m = menu.getClass().getDeclaredMethod(
	                    "setOptionalIconsVisible", Boolean.TYPE);
	                m.setAccessible(true);
	                m.invoke(menu, true);
	            }
	            catch(NoSuchMethodException e){
	                Log.e("TAG", "onMenuOpened", e);
	            }
	            catch(Exception e){
	                throw new RuntimeException(e);
	            }
	        }
	    }
	    return super.onMenuOpened(featureId, menu);
	}


	
	
	
}
