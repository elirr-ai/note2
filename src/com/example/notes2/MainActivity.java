package com.example.notes2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener,SambaShareTaskInformer   {
	////////////////////////////////////
	// create new file with the content:
	//	0-	TimeDate (String)
	//	1-	Urgency (string 1=low,2=Mid,3=High)
	//	2- audio file (x)
	//	3 - Picture (y)
	// 	4 - hand draw (z) 
	//	5 q spare
	// 	6 spares  qq
	//7- message - initialized as black ""
	// COMMA is used as separator
	//
	////////////////////////////////////
	Context context1=this;
	double screenPARAM;
	int screenWidth;
	int width=0,height=0;
	boolean bad_memo_header_Exist=false;
	int Memo_Count=0;
	
	RelativeLayout rl;
	TextView textView1;
	EditText editText;
    ListView listView;
    Button dictate,confirm;
    
    private List<Note> myNotesX = new  ArrayList<Note>();
     
    ArrayAdapter<Note> adapter;
    
    private ActionBar actionBar;
    
    SharedPreferences sharedpreferences;
    
    public getListofFileTypes getListFiles;
    private static String DNAME = "memo_files";			// main folder
    private static final String DNAMEBACKUP = "memo_files_bk";  // backup folder
    private static final String DNAMEDELETE = "memo_files_dl";  // deleted folder
    private static final int memoName_MAX=20;
    private static final int maxMemoCount=100;
    
    public static final String MyPREFERENCES = "MyPrefs" ;  // my pref internal folder name
    public static final String serFileName = "abcd" ;  // my pref internal folder name
    private final static String GESTUREFILE="gestureaa.txt";
       // keys
    public static final String Initialized = "Initialized";
    public static final String MainDirName = "MainDirName";
    public static final String BackupDirName = "BackupDirName";
    public static final String DeletedDirName = "DeletedDirName";
    public static final String MailAddress = "MailAddress";
    public static final String SMSnumber = "SMSnumber";
    public static final String SoundMusic = "SoundMusic";
    public static final String last_note = "last_note";
    public static final String TextSize = "TextSize";
    public static final String Order = "Order";
    public static final String ROTATEPICTUREANGEL = "ROTATEPICTUREANGEL";
    public static final String ROTATECAMERAANGEL = "ROTATECAMERAANGEL";

	public static final String COLORTABLE = "COLORTABLE";
	public static final String COLORTABLECOUNT = "COLORTABLECOLORTABLECOUNT";
    
    public static final String REMOTEIP = "REMOTEIP";
    public static final String REMOTEFOLDER = "REMOTEFOLDER";
    public static final String REMOTEUSERNAME = "REMOTEUSERNAME";
    public static final String REMOTEUSERPASSWORD = "REMOTEUSERPASSWORD";
    final static int WriteZipFoldertoSMB=8;
	final static int UNZIPFRPMSMBTOANDROID=16;
    final static int PING_COMMAND=98;
    
    // 2nd key variable
    String mainDir_ ="";
    String backupDir_ ="";
    String deletedDir_ ="";
    String mailaddr_ ="";
    String smsNumer_ ="";
    String playSplash_ ="";
    String last_note_ ="";
    String TextSize_ ="";
    
    String[] error_list = {
    		"Memo name is empty - re-enter",   //0
    		"File too long",                   //1
    		"created new memo",                //2
    		"Quit program",                    //3
    		"Can not use storage",             //4
    		"List item cleared",               //5
    		"Deleted file",                    //6
    		"Could not delete file",           //7
    		"deleted memo",                    //8
    		"Backup done successfully" ,       //9
    		"Restore done successfully",       //10
    		"All files deleted successfully",  //11
    		"File name already exists",        //12
    		"Memo exceeds 100 memos allowed",  //13
    		"All backup files deleted successfully"         //14
    		  };

    int POSITION_MARKER=-1; // -1 if non valid,positive number is a valid position to set LAST	
	getListofFileTypes gl;
	serializeListArray1 sr;
    private Handler blink = new Handler();	
	private Drawable drd1,drd2,noListen,readyListen,busyListen;
    boolean flagBlink=false;
    private int voiceRecignistionState=1; //0=not active, 1=listening, 2=active
  
	private SpeechRecognizer mSpeechRecognizer;
	private Intent mSpeechRecognizerIntent; 
	private boolean mIslistening=false;     
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rl = (RelativeLayout) findViewById(R.id.rl);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setBackgroundColor(Color.parseColor("#ffccff"));
		
		dictate = (Button) findViewById(R.id.dictate);	
		dictate.setBackgroundColor(Color.parseColor("#99ccff"));
		dictate.setOnClickListener(this);

		confirm = (Button) findViewById(R.id.confirm);	
		confirm.setBackgroundColor(Color.parseColor("#a94cff"));
		confirm.setOnClickListener(this);
		
		
		
		
		editText = (EditText) findViewById(R.id.editText);
        editText.setBackgroundColor(Color.parseColor("#ffff00"));
        listView = (ListView) findViewById(R.id.listView);
        sr =new serializeListArray1();
        gl=new getListofFileTypes();
        screenPARAM=displ.getScreenInches();
        screenWidth=(int)displ.getScreenWidth();
        
        Drawable img = context1.getResources().getDrawable( R.drawable.newnote10);
        Bitmap b = ((BitmapDrawable)img).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        drd1=new BitmapDrawable(this.getResources(), bitmapResized);

        img = context1.getResources().getDrawable( R.drawable.newnote11);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        drd2=new BitmapDrawable(this.getResources(), bitmapResized);

        ////////////
        img = context1.getResources().getDrawable( R.drawable.mic1);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        readyListen=new BitmapDrawable(this.getResources(), bitmapResized);
        
        img = context1.getResources().getDrawable( R.drawable.micno72x72);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        noListen=new BitmapDrawable(this.getResources(), bitmapResized);
        
        img = context1.getResources().getDrawable( R.drawable.micyes172x72);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        busyListen=new BitmapDrawable(this.getResources(), bitmapResized);

//Toast.makeText(getBaseContext(),"screen diag = " +Double.toString(screenPARAM) , Toast.LENGTH_SHORT).show();
     	if (screenPARAM>5 && screenPARAM <=6){
     		textView1.setTextSize(getResources().getDimension(R.dimen.textsize1mainacttv));
     		editText.setTextSize(getResources().getDimension(R.dimen.textsize1mainactet));
     		dictate.setTextSize(getResources().getDimension(R.dimen.textsize1mainactet));
     		confirm.setTextSize(getResources().getDimension(R.dimen.textsize1mainactet));

     	}
     		
     		else	if (screenPARAM>6 && screenPARAM <=7){
         		textView1.setTextSize(getResources().getDimension(R.dimen.textsize2mainacttv));
         		editText.setTextSize(getResources().getDimension(R.dimen.textsize2mainactet));
         		dictate.setTextSize(getResources().getDimension(R.dimen.textsize2mainactet));
        		confirm.setTextSize(getResources().getDimension(R.dimen.textsize2mainactet));
     	}
     	else if (screenPARAM>7 && screenPARAM <=8){
     		textView1.setTextSize(getResources().getDimension(R.dimen.textsize3mainacttv));
     		editText.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
     		dictate.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
     		confirm.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
     	}
     	else if (screenPARAM>8.0 && screenPARAM <=9.9){
     		textView1.setTextSize(getResources().getDimension(R.dimen.textsize3mainacttv));
     		editText.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
     		dictate.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
    		confirm.setTextSize(getResources().getDimension(R.dimen.textsize3mainactet));
     	}
     	else {
     		textView1.setTextSize(getResources().getDimension(R.dimen.textsize4mainacttv));
     		editText.setTextSize(getResources().getDimension(R.dimen.textsize4mainactet));
    		dictate.setTextSize(getResources().getDimension(R.dimen.textsize4mainactet));
       		confirm.setTextSize(getResources().getDimension(R.dimen.textsize4mainactet));
     	}
           
        registerForContextMenu(listView);
        
        adapter = new MyListAdapter();
		//ListView list = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		        
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
        //listView.setAdapter(adapter);
		
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.notes1);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("Memo List");
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
		getListFiles=new getListofFileTypes();
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         
        
        String data = sharedpreferences.getString("Initialized", "");
		if (data.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), "String not found!!!!", Toast.LENGTH_SHORT).show();
		init_all_prefs();
		//myNotesX.clear();
		
		sr.saveSerializedObject(DNAME, serFileName, myNotesX);
		}
		//else Toast.makeText(getApplicationContext(), "String was found!!!!  !!! !!!!", Toast.LENGTH_SHORT).show();
		read_all_prefs();  
		
		setDRD_voiceRecogition();
		
 		if (playSplash_.equalsIgnoreCase("SoundMusic_on")) {
			}
		//Toast.makeText(getApplicationContext(), "!!!!!!!!! last_note_  :  "+last_note_, Toast.LENGTH_SHORT).show();
		POSITION_MARKER=Integer.parseInt(last_note_);
		check_gestures_exists();
		scan_directory();
		
        try {
			Memo_Count=myNotesX.size();
		} catch (Exception e) {
			showError(1);
			e.printStackTrace();
		}
 //       show_MEMOs();
	
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v , int keyCode ,KeyEvent event) {
				if (  (event.getAction())==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER        ){
					//Toast.makeText(getApplicationContext(), "enter pressed  ", Toast.LENGTH_SHORT).show(); 
					add_item1();
					hide_kbd();
					blink.post(blinkLed);
//					blink.removeCallbacks(blinkLed);
		    	    return true;
				}
					return false;
			}
		});
 editText.setOnTouchListener(new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.performClick();
		blink.removeCallbacks(blinkLed);
		return false;
	}
});
		
        
		 listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
	            	gotoNote(position);      
	            }
	    }
   	);	                
	                
