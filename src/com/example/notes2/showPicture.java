package com.example.notes2;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class showPicture extends Activity  implements OnTouchListener  {

	int angle=0;
//	int rotate_degs=90;
	private static final String TAG = "Touch";

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private boolean touchNextBeforeImage=false;
	
	
	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	private long fingerDown=0;
	
	private boolean targetFlag=false;
	
	ImageView ib;
	RelativeLayout rl1;
	String dname1;
    String fname1,Memo,headerMail1,textMail1,mailaddr1;
    String[] a23={"","","",""};	
    
	int picture_sequence=0;
	int data_block=200;
	ArrayList<String> pictures_array = new ArrayList<String>();
	private ActionBar actionBar;
	
	FileReadString fileread;
    FileWriteString filewrite;
    GetPicturefromFile g1;
    GetScreenHieghtWidth gscr;
    Bitmap bm08082019;
    float scale;
    
    static int mWidthScreen=0;
	static int mHeightScreen=0;
	
	double dbl;
	private Menu mMenu;
	public getListofFileTypes getListFiles;
    ArrayList<String> HandFileList = new ArrayList<String>();
	Context context=this;
 
	SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;  // my pref internal folder name
    public static final String ROTATEPICTUREANGEL = "ROTATEPICTUREANGEL";
	public static final String FULLFILENAMEANDPATH = "FULLFILENAMEANDPATH";
	
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    
//	private GetBMPTask taskLoadBitmap;
	
	 int bitmapHeight=0;
	 int bitmapWidth=0;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera10);
		rl1=(RelativeLayout)findViewById(R.id.rl1);
	    ib=(ImageView)findViewById(R.id.imageButton1);
	    ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
	    
		sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//		rotate_degs=0;
//init1();	      

	      
	      
	      //matrix.postScale(0.75f, (float) (0.40f+(( (mHeightScreen*30)/35    )/mWidthScreen) )   );
		    
		    //matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
	      //  matrix.postScale(1f, (float) (0.89f+(( (mHeightScreen*31)/35    )/mWidthScreen) )   );
	        //matrix.postScale((float) (31/35) , (float) (1f+(( (mHeightScreen*31)/35    )/mWidthScreen) )   );
	 	   
	      
	      
	      Bundle extras = getIntent().getExtras();
	      dname1 = extras.getString("Value1");
	      fname1 = extras.getString("Value4");
	      headerMail1 = extras.getString("headerMail");   // mail header
	      textMail1= extras.getString("textmail");  // mail text body
	      mailaddr1= extras.getString("mailaddr");  // mail text body

