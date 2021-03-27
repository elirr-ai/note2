package com.example.notes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseFileMainActivity extends Activity implements MyTaskInformer{
	

	View vv;
	PopupMenu popup;
	Context context1=this;
	int width=0,height=0;
	int listViewChildrenCount;
	public static final int ACTIVITY_REQUEST_CODE = 1777;
	
	boolean go_button_color_flag=false;
	boolean invalidate_asterisk=true;
	File Path1,Path_def;
	String Path_default,Path_current;
	
	String currentDir1="";
	File[] files;
	TextView enterDirName;
	Button dir_up,go,new_file1,exit,paste1000,back,favor1000;
		
	ListView listView;
    private List<DirectoryFilesHolder> myDirectoryFilesHolder = new  ArrayList<DirectoryFilesHolder>();
    public boolean readyFlag;
    
    public boolean inTaskProgerss=false;
    ArrayList<String> fileBMPs = new ArrayList<String>();
    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
    loadBitmaps loadbmp;
    Bitmap bm;
    int imageLoadErrorCounter;
    
    ArrayAdapter<DirectoryFilesHolder> adapter;
	
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
 // keys
    public static final String Initialized = "Initialized";
    public static final String size_x = "size_x";
    public static final String size_y = "size_y";
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String position_location = "POSIION";
    public static final String sort_type_Main_Activity = "sort_Main_Act"; 	// 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String myVeryFavouriteDir = "MYVeryFavouriteDir";
    public static final String fitFullScreen = "FITFULL";
    public static final String volume = "VOLUME";
    public static final String showFullScreenMode = "showFullScreenMode";
    public static final String EMAILADDRESS = "EMAILADDRESS";
    public static final String EMAILADDRESS1 = "EMAILADDRESS1";
    public static final String EMAILCHECK = "EMAILCHECK";
    public static final String EMAILCHECK1 = "EMAILCHECK1";
    
    																		// 5 by size asc, 6 by size dsc
    
	//  set of params for  4X4 ///////////////////////////////////////////
	String size_x_="4",size_y_="4"; 	  // matrix of images size to be stored in preferenecs 
	//String Dir="";
	String Fname="",sort_="",position_="",sort_main_="",favour_="";
	//String apo; 
	
	String[] file_types = new String[]{"folder1", "imagemp3","imagepng","imagejpg","imagebmp", "imagegif","imagetext",
			"imageapk","pdf1",
			"imageother","imageother","imagemp4","image3gp","imageavi","imagemov","imageflv","imagempg",
			"imagemidi","imagewav","imagemkv","imagexml","imagevcf","imagezip","imagerar"};
	
	int[] resid_ = new int[40];
	
	String[] f_types = new String[]{"*", ".MP3",".PNG",".JPG",".BMP", ".GIF",".TXT",  //6
			".APK",".PDF","**",".LOG",".MP4",".3GP",".AVI",".MOV",".FLV",".MPG"       //16
			,".MID",".WAV",".MKV", //19
			"XML","VCF","ZIP","RAR"  //20,21,22,23
	};  
	
	ArrayList<String> CopyMoveArray = new ArrayList<String>();
	ArrayList<CurrentDirectoryLastHolder> goBackDir = new ArrayList<CurrentDirectoryLastHolder>();
    private ActionBar actionBar;
    private long lastTimeStampBackpressed=0;
	private int exitCounter=0;
	
    // screen params
    private int display_width,display_height;
    private double display_screen_size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String data = sharedpreferences.getString("Initialized", "");
		if (data.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), "String not found!!!!", Toast.LENGTH_SHORT).show();
		init_all_prefs();		
		}
		read_all_prefs();  
		setContentView(R.layout.activity_main2);
		double dbl=get_screen_params();
		
//		if (dbl>5 && dbl <6){
//		setContentView(R.layout.activity_main);
//		}
//		else if (dbl>7 && dbl <8){
//			setContentView(R.layout.activity_main1);
//		}
//		else if (dbl>7.9 && dbl <9.9){
//			setContentView(R.layout.activity_main2);
//		}
//		else {
//			setContentView(R.layout.activity_main3);
//		}
		
		
		vv=(View)findViewById(R.id.vv);
		favor1000 = (Button) findViewById(R.id.favor1000);
		back = (Button) findViewById(R.id.dir_back);
		dir_up = (Button) findViewById(R.id.dir_up);
		go = (Button) findViewById(R.id.go);
		exit = (Button) findViewById(R.id.exit1000);
		new_file1=(Button) findViewById(R.id.new1000);
		paste1000=(Button) findViewById(R.id.paste1000);
		enterDirName=(TextView)findViewById(R.id.enterguess);
		listView = (ListView) findViewById(R.id.listView);
    
		
		adapter = new MyListAdapter();
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		//listView.setLongClickable(true);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		String PACKAGE_NAME = getApplicationContext().getPackageName();
	    for (int i=0;i<file_types.length;i++) {
	    	resid_[i] = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+file_types[i] , null, null);
	    }
	
		enterDirName.setBackgroundColor(Color.YELLOW);
		enterDirName.setGravity((Gravity.BOTTOM | Gravity.START));
		enterDirName.setTextSize(16.0f);
		enterDirName.setTextColor(Color.BLACK);
		
		//dir_up.setBackgroundColor(Color.BLUE);
		dir_up.setBackgroundResource(R.drawable.dirup100);
		//dir_up.setTextSize(18.0f);
				
		go.setBackgroundResource(R.drawable.grid11);
		//go.setTextSize(18.0f);
				
		exit.setBackgroundResource(R.drawable.exitprogram);
		//exit.setTextSize(18.0f);
		
		paste1000.setBackgroundResource(R.drawable.paste1000);
		//paste1000.setTextSize(18.0f);
		paste1000.setVisibility(View.INVISIBLE);
		
		new_file1.setBackgroundResource(R.drawable.addnewfile);
		//new_file1.setTextSize(18.0f);
		back.setBackgroundResource(R.drawable.back1);	
		favor1000.setBackgroundResource(R.drawable.heart1);	
		
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.viewimage);
		actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setTitle("View "+Integer.toString(files.length)+" "+Integer.toString(filex.size()) );
		goBackDir.clear();
		goBackDir.add(new CurrentDirectoryLastHolder(currentDir1, "")); 
///////////////////
//     	mz=new MakeZipFile(context1, currentDir1,3);
//     	mz.execute();
//	mz.walk(Path1);

//        mz1=mz.getDirectoryAllfiles();
		
		////////////////////////////////
		//hide_kbd();
		get_directory();
		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));

		
back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadbmp.myCancel();
				//Toast.makeText(getApplicationContext(), "BACK pressed !!! ", Toast.LENGTH_SHORT).show();		
				if (goBackDir.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Top of Stack - can't go back !!! ", Toast.LENGTH_SHORT).show();
				}
				else {
//					if (!goBackDir.isEmpty()         ) {
//						if (currentDir1.equals(goBackDir.get(goBackDir.size()-1).getCurrentDir()) ){
//							
//							currentDir1 = goBackDir.remove(goBackDir.size()-1).getCurrentDir();
//						}
//					}
//					if (!goBackDir.isEmpty()         ) {
//					currentDir1 = goBackDir.remove(goBackDir.size()-1).getCurrentDir(); 
//					}
					
					currentDir1 = goBackDir.remove(goBackDir.size()-1).getCurrentDir(); 
					Toast.makeText(getApplicationContext(), "current "+currentDir1, Toast.LENGTH_SHORT).show();
					add_prefs_key(directory, currentDir1);
					get_directory();
										
					
				}
				
				}

			

		
		});
		
		dir_up.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadbmp.myCancel();
				parse_current_dir();
				add_prefs_key(directory, currentDir1);
				get_directory();
