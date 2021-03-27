package com.example.notes2 ;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GestureDetectSimpleMainActivity extends Activity implements OnGesturePerformedListener,
	OnClickListener, OnGestureListener{

	private GestureLibrary mLibrary;
	private TextView tvtop;
//	private final static String DIR="GestureLib";
	private final static String FILE="gestureaa.txt";
	private StringBuilder sb=new StringBuilder();
	private Button btadd,bttest;
	private GestureOverlayView gestures=null;
	private String gestureDir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_detect_activity_main);
		tvtop = (TextView) findViewById(R.id.tvtop);

		btadd = (Button) findViewById(R.id.btadd);
		bttest = (Button) findViewById(R.id.bttest);
		btadd.setBackgroundColor(Color.GREEN);
		bttest.setBackgroundColor(Color.CYAN);
		
		
		btadd.setOnClickListener(this);
		bttest.setOnClickListener(this);
		
		gestureDir = getIntent().getStringExtra("DIR1");
		
//		tv = (TextView) findViewById(R.id.prediction);
//		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gesture);
		
		File rootPath = new File(Environment.getExternalStorageDirectory(), gestureDir);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
	    File dataFile = new File(rootPath, FILE);
		mLibrary = GestureLibraries.fromFile(dataFile);
	boolean bload=mLibrary.load();
		
		
		if (!bload) finish();
		
		 Set<String> setStr = mLibrary.getGestureEntries();
		 List<String> list2 = new ArrayList<String>(setStr);
		 StringBuilder sb= new StringBuilder();
		for (int i=0;i<list2.size();i+=6){
			for (int j=0;j<6;j++){
				if (i+j<list2.size()){
					sb.append(list2.get(i+j)+" , ");
					}			
			} 
			sb.append("\n");
		}
//		tv.setText(sb.toString());
		
		gestures =(GestureOverlayView) findViewById(R.id.gestures);
		gestures.setBackgroundResource(R.color.Beige);
		gestures.addOnGesturePerformedListener(this);
		gestures.addOnGestureListener(this);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesturedetectmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
		String predList = "";
		NumberFormat formatter = new DecimalFormat("#0.00");
		if (predictions.size()>0) {
			sb.append(predictions.get(0));
//		    Toast.makeText(this, "recognize size "+predictions.size()+"  "+ predictions.get(0)
//		    		, Toast.LENGTH_SHORT).show();

			tvtop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green72x72, 0, 0, 0);

		    
		}
		else {
//		    Toast.makeText(this, "Could not recognize !!!!!", Toast.LENGTH_SHORT).show();

		}
		for(int i=0; i<predictions.size(); i++) {
		Prediction prediction = predictions.get(i);

		predList = predList + prediction.name + " "
		+ formatter.format(prediction.score) + "\n";
		}
//		tv.setText(predList);
		tvtop.setText(sb.toString());
	}

	
	
	@Override
	public void onClick(View v) {
int id=v.getId();
if (id==R.id.btadd){
//    Toast.makeText(this, "btadd pressed", Toast.LENGTH_SHORT).show();

    Intent data = new Intent(); 
    data.putExtra("MESSAGE",sb.toString());
    data.putExtra("STATUS","OK");
    setResult(RESULT_OK,data);
    gestures.removeAllOnGestureListeners();
    gestures.removeAllOnGesturePerformedListeners();
    gestures.removeAllOnGesturingListeners();   
    finish();
    
    
    
}
if (id==R.id.bttest){  // delete last char
	
	if (sb.length()>0){
		sb.deleteCharAt(sb.length()-1);
		tvtop.setText(sb.toString());
	}
	
	
//    Toast.makeText(this, "bttest pressed", Toast.LENGTH_SHORT).show();

}
		
		
	}

	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		tvtop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blue70x070, 0, 0, 0);
		
	}

	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		
	}

	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
//	    Toast.makeText(this, "onGestureEnded", Toast.LENGTH_SHORT).show();
		tvtop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle, 0, 0, 0);

	}

	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		
	}

	@Override
	public void onBackPressed() {

		
	    Intent data = new Intent(); 
	    data.putExtra("MESSAGE",sb.toString());
	    data.putExtra("STATUS","OK");
	    setResult(RESULT_OK,data);
	    gestures.removeAllOnGestureListeners();
	    gestures.removeAllOnGesturePerformedListeners();
	    gestures.removeAllOnGesturingListeners();   
	    finish();
		super.onBackPressed();
		
	}
	
	
	
	
	
}