//	      Toast.makeText(getApplicationContext(), "rotate= "+angle+" . ", Toast.LENGTH_LONG).show(); 
	      
	      	actionBar = getActionBar();
			actionBar.setIcon(R.drawable.notes1);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle("Camera ");
	      
			fileread = new FileReadString();
			filewrite = new FileWriteString();
			g1=new GetPicturefromFile();
		
      init_pics_array();
      angle=get_pic_angle();
      ib.setOnTouchListener(this);  
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
if (mWidthScreen==0 || mHeightScreen==0){
	mHeightScreen=rl1.getHeight();
	mWidthScreen=rl1.getWidth();
//Toast.makeText(getApplicationContext(), "WH= "+mWidthScreen+" . "+mHeightScreen, Toast.LENGTH_LONG).show(); 
	init1();

}
if (HandFileList.size()>0) {
	
	if (ib.getDrawable() == null){
		GetBMPTask	taskLoadBitmap = new GetBMPTask();
		taskLoadBitmap.execute();	
//		ib.setImageBitmap(getPicture1());
	   }
	
	
	
	}
		super.onWindowFocusChanged(hasFocus);
	}

	private void init1() {

		ViewGroup.MarginLayoutParams ipetA =(ViewGroup.MarginLayoutParams) ib.getLayoutParams();
		ipetA.width=rl1.getWidth();
		ipetA.height=rl1.getHeight();
		ib.requestLayout();	
		dbl=get_screen_params();
	      
	    //Toast.makeText(getApplicationContext(), "DBL= "+Double.toString(dbl), Toast.LENGTH_LONG).show(); 
			if (dbl>5 && dbl <6){
//				matrix.postScale(1.05f, (float) (1.75f+(( (mHeightScreen*30)/35    )/mWidthScreen) )   );	
			}
			else if (dbl>7 && dbl <7.8){
//				matrix.postScale(0.7f, (float) (0.40f+(( (mHeightScreen*30)/35    )/mWidthScreen) )   );	
			}
			else if (dbl>7.81 && dbl <9.9){
				//matrix.postScale(0.8f, (float) (1.21f+(( (mHeightScreen*30)/35    )/mWidthScreen) )   );	
//				matrix.postScale(0.8f, (float) (0.87f) );	
			}
			else {
//				matrix.postScale(0.75f, (float) (0.40f+(( (mHeightScreen*30)/35    )/mWidthScreen) )   );	
			}


	}

	private void init_pics_array() {
		HandFileList.clear();
	    HandFileList=getListofFileTypes.getListofFiles(dname1, fname1, ".JPG",".jpg");	     
		picture_sequence=0;
		showActionBar();
	}

	private int get_pic_angle(){
		int angle=0;
		String s82="0";
		String s80=HandFileList.get(picture_sequence);
		String s81=s80.substring(0, s80.length()-3)+"PARAM";
		s82=FileReadString.getFileString(dname1, s81);	
		if (s82 !=null && s82.length()>0 && !s82.equals("-1")) angle=Integer.valueOf(s82);
		return angle;
		
	}
	
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
	
	private void deleteFile__PIC() {
	AlertDialog.Builder adb=new AlertDialog.Builder(showPicture.this);
    adb.setTitle("Delete/Cancel?");
    adb.setMessage("Are you sure you want to Delete the file? " );
    adb.setNegativeButton("Cancel", null);
    adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
 
        	File Path = new File(Environment.getExternalStorageDirectory(), dname1);
			if(!Path.exists()) {
			    Path.mkdirs();
			}
			File file9 = new File(Path, HandFileList.get(picture_sequence));
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				finish();
			    return;
			}
          	boolean deleted = (file9.delete());
          	
        	String gww=HandFileList.get(picture_sequence);
        	String gww1 = null;
        	if (gww.contains("JPG")) gww1=gww.replace("JPG", "CAM");
        	if (gww.contains("PNG")) gww1=gww.replace("PNG", "CAM");
			File file29 = new File(Path, gww1);

        	boolean deleted9 = (file29.delete());
          	Toast.makeText(getApplicationContext(), "Deleted picture: "+Integer.toString(picture_sequence), Toast.LENGTH_SHORT).show(); 
           	if (deleted) {
           		init_pics_array();           		
           		if (HandFileList.size()==0) { // all pics deleted 
   	            	Toast.makeText(getApplicationContext(), "All picture deleted ",
             			Toast.LENGTH_SHORT).show(); 	
           					}
           		else {
        			GetBMPTask	taskLoadBitmap = new GetBMPTask();
        			taskLoadBitmap.execute();	
           		} 
				showActionBar();
                     	}	
          }});
      adb.show();	
	}
		
	
	public boolean fileExistsInSD(String sFileName){
	    String sFolder = Environment.getExternalStorageDirectory().toString() + 
	             "/"+dname1;
	    String sFile=sFolder+"/"+sFileName;
	    java.io.File file = new java.io.File(sFile);
	    return file.exists();
	}
	
	public void showActionBar(){
		int sq=0;
		if (HandFileList.size()!=0){
			sq=picture_sequence+1;
			}		
		actionBar.setTitle("Camera "+String.valueOf(sq)+"/"+String.valueOf(HandFileList.size())+" ("+
				String.valueOf(angle)+" )" ) ;				
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showpic2, menu);
		mMenu = menu;
		setActionIconTarget(!targetFlag);
		return true;
	}

	private void setActionIconTarget(boolean showWithBadge)
	{
	    MenuItem item = mMenu.findItem(R.id.target);

	    if(mMenu != null)
	    {
	        if(showWithBadge)
	        {
	            item.setIcon(R.drawable.fullscreen);           
	        }
	        else 
	        {
	            item.setIcon(R.drawable.notfullscreen);
	        }
	    }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {

/*		case R.id.addtext:

			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.notebmptextcustomizeddialog);
			dialog.setTitle("Add text to Image");

			// set the custom dialog components - text, image and button
			 final ImageView iv1 = (ImageView) dialog.findViewById(R.id.iv1);			
			TextView te1 = (TextView) dialog.findViewById(R.id.te1);
			final TextView tvsize = (TextView) dialog.findViewById(R.id.tvsize);
			final EditText ed1 = (EditText) dialog.findViewById(R.id.ed1);
	
			final Button fontsize = (Button) dialog.findViewById(R.id.fontsize);
			final Button fontcolor = (Button) dialog.findViewById(R.id.fontcolor);
			final Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);

			ViewGroup.MarginLayoutParams ipetA;
			
			ipetA =(ViewGroup.MarginLayoutParams) fontsize.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			fontsize.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) fontcolor.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			fontcolor.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) tvsize.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			tvsize.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) iv1.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			iv1.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) dialogButtonOK.getLayoutParams();
			ipetA.width=rl1.getWidth()/4;
			ipetA.height=rl1.getWidth()/8;
			dialogButtonOK.requestLayout();	
			
			final Button settopl = (Button) dialog.findViewById(R.id.settopl);
			final Button setmiddlel = (Button) dialog.findViewById(R.id.setmiddlel);
			final Button setbottoml = (Button) dialog.findViewById(R.id.setbottoml);
			final Button settopr = (Button) dialog.findViewById(R.id.settopr);
			final Button setmiddler = (Button) dialog.findViewById(R.id.setmiddler);
			final Button setbottomr = (Button) dialog.findViewById(R.id.setbottomr);
			
			ipetA =(ViewGroup.MarginLayoutParams) settopl.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			settopl.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) settopr.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			settopr.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) setmiddlel.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			setmiddlel.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) setmiddler.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			setmiddler.requestLayout();	
	
			ipetA =(ViewGroup.MarginLayoutParams) setbottoml.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			setbottoml.requestLayout();	
			
			ipetA =(ViewGroup.MarginLayoutParams) setbottomr.getLayoutParams();
			ipetA.width=rl1.getWidth()/8;
			ipetA.height=rl1.getWidth()/8;
			setbottomr.requestLayout();	
			
			
			fontcolor.setBackgroundResource(R.drawable.fontcolor1);
			fontsize.setBackgroundResource(R.drawable.fontsize1);
//			fontsize.setTextColor(Color.RED);
//			fontsize.setTextSize(TypedValue.COMPLEX_UNIT_PX, 110);
//			fontsize.setTypeface(null,Typeface.BOLD);
			fontsize.getBackground().setAlpha( 255 ); 
			
			fontcolor.setBackgroundResource(R.drawable.fontcolor1);
			
			settopl.setBackgroundResource(R.drawable.arrow3);
			settopr.setBackgroundResource(R.drawable.arrow4);
			setmiddler.setBackgroundResource(R.drawable.arrow6);
			setmiddlel.setBackgroundResource(R.drawable.arrow5);
			setbottomr.setBackgroundResource(R.drawable.arrow1);
			setbottoml.setBackgroundResource(R.drawable.arrow2);

			ed1.setHint("Add text to image");
			te1.setBackgroundColor(Color.YELLOW);
			dialogButtonOK.setBackgroundColor(Color.YELLOW);
			tvsize.setBackgroundColor(Color.YELLOW);
			tvsize.setTextColor(Color.RED);
			tvsize.setTextSize(TypedValue.COMPLEX_UNIT_PX, 110);
			tvsize.setTypeface(null,Typeface.BOLD);
			tvsize.setGravity(Gravity.CENTER);
			
			final String[] colors =new String[]{"#0000ff","#00ff00","#ff0000","#ffff00","#ff8000"};
			final int[] fontSizes = new int[]{20,35,60,75,100};
			fontsize.setTag(0);
			fontcolor.setTag(0);
			int j=(Integer)fontcolor.getTag();
			iv1.setBackgroundColor(Color.parseColor  ( colors[j]));
			int k=(Integer)fontsize.getTag();
			tvsize.setText(""+fontSizes[k]);
			
		// init arrow
			settopl.setTag(1);
			settopl.setBackgroundResource(R.drawable.arrow3frame);
			settopr.setBackgroundResource(R.drawable.arrow4);
			setmiddler.setBackgroundResource(R.drawable.arrow6);
			setmiddlel.setBackgroundResource(R.drawable.arrow5);
			setbottomr.setBackgroundResource(R.drawable.arrow1);
			setbottoml.setBackgroundResource(R.drawable.arrow2);
			
			
			
			
			te1.setText("**************************");
//			ImageView image = (ImageView) dialog.findViewById(R.id.image);
//			image.setImageResource(R.drawable.ic_launcher);

//			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButtonOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					dialogButtonOK.setBackgroundColor(Color.CYAN);
//				dialogButtonOK.setBackgroundColor(j);
					Point p=new Point();
					
				    switch((Integer) settopl.getTag())
				    {
				    case 1: {  p.x=mWidthScreen/4;  p.y=mHeightScreen/4;     break;}
				    case 2: {  p.x=(mWidthScreen/3)*2; p.y=mHeightScreen/4;    break;}
				    case 3: {  p.x=mWidthScreen/4; p.y=mHeightScreen/2;    break;}
				    case 4: {  p.x=(mWidthScreen/3)*2;  p.y=mHeightScreen/2;   break;}
				    case 5: {  p.x=mWidthScreen/4; p.y=3*(mHeightScreen/4);     break;}
				    case 6: {  p.x=(mWidthScreen/3)*2;  p.y=3*(mHeightScreen/4);    break;}
				    default : {  p.x=0;  p.y=0;     break;}
				    }
										
Bitmap b80=g1.getPicture(angle, dname1,HandFileList.get(picture_sequence),
		mWidthScreen,mHeightScreen);
int j=(Integer)fontcolor.getTag();
				Toast.makeText(getApplicationContext(), "middle  !!!!!! ",Toast.LENGTH_SHORT).show(); 				
	
					Bitmap b81=g1.drawStringonBitmap(b80,
							ed1.getText().toString(),
//							"1234890765",
							p,
							Color.parseColor  ( colors[j]),						
//							Color.RED,
							255,
							fontSizes[(Integer)fontsize.getTag()],						
//							50,
							false,b80.getWidth() ,b80.getHeight());
					ib.setImageBitmap(b81);
					g1.writeBMPtoFile1(b81, dname1,fname1+"_"+String.valueOf(System.currentTimeMillis())+".JPG"    );
					init_pics_array();
					Toast.makeText(getApplicationContext(), "Operation Done!!!!!! ",Toast.LENGTH_SHORT).show(); 				
					dialog.dismiss();
				}
			});

			fontcolor.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					int j=(Integer)fontcolor.getTag();

					j++; if (j>=colors.length)  j=0;
					fontcolor.setTag(j);
					iv1.setBackgroundColor(Color.parseColor  ( colors[j]));
					fontcolor.setText(""+j+" / "+colors.length);
					}
				}
			);
			
			
			
			fontsize.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int j=(Integer)fontsize.getTag();
					j++; if (j>=fontSizes.length)  j=0;
					fontsize.setTag(j);
					tvsize.setText(""+fontSizes[j]);
					}
				}
			);
		
			
			settopl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(1);
					settopl.setBackgroundResource(R.drawable.arrow3frame);
					settopr.setBackgroundResource(R.drawable.arrow4);
					setmiddler.setBackgroundResource(R.drawable.arrow6);
					setmiddlel.setBackgroundResource(R.drawable.arrow5);
					setbottomr.setBackgroundResource(R.drawable.arrow1);
					setbottoml.setBackgroundResource(R.drawable.arrow2);
					
	Toast.makeText(getApplicationContext(), "settopl pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);
			
			settopr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(2);
					settopl.setBackgroundResource(R.drawable.arrow3);
					settopr.setBackgroundResource(R.drawable.arrow4frame);
					setmiddler.setBackgroundResource(R.drawable.arrow6);
					setmiddlel.setBackgroundResource(R.drawable.arrow5);
					setbottomr.setBackgroundResource(R.drawable.arrow1);
					setbottoml.setBackgroundResource(R.drawable.arrow2);

	Toast.makeText(getApplicationContext(), "settopr pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);

			setmiddlel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(3);
					settopl.setBackgroundResource(R.drawable.arrow3);
					settopr.setBackgroundResource(R.drawable.arrow4);
					setmiddler.setBackgroundResource(R.drawable.arrow6);
					setmiddlel.setBackgroundResource(R.drawable.arrow5frame);
					setbottomr.setBackgroundResource(R.drawable.arrow1);
					setbottoml.setBackgroundResource(R.drawable.arrow2);

	Toast.makeText(getApplicationContext(), "setmiddlel pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);
			
			setmiddler.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(4);
					settopl.setBackgroundResource(R.drawable.arrow3);
					settopr.setBackgroundResource(R.drawable.arrow4);
					setmiddler.setBackgroundResource(R.drawable.arrow6frame);
					setmiddlel.setBackgroundResource(R.drawable.arrow5);
					setbottomr.setBackgroundResource(R.drawable.arrow1);
					setbottoml.setBackgroundResource(R.drawable.arrow2);

	Toast.makeText(getApplicationContext(), "setmiddler pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);
			
			setbottomr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(5);
					settopl.setBackgroundResource(R.drawable.arrow3);
					settopr.setBackgroundResource(R.drawable.arrow4);
					setmiddler.setBackgroundResource(R.drawable.arrow6);
					setmiddlel.setBackgroundResource(R.drawable.arrow5);
					setbottomr.setBackgroundResource(R.drawable.arrow1frame);
					setbottoml.setBackgroundResource(R.drawable.arrow2);

	Toast.makeText(getApplicationContext(), "setbottomr pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);
			
			setbottoml.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					settopl.setTag(6);
					settopl.setBackgroundResource(R.drawable.arrow3);
					settopr.setBackgroundResource(R.drawable.arrow4);
					setmiddler.setBackgroundResource(R.drawable.arrow6);
					setmiddlel.setBackgroundResource(R.drawable.arrow5);
					setbottomr.setBackgroundResource(R.drawable.arrow1);
					setbottoml.setBackgroundResource(R.drawable.arrow2frame);

	Toast.makeText(getApplicationContext(), "setbottoml pressed", Toast.LENGTH_SHORT).show(); 
					}
				}
			);			
			
			
			dialog.show();
		  //}
//		});
	
			
			break;

*/
		case R.id.addhndnote:
			Intent myIntent = new Intent(this, NoteOnBMPMainActivity.class);
			myIntent.putExtra("dirName",dname1);
			myIntent.putExtra("fileNameORIGINAL",fname1);
			myIntent.putExtra("fileNameEXTENDED",HandFileList.get(picture_sequence));
			myIntent.putExtra("bitmap", true);
			startActivityForResult(myIntent, 4);
			break;
			
		case R.id.exit1:
			showPicture.this.finish();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.propertypicture:
			showPropertiesPictures();
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.whatsapp:
			if (HandFileList.size()==0){
				Toast.makeText(getApplicationContext(), "No pictures for this memo", Toast.LENGTH_SHORT).show(); 
				}
			else {
				sendWhatsAppMsg(dname1,HandFileList.get(picture_sequence));
			showActionBar();
			}
			//Toast.makeText(getApplicationContext(), "pressed", Toast.LENGTH_SHORT).show(); 
			break;
		
		case R.id.rotateplus:
			angle+=90;
			if (angle>=360) angle=0;
			rotateAngel(angle);
//			Toast.makeText(getApplicationContext(), "plus to rotate "+angle, Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.rotateminus:
			angle-=90;
			if (angle<0) angle=270;
			rotateAngel(angle);
//			Toast.makeText(getApplicationContext(), "minus to rotate "+angle, Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.next:
			if (HandFileList.size()>0){
			picture_sequence++;  
			if (  (picture_sequence>=HandFileList.size()) )
			{
				picture_sequence=0;
			}
			angle=get_pic_angle();
			GetBMPTask	taskLoadBitmap = new GetBMPTask();
			taskLoadBitmap.execute();	
		}
			break;
			
		case R.id.previous:
			if (HandFileList.size()>0){	
			picture_sequence--;  
			if (  (picture_sequence<0 )) {
				picture_sequence=HandFileList.size()-1;
					}
			angle=get_pic_angle();
			GetBMPTask		taskLoadBitmap = new GetBMPTask();
			taskLoadBitmap.execute();	
			}
			break;
			
		case R.id.delete11:
			if (picture_sequence>=0 && picture_sequence<HandFileList.size()) {
//				ib.setImageBitmap(getPicture1());	  			 
				deleteFile__PIC();
				showActionBar();
							}
			else {
				Toast.makeText(getApplicationContext(), "Nothing to delete  ", Toast.LENGTH_SHORT).show(); 
				showActionBar();
			}
			
			break;
		case R.id.target:
			targetFlag=!targetFlag;	
			setTargetSize();
			setActionIconTarget(!targetFlag);
			GetBMPTask	taskLoadBitmap = new GetBMPTask();
			taskLoadBitmap.execute();	

		break;
		
		case R.id.showexif:
	
				ArrayList< ExifHolder>	alExif=GetExifDataFromBitMap.getExifData(context,dname1, HandFileList.get(picture_sequence));
//			Toast.makeText(getApplicationContext(), "EXIF  "+f, Toast.LENGTH_SHORT).show(); 
StringBuilder sb=new StringBuilder();
sb.append("EXIF DATA...\n");
sb.append("-------------------------\n");
for (int i=0;i<alExif.size();i++){
	sb.append(alExif.get(i).getTag()+"  "+alExif.get(i).getName()+"\n");
			}
GetPicturefromFile gl=new GetPicturefromFile();
ArrayList<String>	abl=gl.getBitmapProperties(bm08082019);
sb.append("\n\nBITMAP DATA...\n");
sb.append("-------------------------\n");
sb.append("DIR: "+dname1+"\n");
sb.append("MEMO: "+fname1+"\n");
sb.append("FILE: "+HandFileList.get(picture_sequence)+"\n");

for (int i=0;i<abl.size();i++){
	sb.append(abl.get(i)+"\n");
}

final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
alertDialogBuilder.setMessage(sb.toString());
alertDialogBuilder.setPositiveButton("OK", 
   new DialogInterface.OnClickListener() {
   @Override
   public void onClick(DialogInterface arg0, int arg1) {
   					}
		});
AlertDialog alertDialog = alertDialogBuilder.create();
alertDialog.show();
rotateAngel(angle);
setTargetSize();
			
	break;
			
		case R.id.mail:
			if (HandFileList.size()==0){
				Toast.makeText(getApplicationContext(), "No pictures for this memo", Toast.LENGTH_SHORT).show(); 
				}
			else {
			ShareViaEmail(dname1,HandFileList.get(picture_sequence));
			showActionBar();
			}
			break;	
	
			
		case R.id.croppic:
//				Toast.makeText(getApplicationContext(), "crop pic  ", Toast.LENGTH_SHORT).show(); 
				Intent i = new Intent(getApplicationContext(), DrawCropMainActivity.class);  
		        i.putExtra("dirName",dname1);  
		        i.putExtra("fileNameShort",fname1);  
		        i.putExtra("fileNameFull",HandFileList.get(picture_sequence));  
		        startActivityForResult(i, 4);				
			break;	
		
		}
		return super.onOptionsItemSelected(item);
	}

	private void setTargetSize() {
		if (targetFlag){			
//			ib.setScaleType(ScaleType.FIT_XY);
//			ib.setImageBitmap(bm08082019);
				}
			else {
//			ib.setScaleType(ImageView.ScaleType.FIT_CENTER);
//			ib.setImageBitmap(bm08082019);
				}		
	}

	private void rotateAngel(int angl){
		Matrix m=new Matrix();
		m.reset();
		m.postRotate(angle);
		Bitmap rotatedBitmap = Bitmap.createBitmap(bm08082019, 0, 0,
				bm08082019.getWidth(), bm08082019.getHeight(), m, true);
		setTargetSize();
		ib.setImageBitmap(rotatedBitmap);
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
		showPicture.this.finish();
			}
	
