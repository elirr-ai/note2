package com.example.notes2;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class showFullMovie extends Activity implements View.OnTouchListener{
	
//	RelativeLayout layout1;
	ArrayList<String> al=new ArrayList<String>();
	Context context =this;
	private ActionBar actionBar;
	private int position = 0;
	private ProgressDialog progressDialog;
	private MediaController mediaControls;
	private String TAG="TAG>>>";
	SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor ;
	public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
  
    // keys
   
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String initialized = "INITIALIZED";
    public static final String volume = "VOLUME";
    public static final String screen = "SCREEN";
    public static final String indexMovies = "INDEX";

    
	static String Dir="";
	static String Fname="";
//	private boolean fullScreenSize=false;		
	int mWidthScreen,mHeightScreen;
	int mp4width,mp4height;	
	VideoView myVideoView;
	View vTopLeft,vTopRight,vVertLeft,vVertRight;
	AudioManager audioManager;
    final float MAX_VOLUME=100.0f;
    int volumeMax,volumeNow;
	float startX=0,startY=0;
	float leftVolume=0.4f,rightVolume=0.4f;
	RelativeLayout rl1;
	int width,height;
	int indexOfMovies=0;
	public getListofFileTypes getListFiles;
	private ArrayList<String> RecFileList = new ArrayList<String>();
	long   eventDownTime = 0,eventUptime=0;

	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		
		Bundle extras = getIntent().getExtras();
	      final String dname = extras.getString("Value1");   // dir name
	      final String fnameNN= extras.getString("Value4");  // file name

	      indexOfMovies=sharedpreferences.getInt(indexMovies, 0);
	      Dir=dname;
	     	Fname=fnameNN;
//			getListFiles=new getListofFileTypes();
		    RecFileList=getListofFileTypes.getListofFiles(Dir, Fname, ".mp4",".MP4");
		
//		Dir	= sharedpreferences.getString(directory, "");
//		Fname	= sharedpreferences.getString(file_name, "");

		if (sharedpreferences.getString(initialized, "").equals("")){
			editor.putString(initialized, "00000").commit();
			editor.putString(volume, String.valueOf(10)).commit();
			editor.putString(screen, "full").commit();
			editor.putInt(indexMovies, 0).commit();
			
			}
//		if (sharedpreferences.getString(screen, "").equals("")){
//			editor.putString(screen, "full").apply();
//			}
//		String g=sharedpreferences.getString(screen, "");
		
		String volume1=sharedpreferences.getString(volume, "");
//		boolean b=isDigits(volume1);
		if (isDigits(volume1)) volumeNow=Integer.valueOf(volume1);
		else {
			volumeNow=10;
			editor.putString(volume, String.valueOf(10)).commit();

			}
		if (RecFileList.isEmpty()){
		Toast.makeText(getApplicationContext(), "No files to play... ",
				Toast.LENGTH_LONG).show(); 
		showFullMovie.this.finish();	
		}
		else {
		if (indexOfMovies>=RecFileList.size()) { 
			indexOfMovies=0;
			editor.putInt(indexMovies, indexOfMovies).commit();
	
		}
//		if (newFile==-1) {
//			editor.putInt("new", 0).commit();  //not new anymore
//			indexOfMovies=0;newFile=0;
//			editor.putInt(indexMovies, indexOfMovies).commit();  //not new anymore
			
//		}
//indexOfMovies=sharedpreferences.getInt(indexMovies, 0);
		if (sharedpreferences.getString(screen,"").equals("full")) {
		setContentView(R.layout.fullvideoplayview);}
		else  {
		setContentView(R.layout.fullvideoplayview1);}
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		vTopLeft = (View) findViewById(R.id.vtopleft);
		vTopRight = (View) findViewById(R.id.vtopright);
		vVertLeft = (View) findViewById(R.id.vvertleft);
		vVertRight = (View) findViewById(R.id.vvertright);


//	View vTopLeft,vTopRight,vVertLeft,vVertRight;

		
		
		myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
		rl1.setOnTouchListener(this);
		
		
		//File path1 = new File(Environment.getExternalStorageDirectory(), "FileInputOutput");
		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		volumeMax=(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeNow = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		Display disp= getWindowManager().getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		mWidthScreen = size.x;
		mHeightScreen = size.y;
		


Toast.makeText(getApplicationContext(), "volume= "+
		sharedpreferences.getString(volume, ""),
		Toast.LENGTH_SHORT).show(); 
		
		//		String s23=getMetadataOfMP4();

		
//		int y=mp4height;
//		layout1 = new RelativeLayout(this);
//        layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        layout1.setId(66);
//        myVideoView = new VideoView(this);
//        RelativeLayout.LayoutParams params_myVideoView =
//        		new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 
//                LayoutParams.FILL_PARENT);
//        myVideoView.setLayoutParams(params_myVideoView);
//        myVideoView.setId(1000);
//        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
//        params_myVideoView.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        params_myVideoView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        params_myVideoView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        params_myVideoView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        params_myVideoView.height= 2600  ;
//        params_myVideoView.width= 1600  ;
//        params_myVideoView.setMargins(0,	0 , 0,0);
//        ((mHeightScreen*9)/10-mp4height)/2);  
        
//        layout1.addView(myVideoView);
       
//        myVideoView.setLayoutParams
//        (new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//
//        layout1.addView(myVideoView);
//       setContentView(layout1);
		
 //       layout1.setOnTouchListener(this);
        
        actionBar = getActionBar();
		actionBar.setIcon(R.drawable.viewimage);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("View "+"Dir= "+Dir+"   File= "+Fname+"  index= "+(1+indexOfMovies)+
				"/"+String.valueOf(RecFileList.size()));
		
		////////////

		/////////////

		
		
		
		
		// Get the layout from video_main.xml
		//setContentView(R.layout.activity_main);
///////////////////////////////////////////////////////////////////////////////
//		if (mediaControls == null) {
//			mediaControls = new MediaController(showFullMovie.this);
//		}
		
		mediaControls = new MediaController(context);
		mediaControls.show(8000);
		mediaControls.setAnchorView(myVideoView);
		myVideoView.setMediaController(mediaControls);
		
String	path12 = Environment.getExternalStorageDirectory().getAbsolutePath()+
				"/"+Dir+"/"+RecFileList.get(indexOfMovies) ;
//path11 = new File(Environment.getExternalStorageDirectory(), "/"+Dir+"/"+Fname);

		//		mediaControls.show(111770);
//		mediaControls.setVisibility(View.VISIBLE);
//		myVideoView.setMediaController(mediaControls);

		
//		myVideoView.setMediaController(new MediaController(this) {
//		    @Override
//		    public void hide()
//		    {
//		    	mediaControls.show();
//		    }
//
//		    }); 
//////////////////////////////////////////
		// Find your VideoView in your video_main.xml layout
		//myVideoView = (VideoView) findViewById(R.id.video_view);
		progressDialog = new ProgressDialog(showFullMovie.this);
		// Set progressbar title
		progressDialog.setTitle(" Video View Loading");
		// Set progressbar message
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		// Show progressbar
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(true);
		try {
			
			myVideoView.setVideoPath(path12.toString());
			
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}

		myVideoView.requestFocus();
		myVideoView.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				progressDialog.dismiss();
//String hhh=getMetadataOfMP4();
//				Toast.makeText(getApplicationContext(),
//						"BACKKKK !!! "+String.valueOf(mp4width)+
//						" "+String.valueOf(mp4height)+" "+layout1.getHeight()
//						, Toast.LENGTH_SHORT).show();	
//	if (mp4height>rl1.getHeight())	mp4height= rl1.getHeight();		
//	if (mp4width>rl1.getWidth())	mp4width= rl1.getWidth();		
				
				
				
//				ViewGroup.MarginLayoutParams p2 = 
//						(ViewGroup.MarginLayoutParams) myVideoView.getLayoutParams();
//						p2.setMargins((layout1.getWidth()-mp4width)/2,
//								(layout1.getHeight()-mp4height)/4,
//								0,0);
//						p2.height=mp4height;
//						p2.width=mp4width;
//						myVideoView.requestLayout();        		            
				
				myVideoView.seekTo(position);
				if (position == 0) {
					
					myVideoView.start();
				} else {
					myVideoView.pause();
				}
			}
		});
		}
		}
	

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
		myVideoView.pause();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		position = savedInstanceState.getInt("Position");
		myVideoView.seekTo(position);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showmovie1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		
				
		
		case R.id.full:
			myVideoView.stopPlayback();
			mediaControls.removeAllViews();
			mediaControls=null;
