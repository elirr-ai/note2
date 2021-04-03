package com.example.notes2;
///////////113
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityTwo<sendWhatsAppMsg> extends Activity implements View.OnClickListener,
	SharedPreferences.OnSharedPreferenceChangeListener{
//  implements AdapterView.OnItemSelectedListener
Activity activity=this;
boolean netwokExists=false;
	String[] importance = { "NC ","CRT", "IMP", "ORD", "CAS", "Low"  };
	Integer[] images = { 0, R.drawable.ucritical32, R.drawable.uimportant32,
			R.drawable.uusual32, R.drawable.ucasual32,R.drawable.ulow32 };
	public TextView tv,tvpics,tvsound,tvhnd1,tvchar,tvvid,almchar;
	public EditText et22;
	public Spinner spin;
	public ImageButton ImageView1,ImageView2,ImageView3,vidcam,sendall,alarm2;
	public ImageView ImageView4;
	public static final String MainDirName = "MainDirName";
    public static final String BackupDirName = "BackupDirName";
    public static final String DeletedDirName = "DeletedDirName";
    public static final String MailAddress = "MailAddress";
    public static final String SMSnumber = "SMSnumber";
    public static final String SoundMusic = "SoundMusic";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCESALARM = "MyPrefsa"  ;
	public static final String TextSize = "TextSize";
	public static final String FULLFILENAMEANDPATH = "FULLFILENAMEANDPATH";
	public static final String COLORTABLE = "COLORTABLE";
	public static final String COLORTABLECOUNT = "COLORTABLECOLORTABLECOUNT";	
	final static String ALARMNOTEPOSITION="ALARMNOTEPOSITION";
	final static String ALARMNOTENOTEHM="ALARMNOTENOTEHM";

    String picn1="00";
    

	String undo_buffer="";
	int data_block=200,j;
	String CRLF="\r\n";
	String Memo="";
	String header="";
	String dnameNN= "memo_files";
	String[] error_list = {
    		"Saved...",
    		"Can not use storage",
    		"Voice memo not found or could not be deleted",
    		"Voice memo deleted succesfully",
    		"Voice memo not found",
    		"Picture not found",
    		"Voice command not found"
    		  };	
	
	boolean shw; // true: show custom toast, false: do not show
	
	String mainDir_ ="";
    String backupDir_ ="";
    String deletedDir_ ="";
    String mailaddr_ ="";
    String smsNumer_ ="";
    String TextSize_ ="";
	String memo_fname="";
	String memobody1="",memopriority1="",memodate1="";
	TextToSpeech t1;
	int spinner_position;
	int pictures_max_number;
	SharedPreferences sharedpreferences, sharedpreferencesAlarm;
	public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
	public static final int GETDICATEDMESSAGE = 1788;
	public static final int BROWSE_ACTIVITY_REQUEST_CODE = 168;
	public static final int SPEECH_RECOGNITION = 1679;
	public static final int GESTUREACTIVITY = 1879;
	public static final int REQUEST_CODE_BARCODE = 0x0000c0de; // Only use bottom 16 bits

    public static final String ROTATECAMERAANGEL = "ROTATECAMERAANGEL";
	final static String alarmOnOfStatus="alarmOnOfStatus";
	
	boolean flag_can_make_more_pics=false;
//	private ActionBar actionBar;
	PopupMenu popup;
	 FileReadString fileread;
	 FileWriteString filewrite;
//	 private dateFrom1970 dt1970;
	 private long eventDown;
	 static int mWidthScreen;
	 static int mHeightScreen;
	 GetScreenHieghtWidth g1;
	 Context context=this;
	 public getListofFileTypes getListFiles;
	 public dateFrom1970 df1970;
	 public Note mynt;
	 Date dt;
	 
	 int pointerText=0;
	 float startY ;
	 float startX ;
	 InputMethodManager imm;
	 boolean isKeyBoardShow=true;

	 int initCharCount;
	 private boolean textLengthChanged=false;
	 int initPriorityState;
	 int priorityStateChanged=0;
	 int last_ET_Length=0;
	 
	 long firstBackPressedTime=-1;
	 long lastKeyboardPressed,lastDualFingersPressed;
	 public String xzxzxzxzxzxzxzxzx;
	 
	 private nAsyncTask atask;
	 
	 private RelativeLayout rl1;
	 private int width=0,height=0;
	 
	 serializeListArray1 sr;
	 Note n8;
	 private List<Note> myNotesX = new  ArrayList<Note>();
	 private String dname,serFileName;
	 private int position_marker;
//color
	 private ArrayList<TextColorsHolder> colorArrayList= new ArrayList<TextColorsHolder>();
	 private ArrayList<TextColorsHolder> colorArrayListBack= new ArrayList<TextColorsHolder>();
	 private int colorTextStart,colorTextEnd;	

// for debug only of text change listener...........................	 
private CharSequence csDebug;
private int startDebug,countDebug,beforeDebug;	 
private int LEN_Before,LEN_After;	 
private String pasteFromClipboard="";	 
private int clipCopyStartSelection=0;	 
	 
	 
	//////////////temps///////////////// 
	 int temph,tempw;
	 
	 
long timeStampForMove;
boolean firstTimeEnteretdmove=false;
int moveCounter;

private static final int INVALID_POINTER_ID = -1;
private int ptrID1, ptrID2;
private float  fY,  sY;
private int angleDialog=0;

private Button ib;
String message = "";

	  @Override
	  public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
		activity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    mynt=new Note();
	    colorArrayList.clear();
	    colorArrayListBack.clear();
       	imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
	    Display disp= getWindowManager().getDefaultDisplay();
	    GetScreenHieghtWidth g1 = new GetScreenHieghtWidth(disp);
		mWidthScreen=g1.getmWidthScreen();
	    mHeightScreen=g1.getmHeightScreen();
	    
	    lastKeyboardPressed=System.currentTimeMillis();
	    lastDualFingersPressed=System.currentTimeMillis();
	    
	    netwokExists= GetNetworkConnecionStatus.isNetworkAvailable(context) &&
	    		GetNetworkConnecionStatus.isInternetAvailable();
	    
	    
	    
//this.getActionBar().setDisplayShowTitleEnabled(false);   
this.getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
getActionBar().setDisplayShowCustomEnabled(true);
getActionBar().setCustomView(R.layout.actiobartwo);
//        getActionBar().setElevation(0);

        View view = getActionBar().getCustomView();
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);       
      
        ImageView ivmenu = (ImageView) view.findViewById(R.id.imageactionbar2);       
        TextView name = (TextView) view.findViewById(R.id.tvactionbar2);
//        name.setGravity(Gravity.CENTER);
        name.setTextColor(Color.WHITE);
//        name.setBackgroundColor(Color.YELLOW);
        name.setText("MEMO");
        
        ib = (Button) view.findViewById(R.id.imagebuttonactbar2);        
        final Bitmap[] bi=new Bitmap[5];
        bi[0]=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
        		R.drawable.notes1),1);       
        bi[1]=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
        		R.drawable.dicatateactionbar2),1);
        bi[2]=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
        		R.drawable.dicatateactionbar2no),1);
        bi[3]=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
        		R.drawable.dicatateactionbar2grey),1);
        bi[4]=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
        		R.drawable.dicatateactionbar2red),1);
        ivmenu.setImageBitmap(bi[0]);  
        ivmenu.setScaleType(ScaleType.FIT_XY);
           
        if (netwokExists) ib.setBackground(new BitmapDrawable(getResources(), bi[1]));
        else ib.setBackground(new BitmapDrawable(getResources(), bi[3]));
       
		ViewGroup.MarginLayoutParams ipet1;  
		
		 ipet1 = 	(ViewGroup.MarginLayoutParams) ll.getLayoutParams();
			ipet1.width=60*mWidthScreen/100;
			ipet1.height=5*mHeightScreen/100;
//			ipet1.setMargins(0,0,0,0);
			ll.requestLayout();

			 ipet1 = 	(ViewGroup.MarginLayoutParams) ivmenu.getLayoutParams();
				ipet1.width=12*mWidthScreen/100;
				ipet1.height=5*mHeightScreen/100;
//				ipet1.setMargins(0,0,0,0);
				ivmenu.requestLayout();
			
	 ipet1 = 	(ViewGroup.MarginLayoutParams) name.getLayoutParams();
		ipet1.width=12*mWidthScreen/100;
		ipet1.height=5*mHeightScreen/100;
	 //				ipet1.width=4*bi[0].getWidth()/5;
//				ipet1.height=3*bi[0].getHeight()/3;
//				ipet1.setMargins(0,0,0,0);
				name.requestLayout();
				
				ipet1 =(ViewGroup.MarginLayoutParams) ib.getLayoutParams();
				ipet1.width=12*mWidthScreen/100;
				ipet1.height=5*mHeightScreen/100;
//						ipet1.setMargins(0,0,0,0);
						ib.requestLayout();
						
        
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 
            	if (netwokExists){
            		ib.setBackground(new BitmapDrawable(getResources(), bi[4]));
            		Intent speech = new Intent(getApplicationContext(), Speech2textRecognitionMainActivity.class);
            		startActivityForResult(speech, SPEECH_RECOGNITION);
            		
            	}
            	else {
            		Toast.makeText(ActivityTwo.this, "Network not connected - Pls connect", Toast.LENGTH_LONG).show();
 //           		ib.setBackground(new BitmapDrawable(getResources(), bi[4]));
            	}         	
