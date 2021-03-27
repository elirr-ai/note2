package com.example.notes2;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HandDraw1 extends Activity implements View.OnTouchListener {

	boolean useBitmap=false;
	boolean need2Save=false;
	Bitmap resizedBitmap;
	RelativeLayout rl1;
	CanvasViewHnd customCanvas;
	Button[] listBtn=null;
	Context context=this;
	int width=0,height=0;
	ArrayList<PointHolderHnd> pHolder =new ArrayList<PointHolderHnd>();
	public getListofFileTypes getListFiles;
	int handFileIndex=0;
	float downx,downy,upx,upy;
	String fullFileName;
	int[] colors = {Color.BLACK,Color.YELLOW,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.RED,  //5
			Color.BLUE,Color.WHITE};
	int[] strokes = {7,14,21,28};

	Drawable drd1;
	Drawable[] drd2arr;
	
	SharedPreferences sharedpreferences;
	final static String MyPREFERENCES="MyPREFERENCES";
	final static String WIDTHhnd="WIDTHhnd";
	final static String COLOURhnd="COLOURhnd";
//	final static String BACKGROUNDCOLOURhnd="BACKGROUNDCOLOURhnd";
	
	int penWidth,penColour=0;
	String Dir,FnameOrg,FnameOrgEXT;
	ArrayList<String> HandFileList = new ArrayList<String>();
	
	ActionBar actionBar;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent showpicIntent1 = getIntent(); // gets the previously created intent
		Dir = showpicIntent1.getStringExtra("dirName"); // will return "FirstKeyValue"
		FnameOrg= showpicIntent1.getStringExtra("fileNameORIGINAL"); // only name note - no date
//		
//		FnameOrgEXT= showpicIntent1.getStringExtra("fileNameEXTENDED"); // With date
		useBitmap= showpicIntent1.getBooleanExtra("bitmap", false);
		
		getListFiles=new getListofFileTypes();
	    handFileIndex=0;
	    HandFileList=getListFiles.getListofFiles(Dir, FnameOrg, ".hnd",".HND");
//		PointHolder1.backgroundcolor=Color.YELLOW;
	    
	    
		setContentView(R.layout.notehnd1);
		rl1=(RelativeLayout)findViewById(R.id.rl1);
		customCanvas = (CanvasViewHnd) findViewById(R.id.canvas1);
		customCanvas.setOnTouchListener(this);
		pHolder.clear();
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
				if (sharedpreferences.getString(WIDTHhnd, "").equals("")){
					saveParams(4);
					saveParams(9);
//					saveBackGroundColor(1);
					}
		readPen();
		actionBar = getActionBar();
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
			
			if (HandFileList.size()==0) {
				createNewHandDraw();

			}	
			
			showActionbarStatus();

			
			new LoadBitmapBackground(true).execute();  ///////////////////////////////////////////////////
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
		getMenuInflater().inflate(R.menu.note_on_handnote, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.undo) {
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
 			need2Save=true;
			return true;
		}
		
		if (id == R.id.save) {
			saveHndToBitmapFile();
			showActionbarStatus();
			need2Save=false;
//LBF.saveBitmap(path1, file,customCanvas.refreshGet());
			return true;
		}
		

		if (id == R.id.savebitmap) {
			saveHndToBitmapFile();
			showActionbarStatus();
			return true;
		}
		
		
		if (id == R.id.load) {
			new LoadBitmapBackground(true).execute();
			showActionbarStatus();
			return true;
		}
		
		if (id == R.id.clearn) {  
//			new LoadBitmapBackground(false).execute();
			clearHanddraw();
			showActionbarStatus();
			need2Save=true;
			return true;
			
		}	

		if (id == R.id.delete) {
			deleteHndHnp();
			showActionbarStatus();
			need2Save=false;
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
	
		if (id == R.id.about) {
			getAppInfo();
			return true;
		}
		
		if (id == R.id.newnote) {
createNewHandDraw();
showActionbarStatus();
need2Save=true;
			return true;
		}	
		
	if (id == R.id.next){
//		new saveBitmapBackground().execute();
	    //  handFileIndex=getListFiles.getNumberofFiles(dname1, fname1, ".hnd");
		int yy=0;
	      HandFileList=getListFiles.getListofFiles(Dir, FnameOrg, ".hnd",".HND");

		handFileIndex++;  
		
		if (  (handFileIndex>HandFileList.size()-1) && HandFileList.size()>0)
		{
			handFileIndex=0;
		}
		int cl=loadBackgroundColor();
		Toast.makeText(getBaseContext(),"color= "+cl, Toast.LENGTH_SHORT).show();
		new LoadBitmapBackground(true).execute();
		showActionbarStatus();
////////////////////		new saveBitmapBackground().execute();
		return true;
	}
		
	if (id == R.id.previous){
//		new saveBitmapBackground().execute();
	    //  handFileIndex=getListFiles.getNumberofFiles(dname1, fname1, ".hnd");
	      HandFileList=getListFiles.getListofFiles(Dir, FnameOrg, ".hnd",".HND");

		handFileIndex--;  
		if (  (handFileIndex<0 && HandFileList.size()>0))
		{
			handFileIndex=HandFileList.size()-1;
		}
		int cl=loadBackgroundColor();
		Toast.makeText(getBaseContext(),"color= "+cl, Toast.LENGTH_SHORT).show();
		new LoadBitmapBackground(true).execute();
		showActionbarStatus();

		return true;}
	
		return super.onOptionsItemSelected(item);
	}


//////////////////  on touch listener  ////////////////////////////
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
    pHolder.add(new PointHolderHnd(event.getX(),event.getY(),
    		(float)strokes[penWidth],
    		colors[penColour],
    		colors[0]));
        break;
    case MotionEvent.ACTION_MOVE:
        pHolder.add(new PointHolderHnd(event.getX(),event.getY(),
          		(float)strokes[penWidth],
        		colors[penColour],
        		colors[0]));
        customCanvas.drawLine1(pHolder);
        break;
    case MotionEvent.ACTION_UP:
        pHolder.add(new PointHolderHnd(event.getX(),event.getY(),(float)strokes[penWidth],colors[penColour],
        		colors[0]));
        pHolder.add(new PointHolderHnd(-1,-1,
          		(float)strokes[penWidth],
        		colors[penColour],
        		colors[0]));
        customCanvas.drawLine1(pHolder);  
        need2Save=true;
        break;
       
		 } 
		return true;
	}
	
