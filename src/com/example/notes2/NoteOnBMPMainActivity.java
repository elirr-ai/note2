package com.example.notes2;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteOnBMPMainActivity extends Activity implements View.OnTouchListener {

	boolean useBitmap=false;
	Bitmap resizedBitmap;
	RelativeLayout rl1;
	CanvasView customCanvas;
	Button[] listBtn=null;
	Context context=this;
	Activity activity=this;
	int width=0,height=0;
	ArrayList<PointHolder1> pHolder =new ArrayList<PointHolder1>();

	float downx,downy,upx,upy;

	int[] colors = {Color.BLACK,Color.YELLOW,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.RED,
			Color.BLUE,Color.WHITE};
	int[] strokes = {7,14,21,28};

	Drawable drd1;
	Drawable[] drd2arr;
	
	SharedPreferences sharedpreferences;
	final static String MyPREFERENCES="MyPREFERENCES";
	final static String WIDTH="WIDTH";
	final static String COLOUR="COLOUR";
	final static String BACKGROUNDCOLOUR="BACKGROUNDCOLOUR";
	
	int penWidth,penColour;
	String Dir,FnameOrgBMP,FnameOrgEXT;
	Bitmap cacheBitmap =null;
	boolean savedWork=true;  // Initially declare work saved no unsaved content

	int colorPointer=0;
	String showTextOnBitmap="";
	
	boolean flagLinetext=true; // true=line, false=text
	private final String actionBarMessage="Hand note";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent showpicIntent = getIntent(); // gets the previously created intent
		Dir = showpicIntent.getStringExtra("dirName"); // will return "FirstKeyValue"
		FnameOrgBMP= showpicIntent.getStringExtra("fileNameORIGINAL"); // only name note - no date
		FnameOrgEXT= showpicIntent.getStringExtra("fileNameEXTENDED"); // With date
		useBitmap= showpicIntent.getBooleanExtra("bitmap", false);
		setContentView(R.layout.activity_note_on_bmpmain);
		rl1=(RelativeLayout)findViewById(R.id.rl1);
		customCanvas = (CanvasView) findViewById(R.id.canvas1);
		customCanvas.setOnTouchListener(this);
		pHolder.clear();
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				if (sharedpreferences.getString(WIDTH, "").equals("")){
					saveParams(4);
					saveParams(9);
					saveBackGroundColor(1);
					}
		readPen();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (width==0 || height==0){
			width=rl1.getWidth();
			height=rl1.getHeight();
			
			ViewGroup.MarginLayoutParams p = 
					(ViewGroup.MarginLayoutParams) customCanvas.getLayoutParams();
			p.width=rl1.getWidth();
			p.height=rl1.getHeight();
			p.setMargins(0,   //left
							0,    //top
							0,0);
			customCanvas.requestLayout();
			resizedBitmap=null;

			new LoadBitmapBackground(true).execute();  ///////////////////////////////////////////////////
			setActivityBarcustom(flagLinetext, actionBarMessage);

		}

		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		if ( pHolder.size()!=0)
        customCanvas.drawLine1(pHolder); 
		super.onResume();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note_on_bmpmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.undo) {
			savedWork=false;
			boolean f=false;
	if (pHolder.size()>1 &&  (pHolder.get(pHolder.size()-1).getX()==-1 &&
			pHolder.get(pHolder.size()-1).getY()==-1)  ) pHolder.remove(pHolder.size()-1);;

			while (true) {
					if (   pHolder.size()<=1 ) {
						f=true;
						break;
					}				
				if (  (pHolder.get(pHolder.size()-1).getX()==-1 && 
						pHolder.get(pHolder.size()-1).getY()==-1)  ) break;

				pHolder.remove(pHolder.size()-1);
				}
			if (f) {
				pHolder.clear();f=false;
				}
 			customCanvas.drawLine1(pHolder);
			return true;
		}
		
		if (id == R.id.save) {
			savedWork=false;
			new saveBitmapBackground().execute();
//LBF.saveBitmap(path1, file,customCanvas.refreshGet());
			return true;
		}
		
		
		if (id == R.id.load) {
			new LoadBitmapBackground(true).execute();
			return true;
		}
		
		if (id == R.id.clearn) {
			new LoadBitmapBackground(false).execute();
			return true;
		}	

		if (id == R.id.delete) {
  		File file = new File(Environment.getExternalStorageDirectory().toString()+"/"+Dir,
  				FnameOrgEXT.substring(0, FnameOrgEXT.length()-4)+".hnp");
  				// the File to save , append increasing numeric counter to prevent files from getting overwritten.
  	  	if (file.exists()){
  	  		boolean f=file.delete();
  	  		Toast.makeText(getBaseContext(),"hnp file deleted..." , Toast.LENGTH_SHORT).show();
  	  	}		
  	  	else {
  	  		Toast.makeText(getBaseContext(),"No hnp files exists..." , Toast.LENGTH_SHORT).show();
  	  	}	
    		
			return true;
		}	
		
		
		if (id == R.id.setupb) {
			setupDialog();
			return true;
		}		
	
		if (id == R.id.setupp) {
			setupDialogPage();
			return true;
		}
	
		if (id == R.id.textplus) {
			customCanvas.increasetextSize();
			return true;
		}
		if (id == R.id.textminus) {
			customCanvas.decreasetextSize();
			return true;
		}
		
		

		if (id == R.id.text) {
			ViewDialog alert = new ViewDialog();
			alert.showDialog(activity, "Please enter your text...");
			return true;
		}
		
		
		
		
		
		
		if (id == R.id.about) {
			getAppInfo();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
    if (flagLinetext){	
    	savedWork=false;
    	pHolder.add(new PointHolder1 (event.getX(),event.getY(),
    		(float)strokes[penWidth],colors[penColour]));
    		}
    else {
    	
    }
        break;
    case MotionEvent.ACTION_MOVE:
        if (flagLinetext){	  	
        pHolder.add(new PointHolder1 (event.getX(),event.getY(),
          		(float)strokes[penWidth],colors[penColour]));
        customCanvas.drawLine1(pHolder);
        } 
        else {
        	customCanvas.setTextxy(showTextOnBitmap , event.getX(), event.getY(), colorPointer) ;

        }
        break;
    case MotionEvent.ACTION_UP:
        if (flagLinetext){	
        	setActivityBarcustom(flagLinetext, actionBarMessage);
        pHolder.add(new PointHolder1 (event.getX(),event.getY(),(float)strokes[penWidth],colors[penColour]));
        pHolder.add(new PointHolder1 (-1,-1,	(float)strokes[penWidth],colors[penColour]));
        customCanvas.drawLine1(pHolder);   
        }
        else {
        	flagLinetext=true;
        	setActivityBarcustom(flagLinetext, actionBarMessage);
        }
        break;
       
		 } 
		return true;
	}
	
	
	private Bitmap getScaledImage(){
		Bitmap resizedBitmap=null;
		try
		{
		    int inWidth = 0;
		    int inHeight = 0;
String f= new File(Environment.getExternalStorageDirectory()+
  		File.separator + Dir +File.separator+"ttt.jpg" ).getAbsolutePath();
String f1= new File(Environment.getExternalStorageDirectory()+
  		File.separator + Dir +File.separator+"ttt1.jpg" ).getAbsolutePath();

		    InputStream in = new FileInputStream(f);

		    // decode image size (decode metadata only, not the whole image)
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
		    in.close();
		    in = null;

		    // save width and height
		    inWidth = options.outWidth;
		    inHeight = options.outHeight;

		    // decode full image pre-resized
		    in = new FileInputStream(f);
		    options = new BitmapFactory.Options();
		    // calc rought re-size (this is no exact resize)
		    options.inSampleSize = Math.max(inWidth/width, inHeight/height);
		    // decode full image
		    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

		    // calc exact destination size
		    Matrix m = new Matrix();
		    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
		    RectF outRect = new RectF(0, 0, width, height);
		    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
		    float[] values = new float[9];
		    m.getValues(values);

		    // resize bitmap
		    resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

		    // save image
		    try
		    {
		        FileOutputStream out = new FileOutputStream(f1);
		        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
		    }
		    catch (Exception e)
		    {
		        Log.e("Image", e.getMessage(), e);
		    }
		}
		catch (IOException e)
		{
		    Log.e("Image", e.getMessage(), e);
		}
		
		return resizedBitmap;
	}


	@Override
	public void onBackPressed() {
		if (!savedWork){
		Toast.makeText(getBaseContext(),"Pls save work", Toast.LENGTH_SHORT).show();
//////////////////////
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

			// set title
			alertDialogBuilder.setTitle("Your work is not saved !!!");
			alertDialogBuilder
				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						NoteOnBMPMainActivity.this.finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
		
////////////////////
	}

//		new saveBitmapBackground().execute();
		else {
        Intent intent = new Intent();
        intent.putExtra("keyName", "1234");
        setResult(RESULT_OK, intent);
		finish();
		super.onBackPressed();
		}
	}