//Toast.makeText(ActivityTwo.this, "You have clicked tittle", Toast.LENGTH_LONG).show();
//Intent speech = new Intent(getApplicationContext(), Speech2textRecognitionMainActivity.class);



            }
        });

        
	    
	    setContentView(R.layout.activity_two);
	    rl1=(RelativeLayout)findViewById(R.id.rl1);
	    et22=(EditText)findViewById(R.id.et22);
	    tv=(TextView)findViewById(R.id.textView1);
	    tvpics=(TextView)findViewById(R.id.tvp1);
	    tvsound=(TextView)findViewById(R.id.tvs1);
	    tvvid=(TextView)findViewById(R.id.tvvid);
	    ImageView1=(ImageButton)findViewById(R.id.imageView1);
	    ImageView2=(ImageButton)findViewById(R.id.imageView2);
	    sendall=(ImageButton)findViewById(R.id.sendall);
	    alarm2=(ImageButton)findViewById(R.id.alarm2);
	    almchar=(TextView)findViewById(R.id.almchar);
	    
	    
	    try {
			ImageView3=(ImageButton)findViewById(R.id.imageView3);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	    try {
			ImageView4=(ImageView)findViewById(R.id.imageView4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    vidcam=(ImageButton)findViewById(R.id.vidcam);
	    spin = (Spinner) findViewById(R.id.spinner1);
	    tvhnd1=(TextView)findViewById(R.id.tvhnd1);
	    tvchar=(TextView)findViewById(R.id.tvchar);

	    ImageView1.setBackgroundColor(Color.YELLOW);
	    ImageView2.setBackgroundColor(Color.YELLOW);
	    ImageView3.setBackgroundColor(Color.YELLOW);
	    ImageView4.setBackgroundColor(Color.YELLOW);
	    vidcam.setBackgroundColor(Color.YELLOW);
	    sendall.setBackgroundColor(Color.YELLOW);
	    alarm2.setBackgroundColor(Color.YELLOW);
	    
	    ImageView1.setOnClickListener(this);
	    ImageView2.setOnClickListener(this);
	    ImageView3.setOnClickListener(this);
//	    ImageView4.setOnClickListener(this);
	    vidcam.setOnClickListener(this);	    
	    sendall.setOnClickListener(this);	    
	    alarm2.setOnClickListener(this);	    
	    
	    ImageView1.setScaleType(ScaleType.CENTER);
	    ImageView2.setScaleType(ScaleType.CENTER);
	    ImageView3.setScaleType(ScaleType.CENTER);
	    vidcam.setScaleType(ScaleType.CENTER);	    
		sendall.setScaleType(ScaleType.CENTER);
		alarm2.setScaleType(ScaleType.CENTER);

	    
	    
	    et22.setMovementMethod(new ScrollingMovementMethod());    
	    et22.setScrollbarFadingEnabled(false);
	    
	    
	    
	    
	    
	    
	    
	    

// set cursor ////////////////////////////////////////////////////////	    
	    try {
	        java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
	        f.setAccessible(true);
	        f.set(et22, R.drawable.cursoret);
	    } catch (Exception ignored) {
	    }
//////////////////////////////////////////////////////////////////	    
	    //OK.setBackgroundColor(Color.parseColor("#ff8a80"));
	  //  Save.setBackgroundColor(Color.parseColor("#66bb6a"));
	    	
	    ImageView1.setVisibility(View.VISIBLE);
	    ImageView2.setVisibility(View.VISIBLE);
	    ImageView3.setVisibility(View.VISIBLE);
	    ImageView4.setVisibility(View.VISIBLE);
	    vidcam.setVisibility(View.VISIBLE);
	    
	    
	    et22.addTextChangedListener(new TextWatcher() {

	    	   public void afterTextChanged(Editable s) {
	    	LEN_After=s.toString().length();
	    	if (LEN_After-LEN_Before>0 && pointerText<LEN_After){
	    		pointerText++; updateTVCHAR();
	    	}
	    	if (LEN_After-LEN_Before<0 && pointerText>0){
	    		pointerText--; updateTVCHAR();
	    	}	   
	    	updateTVCHAR();
	    	   }

	    	   public void beforeTextChanged(CharSequence s, int start,
	    	     int count, int after) {
	    		   LEN_Before=s.toString().length();
	    	   }

	    	   public void onTextChanged(CharSequence s, int start,
	    	     int before, int count) {
csDebug=s;startDebug=start;countDebug=count;beforeDebug=before;	    		   
//	    		   tvchar.setText(String.valueOf(1+pointerText)+"/"+String.valueOf(et22.length())); 
	    		   updateTVCHAR();	    		   
	    		   if (et22.length()!=initCharCount) { // used to see if text was changed
	    			   textLengthChanged=true;  // used as boolean to show text was changed
	    		   }
	    	   		}
	    	  });
	    	
        et22.setOnTouchListener(new OnTouchListener() {        
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               	String a100=et22.getText().toString()+"\n";
 //           	et22.setText(a100);
            	ArrayList<Integer> al =new ArrayList<Integer>();    	
            	final ArrayList<String> sal =new ArrayList<String>();    	
            	
            	
            	switch (event.getAction() & MotionEvent.ACTION_MASK) {
            	
                case MotionEvent.ACTION_DOWN:
                	ptrID1 = event.getPointerId(event.getActionIndex());
                   	timeStampForMove=System.currentTimeMillis();
                   	firstTimeEnteretdmove=true;
//                   	int moveCounter;

                	startY = event.getY();
    				startX=event.getX();
    				eventDown=event.getDownTime();

               for (int i=0;i<et22.getText().toString().length();i++){
            	   if (et22.getText().toString().substring(i, i+1).equals("\n")) {
            		   al.add(i);
            	   	}
               } 
			     for (int i=0;i<al.size();i++){
			    	 if (i==0){
			    		 sal.add(et22.getText().toString().substring(0, al.get(0)));
			    		 }
			    		 else {
			    			 sal.add(et22.getText().toString().substring(al.get(i-1)+1, al.get(i)));
			    		 	}
			     		}          
 if (sal.isEmpty() && et22.getText().toString().length()>0){
	 sal.add(et22.getText().toString());
 }  
 
                    break;
       
                case MotionEvent.ACTION_POINTER_DOWN:
                    ptrID2 = event.getPointerId(event.getActionIndex());
//                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
 //                   fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
//tv.setText("2 f "+sX+" "+sY+" "+fX+" "+fY);
//	Toast.makeText(getApplicationContext(), "2 fingers", Toast.LENGTH_SHORT).show();	
                    break;
                    
                    
                case MotionEvent.ACTION_MOVE:
                    break;
                    
                case MotionEvent.ACTION_UP:
//                	ptrID1 = INVALID_POINTER_ID;
        			float endY = event.getY();
    				float endX = event.getX();
    				float diffY=endY - startY ;
    				float diffX=endX - startX;
    		    	al=findHowManyCRinText();
 
    			    if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f && (event.getEventTime()-eventDown>1500)) {// long press
   	Toast.makeText(getApplicationContext(), "TOUCH LONG  ", Toast.LENGTH_SHORT).show();	

   	AlertDialog.Builder builder = new AlertDialog.Builder(context);
   	builder.setTitle("Copy to clipBoard");
   	final String s0=et22.getText().toString()+"\n";
    for (int i=0;i<s0.length();i++){
  	   if (s0.substring(i, i+1).equals("\n")) {
  		   al.add(i);
  	   	}
     } 
 	     for (int i=0;i<al.size();i++){
 	    	 if (i==0){
 	    		 sal.add(s0.substring(0, al.get(0)));
 	    		 }
 	    		 else {
 	    			 sal.add(s0.substring(al.get(i-1)+1, al.get(i)));
 	    		 	}
 	     		}          
 if (sal.isEmpty() && s0.length()>0){
 sal.add(s0);
 }    
 if (sal.get(sal.size()-1).equals("")) sal.remove(sal.size()-1);
 sal.add("Copy line/whole text");
 sal.add("Paste from ClipBoard"+"  '"+getClip()+"'");
  String[] textStrings=new String[sal.size()];
  textStrings = sal.toArray(textStrings);
   	builder.setItems(textStrings, new DialogInterface.OnClickListener() {
   	@Override
   	public void onClick(DialogInterface dialog, int which) {
 		if (which==sal.size()-1){
 			et22SetSelection();	
 		copyClip();
 		}
 		else if (which==sal.size()-2){
 			et22SetSelection();	
   	setClipboard(context, s0.substring(0, s0.length()-1));/////////////?????????????????
//   	Toast.makeText(getApplicationContext(), "Strung is    "+which+"/"+et22.getText().toString(), Toast.LENGTH_SHORT).show();	
   	}
   	else {
   		et22SetSelection();	
   	   	setClipboard(context, sal.get(which));   		
   	}
//   	Toast.makeText(getApplicationContext(), "Strung is    "+which+"/"+sal.get(which), Toast.LENGTH_SHORT).show();	
   			}
   	});
   	// create and show the alert dialog
   	AlertDialog dialog = builder.create();
   	dialog.show();
   	
   	
//   	setClipboard(context, et22.getText().toString()); 
//	Toast.makeText(getApplicationContext(), "Copied to Clipboard !!!@@@@@@", Toast.LENGTH_SHORT).show(); 

    			    }  ////////////////  end of long touch  ///////////////////////
    				
    				
    				
   else if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f && (event.getEventTime()-eventDown<1500)) {  // click to ena/dis keyboard
if (System.currentTimeMillis()-lastKeyboardPressed>900){
	lastKeyboardPressed=System.currentTimeMillis();

	   if (isKeyBoardShow){
    	 imm.showSoftInput(et22, 0);
    	 last_ET_Length=et22.getText().toString().length();
    	 }
     else{
    	 imm.hideSoftInputFromWindow(et22.getWindowToken(), 0);
    	 int lst1=pointerText;//push 
//    	 pointerText+=et22.length()-last_ET_Length;
//    	 pointerText=lst1+(et22.getText().toString().length()-last_ET_Length)-(et22.getText().toString().length()-last_ET_Length);

    	 if (pointerText>et22.getText().toString().length() || pointerText<0) pointerText=lst1;// restore if needed to avoid error
    	 }
    isKeyBoardShow=!isKeyBoardShow;	
    updateTVCHAR();
    et22.requestFocus();
		}
    }
    				
    				else if (Math.abs(diffY)> Math.abs(diffX) && endY < startY) {/// handle up move
//    	al=findHowManyCRinText();
    	pointerText=FindtextPointerLocation(al);
    	updateTVCHAR();
    	et22SetSelection();
    				}

    				else if (Math.abs(diffY)> Math.abs(diffX) && endY > startY) {  // handle down
    	pointerText=positionTextInMiddleOfLIne(al);
    	et22SetSelection();
    	updateTVCHAR();	
    				}	
 
    			    
                	if (firstTimeEnteretdmove && Math.abs(diffY)< Math.abs(diffX) && endX > startX) {  // H right move
                		firstTimeEnteretdmove=false;
  if (TextColorsHolder.colorEnabled){ 					
            			colorArrayList.add(new TextColorsHolder(colorTextStart, pointerText));			
   }
                     		pointerText+=getDeltaX(endX, startX);
                        	if (pointerText>et22.getText().length()) pointerText=et22.getText().toString().length();
                     	   if (TextColorsHolder.colorEnabled){ 	    	
                    		colorArrayList.add(new TextColorsHolder(colorTextStart, pointerText));			
                    		et22.setText(parseFromColorsArray (colorArrayList));      
                        	   }
                        	et22SetSelection();	
                        	updateTVCHAR();
                        	saveColorTable(colorArrayList);
                		}
               	
                	if (firstTimeEnteretdmove && Math.abs(diffY)< Math.abs(diffX) && endX < startX) {  // H left move
                		firstTimeEnteretdmove=false;
                     		pointerText-=getDeltaX(endX, startX);;
                        	if (pointerText<0) pointerText=0;
                        	et22SetSelection();	
                        	updateTVCHAR();
                		}   			    
    			    
    			    
    			    
    			    
    			    
    			    
    			    
    			    
    			    
//    				else if (Math.abs(diffY)< Math.abs(diffX) && endX > startX) { // handle right
//
// 	Toast.makeText(getApplicationContext(), "right   ", Toast.LENGTH_SHORT).show();
//  if (TextColorsHolder.colorEnabled){ 					
//			colorArrayList.add(new TextColorsHolder(colorTextStart, pointerText));			
//   }
//    	pointerText+=getDelta(endX,startX);
//    	if (pointerText>et22.getText().length()) pointerText=et22.getText().length();
//    	   if (TextColorsHolder.colorEnabled){ 	    	
//		colorArrayList.add(new TextColorsHolder(colorTextStart, pointerText));			
//		et22.setText(parseFromColorsArray (colorArrayList));      
//    	   }
//    	et22SetSelection();	
//    	updateTVCHAR();
//    	saveColorTable(colorArrayList);
//    				}
//    				else {  // handle left
//    				   	if (pointerText>et22.getText().length()) pointerText=et22.getText().length(); 
//    					pointerText-=getDelta(endX,startX);
//    			    	if (pointerText<0){ 
//    			    		pointerText=0;
//    			    	}
//    			    	et22SetSelection();
//    			    	updateTVCHAR();
//    				}
 	
                    break;
                
                case MotionEvent.ACTION_POINTER_UP:
                	
                	if (System.currentTimeMillis()-lastDualFingersPressed>900){
                		lastDualFingersPressed=System.currentTimeMillis();
                	
                	float d1 = 0,d2 = 0;
                    if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
                        float nfX, nfY, nsX, nsY;
                        nsX = event.getX(event.findPointerIndex(ptrID1));
                        nsY = event.getY(event.findPointerIndex(ptrID1));
                        nfX = event.getX(event.findPointerIndex(ptrID2));
                        nfY = event.getY(event.findPointerIndex(ptrID2));
                        d1=sY-nsY;d2=fY-nfY;    
                    }
                	ptrID1 = INVALID_POINTER_ID;
                    ptrID2 = INVALID_POINTER_ID;
                     if (d1>0 && d2>0 ){
                    	 if (et22.getText().toString().length()>10 ) {
                    		 pointerText=2;
                    	 et22SetSelection();
                    	 }
//                    	 Toast.makeText(getApplicationContext(), "pointer up  UP ", Toast.LENGTH_SHORT).show();
                     }

                     if (d1<0 && d2<0 ){
//                    	 Toast.makeText(getApplicationContext(), "pointer up  DOWN ", Toast.LENGTH_SHORT).show();
                    	 if (et22.getText().toString().length()>10 ) {
                    		 pointerText=et22.getText().toString().length()-2;
                    	 et22SetSelection();
                    	 }
                     }
                     
                     
//Toast.makeText(getApplicationContext(), "pointer up   ", Toast.LENGTH_SHORT).show();
                	}
                     
                     
                     
                     
                     
                    break;
                    
                case MotionEvent.ACTION_CANCEL:
                    ptrID1 = INVALID_POINTER_ID;
                    ptrID2 = INVALID_POINTER_ID;
                    break;
                    
                default:
                    break;
                }
                return true;

            }





        });
        
        
        
	    
	    
	    //spin.setOnItemSelectedListener(this);
	    spin.setAdapter(new MyAdapter(ActivityTwo.this, R.layout.custom,importance));
	    
