package com.example.notes2;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;



public class getGPS extends Activity implements LocationListener {
	Context context=this;
	private LocationManager locationManager;
	TextView latituteField,longitudeField;
	String msg3="0";
	String flag="BAD";
	String CRLF="\r\n";
	boolean[] bb =new boolean[2];
	GeoCoderGoogle1 gcd1;
	ArrayList<String> al=new  ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpsl);
		
		latituteField = (TextView) findViewById(R.id.TextView02);
	    longitudeField = (TextView) findViewById(R.id.TextView04);
	    
	    flag="BAD";
	    msg3="0";
	    		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 1, this);
		
		CheckConnectivity1 CC1=new CheckConnectivity1(context);
		bb=CC1.haveNetworkConnection();
		if (bb[0] || bb[1]){
			 gcd1=new GeoCoderGoogle1(context);
		}
	}

	//@Override
	//public void onPause(){
	//	locationManager.removeUpdates(this);
	//    super.onPause();
	//} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gpsmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.copyrt:
			//act_copyright();
			Toast.makeText(getApplicationContext(), "Veresion 1.0 - Copyright Eli Rajchert", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.help:
			//act_help();
			Toast.makeText(getApplicationContext(), "Help menu item pressed", Toast.LENGTH_SHORT).show(); 
			break;
				
		case R.id.gps:
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
			
			Toast.makeText(getApplicationContext(), "gps menu item pressed", Toast.LENGTH_SHORT).show(); 
			break;
		
			
		case R.id.abort:
			Intent intentb = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intentb);
			Intent intentMessage=new Intent();
		    // put the message to return as result in Intent
	        intentMessage.putExtra("MESSAGE",msg3);
			intentMessage.putExtra("STATUS","BAD");
			// Set The Result in Intent
	        setResult(2,intentMessage);
	       //
	        if(locationManager != null){
	        	locationManager.removeUpdates(this);
	        	//locationManager.removeUpdates(_locationListenerNetworkProvider);
	        	locationManager = null;
	             }
	       //
	        
	        
	        
	        //locationManager.removeUpdates(this);
			getGPS.this.finish();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
				
		case R.id.exit:
			Intent intentc = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intentc);
			Intent intentMessage_1=new Intent();
		    // put the message to return as result in Intent
	        intentMessage_1.putExtra("MESSAGE",msg3);
			intentMessage_1.putExtra("STATUS",flag);
			// Set The Result in Intent
	        setResult(2,intentMessage_1);
	        
	        if(locationManager != null){
	        	locationManager.removeUpdates(this);
	        	//locationManager.removeUpdates(_locationListenerNetworkProvider);
	        	locationManager = null;
	             }
	        getGPS.this.finish();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
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
	
	
	

	@Override
	public void onLocationChanged(Location location) {
	double longtitude,latitude;
	longtitude=location.getLongitude();
	latitude=location.getLatitude();
		al.clear();
		String s="";
	al=gcd1.getGoogleLocationArray(longtitude,latitude );
	for(int i=0;i<al.size();i++){
		s+=al.get(i);
	}
	
		flag="OK";
		String msg = "Latitude: " + latitude
				+ "Longitude: " + longtitude;
		String msg1=location.getLatitude()+"";
		String msg2=String.valueOf(location.getLongitude());

		msg3="Time: "+currentDateFormat()+CRLF+
				"Latitude: "+msg1+CRLF+
				"Longitude: "+msg2+CRLF+s+CRLF;
		latituteField.setText(msg1);
	    longitudeField.setText(msg2);
	    
		Toast.makeText(getBaseContext(),flag+"  "+ msg, Toast.LENGTH_LONG).show();
				
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(getBaseContext(), "Gps is status changed! ",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps is turned on!! ",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(getBaseContext(), "Gps is turned off!! ",Toast.LENGTH_SHORT).show();
	}
	
	private String currentDateFormat() {
		
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy-HH:mm");
    	return dateFormat.format(new Date());
            
	}
	
	@Override
	public void onBackPressed() 
	{
		
		Intent intentc = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intentc);
		Intent intentMessage_1=new Intent();
	    // put the message to return as result in Intent
        intentMessage_1.putExtra("MESSAGE",msg3);
		intentMessage_1.putExtra("STATUS",flag);
		// Set The Result in Intent
        setResult(2,intentMessage_1);
        
        if(locationManager != null){
        	locationManager.removeUpdates(this);
        	//locationManager.removeUpdates(_locationListenerNetworkProvider);
        	locationManager = null;
             }
		getGPS.this.finish();
			}
	
	
}