private void saveParams(int buttn){
    SharedPreferences.Editor editor = sharedpreferences.edit();
   if (buttn<8){
    editor.putString(COLOUR, String.valueOf(buttn)).commit();
    }
   else {
    editor.putString(WIDTH, String.valueOf(buttn)).commit();
   }	
}

private void saveBackGroundColor(int bgc){
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putInt(BACKGROUNDCOLOUR, bgc).commit();
}


private void readPen() {
	penWidth=-8+Integer.valueOf(sharedpreferences.getString(WIDTH, ""))  ;
	penColour=Integer.valueOf(sharedpreferences.getString(COLOUR, ""))  ;
}

private void updateChekMark(){
	readPen();
	makedrd2arr();
	for (int i=0;i<listBtn.length;i++){
	if (i<8 && i==penColour)
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );	
	if (i<8 && i!=penColour)
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null );	
	if (i>=8 && i==penWidth+8 )
			listBtn[i].setCompoundDrawablesWithIntrinsicBounds(drd2arr[i-8],null, drd1, null );
	if (i>=8 && i!=penWidth+8 )
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(drd2arr[i-8],null, null, null );
	}
	customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);
	}

private void updateChekMarkPage(){
	readPen();
	makedrd2arr();
	for (int i=0;i<listBtn.length;i++){
	if (i<8 && i==sharedpreferences.getInt(BACKGROUNDCOLOUR, 0))
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );	
	else
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null );	
	}

	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(resizedBitmap);
    Paint paint = new Paint();
    paint.setColor(colors[sharedpreferences.getInt(BACKGROUNDCOLOUR, 0)]);
    canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
 	customCanvas.setBitmap(resizedBitmap,width, height);
	customCanvas.drawLine1(pHolder);
	customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);

}