//			setupSpeechRecognizer();    
//		 blink.postDelayed(blinkLed,0);       
	    	}   //  end of onCreate
	    
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (width==0 || height==0) {
			int heightWidget=6;
			width=rl.getMeasuredWidth();
			height=rl.getMeasuredHeight();
//			Toast.makeText(getBaseContext(),"onWindows = "+width+"  "+height,  Toast.LENGTH_SHORT).show();    

			ViewGroup.MarginLayoutParams ipet1 = 
					(ViewGroup.MarginLayoutParams) textView1.getLayoutParams();
			ipet1.width=(width*75)/100;
			ipet1.height=(6*height*heightWidget)/(100*4);
			ipet1.setMargins(0,   //left
					0,    //top
					0,0);
			textView1.requestLayout();
			
			 ipet1 = (ViewGroup.MarginLayoutParams) dictate.getLayoutParams();
					ipet1.width=(width*25)/100;
					ipet1.height=(6*height*heightWidget)/(100*4);
					ipet1.setMargins(0,   //left
							0,    //top
							0,0);
					dictate.requestLayout();		
					
					 ipet1 = (ViewGroup.MarginLayoutParams) confirm.getLayoutParams();
						ipet1.width=(width*25)/100;
						ipet1.height=(height*heightWidget)/100;
						ipet1.setMargins(0,   //left
								0,    //top
								0,0);
						confirm.requestLayout();		
					
	
			 ipet1 = (ViewGroup.MarginLayoutParams) editText.getLayoutParams();
			 	ipet1.width=(width*75)/100;
			 	ipet1.height=(height*heightWidget)/100;
			 	ipet1.setMargins(0,   //left
			 			0,    //top
			 			0,0);
			 	editText.requestLayout();	
					
			///////////////////

				 ipet1 = (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
				 	ipet1.width=(width*95)/100;
				 	ipet1.height=(height*97)/100;
				 	ipet1.setMargins(0,   //left
				 			0,    //top
				 			0,0);
				 	listView.requestLayout();	
					




		}
///////////////
		super.onWindowFocusChanged(hasFocus);
	}





	@Override
    public void onResume(){
        super.onResume();
        // put your code here...
        scan_directory();
    }



// added methods
    
    //custom toast
    
   //////////// add here //////////////
    private void add_item1() {
    	bad_memo_header_Exist=false;
    	String memoName=(editText.getText().toString());
    	editText.setText("");
    	
    	try {
			for (int i=0;i<myNotesX.size();i++) {
				if (memoName.equalsIgnoreCase(myNotesX.get(i).getMemo_header())) 
				{
					bad_memo_header_Exist=true;	
					showCustomtoast(12);
					}
			}
		} catch (Exception e) {
		showError(2);
			e.printStackTrace();
		}
    	
    	
    	
    	
    	
        if (memoName.length()==0 && !bad_memo_header_Exist ) 
        {
        	showCustomtoast(0);
        	//Toast.makeText(MainActivity.this, "Memo name is empty - re-enter", Toast.LENGTH_SHORT).show();
          	bad_memo_header_Exist=true;
        }
        
        if (Memo_Count+1 > maxMemoCount && !bad_memo_header_Exist ) 
        {
        	showCustomtoast(13);
         	bad_memo_header_Exist=true;
        }
        
        else if ( memoName.length()>memoName_MAX && !bad_memo_header_Exist) 
        {
        	showCustomtoast(1);
        	bad_memo_header_Exist=true;
        }
                          
        if (!bad_memo_header_Exist) {
        Note n2=new Note();
        n2.setDate(currentDateFormat());
        n2.setCurrentDate(currentDateFormat());
        n2.setPriority("3");
        n2.setMemo_header(memoName);
        n2.setMemoBody("<E>");
        n2.setnote_count(String.valueOf(n2.getMemoBody().length()));
        n2.setlast_note("not_last");
        n2.setcameracnt("0");
        n2.set_hand("0");
        n2.set_audio_record("0");
        n2.setMemoID(String.valueOf(System.currentTimeMillis()));
        
        try {
			myNotesX.add(n2);
			sr.saveSerializedObject(DNAME, serFileName, myNotesX);
		} catch (Exception e) {
		showError(3);
			e.printStackTrace();
		}
        
        
        Memo_Count++;
        showCustomtoast(2);
        editText.setText("");
        adapter.notifyDataSetChanged();
          }

    	
    	
    	
		
	}
    ///////////////////////////////////////////
    

/*
    private void file_read(String dwrite_,String fname_) {
  	  try {
  		  
  		  //File sdcard = Environment.getExternalStorageDirectory();
  			//File directory = new File(sdcard.getAbsolutePath()+"MyDirectory");
  			//File file= new File (directory,"textfile.txt");
  			
  			File Path = new File(Environment.getExternalStorageDirectory(), dwrite_);
  			if(!Path.exists()) {
  			    Path.mkdirs();
  			}
  			File file = new File(Path, fname_);
  			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
  				showCustomtoast(1);
  				//Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
  			    finish();
  			    return;
  			}
  			
  			
  			//File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
  			//String name = "textfile.txt";
  		//	File file = new File (path,name);
  			
  			FileInputStream fis=new FileInputStream (file);
  			
  			InputStreamReader isr = new InputStreamReader(fis);
  			char[] data=new char[data_block];  // data is char array with size=100
  			String final_data="";
  			int size;					
  				try {
  					while(  ( size=isr.read(data))>0 )
  					{
  					String read_data=String.copyValueOf(data, 0, size); 
  					final_data+=read_data;	
  					data = new char[data_block];
  					Memo=final_data;
  					}
  										
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}}
  							
  		 catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}	
    }

*/

	private void init_all_prefs() {
	
		add_prefs_key(Initialized, Initialized);
		add_prefs_key(MainDirName, DNAME);	
		add_prefs_key(BackupDirName, DNAMEBACKUP);	
		add_prefs_key(DeletedDirName, DNAMEDELETE);	
		//add_prefs_key(MailAddress, "noName@gmail.com");	
		add_prefs_key(MailAddress, "noName@gmail.com,noName@gmail.com,noName@gmail.com,100,102,104");
		add_prefs_key(SMSnumber, "0000000000,0000000000,0000000000,100,102,104");
		//add_prefs_key(SMSnumber, "0000000000");	
		add_prefs_key(SoundMusic, "SoundMusic_on");
		add_prefs_key(last_note, "-1");
		add_prefs_key(TextSize, "20"); 
		add_prefs_key(Order, "0,0");
		add_prefs_key(ROTATEPICTUREANGEL, "0");
		add_prefs_key(ROTATECAMERAANGEL, "0");
		add_prefs_intger_key(COLORTABLECOUNT,-1);
	}
    






	private void read_all_prefs() {
		// TODO Auto-generated method stub
	
		 DNAME = sharedpreferences.getString(MainDirName, "");
		 backupDir_ = sharedpreferences.getString(BackupDirName, "");
		 deletedDir_ = sharedpreferences.getString(DeletedDirName, "");
		 mailaddr_ = sharedpreferences.getString(MailAddress, "");
		 smsNumer_ = sharedpreferences.getString(SMSnumber, "");
		 playSplash_ = sharedpreferences.getString(SoundMusic, "");
		 last_note_ = sharedpreferences.getString(last_note, "");
		 TextSize_=sharedpreferences.getString(TextSize, "");
//		 Order_=sharedpreferences.getString(Order, "");
	}
    
   
	private void add_prefs_key(String string, String string2) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putString(string, string2).apply();
	}
	private void add_prefs_intger_key(String colortablecount2, int i) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putInt(colortablecount2, i).apply();		
		
	}	
    private void showCustomtoast(int i) {
    	LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast,
		  (ViewGroup) findViewById(R.id.custom_toast_layout_id));

		// set a dummy image
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		
		if (i==0) image.setImageResource(R.drawable.emptynote); //0
		if (i==1) image.setImageResource(R.drawable.filelong); //1
		if (i==2) image.setImageResource(R.drawable.add_note); //2
		if (i==3) image.setImageResource(R.drawable.iquit); //3
		if (i==4) image.setImageResource(R.drawable.notstorage); //4
		if (i==5) image.setImageResource(R.drawable.listclr);
		if (i==6) image.setImageResource(R.drawable.deletedfile); //6
		if (i==7) image.setImageResource(R.drawable.notdeleted); //7
		if (i==8) image.setImageResource(R.drawable.deletedmemo); //8
		if (i==9) image.setImageResource(R.drawable.backupdone); //9
		if (i==10) image.setImageResource(R.drawable.backupdone); //10
		if (i==11) image.setImageResource(R.drawable.deletedall); //11
		if (i==12) image.setImageResource(R.drawable.duplicatefiles); //12
		if (i==13) image.setImageResource(R.drawable.toomanyfiles); //13
		if (i==14) image.setImageResource(R.drawable.backupdeleted); //14
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
     
    
/*     
    private void copyFolder(File sourcePath, File destPath)  throws IOException   {   	
    	if (!sourcePath.isDirectory()) {
    		   // If it is a File the Just copy It to the new Folder
    			            InputStream in = new FileInputStream(sourcePath);
    			            OutputStream out = new FileOutputStream(destPath);
    			            byte[] buffer = new byte[1024];
    			            int length;
    			            while ((length = in.read(buffer)) > 0) {
    			            out.write(buffer, 0, length);
    			            	}
    			            in.close();
    			            out.close();
     			        }
    	else {
    	if (!destPath.exists()) {
    		      destPath.mkdir();
    		 			            }
    	String folder_contents[] = sourcePath.list();
    	for (String file : folder_contents) {
    		       File srcFile = new File(sourcePath, file);
    		       File destFile = new File(destPath, file);
    		        copyFolder(srcFile, destFile);
    					}
    	}
    	
	}    
 */  