//			String gg=sharedpreferences.getString(screen,"");
			if (sharedpreferences.getString(screen,"").equals("full")){
				editor.putString(screen, "notfull").commit();
			}
			else {
				editor.putString(screen, "full").commit();

			}
				
				
				
				
//			if (sharedpreferences.getString(screen,"").equals("full")) {
//				editor.putString(screen, "notfull").commit();}
//				else  {
//					editor.putString(screen, "full").commit();}

			
			
//Toast.makeText(getApplicationContext(), "screen "+String.valueOf(fullScreenSize), Toast.LENGTH_SHORT).show(); 

//			editor.putBoolean(screen, !fullScreenSize).commit();
			recreate();
			break;	
			
				
		case R.id.exit:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
			showFullMovie.this.finish();
			break;
		case R.id.show:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
showDialogmsg();
			break;	

		case R.id.next:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 

			if (!RecFileList.isEmpty()){
			indexOfMovies++;
				if (indexOfMovies>RecFileList.size()-1) indexOfMovies=0;
				editor.putInt(indexMovies, indexOfMovies).commit();
				myVideoView.stopPlayback();
				mediaControls.removeAllViews();
				mediaControls=null;
				recreate();
			}
			break;
			
	
		case R.id.del:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
			RecFileList=getListofFileTypes.getListofFiles(Dir, Fname, ".mp4",".MP4");
			if (!RecFileList.isEmpty()){
				String	path12 = Environment.getExternalStorageDirectory().getAbsolutePath()+
						"/"+Dir+"/"+RecFileList.get(indexOfMovies) ;
				File file = new File(path12);
				boolean deleted = file.delete();
			    RecFileList=getListofFileTypes.getListofFiles(Dir, Fname, ".mp4",".MP4");
				indexOfMovies=0;
				editor.putInt(indexMovies, indexOfMovies).commit();
				myVideoView.stopPlayback();
				mediaControls.removeAllViews();
				mediaControls=null;
				recreate();
			}
			break;			
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		myVideoView.stopPlayback();
		mediaControls.removeAllViews();
		mediaControls=null;
		showFullMovie.this.finish();
		super.onBackPressed();
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
	 
	public void showDialogmsg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        //Uncomment the below code to Set the message and title from the strings.xml file  
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);  
          
        //Setting message manually and performing action on button click  
        
        builder.setMessage(getMetadataOfMP4())  
            .setCancelable(false)  
            
            .setNegativeButton("Done", new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int id) {  
                //  Action for 'NO' Button  
                dialog.cancel();  
             }  
            });  
  
        //Creating dialog box  
        AlertDialog alert = builder.create();  
        //Setting the title manually  
        alert.setTitle("File description");  
        alert.show();  
		
		
		
		
	}	
	
	
	public String getMetadataOfMP4(){
//		File file = new File(Environment.getExternalStorageDirectory(), "/"+Dir+"/"+Fname);
		 al.clear();
		String[] s11=new String[50];
		for (int i=0;i<s11.length;i++) {
			s11[i]="";
			}
		s11[5]="date/Time";
		s11[9]="Duration (mSec)";
		s11[12]="Type";
		s11[18]="width";
		s11[19]="height";
		s11[20]="frames";
		s11[43]="Audio sampling rate";
		s11[44]="audio bits/sample";
	
		String s22="";
		        if (!RecFileList.isEmpty()) {
		        	String	path12 = Environment.getExternalStorageDirectory().getAbsolutePath()+
		    				"/"+Dir+"/"+RecFileList.get(0) ;
		            Log.i(TAG, ".mp4 file Exist");

		            //Added in API level 10
		            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		            try {
		                retriever.setDataSource(path12);
		                for (int i = 0; i < 100; i++){
		                     //only Metadata != null is printed!
		                    if(retriever.extractMetadata(i)!=null) {

		                    	al.add(retriever.extractMetadata(i));
		                    	s22+="\n\r"+String.valueOf(i)+" : \t"+
		                    			s11[i]+"\t"+
		                    			retriever.extractMetadata(i);
if (s11[i].equals("width"))	mp4width=Integer.parseInt(retriever.extractMetadata(i));
if (s11[i].equals("height")) mp4height=Integer.parseInt(retriever.extractMetadata(i));
		                    	
		                    	
		                    	
		                        Log.i(TAG, "Metadata :: " + retriever.extractMetadata(i));
		                    }

		                }
		                s22+="\n\r------------------------------------------------------------------------------";

		                s22+="\n\r\n\rFile name: "+Fname;
		                s22+="\n\rDir name: "+Dir;

		                for(int i=0;i<RecFileList.size();i++){
		                	String s10="";
		                	if (i==indexOfMovies) s10="  <<<<";
		                	String s10a="";
		                	if (i<9) s10a="0";
			                s22+="\n\r("+s10a+String.valueOf(1+i)+") REC file name: "+RecFileList.get(i)+s10;
                	
		                }
		                
		                
		                
		                
		                
		                
                
//Toast.makeText(getApplicationContext(), "BACK !!! "+mp4width+
//		" "+mp4height
//		, Toast.LENGTH_SHORT).show();		


		               
		            }catch (Exception e){
		                Log.e(TAG, "Exception : " + e.getMessage());
		            }

		        }else {
		            Log.e(TAG, ".mp4 file doesn´t exist.");
		        }	
		return s22;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
float yu=0,xu=0;

	       switch(v.getId()){
           case R.id.rl1:  // layout1 id

               switch (event.getAction()) {
                   case MotionEvent.ACTION_DOWN:
           			startY = event.getY();
        			startX=event.getX();
        			eventDownTime=event.getEventTime();
                       break;
                   case MotionEvent.ACTION_MOVE:
 
                       break;
                   case MotionEvent.ACTION_UP:
                        xu =  event.getX();
                        yu =  event.getY();
//                        eventUptime=event.getEventTime();
//           if ((event.getEventTime()-eventDownTime)>500){
//      	   Toast.makeText(getBaseContext(),"LONG CLICK",  Toast.LENGTH_SHORT).show();  
//
//           }
           if ( (event.getEventTime()-eventDownTime)>900 && Math.abs(startY-yu)<rl1.getHeight()/24
        		 &&  Math.abs(startX-xu)<rl1.getWidth()/24 && (xu>0) && (xu<rl1.getWidth()/6)
        		 && (yu>0) && (yu<rl1.getHeight()/6) ) {            
          	   Toast.makeText(getBaseContext(),"screen change",  Toast.LENGTH_SHORT).show(); 
   			myVideoView.stopPlayback();
   			mediaControls.removeAllViews();
   			mediaControls=null;
   			if (sharedpreferences.getString(screen,"").equals("full")){
   				editor.putString(screen, "notfull").commit();
   			}
   			else {
   				editor.putString(screen, "full").commit();

   			}
   			recreate();
            }         
  
           if ( (event.getEventTime()-eventDownTime)>900 && Math.abs(startY-yu)<rl1.getHeight()/24
        		 &&  Math.abs(startX-xu)<rl1.getWidth()/24 && (xu<rl1.getWidth()) && (xu>5*(rl1.getWidth()/6))
        		 && (yu>0) && (yu<(rl1.getHeight()/6)) ) {            
        	   showDialogmsg();
            }   
           
           
           
if (Math.abs(startY-yu)>rl1.getHeight()/12){
		if (startY-yu >0 && Math.abs(startX-xu)<120 ){
		volumeNow++;
		if (volumeNow>volumeMax) volumeNow=volumeMax; 
						}
		if (startY-yu <0 && Math.abs(startX-xu)<120 ){
			volumeNow--;
			if (volumeNow<0) volumeNow=0; 
		}                       
			Toast.makeText(getApplicationContext(), "Volume= "+(volumeNow)+" / "+volumeMax
    		   , Toast.LENGTH_SHORT).show(); 
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeNow, 0);
			editor.putString(volume, String.valueOf(volumeNow)).apply();    
			}                   
 
if (Math.abs(startX-xu)>rl1.getWidth()/12){
	if (xu-startX >0 && Math.abs(startY-yu)<120 ){

		Toast.makeText(getApplicationContext(), "Next record"
				
		   , Toast.LENGTH_SHORT).show(); 
		if (!RecFileList.isEmpty()){
		indexOfMovies++;
			if (indexOfMovies>RecFileList.size()-1) indexOfMovies=0;
			editor.putInt(indexMovies, indexOfMovies).commit();
			myVideoView.stopPlayback();
			mediaControls.removeAllViews();
			mediaControls=null;
			recreate();
								}
		
					}

			}  

///////////////////////////////////
if (Math.abs(startX-xu)>rl1.getWidth()/12){
	if (xu-startX <0 && Math.abs(startY-yu)<120 ){

		Toast.makeText(getApplicationContext(), "Deleted File"  , Toast.LENGTH_SHORT).show(); 
		RecFileList=getListofFileTypes.getListofFiles(Dir, Fname, ".mp4",".MP4");
		if (!RecFileList.isEmpty()){
			String	path12 = Environment.getExternalStorageDirectory().getAbsolutePath()+
					"/"+Dir+"/"+RecFileList.get(indexOfMovies) ;
			File file = new File(path12);
			boolean deleted = file.delete();
		    RecFileList=getListofFileTypes.getListofFiles(Dir, Fname, ".mp4",".MP4");
			indexOfMovies=0;
			editor.putInt(indexMovies, indexOfMovies).commit();
			myVideoView.stopPlayback();
			mediaControls.removeAllViews();
			mediaControls=null;
			recreate();
		}
		
					}

			}  







break;  //switch events
 /////////////////////
                 
   //                                       break;  //switch events                       
 ////////////////
               
               
               
               
               }
               break;  //case 1
           case 2:
               //do stuff for button 2
               break;
           case 3:
               //do stuff for button 3
               break;
           case 4:
               //do stuff for button 4
               break;
       }
		
		
		
		///////
		
		
		


        return true;
	}
	
	///////////////
	public void onWindowFocusChanged(boolean hasFocus) {

		if (width==0 || height==0) {
			
//	        float tsize=0;
//			displ disp=new displ();
//			double d=disp.screenInches;

	width=rl1.getMeasuredWidth();
	height=rl1.getMeasuredHeight();

	ViewGroup.MarginLayoutParams ipetA =(ViewGroup.MarginLayoutParams) vTopLeft.getLayoutParams();
			ipetA.width=rl1.getWidth()/6;
			vTopLeft.requestLayout();	
			
	ViewGroup.MarginLayoutParams ipetB =(ViewGroup.MarginLayoutParams) vTopRight.getLayoutParams();
					ipetB.width=rl1.getWidth()/6;
					vTopRight.requestLayout();		
	
	ViewGroup.MarginLayoutParams ipetC =(ViewGroup.MarginLayoutParams) vVertLeft.getLayoutParams();
					ipetC.height=rl1.getHeight()/6;
					vVertLeft.requestLayout();	
					
	ViewGroup.MarginLayoutParams ipetD =(ViewGroup.MarginLayoutParams) vVertRight.getLayoutParams();
							ipetD.height=rl1.getHeight()/6;
							vVertRight.requestLayout();	
	
	
	
	
	
	
if (!sharedpreferences.getString(screen, "").equals("full")) {
getMetadataOfMP4();
Toast.makeText(getBaseContext(),"onWindowsMP4 = "+mp4width+"  "+mp4height,  Toast.LENGTH_SHORT).show();  
ViewGroup.MarginLayoutParams ipet1 = 
			(ViewGroup.MarginLayoutParams) myVideoView.getLayoutParams();
			ipet1.width=mp4width;
			ipet1.height=mp4height;
			ipet1.setMargins(width/2-mp4width/2,   //left
					height/2-mp4height/2,    //top
					0,0);
			myVideoView.requestLayout();
			
			
			
			
			
			
		}
	}	
		super.onWindowFocusChanged(hasFocus);
	}
	/////////////////////
	
	
	
	public boolean isDigits(String number){ 
		   if(!TextUtils.isEmpty(number)){
		       return TextUtils.isDigitsOnly(number);
		   }else{
		       return false;
		   }
		}
	
	
	
}


//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 

/////////////////////////////////////
//MP4 metadata content:
// 5 -creation date
// 9 duration mSec
// 12 type Video/mp4
//18 width
//19 height
//20 Total numbers of frames
//43- audio sample rate per Sec
//44 WAV sampling bits
////////////////////////////////////////

//screen - long click left - switch full/small screen
// screen long click right - show info
// screen swap right go to next movie
// screen up - volume up
//screen down - volume down