private void makeDRD1(){
    Drawable img = context.getResources().getDrawable( R.drawable.check72x72);
    Bitmap b = ((BitmapDrawable)img).getBitmap();
    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
    drd1=new BitmapDrawable(this.getResources(), bitmapResized);
}	
	
private void makedrd2arr(){
	readPen();
	drd2arr=new Drawable[strokes.length] ;
	for (int i=0;i<strokes.length;i++){
		Bitmap bmpQQ = Bitmap.createBitmap(240, 60, Config.ARGB_8888);
	    Canvas c = new Canvas(bmpQQ);
	    Paint pnt = new Paint();
	    pnt.setColor(colors[penColour]);
	    pnt.setStrokeWidth(strokes[i]);
	    pnt.setStyle(Paint.Style.STROKE);
	    c.drawLine(0, 30, 240, 30, pnt);  // Drawn into bmp
		drd2arr[i]=new BitmapDrawable(this.getResources(), bmpQQ);
	}
}


private class LoadBitmapBackground extends AsyncTask<Void, Void, Void> {  //////////////// load///////
	ProgressDialog pDialog;
	boolean f;
	public LoadBitmapBackground(boolean f){ // f says: true use loaded pHolder, false- clear pHolder
		this.f=f;
	}
	
    @Override
    protected Void doInBackground(Void... params) {

    	if (f){   	
    		SerializeListArray2 sr =new SerializeListArray2();
    		pHolder=(ArrayList<PointHolder1>) sr.readSerializedObject(Dir,
    				FnameOrgEXT.substring(0, FnameOrgEXT.length()-4)+".hnp");
    		}
    	else {
    		pHolder.clear();
    		}

    if (null==resizedBitmap && useBitmap){	
    	LoadSaveBitmapFile LBF=new LoadSaveBitmapFile(width,height);
		Bitmap myBitmap=LBF.loadBitMap( Dir,FnameOrgEXT,LBF.getBitmapScale( Dir,FnameOrgEXT));	
		resizedBitmap = LBF.getScaledBitmap(myBitmap, width, height);	
    }
    if (!useBitmap) {
//     	Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//     	resizedBitmap = Bitmap.createBitmap(width, height, conf); // this creates a MUTABLE bitmap
     	
    	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resizedBitmap);
        Paint paint = new Paint();
        paint.setColor(colors[sharedpreferences.getInt(BACKGROUNDCOLOUR, 0)]);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
     	
    }	
		return null;
    }

    @Override
    protected void onPostExecute(Void result) {
		customCanvas.setBitmap(resizedBitmap,width, height);
		customCanvas.drawLine1(pHolder);
		customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);
		pDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
		pDialog = new ProgressDialog(NoteOnBMPMainActivity.this);
        pDialog.setMessage("Loading ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {}
}