///////////////////////////////	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		   ImageView view = (ImageView) v;
		   // make the image scalable as a matrix
		   view.setScaleType(ImageView.ScaleType.MATRIX);
		   float scale;

		   // Handle touch events here...
		   switch (event.getAction() & MotionEvent.ACTION_MASK) {

		   case MotionEvent.ACTION_DOWN: //first finger down only
//			   fingerDown=event.getDownTime(); 
			   touchNextBeforeImage=false;
			   savedMatrix.set(matrix);
			      start.set(event.getX(), event.getY());
			      Log.d(TAG, "mode=DRAG" );
			      mode = DRAG;
//			      lastEvent = null;
		      break;
		   case MotionEvent.ACTION_UP: //first finger lifted
				if (mode == DRAG) {
					mode = NONE;
					int diffX=Math.abs((int)(start.x-event.getX()));
					int diffY=Math.abs((int)(start.y-event.getY()));	
					if (diffX<10 && diffY<10 && event.getY()<mHeightScreen/2 && !touchNextBeforeImage) {
//Toast.makeText(getApplicationContext(), "SLIDER TOP"+event.getX()+" / "+event.getY()+
//"  "+mWidthScreen+" "+mHeightScreen , Toast.LENGTH_SHORT).show(); 
						showCustomtoast(true);
						
						if (HandFileList.size()>0){
							picture_sequence++;  
							if (  (picture_sequence>=HandFileList.size()) )
							{
								picture_sequence=0;
							}
							angle=get_pic_angle();
							GetBMPTask	taskLoadBitmap = new GetBMPTask();
							taskLoadBitmap.execute();	
						}
						
						
						
						
						
					}
	
					else		if (diffX<10 && diffY<10 && event.getY()>mHeightScreen/2 && !touchNextBeforeImage){
//Toast.makeText(getApplicationContext(), "SLIDER BOTTOM"+event.getX()+" / "+event.getY()+
//"  "+mWidthScreen+" "+mHeightScreen , Toast.LENGTH_SHORT).show(); 
showCustomtoast(false);
						
						if (HandFileList.size()>0){	
							picture_sequence--;  
							if (  (picture_sequence<0 )) {
								picture_sequence=HandFileList.size()-1;
									}
							angle=get_pic_angle();
							GetBMPTask		taskLoadBitmap = new GetBMPTask();
							taskLoadBitmap.execute();	
							}
						
						
						
						
					}
					
					
				}
			      Log.d(TAG, "mode=NONE" );
			   break;
			   
		   case MotionEvent.ACTION_POINTER_UP: //second finger lifted
		      mode = NONE;
		      lastEvent = null;
//		      Toast.makeText(getApplicationContext(), "ACTION_POINTER_UP= ", Toast.LENGTH_LONG).show(); 
		      Log.d(TAG, "mode=NONE" );
		      break;
		   case MotionEvent.ACTION_POINTER_DOWN: //second finger down
		      oldDist = spacing(event); // calculates the distance between two points where user touched.
		      Log.d(TAG, "oldDist=" + oldDist);
		      // minimal distance between both the fingers
		      if (oldDist > 5f) {
		         savedMatrix.set(matrix);
		         midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched. 
		         mode = ZOOM;
		         Log.d(TAG, "mode=ZOOM" );
		         
		      }
		      
              lastEvent = new float[4];
              lastEvent[0] = event.getX(0);
              lastEvent[1] = event.getX(1);
              lastEvent[2] = event.getY(0);
              lastEvent[3] = event.getY(1);
              d = rotation(event);
		      break;

		   case MotionEvent.ACTION_MOVE: 
		      if (mode == DRAG) 
		      { //movement of first finger
		    	  if (Math.abs(event.getX() - start.x)>10 || Math.abs(event.getY() - start.y)>10 ){
		    		  touchNextBeforeImage=true;
		    	  }
		         matrix.set(savedMatrix);
//		         if (view.getLeft() >= -192)    {
		            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);		           
//		         }
		      }
		      else if (mode == ZOOM) { //pinch zooming
		         float newDist = spacing(event);
		         Log.d(TAG, "newDist=" + newDist);
		         if (newDist > 5f) {
		            matrix.set(savedMatrix);
		            scale = newDist/oldDist; //thinking I need to play around with this value to limit it**
		            matrix.postScale(scale, scale, mid.x, mid.y);
		            showActionBar();
		            //matrix.postScale(scale, scale, 0f, 0f);
		         }
                 if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                     newRot = rotation(event);
                     float r = newRot - d;
                     float[] values = new float[9];
                     matrix.getValues(values);
                     float tx = values[2];
                     float ty = values[5];
                     float sx = values[0];
                     float xc = (view.getWidth() / 2) * sx;
                     float yc = (view.getHeight() / 2) * sx;
 //                    matrix.postRotate(r, tx + xc, ty + yc);
                 }   
		         
		      }
		      break;
		   }

		   // Perform the transformation
		   view.setImageMatrix(matrix);

		   return true; // indicate event was handled
		}
	
	
	   /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);              
    }
    
    
    
    
	private float spacing(MotionEvent event) {
		   float x = event.getX(0) - event.getX(1);
		   float y = event.getY(0) - event.getY(1);
		   return (float) Math.sqrt(x * x + y * y);
		}

		private void midPoint(PointF point, MotionEvent event) {
		   float x = event.getX(0) + event.getX(1);
		   float y = event.getY(0) + event.getY(1);
		   point.set(x / 2, y / 2);
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
			       
			return screenInches;
		}	
	
		

		// send mail with PNG recorded sound
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
/////////////////////////////////////////////////////////////////////////////////
		public void sendWhatsAppMsg(String folder_name, String file_name) {

			PackageManager pm = context.getPackageManager();
		    boolean isInstalled = isPackageInstalled("com.whatsapp", pm);
		if (!isInstalled) {
			       Toast.makeText(getApplicationContext(), "WHATSUP is not installed", Toast.LENGTH_SHORT).show();
		 }
			
		else {
			File root = Environment.getExternalStorageDirectory();
	    	String pathToMyAttachedFile = folder_name+"/"+file_name;
	       	File file = new File(root, pathToMyAttachedFile);
	       	Uri uri = Uri.fromFile(file);
	    	Intent intentw = new Intent(Intent.ACTION_SEND);
	        //intentw.setType("text/plain");
	        intentw.setPackage("com.whatsapp");
	        intentw.setType("image/*");
	        intentw.putExtra(Intent.EXTRA_SUBJECT, headerMail1);
		    intentw.putExtra(Intent.EXTRA_STREAM, uri);
		    intentw.putExtra(Intent.EXTRA_TEXT, textMail1);
	  		 //   intentw.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		    try {
		        startActivity(intentw);
		    } catch (android.content.ActivityNotFoundException ex) {
Toast.makeText(getApplicationContext(), "whatapp failed", Toast.LENGTH_SHORT).show(); 

		    }
		    
		  	}
		 
		}
		
		//////////////////////////////////////////////////////////////////////////////
		
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
	
		private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
		    try {
		        packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
		        return true;
		    } catch (NameNotFoundException e) {
		        return false;
		    }
		}
	