//				sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
    		
				}
		
		});
	
		go.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadbmp.myCancel();
				go_grid();
						
			}

		});
		
		
		
		
		
		new_file1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Creating the instance of PopupMenu  
				loadbmp.myCancel();
				popup = new PopupMenu(BrowseFileMainActivity.this, new_file1);  
	            //Inflating the Popup using xml file  
	           // popup.getMenuInflater().inflate(R.menu.popupmenu1, popup.getMenu());  
	          
	           // popup.setOnMenuItemClickListener(MainActivity.this);
	            popup.getMenu().add(1, 1, 1, "New File");
	            popup.getMenu().add(1,2,2,"New Dir");
	            popup.show();
	           	            
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {  
	             int a = item.getItemId();
	            	 Toast.makeText(BrowseFileMainActivity.this,"You Clicked : " + item.getTitle()+" "+a,Toast.LENGTH_SHORT).show();  
	              if (item.getItemId()==1) make_new_file();
	              if (item.getItemId()==2) make_new_dir();
	          
	              return true;  
	             }  
	            });  
	              //popup.show();//showing popup menu  
	           }  
	          });//closing the setOnClickListener method  
				
				
		favor1000.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Creating the instance of PopupMenu  
				loadbmp.myCancel();
				popup = new PopupMenu(BrowseFileMainActivity.this, new_file1);  
	            //Inflating the Popup using xml file  
	           // popup.getMenuInflater().inflate(R.menu.popupmenu1, popup.getMenu());  
	          
	           // popup.setOnMenuItemClickListener(MainActivity.this);
	            popup.getMenu().add(1, 1, 1, "Load Favorite ("+ sharedpreferences.getString(myVeryFavouriteDir, "")+" )");
	            popup.getMenu().add(1,2,2,"set Favorite ("+currentDir1+")");
	            popup.show();
	           	            
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {  
	             int a = item.getItemId();
	            	 Toast.makeText(BrowseFileMainActivity.this,"You Clicked : " + item.getTitle()+" "+a,Toast.LENGTH_SHORT).show();  
	              if (item.getItemId()==1) {
	            	  favour_ 	= sharedpreferences.getString(myVeryFavouriteDir, "");
	            	  currentDir1=favour_;
	            	  add_prefs_key(directory, currentDir1);
	            	  get_directory();
	              }
	              if (item.getItemId()==2) {
	            	  add_prefs_key(myVeryFavouriteDir, currentDir1);
	            	  }
	                     return true;  
	             }  
	            });  
	              //popup.show();//showing popup menu  
	           }  
	          });//closing the setOnClickListener method  			
				
			
				
						
		
		
	
		
		
		paste1000.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadbmp.myCancel();          
				//paste1000.setVisibility(View.INVISIBLE);
				if (CopyMoveArray.size()!=0 && CopyMoveArray.get(2).equals("c") ){
					Toast.makeText(getBaseContext(), "copy pressed    ", Toast.LENGTH_SHORT).show(); 
					copy_file();
					}
				if (CopyMoveArray.size()!=0 && CopyMoveArray.get(2).equals("m") ){
					Toast.makeText(getBaseContext(), "move pressed    ", Toast.LENGTH_SHORT).show(); 
					copy_file();
					delete___file();
					
					
							
					}
				paste1000.setVisibility(View.INVISIBLE);

			}

			
		});		
		
		
exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadbmp.myCancel();
       	        Intent intentb = new Intent();
    	        intentb.putExtra("dirFromBrowserName", "");
    	        intentb.putExtra("fileFromBrowserName", "");
       	        intentb.putExtra("resultFromBrowserName", "0");
    	        setResult(RESULT_OK, intentb);
    			
				BrowseFileMainActivity.this.finish();
			}

		});
		
	       
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                
            	loadbmp.myCancel();
      

            	
            	add_prefs_key(position_location, Integer.toString(position));
           		add_prefs_key(directory, currentDir1);
           		add_prefs_key(file_name, myDirectoryFilesHolder.get(position).getFullFileName()); 
//Toast.makeText(getBaseContext(), "File is:  "+sharedpreferences.getString(file_name, ""), Toast.LENGTH_SHORT).show();

            		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
   
boolean flag30=false;
for (int u=0;u<goBackDir.size();u++){
	if (goBackDir.get(u).getCurrentDir().equals(currentDir1)){
		flag30=true;
		CurrentDirectoryLastHolder c=goBackDir.get(u);
		c.setLast(myDirectoryFilesHolder.get(position).getFullFileName());
		goBackDir.set(u, c);
//		goBackDir.get(u).setLast(myDirectoryFilesHolder.get(position).getFullFileName());break;
	}
}
	if (!flag30){
      		
goBackDir.add(new CurrentDirectoryLastHolder(currentDir1, myDirectoryFilesHolder.get(position).getFullFileName()));////////////      		
}  
            		
            		
            		
            		
            		
            		if (myDirectoryFilesHolder.get(position).getDir_image()==0 ){  // is dir
             		currentDir1=currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName(); // holds the extra path for the absolute....
             		add_prefs_key(directory, currentDir1);
             		//add_prefs_key(position_location, "0");
             		
//                		goBackDir.add(new CurrentDirectoryLastHolder(currentDir1, ""));
                		get_directory();
                		
                		Toast.makeText(getBaseContext(), "Position:  "+Integer.toString(position)+"  "+ 
                  				currentDir1+" AT listview ??"+Integer.toString(myDirectoryFilesHolder.size()), Toast.LENGTH_SHORT).show();
                		
                		if (myDirectoryFilesHolder.size()>0){
                			//add_prefs_key(position_location, "-1");
                			//add_prefs_key(file_name, myDirectoryFilesHolder.get(position).getFullFileName()); 
//////////////////////////////     			add_prefs_key(file_name, ""); 
                			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
//Toast.makeText(getBaseContext(), "**************:  ", Toast.LENGTH_SHORT).show();
                      			
                		}

                	}
 
                
                	else if (myDirectoryFilesHolder.get(position).getDir_image()==1
                	|| myDirectoryFilesHolder.get(position).getDir_image()==17 
                	|| myDirectoryFilesHolder.get(position).getDir_image()==18)
               		
                	{  //  MP3, WAV , MID
 //               		Toast.makeText(getBaseContext(), "End with MP3  !!!!!!!!!  "+Integer.toString(position), Toast.LENGTH_SHORT).show();
 //                		Intent i2 = new Intent(getApplicationContext(), audioPlay1.class);
 //       				startActivity(new Intent(getApplicationContext(), audioPlay1.class));
            	
                	}
                	
                   	else if (myDirectoryFilesHolder.get(position).getDir_image()==6
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==10
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==20
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==21
                   			) // txt
                               	{
 
//                		Intent i10 = new Intent(getApplicationContext(), showText1.class);
//                		startActivityForResult(new Intent(getApplicationContext(), showText1.class), 2);
                	       	          	}
                	
                	else if (myDirectoryFilesHolder.get(position).getDir_image()==8){  //pdf
//                		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
//                				"/"+currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName());
//                		Intent intent = new Intent(Intent.ACTION_VIEW);
//                		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                		startActivity(intent);
 String dd=currentDir1;
 String ff=myDirectoryFilesHolder.get(position).getFullFileName();
//	Intent i199 = new Intent(getApplicationContext(), ReadPdf_MainActivity.class);

// Bundle mBundle = new Bundle();
// mBundle.putString("dir", dd);
// mBundle.putString("file", ff);
// i199.putExtras(mBundle);
//              		startActivity(i199);
                	                	
                	}

                	else	if (myDirectoryFilesHolder.get(position).getDir_image()==7){ //apk
                		Toast.makeText(getBaseContext(), "End with apk !!!!!!!!!   "+myDirectoryFilesHolder.get(position).getFullFileName() , Toast.LENGTH_SHORT).show();
//   		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
//                				"/"+currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName());
  
//                		Intent intentapk = new Intent(Intent.ACTION_VIEW);
//intentapk.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                		intentapk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                		startActivity(intentapk);
                		
                		
                		
                		
                	}	
        	
                	else	if (myDirectoryFilesHolder.get(position).getDir_image()==9){
                		Toast.makeText(getBaseContext(), "File no DOT   ????? ", Toast.LENGTH_SHORT).show();
                	}
                	//  picture
              
                	else if (myDirectoryFilesHolder.get(position).getDir_image()>=2 &&
                			myDirectoryFilesHolder.get(position).getDir_image()<=5)  {  // is picture  AND NEEDED  !!!!!!!!!!!
                		Toast.makeText(getBaseContext(), "position= "+Integer.toString(position)+" last position= ", Toast.LENGTH_SHORT).show();
//                		Intent i1 = new Intent(getApplicationContext(), showFullScreen1.class);
                		add_prefs_key(showFullScreenMode, "0");
//                		startActivityForResult(new Intent(getApplicationContext(), showFullScreen1.class),3);
                	
//                        ClipboardManager myClipboard;
//                        ClipData myClip;
//             		     myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//           	            myClip = ClipData.newPlainText("text", currentDir1+","+myDirectoryFilesHolder.get(position).getFullFileName());
//           	            myClipboard.setPrimaryClip(myClip);
   
//                   		add_prefs_key(directory, currentDir1);
//                   		add_prefs_key(file_name, myDirectoryFilesHolder.get(position).getFullFileName()); 
Toast.makeText(getBaseContext(), "File is:  "+currentDir1+" "+myDirectoryFilesHolder.get(position).getFullFileName(), Toast.LENGTH_SHORT).show();
         			loadbmp.myCancel();
        	        // Put the String to pass back into an Intent and close this activity
            	        Intent intentb = new Intent();
            	        intentb.putExtra("dirFromBrowserName", currentDir1);
            	        intentb.putExtra("fileFromBrowserName", myDirectoryFilesHolder.get(position).getFullFileName());
               	        intentb.putExtra("resultFromBrowserName", "1");
               	                 	        
            	        setResult(RESULT_OK, intentb);
            			
            			BrowseFileMainActivity.this.finish();
                           	}
           
                	else if ((myDirectoryFilesHolder.get(position).getDir_image()>=11 &&  // is movie
                			myDirectoryFilesHolder.get(position).getDir_image()<=16) || 
                			myDirectoryFilesHolder.get(position).getDir_image()==19 //mkv
                			)  {  
                 		add_prefs_key(showFullScreenMode, "0");
//         		   		startActivity(new Intent(getApplicationContext(), showFullMovie.class));
                	}

                
                	else if ((myDirectoryFilesHolder.get(position).getDir_image()==22 )
                	|| (myDirectoryFilesHolder.get(position).getDir_image()==23 ) ) {  //zip
//Toast.makeText(getBaseContext(), "ZIP= "+Integer.toString(position)+" last position= ", Toast.LENGTH_SHORT).show();
File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
		"/"+currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName());
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setDataAndType(Uri.fromFile(file), "application/zip");
intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//startActivity(intent);              
                	}
                
                
                
                
                
                
                	
                	else  {
                           		Toast.makeText(getBaseContext(), "End with ????   QQQQQ   !!  ", Toast.LENGTH_SHORT).show();
                          
                	}
              
            			      
            	
                }

			
            });
		
				
		//////////////// 
	
		
	}   //////////////////////////////////////////////////////////////////////////////////////////////////// end of on create method

	
	
	private double get_screen_params() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		double wp=dm.widthPixels;
			display_width=dm.widthPixels;
		double xdpi=dm.xdpi;
		double x = Math.pow(wp/xdpi,2);
		
		double hp=dm.heightPixels;
			display_height=dm.heightPixels;
		double ydpi=dm.ydpi;
		double y = Math.pow(hp/ydpi,2);
		double screenInches = Math.sqrt(x+y);