/*    
    private void copyFoldersimple(File sourcePath, File destPath)  throws IOException   {
     	String  fil[] = sourcePath.list();
    	for (int i=0;i<fil.length;i++){
    		File srcFile = new File(sourcePath, fil[i].toString());
		    File destFile = new File(destPath, fil[i].toString());
		    InputStream in = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
            	}
            in.close();
            out.close();
    	}    	
 	}  
     
//    public boolean fileExistsInSD(String sFileName){
//        String sFolder = Environment.getExternalStorageDirectory().toString() + 
//                 "/"+mainDir_;
//        String sFile=sFolder+"/"+sFileName;
//        java.io.File file = new java.io.File(sFile);
//        return file.exists();
//    }
*/    
    
     
    
    private void scan_directory() {
	List<Note> l =sr.readSerializedObject(DNAME, serFileName);
    myNotesX.clear();
   for (int i=0;i<l.size();i++){
	   Note   n8=l.get(i);
           	if (POSITION_MARKER == i  ) {
           		n8.setlast_note("last");
           		}
           	else {
           		n8.setlast_note("not_last");
           		}
        	   myNotesX.add(n8);
   }
           

			File Path1 = new File(Environment.getExternalStorageDirectory(), DNAME);
			File[] ff=Path1.listFiles();
            int k=ff.length;
            try {
				for (int i=0;i<k;i++){
					String[] split=ff[i].toString().split("/");           	
						for (int j=0;j<myNotesX.size();j++){
							String s1=myNotesX.get(j).getMemo_header();
							String s2=split[split.length-1];
								if (s2.startsWith(s1) || s2.equals(serFileName) || s2.equals(GESTUREFILE)      ){
									ff[i]=null;
									}
						}
        }
			} catch (Exception e) {
			showError(4);
				e.printStackTrace();
			}
            int k1=0;
        for (int i=0;i<k;i++){
        	if (ff[i]!=null){
        		boolean b=ff[i].delete();
        		k1++;
        	}
        }            
            
if (k1!=0)           
	Toast.makeText(getApplicationContext(), "Cleaned data base,  "+k1+
			"  entries cleaned", Toast.LENGTH_SHORT).show();	





            adapter.notifyDataSetChanged();
            try {
				Memo_Count=myNotesX.size();
			} catch (Exception e) {
				showError(6);
				e.printStackTrace();
			}
            show_MEMOs();
    }
            
          private void sort_directory ()  {
        	  String Order_=sharedpreferences.getString(Order, "");
         String[] orderz = Order_.split(",");
         int sortType=Integer.parseInt(orderz[0]);
         int sortDirection=Integer.parseInt(orderz[1]);
 
  		if (sortType==3  && sortDirection==0  ) {  // sort by len ASC
 			Collections.sort(myNotesX,new CompareByNumbers(true)); // true=ASC, false=DSC 	     		    				
 		}// end of sort
         
 		if (sortType==3  && sortDirection==1  ) {  // sort by len DESC
 			Collections.sort(myNotesX,new CompareByNumbers(false)); // true=ASC, false=DSC 	
		}

 		if (sortType==0  && sortDirection==0  ) {  // sort by name ASC
 			Collections.sort(myNotesX,new CompareByString(0,true)); // true=ASC, false=DSC 	     		    				
 		}// end of sort
         
 		if (sortType==0  && sortDirection==1  ) {  // sort by name DESC
 			Collections.sort(myNotesX,new CompareByString(0,false)); // true=ASC, false=DSC 	
		}
        
 		if (sortType==2  && sortDirection==0  ) {  // sort by PRI ASC
 			Collections.sort(myNotesX,new CompareByString(2,true)); // true=ASC, false=DSC 	    		     		    				
		}
        
		if (sortType==2  && sortDirection==1  ) {  // sort by PRI DESC
			Collections.sort(myNotesX,new CompareByString(2,false)); // true=ASC, false=DSC 	      				
		}
        
 		boolean canCompareDate=true;
		try {
			for (int j=0;j<myNotesX.size();j++){
				if (myNotesX.get(j).get_millisStringAll().length()<16) {
					canCompareDate=false;
					}
				}
		} catch (Exception e) {
		showError(7);
			e.printStackTrace();
		}		
		
		if (sortType==1  && sortDirection==0 && canCompareDate ) {  // sort by DATE ASC
 			Collections.sort(myNotesX,new CompareByString(1,true)); // true=ASC, false=DSC 	     		    						     		     		    				
		}
       
		if (sortType==1  && sortDirection==1 && canCompareDate ) {  // sort by Date DESC
 			Collections.sort(myNotesX,new CompareByString(1,false)); // true=ASC, false=DSC 	     		    				     		     		    				
		}	        
        	        	
         adapter.notifyDataSetChanged();
         Memo_Count=myNotesX.size();
        show_MEMOs();
         
    }
  
    private String currentDateFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy-HH:mm");
    	Date dt= new Date();
        return dateFormat.format(dt)+"*"+String.valueOf(dt.getTime());
            
	}

    private void show_MEMOs() {

    	String[] sort__list = {
        		"Name", "date", "Pri", "Length"    };
    	String[] ASC_DSC = {
        		"Asc", "Dsc"   };
    	
    	
    	
    	
    	String[] v1 = sharedpreferences.getString(Order, "").split(",");
    	int p1=Integer.parseInt(v1[0]);
    	int p2=Integer.parseInt(v1[1]);
     	if (Memo_Count !=0 ) {   		
    		
    		textView1.setText("Number of MEMO's:  "+ Integer.toString(Memo_Count)
        		+"\nSort: "+ sort__list[p1]+"/"+ASC_DSC[p2]																																																					  	);
    	}
    		
    	//	textView1.setText("Number of MEMO's:  "+ Integer.toString(Memo_Count)
    	//	+"   Sort: "+  sharedpreferences.getString(Order, "") 	);
    
    	
        else textView1.setText("No MEMO's");
        //Toast.makeText(MainActivity.this, "Removed Memo # : "+Integer.toString(positionToRemove), Toast.LENGTH_SHORT).show();
    	
	}

    
    private void act_copyright() {
    	showDialog();
    }