/////////////
	    // This method is called when the second activity finishes
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
int yy=0;
	        // check that it is the SecondActivity with an OK result
	        if (requestCode == 4) {
	            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
//	                String returnString = data.getStringExtra("keyName");
	                init_pics_array();
	    			GetBMPTask		taskLoadBitmap = new GetBMPTask();
	    			taskLoadBitmap.execute();	
	            }
	        }
	    }		
		

private void showPropertiesPictures() {
	GetPicturefromFile gl=new GetPicturefromFile();
	ArrayList<String>	al=gl.getBitmapProperties(bm08082019);
	showDialog1(al);
}
	

private void showDialog1(ArrayList<String> al){
	AlertDialog.Builder adb=new AlertDialog.Builder(showPicture.this);
    adb.setTitle("Bitmap file properties ");
	StringBuilder sb=new StringBuilder();
	al.add(0, "Directory: "+dname1);
	al.add(0, "Memo name: "+fname1);
	al.add(0, "File name: "+HandFileList.get(picture_sequence));
	
	
	for (int i=0;i<al.size();i++){
		sb.append(al.get(i)+"\n");
	}
    adb.setMessage(sb.toString());
    adb.setNegativeButton("OK", null);
    
    adb.show();	

    rotateAngel(angle);
    setTargetSize();
    
  }