//		display_screen_size=Math.sqrt(x+y);
		
//		Toast.makeText(getBaseContext(), "screenInches=  "+Double.toString(screenInches)
//				+"\n heightPixels=  "+Double.toString(hp)
//				+"\n widthPixels=  "+Double.toString(wp)
//				
//				+"\n ydpi=  "+Double.toString(ydpi)
//				+"\n xdpi=  "+Double.toString(xdpi)
//				+"\n", Toast.LENGTH_SHORT).show();  
	int yyy=0;			       
		return screenInches;
	}

	public void make_new_file()
	{
///////////////////	  file create	
		AlertDialog.Builder alert = new AlertDialog.Builder(BrowseFileMainActivity.this);
		alert.setTitle("Create file");
		alert.setMessage("Enter new file name");
		// Set an EditText view to get user input 
		final EditText input = new EditText(BrowseFileMainActivity.this);
		alert.setView(input);
		input.setFocusable(true);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			File Path = new File(Environment.getExternalStorageDirectory(), currentDir1);
			if(!Path.exists()) {
			    Path.mkdirs();
			}
			File file = new File(Path, input.getText().toString());
			if (!file.exists()) {
		        try {
		            file.createNewFile();
		            } catch (IOException e) {
		            e.printStackTrace();
		        }
			finally {
				get_directory();
			}
			}
			
			else	 {
					Toast.makeText(getBaseContext(), "TESTED  "+input.getText().toString(), Toast.LENGTH_SHORT).show();           
				}
		  }
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		alert.show();
//////////////////////////////////////////////////////////file create	
	}	
	
	
	public void make_new_dir()
	{
///////////////////	  file create	
		AlertDialog.Builder alert = new AlertDialog.Builder(BrowseFileMainActivity.this);
		alert.setTitle("Create directory");
		alert.setMessage("Enter new directory name");
		// Set an EditText view to get user input 
		final EditText input = new EditText(BrowseFileMainActivity.this);
		alert.setView(input);
		input.setFocusable(true);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			File Path = new File(Environment.getExternalStorageDirectory(), currentDir1+"/"+input.getText().toString());
			if(!Path.exists()) {
			    Path.mkdirs();
			}
			//File file = new File(Path, input.getText().toString());
						
			get_directory();
					Toast.makeText(getBaseContext(), "TESTED  "+input.getText().toString(), Toast.LENGTH_SHORT).show();           
				
		  }
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		alert.show();
//////////////////////////////////////////////////////////file create	
	}	
	
	
	
	
	
	
	
	
	
	
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.listView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		//menu.setHeaderTitle(Countries[info.position]);
    	    menu.setHeaderTitle(myDirectoryFilesHolder.get(info.position).getFullFileName());
    		String[] menuItems = new String[]{"Copy", "Move","rename","Delete","Properties","Open with...","Zip","Send to..."};
    		
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
		String[] menuItems = new String[]{"Copy", "Move","rename","Delete","Properties","Open with...","Zip","Send to..."};
		String menuItemName = menuItems[menuItemIndex];
	    String listItemName = (myDirectoryFilesHolder.get(info.position).getFullFileName());
	    
	   // TextView text = (TextView)findViewById(R.id.footer);
	   // text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