private class saveBitmapBackground extends AsyncTask<Void, Void, Void> {  //  save  ///////
	ProgressDialog pDialog;
	Bitmap b6=null;    
	public saveBitmapBackground (){
	}
	
	@Override
    protected void onPreExecute() {
customCanvas.toggleShowPen();
customCanvas.setBitmap(resizedBitmap,width, height);
View v = customCanvas;  ////////////////////////////  how to capture screen shot
		
		customCanvas.refresh();
			v.setDrawingCacheEnabled(true);
	    b6 = Bitmap.createBitmap(v.getDrawingCache());
		    v.setDrawingCacheEnabled(false);   
			customCanvas.setBitmap(resizedBitmap,width, height);
		    customCanvas.toggleShowPen();
			customCanvas.refresh();
			
			pDialog = new ProgressDialog(NoteOnBMPMainActivity.this);
	        pDialog.setMessage("saving ...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();

	}

	@Override
    protected Void doInBackground(Void... params) {
		
		SerializeListArray2 sr =new SerializeListArray2();
		sr.saveSerializedObject(Dir,
				FnameOrgEXT.substring(0, FnameOrgEXT.length()-4)+".hnp",pHolder)	;

		SaveBitmapFile sb= new SaveBitmapFile();
		sb.saveBitmapJPG(b6, Dir,FnameOrgBMP+"_"+System.currentTimeMillis()+".JPG");

		
		return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    	customCanvas.refresh();
//		SaveBitmapFile sb= new SaveBitmapFile();
//		sb.saveBitmapJPG(b6, Dir, "aaaaaaaaa_"+System.currentTimeMillis()+".jpg");
    	savedWork=true;
    	pDialog.dismiss();
    	
    }


    @Override
    protected void onProgressUpdate(Void... values) {}
}


private void setupDialog(){
	makeDRD1();
	makedrd2arr();
	final Dialog dialog = new Dialog(context);
	dialog.setContentView(R.layout.custombrush);
	dialog.setTitle("Brush Select "+sharedpreferences.getString(WIDTH, "")+" "+sharedpreferences.getString(COLOUR, ""));
	RelativeLayout rlcustom=(RelativeLayout) dialog.findViewById(R.id.rlcustom);
	TextView text = (TextView) dialog.findViewById(R.id.text);
	text.setText("Color/Width brush select");
	ImageView image = (ImageView) dialog.findViewById(R.id.image);
	image.setImageResource(R.drawable.ic_launcher);

	final int[] idList = {R.id.l1, R.id.l2, R.id.l3,R.id.l4, R.id.l5, R.id.l6,R.id.l7,R.id.l8,
			R.id.l9,R.id.l10,R.id.l11,R.id.l12};
	listBtn = new Button[idList.length];
    for(int i = 0 ; i < listBtn.length ; i++){
  	  	listBtn[i] =  (Button) dialog.findViewById(idList[i]); 
  	  		if (i<8) listBtn[i].setBackgroundColor(colors[i]);
  	  		else {
  	  			listBtn[i].setBackgroundColor(Color.CYAN);
  	  			}
			listBtn[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int j=0;
					for (j=0;j<idList[j];j++){
						if (v.getId()==idList[j]){
							break;
						}
					}
					
Toast.makeText(getBaseContext(),"screen diag = "+j , Toast.LENGTH_SHORT).show();
saveParams(j);
dialog.setTitle("Title  "+sharedpreferences.getString(WIDTH, "")+" "+sharedpreferences.getString(COLOUR, ""));
updateChekMark();
setActivityBarcustom(flagLinetext, actionBarMessage);

				}
			});	  		 
    }

	ViewGroup.MarginLayoutParams p;
			p =	(ViewGroup.MarginLayoutParams) rlcustom.getLayoutParams();
		p.width=width/1;
		p.height=height/2;
		p.setMargins(0,0,0,0);
		rlcustom.requestLayout();
	
	for (int i=0;i<listBtn.length;i++){

	p =(ViewGroup.MarginLayoutParams) listBtn[i].getLayoutParams();
	p.width=width/4;
	p.height=height/20;
	if (i<8) p.setMargins(5,	10,0,10);
	else p.setMargins(width/5,	10,0,10);
	listBtn[i].requestLayout();
	}