private Bitmap getPicture1(){
//	String gww=HandFileList.get(picture_sequence);
//	String gww1 = null;
//	if (gww.contains("JPG")) gww1=gww.replace("JPG", "CAM");
//	if (gww.contains("PNG")) gww1=gww.replace("PNG", "CAM");
//	rotate_degs=getCameraRotateAngel(gww1);
      	File Path = new File(Environment.getExternalStorageDirectory(), dname1);
			if(!Path.exists()) {
			    Path.mkdirs();
			}
		loadBitmap(new File(Path, HandFileList.get(picture_sequence)));
		return bm08082019;
}		


private void loadBitmap(File f) {
	float ratio = 0;
	if(f.exists()) {
	BitmapFactory.Options options = new BitmapFactory.Options();	
	options.inJustDecodeBounds = true;// do not load picture, just decode params
	BitmapFactory.decodeFile(f.getAbsolutePath(),options); 
	 bitmapHeight=options.outHeight;
	 bitmapWidth=options.outWidth;
	float wRatio=1*(bitmapWidth/mWidthScreen);   
	float hRatio=1*(bitmapHeight/mHeightScreen);
	
	options.inSampleSize =1; 
	
	if (wRatio>1 || hRatio>1){
		ratio=Math.max(wRatio, hRatio);
		
		options.inSampleSize =(int)Math.round(ratio); 
		
	}
	
	
 //   Toast.makeText(showPicture.this, "value is  :"+bitmapHeight+"  /  "+bitmapWidth+ " "+ratio,  Toast.LENGTH_SHORT).show(); 
    	options.inJustDecodeBounds = false;// load real picture
Bitmap bm08082019a=BitmapFactory.decodeFile(f.getAbsolutePath(),options);    
Matrix rotateMatrix = new Matrix();
rotateMatrix.postRotate(get_pic_angle());
bm08082019 = Bitmap.createBitmap(bm08082019a, 0, 0,bm08082019a.getWidth(),
		bm08082019a.getHeight(),rotateMatrix, false);
float bmsx=(float)( (float)bm08082019.getWidth()/(float)mWidthScreen);
float bmsy=(float)( (float)bm08082019.getHeight()/(float)mHeightScreen);

matrix.reset();
matrix.preScale(  (float)(1/bmsx), (float)((float)(1/   (bmsy*31/31)  )));//was 35/31
ib.setImageMatrix(matrix);
ib.setScaleType(ScaleType.MATRIX);
ib.setImageBitmap(bm08082019);
	}			
}