//	    Toast.makeText(getBaseContext(), "just "+menuItemName+"  "+listItemName+" *** "+Integer.toString(info.position)+"X"+Integer.toString(menuItemIndex), Toast.LENGTH_SHORT).show();           

	    if (menuItemIndex==0){
	    	copy_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	
	    
	    if (menuItemIndex==1){
	    	move_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }
	    
	    if (menuItemIndex==2){
	    	rename_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	
	    
	    if (menuItemIndex==3){
	    	delete_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	  
	    	    
	    if (menuItemIndex==4){
	      	
	    	property_file_cotext_menu(   info.position     );
	//    	myDirectoryFilesHolder.get(info.position).getFullFileName()  ,
	    	// final String stringnm,
	    }
	    if (menuItemIndex==5){
	    	open_with_file_cotext_menu(   info.position     );
	//    	myDirectoryFilesHolder.get(info.position).getFullFileName()  ,
	    	// final String stringnm,
	    }
	    
	    if (menuItemIndex==6){  // zip
	    	if (myDirectoryFilesHolder.get(info.position).getDir_image()==0){  //is dir
	    	zip_context_menu( ((myDirectoryFilesHolder.get(info.position).getFullFileName()))   );}
	    	else {  // is file
	    		zip_context_menu1( ((myDirectoryFilesHolder.get(info.position).getFullFileName()))   );	    		 	    		
	    	}
	    }	    
	    if (menuItemIndex==7){
	    	send_with_file_cotext_menu(   info.position     );

	    }    
	    
	    return true;
    }
	
    
 
    private void send_with_file_cotext_menu(int position) {
    	if (myDirectoryFilesHolder.get(position).getDir_image()>1){

String email= sharedpreferences.getString(EMAILADDRESS, "");
String email1= sharedpreferences.getString(EMAILADDRESS1, "");

 android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context1.getSystemService(Context.CLIPBOARD_SERVICE);
 
 if (sharedpreferences.getBoolean(EMAILCHECK, false)){
 android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", email);
 clipboard.setPrimaryClip(clip);  // get mail address if needed in clip board 
 }
 else if (sharedpreferences.getBoolean(EMAILCHECK1, false) && !sharedpreferences.getBoolean(EMAILCHECK, false)
		 
		 ){
 android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", email1);
 clipboard.setPrimaryClip(clip);  // get mail address if needed in clip board 
 }
 
 
  
    	final Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("image/jpg");
File photoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
    					"/"+currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName());
//    	final File photoFile = new File(getFilesDir(), "foo.jpg");

    	shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));

    	startActivity(Intent.createChooser(shareIntent, "Share image using"));
  	
Toast.makeText(getBaseContext(), "send to ", Toast.LENGTH_SHORT).show();  
	
    	}
    	else {
    		Toast.makeText(getBaseContext(), "can not send...", Toast.LENGTH_SHORT).show();  
  		
    	}
	}



	private void zip_context_menu(String string) {  // zip dir
Toast.makeText(getBaseContext(), "zip dir***** "+string, Toast.LENGTH_SHORT).show();  

//MakeZipFile mz=new MakeZipFile(context1, string, string, 2);
//mz.delegate=this;
//mz.execute();

	}
    private void zip_context_menu1(String string) {  //zip file
Toast.makeText(getBaseContext(), "zip file***** "+string, Toast.LENGTH_SHORT).show();  

//MakeZipFile mz=new MakeZipFile(context1, currentDir1, string, 1);
//mz.delegate=this;
//mz.execute();

	}


	////////
    
	
	private void open_with_file_cotext_menu(int position) {
	
		//String dr= sharedpreferences.getString(directory, "");
		//String fr= sharedpreferences.getString(file_name, "");
		//String fp = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+currentDir1+"/"+fr ;
	int yyy=myDirectoryFilesHolder.get(position).getDir_image();
  //	Toast.makeText(getBaseContext(), "***** "+yyy, Toast.LENGTH_SHORT).show();  
    if (yyy!=9 && yyy!=7) {
		File flr = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
				"/"+currentDir1+"/"+myDirectoryFilesHolder.get(position).getFullFileName());
				
        Intent intentx = new Intent();
        intentx.setAction(android.content.Intent.ACTION_VIEW);
               
       // MimeTypeMap mime = MimeTypeMap.getSingleton();
       
        //String ext=myDirectoryFilesHolder.get(position).getFullFileName().substring(myDirectoryFilesHolder.get(position).getFullFileName().indexOf(".")+1);
        String ext=myDirectoryFilesHolder.get(position).getext();
        //Toast.makeText(getBaseContext(), "EXT=  "+ext, Toast.LENGTH_SHORT).show(); 
        
        //String type = mime.getMimeTypeFromExtension(ext);
        intentx.setDataAndType(Uri.fromFile(flr),ext);
        
        startActivity(intentx);
    }
	else{
Toast.makeText(getBaseContext(), "Nothing associated with this file type", Toast.LENGTH_LONG).show();  

	}	
	}



	private void copy_file_cotext_menu(String string) {
		paste1000.setVisibility(View.VISIBLE);
		CopyMoveArray.clear();
		CopyMoveArray.add(currentDir1);
		CopyMoveArray.add(string);
		CopyMoveArray.add("c");
	}

	private void move_file_cotext_menu(String string) {
		paste1000.setVisibility(View.VISIBLE);
		CopyMoveArray.clear();
		CopyMoveArray.add(currentDir1);
		CopyMoveArray.add(string);
		CopyMoveArray.add("m");
	}
	
	private void rename_file_cotext_menu(String string4) {
		final String t5=string4;
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Rename file");
		alert.setMessage("From "+t5);
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setFocusable(true);
			input.setText(t5);	
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {

			File Path = new File(Environment.getExternalStorageDirectory(), currentDir1);
			if(Path.exists()){
			    File from = new File(Path,t5);
			    File to = new File(Path,input.getText().toString());
			     if(from.exists())
			        from.renameTo(to);
			     Toast.makeText(getBaseContext(), "Renamed  "+
			        t5+" to "+
			    		 input.getText().toString(), Toast.LENGTH_SHORT).show(); 
			     get_directory();
			}
			
				
			          

		  }
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		alert.show();
				
			
	}

	private void delete_file_cotext_menu(final String stringn) {

    	AlertDialog.Builder adb=new AlertDialog.Builder(BrowseFileMainActivity.this);
        adb.setTitle("Delete/Cancel?");
        adb.setMessage("Are you sure you want to Delete file "+stringn+" ?" );
        adb.setNegativeButton("Cancel", null);
        
        adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
  		
        		File Path = new File(Environment.getExternalStorageDirectory(), currentDir1);

        		if(!Path.exists()) {
        		    Path.mkdirs();
        		}
        		File file = new File(Path, stringn);
        		
        		if (file.exists()) {
        	    	//Toast.makeText(getBaseContext(), "just made it "+Integer.toString(ind1)+
        	    		//	currentDir1+myDirectoryFilesHolder.get(ind1).getFullFileName()+"   *** Exists/deleted   ****", Toast.LENGTH_SHORT).show();           
        	    	boolean b1=file.delete();
        	    	get_directory();
        	    	Toast.makeText(getBaseContext(), "File was deleted ", Toast.LENGTH_SHORT).show();  
        	    }
        	    
        	    else {
        	    	Toast.makeText(getBaseContext(), "File not deleted ", Toast.LENGTH_SHORT).show();           

        	    }
     	        
                       }});
       
        adb.show();	
	
	}

	private void property_file_cotext_menu( int positionm) {

		AlertDialog.Builder adb=new AlertDialog.Builder(BrowseFileMainActivity.this);
        adb.setTitle("File properties ");
		String isDir1="";
        if (myDirectoryFilesHolder.get(positionm).getAttrib().contains("d")) {
   	isDir1="Dir size:		"+myDirectoryFilesHolder.get(positionm).getParsedSize()+" \n";
        			}
		else{
isDir1="File size:		"+myDirectoryFilesHolder.get(positionm).getParsedSize()+" \n";
		}
        adb.setMessage(
        "File name:		"+myDirectoryFilesHolder.get(positionm).getFullFileName()+"\n"+
        "File date:		"+myDirectoryFilesHolder.get(positionm).getDate_created()+"\n"+
        "File attrib:	"+myDirectoryFilesHolder.get(positionm).getAttrib()+" \n"+	
        isDir1+
        "LEN= "+Integer.toString(myDirectoryFilesHolder.get(positionm).getFullFileName().length())
        +" "+Double.toString((double) get_screen_params())
        
        		);
  
        adb.setNegativeButton("OK", null);
  
        adb.show();	
	
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//currentDir1	= sharedpreferences.getString(directory, "");
		//position_ 	= sharedpreferences.getString(position_location, "");
//		Toast.makeText(getBaseContext(), "On resume  "+sharedpreferences.getString(position_location, ""), Toast.LENGTH_SHORT).show();
		
		//get_directory();
    //	sort(sort_main_);
    //	listView.setSelection(Integer.valueOf(position_));
    	//adapter.notifyDataSetChanged();	
				
	}



	private void get_directory() {
		String correct_separated;
		int size1=0,dir_type=0 ;
		myDirectoryFilesHolder.clear();
		fileBMPs.clear();
		bitmapArray.clear();
		//myDirectoryFilesHolder.clear();
		//Path = new File("/storage/emulated/0/FileInputOutput" );  //  working
		//Path = new File("/storage/emulated/0/DCIM" );  //  working
		//Path = new File("/storage/emulated/0/DCIM/camera/" );  //  not working
		//Path = new File("/mnt/sdcard/DCIM" );  // not working
			Path1 = new File(Environment.getExternalStorageDirectory(), currentDir1);  //ok
		//Path1 = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");  //ok shows 3 files - all are camera
		
		if(!Path1.exists()) {
		    Path1.mkdirs();
		    Toast.makeText(getBaseContext(), "path does not exist.", Toast.LENGTH_SHORT).show();
		}
		
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			
			Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
		    finish();
		    return;
		    		}
		else {
						
			enterDirName.setText("path:"+currentDir1);
			add_prefs_key(directory, currentDir1);
			files = Path1.listFiles();
			
			
			for (int i=0;i<files.length;i++){
				dir_type = 9;  // un-known file tyep
				String[] separated = files[i].toString().split("/");
				String fileAttrib="";
				correct_separated=separated[separated.length-1];	
				File file1 = new File(Path1, correct_separated);
				//	int it=getFileTypeID(file1,correct_separated);
					SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
	 	            formatter.setLenient(false); 
	 	               
 	                Date lastModified = new Date(file1.lastModified());
 	                long curMillis = lastModified.getTime();
 	                String lastModified1 = formatter.format(lastModified); 
 	               
 	                
 	                if (!file1.isDirectory()) {
 	                   	size1 = (int) (file1.length());  // file size
 	                   	fileAttrib+=" ";	               
 	                		}
 	                else {

 	                   
 	                		dir_type = 0;  // file type is dir
 							File childfile[] = file1 .listFiles();
							size1=childfile.length;
							fileAttrib+="d";
							
 	                	    }
 	               if (file1.canRead()) {
	                	fileAttrib+="r";
	                }
	                else {
	                	fileAttrib+=" ";
	                }
	               if (file1.canWrite()) {
	            	  fileAttrib+="w";
	                }
	                else {
	                	fileAttrib+=" ";
	                }
	             if (file1.canExecute()) {
	            	 fileAttrib+="e";
	                }
	             else {
	                	fileAttrib+=" ";
	                }
 	                
 	             if (!file1.isDirectory()) {
 	                    	dir_type=getFileTypeID(correct_separated) ;  
 	                   if (  (dir_type==2 || dir_type==3 || dir_type==4 || dir_type==5) ){
 	                	   fileBMPs.add(correct_separated);
  	                   				}
 	                    	}   
 	                    
 	    
                    // show only non system files - system starts with ".'
        myDirectoryFilesHolder.add(new DirectoryFilesHolder(dir_type, size1,
		files.length, correct_separated, lastModified1,	correct_separated, i+1,fileAttrib , curMillis , null ));
                 
             	// Field 1 --file type:
 	             			//0 non empty dir
 	             			// 1 empty dir
 	             			// 2 PNG file
 	             			//3 JPG file
 	             			//4 BMP file
 	             			//5 GIF file
 	             			// 6 TXT file
 	             // 7 APK
 	             //8 .file
 	             			//9 other type - non handled
 	             	// Field 2 --file size
 	             	// Field 3 --file size   optional
 	             	// Field 4 --file name (truncated)
 	             	// Field 5 --file date
 	             
 	         	}
			
//             for (int ip=0;ip<fileBMPs.size();ip++){
//Toast.makeText(getBaseContext(), "@@@ "+fileBMPs.get(ip)+"   "+fileBMPs.size(), Toast.LENGTH_SHORT).show();
//             }

sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
			//update_asterisk(invalidate_asterisk2);
			
		loadbmp = new loadBitmaps();
            loadbmp.execute();	
			adapter.notifyDataSetChanged();
	listView.invalidate();		

//		listView.smoothScrollToPosition(adapter.getCount() -3);		
listView.smoothScrollToPosition(Integer.valueOf(sharedpreferences.getString(position_location, ""))+
		(listViewChildrenCount)/2);		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
showTitleActionBar();
	
		}
			
	}
	

	private int getFileTypeID(String correct_separated2) {
		int ret=9;
//		if (!file1.isDirectory()){
		for (int i=0;i<f_types.length;i++){
			if ( correct_separated2.toUpperCase(Locale.US).endsWith(f_types[i]) &&
					!correct_separated2.startsWith(".")
					){
				ret=i;
				break;
				}	
			} 
		
//		}
		return ret;
	}



	private void get_default_path() {
		Path_def = new File(Environment.getExternalStorageDirectory(), "");
		Path_default=Path_def.toString();
		Toast.makeText(getBaseContext(), "DEF PATH= "+Path_default+" "+Integer.toString( Path_default.length() ), Toast.LENGTH_SHORT).show();
	}
	
	private void init_all_prefs() {
		//SharedPreferences.Editor editor = sharedpreferences.edit();
	
		add_prefs_key(Initialized, Initialized);
		add_prefs_key(size_x, "4");
		add_prefs_key(size_y, "4"); 
		add_prefs_key(directory, "");
		add_prefs_key(file_name, "NULL");
		add_prefs_key(sort_type, "1");
		add_prefs_key(position_location, "1"); 
		add_prefs_key(sort_type_Main_Activity, "1");
		add_prefs_key(myVeryFavouriteDir, "");
		add_prefs_key(fitFullScreen, "YES");
		add_prefs_key(volume, "0.4");
		add_prefs_key(EMAILADDRESS, "noname@gmail.com");
		add_prefs_key(EMAILADDRESS1, "noname1@gmail.com");
		add_prefs_keyboolean(EMAILCHECK, false);
		add_prefs_keyboolean(EMAILCHECK1, false);
		
		
	//editor.apply();
	}
    




	private void read_all_prefs() {
	
		size_x_ = sharedpreferences.getString(size_x, "");
		size_y_ = sharedpreferences.getString(size_y, "");
		currentDir1	= sharedpreferences.getString(directory, "");
		Fname	= sharedpreferences.getString(file_name, "");
		sort_ 	= sharedpreferences.getString(sort_type, "");
		position_ 	= sharedpreferences.getString(position_location, "");
		sort_main_ 	= sharedpreferences.getString(sort_type_Main_Activity, "");
		favour_ 	= sharedpreferences.getString(myVeryFavouriteDir, "");
			}
    
   
	private void add_prefs_key(String string, String string2) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putString(string, string2).apply();
		}
	
	private void add_prefs_keyboolean(String emailaddress2, boolean b) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putBoolean(emailaddress2, b).apply();
		
	}	
	
	
	
	private void parse_current_dir() {
		String[] separated_up = currentDir1.split("/");
		currentDir1="";
			for (int ind=0;ind<separated_up.length-1;ind++){ 
			currentDir1+=separated_up[ind]+"/";
			}
			if (currentDir1.endsWith("/") ) {
				currentDir1=currentDir1.substring(0, currentDir1.length()-1);
				}
			Toast.makeText(getApplicationContext(), "from parse: current "+currentDir1, Toast.LENGTH_LONG).show();

		goBackDir.add(new CurrentDirectoryLastHolder(currentDir1, ""));
		add_prefs_key(directory, currentDir1);
		get_directory();

	}
	
