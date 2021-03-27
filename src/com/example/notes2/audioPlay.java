package com.example.notes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class audioPlay extends Activity   {
	
	private Context context=this;
	private Button startButton,pauseButton,clrButton,button_back;
    private TextView timerValue,status1,volstatus;
    private ImageView iv;
    private ProgressBar pb;
    SeekBar seekBar;
    
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    //private String audioFilePath=null;
    File[] files,filex;
    private boolean isPaused=false;
    MediaPlayer mediaPlayer;
    int length;
    boolean isAudioKilled=true;
    int file_exists_flag=0;
    int soundFileIndex=0;
    int mediaDuration=0;
    int seconds,msecs;
    private String dname1,fnamenn1,headerMail1,textMail1,mailaddr1;
    double screenPARAM;
    public getListofFileTypes getListFiles;
    ArrayList<String> RecFileList = new ArrayList<String>();
    private ActionBar actionBar;
    float startX,startY;
    float leftVolume=0.7f, rightVolume=0.7f;
    final float MAX_VOLUME=100.0f;
	private int width=0,height=0;
    int progressChangedValue = 50;

	 
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
   		setContentView(R.layout.audioplay1);    

		volstatus = (TextView) findViewById(R.id.volstatus1);	
		status1 = (TextView) findViewById(R.id.status1);	
		timerValue = (TextView) findViewById(R.id.timerValue);
		iv = (ImageView) findViewById(R.id.imageView12345);	
		startButton = (Button) findViewById(R.id.startButton);		
		clrButton = (Button) findViewById(R.id.clrButton);
		pauseButton = (Button) findViewById(R.id.pause100);
		button_back= (Button) findViewById(R.id.button_back);
		seekBar= (SeekBar) findViewById(R.id.seekBar12);

		
		
		
		pb=(ProgressBar) findViewById(R.id.simpleProgressBar); 
		pb.setBackgroundColor(Color.RED);
	
		Bundle extras = getIntent().getExtras();
		dname1 = extras.getString("Value1");   // dir name
		fnamenn1= extras.getString("Value4");  // file name
		headerMail1 = extras.getString("headerMail");   // mail header
		textMail1= extras.getString("textmail");  // mail text body
		mailaddr1= extras.getString("mailaddr");  // mail text body     
	      
			getListFiles=new getListofFileTypes();
		    RecFileList=getListofFileTypes.getListofFiles(dname1, fnamenn1, ".3gp",".3GP");
		   
		    actionBar = getActionBar();
			actionBar.setIcon(R.drawable.notes1);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle("Player ");
			showActionBar();
			updateStatus1();
		isAudioKilled=true;
		pauseButton.setEnabled(false);
		clrButton.setEnabled(false);
		button_back.setEnabled(true);		
		startButton.setEnabled(true);

			
	    startButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		        if (RecFileList.size()>0) {    	
		            	startButton.setEnabled(false);
		        		pauseButton.setEnabled(true);
		        		clrButton.setEnabled(true);		            	
		            	mediaPlayer = new MediaPlayer();
		            	mediaPlayer.setVolume(leftVolume, rightVolume);

String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
"/"+dname1+"/"+RecFileList.get(soundFileIndex) ;

File file = new File(filePath);
FileInputStream inputStream = null;
try {
	inputStream = new FileInputStream(file);
} catch (FileNotFoundException e2) {
	e2.printStackTrace();
}
 	 			        	 
			        			try {
			        				mediaPlayer.setDataSource(inputStream.getFD());
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
			        			
			        			
			        			try {
			        				inputStream.close();
			        			} catch (IOException e1) {
			        				e1.printStackTrace();
			        			}	
			        			
			        			try {
									mediaPlayer.prepare();
									mediaDuration=mediaPlayer.getDuration();
seconds=(int)(mediaDuration/1000);
msecs=(int)(mediaDuration-seconds*1000);
pb.setMax(mediaDuration);																		  									
									
									
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
			        			
			        			if(!isPaused) {
			        				isPaused=false;
//			        				startButton.setText("Start");
			        		    	startButton.setText("");
			        				startButton.setBackgroundResource(R.drawable.startplay);

			        				mediaPlayer.start();
			        			}
			        			else {
			        				isPaused=false;
			        		    	startButton.setText("");
			        				startButton.setBackgroundResource(R.drawable.contplay);
//			        				startButton.setText("Cont ");
			        				mediaPlayer.seekTo(length);
			        				mediaPlayer.start();
			        			}
			        			
			        			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

                                    // @Override
                                    public void onCompletion(MediaPlayer arg0) {
                                            // File has ended !!! 

Toast.makeText(getBaseContext(), "Media Completed Successfully", Toast.LENGTH_SHORT).show();
set_clear_actions();
                                    }
                            });
			        			
			        			
			        			isAudioKilled=false;		
		            	startTime = SystemClock.uptimeMillis();
		              customHandler.postDelayed(updateTimerThread, 0);
		            }  //  end if
		        else 
		        	Toast.makeText(getBaseContext(), "No file to play", Toast.LENGTH_SHORT).show();	
		        }