//private int getCameraRotateAngel(String fullFname) {
//	File Path = new File(Environment.getExternalStorageDirectory(), dname1);
//	if(!Path.exists()) {
//	    Path.mkdirs();
//	}
//	File file = new File(Path, fullFname);
//	if (!file.exists()){
//		FileWriteString.setFileString(dname1, fullFname, "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0");
//	}
//	String a1=FileReadString.getFileString(dname1, fullFname);
////	Toast.makeText(getApplicationContext(),	"read   "+a1,Toast.LENGTH_LONG).show(); 
//	String[] separated = a1.split(":");
//	
//	getListofFileTypes gl=new getListofFileTypes();
//	ArrayList<String> arr90= new ArrayList<String>();
//	arr90=gl.getOnlyGraphicsFiles(dname1, fname1);
//	return Integer.valueOf(separated[0]);
//}

/*
private void setCameraRotateAngel(String fullFname, int rotate_degs2) {
	File Path = new File(Environment.getExternalStorageDirectory(), dname1);
	if(!Path.exists()) {
	    Path.mkdirs();
	}
	String gww=fullFname;
	String gww1 = null;
	if (gww.contains("JPG")) gww1=gww.replace("JPG", "CAM");
	if (gww.contains("PNG")) gww1=gww.replace("PNG", "CAM");
	String aa=gww1;
	File file = new File(Path, gww1);
	if (!file.exists()){
		FileWriteString.setFileString(dname1, gww1, "0:0:0:0;0:0:0:0:0:0:0:0:0:0:0:0:0:0:0");
	}
	String a1=FileReadString.getFileString(dname1, gww1);
	Toast.makeText(getApplicationContext(),	"write   "+a1,Toast.LENGTH_LONG).show(); 
	String[] separated = a1.split(":");
	separated[0]=String.valueOf(rotate_degs2);
	String a0="";
	for (int i=0;i<separated.length;i++){
		a0+=separated[i]+":";
	}
	FileWriteString.setFileString(dname1, gww1, a0);

//	getListofFileTypes gl=new getListofFileTypes();
//	ArrayList<String> arr90= new ArrayList<String>();
//	arr90=gl.getOnlyGraphicsFiles(dname1, fname1);
//	String hhh=HandFileList.get(picture_sequence);

}
*/