//	if (goBackDir.isEmpty()) {
//		Toast.makeText(getApplicationContext(), "Top of Stack - can't go back !!! ", Toast.LENGTH_SHORT).show();
//	}
//	else {
//		
//		currentDir1 = goBackDir.remove(goBackDir.size()-1).getCurrentDir(); 
//		Toast.makeText(getApplicationContext(), "current "+currentDir1, Toast.LENGTH_SHORT).show();

//
//	}
	
	
	
	
	
	
	
	
	
	private void go_grid() {
		loadbmp.myCancel();
		add_prefs_key(directory, currentDir1);
//		Toast.makeText(getBaseContext(), "GO:    "+currentDir1, Toast.LENGTH_SHORT).show();
//    	Intent i1 = new Intent(getApplicationContext(), showGrid1.class);
//    	startActivityForResult(i1,4);
	
		get_directory();
	}
	
	public static void folderdel(File path, String string){
	    File f= new File(path,string);
	    if(f.exists()){
	        String[] list= f.list();
	        if(list.length==0){
	            if(f.delete()){
	            	return;
	            }
	        }
	        else {
	            for(int i=0; i<list.length ;i++){
	                File f1= new File(path,list[i]);
	                if(f1.isFile()&& f1.exists()){
	                    f1.delete();
	                }
	                if(f1.isDirectory()){
	                    folderdel(path,f1.toString());
	                }
	            }
	            //folderdel(path);
	         //   Toast.makeText(MainActivity.getBaseContext().this, "just made it ", Toast.LENGTH_SHORT).show();           
    	    	
	        }
	    }
	}
	//getBaseContext
	
		private void copy_file(){
	
		File PathTO = new File(Environment.getExternalStorageDirectory(), currentDir1);
		File PathFrom = new File(Environment.getExternalStorageDirectory(), CopyMoveArray.get(0));
		File to =new File (PathTO,CopyMoveArray.get(1));    
		File from = new File(PathFrom,CopyMoveArray.get(1));
		
		
		InputStream in = null;
		try {
			in = new FileInputStream(from);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(to);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Copy the bits from instream to outstream
		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally 
		{
			get_directory();
		}
		
		
	}

	private void delete___file() {
		File Path = new File(Environment.getExternalStorageDirectory(), CopyMoveArray.get(0));

		if(!Path.exists()) {
		    Path.mkdirs();
		}
		File file = new File(Path, CopyMoveArray.get(1));
		
		if (file.exists()) {
	    	//Toast.makeText(getBaseContext(), "just made it "+Integer.toString(ind1)+
	    		//	currentDir1+myDirectoryFilesHolder.get(ind1).getFullFileName()+"   *** Exists/deleted   ****", Toast.LENGTH_SHORT).show();           
	    	boolean b1=file.delete();
	    	get_directory();
	    	Toast.makeText(getBaseContext(), "File was deleted ", Toast.LENGTH_SHORT).show();  
	    }
	    
	    else {
	    	Toast.makeText(getBaseContext(), "File not deleted ", Toast.LENGTH_SHORT).show();           

	    	}
	}

	
	
	
	/*
	private void hide_kbd() {
		// TODO Auto-generated method stub

this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(enterDirName.getWindowToken(), 0);


	}
	*/
	
	private class MyListAdapter extends ArrayAdapter<DirectoryFilesHolder> {
		public MyListAdapter() {
			super(BrowseFileMainActivity.this, R.layout.item_view, myDirectoryFilesHolder);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.filebrowseitem_view, parent, false);
			}
			
			DirectoryFilesHolder currentDir = myDirectoryFilesHolder.get(position);
			
			ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
			ImageView imageView1 = (ImageView)itemView.findViewById(R.id.last_icon);
			TextView item_attrib = (TextView) itemView.findViewById(R.id.item_attrib);
			TextView file_name = (TextView) itemView.findViewById(R.id.name);
			TextView condionText = (TextView) itemView.findViewById(R.id.item_txtCondition);
			TextView size = (TextView) itemView.findViewById(R.id.size);
			TextView date1 = (TextView) itemView.findViewById(R.id.date1);
			
			android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
			params.height = getResources().getDimensionPixelSize(R.dimen.height);
			imageView.setLayoutParams(params);
			
			android.view.ViewGroup.LayoutParams paramsv = imageView1.getLayoutParams();
			paramsv.height = getResources().getDimensionPixelSize(R.dimen.height4);
			imageView1.setLayoutParams(paramsv);

			android.view.ViewGroup.LayoutParams params1 = file_name.getLayoutParams();
			params1.height = getResources().getDimensionPixelSize(R.dimen.height2);

			file_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
		getResources().getDimension(R.dimen.font_size1));			
//file_name.setBackgroundColor(Color.YELLOW);
file_name.setTypeface(null, Typeface.BOLD);
file_name.setLayoutParams(params1);

			android.view.ViewGroup.LayoutParams params2 = date1.getLayoutParams();
			params2.height = getResources().getDimensionPixelSize(R.dimen.height3);
			date1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size3));
//			date1.setBackgroundColor(Color.GREEN);
			date1.setLayoutParams(params2);
	
			android.view.ViewGroup.LayoutParams params3 = size.getLayoutParams();
			params3.height = getResources().getDimensionPixelSize(R.dimen.height3);
size.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size4));
//size.setBackgroundColor(Color.YELLOW);
size.setLayoutParams(params3);

			android.view.ViewGroup.LayoutParams params4 = item_attrib.getLayoutParams();
			params4.height = getResources().getDimensionPixelSize(R.dimen.height4);
			item_attrib.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size4));
			item_attrib.setLayoutParams(params4);
			