///////////////////////////////////////////////////
					
		        });
	
	    
	    
	    clrButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		            set_clear_actions();			            			              		              
		            	}
		        });	        
		
		        pauseButton.setOnClickListener(new View.OnClickListener() {
		
		            public void onClick(View view) {
		            	
		            	startButton.setEnabled(true);
		        		pauseButton.setEnabled(false);
		        		clrButton.setEnabled(true);
		        		isPaused=true;
		        		isAudioKilled=true;
//		        		startButton.setText("Cont ");
		            	startButton.setText("");
		        		startButton.setBackgroundResource(R.drawable.contplay);

		            	mediaPlayer.pause();
		            	length=mediaPlayer.getCurrentPosition();
		                timeSwapBuff += timeInMilliseconds;
		                customHandler.removeCallbacks(updateTimerThread);
		            }
		        });
		
		        
		        button_back.setOnClickListener(new View.OnClickListener() {
		               public void onClick(View view) {
		            	if (!isAudioKilled) {
			            	mediaPlayer.release();
			            	mediaPlayer = null;
		            	}		            	
		        		audioPlay.this.finish();	
//		        		System.exit(0);////////////////////////////
		            }
		        });
		        
		        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

		            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		                progressChangedValue = progress;
		            	volstatus.setText(""+progressChangedValue);		                

		            }

		            public void onStartTrackingTouch(SeekBar seekBar) {
		           
		            }

		            public void onStopTrackingTouch(SeekBar seekBar) {
	            	volstatus.setText(""+progressChangedValue);	
//	            	float c=(float)(progressChangedValue)/100;
	            	if (progressChangedValue==0) progressChangedValue =10;
//float log1=(float)( (Math.log(0.95)-c)/Math.log(0.97));
 	float log1=(float)(1-(Math.log(100-progressChangedValue)/Math.log(100)));	            	
		            	leftVolume=log1;
		            	rightVolume=log1;
		            	
//		            	float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
		            	
Toast.makeText(context, "Seek bar progress is :" + String.valueOf(leftVolume),
                        Toast.LENGTH_SHORT).show();

		            }
		        });
		        
		        
		        
	        
		    }
	
    
	/////////////////////////////////////////////////////////
	public void onWindowFocusChanged(boolean hasFocus) {

		if (width==0 || height==0) {
			screenPARAM=displ.getScreenInches();
			width=(int)displ.getScreenWidth();
			height=(int)displ.getScreenHeight();
	Toast.makeText(getBaseContext(),"screen diag = " +Double.toString(screenPARAM) , Toast.LENGTH_SHORT).show();
	 
			
			
	        float tsize=0;	        
			if (screenPARAM<5) tsize=getResources().getDimension(R.dimen.audiotp1) ;
			if (screenPARAM>5 && screenPARAM<6) tsize=getResources().getDimension(R.dimen.audiotp2) ;
			if (screenPARAM>6 && screenPARAM<7) tsize=getResources().getDimension(R.dimen.audiotp3) ;
			if (screenPARAM>7 && screenPARAM<8) tsize=getResources().getDimension(R.dimen.audiotp4) ;
			if (screenPARAM>8 && screenPARAM<9) tsize=getResources().getDimension(R.dimen.audiotp5) ;
			if (screenPARAM>9 && screenPARAM<10) tsize=getResources().getDimension(R.dimen.audiotp6) ;

			ViewGroup.MarginLayoutParams ipet1;
			int m=height/40;
			
			ipet1 =	(ViewGroup.MarginLayoutParams) timerValue.getLayoutParams();
			ipet1.width=(3*width)/4;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(width/8,	height/60,0,height/60);
			timerValue.requestLayout();
			timerValue.setBackgroundColor(Color.YELLOW);
			timerValue.setTextSize(tsize);
			timerValue.setTextColor(Color.BLACK);
			timerValue.setGravity(Gravity.CENTER);
			
			ipet1 =	(ViewGroup.MarginLayoutParams) status1.getLayoutParams();
			ipet1.width=(3*width)/4;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(width/8,	height/60,0,height/60);
			status1.requestLayout();
			status1.setBackgroundColor(Color.YELLOW);
			status1.setTextSize(tsize);
			status1.setTextColor(Color.BLACK);
			status1.setGravity(Gravity.CENTER);

			
			ipet1 =	(ViewGroup.MarginLayoutParams) startButton.getLayoutParams();
			ipet1.width=width/4;
			ipet1.height=(height*10)/100;
			ipet1.setMargins(0,	m,0,m);
			startButton.requestLayout(); //startButton,pauseButton,clrButton,button_back
//			startButton.setBackgroundColor(Color.YELLOW);

			
			ipet1 =	(ViewGroup.MarginLayoutParams) pauseButton.getLayoutParams();
			ipet1.width=width/4;
			ipet1.height=(height*10)/100;
			ipet1.setMargins(0,	m,0,m);
			pauseButton.requestLayout(); //startButton,pauseButton,clrButton,button_back
//			pauseButton.setBackgroundColor(Color.YELLOW);
			pauseButton.setBackgroundResource(R.drawable.pauseplay);	
			pauseButton.setText("");
			
			ipet1 =	(ViewGroup.MarginLayoutParams) clrButton.getLayoutParams();
			ipet1.width=width/4;
			ipet1.height=(height*10)/100;
			ipet1.setMargins(0,	m,0,m);
			clrButton.requestLayout(); //startButton,pauseButton,clrButton,button_back
//			clrButton.setBackgroundColor(Color.YELLOW);
			clrButton.setBackgroundResource(R.drawable.stopplay);
			clrButton.setText("");
			
			ipet1 =	(ViewGroup.MarginLayoutParams) button_back.getLayoutParams();
			ipet1.width=width/4;
			ipet1.height=(height*10)/100;
			ipet1.setMargins(width/2-width/8,	m,0,m);
			button_back.requestLayout(); //startButton,pauseButton,clrButton,button_back
//			button_back.setBackgroundColor(Color.YELLOW);
			button_back.setBackgroundResource(R.drawable.exitprogram);
			button_back.setText("");

			ipet1 =(ViewGroup.MarginLayoutParams) pb.getLayoutParams();
			ipet1.width=width;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(0,m,0,m);
			pb.requestLayout();
			
			
			ipet1 =(ViewGroup.MarginLayoutParams) iv.getLayoutParams();
			ipet1.width=width/4;
			ipet1.height=width/4;
			ipet1.setMargins(width/8,m,0,m);
			iv.requestLayout();
	
			ipet1 =(ViewGroup.MarginLayoutParams) seekBar.getLayoutParams();
			ipet1.width=75*width/100;
			ipet1.height=(height*6)/100;
			ipet1.setMargins(0,m,0,m);
			seekBar.requestLayout();
			seekBar.setBackgroundColor(Color.GREEN);
			
			ipet1 =(ViewGroup.MarginLayoutParams) volstatus.getLayoutParams();
			ipet1.width=20*width/100;
			ipet1.height=(height*6)/100;
			ipet1.setMargins(width/40,m,0,m);
			volstatus.requestLayout();
			volstatus.setBackgroundColor(Color.YELLOW);
			volstatus.setTextColor(Color.BLUE);
			volstatus.setGravity(Gravity.CENTER);
			volstatus.setTextSize(tsize);
			set_clear_actions();
		}

		super.onWindowFocusChanged(hasFocus);
	}
    
    
    
    
    
    
    
    
    private void set_clear_actions() {
    	pb.setProgress(0);	
    	startButton.setText("");
		startButton.setBackgroundResource(R.drawable.startplay);

    	isPaused=false;
    	startButton.setEnabled(true);
		pauseButton.setEnabled(false);
		clrButton.setEnabled(false);
		isAudioKilled=true;
		if (mediaPlayer!=null){
		mediaPlayer.release();
    	mediaPlayer = null;}
       	timeSwapBuff = 0L;
       	customHandler.removeCallbacks(updateTimerThread);
       	timerValue.setText("TIME:  " + "00:00:00");
       	volstatus.setText(""+progressChangedValue);	
	}  
		
		    private Runnable updateTimerThread = new Runnable() {
		        public void run() {
			
		            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
		            updatedTime = timeSwapBuff + timeInMilliseconds;
		            int secs = (int) (updatedTime / 1000);
		            int mins = secs / 60;
		            secs = secs % 60;
		            int milliseconds = (int) (updatedTime % 1000);
		            timerValue.setText("TIME:  " + mins + ":"
		            + String.format("%02d", secs) + ":"
		            + String.format("%03d", milliseconds)+ " / "+
		            String.valueOf(seconds)+"."+String.valueOf(msecs)+" Secs" );		          
		            pb.setProgress(secs*1000+milliseconds);		            
		            customHandler.postDelayed(this, 200);
		        }				
		    };
	
		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audioplay100, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		
		switch (id) {
		
	case R.id.next:
		soundFileIndex++;  
		if (  (soundFileIndex>RecFileList.size()-1) && RecFileList.size()>0)
		{
			soundFileIndex=0;
		}
		updateStatus1();
		showActionBar();	
		set_clear_actions();
		break;
		
	case R.id.previous:
		soundFileIndex--;  
		if (  (soundFileIndex<0 && RecFileList.size()>0)) {
			soundFileIndex=RecFileList.size()-1;
		}
		showActionBar();
		updateStatus1();
		set_clear_actions();
		break;
		
	case R.id.mail:

		ShareViaEmail(dname1,RecFileList.get(soundFileIndex));
		showActionBar();
		updateStatus1();
		break;			
		
		
case R.id.delaudio:
	
  	AlertDialog.Builder adb=new AlertDialog.Builder(audioPlay.this);
      adb.setTitle("Delete/Cancel?");
      adb.setMessage("Are you sure you want to Delete the file? " );
      adb.setNegativeButton("Cancel", null);
      adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
   
        	  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
        			  "/"+dname1+"/"+RecFileList.get(soundFileIndex) ;

        			  File file = new File(filePath);             	  
          	            	
          	boolean deleted = (file.delete());
          	soundFileIndex=0;
		    RecFileList=getListofFileTypes.getListofFiles(dname1, fnamenn1, ".3gp","3GP");
		    showActionBar();
		    updateStatus1();
          	}
          });
     
      	adb.show();	
		showActionBar();
		updateStatus1();
		break;	
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() 
	{
		if (mediaPlayer!=null){
		mediaPlayer.release();
    	mediaPlayer = null;}
 
		audioPlay.this.finish();
//		System.exit(0);//////////////////////////////////////////////
			}
	public void showActionBar(){

//		if (RecFileList.size()>0)
//			actionBar.setTitle("Sound memo: "+
//					String.valueOf(1+soundFileIndex)+"/"+String.valueOf(RecFileList.size())
//					+ " Vol="+String.valueOf( (int)(leftVolume*100))
//					) ;
//		else 
//			actionBar.setTitle("Sound memo: "+
//					String.valueOf(0)+"/"+String.valueOf(RecFileList.size())+"No file") ;
		actionBar.setTitle("Voice player");
	
	}
	
	
	// send mail with 3gp recorded sound
	private void ShareViaEmail(String folder_name, String file_name) {
	    try {
	    	File root = Environment.getExternalStorageDirectory();
	    	String pathToMyAttachedFile = folder_name+"/"+file_name;
	    	File file = new File(root, pathToMyAttachedFile);
	       	Uri uri = Uri.fromFile(file);
	    	Intent intent = new Intent(Intent.ACTION_SENDTO);
	        intent.setType("text/plain");
	        intent.putExtra(Intent.EXTRA_SUBJECT, headerMail1);
	        intent.putExtra(Intent.EXTRA_STREAM, uri);
	        intent.putExtra(Intent.EXTRA_TEXT, textMail1);
	        /////////////////////////////////////////////////////////////
	        intent.setData(Uri.parse(resolvemailAddresses(mailaddr1)));        
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	    } catch(Exception e)  {
	        System.out.println("is exception raises during sending mail"+e);
	    }
	}

	public String resolvemailAddresses (String mailaddr1){  //get mail address string from setup
		ArrayList<String> al = new ArrayList<String>();
    	String[] a=mailaddr1.split(",");
    	String mail_addr__="";
    	if (Integer.parseInt(a[3])==101) {
    		al.add(a[0]);
    	}
    	if (Integer.parseInt(a[4])==103) {
    		al.add(a[1]);
    	}
    	if (Integer.parseInt(a[5])==105) {
    		al.add(a[2]);
    	}
    	if (al.size()==1) {
    		mail_addr__="mailto:"+al.get(0);
    	}
    	else if (al.size()==2) {
    		mail_addr__="mailto:"+al.get(0)+","+al.get(1);
    	}	
    	else if (al.size()==3) {
    		mail_addr__="mailto:"+al.get(0)+","+al.get(1)+","+al.get(2);
    	}
		
		return mail_addr__; // return resolved
		
		
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getY();
			startX=event.getX();
			break;
		case MotionEvent.ACTION_UP: {
			float endY = event.getY();
			float endX = event.getX();
			
			float diffY=endY - startY ;
			float diffX=endX - startX;
	
			if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f) {
Toast.makeText(getApplicationContext(), "TOUCH", Toast.LENGTH_SHORT).show();	

}
			
			else if (Math.abs(diffY)> Math.abs(diffX) && endY < startY) {
 
//				float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//				yourMediaPlayer.setVolume(1-log1);
//
final float volume=(float) (1 - (Math.log(MAX_VOLUME - Math.abs(diffY/(10))) / Math.log(MAX_VOLUME)));
			
		leftVolume += volume;
		rightVolume += volume;
					if (leftVolume>0.95f){
						leftVolume=0.95f;
						rightVolume=0.95f;
					}
 Toast.makeText(getApplicationContext(), "Volume UP "+String.valueOf(Math.abs(leftVolume*100)), Toast.LENGTH_SHORT).show();	
 showActionBar();
			}

			else if (Math.abs(diffY)> Math.abs(diffX) && endY > startY) {
final float volume=(float) (1 - (Math.log(MAX_VOLUME - Math.abs(diffY/10)) / Math.log(MAX_VOLUME)));
				
	leftVolume -= volume;
	rightVolume -= volume;

		if (leftVolume<0){
			leftVolume=0.02f;
			rightVolume=0.02f;
		}
Toast.makeText(getApplicationContext(), "Volume DOWN "+String.valueOf(Math.abs(leftVolume*100)), Toast.LENGTH_SHORT).show();
showActionBar();


}	
	
			else if (Math.abs(diffY)< Math.abs(diffX) && endX > startX) {
Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();	
	soundFileIndex++;  
		if (  (soundFileIndex>RecFileList.size()-1) && RecFileList.size()>0)
			{
			soundFileIndex=0;
			}
		updateStatus1();
showActionBar();	
			}
			else {
				Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show();	
				soundFileIndex--;  
				if (  (soundFileIndex<0 && RecFileList.size()>0)) {
					soundFileIndex=RecFileList.size()-1;
				}
				showActionBar();
				updateStatus1();
			}
	    }
		
		
	
	}
		
		
		
		return false;
	}
	
	private void updateStatus1(){
	String s=dname1+"/"+ fnamenn1+"  ("  +
			String.valueOf(1+soundFileIndex)+"/"+String.valueOf(RecFileList.size()+")");
	status1.setText(s);
		
		
	}
	
}