updateChekMark();  
//	readPen();
	dialog.show();
}


private void setupDialogPage(){
	makeDRD1();
//	makedrd2arr();
	final Dialog dialog = new Dialog(context);
	dialog.setContentView(R.layout.custompage);
	dialog.setTitle("Select page color");
	RelativeLayout rlcustom=(RelativeLayout) dialog.findViewById(R.id.rlcustom);
	TextView text = (TextView) dialog.findViewById(R.id.text);
	text.setText("Please select color");
	ImageView image = (ImageView) dialog.findViewById(R.id.image);
	image.setImageResource(R.drawable.ic_launcher);

	final int[] idList = {R.id.l1, R.id.l2, R.id.l3,R.id.l4, R.id.l5, R.id.l6,R.id.l7,R.id.l8};
	listBtn = new Button[idList.length];
    for(int i = 0 ; i < listBtn.length ; i++){
  	  	listBtn[i] =  (Button) dialog.findViewById(idList[i]); 
  	  	listBtn[i].setBackgroundColor(colors[i]);
		listBtn[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int j=0;
					for (j=0;j<idList[j];j++){
						if (v.getId()==idList[j]){
							break;
						}
					}
					
Toast.makeText(getBaseContext(),"screen diag = "+j , Toast.LENGTH_SHORT).show();
saveBackGroundColor(j);
dialog.setTitle("Title  "+sharedpreferences.getString(WIDTH, "")+" "+sharedpreferences.getString(COLOUR, ""));
updateChekMarkPage();
				}
			});	  		 
    }

	ViewGroup.MarginLayoutParams p;
			p =	(ViewGroup.MarginLayoutParams) rlcustom.getLayoutParams();
		p.width=width/1;
		p.height=height/2;
		p.setMargins(0,0,0,0);
		rlcustom.requestLayout();
	
	for (int i=0;i<listBtn.length;i++){

	p =(ViewGroup.MarginLayoutParams) listBtn[i].getLayoutParams();
	p.width=width/4;
	p.height=height/20;
	if (i<8) p.setMargins(5,	10,0,10);
	else p.setMargins(width/5,	10,0,10);
	listBtn[i].requestLayout();
	}
	updateChekMarkPage();  
	dialog.show();
}

private void getAppInfo(){

	 ArrayList<String> al=new ArrayList<String>();
	 String s=""; 
	 getAbout1 ga=new getAbout1(context);	
	 al=ga.getAppInfo();	
	
	for (int i=0;i<al.size();i++){
		s+=al.get(i)+"\n\r";
		}
ga.showAlertDialog1("ABOUT", s);	
	}



///////////////////////////////////////////////////////////////
public class ViewDialog {

        int tmpColor=colorPointer;
        EditText editt;
    public void showDialog(Activity activity, String msg){
    	flagLinetext=false;
    	setActivityBarcustom(flagLinetext, actionBarMessage);
        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        
        editt = (EditText) dialog.findViewById(R.id.edittext200);
        setSpan();
        

        
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	showTextOnBitmap=editt.getText().toString();
            	colorPointer=tmpColor;
            	customCanvas.setTextxy(showTextOnBitmap , width/2, height/2, colorPointer) ;
    			dialog.dismiss();
            }
        });
        
        Button dialogButtonno = (Button) dialog.findViewById(R.id.btn_dialogno);
        dialogButtonno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	flagLinetext=true;
            	setActivityBarcustom(flagLinetext, actionBarMessage);
                dialog.dismiss();
            }
        });
        
        final Button btn_color = (Button) dialog.findViewById(R.id.btn_color);
		btn_color.setBackgroundColor(colors[colorPointer]);
		
        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//    			Toast.makeText(context, "kuku... ",	Toast.LENGTH_LONG).show();
            	tmpColor+=1;
        		if (tmpColor==colors.length) {
        			tmpColor=0;	
        		}	
        		btn_color.setBackgroundColor(colors[tmpColor]);
        		setSpan();
            }
        });	  
        
        