/*	
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
*/
	

	@Override
	public void onBackPressed() {
if (need2Save)		{
	saveHndToBitmapFile();
	need2Save=false;
	Toast.makeText(getBaseContext(),"Saved...", Toast.LENGTH_SHORT).show();
}
else{
	Toast.makeText(getBaseContext(),"Nothing to save...", Toast.LENGTH_SHORT).show();

}
		finish();
		super.onBackPressed();
	}

private void saveParams(int buttn){
    SharedPreferences.Editor editor = sharedpreferences.edit();
   if (buttn<8){
    editor.putString(COLOURhnd, String.valueOf(buttn)).commit();
    }
   else {
    editor.putString(WIDTHhnd, String.valueOf(buttn)).commit();
   }	
}

private void readPen() {
	penWidth=-8+Integer.valueOf(sharedpreferences.getString(WIDTHhnd, ""))  ;
	penColour=Integer.valueOf(sharedpreferences.getString(COLOURhnd, ""))  ;
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
	int c=loadBackgroundColor();
	for (int i=0;i<listBtn.length;i++){
	if (i<8 && i==c)
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );	
	else
		listBtn[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, null );	
	}
//    int c=loadBackgroundColor();
	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(resizedBitmap);
    Paint paint=new Paint();
    paint.setColor(colors[c]);
    
    canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
	customCanvas.setBitmap(resizedBitmap,width, height);
	customCanvas.drawLine1(pHolder);
	customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);
//	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//    Canvas canvas = new Canvas(resizedBitmap);
//    Paint paint = new Paint();
//    paint.setColor(colors[sharedpreferences.getInt(BACKGROUNDCOLOURhnd, 0)]);
//    canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
// 	customCanvas.setBitmap(resizedBitmap,width, height);
//	customCanvas.drawLine1(pHolder);
//	customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);

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
//   	    handFileIndex=0;
//    	    HandFileList=getListFiles.getListofFiles(Dir, FnameOrgBMP, ".hnd");
    		SerializeListArrayHnd sr =new SerializeListArrayHnd();
    		pHolder=(ArrayList<PointHolderHnd>) sr.readSerializedObject(Dir,
    				HandFileList.get(handFileIndex)		);
    		
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
//    	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(resizedBitmap);
//        Paint paint = new Paint();
//        paint.setColor(colors[pHolder.get(0).getBackGroundColor()]);
//        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
     	
    }	
		return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    	resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resizedBitmap);
        Paint paint = new Paint();
