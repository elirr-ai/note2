package com.example.notes2;

import java.util.ArrayList;
 
import java.util.Locale;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class Speech10MainActivity extends Activity {
private static final int REQUEST_CODE = 1234;
public static final int GETDICATEDMESSAGE = 1788;

TextView swipe;
Button Start;
EditText Speech;
Dialog match_text_dialog;
ListView textlist;
ArrayList<String> matches_text;
String selectedSpeech="";
int pointerText=0;
float startY ;
float startX ;
InputMethodManager imm;
boolean isKeyBoardShow=true;
double screenPARAM;
private displ di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech10_main);
        Start = (Button)findViewById(R.id.start_reg);
        Speech = (EditText)findViewById(R.id.speech);
        swipe = (TextView)findViewById(R.id.swipe);
        swipe.setBackgroundColor(Color.BLUE);
        swipe.setTextColor(Color.RED);
       
        Start.setBackgroundColor(Color.YELLOW);
        di=new displ();
        screenPARAM=di.getScreenInches();
     	if (screenPARAM>5 && screenPARAM <6){
     		swipe.setTextSize(14.0f);
     	}
     	else if (screenPARAM>7 && screenPARAM <8){
     		swipe.setTextSize(23.0f);
     	}
     	else if (screenPARAM>8.0 && screenPARAM <9.9){
     		swipe.setTextSize(25.0f);
     	}
     	else {
     		swipe.setTextSize(26.0f);
     	}
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
       	imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
//       	imm.hideSoftInputFromWindow(Speech.getWindowToken(), 0); 
        
        swipe.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	
            	
            	switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	startY = event.getY();
    				startX=event.getX();
               
//                	swipe.setFocusable(true);
//                	swipe.requestFocus();
//                	Speech.setSelection(Speech.getText().length());
                    break;
                case MotionEvent.ACTION_UP:
        			float endY = event.getY();
    				float endX = event.getX();
    				float diffY=endY - startY ;
    				float diffX=endX - startX;
    		
    				if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f) {
    	Toast.makeText(getApplicationContext(), "TOUCH", Toast.LENGTH_SHORT).show();	
     if (isKeyBoardShow){
    	 imm.showSoftInput(Speech, 0);
    	// Speech.setFocusable(false);
     }
     else {
    	 imm.hideSoftInputFromWindow(Speech.getWindowToken(), 0); 
    	 //Speech.setFocusable(true);
    	// Speech.requestFocus();
     }
  
     isKeyBoardShow=!isKeyBoardShow;	
    
    }
    				
    				else if (Math.abs(diffY)> Math.abs(diffX) && endY < startY) {
    	Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT).show();
    	pointerText=0;
    	Speech.setSelection(pointerText);
    				}

    				else if (Math.abs(diffY)> Math.abs(diffX) && endY > startY) {
    	Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
    	pointerText=Speech.getText().length();
    	Speech.setSelection(pointerText);
    				}	
    		
    				else if (Math.abs(diffY)< Math.abs(diffX) && endX > startX) {
    	Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();	
    	pointerText++;
    	if (pointerText>Speech.getText().length()) pointerText=Speech.getText().length();
    	Speech.setSelection(pointerText);
    				}
    				else {
    					Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show();	
    					pointerText--;
    			    	if (pointerText<0) pointerText=0;
    			    	Speech.setSelection(pointerText);
    				}
                	
                	
                	

            	
   //                 v.performClick();
                    break;
                default:
                    break;
                }
                return true;

            }
        });
        
        
        
        
        if (isConnected())
        Toast.makeText(getApplicationContext(), "Netwrok is connected!!!", Toast.LENGTH_LONG).show();
 if (!isConnected()){
 	 Toast.makeText(getApplicationContext(), "NO Netwrok is connected!!!", Toast.LENGTH_LONG).show();
 	wificonn();
	 

 }    
        
           Start.setOnClickListener(new OnClickListener() {
        @Override
          public void onClick(View v) {
          	imm.hideSoftInputFromWindow(Speech.getWindowToken(), 0); 
        	if(isConnected()){
                Start.setBackgroundColor(Color.YELLOW); 
        		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
             
             // Original
  //            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
  //            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
              

 	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
 	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
 //	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
 	       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"es-US");
 	      // 
 	        
 	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
	                "Please speak now...");
 	        
 	        
              
              
              startActivityForResult(intent, REQUEST_CODE);
                     }
            else{
                Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
            }}
 
        });
}
    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo net = cm.getActiveNetworkInfo();
    if (net!=null && net.isAvailable() && net.isConnected()) {
        return true;
    } else {
        return false;
    }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
         Start.setBackgroundColor(Color.GREEN); 
         
     match_text_dialog = new Dialog(Speech10MainActivity.this);
     match_text_dialog.setContentView(R.layout.dialog_matches_frag);
     match_text_dialog.setTitle("Select Matching Text");
     textlist = (ListView)match_text_dialog.findViewById(R.id.list);
     matches_text = data
             .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
     matches_text.add(""); // last entry nothing - if U want ti ret null
     ArrayAdapter<String> adapter =    new ArrayAdapter<String>(this,
             android.R.layout.simple_list_item_1, matches_text);
     textlist.setAdapter(adapter);
     textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> parent, View view,
                             int position, long id) {
    	 selectedSpeech=matches_text.get(position);
    	 Speech.setText("You have said\n" +selectedSpeech);
         match_text_dialog.hide();
         Start.setBackgroundColor(Color.YELLOW);
         
     }
 });
     match_text_dialog.show();
 
     }
     super.onActivityResult(requestCode, resultCode, data);
    }


    ////////////////
    public static boolean isInternetConnected (Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }
    /////////////////////
	
    public void wificonn(){
    WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE); 
	 
	  if(!wifi.isWifiEnabled()){

		  
		  try{
		  wifi.setWifiEnabled(true);
	   Toast.makeText(getApplicationContext(),"WIFI is enabled now....   ", Toast.LENGTH_SHORT).show();
	  }
		  
	  //Log.d("WifiPreference", "enableNetwork returned " + b );

	           catch (Exception e) {

	              e.printStackTrace();
	          }
	 
	 

}  
	  }  
    /////////////////////////
	
    public void onBackPressed() 
	{
	Intent intentMessage_1=new Intent();
    // put the message to return as result in Intent
    intentMessage_1.putExtra("DICTATE_MESSAGE",selectedSpeech);
	// Set The Result in Intent
    setResult(GETDICATEDMESSAGE,intentMessage_1);
    if (selectedSpeech!=null) { 
    	if (selectedSpeech.length()!=0){  setResult(Activity.RESULT_OK,intentMessage_1);
    		}
     }
    
    
    Speech10MainActivity.this.finish();
		}
 
}