//		new LoadBitmapBackground(true).execute();
//		new LoadBitmapBackground(true).execute();  ///////////////////////////////////////////////////

//    	customCanvas.setTextxy("1111", 150, 170, 0);
//        customCanvas.refresh();
        dialog.show();

    }
    
    private void setSpan(){
    	if (editt.getText().toString().length()>0){
    	Spannable spannable = new SpannableString(editt.getText().toString());	    	
    	spannable.setSpan(new ForegroundColorSpan(colors[tmpColor]), 
    			0, editt.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	editt.setText( spannable );
    	}
    }
    
}

private void setActivityBarcustom(boolean f, String s){  //f true DRAW, false TEXT
	Bitmap b,b1;
	   final ActionBar actionBar = getActionBar();
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setDisplayShowCustomEnabled(true);
	    actionBar.setCustomView(R.layout.customactionbar1);

	    final TextView actionBarTitle = (TextView) findViewById(R.id.tv110);
	    final ImageView ivv = (ImageView) findViewById(R.id.image);
	    final ImageView iv = (ImageView) findViewById(R.id.image110);

	    
	    
		ViewGroup.MarginLayoutParams p;
		p = 	(ViewGroup.MarginLayoutParams) actionBarTitle.getLayoutParams();
		p.width=width/8;
		p.height=height/20;
		p.setMargins(width/80,0,	width/80,0);
		actionBarTitle.requestLayout();
		
//	   Bitmap b3 = Bitmap.createScaledBitmap(imageBrush,   width/50,width/40, false); 
//		actionBarTitle.setBackground(new BitmapDrawable(getResources(), b3));		
		
		p = 	(ViewGroup.MarginLayoutParams) ivv.getLayoutParams();
		p.width=width/20;
		p.height=height/20;
		p.setMargins(0,0,	0,0);
		ivv.requestLayout();
		
		p = 	(ViewGroup.MarginLayoutParams) iv.getLayoutParams();
		p.width=width/20;
		p.height=height/20;
		p.setMargins(0,0,	0,0);
		iv.requestLayout();
		
		
		
	   
	    actionBarTitle.setText(s);
	    actionBarTitle.setBackgroundColor(Color.GRAY);
	    actionBarTitle.setTextColor(Color.BLUE);
	    b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    b.eraseColor(colors[penColour]);
	    //penWidth
	    if (penWidth==0 ) b1 = Bitmap.createScaledBitmap(b,   width/12,height/120, false); 
	    else if (penWidth==1 ) b1 = Bitmap.createScaledBitmap(b,   width/12,height/87, false); 
	    else if (penWidth==2 ) b1 = Bitmap.createScaledBitmap(b,   width/12,height/74, false); 
	    else b1 = Bitmap.createScaledBitmap(b,   width/12,height/60, false); 

	    
	    
//	     b1 = Bitmap.createScaledBitmap(b,   width/12,width/40, false); 
	    BitmapDrawable drawableLeft = new BitmapDrawable(getResources(), b1);
	    actionBarTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null,drawableLeft);


    	b = BitmapFactory.decodeResource(getResources(), R.drawable.handnote64x64);	
	    b1 = Bitmap.createScaledBitmap(b,   width/20,width/20, false);
	    ivv.setImageBitmap(b1);
	    ivv.setBackgroundColor(Color.YELLOW);
	    
	    

	    
	    

		
		
		
		
	    
	    
	    
	    if (f){
	    	b = BitmapFactory.decodeResource(getResources(), R.drawable.handdraw);	
	    }
	    else{
	    	b = BitmapFactory.decodeResource(getResources(), R.drawable.letters);	

	    }

	    b1 = Bitmap.createScaledBitmap(b,width/20,width/20, false);
	    iv.setImageBitmap(b1);
	    iv.setBackgroundColor(Color.YELLOW);
	    
}

}