////	    actionBar = getActionBar();
//		actionBar.setIcon(R.drawable.notes1);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle("Memo");
	    
		fileread = new FileReadString();
		filewrite = new FileWriteString();
//		 dt1970=new dateFrom1970();
//		 dateMillis=dt1970.getmillisString();
	    //sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    df1970=new dateFrom1970();
	    //txt to speech
//	    t1 = new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {	    		    	
//			@Override
//			public void onInit(int status) {
//				if (status !=TextToSpeech.ERROR) {
//					t1.setLanguage(Locale.UK);
//				}
//			}
//		});
	    
	    Intent intent =this.getIntent(); 
	    ///////////////
	   dname= intent.getStringExtra("DIRNAME");
	   serFileName= intent.getStringExtra("SERFILENAME");
	   position_marker=intent.getIntExtra("POSITION_MARKER1", 0);
//      	i.putExtra("DIRNAME", DNAME);
//      	i.putExtra("SERFILENAME", serFileName);
//      	i.putExtra("POSITION_MARKER1", POSITION_MARKER);
	    
	  mynt= (Note)intent.getSerializableExtra("keyClass") ; 
	  memodate1=mynt.getDate();
	  memopriority1=mynt.getPriority();
	  memo_fname=mynt.getMemo_header();
	  memobody1=mynt.getMemoBody();
	  initCharCount=memobody1.length();
	  initPriorityState=Integer.valueOf(memopriority1);
	  getListFiles=new getListofFileTypes();
	  
  	List<Note> l = new ArrayList<Note>();
    sr =new serializeListArray1();
	 l=sr.readSerializedObject(dname, serFileName);
       n8=new Note();
       myNotesX.clear();
      for (int i=0;i<l.size();i++){
           	   n8=l.get(i);
              	if (position_marker == i  ) n8.setlast_note("last");
              	else n8.setlast_note("not_last");
           	   myNotesX.add(n8);
      }
	  
	  
	  
	  
    /////////////////////////////////////////////////////////////////////////////////////////////

    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    sharedpreferencesAlarm = getSharedPreferences(MyPREFERENCESALARM,Context.MODE_PRIVATE);
    sharedpreferences.registerOnSharedPreferenceChangeListener(this);
//    sharedpreferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
//		
//		@Override
//		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences1, String key) {
//	if (sharedPreferences1.contains(alarmOnOfStatus))
//			Toast.makeText(ActivityTwo.this, "SP chnaged......  ...", Toast.LENGTH_SHORT).show(); 
//		
//		}
//	});
    
    read_all_prefs(); 

    /////////////////////////////////////////////////////////////////////////////////////////////
	//ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,importance);
	//aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
    et22.setText(memobody1);
    //et22.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Float.parseFloat(TextSize_));  //set text size
    et22.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Float.parseFloat(TextSize_));  //set text size
  //Toast.makeText(getBaseContext(),"etxt size ....   " +TextSize_+"" , Toast.LENGTH_SHORT).show();
    add_prefs_key("memo_text", et22.getText().toString());
    loadColorTable();     
    
    
    undo_buffer=et22.getText().toString();
    //tv.setBackgroundColor(Color.parseColor("#E91E63")); 
    //String[] fname_sep = fname.split("_{1}|\\.{1}");
    String ttab="";
   
    //Toast.makeText(getBaseContext(),"j1= " +Integer.toString(j1) , Toast.LENGTH_SHORT).show();
    for ( int j=0 ;j < get_basic_tab_seed(mWidthScreen)-memo_fname.length(); j++) {
    	ttab=ttab+" ";	}
   
    header="Memo: "+memo_fname+ttab+" Date "+memodate1.substring(0, 14);
    //header="Memo: "+memo_fname+"   "+Integer.toString(mHeightScreen)+"/"+Integer.toString(mWidthScreen)+" Date "+a23[0].substring(0, 14);

    double dbl=get_screen_params();
   // Toast.makeText(getBaseContext(),"screen diag = " +Double.toString(dbl) , Toast.LENGTH_SHORT).show();
	if (dbl>5 && dbl <6){
		tv.setTextSize(14.0f);
	}
	else if (dbl>7 && dbl <8){
		tv.setTextSize(23.0f);
	}
	else if (dbl>8.0 && dbl <9.9){
		tv.setTextSize(25.0f);
	}
	else {
		tv.setTextSize(26.0f);
	}
    
    
    
    
  // tv.setTextSize(17.0f);
 
    tv.setText(header);
    
    //Toast.makeText(getBaseContext(),"COLOR.... " +a23[1] , Toast.LENGTH_SHORT).show();
    
    
    show();

        
    spin.setOnItemSelectedListener(
            new OnItemSelectedListener() {
                public void onItemSelected(
                        AdapterView<?> parent, View view, int position, long id) {
              
                	
                	
                	spinner_position=position;
                	//Toast.makeText(getBaseContext(),spin.getSelectedItem().toString()+" "+ Integer.toString(spinner_position), Toast.LENGTH_SHORT).show();
                	memopriority1=String.valueOf(spinner_position);
if (spinner_position!=0 && spinner_position!=initPriorityState ) priorityStateChanged++; 
//Toast.makeText(getBaseContext(),"a23 "+memopriority1+" "+String.valueOf(initPriorityState)+" <> ",
//Toast.LENGTH_SHORT).show();
                	  
                	show();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                	
                	//Toast.makeText(getBaseContext(),"a24" , Toast.LENGTH_SHORT).show();
                }
            });
    
  }
	    



	/////////////////////////////////////////////////////////
		public void onWindowFocusChanged(boolean hasFocus) {

			if (width==0 || height==0) {
				
		        float tsize=0;
				double d=displ.getScreenInches();
				if (d<5) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
				if (d>5 && d<6) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
				if (d>6 && d<7) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
				if (d>7 && d<8) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
				if (d>8 && d<9) tsize=getResources().getDimension(R.dimen.textsize3cam) ;
				if (d>9 && d<10) tsize=getResources().getDimension(R.dimen.textsize3cam) ;

		width=rl1.getMeasuredWidth();
		height=rl1.getMeasuredHeight();


		ViewGroup.MarginLayoutParams ipet1 = 
				(ViewGroup.MarginLayoutParams) tv.getLayoutParams();
				ipet1.width=width;
				ipet1.height=(height*4)/100;
				ipet1.setMargins(0,   //left
						0,    //top
						0,0);
				tv.requestLayout();
				///////////////////
				 int m=width/36;int j1=width/13;int k=(height*8)/100;int off=width/70;
				 ipet1 =(ViewGroup.MarginLayoutParams) vidcam.getLayoutParams();
					ipet1.width=60*j1/50;
						ipet1.height=k;
						ipet1.setMargins(0*j1+0*m+off,0,0,0);
						vidcam.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
								R.drawable.vidrec28x28), j1,k));
				
				vidcam.requestLayout();

				ipet1 =(ViewGroup.MarginLayoutParams) tvvid.getLayoutParams();
//				ipet1.width=j1;
//				ipet1.height=k;
				ipet1.setMargins(0*j1+0*m+off,0,0,0);
				tvvid.requestLayout();
				
				 ipet1 =(ViewGroup.MarginLayoutParams) ImageView3.getLayoutParams();
							ipet1.width=60*j1/50;
							ipet1.height=k;
							ipet1.setMargins(1*j1+1*m+off,0,0,0);
//							ipet1.setMargins(0,   //left
//									0,    //top
//									0,0);
			ImageView3.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
						R.drawable.hand2), j1,k));

				ImageView3.requestLayout();	

				ipet1 =(ViewGroup.MarginLayoutParams) tvhnd1.getLayoutParams();
//				ipet1.width=j1;
//				ipet1.height=k;
				ipet1.setMargins(1*j1+1*m+off,0,0,0);
				tvhnd1.requestLayout();						

				
				
				 ipet1 =(ViewGroup.MarginLayoutParams) ImageView2.getLayoutParams();
					ipet1.width=60*j1/50;
					ipet1.height=k;
					ipet1.setMargins(2*j1+2*m+off,0,0,0);
					ImageView2.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
							R.drawable.camera1a), j1,k));
					ImageView2.requestLayout();	
					
					ipet1 =(ViewGroup.MarginLayoutParams) tvpics.getLayoutParams();
//					ipet1.width=j1;
//					ipet1.height=k;
					ipet1.setMargins(2*j1+2*m+off,0,0,0);
					tvpics.requestLayout();						
					
				
			ipet1 =(ViewGroup.MarginLayoutParams) ImageView1.getLayoutParams();
			ipet1.width=60*j1/50;
			ipet1.height=k;
			ipet1.setMargins(3*j1+3*m+off,0,0,0);
			ImageView1.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
					R.drawable.tape2), j1,k));		
			ImageView1.requestLayout();	
			
			ipet1 =(ViewGroup.MarginLayoutParams) tvsound.getLayoutParams();
//			ipet1.width=j1;
//			ipet1.height=k;
			ipet1.setMargins(3*j1+3*m+off,0,0,0);
			tvsound.requestLayout();	
///////////
			ipet1 =(ViewGroup.MarginLayoutParams) sendall.getLayoutParams();
			ipet1.width=60*j1/50;
			ipet1.height=k;
			ipet1.setMargins(5*j1+5*m+off,0,0,0);///

			sendall.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
					R.drawable.send28x28), j1,k));
			sendall.requestLayout();	
/////			
			ipet1 =(ViewGroup.MarginLayoutParams) alarm2.getLayoutParams();
			ipet1.width=60*j1/50;
			ipet1.height=k;
			ipet1.setMargins(4*j1+4*m+off,0,0,0);///

			alarm2.setImageDrawable(getDrawable1(BitmapFactory.decodeResource(getResources(),
					R.drawable.alarm28x28), j1,k));
			alarm2.requestLayout();		

			
			ipet1 =(ViewGroup.MarginLayoutParams) almchar.getLayoutParams();
//			ipet1.width=j1;
//			ipet1.height=k;
			ipet1.setMargins(4*j1+4*m+off,0,0,0);
			almchar.requestLayout();	
			
////////////
			
			 ipet1 =(ViewGroup.MarginLayoutParams) ImageView4.getLayoutParams();
				ipet1.width=2*j1;
				ipet1.height=k;
				ipet1.setMargins(6*j1+6*m+off,0,0,0);
//				ipet1.setMargins(0,   //left
//						0,    //top
//						0,0);
				ImageView4.requestLayout();	
				
				ipet1 =(ViewGroup.MarginLayoutParams) tvchar.getLayoutParams();
//				ipet1.width=j1;
//				ipet1.height=k;
				ipet1.setMargins(6*j1+6*m+off,0,0,0);
				tvchar.requestLayout();	



				
				 ipet1 =(ViewGroup.MarginLayoutParams) spin.getLayoutParams();
					ipet1.width=(width*40*10)/1600;
					ipet1.height=k;
//					ipet1.setMargins(0,   //left
//							0,    //top
//							0,0);
					spin.requestLayout();	
///////////////////////////////////////////////////////////////////					
					 ipet1 =(ViewGroup.MarginLayoutParams) et22.getLayoutParams();
						ipet1.width=width;
						ipet1.height=(height*90)/100;
//						ipet1.setMargins(0,   //left
//								0,    //top
//								0,0);
						et22.requestLayout();	

et22SetSelection();
//GetPicturefromFile g2=new GetPicturefromFile();  ////  add pattern to et22
Bitmap bmp=CreateBMPfromParams1.getIVBMPm(context, (height*90)/100, width);
Drawable d0=CreateBMPfromParams1.convertBitmaptodrawable(context, bmp);
et22.setBackground(d0);	
updateALMCHAR();
}
////////
			super.onWindowFocusChanged(hasFocus);
		}
	  ///////////////////////////////////////////////////////////
	  