//			ViewGroup.MarginLayoutParams p8 = 
//					(ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
//				p8.setMargins(0,0,0,0);
//				p8.height=h;
//				p8.width=h;
//				p8.height=((display_height*90)/100)/12;
//				p8.width=((display_height*90)/100)/12;
//				p8.width=(2*display_width)/16;
//				imageView.requestLayout(); 		

//			ViewGroup.MarginLayoutParams p81 = 
//						(ViewGroup.MarginLayoutParams) imageView1.getLayoutParams();
//				p81.setMargins(0,((display_height*90)/100)/108,display_width/40,0);
//				p81.height=((display_height*90)/100)/36;
//				p81.width=((display_height*90)/100)/36;
//				p81.height=h;
//
//				imageView1.requestLayout(); 			
			
//				ViewGroup.MarginLayoutParams p82 = 
//						(ViewGroup.MarginLayoutParams) file_name.getLayoutParams();
//				p82.setMargins(display_width/50,0,0,0);
//p82.height=h;
				//				p82.height=((display_height*90)/100)/20;
//				p82.width=((display_height*90)/100)/36;
//				file_name.setTextSize(((display_height*90)/100)/102);
//				file_name.setBackgroundColor(Color.YELLOW);
//				file_name.requestLayout(); 
	
//				ViewGroup.MarginLayoutParams p83 = 
//						(ViewGroup.MarginLayoutParams) date1.getLayoutParams();
//				p83.setMargins(display_width/40,((display_height*90)/100)/250,0,0);
//				p83.height=((display_height*90)/100)/30;
//p83.height=h*2/3;
//				p82.width=((display_height*90)/100)/36;
//				date1.setTextSize(((display_height*90)/100)/148);
//				date1.setBackgroundColor(Color.GREEN);
//				date1.requestLayout(); 				
				///////////////////////////////////////
//				ViewGroup.MarginLayoutParams p84 = 
//						(ViewGroup.MarginLayoutParams) size.getLayoutParams();
//				p84.setMargins(display_width/40,((display_height*90)/100)/250,0,0);
//				p84.height=((display_height*90)/100)/36;
//p84.height=h*2/3;				
//				p82.width=((display_height*90)/100)/36;
//				size.setTextSize(((display_height*90)/100)/168);
//				size.setBackgroundColor(Color.YELLOW);
//				size.requestLayout(); 	
	
				
				
				
//				ViewGroup.MarginLayoutParams p85 = 
//						(ViewGroup.MarginLayoutParams) item_attrib.getLayoutParams();
//				p85.setMargins(display_width/10,((display_height*90)/100)/250,0,0);///////////////////////////
//				p85.height=((display_height*90)/100)/36;
//				p85.height=h*2/3;	
//				item_attrib.setTextSize(((display_height*90)/100)/168);
//				item_attrib.setBackgroundColor(Color.MAGENTA);
//				item_attrib.requestLayout(); 	
//			
								
				
				
			item_attrib.setText(currentDir.getAttrib());

			
			
//			int img=currentDir.getDir_image();
		
			//0 non empty dir
  			// 1 empty dir
  			// 2 PNG file
  			//3 JPG file
  			//4 BMP file
  			//5 GIF file
  			// 6 TXT file
			// 7 apk file
			// .file
  			//9 other type
			
				
			if (currentDir.getBMP()!=null){
				imageView.setImageBitmap(currentDir.getBMP());	
			}
			else if (currentDir.getDir_image()==0 || currentDir.getDir_image()==1){
				imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.folder1));  

			}
			else {
//			Bitmap	b8=BitmapFactory.decodeResource(getResources(), R.drawable.noimageavailable);

				imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.noimageavailable));  
			}
//			else imageView.setImageResource(    resid_[img]  );
//			b8=BitmapFactory.decodeResource(getResources(), R.drawable.noman);
			
			
			file_name.setTextColor(Color.BLACK);

//			if (currentDir.getPositionAsterisk()==1) {
//				file_name.setTextColor(Color.BLUE);
//			}
//			 else file_name.setTextColor(Color.BLACK);

			int limit1=0;
			if (display_screen_size>5 && display_screen_size <6){
				limit1=22;
			}
			else if (display_screen_size>7 && display_screen_size <8){
				limit1=40;
			}
			else if (display_screen_size>7.9 && display_screen_size <9.9){
				limit1=60;
			}
			else {
				limit1=58;
			}
			
			if (currentDir.getFullFileName().length()<limit1){
		
				file_name.setText(currentDir.getFullFileName()+" "+String.valueOf(currentDir.getDir_image()));
			}
			
			else {
				file_name.setText(currentDir.getFullFileName().substring(0, limit1));
			}
			
			// total files:
			if (currentDir.getAttrib().contains("d")){
				size.setText(currentDir.getParsedSize());
			}
			else {			
			
			
			
			int a4a=currentDir.getTotal_files();
			String a4;
			if (a4a<1024) {
				a4=String.valueOf(currentDir.getTotal_files()+" b")  ;
			}
			else if (a4a<1048576) {
				a4=String.valueOf(currentDir.getTotal_files()/1024)+" kb"  ;
			}
			
			else {
				a4=String.valueOf(currentDir.getTotal_files()/1048576)+" mb"  ;
			}
			 
			size.setText(a4);
			}
			// image files:
			String a5r=String.valueOf(currentDir.getImage_files())  ;
			String a5t=String.valueOf(currentDir.getIndex())  ;
			if (a5t.length()==1){
				a5t="  "+a5t;
			}
			else if (a5t.length()==2){
				a5t=" "+a5t;
			}
			condionText.setText(a5t+"/"+a5r);
			
			date1.setText(currentDir.getDate_created());

			imageView1 .setVisibility(View.INVISIBLE);
		
			
for (int u=goBackDir.size()-1;u>=0;u--){
if (currentDir1.equals(goBackDir.get(u).getCurrentDir())	&& 
		goBackDir.get(u).getLast().equals(currentDir.getFullFileName()) )
		{imageView1 .setVisibility(View.VISIBLE);file_name.setTextColor(Color.BLUE);break;}
	
}

//			 else imageView1 .setVisibility(View.INVISIBLE);
			 
			return itemView;
		}				
	}
    
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filebrose1main, menu);
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
			init_all_prefs();
			read_all_prefs(); 
			currentDir1="";
			goBackDir.clear();
			goBackDir.add(new CurrentDirectoryLastHolder(currentDir1, ""));
			get_directory();
			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));

	showDialog();		
			
			break;
		
		case R.id.about:
//	    	getAbout1 ga =new getAbout1(context1); // this is really the about
//	    	ga.showAlertDialog2(ga.getAppInfo());	// this is really the about
			String s="";
			for (int i=0;i<goBackDir.size();i++){
				s+="\n"+goBackDir.get(i).getCurrentDir()+"  "+goBackDir.get(i).getLast();
				
			}
			
			
			
			//act_help();
Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show(); 
			break;
			
		case R.id.refresh:	
			currentDir1	= sharedpreferences.getString(directory, "");
			get_directory();
			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
			Toast.makeText(getApplicationContext(), "Refreshed...  ", Toast.LENGTH_SHORT).show(); 
			
			break;
			
		case R.id.exit:
			loadbmp.myCancel();
   	        Intent intentb = new Intent();
	        intentb.putExtra("dirFromBrowserName", "");
	        intentb.putExtra("fileFromBrowserName", "");
   	        intentb.putExtra("resultFromBrowserName", "0");
	        setResult(RESULT_OK, intentb);

			BrowseFileMainActivity.this.finish();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.go:
			go_grid();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;

		case R.id.rec1:
		
			if (currentDir1.length()==0 || currentDir1.equals("") || currentDir1.equals("/")){
				Toast.makeText(getApplicationContext(), "Can not record to root folder", Toast.LENGTH_LONG).show(); 
			}
			else rec1();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.goup:
			parse_current_dir();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.SortTitleAsc:
			Toast.makeText(getApplicationContext(), "pressed 1", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "1");
			sort("1");
			//get_directory();
			break;
			
		case R.id.SortTitleDsc:
			Toast.makeText(getApplicationContext(), "pressed 2", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "2");
			sort("2");
			//get_directory();
			break;
		
			
		case R.id.SortDateAsc:
			Toast.makeText(getApplicationContext(), "pressed 3", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "3");
			sort("3");
			//sort date not working !!!!!!!!!
			//sort("1");
			//get_directory();
			break;
			
		case R.id.SortDateDsc:
			Toast.makeText(getApplicationContext(), "pressed 4", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "4"); 
			sort("4");
			//sort date not working !!!!!!!!!
			//sort("2");
			//get_directory();
			break;
		case R.id.SortSizeAsc:
			Toast.makeText(getApplicationContext(), "pressed 5", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "5");
			sort("5");
			//get_directory();
			break;
			
		case R.id.SortSizeDsc:
			Toast.makeText(getApplicationContext(), "pressed 6", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "6");
			sort("6");
			//get_directory();
			break;
	
		case R.id.pdf:
			Toast.makeText(getApplicationContext(), "pdf", Toast.LENGTH_SHORT).show();