//        paint.setColor(colors[pHolder.get(0).getBackGroundColor()]);
        int c=loadBackgroundColor();
        paint.setColor(colors[c]);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
		customCanvas.setBitmap(resizedBitmap,width, height);
		customCanvas.drawLine1(pHolder);
		customCanvas.showBrush(colors[penColour],strokes[penWidth],colors);
		pDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
		pDialog = new ProgressDialog(HandDraw1.this);
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
	
	public saveBitmapBackground (){	}
	
	@Override
    protected void onPreExecute() {
			pDialog = new ProgressDialog(HandDraw1.this);
	        pDialog.setMessage("saving ...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();

	}

	@Override
    protected Void doInBackground(Void... params) {
		
//		SerializeListArrayHnd sr =new SerializeListArrayHnd();
//		PointHolderHnd ph =pHolder.get(0);
//		ph.setBackGroundColor(readBackGroundColor());
//		pHolder.set(0, ph);
		SerializeListArrayHnd sr =new SerializeListArrayHnd();
		sr.saveSerializedObject(Dir,HandFileList.get(handFileIndex),pHolder);
		return null;
    }

	
    @Override
    protected void onPostExecute(Void result) {
    	customCanvas.refresh();
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
	dialog.setTitle("Set brush width and color");
	RelativeLayout rlcustom=(RelativeLayout) dialog.findViewById(R.id.rlcustom);
	TextView text = (TextView) dialog.findViewById(R.id.text);
	text.setText("Select brush");
	ImageView image = (ImageView) dialog.findViewById(R.id.image);
	image.setImageResource(R.drawable.setofbrushes);

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
updateChekMark();
				}
			});	  		 
    }

	ViewGroup.MarginLayoutParams p;
			p =	(ViewGroup.MarginLayoutParams) rlcustom.getLayoutParams();
		p.width=width;
		p.height=(75*height)/100;
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
	dialog.setTitle("Set page color");
	RelativeLayout rlcustom=(RelativeLayout) dialog.findViewById(R.id.rlcustom);
	TextView text = (TextView) dialog.findViewById(R.id.text);
	text.setText("Select page color");
	ImageView image = (ImageView) dialog.findViewById(R.id.image);
	image.setImageResource(R.drawable.setpagecolor);

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
					
//Toast.makeText(getBaseContext(),"screen diag = "+j , Toast.LENGTH_SHORT).show();
//saveBackGroundColor(j);
//backGroundColorhnd=j;
saveBackgroundColor(j);
//int cl=loadBackgroundColor();
//Toast.makeText(getBaseContext(),"color= "+cl, Toast.LENGTH_SHORT).show();
//PointHolder1.backgroundcolor=j;
new saveBitmapBackground().execute();
//dialog.setTitle("Title  "+sharedpreferences.getString(WIDTHhnd, "")+" "+sharedpreferences.getString(COLOURhnd, ""));
updateChekMarkPage();
				}
			});	  		 
    }

	ViewGroup.MarginLayoutParams p;
			p =	(ViewGroup.MarginLayoutParams) rlcustom.getLayoutParams();
		p.width=width;
		p.height=(75*height)/100;
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

private void createNewHandDraw(){
	dateFrom1970 df1970=new dateFrom1970();
	fullFileName =FnameOrg+"_"+df1970.getmillisString()+".hnd";
	HandFileList.add(fullFileName);
	handFileIndex=HandFileList.size()-1;
	saveBackgroundColor(1);
	
	pHolder.clear();
	PointHolderHnd ph1=new PointHolderHnd (-1,-1,
      		(float)strokes[penWidth],
    		colors[penColour],
    		colors[0]);

    pHolder.add(ph1);
	new saveBitmapBackground().execute();
	new LoadBitmapBackground(true).execute();
}