private int get_basic_tab_seed(int mWidthScreen2) {
	if (mWidthScreen2<=600) return 21;
	if (mWidthScreen2<=800) return 21;
	else return 21;
	}

	////////////////  
	  @Override
	  public void onResume(){
	        super.onResume();
//		    t1 = new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener() {	    		    	
//				@Override
//				public void onInit(int status) {
//					if (status !=TextToSpeech.ERROR) {
//						t1.setLanguage(Locale.UK);
//					}
//				}
//			});
	        show();
	    }  
	  
	// Creating an Adapter Class
	  public class MyAdapter extends ArrayAdapter<Object> {

	  public MyAdapter(Context context, int textViewResourceId, String[] objects)
	  {
		  super(context, textViewResourceId, objects);	  
		  }

	  public View getCustomView(int position, View convertView,  ViewGroup parent) {

	  // Inflating the layout for the custom Spinner
	  LayoutInflater inflater = getLayoutInflater();
	  View layout = inflater.inflate(R.layout.custom, parent, false);

	  // Declaring and Typecasting the textview in the inflated layout
	  TextView tvLanguage = (TextView) layout.findViewById(R.id.tvLanguage);
	  // Setting the text using the array
	  tvLanguage.setText(importance[position]);
	  double dbl=get_screen_params();
	   // Toast.makeText(getBaseContext(),"screen diag = " +Double.toString(dbl) , Toast.LENGTH_SHORT).show();
		if (dbl>5 && dbl <6){
			tvLanguage.setTextSize(14.0f);
		}
		else if (dbl>7 && dbl <8){
			tvLanguage.setTextSize(18.0f);
		}
		else if (dbl>8.0 && dbl <9.9){
			tvLanguage.setTextSize(25.0f);
		}
		else {
			tvLanguage.setTextSize(26.0f);
		}
	  
	  //tvLanguage.setTextSize(14.0f);
	  // Setting the color of the text
//	  tvLanguage.setTextColor(Color.rgb(35, 190, 225));
		String[] colorTextTable=new String[]{"#FFFFFF","#FF0000","#9933FF","#33FF33","#FFB266","#0080FF" };
		tvLanguage.setTextColor(Color.parseColor(colorTextTable[position]));

	  // Declaring and Typecasting the imageView in the inflated layout
	  ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

	  // Setting an image using the id's in the array
	  img.setImageResource(images[position]);

	  // Setting Special atrributes for 1st element
	  if (position == 0) {
	  // Removing the image view
	  img.setVisibility(View.GONE);
	  // Setting the size of the text
	  //tvLanguage.setTextSize(14f);
	  // Setting the text Color
	  tvLanguage.setTextColor(Color.BLACK);

	  }

	  return layout;
	  }

	  // It gets a View that displays in the drop down popup the data at the specified position
	  @Override
	  public View getDropDownView(int position, View convertView,
	  ViewGroup parent) {
	  return getCustomView(position, convertView, parent);
	  }

	  // It gets a View that displays the data at the specified position
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	  return getCustomView(position, convertView, parent);
	  }
	  }
	 
  
private void show() {

	tvpics.setText(Integer.toString(getListFiles.getNumberofFiles(dnameNN, memo_fname, ".JPG")));
	if (getListFiles.getNumberofFiles(dnameNN, memo_fname, ".JPG")==0){
//		tvpics.setVisibility(View.INVISIBLE);
//		ImageView2.setVisibility(View.INVISIBLE);
	}
	else { tvpics.setVisibility(View.VISIBLE);
		ImageView2.setVisibility(View.VISIBLE);
		}
	
	tvsound.setText(Integer.toString(getListFiles.getNumberofFiles(dnameNN, memo_fname, ".3gp")));
	if ((getListFiles.getNumberofFiles(dnameNN, memo_fname, ".3gp")==0)){
//		tvsound.setVisibility(View.INVISIBLE);
//		ImageView1.setVisibility(View.INVISIBLE);
		}
	else{ tvsound.setVisibility(View.VISIBLE);
	ImageView1.setVisibility(View.VISIBLE);
	}
	
	tvhnd1.setText(Integer.toString(getListFiles.getNumberofFiles(dnameNN, memo_fname, ".hnd")));
	if ((getListFiles.getNumberofFiles(dnameNN, memo_fname, ".hnd")==0)) {
//		tvhnd1.setVisibility(View.INVISIBLE);
//		ImageView3.setVisibility(View.INVISIBLE);
		}
	else{ tvhnd1.setVisibility(View.VISIBLE);
	ImageView3.setVisibility(View.VISIBLE);
	}
	
	tvvid.setText(Integer.toString(getListFiles.getNumberofFiles(dnameNN, memo_fname, ".mp4")));
	if (getListFiles.getNumberofFiles(dnameNN, memo_fname, ".mp4")==0){
//		tvvid.setVisibility(View.INVISIBLE);
//		vidcam.setVisibility(View.INVISIBLE);
	}
	else { tvvid.setVisibility(View.VISIBLE);
		vidcam.setVisibility(View.VISIBLE);}
	
//	tvchar.setText(String.valueOf(memobody1.length())           );
//	   tvchar.setText(String.valueOf(1+pointerText)+"/"+String.valueOf(memobody1.length()));  
	updateTVCHAR();	
	
	if (Integer.parseInt(memopriority1)==1) tv.setBackgroundColor(Color.parseColor("#E53935")); 
    if (Integer.parseInt(memopriority1)==2)   tv.setBackgroundColor(Color.parseColor("#AB47BC"))      ;
    if (Integer.parseInt(memopriority1)==3)  tv.setBackgroundColor(Color.parseColor("#00E676"))       ;
    if (Integer.parseInt(memopriority1)==4)   tv.setBackgroundColor(Color.parseColor("#FFB300"))      ;
    if (Integer.parseInt(memopriority1)==5)  tv.setBackgroundColor(Color.parseColor("#00B0FF")) ;
   
}
 
  private void deleteFile__() {
		// TODO Auto-generated method stub
  	AlertDialog.Builder adb=new AlertDialog.Builder(ActivityTwo.this);
      adb.setTitle("Delete/Cancel?");
      adb.setMessage("Are you sure you want to Delete the file? " );
      adb.setNegativeButton("Cancel", null);
      adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
   
          }});
     
      adb.show();	
  	
	}
  
  
  public void onPause(){
	  if (t1!=null) {
		  t1.stop();
		  t1.shutdown();
	  }
	  super.onPause();
  }
 
private void showCustomtoast(int i , boolean sh) {

    	if (sh)    {  	
    	LayoutInflater inflater = getLayoutInflater();

		View layout = inflater.inflate(R.layout.custom_toast,
		  (ViewGroup) findViewById(R.id.custom_toast_layout_id));

		// set a dummy image
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		
		if (i==0) image.setImageResource(R.drawable.saveicon_g); //0
		if (i==1) image.setImageResource(R.drawable.notstorage); //1
		if (i==2) image.setImageResource(R.drawable.voicenotdeleted); //2
		if (i==3) image.setImageResource(R.drawable.voicedeleted); //3
		if (i==4) image.setImageResource(R.drawable.filenotfound); //3
		if (i==5) image.setImageResource(R.drawable.filenotfound); //3
		if (i==6) image.setImageResource(R.drawable.dicatateactionbar2no);// dictate error
		// set a message
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(error_list[i]);

		// Toast...
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
    	}	
}

private void set_alarm() {
	Intent i = new Intent(getApplicationContext(), AlarmClk3_MainActivity.class);
	   // i.putExtra("_header", header);  //memo header
	SharedPreferences.Editor editor = sharedpreferencesAlarm.edit();  
	editor.putString("memo_text",et22.getText().toString()).apply();
	editor.putString("AlarmPerfPositionHeader",Integer.toString(spinner_position)+
			": "+header).apply();
	
	    startActivity(i);
}

private void read_all_prefs() {
	 mainDir_ = sharedpreferences.getString(MainDirName, "");
	 backupDir_ = sharedpreferences.getString(BackupDirName, "");
	 deletedDir_ = sharedpreferences.getString(DeletedDirName, "");
	 mailaddr_ = sharedpreferences.getString(MailAddress, "");
	 smsNumer_ = sharedpreferences.getString(SMSnumber, "");
	 TextSize_=sharedpreferences.getString(TextSize, "");
	 //Toast.makeText(getApplicationContext(),"SMS "+smsNumer_ , Toast.LENGTH_SHORT).show();
}

private void add_prefs_key(String string, String string2) {
	SharedPreferences.Editor editor = sharedpreferences.edit();  
	editor.putString(string, string2).apply();
	}


private void send_mail() {
	send_mail1();	
}

private void send_sms() {
	send_sms1();
}