/*
    private void act_help() {
    	// TODO Auto-generated method stub
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    			MainActivity.this);
    	alertDialogBuilder.setTitle("HELP Memory Game !!!");
    	alertDialogBuilder
    			.setMessage("Find all pairs of items with minimal steps. "
    					+ "\n\r Improve your memory")
    			.setCancelable(false)
    			.setPositiveButton("Click  OK",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog,
    								int id) {
     							dialog.cancel();
    						}
    					});
    	AlertDialog alertDialog = alertDialogBuilder.create();
    	alertDialog.show();
    }
*/
    
    private void ClearAll(final String dname2) {
		AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Delete/Cancel?");
        adb.setMessage("Are you sure you want to Delete all files? " );
        adb.setNegativeButton("Cancel", null);
        adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            	myNotesX.clear(); 
            	adapter.notifyDataSetChanged();

            	File rootPath = new File(Environment.getExternalStorageDirectory(), dname2); // need to clear also directory.......
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }
                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                	showCustomtoast(4);
                	Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                File[] files = rootPath.listFiles();
                 for (int i=0; i<files.length; i++) {  
                     files[i].delete();
                      }  
             	sr.saveSerializedObject(DNAME, serFileName, myNotesX);
             	Memo_Count=myNotesX.size();
                show_MEMOs();
                 if(dname2.equals(DNAME))    showCustomtoast(11);
                 if(dname2.equals(DNAMEBACKUP))    showCustomtoast(14);
                       }});
       
        adb.show();	
	}
    
 /*   
    private void deleteFile_no_confirmation_(final String memo_header) {
    	File Path = new File(Environment.getExternalStorageDirectory(), DNAME);
		if(!Path.exists()) {
		    Path.mkdirs();
		}
		File file99 = new File(Path, memo_header);
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			showCustomtoast(1);
			finish();
		    return;
		}
    	            	
    	boolean deleted = (file99.delete());
    	if (!deleted) showCustomtoast(7);
       	  }
*/    
 ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    class CopyFilesInbackground extends AsyncTask<File, String, Integer>
    {
    	File backOrRestore;
    	boolean isCanceled = false;

        public void myCancel()
        {
            isCanceled = true;
        }

        @Override
        protected Integer doInBackground(File... params) {
            File src = params[0];
            File dst = params[1];
            backOrRestore=params[2];
            Integer cntr=0;
        //    BigInteger result = BigInteger.ZERO;
          	String  fil[] = src.list();
        	for (int i=0;i<fil.length;i++){
        		File srcFile = new File(src, fil[i].toString());
    		    File destFile = new File(dst, fil[i].toString());
    		    InputStream in = null;
				try {
					in = new FileInputStream(srcFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                OutputStream out = null;
				try {
					out = new FileOutputStream(destFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                byte[] buffer = new byte[1024];
                int length;
                try {
					while ((length = in.read(buffer)) > 0) {
					try {
						out.write(buffer, 0, length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                publishProgress(String.valueOf(i)+" # "+fil[i].toString());
                cntr++;
        	}  
         /////////////////  ZIP file
        	dateFrom1970 dt1970=new dateFrom1970();
          File[] files = src.listFiles();
          ZipOutputStream zipOutputStream = null;  
        	File zfile=new File(dst,"MEMO_"+dt1970.toDateStr()+".zip");
        	try {
        		zipOutputStream = new ZipOutputStream(new FileOutputStream(zfile));
        	       int bufferSize = 1024;
        	        byte[] buf = new byte[bufferSize];

                Log.d("", "Zip directory: " + src.getName());
                for (int i = 0; i < files.length; i++) {
                    ZipEntry zipEntry;
                    File file = files[i];
                    zipEntry = new ZipEntry(files[i].toString());
                    zipOutputStream.putNextEntry(zipEntry);
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    int readLength;
                    while ((readLength = inputStream.read(buf, 0, bufferSize)) != -1) {
                        zipOutputStream.write(buf, 0, readLength);
                    }
                    inputStream.close();
                    publishProgress("ZIP..........."+String.valueOf(i)+" # "+fil[i].toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
                
            } finally {
                try {
					zipOutputStream.close();
	                publishProgress("ZIPPING done");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            } 

/////////////////  ZIP file



            return cntr;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
     if (backOrRestore.toString().endsWith("BACKUP")) { 
    	 showCustomtoast(9);
    	 }
     else {
    	 showCustomtoast(10);
    	 POSITION_MARKER=0;
         scan_directory();
    	 }
     show_MEMOs();  
        } 

        
        protected void onProgressUpdate(String... values) {
           textView1.setText("Backing file # :"+values[0]);
            }
    }

    private void backupAll() {
       			AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
    	        adb.setTitle("Continue/Cancel?");
    	        adb.setMessage("Are you sure you want to Backup all files? " );
    	        adb.setNegativeButton("Cancel", null);    	        
    	        adb.setNeutralButton("Backup", new AlertDialog.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
             	
    	            	//////////////////////// added 16-may-16
    	            	
    	            	File rootPath = new File(Environment.getExternalStorageDirectory(), DNAMEBACKUP); // need to clear also directory.......
    	                if(!rootPath.exists()) {
    	                    rootPath.mkdirs();
    	                }
    	                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    	                	showCustomtoast(4);
    	                	Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
    	                    finish();
    	                    return;
    	                }
    	                File[] files = rootPath.listFiles();
    	                 for (int i=0; i<files.length; i++) {  
    	                     files[i].delete();
    	                      }  
//    	                 scan_directory();    	                 
    	                 
    	            	try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
    	            	
    	            	    	            	
    	            	////////////////////////
    	            	
    	            	//ClearAll(DNAMEBACKUP);
    	            	File sourcePath = new File(Environment.getExternalStorageDirectory(), DNAME);
    	                if(!sourcePath.exists()) {
    	                	sourcePath.mkdirs();
    	                }
    	                File destPath = new File(Environment.getExternalStorageDirectory(), DNAMEBACKUP);
    	                if(!destPath.exists()) {
    	                	destPath.mkdirs();
    	                	
    	                } 
    	                
    	                File[] dirs =new File[3];
						dirs[0]=sourcePath;
						dirs[1]=destPath;
						dirs[2]=new File(Environment.getExternalStorageDirectory(), "BACKUP");
					
						CopyFilesInbackground	task = new CopyFilesInbackground();
						task.execute(dirs);		
                   }});
    	        adb.show();	
   		
	}
    
    private void restoreAll() {
     			AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
    	        adb.setTitle("Continue/Cancel?");
    	        adb.setMessage("Are you sure you want to Restore all files? " );
    	        adb.setNegativeButton("Cancel", null);    	        
    	        adb.setNeutralButton("Restore", new AlertDialog.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	File sourcePath = new File(Environment.getExternalStorageDirectory(),DNAMEBACKUP );
    	                if(!sourcePath.exists()) {
    	                	sourcePath.mkdirs();
    	                }
    	                File destPath = new File(Environment.getExternalStorageDirectory(), DNAME  );
    	                if(!destPath.exists()) {
    	                	destPath.mkdirs();
    	                }
       	                File[] dirs =new File[3];
    						dirs[0]=sourcePath;
    						dirs[1]=destPath;
    						dirs[2]=new File(Environment.getExternalStorageDirectory(), "RESTORE");
    					
    						CopyFilesInbackground	task = new CopyFilesInbackground();
    						task.execute(dirs);		
        // Toast.makeText(MainActivity.this, "List items cleared",  Toast.LENGTH_SHORT).show();  	
   	            }});
    	        adb.show();	
   		
	}
    
    
    private class MyListAdapter extends ArrayAdapter<Note> {
		public MyListAdapter() {
			super(MainActivity.this, R.layout.item_view, myNotesX);
			}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
		
if ( itemView == null ) {	
	itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);		
		}				

	
		Note currentNote=null;
		try {
			currentNote = myNotesX.get(position);
		} catch (Exception e) {
			showError(8);
			e.printStackTrace();
		}
		ViewGroup.MarginLayoutParams ipet1;			
			// Fill the view
		ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
		
//RelativeLayout rl = (RelativeLayout)itemView.findViewById(R.id.rl1);
ImageView imageView1 = (ImageView)itemView.findViewById(R.id.item_last);
ImageView imageView2 = (ImageView)itemView.findViewById(R.id.imageView100);
ImageView imageView3 = null;
imageView3 = (ImageView)itemView.findViewById(R.id.imageView101);
ImageView imageViewhnd = (ImageView)itemView.findViewById(R.id.imageViewhnd);
ImageView vicamrec = (ImageView)itemView.findViewById(R.id.vicamrec);

ipet1 =(ViewGroup.MarginLayoutParams) imageView1.getLayoutParams();
ipet1.width=(width*5)/100;	ipet1.height=(width*5)/100;
ipet1.setMargins(0, 	height/240,	width/120,0);	imageView1.requestLayout();	
imageView1.setScaleType(ScaleType.FIT_XY);


ipet1 =(ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
	ipet1.width=(width*16)/100;	ipet1.height=(height*10)/100;
	ipet1.setMargins(0, 	0,	0,0);	imageView.requestLayout();	
	imageView.setScaleType(ScaleType.FIT_XY);

	ipet1 =(ViewGroup.MarginLayoutParams) vicamrec.getLayoutParams();
	ipet1.width=(width*5)/100;	ipet1.height=(height*3)/100;
//	ipet1.setMargins((26*width)/100+width/80, 	0,	0,0);	vicamrec.requestLayout();	
	ipet1.setMargins(width/40, 	0,	0,0);	vicamrec.requestLayout();	

	vicamrec.setScaleType(ScaleType.FIT_XY);

	ipet1 =(ViewGroup.MarginLayoutParams) imageViewhnd.getLayoutParams();
	ipet1.width=(width*5)/100;	ipet1.height=(height*3)/100;
	ipet1.setMargins((4*width)/100, 	0,	0,0);	imageViewhnd.requestLayout();	
	imageViewhnd.setScaleType(ScaleType.FIT_XY);
	
	ipet1 =(ViewGroup.MarginLayoutParams) imageView3.getLayoutParams();
	ipet1.width=(width*5)/100;	ipet1.height=(height*3)/100;
	ipet1.setMargins((4*width)/100, 	0,	0,0);	imageView3.requestLayout();	
	imageView3.setScaleType(ScaleType.FIT_XY);
	
	ipet1 =(ViewGroup.MarginLayoutParams) imageView2.getLayoutParams();
	ipet1.width=(width*5)/100;	ipet1.height=(height*3)/100;
	ipet1.setMargins((4*width)/100, 	0,	0,0);	imageView2.requestLayout();	
	imageView2.setScaleType(ScaleType.FIT_XY);

			Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
			TextView cm = (TextView) itemView.findViewById(R.id.camera_count);
			cm.setText(currentNote.getCameraCnt());
			TextView hand_count = (TextView) itemView.findViewById(R.id.hand_count);
			hand_count.setText(currentNote.get_hand());
			
			TextView sound_count = (TextView) itemView.findViewById(R.id.sound_count);
			sound_count.setText(currentNote.get_audio_record());
			
			TextView vicamtv = (TextView) itemView.findViewById(R.id.vicamtv);
			vicamtv.setText(currentNote.get_audio_record());
								
			TextView memoName = (TextView) itemView.findViewById(R.id.notename);
			Spannable memoNameSpan = new SpannableString(currentNote.getMemo_header());        

			memoNameSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
					currentNote.getMemo_header().length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			memoName.setText(memoNameSpan);
			ipet1 =(ViewGroup.MarginLayoutParams) memoName.getLayoutParams();
			ipet1.width=(width*60)/100;	ipet1.height=(height*4)/100;
			ipet1.setMargins(width/100, 	0,	0,0);	memoName.requestLayout();	
//			memoName.setBackgroundColor(Color.GREEN);
			
			TextView noteCreateDate = (TextView) itemView.findViewById(R.id.noteCreateDate);
			noteCreateDate.setText("CR:  " + currentNote.getDate());
	
			ipet1 =(ViewGroup.MarginLayoutParams) noteCreateDate.getLayoutParams();
			ipet1.width=(width*28)/100;	ipet1.height=(height*3)/100;
			ipet1.setMargins(width/100, 	3,	0,0);	noteCreateDate.requestLayout();	
//			noteCreateDate.setBackgroundColor(Color.YELLOW);
			
			TextView noteUpdatedDate = (TextView) itemView.findViewById(R.id.noteUpdatedDate);
			noteUpdatedDate.setText("MD:  " + currentNote.getCurrentDate());
			ipet1 =(ViewGroup.MarginLayoutParams) noteUpdatedDate.getLayoutParams();
			ipet1.width=(width*28)/100;	ipet1.height=(height*3)/100;
			ipet1.setMargins(width/100, 	3,	0,0);	noteUpdatedDate.requestLayout();	
//			noteUpdatedDate.setBackgroundColor(Color.RED);

			TextView char_count = (TextView) itemView.findViewById(R.id.char_count);			
			char_count.setText(currentNote.getnote_count());
			ipet1 =(ViewGroup.MarginLayoutParams) char_count.getLayoutParams();
			ipet1.width=(width*8)/100;	ipet1.height=(width*5)/100;
//			ipet1.setMargins(0, 	0,	0,0);
			char_count.requestLayout();	
//			char_count.setBackgroundColor(Color.YELLOW);		

			
			
			TextView notenumber = (TextView) itemView.findViewById(R.id.notenumber);			
			notenumber.setText(""+(1+position)
				//	+" "+screenWidth);
					);
			notenumber.setBackgroundColor(Color.YELLOW);		
			
//			Toast.makeText(getBaseContext(),"screen diag = " +Double.toString(screenPARAM) , Toast.LENGTH_SHORT).show();
	     	if (screenPARAM>5 && screenPARAM <=6){
	     		memoName.setTextSize(2*getResources().getDimension(R.dimen.lv0));
	     		noteCreateDate.setTextSize(getResources().getDimension(R.dimen.lv0));
	     		noteUpdatedDate.setTextSize(getResources().getDimension(R.dimen.lv0));
	     		notenumber.setTextSize(2*getResources().getDimension(R.dimen.lv0));
	     		char_count.setTextSize(1*getResources().getDimension(R.dimen.lv0));

	     	}
	     		
	     		else	if (screenPARAM>6 && screenPARAM <=7){
		     		memoName.setTextSize(2*getResources().getDimension(R.dimen.lv1)/1);
		     		noteCreateDate.setTextSize(getResources().getDimension(R.dimen.lv1));
		     		noteUpdatedDate.setTextSize(getResources().getDimension(R.dimen.lv1));
		     		notenumber.setTextSize(2*getResources().getDimension(R.dimen.lv1));
		     		char_count.setTextSize(1*getResources().getDimension(R.dimen.lv1));

	     		}
	     	else if (screenPARAM>7 && screenPARAM <=8){
	     		memoName.setTextSize(2*getResources().getDimension(R.dimen.lv3)/1);
	     		noteCreateDate.setTextSize(getResources().getDimension(R.dimen.lv3));
	     		noteUpdatedDate.setTextSize(getResources().getDimension(R.dimen.lv3));
	     		notenumber.setTextSize(2*getResources().getDimension(R.dimen.lv3));
	     		char_count.setTextSize(1*getResources().getDimension(R.dimen.lv3));

	     	}
	     	else if (screenPARAM>8.0 && screenPARAM <=9.9){
	     		memoName.setTextSize(2*getResources().getDimension(R.dimen.lv3)/1);
	     		noteCreateDate.setTextSize(getResources().getDimension(R.dimen.lv3));
	     		noteUpdatedDate.setTextSize(getResources().getDimension(R.dimen.lv3));
	     		notenumber.setTextSize(2*getResources().getDimension(R.dimen.lv3));
	     		char_count.setTextSize(1*getResources().getDimension(R.dimen.lv3));

	     	}
	     	else {
	     		memoName.setTextSize(getResources().getDimension(R.dimen.lv4));
	     		noteCreateDate.setTextSize(getResources().getDimension(R.dimen.lv4));
	     		noteUpdatedDate.setTextSize(getResources().getDimension(R.dimen.lv4));
	     		notenumber.setTextSize(2*getResources().getDimension(R.dimen.lv4));
	     		char_count.setTextSize(1*getResources().getDimension(R.dimen.lv4));
	     		 
	     	}			
			
	
			
			if ( currentNote.getPriority().equals("1") ) imageView.setImageResource(R.drawable.ucritical);
			if ( currentNote.getPriority().equals("2") ) imageView.setImageResource(R.drawable.uimportant);
			if ( currentNote.getPriority().equals("3") ) imageView.setImageResource(R.drawable.uusual);
			if ( currentNote.getPriority().equals("4") ) imageView.setImageResource(R.drawable.ucasual);
			if ( currentNote.getPriority().equals("5") ) imageView.setImageResource(R.drawable.ulow);
			
			if ( currentNote.getlast_note().equals("last") ) {
					imageView1 .setVisibility(View.VISIBLE);
				      imageView1.startAnimation(animation1);

			}
			else  { 
				imageView1.clearAnimation();
				imageView1 .setVisibility(View.INVISIBLE);
					//imageView1 .setVisibility(View.GONE);
		      

			}
			
			
			cm.setText(Integer.toString
					(getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".JPG")));
			if ((getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".JPG")==0)) {
				cm.setVisibility(View.INVISIBLE);
				imageView3.setVisibility(View.INVISIBLE);}
			else {
				
				cm.setVisibility(View.VISIBLE);
				
				imageView3.setVisibility(View.VISIBLE);}		
			
			sound_count.setText(Integer.toString
					(getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".3gp")));
			if ((getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".3gp")==0)) {
				sound_count.setVisibility(View.INVISIBLE);
				imageView2.setVisibility(View.INVISIBLE);}
			else {
				sound_count.setVisibility(View.VISIBLE);
				imageView2.setVisibility(View.VISIBLE);}
			
			hand_count.setText(Integer.toString
					(getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".hnd")));
			if ((getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".hnd")==0)) {
				hand_count.setVisibility(View.INVISIBLE);
				imageViewhnd.setVisibility(View.INVISIBLE);}
			else {
				hand_count.setVisibility(View.VISIBLE);
				imageViewhnd.setVisibility(View.VISIBLE);}
		
			vicamtv.setText(Integer.toString
					(getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".mp4")));
			if ((getListFiles.getNumberofFiles(DNAME, currentNote.getMemo_header(), ".mp4")==0)) {
				vicamtv.setVisibility(View.INVISIBLE);
				vicamrec.setVisibility(View.INVISIBLE);}
			else {
				vicamtv.setVisibility(View.VISIBLE);
				vicamrec.setVisibility(View.VISIBLE);}
			
			
			return itemView;
		}				
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		
		case R.id.cleanallalarms:
			SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.remove("noteObject").commit();		
			editor.remove("ALARMNOTENOTE").commit();				
			Toast.makeText(getApplicationContext(), "ACT MAIN\n"
					+ "", Toast.LENGTH_SHORT).show(); 
			break;
			
			
		case R.id.showallalarms:
			SharedPreferences spa = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			ArrayList<Note>	  al=new ArrayList<Note>();
			try {

  al = (ArrayList<Note>) ObjectSerializer.deserialize(spa.getString("ALARMNOTENOTE",
			  ObjectSerializer.serialize(new ArrayList<Note>())));
	  } catch (IOException e) {
	    e.printStackTrace();
	  } 
			Toast.makeText(getApplicationContext(), "ACT MAIN\n"
					+ al.size(), Toast.LENGTH_SHORT).show(); 
			break;	
			
		
		case R.id.copyrt:
			act_copyright();
			Toast.makeText(getApplicationContext(), "Veresion 4.1 - Copyright Eli Rajchert\n"
					+ "use for >7 tablets", Toast.LENGTH_SHORT).show(); 
			break;
		case R.id.help:
//			act_help();
//			Toast.makeText(getApplicationContext(), "Help menu item pressed", Toast.LENGTH_SHORT).show(); 
			getAppInfo();
			break;

		case R.id.multiclip:
//		    Intent i = new Intent(getApplicationContext(), MultiClipBoardMainActivity.class);  
		    startActivity(new Intent(getApplicationContext(), MultiClipBoardMainActivity.class)  );  
//			Toast.makeText(getApplicationContext(), "Help menu item pressed", Toast.LENGTH_SHORT).show(); 

			break;
			
		case R.id.voicecommand:
				if (!mIslistening)
				{
				    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
				}
				break;
			
		case R.id.clrall:
			ClearAll(DNAME);
			//Toast.makeText(getApplicationContext(), "Set location", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.clrallbckup:
			ClearAll(DNAMEBACKUP);
			//Toast.makeText(getApplicationContext(), "Set location", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.backup:
			//Toast.makeText(getApplicationContext(), "profilesn", Toast.LENGTH_SHORT).show(); 
			backupAll();
		break;
		
		
		
			
		case R.id.restore:
			restoreAll();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;

		case R.id.zipfolder:
			//Toast.makeText(getApplicationContext(), "profilesn", Toast.LENGTH_SHORT).show(); 
			zipFolder();
		break;
			
		case R.id.zipfolderremote:
			//Toast.makeText(getApplicationContext(), "profilesn", Toast.LENGTH_SHORT).show(); 
			makeZipFolderRemote();
		break;
		
		case R.id.unzipfolderremote:
			makeUNZipFolderRemote();
		break;
		
		
		
		
		
		
		case R.id.setup:
			startActivity(new Intent(getApplicationContext(), setup1.class));
			break;
			
			
		case R.id.setuppage:
			startActivity(new Intent(getApplicationContext(), MainActivityNotepage1.class));
			break;			
			
		case R.id.flashlight:
			startActivity(new Intent(getApplicationContext(), flashlight.class));
			break;
		
			
		case R.id.exit:
			blink.removeCallbacks(blinkLed);
			finish();
			break;
		
		case R.id.additem:
			
			
			blink.postDelayed(blinkLed, 2*1000);
			add_item1();
			hide_kbd();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
		
			
		case R.id.SortTitleAsc:
			//sortType=0; // 0=memo header,1=date,2=priority
			//sortDirection=0;
			add_prefs_key(Order, "0,0");
			sort_directory();
			
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortTitleDsc:
			//sortType=0;
			//sortDirection=1;
			add_prefs_key(Order, "0,1");
			sort_directory();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortPriAsc:
			//sortType=2; // 0=memo header,1=date,2=priority
			//sortDirection=1;
			add_prefs_key(Order, "2,0");
			sort_directory();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortPriDsc:
			//sortType=2;
			//sortDirection=0;
			add_prefs_key(Order, "2,1");
			sort_directory();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
			
			
			
		case R.id.SortDateAsc:
			//sortType=1; // 0=memo header,1=date,2=priority
			//sortDirection=0;
			add_prefs_key(Order, "1,0");
			sort_directory();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortDateDsc:
			//sortType=1;
			//sortDirection=1;
			add_prefs_key(Order, "1,1");
			sort_directory();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortLengthAsc:
			//sortType=1; // 0=memo header,1=date,2=priority
			//sortDirection=0;
			add_prefs_key(Order, "3,0");
			sort_directory();
