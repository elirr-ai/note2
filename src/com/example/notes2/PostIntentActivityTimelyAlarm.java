package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PostIntentActivityTimelyAlarm extends Activity {

	final static String MYPREFERNCES="MyPrefs";
	int width=0,height=0;
	RelativeLayout rl1;
	TextView tv1,tv2,tv3;
	ImageView iv;
	
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
		setContentView(R.layout.postalarmactivity);
		Context context=this;
		rl1=(RelativeLayout)findViewById(R.id.rlpostalarm);
		tv1=(TextView)findViewById(R.id.tvpost1);
		tv2=(TextView)findViewById(R.id.tvpost2);
		tv3=(TextView)findViewById(R.id.tvpostnote3);
		tv1.setBackgroundColor(Color.CYAN);
		tv2.setBackgroundColor(Color.GREEN);
		tv3.setBackgroundColor(Color.YELLOW);
		iv=(ImageView)findViewById(R.id.imgv1);
		
		SharedPreferences sp = getSharedPreferences(MYPREFERNCES, Context.MODE_PRIVATE);

		  ArrayList<Note>  alarmSet1 = new ArrayList<Note>();		
		  
		  try {
			  alarmSet1 = (ArrayList<Note>) ObjectSerializer.deserialize(sp.getString("ALARMNOTENOTE",
					  ObjectSerializer.serialize(new ArrayList<Note>())));
			  } catch (IOException e) {
			    e.printStackTrace();
			  } 
		
   		int idx=Integer.valueOf(alarmSet1.get(0).getPriority());
 		int i=context.getResources().getIdentifier("ic_launcher", "drawable", context.getPackageName());
 		
 		try {
			if (idx==1) i=context.getResources().getIdentifier("ucritical", "drawable", context.getPackageName());
			if (idx==2) i=context.getResources().getIdentifier("uimportant", "drawable", context.getPackageName());
			if (idx==3) i=context.getResources().getIdentifier("uusual", "drawable", context.getPackageName());
			if (idx==4) i=context.getResources().getIdentifier("ucasual", "drawable", context.getPackageName());
			if (idx==5) i=context.getResources().getIdentifier("ulow", "drawable", context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
 		iv.setImageResource(i);	  
 		tv1.setText(alarmSet1.get(0).getDate());
 		tv2.setText(alarmSet1.get(0).getMemo_header());
 		tv3.setText(alarmSet1.get(0).getMemoBody());		
 		
//	  
//	  Toast.makeText(getApplicationContext(), 
//	    "Do Something NOW  "+rec11, 
//	    Toast.LENGTH_LONG).show();
	 }


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (width==0 || height==0) {			

			width=rl1.getMeasuredWidth();
			height=rl1.getMeasuredHeight();
			ViewGroup.MarginLayoutParams ipet1;
			
			ipet1 = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
			ipet1.width=(2*width)/3;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(width/100, 	0,  0,0);
			tv1.requestLayout();
			
			ipet1 = (ViewGroup.MarginLayoutParams) tv2.getLayoutParams();
			ipet1.width=(2*width)/3;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(width/100, 	0,  0,0);
			tv2.requestLayout();
			
			ipet1 = (ViewGroup.MarginLayoutParams) tv3.getLayoutParams();
			ipet1.width=(3*width)/3;
			ipet1.height=(height*80)/100;
			ipet1.setMargins(width/100, 	0,  0,0);
			tv3.requestLayout();		
			
			ipet1 = (ViewGroup.MarginLayoutParams) iv.getLayoutParams();
			ipet1.width=(15*width)/90;
			ipet1.height=(height*12)/100;
			ipet1.setMargins(0, 	0,  0,0);
			iv.requestLayout();
			
		super.onWindowFocusChanged(hasFocus);
			}
		}
	 
	 
	 

	}