////////////////
private class GetBMPTask extends AsyncTask<Void,Boolean,Void>{ 
	protected void onPreExecute(){
			   super.onPreExecute();
		     }
		   protected Void doInBackground(Void...params){ 
		      	File Path = new File(Environment.getExternalStorageDirectory(), dname1);
				if(!Path.exists()) {
				    Path.mkdirs();
				}
File f=new File(Path, HandFileList.get(picture_sequence));
float ratio = 0;
BitmapFactory.Options options = new BitmapFactory.Options();	
options.inJustDecodeBounds = true;// do not load picture, just decode params
BitmapFactory.decodeFile(f.getAbsolutePath(),options); 
 bitmapHeight=options.outHeight;
 bitmapWidth=options.outWidth;
float wRatio=1*(bitmapWidth/mWidthScreen);   
float hRatio=1*(bitmapHeight/mHeightScreen);
options.inSampleSize =1; 
if (wRatio>1 || hRatio>1){
	ratio=Math.max(wRatio, hRatio);	
	options.inSampleSize =(int)Math.round(ratio); 	
			}

//   Toast.makeText(showPicture.this, "value is  :"+bitmapHeight+"  /  "+bitmapWidth+ " "+ratio,  Toast.LENGTH_SHORT).show(); 
	options.inJustDecodeBounds = false;// load real picture
Bitmap bm08082019a=BitmapFactory.decodeFile(f.getAbsolutePath(),options);    
Matrix rotateMatrix = new Matrix();
rotateMatrix.postRotate(get_pic_angle());
bm08082019 = Bitmap.createBitmap(bm08082019a, 0, 0,bm08082019a.getWidth(),
		bm08082019a.getHeight(),rotateMatrix, false);
float bmsx=(float)( (float)bm08082019.getWidth()/(float)mWidthScreen);
float bmsy=(float)( (float)bm08082019.getHeight()/(float)mHeightScreen);

matrix.reset();
matrix.preScale(  (float)(1/bmsx), (float)((float)(1/   (bmsy*31/31)  )));//was 35/31		   
			   publishProgress(true);
			   return null;
		     }  
		   @Override
	protected void onProgressUpdate(Boolean... values) {		   
		super.onProgressUpdate(values);
	}
		   @Override	    
	protected void onPostExecute(Void v){
			   ib.setImageMatrix(matrix);
			   ib.setScaleType(ScaleType.MATRIX);
			   ib.setImageBitmap(bm08082019);
			   showActionBar();
//Toast.makeText(getApplicationContext(), "From backgroud: bitmap completed",Toast.LENGTH_SHORT).show();
		     }//end of post....
		}
/////////////////
private void showCustomtoast(boolean b){
    //Creating the LayoutInflater instance  
    LayoutInflater li = getLayoutInflater();  
    //Getting the View object as defined in the customtoast.xml file  
    View layout = li.inflate(R.layout.customtoastaaa,(ViewGroup) findViewById(R.id.custom_toast_layout));  
ImageView iv=(ImageView)layout.findViewById(R.id.custom_toast_image);
Drawable myIcon1=null;
if (b){
	myIcon1 = getResources().getDrawable( R.drawable.next1111 );}
else {
	myIcon1 = getResources().getDrawable( R.drawable.back1 );	
}
Bitmap bitmap = ((BitmapDrawable)myIcon1).getBitmap();

    iv.setImageBitmap(bitmap);
    
    //Creating the Toast object  
    Toast toast = new Toast(getApplicationContext());  
    toast.setDuration(Toast.LENGTH_SHORT); 
    
    if (b){
    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0-mHeightScreen/3);  
    }
    else {
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0+mHeightScreen/3);  
	
    }
    
    toast.setView(layout);//setting the view of custom toast layout  
    toast.show();  
	
	
}




}// end of class

	