Toast.makeText(getApplicationContext(), "pressed L1", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortLengthDsc:
			//sortType=1;
			//sortDirection=1;
			add_prefs_key(Order, "3,1");
			sort_directory();
Toast.makeText(getApplicationContext(), "pressed L2", Toast.LENGTH_SHORT).show(); 
			break;
			
			
			
			
			
			
			
		}
		return super.onOptionsItemSelected(item);
	}
	


	
	private void makeZipFolderRemote() {
		setup_globals();
		SaveToSmb.AndroidFolder=DNAME;  // android zip source foder to make the zipped file on smb
		SaveToSmb.sharedFile1=DNAME+"100.zip"; // smb file final zipped file name
		SaveToSmb.opType=WriteZipFoldertoSMB;
		SaveToSmb sv =new SaveToSmb(MainActivity.this);	 // at the end
		sv.execute("");		
	}
	
	private void makeUNZipFolderRemote() {
		setup_globals();
		//SaveToSmb.AndroidFolder="memo_files";  // android zip source foder to make the zipped file on smb
		SaveToSmb.sharedFile1=DNAME+"100.zip"; // smb file final zipped file name
		SaveToSmb.opType=UNZIPFRPMSMBTOANDROID;
		SaveToSmb sv =new SaveToSmb(MainActivity.this);	 // at the end
		sv.execute("");
	}
	

	private void setup_globals(){
		SaveToSmb.context=context1;	
//		SaveToSmb.ip="192.168.1.116"; //no michse
////		SaveToSmb.ip="192.168.1.118"; //Irit Hishtaltut
////		SaveToSmb.ip="192.168.1.114"; //toshiba
////		SaveToSmb.ip="192.168.1.105"; //x_201
////		SaveToSmb.ip="192.168.1.100";
		
//		SaveToSmb.username= "elirr";
//		SaveToSmb.password= "elirr";
		
//////		SaveToSmb.username= "tosh1";
//////		SaveToSmb.password= "mnbMNB9988";
		
//		SaveToSmb.sharedFolder= "try//";//no michse
		
		SaveToSmb.ip =  sharedpreferences.getString(REMOTEIP, "");
		SaveToSmb.sharedFolder=  sharedpreferences.getString(REMOTEFOLDER, "");
		 SaveToSmb.username=  sharedpreferences.getString(REMOTEUSERNAME, "");
		 SaveToSmb.password=  sharedpreferences.getString(REMOTEUSERPASSWORD, "");

		
		
		
		
	}




	private void zipFolder() {
		dateFrom1970 d1970 =new dateFrom1970();
		MakeZipFile m1=new MakeZipFile(context1,DNAME,DNAME+"_"+d1970.toDateStr()+".zip",2);	
		m1.execute();
	}





	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.listView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		//menu.setHeaderTitle(Countries[info.position]);

    	    
    	    try {
				menu.setHeaderTitle("NOTE:  "+myNotesX.get(info.position).getMemo_header());
			} catch (Exception e) {
				showError(10);
				e.printStackTrace();
			}

    	    
    	    String[] menuItems = new String[]{"rename","Delete","Properties","ZipNote","CreatePDF"};
    		
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
//		String[] menuItems = new String[]{"rename","Delete","Properties","ZipNote","CreatePDF"};
//		String menuItemName = menuItems[menuItemIndex];
//	    String listItemName = "NOTE:  "+(myNotesX.get(info.position).getMemo_header());
	    
	   // TextView text = (TextView)findViewById(R.id.footer);
	   // text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
	    //Toast.makeText(getBaseContext(), "just "+menuItemName+"  "+listItemName+" *** "+Integer.toString(info.position)+"X"+Integer.toString(menuItemIndex), Toast.LENGTH_SHORT).show();           

	    
	    if (menuItemIndex==0){
	    	//Toast.makeText(getApplicationContext(), "pressed RENAME", Toast.LENGTH_SHORT).show(); 
	    	rename_file_cotext_menu(info.position);
	    }	
	    
	    if (menuItemIndex==1){
	    	//Toast.makeText(getApplicationContext(), "pressed DELETE", Toast.LENGTH_SHORT).show(); 
	    	delete_file_cotext_menu(info.position); 
	    }	  
	    	    
	    if (menuItemIndex==2){
	    	//Toast.makeText(getApplicationContext(), "pressed PROPERIES", Toast.LENGTH_SHORT).show(); 
	    	property_file_cotext_menu(   info.position     );
	}
	    if (menuItemIndex==3){
	    	//Toast.makeText(getApplicationContext(), "pressed PROPERIES", Toast.LENGTH_SHORT).show(); 
	    	zip_notes_cotext_menu(   info.position     );
	} 
	    if (menuItemIndex==4){
	    	
	    	
			PdfWrite2 pdfw2 = null;
			try {
				pdfw2 = new PdfWrite2(DNAME, myNotesX.get(info.position).getMemo_header()+".pdf",640,480);
			} catch (Exception e) {
				showError(12);
				e.printStackTrace();
			}

			ArrayList<String> noteBitmapImages = null;
			try {
				noteBitmapImages = new ArrayList<String>();
				getListofFileTypes gl=new getListofFileTypes();
				noteBitmapImages=gl.getOnlyGraphicsFiles(DNAME,myNotesX.get(info.position).getMemo_header());
			} catch (Exception e) {
				showError(13);
				e.printStackTrace();
			}

			try {
				ArrayList<String> msgContent=new ArrayList<String>();
				msgContent.add(myNotesX.get(info.position).getPriority());
				msgContent.add("Note name:		"+myNotesX.get(info.position).getMemo_header());
				msgContent.add("Note date created:		"+myNotesX.get(info.position).getDate());
				msgContent.add("Note date modified:		"+myNotesX.get(info.position).getCurrentDate());
				msgContent.add("Note\n\n  "+myNotesX.get(info.position).getMemoBody());
//	boolean b=pdfw2.addParagraph(msgContent,noteBitmapImages);
				nAsyncTask	atask = new nAsyncTask(pdfw2,msgContent,noteBitmapImages);
				atask.execute();
//	    	Toast.makeText(getApplicationContext(), "pressed createPDF  "+b, Toast.LENGTH_SHORT).show(); 
//	    	zip_notes_cotext_menu(   info.position     );
			} catch (Exception e) {
			showError(14);
				e.printStackTrace();
			}
	
	    
	    } 
	    return true;
    }
	
	
    
    
    
    
    private void zip_notes_cotext_menu( int positionm) {  // make zip for only note
    	String s=myNotesX.get(positionm).getMemo_header(); // note name
//    	GetNoteProperties gnp=new GetNoteProperties(DNAME,s);

    	ArrayList<String> t = new ArrayList<String>();
    	getListofFileTypes gl=new getListofFileTypes();
    	t=gl.getFilesFullPath(DNAME, s);

//    	new MakeZipFile(context1,DNAME, t, s,myNotesX.get(positionm).getMemoBody()).execute();
    	MakeZipFile m=new MakeZipFile(context1,DNAME, t, s,myNotesX.get(positionm).getMemoBody(),1);	
    	m.execute(); 
    	
    }
    
    private void property_file_cotext_menu( int positionm) {
    	
String s=myNotesX.get(positionm).getMemo_header(); // note name
GetNoteProperties gnp=new GetNoteProperties(DNAME,s);

//MakeZipFile m1=new MakeZipFile(context1,"Download", "1",2);	
//m1.execute();

//m1.execute(); 

//ArrayList<String> t = new ArrayList<String>();
//getListofFileTypes gl=new getListofFileTypes();
//t=gl.getFilesFullPath(DNAME, s);

//new MakeZipFile(context1,DNAME, t, s,myNotesX.get(positionm).getMemoBody()).execute();

//MakeZipFile m=new MakeZipFile(DNAME, t, s,myNotesX.get(positionm).getMemoBody());

//try {
//	m.zipper();
//} catch (IOException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}

	
    	
		AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);

        adb.setTitle("File properties ");
        adb.setMessage(
        "Note name:		"+myNotesX.get(positionm).getMemo_header()+"\n"+
        "Note date created:		"+myNotesX.get(positionm).getDate()+"\n"+
        "Note date modified:		"+myNotesX.get(positionm).getCurrentDate()+"\n"+
        "Note priority:	"+myNotesX.get(positionm).getPriority()+" \n"+		
        "Note size:		"+myNotesX.get(positionm).getnote_count()+" chars \n"+
       
        //      al=getListFiles.getListofFiles(DNAME, hdr, ".3gp");
"Note PICS:		"+
String.valueOf(getListFiles.getNumberofFiles(DNAME,myNotesX.get(positionm).getMemo_header(),"JPG"))+" \n"+
         
//        "Note PICS:		"+myNotesX.get(positionm).getCameraCnt()+" \n"+
        "Note HANDDARW:	"+
String.valueOf(getListFiles.getNumberofFiles(DNAME,myNotesX.get(positionm).getMemo_header(),"hnd"))+
        " \n"+

//        myNotesX.get(positionm).get_hand()+" \n"+
        "Note AUDIOREC:	"+
String.valueOf(getListFiles.getNumberofFiles(DNAME,myNotesX.get(positionm).getMemo_header(),"3gp"))+
        " \n"+
        "Note VIDEOREC:	"+
String.valueOf(getListFiles.getNumberofFiles(DNAME,myNotesX.get(positionm).getMemo_header(),"mp4"))+
        " \n"+
        
//myNotesX.get(positionm).get_audio_record()+" \n"+
 //       "Note time MS:	"+myNotesX.get(positionm).get_millisStringAll()+" \n"+
        "Note last:		"+myNotesX.get(positionm).getlast_note()+" \n"+
 "total number of file= "+String.valueOf(gnp.getNumberOfFilesInNote()  )+"\n"+
   "size on disk= "+
 String.valueOf((long)Integer.valueOf(myNotesX.get(positionm).getnote_count())+gnp.getSizeOfFilesInNote()) +
 " bytes\n"
        		);
  
        adb.setNegativeButton("OK", null);
  
        adb.show();	
	
	}




