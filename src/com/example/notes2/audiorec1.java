package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.CircularRedirectException;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class audiorec1   extends Activity {

	int width=0 ,height=0;
	private TextView textHeader;   
	Button stop,record,back;
	RelativeLayout ll;
	ImageView iv;
	   private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   private String dname1,fnameNNN;
	   private String recFname;
	   
	   boolean isRecording=false;
	   
	   private long startTime = 0L;
	    private Handler customHandler = new Handler();
	    long timeInMilliseconds = 0L;
	    long timeSwapBuff = 0L;
	    long updatedTime = 0L;
	   
//	    public getListofFileTypes getListFiles;
	    public dateFrom1970 df1970;
	    ArrayList<String> RecFileList = new ArrayList<String>();
	    VisualizerrecView visualizerView;
	    private int circleCounter=0;
	    
	    
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.audiorec2);
	      
//	      getListFiles=new getListofFileTypes();
	      df1970=new dateFrom1970();
	      
	      stop=(Button)findViewById(R.id.button2);
	      record=(Button)findViewById(R.id.button);
	      back=(Button)findViewById(R.id.button3);
//	      tv1=(TextView)findViewById(R.id.tv1);
	      textHeader=(TextView)findViewById(R.id.textView);
	      iv=(ImageView)findViewById(R.id.imageView);
	      visualizerView = (VisualizerrecView) findViewById(R.id.visualrec);
	      ll = (RelativeLayout) findViewById(R.id.ll); 
	   
	      Bundle extras = getIntent().getExtras();
	      final String dname = extras.getString("Value1");
	      final String fnameNN= extras.getString("Value4");
	      dname1=dname;
	      fnameNNN=fnameNN;
	      
	      stop.setEnabled(false);