public void sendWhatsAppMsg(View view) {
	send_whatapp1();

}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
     
         if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK ) {
        	try {

Toast.makeText(getApplicationContext(), "Picture captured "+sharedpreferences.getString(FULLFILENAMEANDPATH, "")
						, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "BAD BAD", Toast.LENGTH_SHORT).show();

				e.printStackTrace();
			} 
        showImageDialog();
        }
        
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode!=RESULT_OK ) 
        {
        	Toast.makeText(getApplicationContext(), "Picture NOT captured", Toast.LENGTH_SHORT).show();  
        }
        
        
        
     // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)  {
                 // fetch the message String
                  String message=data.getStringExtra("MESSAGE");
                  String flag1=data.getStringExtra("STATUS");
                  
                  if (flag1.equals("OK") ) {
                  String cc78=CRLF+message+CRLF+et22.getText().toString();
                  et22.setText(cc78);
                  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); 
                  }
                  else Toast.makeText(getApplicationContext(), "GPS not captured!", Toast.LENGTH_SHORT).show();  
                  
              }
        /////////////////////////
        if(requestCode==GESTUREACTIVITY)  {
            // fetch the message String
             String message=data.getStringExtra("MESSAGE");
             String flag1=data.getStringExtra("STATUS");             
             if (flag1.equals("OK") && message!=null && message.length()>0 ) {
            	 int in=pointerText;
            	 if (in>0) in-=1;
            	 String cc78=insertString1(et22.getText().toString(), message, in);
            	 et22.setText(cc78);
             Toast.makeText(getApplicationContext(), "Gesture= "+message, Toast.LENGTH_SHORT).show(); 
             }
             
             else if (flag1.equals("OK") && (message==null || message.length()==0) ){
            	 Toast.makeText(getApplicationContext(), "gesture not captured!", Toast.LENGTH_SHORT).show();  
             }
             else if (flag1.equals("NOTOK")){
            	 Toast.makeText(getApplicationContext(), "gesture file could not be read!", Toast.LENGTH_SHORT).show();  

             }
             
             
         }
        ////////////////////////
        if(requestCode==BROWSE_ACTIVITY_REQUEST_CODE)       {
            String dirFrombrowser=data.getStringExtra("dirFromBrowserName");
            String fileFrombrowser=data.getStringExtra("fileFromBrowserName");            
            String resultBrowser=data.getStringExtra("resultFromBrowserName");
            
            if (resultBrowser.equals("1") ) {
//            String cc78=CRLF+message+CRLF+et22.getText().toString();
//            et22.setText(cc78);
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); 
//Toast.makeText(getBaseContext(), "Fileppppppppppp is:  "+dirFrombrowser+" "+fileFrombrowser+" "+resultBrowser, Toast.LENGTH_SHORT).show();

File fileSrc = new File(Environment.getExternalStorageDirectory()+
		File.separator+dirFrombrowser+File.separator + fileFrombrowser);

File file = new File(Environment.getExternalStorageDirectory()+
	File.separator+mainDir_+File.separator + memo_fname+"_"+
	df1970.getmillisString()+".JPG");

/*
File filetest = new File(Environment.getExternalStorageDirectory()+
		File.separator+mainDir_+File.separator + memo_fname+"_"+
		df1970.getmillisString()+"ORIG.JPG");
////

 try {
	copyFile(fileSrc, filetest);
} catch (IOException e1) {
	e1.printStackTrace();
}
*/

   		
    	try {
			atask = new nAsyncTask(2,fileSrc,file);
			atask.execute();

Toast.makeText(getApplicationContext(), "Picture captured "+sharedpreferences.getString(FULLFILENAMEANDPATH, "")
					, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "BAD BAD", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
				}     	
            }
            else Toast.makeText(getApplicationContext(), "broswer not captured!", Toast.LENGTH_SHORT).show();              
        }
 
        ////////////////////////////////////////////////////////////////////////////////////////////////
        if (requestCode == GETDICATEDMESSAGE && resultCode==RESULT_OK ) {
        	Toast.makeText(getApplicationContext(), "text captured"+et22.getSelectionStart()
        			, Toast.LENGTH_SHORT).show(); 
            ArrayList<?> AllresultsSentences = null;
			try {
				AllresultsSentences = (ArrayList<?>) data.getSerializableExtra("resultsSentences");
			} catch (Exception e) {
		       	Toast.makeText(getApplicationContext(), "ACtivity Two line 1433", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			try {
//				message = "";
					if (AllresultsSentences!=null && AllresultsSentences.size()>0){
	//					message=(String)AllresultsSentences.get(0);
					}
			} catch (Exception e) {
		       	Toast.makeText(getApplicationContext(), "ACtivity Two line 1443", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}      	
//        	int is=-1;
        try {
        	final String[] c1 =new String[AllresultsSentences.size()];
        	final String[] c2 = AllresultsSentences.toArray(c1);

			AlertDialog.Builder alert = new AlertDialog.Builder(activity);
			alert.setTitle("Upload Photo");
			alert.setSingleChoiceItems(c2, -1, new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	message=c2[which];

			    }
			});
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
					String d1=et22.getText().toString().substring(0, pointerText);
					String d2=et22.getText().toString().substring(pointerText, et22.getText().toString().length());
					et22.setText(d1+" "+message+" "+d2);
					pointerText+=message.length();
					et22SetSelection();
			  
			    }
			});
			alert.show();
 
		} catch (Exception e) {
	       	Toast.makeText(getApplicationContext(), "ACtivity Two line 1453, is= "+pointerText, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        }
        
        if (requestCode == GETDICATEDMESSAGE && resultCode==RESULT_CANCELED ) 
        {
        	Toast.makeText(getApplicationContext(), "Dictate Failed", Toast.LENGTH_SHORT).show();  
        }
 
        
        
        if (requestCode == SPEECH_RECOGNITION && resultCode==RESULT_OK ) {
        	
            Bitmap b9=resizeActionbarImage(BitmapFactory.decodeResource(getResources(),
            		R.drawable.dicatateactionbar2),1);         
            ib.setBackground(new BitmapDrawable(getResources(), b9));
        	     	
            int recogCode=data.getIntExtra("result",-1);
            int speechErros=data.getIntExtra("speechErrors",-1);
            ArrayList<?> AllresultsSentences = (ArrayList<?>) data.getSerializableExtra("resultsSentences");
            String s8="";
            	if (AllresultsSentences!=null && AllresultsSentences.size()>0){
            		for (int i=0;i<AllresultsSentences.size();i++){
            			s8+=(String)AllresultsSentences.get(i)+"\n";
            					}
            	}

if (speechErros!=0 || recogCode<0) {
       		showCustomtoast(6, true);
//	Toast.makeText(getApplicationContext(), "recognition errors: "+speechErros+"\n"+
//            				s8+"\n"			, Toast.LENGTH_SHORT).show();             	
            }
            else {
               	Toast.makeText(getApplicationContext(), "recognition errors: "+speechErros+"\n"+
               			(String)AllresultsSentences.get(0)+"\n"+s8+"\n" +
               			"Code=    "+recogCode
               			, Toast.LENGTH_SHORT).show();  
            	
            	
            	if (recogCode==101) cameraShow1();
            	else if (recogCode==909) send_mail1();
            	else if (recogCode==900) send_sms1();
            	else if (recogCode==910) send_whatapp1();
            	else if (recogCode==507) hand_draw1();
            	else if (recogCode==302) audio_record1();
            	else if (recogCode==304) audio_play1();
            	else if (recogCode==707) speak_note1();
               	else if (recogCode==102) camera_record1();
               	else if (recogCode==604) video_play1();
            	else if (recogCode==602) video_record1();
            	else if (recogCode==203) browse1();            	

            }
        }
        
        
   /////////////// bar code////////////////////////////////////////////////////////////////
		//retrieve scan result
        if (requestCode == REQUEST_CODE_BARCODE ) {

		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanningResult != null) {
			//we have a result
						
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			String date9999=df1970.toDateStr();
			String message89="\ndate: "+date9999+"\nBar code: "+scanContent+"\nBar format: "+scanFormat+"\n";
			
          	 int in=pointerText;
        	 if (in>0) in-=1;
        	 String cc78=insertString1(et22.getText().toString(), message89, in);
        	 et22.setText(cc78);		
//			formatTxt.setText("FORMAT: " + scanFormat);
//			formatTxt.setText("FORMAT: " + requestCode);
//			contentTxt.setText("CONTENT: " + scanContent);
		    Toast.makeText(getApplicationContext(), 
			        "Code req= "+requestCode+ "bar= "+scanContent, Toast.LENGTH_SHORT).show();		
			
			}
		
		else{
		    Toast toast = Toast.makeText(getApplicationContext(), 
		        "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
        
        
        ////////////////////////////////
		}
        
        
        
        }   
        
        if(requestCode==1256)  {     
        	Toast.makeText(getBaseContext(),"back ALARM = " , Toast.LENGTH_SHORT).show();
      
        	updateALMCHAR();
         } 
        
        
    }







private void addDateNameToPicture() {
	
int angelcamera=Integer.valueOf(sharedpreferences.getString(ROTATECAMERAANGEL, ""));
String adr=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
	GetPicturefromFile g2=new GetPicturefromFile();
	Bitmap bmp=g2.getPicture3(angelcamera, adr, width, height);
	 temph=bmp.getHeight();
	 tempw=bmp.getWidth();

	 
Bitmap bmp1=g2.drawStringonBitmap(bmp, adr, 
		new Point (bmp.getWidth()/35,2*bmp.getHeight()/100), Color.RED, 255, 25, false, 

bmp.getWidth(), bmp.getHeight());
dateFrom1970 dta=new dateFrom1970();
String[] t1=adr.split("/");
String t2=t1[t1.length-1];
String[] t3=t2.split("_");
String t31=t3[1];
String t4=t31.substring(0, t31.length()-4);
String t5=dta.toDateStrfromStringdae(t4);

Bitmap bmp2=g2.drawStringonBitmap(bmp1, t5, 
		new Point (7*bmp1.getWidth()/10,99*bmp1.getHeight()/100),
Color.GREEN, 255, 25, false, bmp1.getWidth(), bmp1.getHeight());
g2.writeBMPtoFile(bmp2, adr, bmp2.getWidth(), bmp2.getHeight());
	
}

public boolean fileExistsInSD(String sFileName){
    String sFolder = Environment.getExternalStorageDirectory().toString() + 
             "/"+mainDir_;
    String sFile=sFolder+"/"+sFileName;
    java.io.File file = new java.io.File(sFile);
    return file.exists();
}




@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activy_two_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		
		case R.id.Save:
			TextColorsHolder.colorEnabled=false;
			mynt.setMemoBody(et22.getText().toString());
			mynt.setnote_count(String.valueOf(mynt.getMemoBody().length()));
			if (spinner_position!=0){ 
				mynt.setPriority(String.valueOf(spinner_position));
			}
				mynt.setCurrentDate(currentDateFormat());
				myNotesX.set(position_marker, mynt);
				sr.saveSerializedObject(dname, serFileName, myNotesX);
				textLengthChanged=false;
				priorityStateChanged=0;
Toast.makeText(getApplicationContext(), "File saved...", Toast.LENGTH_SHORT).show(); 
					
			break;
			
		case R.id.Back:
			if (!textLengthChanged && priorityStateChanged==0) ActivityTwo.this.finish();
			else {
			mynt.setMemoBody(et22.getText().toString());
			mynt.setnote_count(String.valueOf(mynt.getMemoBody().length()));
			if (spinner_position!=0){ 
				mynt.setPriority(String.valueOf(spinner_position));
			}
				mynt.setCurrentDate(currentDateFormat());
				
				openAlertDialog();
			}	
//			Intent returnIntent = new Intent();
//			returnIntent.putExtra("result",mynt);
//			setResult(Activity.RESULT_OK,returnIntent);
//			ActivityTwo.this.finish();
			break;
			
		case R.id.say_message:
//			t1.speak(et22.getText().toString(), TextToSpeech.QUEUE_FLUSH, null) ; 
			speak_note1();
//			Toast.makeText(getApplicationContext(), "Not active...", Toast.LENGTH_SHORT).show(); 
			break;
			
	
		case R.id.browse:
			browse1();
			break;
			
		case R.id.gesture:
			Intent gest = new Intent(getApplicationContext(), GestureListViewMainActivity.class);
			gest.putExtra("DIR1", mainDir_);  //directory name
//			rec2.putExtra("Value4", memo_fname); // memo file name
			startActivityForResult(gest, GESTUREACTIVITY);
			break;
			
		case R.id.barcode:
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
			break;
			
		case R.id.set_alaram:
//	  mynt= (Note)intent.getSerializableExtra("keyClass") ;
//			Intent i77 = new Intent(this, MainActivityTimelyAlarm.class);
//			i77.putExtra("noteObject", mynt);
//			startActivity(i77);
	
			Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
			set_alarm();
			break;
			
		case R.id.sendmail:
			send_mail();
			Toast.makeText(getApplicationContext(), "MAIL pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.sendsms:
			send_sms();
			Toast.makeText(getApplicationContext(), "SMS pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.undo:
			et22.setText(undo_buffer);
		   colorArrayList.clear();
		   for (int i=0;i<colorArrayListBack.size();i++){
			   colorArrayList.add(colorArrayListBack.get(i));
		   }
			et22.setText(parseFromColorsArray (colorArrayList));  
			pointerText=0;
			et22SetSelection();			   
			Toast.makeText(getApplicationContext(), "Un-done", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.sendahatsupsms:
			sendWhatsAppMsg(et22);
		    Toast.makeText(getApplicationContext(), "Sent Whatsup message", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.rec1:
			Intent rec2 = new Intent(getApplicationContext(), audiorec1.class);
			rec2.putExtra("Value1", mainDir_);  //directory name
			rec2.putExtra("Value4", memo_fname); // memo file name
			startActivity(rec2);
		    //Toast.makeText(getApplicationContext(), "record", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.vrec1:
			Intent vrec2 = new Intent(getApplicationContext(), CameraCapture1.class);
			vrec2.putExtra("Value1", mainDir_);  //directory name
			vrec2.putExtra("Value4", memo_fname); // memo file name
			startActivity(vrec2);
			break;
	
		case R.id.vplay1:
			Intent vplay = new Intent(getApplicationContext(), showFullMovie.class);
			vplay.putExtra("Value1", mainDir_);  //directory name
			vplay.putExtra("Value4", memo_fname); // memo file name
			startActivity(vplay);
			break;
			
		case R.id.play1:
			if (getListFiles.getNumberofFiles(dnameNN, memo_fname, ".3gp")==0 )
			{
				showCustomtoast(4,true);
			}
			else {
				Intent play2 = new Intent(getApplicationContext(), audioPlay.class);
				play2.putExtra("Value1", mainDir_);  //directory name
				play2.putExtra("Value4", memo_fname); // memo file name
				play2.putExtra("headerMail", header);  //directory name
				play2.putExtra("textmail", et22.getText().toString()); // memo file name
				play2.putExtra("mailaddr", mailaddr_); // mail send to address list
	      	    startActivity(play2);
			}
				//Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.camerarec1:
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
       	 	File file = new File(Environment.getExternalStorageDirectory()+
       		File.separator+mainDir_+File.separator + memo_fname+"_"+
       		df1970.getmillisString()+".JPG");
       		SharedPreferences.Editor editorcamera = sharedpreferences.edit();  
       		editorcamera.putString(FULLFILENAMEANDPATH, file.toString()).apply();
       	 	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
       	 	intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);       
       	 	intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, true);      
   		    intent.putExtra(MediaStore.EXTRA_FULL_SCREEN,true);
   		    intent.putExtra(MediaStore.EXTRA_SHOW_ACTION_ICONS,true);
   		 
   		 
       	 	startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
       	 break;
			
		case R.id.camerashow1:

				Intent play23 = new Intent(getApplicationContext(), showPicture.class);
				play23.putExtra("Value1", mainDir_);  //directory name
				play23.putExtra("headerMail", header);  //directory name
				play23.putExtra("textmail", et22.getText().toString()); // memo file name
				play23.putExtra("mailaddr", mailaddr_); // mail send to address list

		//		play23.putExtra("Value2", MyPREFERENCES);
		//		play23.putExtra("Value3", a23[3]);  //file name
				play23.putExtra("Value4", memo_fname);   // memo file
	      	    startActivity(play23);

			//Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show(); 
			break;
				//////////////////////////////////
		case R.id.dictate:
			//Toast.makeText(getApplicationContext(),a23[3]+ "!!!!!", Toast.LENGTH_SHORT).show();
//			Intent intentDIC = new Intent(getApplicationContext(), Speech10MainActivity.class);
//       	 	startActivityForResult(intentDIC, GETDICATEDMESSAGE);
 
			Intent speech = new Intent(getApplicationContext(), Speech2textRecognitionMainActivity.class);
			startActivityForResult(speech, GETDICATEDMESSAGE);
			break;	
					
		case R.id.gps1:
			
			Intent i89 = new Intent(this, getGPS.class);
		    //i89.putExtra("yourkey", string);
		    // TODO 2.. now use 
		     startActivityForResult(i89, 2);
		
			//Intent intentGetMessage=new Intent(this,getGPS.class);
           // startActivityForResult(intentGetMessage, 2);// Activity is started with requestCode 2
		//Toast.makeText(getApplicationContext(), "pressed ", Toast.LENGTH_SHORT).show(); 
			break;
					
		case R.id.handdarw:
			hand_draw1();
			break;

			
		case R.id.startclip1:
			clipCopyStartSelection=pointerText;
			Toast.makeText(getApplicationContext(), "Marked starting text: "+String.valueOf(clipCopyStartSelection)
					, Toast.LENGTH_SHORT).show(); 
			break;			
			
		case R.id.endclip1:
			setClipboard(context, et22.getText().toString().substring(clipCopyStartSelection, pointerText)); 
			Toast.makeText(getApplicationContext(), "Copied Selected text to Clipboard !!!", Toast.LENGTH_SHORT).show(); 
			break;			
			
		case R.id.fullclip1:
			setClipboard(context, et22.getText().toString()); 
			Toast.makeText(getApplicationContext(), "Copied Full text to Clipboard !!!", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.clippaste:
			copyClip();			

			break;
			
			
			
			
			
			
			
			
			
		case R.id.ForeGreen:
		colorTextStart=pointerText;	
		TextColorsHolder.optypeTemp=1;  // fore ground color
		TextColorsHolder.ColorTemp=Color.GREEN;	
		TextColorsHolder.colorEnabled=true;
		break;	
		
		case R.id.Foreblue:
		colorTextStart=pointerText;	
		TextColorsHolder.optypeTemp=1;  // fore ground color
		TextColorsHolder.ColorTemp=Color.BLUE;	
		TextColorsHolder.colorEnabled=true;
		break;			

		case R.id.Foreorange:
		colorTextStart=pointerText;	
		TextColorsHolder.optypeTemp=1;  // fore ground color
		TextColorsHolder.ColorTemp=Color.RED;
		TextColorsHolder.colorEnabled=true;
		break;		
		//et22	
			
		case R.id.stopColor:
			TextColorsHolder.colorEnabled=false;
		break;

		case R.id.clearColor:
			clearColor();
		break;
			
		case R.id.debug1:
			ArrayList<Integer> al =new ArrayList<Integer>();
			ArrayList<String> sal =new ArrayList<String>();
			
            for (int i=0;i<et22.getText().toString().length();i++){
         	   if (et22.getText().toString().substring(i, i+1).equals("\n")) {
         		   al.add(i);
         	   	}
            } 
			     for (int i=0;i<al.size();i++){
			    	 if (i==0){
			    		 sal.add(et22.getText().toString().substring(0, al.get(0)));
			    		 }
			    		 else {
			    			 sal.add(et22.getText().toString().substring(al.get(i-1)+1, al.get(i)));
			    		 	}
			     		}
			     String s="";
			     for (int i=0;i<al.size();i++){
			    	 s+=String.valueOf(al.get(i))+",";
			     }
			     s+=String.valueOf(pointerText);
			     tv.setText(s);
//			Toast.makeText(getApplicationContext(), "debug 1 !!!"+s, Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.debug2:
			
//			tv.setText(" "+firstBackPressedTime+" "+moveCounter+" "+timeStampForMove);
boolean b=sharedpreferences.getBoolean(alarmOnOfStatus, false);
b=!b;
SharedPreferences.Editor editor = sharedpreferences.edit();  
editor.putBoolean(alarmOnOfStatus,b).commit();
			
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
	public void onBackPressed() 
	{
 
		if (firstBackPressedTime==-1) {
			firstBackPressedTime=System.currentTimeMillis();
			Toast.makeText(ActivityTwo.this,"press back AGAIN to exit",
					Toast.LENGTH_LONG).show();
		}
		else if (firstBackPressedTime!=-1 && 
			System.currentTimeMillis()-firstBackPressedTime<5*1000) {
			firstBackPressedTime=-1;
			
			if ((!textLengthChanged && priorityStateChanged==0) ) {
				ActivityTwo.this.finish();
				}
			else {
			mynt.setMemoBody(et22.getText().toString());
			mynt.setnote_count(String.valueOf(mynt.getMemoBody().length()));
			if (spinner_position!=0){ 
				mynt.setPriority(String.valueOf(spinner_position));
			}
				mynt.setCurrentDate(currentDateFormat());
				
				openAlertDialog();
			}	
			
			
		}		
		else  {
			firstBackPressedTime=-1;
			Toast.makeText(ActivityTwo.this,"Back pressed too slow",
					Toast.LENGTH_LONG).show();

		}
//		Toast.makeText(ActivityTwo.this,"Please use the GO BACK button",Toast.LENGTH_LONG).show();

//		openAlertDialog();
//		Intent returnIntent = new Intent();
//		mynt.setMemoBody(et22.getText().toString());
//		mynt.setnote_count(String.valueOf(mynt.getMemoBody().length()));
//		if (spinner_position!=0) 
//			mynt.setPriority(String.valueOf(spinner_position));
//		mynt.setCurrentDate(currentDateFormat());
//		returnIntent.putExtra("result",mynt);
//		setResult(Activity.RESULT_OK,returnIntent);
//		ActivityTwo.this.finish();
			}

	private double get_screen_params() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		double wp=dm.widthPixels;
		double xdpi=dm.xdpi;
		double x = Math.pow(wp/xdpi,2);
		
		double hp=dm.heightPixels;
		double ydpi=dm.ydpi;
		double y = Math.pow(hp/ydpi,2);
		double screenInches = Math.sqrt(x+y);
		/*
		Toast.makeText(getBaseContext(), "screenInches=  "+Double.toString(screenInches)
				+"\n heightPixels=  "+Double.toString(hp)
				+"\n widthPixels=  "+Double.toString(wp)
				
				+"\n ydpi=  "+Double.toString(ydpi)
				+"\n xdpi=  "+Double.toString(xdpi)
				+"\n", Toast.LENGTH_SHORT).show();  
				*/         
		return screenInches;
	}
	
	   private void setClipboard(Context context,String text) {
	        
		   android.content.ClipboardManager clipboard =
		  		 	(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		   android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
		   clipboard.setPrimaryClip(clip);
		       
		      }
	
	   private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
		    try {
		        packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
		        return true;
		    } catch (NameNotFoundException e) {
		        return false;
		    }
		}
	   
	   private String currentDateFormat() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy-HH:mm" , Locale.getDefault());
	    	dt= new Date();
			//String  currentTimeStamp = dateFormat.format(dt);
	    	long curMillis = dt.getTime();
	    	//String curMillisStr=String.valueOf(curMillis);
	        return dateFormat.format(dt)+"*"+String.valueOf(curMillis);
	            
		}
	   
	   public void openAlertDialog(){

		      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		      alertDialogBuilder.setTitle("Note not saved !!! ");
		      alertDialogBuilder.setMessage("Do you want to save note?");
		      
		      alertDialogBuilder.setPositiveButton("yes", 
		         new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		            Toast.makeText(ActivityTwo.this,"You clicked yes button",Toast.LENGTH_LONG).show();
					Intent returnIntent = new Intent();
					returnIntent.putExtra("result",mynt);
					setResult(Activity.RESULT_OK,returnIntent);
					ActivityTwo.this.finish();
		         
		         
		         
		         }
		      });

		      alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
		    	  @Override
		         public void onClick(DialogInterface dialog, int which) {
saveColorTable(colorArrayListBack);
		    		  ActivityTwo.this.finish();
		         }
		      });

		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
		   }
	   

	   ProgressDialog progressDialog;

	   private class nAsyncTask extends AsyncTask<Void, Void, Void> {
		   int opType;
		   File file,fileSrc;
	   public nAsyncTask(int i) {
		   this.opType=i;
			}

	public nAsyncTask(int i, File fileSrc, File file) {
		this.file=file; this.fileSrc=fileSrc; this.opType=i;
			SharedPreferences.Editor editorcamera = sharedpreferences.edit();  
	   		editorcamera.putString(FULLFILENAMEANDPATH, file.toString()).apply();
	}

	@Override
	     protected void onPreExecute() {
	         progressDialog= new ProgressDialog(context);
	         progressDialog.setTitle("Processing Image");
	         progressDialog.setMessage("Please wait...");
	         progressDialog.setIcon(R.drawable.camera1a);
	         progressDialog.show();
	         super.onPreExecute();
	     }

	      protected Void doInBackground(Void... args) {
	    	  if (opType==2){
	    		  int angelcamera=Integer.valueOf(sharedpreferences.getString(ROTATECAMERAANGEL, ""));
	    			GetPicturefromFile g2=new GetPicturefromFile();
	    			Bitmap bmp=GetPicturefromFile.getPicture3(angelcamera, fileSrc.toString(), width, height);
	    			g2.writeBMPtoFile(bmp, file.toString(), bmp.getWidth(), bmp.getHeight());
//	   	         addDateNameToPicture();
	    	  }
	    	  
	    	  
	    	  
	    	  if (opType==1){
//	         addDateNameToPicture();
	    	  }
	    	  
	         return null;
	      }

	      protected void onPostExecute(Void result) {
	         if (progressDialog.isShowing()) progressDialog.dismiss();
	    	 Toast.makeText(getApplicationContext(), "D"+width+" x "+height+" "+tempw+" x "+temph
	 		, Toast.LENGTH_SHORT).show();  	 
				show();
	    	 super.onPostExecute(result);
	      }
	  }
	   
	   
	   
		private SpannableString parseFromColorsArray(ArrayList<TextColorsHolder> al) {
			SpannableString styledString = new SpannableString(et22.getText().toString());

			for (int i=0;i<al.size();i++){
			TextColorsHolder tc= al.get(i);

if (tc.getType()==1){
			styledString.setSpan(new ForegroundColorSpan(tc.getColor()), tc.getStart(), tc.getEnd(),0);
				}
if (tc.getType()==2){
	styledString.setSpan(new BackgroundColorSpan(tc.getColor()), tc.getStart(), tc.getEnd(),0);
		}

			}
			
			return styledString;
		}   

		  private void loadColorTable() {
colorArrayList=read_colorTable();
colorArrayListBack=read_colorTable();

if (colorArrayList!=null && colorArrayList.size()>0){
	et22.setText(parseFromColorsArray (colorArrayList));      
	et22SetSelection();	
	
}
				
			}

		private ArrayList<TextColorsHolder> read_colorTable() {
		    ArrayList<TextColorsHolder> userList   = new ArrayList<TextColorsHolder>();
//		    SharedPreferences prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
try {
userList = (ArrayList<TextColorsHolder>) ObjectSerializer.deserialize(sharedpreferences.getString(memo_fname+"COLOR",
		ObjectSerializer.serialize(new ArrayList<TextColorsHolder>())));
		      } catch (IOException e) {
		          e.printStackTrace();
		      }			
			return userList;

		}

		private void saveColorTable(ArrayList<TextColorsHolder> al) {
			Editor editorC = sharedpreferences.edit();
			try {
			editorC.putString(memo_fname+"COLOR", ObjectSerializer.serialize(al));
			} catch (IOException e) {
			e.printStackTrace();
			}
			editorC.commit();
			
			}		
		
		
		private void clearColor() {
			colorArrayList.clear();
			TextColorsHolder.colorEnabled=false;
			saveColorTable(colorArrayList);
			et22.setText(parseFromColorsArray (colorArrayList));      
			et22SetSelection();	
			updateTVCHAR();
		}	
		
	private void updateTVCHAR(){
		
//		String z="cs="+csDebug.toString()+" START="+startDebug+" COUNT="+countDebug+" BEFORE="+beforeDebug;
				
//				csDebug=s;startDebug=start;countDebug=count;beforeDebug=before;	 
		
		   tvchar.setText(String.valueOf(0+pointerText)+"/"+String.valueOf(0+et22.length()));  
	}	

	@SuppressWarnings("unchecked")
	private void updateALMCHAR(){			

		ArrayList<AlarmPostActivityHolder1> alx=new ArrayList<AlarmPostActivityHolder1>();
		try {
alx = (ArrayList<AlarmPostActivityHolder1>) ObjectSerializer.deserialize(sharedpreferences.getString(ALARMNOTENOTEHM,
		  ObjectSerializer.serialize(new ArrayList<AlarmPostActivityHolder1>())));
  } catch (IOException e) {
    e.printStackTrace();
  } 
	if (alx!=null && !alx.isEmpty() && alx.size()>0){
		boolean f =false;
		for (int i=0;i<alx.size();i++){
			if (memo_fname.equals(alx.get(i).getNotee().getMemo_header())){
				f=true; 				
				break;				
			}
		}
		if (f){
			   almchar.setText("7"); 
			   almchar.setBackgroundColor(Color.RED);
		}
		else {
			almchar.setText("5"); 
			almchar.setBackgroundColor(Color.GREEN);
		}
		
	}	
		
	else {
		almchar.setText("5"); 
		almchar.setBackgroundColor(Color.GREEN);
	}	
		
		
		
		
//		if (sharedpreferences.getBoolean(alarmOnOfStatus, false)){
//		   almchar.setText("7"); 
//		   almchar.setBackgroundColor(Color.RED);
//		   }
//		else{
//			almchar.setText("5"); 
//			almchar.setBackgroundColor(Color.GREEN);
//			}	
	}	
	
		
	private void copyClip(){
//		getClip();
		StringBuilder str = new StringBuilder(et22.getText().toString());
		// insert character value at offset 8
		str.insert(pointerText, getClip());
		et22.setText(str.toString());      
		clearColor();	
		pointerText--;

		et22SetSelection();	
	
		
		updateTVCHAR();
	}	
		
	private String getClip(){
		android.content.ClipboardManager clipboardAA =(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData abc = clipboardAA.getPrimaryClip();
		ClipData.Item itemC = abc.getItemAt(0);
		//Toast.makeText(getApplicationContext(), "clip is...  "+text, Toast.LENGTH_SHORT).show(); 

		if (itemC.getText().toString()!=null && itemC.getText().toString().length()>0){
			pasteFromClipboard=itemC.getText().toString();
		}
		else {
			pasteFromClipboard="";
		}
		
		
		return pasteFromClipboard;
	}	
	   
	private ArrayList<Integer> findHowManyCRinText(){
		ArrayList<Integer> t = new ArrayList<Integer>();
		for (int i=0;i<et22.getText().toString().length();i++){
    		if (et22.getText().toString().substring(i, i+1).equals("\n")) {
    			t.add(i);
    			}
    		}
		return t;
	}
	
	private int positionTextInMiddleOfLIne(ArrayList<Integer> al) {  // handle down
		float k = pointerText;
		int alSize=al.size();
		int et22Size=et22.getEditableText().toString().length();
		if (alSize==0) return Math.round(k); // no CR - single line
		if (alSize==1 && et22Size>al.get(0) ) return al.get(0)+(et22Size-al.get(0))/2;
		
		
		
if (!(al.size()<2)) {  // Otherwise just keep current pointer
			for (int i=0;i<al.size()-1;i++){
				if (k<=al.get(i)){
				k=	(1+al.get(i+1)-al.get(i))/2+al.get(i);//break;
				return Math.round(k);
					}
		}

	if (k>al.get(al.size()-2) && k<al.get(al.size()-1)){
		k=1+al.get(al.size()-1) + ( et22.getEditableText().toString().length() - al.get(al.size()-1))/2        ;
		return Math.round(k);
	}
//	   	Toast.makeText(getApplicationContext(), "down "+pointerText+" / "+k+" . "+
//		al.get(0)+" . "+al.get(1)+" . "+al.get(2), Toast.LENGTH_SHORT).show();	
}
if (k<0 || k>= et22.getText().toString().length())  {
   	Toast.makeText(getApplicationContext(), "Fatal error in DOWN  ", Toast.LENGTH_SHORT).show();	
	k=pointerText;
}
return (int)Math.round(k);

	}
	
	
	private int getDelta(float endX, float startX) {
		  float k=width/Math.abs(endX-startX);
		  int delta=0;
		  if (k<60 && k>13) delta =1;
		  else if(k<=13 && k>7) delta =2;
		  else if(k<=7 && k>1) delta =3;
		return delta;
	}

	private int getDeltaX(float endX, float startX) {
		  float k=width/Math.abs(endX-startX);
		  int delta=1;
		  if (k<4 && k>3) delta =2;
		  else if(k<=3 && k>2) delta =3;
		  else if(k<=2&& k>=1) delta =4;
//		  tv.setText("K "+k+"delta "+delta+"StartX "+startX+"endx "+endX);
		return delta;
	}

	
	private int FindtextPointerLocation(ArrayList<Integer> al) {  // up function
		float p=pointerText;
		if (al.size()==0) return et22.getEditableText().toString().length()/2;
		else if (al.size()==1 && p<=et22.getEditableText().toString().length() && p>al.get(0)){ 
			return al.get(0)/2;
		}

		else if (al.size()>1 && p<al.get(0  )){ 
			p=al.get(0)/2;
			}		

		else if (al.size()>1 && p>al.get(0  ) && p<al.get(1)){ 
			return al.get(0)/2;
			}
		
		else if (al.size()>1 && p>=al.get(al.size()-1)){
			return al.get(al.size()-2)+(al.get(al.size()-1)-al.get(al.size()-2))/2;
		}
		
		
		else if (al.size()>1) {  // we know al lentgh is 2 or more
			for (int i=2;i<al.size();i++){
//				if (p>-al.get(i-1) && p<=al.get(i)){
					if (p<=al.get(i)){
						if (p==al.get(i) && al.get(i)-al.get(i-1)==1){
							p=p-1;break;//check zero
						}
						else if (p>al.get(i-1) && al.get(i-1)-al.get(i-2)==1  ){p=al.get(i-1);break;}
						else {
					p=al.get(i-2)+ (  al.get(i-1)-al.get(i-2)  )/2;
							break;}
				}
			}
		}
		if (p<0 || p>=et22.getText().toString().length()) {
//		   	Toast.makeText(getApplicationContext(), "Fatal error in UP  ", Toast.LENGTH_SHORT).show();	
			return pointerText;
			}
		else return (int)Math.round(p);
	}

	private void et22SetSelection(){
		if (pointerText<0) pointerText=0;
		if (pointerText>et22.getText().toString().length()) pointerText=et22.getText().toString().length();
		et22.setSelection(pointerText);	
	}
	
	private static void copyFile(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    try {
	        OutputStream out = new FileOutputStream(dst);
	        try {
	            // Transfer bytes from in to out
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	        } finally {
	            out.close();
	        }
	    } finally {
	        in.close();
	    }
	}
	
	
	
	private void showImageDialog() {


		angleDialog=0;
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.acttwoshowimagedialog);
		dialog.setTitle("Image capture preview...");
		
		// set the custom dialog components - text, image and button
final RelativeLayout rl =(RelativeLayout)dialog.findViewById(R.id.rldialogimage2);
final TextView text = (TextView) dialog.findViewById(R.id.textdialog2);
final ImageButton imageButton = (ImageButton) dialog.findViewById(R.id.imagedialog2);
final Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK2);
final Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel2);