//////////////////////////////////
    private void delete_file_cotext_menu( final int positionm) {  
    AlertDialog.Builder adb1=new AlertDialog.Builder(MainActivity.this);
    adb1.setTitle("Delete/Cancel?");
    //adb.setMessage("Are you sure you want to Delete/Edit/Cancel file position:  " + Integer.toString(position+1)+" ?");
    adb1.setMessage("Delete file: " + myNotesX.get(positionm).getMemo_header()+" ?");
    adb1.setNegativeButton("Cancel", null);
    adb1.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        
           	File Path = new File(Environment.getExternalStorageDirectory(), DNAME);
    			if(!Path.exists()) {
    			    Path.mkdirs();
    			}

        String hdr=myNotesX.get(positionm).getMemo_header();
        ArrayList<String> al = new ArrayList<String>(); 
      
        al.clear();
        al=getListofFileTypes.getListofFiles(DNAME, hdr, ".hnd","HND");
	    
        for (int i=0;i<al.size();i++){
  //         	Toast.makeText(getApplicationContext(), "HND File: "+al.get(i), Toast.LENGTH_SHORT).show();
        	File f = new File(Path, al.get(i));
        	boolean deleted = f.delete();
        }
        
        al.clear();
        al=getListofFileTypes.getListofFiles(DNAME, hdr, ".hnb", ".HNB");
	    
        for (int i=0;i<al.size();i++){
  //         	Toast.makeText(getApplicationContext(), "HND File: "+al.get(i), Toast.LENGTH_SHORT).show();
        	File f = new File(Path, al.get(i));
        	boolean deleted = f.delete();
        }
        
        al.clear();
        al=getListofFileTypes.getListofFiles(DNAME, hdr, ".JPG", ".jpg");
	    
        for (int i=0;i<al.size();i++){
//           	Toast.makeText(getApplicationContext(), "HND File: "+al.get(i), Toast.LENGTH_SHORT).show();
        	File f = new File(Path, al.get(i));
        	boolean deleted = f.delete();
        }
        
        al.clear();
        al=getListofFileTypes.getListofFiles(DNAME, hdr, ".PNG", ".png");
	    
        for (int i=0;i<al.size();i++){
//           	Toast.makeText(getApplicationContext(), "HND File: "+al.get(i), Toast.LENGTH_SHORT).show();
        	File f = new File(Path, al.get(i));
        	boolean deleted = f.delete();
        }
        
        al.clear();
        al=getListofFileTypes.getListofFiles(DNAME, hdr, ".3gp", ".3GP");
	    
        for (int i=0;i<al.size();i++){
 //          	Toast.makeText(getApplicationContext(), "HND File: "+al.get(i), Toast.LENGTH_SHORT).show();
        	File f = new File(Path, al.get(i));
        	boolean deleted = f.delete();
        }   
        
        
        
       myNotesX.remove(positionm);
       sr.saveSerializedObject(DNAME, serFileName, myNotesX);	
       	adapter.notifyDataSetChanged();
       	Toast.makeText(getApplicationContext(), "File succssfully deleted", Toast.LENGTH_SHORT).show();
	
        	}

        }
	
    		);
    
 
    
    adb1.show();
}
    ///////////////////////////////////
	

//  rename  
    private void rename_file_cotext_menu( final int position) {
     hide_kbd();
    	 AlertDialog.Builder adb2=new AlertDialog.Builder(MainActivity.this);
    	    adb2.setTitle("Rename/Cancel?");
    	    //adb.setMessage("Are you sure you want to Delete/Edit/Cancel file position:  " + Integer.toString(position+1)+" ?");
    	adb2.setMessage("Rename file: " + position+"  '"+myNotesX.get(position).getMemo_header()+
    			"' to '" + editText.getText()+"' ?");
   	    adb2.setNegativeButton("Cancel", null);
        adb2.setPositiveButton("Rename", new AlertDialog.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) {

    	            int i1=0,i2=0;
    	            if ( editText.getText().length()==0    ) {
 	             		i2++;
 	          				}
    for (int i=0;i<myNotesX.size();i++){
    if (myNotesX.get(i).getMemo_header().equalsIgnoreCase(editText.getText().toString()) ){	
    	i1++;
    }}
    if (i1==0 && i2==0) {
    Note n=new Note();   
	n=myNotesX.get(position);
	n.setMemo_header(editText.getText().toString());
	myNotesX.set(position, n);	
	sr.saveSerializedObject(DNAME, serFileName, myNotesX);
	adapter.notifyDataSetChanged();
   	Toast.makeText(getApplicationContext(), "File succssfully renamed.", Toast.LENGTH_SHORT).show();
    }            
    	            
     	             if (i1!=0) {
    	            	Toast.makeText(getApplicationContext(), "Cannot rename - file already exists.", Toast.LENGTH_SHORT).show();
    	            	editText.setText("");
    	             }
    	             if (i2!=0) {
     	            	Toast.makeText(getApplicationContext(), "Cannot rename - target file length is zero.", Toast.LENGTH_SHORT).show();
     	            	editText.setText("");
    	             }
    	             editText.setText("");	
    	        		}
    	        
    	        }
    		
    	    		);
    	     	    
    	    adb2.show();
    	    
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
	
	private void hide_kbd() {
		// TODO Auto-generated method stub

this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


	}
	
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

if (requestCode == 1) {
if(resultCode == Activity.RESULT_OK){

	Note n9=new Note();
	n9= (Note)data.getSerializableExtra("result") ;

	try {
		myNotesX.set(POSITION_MARKER, n9);
		sr.saveSerializedObject(DNAME, serFileName, myNotesX);
	} catch (Exception e) {
		showError(16);
		e.printStackTrace();
	}
	adapter.notifyDataSetChanged();
			}
if (resultCode == Activity.RESULT_CANCELED) {
//Write your code if there's no result
				}
}



}//onActivityResult