//	      tv1.setText("" + "00:00:00"  + "" );
	      
	      //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
	
	      //Toast.makeText(getBaseContext(),"outputFile.... " +outputFile , Toast.LENGTH_SHORT).show();
	      prep_audio_rec();
	      textHeader.setText("Audio recording: \n\r"+recFname);
	      
	    //  RecFileList=getListFiles.getListofFiles(dname, fnameNN, ".JPG");
	
	      record.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 prep_audio_rec();
	        	 visualizerView.clear();
	        	 circleCounter=0;
	           	 isRecording=true;	        	 
	            try {
	               myAudioRecorder.prepare();
	               myAudioRecorder.start();
	            }
	            
	            catch (IllegalStateException e) {
	               e.printStackTrace();
	            }
	            
	            catch (IOException e) {
	               e.printStackTrace();
	            }
	            
	            record.setEnabled(false);
	            stop.setEnabled(true);
	            
	            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
	            startTime = SystemClock.uptimeMillis();
	              customHandler.postDelayed(updateTimerThread, 0);
	         }
	      });
	      
	      stop.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 visualizerView.setCircle(0);
	        	 isRecording=false;
	            myAudioRecorder.stop();
	            myAudioRecorder.release();
	            myAudioRecorder  = null;
	            
	            stop.setEnabled(false);
	            record.setEnabled(true);
	            
	            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
	            timeSwapBuff = 0L;
	              customHandler.removeCallbacks(updateTimerThread);
	              //tv1.setText("" + "00:00:00"  + "" );
	            
	         }
	      });
	      
	      
	      back.setOnClickListener(new View.OnClickListener() {
		         @Override
		         public void onClick(View v) {
		            if (isRecording) { 
		            	
		        		if (isRecording) { 
     			
		        	       	 visualizerView.setCircle(0);
		        	       	 isRecording=false;
		        	           myAudioRecorder.stop();
		        	           myAudioRecorder.release();
		        	           myAudioRecorder  = null;
		        	           
		        	           stop.setEnabled(false);
		        	           record.setEnabled(true);
		        	           
		        	           Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
		        	           timeSwapBuff = 0L;
		        	             customHandler.removeCallbacks(updateTimerThread);

		        	  	            } 
		        	           Toast.makeText(getApplicationContext(), "Exiting... ",Toast.LENGTH_LONG).show();
		        	           audiorec1.this.finish();	            	

		   	            } 
		            
		            Toast.makeText(getApplicationContext(), "Exiting... ",Toast.LENGTH_LONG).show();
		            audiorec1.this.finish();
		         }
		      });
	      
	      
	   }
	   
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {

			if (width==0 || height==0) {
				
		        float tsize=0;
				double d=displ.getScreenInches();
				if (d<5) tsize=getResources().getDimension(R.dimen.textsize1) ;
				if (d>5 && d<6) tsize=getResources().getDimension(R.dimen.textsize1) ;
				if (d>6 && d<7) tsize=getResources().getDimension(R.dimen.textsize2) ;
				if (d>7 && d<8) tsize=getResources().getDimension(R.dimen.textsize2) ;
				if (d>8 && d<9) tsize=getResources().getDimension(R.dimen.textsize3) ;
				if (d>9 && d<10) tsize=getResources().getDimension(R.dimen.textsize4) ;

		//width=ll.getWidth();
		width=ll.getMeasuredWidth();
		height=ll.getMeasuredHeight();
//		visualizerView.setWidth(width);
//		visualizerView.setHeight(height);

		//int hh=ll.getHeight();
	Toast.makeText(getBaseContext(),"onWindows = "+width+"  "+height,  Toast.LENGTH_SHORT).show();    

	ViewGroup.MarginLayoutParams ipet1;
		ipet1 = (ViewGroup.MarginLayoutParams) visualizerView.getLayoutParams();
				ipet1.width=(ll.getWidth()*60)/60;
				ipet1.height=(ll.getHeight()*25)/60;
				ipet1.setMargins(ll.getWidth()*1/ll.getWidth(),   //left
						ll.getHeight()/30,    //top
						0,0);
				visualizerView.requestLayout();

/*				ViewGroup.MarginLayoutParams ptv1 = 
						(ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
				ptv1.width=(ll.getWidth()*20)/60;
				ptv1.height=ll.getHeight()/28;
				ptv1.setMargins(ll.getWidth()*40/100,   //left
						ll.getHeight()/30,    //top
								0,0);
				tv1.setTextSize(tsize);
				tv1.requestLayout();  // time counter
*/
				ViewGroup.MarginLayoutParams ptv2 = 
						(ViewGroup.MarginLayoutParams) textHeader.getLayoutParams();
				ptv2.width=(ll.getWidth()*60)/60;
				ptv2.height=ll.getHeight()/12;
				ptv2.setMargins((ll.getWidth()*1)/ll.getWidth(),   //left
								ll.getHeight()/120,    //top
								0,0);
				textHeader.setTextSize(tsize);
				textHeader.requestLayout();
				
				ViewGroup.MarginLayoutParams iv1 = 
						(ViewGroup.MarginLayoutParams) iv.getLayoutParams();
				iv1.width=(ll.getWidth()*63)/100;
				iv1.height=ll.getHeight()/6;
				iv1.setMargins(ll.getWidth()*16/100,   //left
						ll.getHeight()/16,    //top
								0,0);
				iv.requestLayout();
				
				ViewGroup.MarginLayoutParams btn_exit = 
						(ViewGroup.MarginLayoutParams) back.getLayoutParams();
				btn_exit.width=(ll.getWidth());
				btn_exit.height=ll.getHeight()/12;
				btn_exit.setMargins(0,   //left
						0,    //top
								0,ll.getHeight()/480);
				back.requestLayout();		
				back.setTextSize(tsize);
				back.setBackgroundColor(Color.CYAN);
				
				ViewGroup.MarginLayoutParams btn_rec = 
						(ViewGroup.MarginLayoutParams) record.getLayoutParams();
				btn_rec.width=(ll.getWidth()*30/100);
				btn_rec.height=ll.getWidth()*30/100;
//				btn_rec.height=ll.getHeight()/14;
				btn_rec.setMargins(0,   //left
						height=ll.getHeight()/16,    //top
								0,0);
				record.requestLayout();		
					
				record.setBackgroundResource(R.drawable.startrec1);
				
				ViewGroup.MarginLayoutParams btn_stop = 
						(ViewGroup.MarginLayoutParams) stop.getLayoutParams();
				btn_stop.width=(ll.getWidth()*30/100);
				btn_stop.height=ll.getWidth()*30/100;
				btn_stop.setMargins(  (ll.getWidth()-ll.getWidth()*38/100),   //left
						height=ll.getHeight()/16,    //top
								0,0);
				stop.requestLayout();		
				stop.setBackgroundResource(R.drawable.stoprec1);

				visualizerView.setCircle(0);
			}
			super.onWindowFocusChanged(hasFocus);
		}

	   
	   private void prep_audio_rec() {
		
		   myAudioRecorder=new MediaRecorder();
		      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	         
		      recFname =fnameNNN+"_"+df1970.getmillisString()+".3gp"; 
	 	      outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
	 	    		  "/"+dname1+"/"+recFname;
		    
	 	      myAudioRecorder.setOutputFile(outputFile);
	   	   			}

	private Runnable updateTimerThread = new Runnable() {
	        public void run() {
		
	            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
	            updatedTime = timeSwapBuff + timeInMilliseconds;
	            int secs = (int) (updatedTime / 1000);
	            int mins = secs / 60;
	            secs = secs % 60;
	            int milliseconds = (int) (updatedTime % 1000);
//	            tv1.setText("" + mins + ":"
//	            + String.format("%02d", secs) + ":"
//	            + String.format("%03d", milliseconds));

	            visualizerView.addAmplitude(myAudioRecorder.getMaxAmplitude());//get max value of audio+update the VisualizeView
	            visualizerView.showTime("" + mins + ":"
	            		+ String.format("%02d", secs) + ":"   + String.format("%03d", milliseconds));
	            circleCounter++;
				 if (circleCounter>=0 && circleCounter<30) {
					 visualizerView.setCircle(1);
				 			}
				 else if (circleCounter>40 && circleCounter<=60){
					 visualizerView.setCircle(2);	 
				 			}
				 else if (circleCounter>60) circleCounter=0;
	           customHandler.postDelayed(this, 40);
	        }
			
	    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audiorec100, menu);
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
	public void onBackPressed() 
	{
		if (isRecording) { 
			
       	 visualizerView.setCircle(0);
       	 isRecording=false;
           myAudioRecorder.stop();
           myAudioRecorder.release();
           myAudioRecorder  = null;
           
           stop.setEnabled(false);
           record.setEnabled(true);
           
           Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
           timeSwapBuff = 0L;
             customHandler.removeCallbacks(updateTimerThread);
  
  	            } 
           Toast.makeText(getApplicationContext(), "Exiting... ",Toast.LENGTH_LONG).show();
           audiorec1.this.finish();
			}

}