private void clearHanddraw(){
	pHolder.clear();
	PointHolderHnd ph1=new PointHolderHnd (-1,-1,
      		(float)strokes[penWidth],
    		colors[penColour],
    		colors[0]);
    pHolder.add(ph1);
	saveHndToBitmapFile();
	new LoadBitmapBackground(true).execute();
}

private void saveBackgroundColor(int c){
	File Path = new File(Environment.getExternalStorageDirectory(), Dir);
	if(!Path.exists()) {
	    Path.mkdirs();
	}
	String f= HandFileList.get(handFileIndex);
	String filename=f.substring(0,f.length()-1)+"b";
	BackGroundColorHolder bg =new BackGroundColorHolder(c);
	File file = new File(Path, filename);
 
    // save the object to file
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
        fos = new FileOutputStream(file);
        out = new ObjectOutputStream(fos);
        out.writeObject(bg);

        out.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public int loadBackgroundColor(){ 
	int cl = 0;
BackGroundColorHolder bgg;
	File Path = new File(Environment.getExternalStorageDirectory(), Dir);
	if(!Path.exists()) {
	    Path.mkdirs();
	}
	String f= HandFileList.get(handFileIndex);
	String filename=f.substring(0,f.length()-1)+"b";
	File file = new File(Path, filename);
	
FileInputStream fis = null;
ObjectInputStream in = null;
try {
	
    fis = new FileInputStream(file);
    in = new ObjectInputStream(fis);
    bgg = (BackGroundColorHolder) in.readObject();
    cl=bgg.GetColorIndex();
    in.close();
} catch (Exception ex) {
    ex.printStackTrace();
}
return cl;
}

private void showActionbarStatus(){
	actionBar.setIcon(R.drawable.markonpicture);
actionBar.setTitle("Note: "+FnameOrg);
actionBar.setSubtitle("  ("+String.valueOf(1+handFileIndex)+"/"+String.valueOf(HandFileList.size()+")"));
actionBar.show();
	
 
}

private void deleteHndHnp(){
	
	File Path = new File(Environment.getExternalStorageDirectory(), Dir);
	if(!Path.exists()) {
	    Path.mkdirs();
	}
	File FileHnd = new File(Path, HandFileList.get(handFileIndex));
	File FileHnb = new File(Path, HandFileList.get(handFileIndex).substring(0,HandFileList.get(handFileIndex).length()-1)+"b");
String a[]=HandFileList.get(handFileIndex).split("\\.");
String a1=a[0]+"HNDPNG.PNG";
File FileHNDPNG = new File(Path,a1 );

	if (FileHnd.exists() && FileHnb.exists()){
Toast.makeText(getBaseContext(),"h OK...deleting" , Toast.LENGTH_SHORT).show();
boolean b0=FileHnd.delete();
boolean b9=FileHnb.delete();
boolean b91=FileHNDPNG.delete();


handFileIndex=0;
HandFileList=getListFiles.getListofFiles(Dir, FnameOrg, ".hnd",".HND");
if (HandFileList.size()==0) {
			createNewHandDraw();
		}	
showActionbarStatus();
new LoadBitmapBackground(true).execute();  ///////////////////////////////////////////////////
										}
else
	Toast.makeText(getBaseContext(),"h not OK..." , Toast.LENGTH_SHORT).show();

}


private void saveHndToBitmapFile() {
	
new saveBitmapBackground().execute();
	View v = customCanvas;  ////////////////////////////  how to capture screen shot
	customCanvas.refresh();
		v.setDrawingCacheEnabled(true);
	    Bitmap tBitmap1 = Bitmap.createBitmap(v.getDrawingCache());
	    v.setDrawingCacheEnabled(false);
GetPicturefromFile gp=new GetPicturefromFile();
//dateFrom1970 df1970=new dateFrom1970();
String f11=HandFileList.get(handFileIndex).trim();
//String[] f10 = f11.split("_");
String[] f10a=f11.split("\\.");
String f=f10a[0]+"HNDPNG.PNG";
//String f=FnameOrg+"_"+df1970.getmillisString()+"HNDJPG.JPG";
gp.writeBMPtoFile1(tBitmap1, Dir, f);
new LoadBitmapBackground(true).execute();			    
showActionbarStatus();
need2Save=false;
	}
}