private String getModification (){
	String s="*";
	ApplicationInfo ai = null;
		try{
		     ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
		     ZipFile zf = new ZipFile(ai.sourceDir);
		     ZipEntry ze = zf.getEntry("AndroidManifest.xml");
		     long time = ze.getTime();
		     s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
		     zf.close();
		  }catch(Exception e){
		  }
		
		final PackageManager pm = context1.getPackageManager();
		try {
			ai = pm.getApplicationInfo(ai.packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		File file = new File(ai.publicSourceDir);
		String[] s1=file.toString().split("/");
		long size = file.length();
		return "Veresion 1.0 - Copyright Eli Rajchert \n"+
		"\nPackage:\t "+s1[s1.length-1]+		
		"\n\nCreated date:\t "+s+
		"\n"+"FileSize:\t\t\t "+String.valueOf(size)+" bytes ";
	}
	
	private void showDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle("About Notes1...");
		alertDialog.setMessage(getModification());
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	
	}

	private Runnable blinkLed = new Runnable() {
        public void run() {
 if (flagBlink) {
	 editText.setBackgroundColor(Color.CYAN);
	 editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );
 }
 
 else {
	 editText.setBackgroundColor(Color.YELLOW);
	 editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drd2, null );
 }
 flagBlink=!flagBlink;
 
 blink.postDelayed(this, 2*1000);
        }
    };
    
 
    private void getAppInfo(){
    	 ArrayList<String> al=new ArrayList<String>();
    	 String s=""; 
    	 getAbout1 ga=new getAbout1(context1);	
    	al=	ga.getAppInfo();	
    	
    	for (int i=0;i<al.size();i++){
    		s+=al.get(i)+"\n\r";
    		}
    ga.showAlertDialog1("ABOUT", s);	
    }





	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		backExit();

         
		super.onBackPressed();
	}
  
	@Override
	protected void onDestroy() {
		if (mSpeechRecognizer != null)
		{
		        mSpeechRecognizer.destroy();
		}
		super.onDestroy();
	}
	
	private void backExit() {
		blink.removeCallbacks(blinkLed);
		finish();
	}




private void setDRD_voiceRecogition(){
	  if (voiceRecignistionState==0) textView1.setCompoundDrawablesWithIntrinsicBounds(null, null, noListen, null );
	  else if (voiceRecignistionState==1) textView1.setCompoundDrawablesWithIntrinsicBounds(null, null, busyListen, null );
	  else if (voiceRecignistionState==2) textView1.setCompoundDrawablesWithIntrinsicBounds(null, null, readyListen, null );
  }
	

private void gotoNote(int position) {
	POSITION_MARKER=position;
Toast.makeText(getApplicationContext(), "listview position  "+Integer.toString(POSITION_MARKER), Toast.LENGTH_SHORT).show(); 
	add_prefs_key(last_note, Integer.toString(POSITION_MARKER));
	add_prefs_key("memo_header",myNotesX.get(POSITION_MARKER).getMemo_header());
////////String s=myNotesX.get(POSITION_MARKER).getMemo_header(); // note name
	add_prefs_key("memo_position",Integer.toString(POSITION_MARKER+1));
Intent i = new Intent(getApplicationContext(), ActivityTwo.class);
	i.putExtra("keyClass",myNotesX.get(POSITION_MARKER)); // send the Note instance
	i.putExtra("DIRNAME", DNAME);  //note directory name
	i.putExtra("SERFILENAME", serFileName);
	i.putExtra("POSITION_MARKER1", POSITION_MARKER);
	startActivityForResult(i,1);
}

private void setupSpeechRecognizer() {
    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-UK");
    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
    		"en-UK");
    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                     this.getPackageName());


    SpeechRecognitionListener listener = new SpeechRecognitionListener();
    mSpeechRecognizer.setRecognitionListener(listener);
    
	
}


protected class SpeechRecognitionListener implements RecognitionListener
{

    @Override
    public void onBeginningOfSpeech()
    {               
mIslistening=true;
    }

@Override
public void onBufferReceived(byte[] buffer)
{

}

@Override
public void onEndOfSpeech()
{
	
  }

@Override
public void onError(int error)
{
//        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);


}

@Override
public void onEvent(int eventType, Bundle params)
{

}

@Override
public void onPartialResults(Bundle partialResults)
{

}

@Override
public void onReadyForSpeech(Bundle params)
{
	mIslistening=true;
}

@Override
public void onResults(Bundle results)
{
	mIslistening=false;
	Lev.initDB();
//	VoiceRecognitionParse1 vr=new VoiceRecognitionParse1();
    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    int t1[]=Lev.compareBufferDict(matches);

String s="";
for (int i=0;i<matches.size();i++){
 s+=matches.get(i)+"\n";
 
}    
//    tv2.setText("distance:  " +t1+"  "+Lev.al.get(t1).getS()+" "+Lev.al.get(t1).getNumberparams());
Toast.makeText(getBaseContext(),s+"\n" +t1[0]+"  "+t1[1]+" ",
		Toast.LENGTH_SHORT).show();
if (t1[0]==0){
	backExit();
}
else if (t1[0]==1){
	
	try {
		gotoNote(Integer.valueOf(t1[1]-1));
	} catch (Exception e) {
		Toast.makeText(getBaseContext(),"no such note",	Toast.LENGTH_SHORT).show();

		e.printStackTrace();
	}
} 
   
else if (t1[0]==9){
			startActivity(new Intent(getApplicationContext(), flashlight.class));
}

else if (t1[0]==8){
startActivity(new Intent(getApplicationContext(), MultiClipBoardMainActivity.class)  );  
}

else if (t1[0]==7){
startActivity(new Intent(getApplicationContext(), setup1.class));
}
//Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();

    
    // matches are the return values of speech recognition engine
    // Use these values for whatever you wish to do
}

@Override
public void onRmsChanged(float rmsdB)
{
}
}





@Override
public void onClick(View v) {
    Button b = (Button) v;
    switch(b.getId()) {
        case R.id.dictate:
			if (!mIslistening){
			    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
			}
//Toast.makeText(getBaseContext(),"clk" , Toast.LENGTH_SHORT).show();
            break;
        case R.id.confirm:

//Toast.makeText(getBaseContext(),"confirm" , Toast.LENGTH_SHORT).show();
blink.postDelayed(blinkLed, 2*1000);
add_item1();
hide_kbd();

break;
    }
    
    
    
    
}
	
ProgressDialog progressDialog;
private class nAsyncTask extends AsyncTask<Void, Void, Void> {
	PdfWrite2 pdfw2;
	ArrayList<String> msgContent, noteBitmapImages;

	public nAsyncTask(PdfWrite2 pdfw2, ArrayList<String> msgContent, ArrayList<String> noteBitmapImages) {
		this.msgContent=msgContent;
		this.noteBitmapImages=noteBitmapImages;
		this.pdfw2=pdfw2;
	}



@Override
  protected void onPreExecute() {
      progressDialog= new ProgressDialog(context1);
      progressDialog.setTitle("Processing PDF "+noteBitmapImages.size()+" file(s)");
      progressDialog.setMessage("Please wait...");
      progressDialog.setIcon(R.drawable.pdf72x72);
      progressDialog.show();
      super.onPreExecute();
  }

   protected Void doInBackground(Void... args) {
	   boolean b=pdfw2.addParagraph(msgContent,noteBitmapImages);
      return null;
   }

   protected void onPostExecute(Void result) {
      if (progressDialog.isShowing())
                      progressDialog.dismiss();
// 	 Toast.makeText(getApplicationContext(), "D"+width+" x "+height+" "+tempw+" x "+temph
//		, Toast.LENGTH_SHORT).show();  	 
      super.onPostExecute(result);
   }
}





@Override
public void onTaskDone(DelegateDataBackHolder d) {
	DelegateDataBackHolder s2=d;
//Toast.makeText(this, "@@@!!!!!!!!!!!!!!!!!! "+String.valueOf(s2.getOp())+" ***  ", Toast.LENGTH_SHORT).show();
if (s2.getOp()==UNZIPFRPMSMBTOANDROID){
	if (SaveToSmb.exeption1.equals("")){
		recreate();
	}
	else {
		Toast.makeText(this, "Error: "+SaveToSmb.exeption1+" ***  ", Toast.LENGTH_LONG).show();	
			}	
		}
	}

private void check_gestures_exists(){
	File Path1 = new File(Environment.getExternalStorageDirectory(), DNAME);
	File file100 = new File (Path1,GESTUREFILE);
	if (!file100.exists()){
		FileOutputStream out = null;
		InputStream in = getResources().openRawResource(R.raw.gestureaa);
		try {
			out = new FileOutputStream(file100);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		byte[] buff = new byte[1024];
		int read = 0;

		try {
			   try {
				while ((read = in.read(buff)) > 0) {
				      try {
						out.write(buff, 0, read);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} finally {
			     try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}




		}  //end of if exist
	}// end of check


//////////////////////
// fatal error hanler
private void showError(int e){
 Toast.makeText(getApplicationContext(), "ERROR "+e, Toast.LENGTH_SHORT).show();  	 
	finish();
}






/////////////////////////
}