dialogButton.setBackgroundResource(R.drawable.okkkk);
dialogButtonCancel.setBackgroundResource(R.drawable.cancelllll);
		
	    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
	        @Override
	        public void onShow(DialogInterface d) {
	        	
ViewGroup.MarginLayoutParams ipet1 = 
	    				(ViewGroup.MarginLayoutParams) text.getLayoutParams();
	    				ipet1.width=rl.getWidth();
	    				ipet1.height=(rl.getHeight()*15)/100;
	    				ipet1.setMargins(0,0,0,0);
	    				text.requestLayout();
	    				///////////////////
ipet1 =(ViewGroup.MarginLayoutParams) imageButton.getLayoutParams();
	    						ipet1.width=rl.getWidth();
	    						ipet1.height=(rl.getHeight()*65)/100;
	    						ipet1.setMargins(0,0,0,0);
	    						imageButton.requestLayout();
	        	
ipet1 =(ViewGroup.MarginLayoutParams) dialogButton.getLayoutParams();
	    						ipet1.width=rl.getWidth()/8;
	    						ipet1.height=(rl.getHeight()*12)/100;
	    						ipet1.setMargins(0,0,0,0);
	    						dialogButton.requestLayout();   	
	    						
	    						ipet1 =(ViewGroup.MarginLayoutParams) dialogButtonCancel.getLayoutParams();
	    						ipet1.width=rl.getWidth()/8;
	    						ipet1.height=(rl.getHeight()*12)/100;
	    						ipet1.setMargins(0,0,0,0);
	    						dialogButtonCancel.requestLayout();  
	        	
	    						Matrix rotateMatrix = new Matrix();
 	    						String file=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
//	    						String s80=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
//	    						String s81=s80.substring(0, s80.length()-3)+"PARAM";
//	    						String s82=FileReadString.getFileStringSingle(s81);
//	    						angleDialog=Integer.valueOf(s82);
	    						rotateMatrix.postRotate(angleDialog);				
	    						final Bitmap b=GetPicturefromFile.getPicture3(0, file, rl.getHeight()*60/100, rl.getHeight()*60/100);
	    						Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0,b.getWidth(), b.getHeight(),rotateMatrix, false);
	    						imageButton.setImageBitmap(rotatedBitmap);	   
	    						text.setText("Angel= "+angleDialog);

	        }
	    });
		
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				
Matrix rotateMatrix = new Matrix();
angleDialog+=90;
if (angleDialog==360) angleDialog=0;
rotateMatrix.postRotate(angleDialog);
String file=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
final Bitmap b=GetPicturefromFile.getPicture3(0, file, rl.getHeight()*60/100, rl.getHeight()*60/100);
Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0,b.getWidth(), b.getHeight(),rotateMatrix, false);
imageButton.setImageBitmap(rotatedBitmap);				
text.setText("Angel= "+angleDialog);
			}
		});
	
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String s=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
				String s1=s.substring(0, s.length()-3)+"PARAM";
				String s2=String.valueOf(angleDialog);
				FileWriteString.setFileStringSingle(s1, s2);
				
				
				
				show();
				dialog.dismiss();
			}
		});

		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String file1=sharedpreferences.getString(FULLFILENAMEANDPATH, "");
				File file = new File(file1);
				if(file.exists())  {
					boolean b = file.delete();
				} 
				show();
				dialog.dismiss();
			}
		});
		
		dialog.show();
	  }