//Intent i199 = new Intent(getApplicationContext(), ReadPdf_MainActivity.class);
//startActivity(i199);
			break;
			
		case R.id.setemail:
			loadbmp.myCancel();
			getEMAILAddress();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
			
				
		}
		return super.onOptionsItemSelected(item);
	}
	

	private void rec1() {
		loadbmp.myCancel();
		add_prefs_key(directory, currentDir1);
		Toast.makeText(getBaseContext(), "GO:    "+currentDir1, Toast.LENGTH_SHORT).show();
//    	Intent i199 = new Intent(getApplicationContext(), audiorec1.class);
//startActivityForResult(i199, 9);
    	//    	startActivityForResult(i1, 2);
	
//		get_directory();
		
	}



	private void sort(String string) {
		
		//sort_main_ 	= sharedpreferences.getString(sort_type_Main_Activity, "");
		//add_prefs_key(sort_type_Main_Activity, string); 
		int sort1=1;			
		if (string.length()==0 || string.length()>1 || string.equals("0") ) {
			add_prefs_key(sort_type_Main_Activity, "1");
			sort1=1;
			}
		
		else {
			add_prefs_key(sort_type_Main_Activity, string);
			sort1=Integer.valueOf(string);
			}
			
		boolean flag_sort=true;
		DirectoryFilesHolder temp_holder_sort;
		enterDirName.setText("path:"+currentDir1);
		//enterDirName.setText("path:"+Path1.toString());
		//actionBar.setTitle("View "+Integer.toString(myDirectoryFilesHolder.get(0).getDate_created().length()));
				
		
		if (sort1==1) {  // sort by name ASC
			//String vv=myDirectoryFilesHolder.get(position).getFullFileName().toLowerCase();
			//  private DirectoryFilesHolder temp_holder_sort;
			//  myDirectoryFilesHolder.get(position).getDir_image()==0
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		          		            	
		                 	if (myDirectoryFilesHolder.get(j_sort).getFullFileName().compareToIgnoreCase(myDirectoryFilesHolder.get(j_sort+1).getFullFileName())>0) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            		           
		      } 
		     		     
		    				
		}
		
		else if (sort1==2) {  // sort by name DSC
			
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		          		            	
		                 	if (myDirectoryFilesHolder.get(j_sort).getFullFileName().compareToIgnoreCase(myDirectoryFilesHolder.get(j_sort+1).getFullFileName())<0) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            
		      } 
		     			
		}
		
		else if (sort1==3) {  // sort by date  ASC
			
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		          		            	
		                 	if (myDirectoryFilesHolder.get(j_sort).getParsedSeparatedDate_in_millis()>(myDirectoryFilesHolder.get(j_sort+1).getParsedSeparatedDate_in_millis())) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            
		      } 
		     		
		}
		
		else if (sort1==4) {  // sort by date  DSC
			
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		            		
		            	if (myDirectoryFilesHolder.get(j_sort).getParsedSeparatedDate_in_millis()<(myDirectoryFilesHolder.get(j_sort+1).getParsedSeparatedDate_in_millis())) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            
		      } 
		     			
		}
		
		////////// 
		else if (sort1==5) {  // sort by size  ASC   
			
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		          		         	
		                 	if (myDirectoryFilesHolder.get(j_sort).getTotal_files()>myDirectoryFilesHolder.get(j_sort+1).getTotal_files()) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            
		      }
		     			
		}
		
		else if (sort1==6) {  // sort by size  DSC
			
		     while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
		          		            	
		            	if (myDirectoryFilesHolder.get(j_sort).getTotal_files()<myDirectoryFilesHolder.get(j_sort+1).getTotal_files()) {
		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
		                 		           flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		            
		      }
		     		
		}
		
	for (int i=0;i<myDirectoryFilesHolder.size();i++){
		myDirectoryFilesHolder.get(i).setIndex(i+1);
		}
	
	     adapter.notifyDataSetChanged();	
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);
	    
	     
	        //Check that request code matches ours:
	     // check if the request code is same as what is passed  here it is 2
	        if(requestCode==2)
	              {
	                 // fetch the message String
	                  String message=data.getStringExtra("MESSAGE");
	           	        
	                  currentDir1	= sharedpreferences.getString(directory, "");
	      			get_directory();
	      			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
	                  
	                  
	                  
	                  //Toast.makeText(getApplicationContext(), "text edit returned!"+message, Toast.LENGTH_SHORT).show();  
	                  
	              }
	        if(requestCode==3)
            {
               // fetch the message String
                String message=data.getStringExtra("MESSAGE");
         	        
                currentDir1	= sharedpreferences.getString(directory, "");
    			get_directory();
    			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
                
                
                
                //Toast.makeText(getApplicationContext(), "text edit returned!"+message, Toast.LENGTH_SHORT).show();  
                
            }
	        if(requestCode==4)
            {
                String message=data.getStringExtra("MESSAGE");
                currentDir1	= sharedpreferences.getString(directory, "");
    			get_directory();
    			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
                 //Toast.makeText(getApplicationContext(), "text edit returned!"+message, Toast.LENGTH_SHORT).show();  
                
            }
	        
	        if(requestCode==9)
            {
                String message=data.getStringExtra("MESSAGE");
                currentDir1	= sharedpreferences.getString(directory, "");
    			get_directory();
    			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
                 //Toast.makeText(getApplicationContext(), "text edit returned!"+message, Toast.LENGTH_SHORT).show();  
                
            }
	    }
	
	public void onBackPressed() {
//		long l=System.currentTimeMillis();
//		if (l-lastTimeStampBackpressed<350) {
//			exitCounter++;
//			}
//		else {
//		exitCounter=0;	
//		}
//		lastTimeStampBackpressed=l;
//		if (exitCounter==4) finish();
		loadbmp.myCancel();
		parse_current_dir();
		add_prefs_key(directory, currentDir1);
		get_directory();
		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
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
		"\n\nCreated date:\t "+s+""+
		"\nFileSize:\t\t\t "+String.valueOf(size)+" bytes";
	}
	
	private void showDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(BrowseFileMainActivity.this).create();
		alertDialog.setTitle("About DirectoryShow...");
		alertDialog.setMessage(getModification());
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	
	}
	
	
	
	
	/////// async task

	class loadBitmaps extends AsyncTask<Void, Void, Void>    {
		boolean isCanceled = false;

        public void myCancel()
        {
            isCanceled = true;
        }
        
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			inTaskProgerss=true;
imageLoadErrorCounter=0;
		}

        @Override
        protected Void doInBackground(Void... params) {
        	
        	inTaskProgerss=true;
        	bitmapArray.clear();
        	int mHeight_= 60;//(int) ((tablet_width-3*margin_left-3*margin_right)/3);  // overrides the cooeff table
			int mWidth_= 60;//(int) ((tablet_width-3*margin_left-3*margin_right)/3);

            for (int r=0; r<fileBMPs.size() ; r++){
            	if (isCanceled)
                {
                    return null;
                }
            	try {
//           		   File file = new File(Path1, fileBMPs.get(r));
//  			Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), mHeight_,mWidth_);
// Bitmap rotated =  Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),null,false);
 // 			bitmapArray.add(bitmap);
 
 bm=null;           		
 bm=getResizedBitmap(mWidth_,mHeight_,new File(Path1, fileBMPs.get(r)).toString());
// bitmapArray.add(getResizedBitmap(mWidth_,mHeight_,new File(Path1, fileBMPs.get(r)).toString())        );			
  if (bm!=null) bitmapArray.add(bm);			
  
  else {
	  imageLoadErrorCounter++;
	  Bitmap icon = BitmapFactory.decodeResource(context1.getResources(),
              R.drawable.badimage100);
	  bitmapArray.add(icon);
  }	
//bitmapArray.add(Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),null,false));
	for (int ip=0;ip<myDirectoryFilesHolder.size();ip++){
		DirectoryFilesHolder cur = myDirectoryFilesHolder.get(ip);
		for (int il=0;il<bitmapArray.size();il++){
		if (myDirectoryFilesHolder.get(ip).getFullFileName().equals(fileBMPs.get(il)) ){
			cur.setBMP(bitmapArray.get(il));
			myDirectoryFilesHolder.set(ip, cur);
		
			break;
									}

		
		
								}

						}	
  
            	}
        	        
        	      catch (Exception e) {   	  }
       finally {
       		} 	        
   	        publishProgress();
                	}
			return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            inTaskProgerss=false;
showTitleActionBar();
//            Toast.makeText(MainActivity.this, "Images download done\n "+
//            "Total files "+fileBMPs.size()+" \n"+
//            	"Total  errors= "+imageLoadErrorCounter	
//            		,  Toast.LENGTH_SHORT).show();    
            imageLoadErrorCounter=0;
            adapter.notifyDataSetChanged();
			
   
        } 

        
        protected void onProgressUpdate(Void... values) {
        	
			adapter.notifyDataSetChanged();
showTitleActionBar();	
            }

}  // end of class
	

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
	   { // BEST QUALITY MATCH
	       
	       //First decode with inJustDecodeBounds=true to check dimensions
	       final BitmapFactory.Options options = new BitmapFactory.Options();
	       options.inJustDecodeBounds = true;
	       BitmapFactory.decodeFile(path, options);

	       // Calculate inSampleSize, Raw height and width of image
	       final int height = options.outHeight;
	       final int width = options.outWidth;
	       options.inPreferredConfig = Bitmap.Config.RGB_565;
	       int inSampleSize = 1;

	       if (height > reqHeight) 
	       {
	           inSampleSize = Math.round((float)height / (float)reqHeight);
	       }
	       int expectedWidth = width / inSampleSize;

	       if (expectedWidth > reqWidth) 
	       {
	           //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
	           inSampleSize = Math.round((float)width / (float)reqWidth);
	       }

	       options.inSampleSize = inSampleSize;

	       // Decode bitmap with inSampleSize set
	       options.inJustDecodeBounds = false;

	       return BitmapFactory.decodeFile(path, options);
	   }
	
	public Bitmap getResizedBitmap(int targetW, int targetH,  String imagePath) {

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    //inJustDecodeBounds = true <-- will not load the bitmap into memory
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imagePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
	    return(bitmap);
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

if (width==0 || height==0) {
	
			 height=vv.getHeight();
	         width=vv.getWidth();
		       
ViewGroup.MarginLayoutParams pc = 
(ViewGroup.MarginLayoutParams) enterDirName.getLayoutParams();
pc.setMargins(width/80,0,width/80,0);
pc.height=height/33;
pc.width=width;
enterDirName.setTextSize(height/110);
enterDirName.requestLayout();	      
		       
		       
ViewGroup.MarginLayoutParams p = 
(ViewGroup.MarginLayoutParams) favor1000.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
favor1000.requestLayout();
           
ViewGroup.MarginLayoutParams p1 = 
(ViewGroup.MarginLayoutParams) exit.getLayoutParams();
p1.setMargins(width/80,height/240,width/80,0);
p1.height=height/20;
p1.width=width/13;
exit.requestLayout();
       		              
ViewGroup.MarginLayoutParams p2 = 
(ViewGroup.MarginLayoutParams) paste1000.getLayoutParams();
p2.setMargins(width/80,height/240,width/80,0);
p2.height=height/20;
p2.width=width/13;
paste1000.requestLayout();        		              
               		           		
ViewGroup.MarginLayoutParams p4 = 
(ViewGroup.MarginLayoutParams) go.getLayoutParams();
p4.setMargins(width/80,height/240,width/80,0);
p4.height=height/20;
p4.width=width/13;
//p4.height=100;p4.width=300;
go.requestLayout();
       		              
ViewGroup.MarginLayoutParams p3 = 
(ViewGroup.MarginLayoutParams) dir_up.getLayoutParams();
p3.setMargins(width/80,height/240,width/80,0);
p3.height=height/20;
p3.width=width/13;
dir_up.requestLayout();   
        		           		
ViewGroup.MarginLayoutParams p8 = 
(ViewGroup.MarginLayoutParams) back.getLayoutParams();
p8.setMargins(width/80,height/240,width/80,0);
p8.height=height/20;
p8.width=width/13;
back.requestLayout(); 

ViewGroup.MarginLayoutParams p6 = 
(ViewGroup.MarginLayoutParams) new_file1.getLayoutParams();
p6.setMargins(width/80,height/240,width/80,0);
p6.height=height/20;
p6.width=width/13;
new_file1.requestLayout(); 
listViewChildrenCount=listView.getChildCount();
	}
//Toast.makeText(this, "@@@ "+"   "+mz1.size()+"  "+mz.getready(), Toast.LENGTH_SHORT).show();

super.onWindowFocusChanged(hasFocus);
	}
	
private void showTitleActionBar(){
	int yyy666=adapter.getCount();
	int yyy777=	listView.getFirstVisiblePosition();     
	int yyy888=listView.getLastVisiblePosition();
	int yyy999 = listView.getChildCount();	
	actionBar.setTitle("View "+" sort: "+sharedpreferences.getString(sort_type_Main_Activity, "")
	+" "+exitCounter
//			" # "+String.valueOf(myDirectoryFilesHolder.size())+" * "+
//			String.valueOf(bitmapArray.size())
//			+"  cnt "+yyy666+
//			"  f "+yyy777+"  l "+yyy888+
//			"  chld "+yyy999+" "+sharedpreferences.getString(position_location, "")
//+" "+sharedpreferences.getString(file_name, "")
//	+" "+listViewChildrenCount		
			);
//Toast.makeText(getBaseContext(), "997 "+sharedpreferences.getString(position_location, ""), Toast.LENGTH_SHORT).show();
//	listView.smoothScrollToPosition(Integer.valueOf(sharedpreferences.getString(position_location, ""))+
//			(listView.getChildCount())/2);	
}	


private String saveToInternalStorage(Bitmap bitmapImage){
    ContextWrapper cw = new ContextWrapper(getApplicationContext());
     // path to /data/data/yourapp/app_data/imageDir
    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    // Create imageDir
    File mypath=new File(directory,"profile.jpg");

    FileOutputStream fos = null;
    try {           
        fos = new FileOutputStream(mypath);
   // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
    } catch (Exception e) {
          e.printStackTrace();
    } finally {
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    } 
    return directory.getAbsolutePath();
}

private Bitmap loadImageFromStorage(String path)
{
	Bitmap b=null;
    ContextWrapper cw = new ContextWrapper(getApplicationContext());
    // path to /data/data/yourapp/app_data/imageDir
   File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
   // Create imageDir
   File mypath=new File(directory,"profile.jpg");
    try {
//        File f=new File(path, "profile.jpg");
        b = BitmapFactory.decodeStream(new FileInputStream(mypath));
    } 
    catch (FileNotFoundException e) 
    {
        e.printStackTrace();
    }
return b;
}



@Override
public void onTaskDone(Integer y) {
int yhgf=0;
Toast.makeText(this, "@@@!!!!!!!!!!!!!!!!!! "+String.valueOf(y), Toast.LENGTH_SHORT).show();
get_directory();
}


private void getEMAILAddress(){
	// get prompts.xml view
	LayoutInflater li = LayoutInflater.from(context1);
	View promptsView = li.inflate(R.layout.prompts, null);

	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			context1);

	// set prompts.xml to alertdialog builder
	alertDialogBuilder.setView(promptsView);

	final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
	final EditText userInput1 = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput1);
	 final CheckBox chkbox = (CheckBox) promptsView.findViewById(R.id.chkbox);
	 final CheckBox chkbox1 = (CheckBox) promptsView.findViewById(R.id.chkbox1);

//	 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
//	 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));
	 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
	 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));
	 chkbox.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				add_prefs_keyboolean(EMAILCHECK, true);
				add_prefs_keyboolean(EMAILCHECK1, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));
				Toast.makeText(context1,"checked", Toast.LENGTH_LONG).show();
			}
			else {
				add_prefs_keyboolean(EMAILCHECK, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));	
				Toast.makeText(context1,"not checked", Toast.LENGTH_LONG).show();

			}
		  }
		});
	
	 chkbox1.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				add_prefs_keyboolean(EMAILCHECK1, true);
				add_prefs_keyboolean(EMAILCHECK, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));				
				
				Toast.makeText(context1,"checked 1", Toast.LENGTH_LONG).show();
			}
			else {
				add_prefs_keyboolean(EMAILCHECK1, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));	
				Toast.makeText(context1,"not checked 1", Toast.LENGTH_LONG).show();

			}
		  }
		});
	 
	userInput.setText(sharedpreferences.getString(EMAILADDRESS, ""));
	userInput1.setText(sharedpreferences.getString(EMAILADDRESS1, ""));
	// set dialog message
	alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							// get user input and set it to result
							// edit text
Toast.makeText(context1, "EMAIL   "+"   "+userInput.getText().toString()+"  saved...", Toast.LENGTH_SHORT).show();
add_prefs_key(EMAILADDRESS, userInput.getText().toString());
add_prefs_key(EMAILADDRESS1, userInput1.getText().toString());
//							result.setText(userInput.getText());
						}
					})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							dialog.cancel();
						}
					});

	// create alert dialog
	AlertDialog alertDialog = alertDialogBuilder.create();

	// show it
	alertDialog.show();

}


@Override
public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) 
    {
    	Toast.makeText(context1, "presed long back   ", Toast.LENGTH_SHORT).show();
		loadbmp.myCancel();
//		parse_current_dir();
		add_prefs_key(directory, currentDir1);
//		get_directory();
//		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
		loadbmp.myCancel();
	        Intent intentb = new Intent();
        intentb.putExtra("dirFromBrowserName", "");
        intentb.putExtra("fileFromBrowserName", "");
	        intentb.putExtra("resultFromBrowserName", "0");
        setResult(RESULT_OK, intentb);
		
		BrowseFileMainActivity.this.finish();

        return true;
    }
    return super.onKeyLongPress(keyCode, event);
}





}