//////// end of show image dialog 




	@Override
	public void onClick(View v) {
int id=v.getId();

if (id==R.id.alarm2){
//Toast.makeText(getApplicationContext(), "alarm", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, alarm2);  
popup.getMenuInflater().inflate(R.menu.popalarm2, popup.getMenu());
popup.show();
}

if (id==R.id.sendall){
Toast.makeText(getApplicationContext(), "sendall", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, ImageView1);  
popup.getMenuInflater().inflate(R.menu.popsend10, popup.getMenu());
popup.show();
}

if (id==R.id.imageView1){
//Toast.makeText(getApplicationContext(), "iv1", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, ImageView1);  
popup.getMenuInflater().inflate(R.menu.popaudio, popup.getMenu());
popup.show();
	            
}
if (id==R.id.imageView2){  // camera
//Toast.makeText(getApplicationContext(), "iv2", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, ImageView2);  
popup.getMenuInflater().inflate(R.menu.popcamera, popup.getMenu());
popup.show();
}

if (id==R.id.imageView3){  // hand draw
//Toast.makeText(getApplicationContext(), "iv3", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, ImageView3);  
popup.getMenuInflater().inflate(R.menu.pophanddraw, popup.getMenu());
popup.show();	
}

if (id==R.id.vidcam){
//Toast.makeText(getApplicationContext(), "iv5", Toast.LENGTH_SHORT).show();	
popup = new PopupMenu(ActivityTwo.this, vidcam);  
popup.getMenuInflater().inflate(R.menu.popvideorecorder, popup.getMenu());
popup.show();	
}
popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	 public boolean onMenuItemClick(MenuItem item) {  
		 int y=item.getItemId();
		 Toast.makeText(getApplicationContext(), "y"+y, Toast.LENGTH_SHORT).show();	

		  if (item.getItemId()==R.id.alarm20) {
				 Toast.makeText(getApplicationContext(), "ALARM  "+y, Toast.LENGTH_SHORT).show();	
					Intent alm = new Intent(getApplicationContext(), MainActivityTimelyAlarm.class);
					alm.putExtra("noteObject", mynt);	
					alm.putExtra("position_marter", position_marker);
					startActivityForResult(alm, 1256);

				 
				 //
		  }	  
		  
		  
	  if (item.getItemId()==R.id.vidoeplay) {
		  video_play1();

	  }
	  if (item.getItemId()==R.id.videorecord) {
		  video_record1();
	  }
	  if (item.getItemId()==R.id.handdraw12) {
			 Toast.makeText(getApplicationContext(), "hand hand y"+y, Toast.LENGTH_SHORT).show();	
				Intent handdraw1 = new Intent(getApplicationContext(), HandDraw1.class);
				handdraw1.putExtra("dirName", mainDir_);  //directory name
				handdraw1.putExtra("fileNameORIGINAL", memo_fname);  //handwrite file name
				handdraw1.putExtra("bitmap", false);  //handwrite file name
				startActivity(handdraw1);
	  }
	  
	  if (item.getItemId()==R.id.camerabrowse) { 
		Intent brsw = new Intent(getApplicationContext(), BrowseFileMainActivity.class);
		startActivityForResult(brsw, BROWSE_ACTIVITY_REQUEST_CODE);			
	  }
	  
	  if (item.getItemId()==R.id.camerarecord) {
		  camera_record1();
	  }
	  if (item.getItemId()==R.id.camerashow) { 
cameraShow1();
	  }
	  
	  if (item.getItemId()==R.id.audiorecord) { 
		  audio_record1();
	  }
	  if (item.getItemId()==R.id.audioplay) { 
		  audio_play1();
	  }
	  if (item.getItemId()==R.id.speaknote) { 
		  speak_note1();
	  }
	  if (item.getItemId()==R.id.dictatenote) { 
			Intent speech = new Intent(getApplicationContext(), Speech2textRecognitionMainActivity.class);
			startActivityForResult(speech, GETDICATEDMESSAGE);
//			Intent intentDIC = new Intent(getApplicationContext(), Speech10MainActivity.class);
//       	 	startActivityForResult(intentDIC, GETDICATEDMESSAGE);
	  }
	
	  if (item.getItemId()==R.id.sendmail) { 
		  send_mail();
		  }
	  if (item.getItemId()==R.id.sendsms) { 
		  send_sms();
		  }	  
	  if (item.getItemId()==R.id.sendwa) { 
			sendWhatsAppMsg(et22);

		  }	  
	  
	  
	  
	  
	  return true;  
	 }  
	});  







	}// on click view

		
		
		
private Drawable getDrawable1 (Bitmap b, int j1,int k){
	Bitmap resizedBitmap = Bitmap.createScaledBitmap(
		    b, (2*j1/2), 5*k/10, true);
	return new BitmapDrawable(getResources(), resizedBitmap);
	
}		

private Bitmap resizeActionbarImage(Bitmap b, int i){
	if (i==1)
	return Bitmap.createScaledBitmap(b, mWidthScreen/10, mHeightScreen/15, false); 
	else 
	return Bitmap.createScaledBitmap(b, mWidthScreen/30, mHeightScreen/20, false); 

}
////////////  dispatch to execute  ///////////////////////////////////////		
	private void cameraShow1 (){
		  String k=tvpics.getText().toString();
		  if (k!=null && !k.equals("0")){
			Intent play23 = new Intent(getApplicationContext(), showPicture.class);
			play23.putExtra("Value1", mainDir_);  //directory name
			play23.putExtra("headerMail", header);  //directory name
			play23.putExtra("textmail", et22.getText().toString()); // memo file name
			play23.putExtra("mailaddr", mailaddr_); // mail send to address list
			play23.putExtra("Value4", memo_fname);   // memo file
  	    startActivity(play23);	
  	    }
		  else {
			  
Toast.makeText(getApplicationContext(), "No images to show !!!", Toast.LENGTH_SHORT).show();		

		  }
	}	
	
	private void send_mail1(){
	Intent email = new Intent(Intent.ACTION_SEND);
	email.setType("message/rfc822");
	
	ArrayList<String> al = new ArrayList<String>();
	String[] a=mailaddr_.split(",");
	if (Integer.parseInt(a[3])==101) {
		al.add(a[0]);
	}
	if (Integer.parseInt(a[4])==103) {
		al.add(a[1]);
	}
	if (Integer.parseInt(a[5])==105) {
		al.add(a[2]);
	}
	if (al.size()==1){
		String[] ml = new String[1];
		ml[0]=al.get(0);
		email.putExtra(Intent.EXTRA_EMAIL, ml);
	}
	if (al.size()==2){
		String[] ml = new String[2];
		ml[0]=al.get(0);
		ml[1]=al.get(1);
		email.putExtra(Intent.EXTRA_EMAIL, ml);
	}	
	if (al.size()==3){
		String[] ml = new String[3];
		ml[0]=al.get(0);
		ml[1]=al.get(1);
		ml[2]=al.get(2);
		email.putExtra(Intent.EXTRA_EMAIL, ml);
	}	
		
	
	//email.putExtra(Intent.EXTRA_EMAIL, new String[]{ mailaddr_});
	//email.putExtra(Intent.EXTRA_EMAIL, ml);

	email.putExtra(Intent.EXTRA_SUBJECT, header);
	email.putExtra(Intent.EXTRA_TEXT, et22.getText().toString());
	startActivity(email);
	Toast.makeText(getApplicationContext(),"Mail sent..." , Toast.LENGTH_SHORT).show();
	}
	
	private void send_sms1(){
		String asms[]=smsNumer_.split(",");
		String aasms="";
		if (Integer.parseInt(asms[3])==101){
			aasms=asms[0];
		}
		if (Integer.parseInt(asms[4])==103){
			aasms=asms[1];
		}
		if (Integer.parseInt(asms[5])==105){
			aasms=asms[2];
		}
			
		String bigString1=header+ " :-----  "+et22.getText().toString();		
		 try {
	        SmsManager smsManager = SmsManager.getDefault();
	        smsManager.sendTextMessage(aasms, null, bigString1, null, null);
	        Toast.makeText(getApplicationContext(), "SMS sent. "+aasms, Toast.LENGTH_SHORT).show();
	     } 
	     
	     catch (Exception e) {
	        Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_SHORT).show();
	        e.printStackTrace();
	     }
		
		Toast.makeText(getApplicationContext(),"SMS sent..." , Toast.LENGTH_SHORT).show();	
		
	}
	
	private void send_whatapp1(){
		PackageManager pm = context.getPackageManager();
	    boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
	if (!isInstalled) {
		       Toast.makeText(getApplicationContext(), "WHATSUP is not installed", Toast.LENGTH_SHORT).show();
	 }
		
	else {
	    Intent waIntent = new Intent(Intent.ACTION_SEND);
	    waIntent.setType("text/plain");
	            //String text = "testing message";
	    String text = et22.getText().toString();
	    
	    waIntent.setPackage("com.whatsapp");
	    waIntent.putExtra(Intent.EXTRA_TEXT, text);//
	    startActivity(Intent.createChooser(waIntent, text));
	  	}
	 
	}
	
	private void hand_draw1(){
		Intent handdraw1 = new Intent(getApplicationContext(), HandDraw1.class);
		handdraw1.putExtra("dirName", mainDir_);  //directory name
		handdraw1.putExtra("fileNameORIGINAL", memo_fname);  //handwrite file name
		handdraw1.putExtra("bitmap", false);  //handwrite file name
		startActivity(handdraw1);
	}
	
	private void audio_record1(){
		Intent rec2 = new Intent(getApplicationContext(), audiorec1.class);
		rec2.putExtra("Value1", mainDir_);  //directory name
		rec2.putExtra("Value4", memo_fname); // memo file name
		startActivity(rec2);
	}
	
	private void audio_play1(){
		if (getListFiles.getNumberofFiles(dnameNN, memo_fname, ".3gp")==0 )
		{
			showCustomtoast(4,true);
		}
		else {
			Intent play2 = new Intent(getApplicationContext(), audioPlay.class);
			play2.putExtra("Value1", mainDir_);  //directory name
			play2.putExtra("Value4", memo_fname); // memo file name
			play2.putExtra("headerMail", header);  //directory name
			play2.putExtra("textmail", et22.getText().toString()); // memo file name
			play2.putExtra("mailaddr", mailaddr_); // mail send to address list
      	    startActivity(play2);
		}
	}
	
	public void speak_note1(){
		
	    t1 = new TextToSpeech(context,new TextToSpeech.OnInitListener() {	    		    	
			@Override
			public void onInit(int status) {
				if (status !=TextToSpeech.ERROR) {
					Toast.makeText(getApplicationContext(),"Speaking..." , Toast.LENGTH_SHORT).show();	
					t1.setLanguage(Locale.UK);
					t1.speak(et22.getText().toString(), TextToSpeech.QUEUE_FLUSH, null) ; 
				}
				else {
					Toast.makeText(getApplicationContext(),"Speak error..." , Toast.LENGTH_SHORT).show();	
				}
			}
		});
		
		
//		t1.speak(et22.getText().toString(), TextToSpeech.QUEUE_FLUSH, null) ; 

	}
	
	private void camera_record1(){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
   	 	File file = new File(Environment.getExternalStorageDirectory()+
   		File.separator+mainDir_+File.separator + memo_fname+"_"+
   		df1970.getmillisString()+".JPG");
   		SharedPreferences.Editor editorcamera = sharedpreferences.edit();  
   		editorcamera.putString(FULLFILENAMEANDPATH, file.toString()).apply();
   	 	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
   	 	intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);       
   	 	intent.putExtra(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, true);      
		    intent.putExtra(MediaStore.EXTRA_FULL_SCREEN,true);
		    intent.putExtra(MediaStore.EXTRA_SHOW_ACTION_ICONS,true);  		 
   	 	startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

	}
	
	private void video_play1(){
		Intent vplay = new Intent(getApplicationContext(), showFullMovie.class);
		vplay.putExtra("Value1", mainDir_);  //directory name
		vplay.putExtra("Value4", memo_fname); // memo file name
		startActivity(vplay);	      
	}
	
	private void video_record1(){
		Intent vrec2 = new Intent(getApplicationContext(), CameraCapture1.class);
		vrec2.putExtra("Value1", mainDir_);  //directory name
		vrec2.putExtra("Value4", memo_fname); // memo file name
		startActivity(vrec2);
	}
	
	private void browse1(){
		Intent brsw = new Intent(getApplicationContext(), BrowseFileMainActivity.class);
		startActivityForResult(brsw, BROWSE_ACTIVITY_REQUEST_CODE);			
	Toast.makeText(getApplicationContext(), "BROWSE  ...", Toast.LENGTH_SHORT).show(); 
	}

	public static String insertString( 
	        String originalString, String stringToBeInserted, int index)  { 
	        String newString = originalString.substring(0, index + 1) 
	                           + stringToBeInserted 
	                           + originalString.substring(index + 1); 
	        return newString; 
	    } 	
	
	
	public static String insertString1(String originalString, String stringToBeInserted, int index) { 
	        StringBuffer newString = new StringBuffer(originalString); 
	        newString.insert(index + 1, stringToBeInserted); 
	        return newString.toString(); 
	    }




	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//		Toast.makeText(ActivityTwo.this, "SP chnaged......  "+key, Toast.LENGTH_SHORT).show(); 
		updateALMCHAR();
	}
	
}
 