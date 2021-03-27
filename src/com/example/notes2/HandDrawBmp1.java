//package com.example.notes2;

////public class HandDrawBmp1 {

//}
package com.example.notes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class HandDrawBmp1 extends Activity {

	
	public dateFrom1970 df1970;
	DrawingView dv ;
    private Paint mPaint,initpaint,txtPaint;  
    float StrokeWidth=4; 
    int colorID=0,push_colorID, handFileIndex=0;;
    float push_paintStroke;
    String[] colors = {"Black", "Blue", "Cyan", "Green", "Gray", "Magenta", "Red", "Yellow" , "White"};
    boolean isEraseing=false;
    boolean newOrExistingFileToSave=false; // false update current fine / true - save as new file
    String text_status="Paint";
    String Memo="",Memo_txt,headerMail1,textMail1,mailaddr1;
     
    private String dname1,fname1,hand_field;
    private ActionBar actionBar;
    private FileReadString fileread;
    private FileWriteString filewrite;
    private dateFrom1970 dt1970;
    private String dateMillis;
    public getListofFileTypes getListFiles;
    ArrayList<String> HandFileList = new ArrayList<String>();
    int backgroundIndex=0;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListFiles=new getListofFileTypes();
	    handFileIndex=0;
		dv = new DrawingView(this);
		df1970=new dateFrom1970();
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(StrokeWidth);
        
        initpaint=new Paint();
        initpaint.setColor(Color.WHITE);
        
        dt1970=new dateFrom1970();
        
        txtPaint = new Paint();
        txtPaint.setColor(Color.BLUE);
        txtPaint.setStyle(Paint.Style.STROKE);
        txtPaint.setStrokeWidth(0.0f);
        txtPaint.setTextSize(23.0f);
        
        actionBar = getActionBar();
		actionBar.setIcon(R.drawable.handwrite1);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("HandWrite ");
		dateMillis=dt1970.getmillisString();
        
		Bundle extras = getIntent().getExtras();
	      final String dname = extras.getString("Value1");
	      final String fname = extras.getString("Value4");
	      final String headerMail = extras.getString("headerMail");   // mail header
	      final String textMail= extras.getString("textmail");  // mail text body
	      final String mailaddr= extras.getString("mailaddr");  // mail text body
	
	      
	      
	      dname1=dname;
	      fname1=fname;
	      headerMail1=headerMail;
	      textMail1=textMail;
	      mailaddr1=mailaddr;
	      
	      
	      actionBar.setTitle("HandWrite ");
	     // Toast.makeText(getBaseContext(), dt1970.getmillisString().toString()      , Toast.LENGTH_SHORT).show();	
	     
	      
	    //  handFileIndex=getListFiles.getNumberofFiles(dname1, fname1, ".hnd");
	      HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");
	      //  RecFileList=getListFiles.getListofFiles(dname, fnameNN, ".3gp");
		  showActionBar();
	      int i999=0;
	;}

	
	
	public class DrawingView extends View {

		ArrayList <Float> pts = new ArrayList<Float>();
		int index=0;
		public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Paint   mBitmapPaint;
        Context context;
        private Path    mPath;
        public float downx,downy,upx,upy;
        
		public DrawingView(Context c) {
			super(c);
			// TODO Auto-generated constructor stub
			context=c;
			mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
                        
            
		}
		/* (non-Javadoc)
		 * @see android.view.View#onSizeChanged(int, int, int, int)
		 */
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			// TODO Auto-generated method stub
			super.onSizeChanged(w, h, oldw, oldh);
			dv.setBackgroundColor(initpaint.getColor());
			width = w;      // don't forget these
		    height = h;
			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
           // mCanvas.drawARGB(255, 255, 255, 255);
          dv.canvas_draw_ARGB(255, 255, 255, 255);
            dv.load_canvas();          
		}

		
		
		/* (non-Javadoc)
		 * @see android.view.View#onDraw(android.graphics.Canvas)
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            //canvas.drawPath( mPath,  mPaint);
            //canvas.drawPath( circlePath,  circlePaint);
            canvas.drawLine(canvas.getWidth()-20, 10, canvas.getWidth()-20, 50, mPaint);
            canvas.drawText(text_status
            //		+" "+Integer.toString(pts.size())+" "+Integer.toString(colorID)+" "
            //		+Float.toString(StrokeWidth)
            		, canvas.getWidth()-95, 25 , txtPaint);
            //canvas.drawPoints(pts, 0, pts.length/2, mPaint);
            //canvas.drawPath( mPath,  mPaint);
            
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
               
                pts.add((float) (colorID -120));
                pts.add(StrokeWidth-1200);
                downx=event.getX();
                downy=event.getY();
                
                pts.add(downx);
                pts.add(downy);
                
             // only for path - start
             //   mPath.reset();
             //   mPath.moveTo(downx, downy);
            // only for path - end
                	invalidate();
                    break;
   
                case MotionEvent.ACTION_MOVE:
                    //touch_move(x, y);
                	upx = event.getX();
                    upy = event.getY();
                    pts.add(upx);
                    pts.add(upy);
                    //  path add start
               //     float dx = Math.abs(upx - downx);
              //      float dy = Math.abs(upy - downy);
              //      if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
              //          mPath.quadTo(downx, downy, (upx + downx)/2, (upy + downy)/2);
              //          downx = upx;
              //          downy = upy;
              //          }
                    //  path add end
                    
                    mCanvas.drawLine(downx, downy, upx, upy, mPaint);
                    downx = upx;
                    downy = upy;  
                    
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    //touch_up();
                	upx = event.getX();
                    upy = event.getY();
                    pts.add(upx);
                    pts.add(upy);
                    pts.add((float) -1);  // end of current stroke
                    pts.add((float) -1);  // end of current stroke
                   
                    
                    
                    //path start here 
                    //mPath.lineTo(downx, downy);
                   // mPath.lineTo(upx, upy);
                    // commit the path to our offscreen
                   //e mCanvas.drawPath(mPath,  mPaint);
                    // kill this so we don't double draw
                   // mPath.reset();
                    // path end here
                    
                    
                    
                 mCanvas.drawLine(downx, downy, upx, upy, mPaint);    
                    invalidate();
                    break;
            }
			return true;
		}
////////////// clear   /////////////////////////////		
		public void clearDrawing()    {
	    //Utils.Log("RaghunandanDraw, how to clear....");

	    setDrawingCacheEnabled(false);
	    // don't forget that one and the match below,
	    // or you just keep getting a duplicate when you save.
	    
	    
	    // originals
	    //onSizeChanged(width, height, width, height);
	    //onSizeChanged(mCanvas.getWidth(), mCanvas.getHeight(), mCanvas.getWidth(), mCanvas.getHeight());
	    // end of originals 
	    //pts.clear();
	    mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
     //   mCanvas.drawARGB(255, 255, 255, 255);
        dv.canvas_draw_ARGB(255, 255, 255, 255);
        pts.clear();
        //mCanvas.drawRect(5, 5, 200, 250, mPaint);
	    invalidate();

	    setDrawingCacheEnabled(true);
	    pts.clear();
	    pts.add(-1f);pts.add(-1f);
	    
	    }
//////////////   load /////////////////////////////		
		public void load_canvas() {
	/////////////////////////////////////////////
				pts.clear();
				 HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");
				if (HandFileList.size()==0 ){
					Toast.makeText(getBaseContext(), "File not found .", Toast.LENGTH_SHORT).show();
					return;
				}
				else {
					int i9=0;
				    
    Memo=FileReadString.getFileString(dname1,HandFileList.get(handFileIndex));
				 		
		//		    Memo=FileReadString.getFileString(dname1,HandFileList.get(handFileIndex));
				// fname1+"_"+df1970.getmillisString()+".hnd"			
				List<String> list = new ArrayList<String>(Arrays.asList(Memo.split(",")));	   // new way
				 for (int i=0;i<list.size();i++){
					 pts.add(Float.valueOf(list.get(i))); 
				 }
			 
				 int s=0,add2=0;
		            while (  s<pts.size()   ){

		                if (pts.get(s)==(float)(-1) && (s>pts.size()-1)  ) {
		                    break;
		                }
		                if (pts.get(s)==(float)(-1) && (s<=pts.size()-1)  ) {
		                    add2=1;
              		                    
		                }
		                if (pts.get(s)<(float)(-100) ) {
		                    float c1= pts.get(s)+120;
		                    colorID= (int) c1;
		                    switch_colors();
		                    StrokeWidth= pts.get(s+1)+1200;
		                    mPaint.setStrokeWidth(StrokeWidth);
		                    add2=2;
	                }

		                if (pts.get(s)>=0 && pts.get(s+1)>=0 && pts.get(s+2)>=0 && pts.get(s+3)>=0) {
                  
		                	mCanvas.drawLine(pts.get(s), pts.get(s+1), pts.get(s+2), pts.get(s+3), mPaint);
		                    add2=2;
		                }
		                
		                invalidate();
		                s=s+add2;
		            }
		    
				Toast.makeText(HandDrawBmp1.this, "canvas loaded  ! "
				,  Toast.LENGTH_SHORT).show();
				}  	  
				  		       
		    	   		 				}
		    	   		
	
		
		
		public void canvas_draw_ARGB (int A, int R, int G, int B) {
		mCanvas.drawARGB(A, R, G, B);
		invalidate();
	}
		
		
		
		
		
		
		
		
//////////////delete /////////////////////////////		
public void delete_canvas() {
			
				File Path = new File(Environment.getExternalStorageDirectory(), dname1);
				if(!Path.exists()) {
				    Path.mkdirs();
				}
				if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
				    finish();
				    return;
				}
			 HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");
			 if (HandFileList.size()>0){
			 
			 File file = new File(Path, HandFileList.get(handFileIndex));
				if (!file.exists()) {
					Toast.makeText(getBaseContext(), "No handwrite file exists ", Toast.LENGTH_SHORT).show();
				    //finish();
				    return;
						}	
			
				boolean del =file.delete();
				if (del) {
					Toast.makeText(getBaseContext(), "file deleted succssefully "+String.valueOf(handFileIndex),
							Toast.LENGTH_SHORT).show();
					handFileIndex=0;
					dv.clearDrawing();
					dv.load_canvas();
		
					showActionBar();
				}
				else {
					Toast.makeText(getBaseContext(), "file not deleted ", Toast.LENGTH_SHORT).show();
				}
			 }
			 else {
					Toast.makeText(getBaseContext(), "Nothing to delete... ", Toast.LENGTH_SHORT).show();
 
			 }	
				
}
   	   		
   	   		 		
			
				 
	        
	///////////////////////////////////////////////////////////////////////////////////////////////////	
		
		public void step_StrokeWidth()  {
			if (StrokeWidth<18){ 
				StrokeWidth +=2 ;
					}
			else {
				StrokeWidth=0;
				}
			swith_StrokeWidth();
			//Toast.makeText(MainActivity.this, "Current stroke width is: "+Float.toString(StrokeWidth),  Toast.LENGTH_SHORT).show();
			update_status();
		}
		
				
		public void swith_StrokeWidth()  {
			mPaint.setStrokeWidth(StrokeWidth);
				}
		
		public void step_Colors()  {
			colorID++;
			if (colorID==colors.length-1){
				colorID=0;
					}
			switch_colors();
			
		}
		
		private void update_status() {
			invalidate();
		}
		
		
		public void switch_colors() {	
		switch (colorID) {
			case 0:
				mPaint.setColor(Color.BLACK);				
				break;
			case 1:
				mPaint.setColor(Color.BLUE);				
				break;
			case 2:
				mPaint.setColor(Color.CYAN);				
				break;
			case 3:
				mPaint.setColor(Color.GREEN);				
				break;
			case 4:
				mPaint.setColor(Color.GRAY);				
				break;
			case 5:
				mPaint.setColor(Color.MAGENTA);				
				break;	
			case 6:
				mPaint.setColor(Color.RED);				
				break;	
			case 7:
				mPaint.setColor(Color.YELLOW);				
				break;
			case 8:
				mPaint.setColor(Color.WHITE);				
				break;
				
				
			default:
				break;
			
			}
						
			//Toast.makeText(MainActivity.this, "Current Color is: "+colors[colorID]    
			//+" Current stroke width is: "+Float.toString(StrokeWidth),  Toast.LENGTH_SHORT).show();
			update_status();
		}
		
		public void setColorBlue(){
		mPaint.setColor(Color.BLUE);
		update_status();
		}
		public void setColorRed(){
			mPaint.setColor(Color.RED);
			update_status();
			}
		/////////////////////////////////////////
//////////////  save   /////////////////////////////	
		public void save_canvas(boolean b) {
			
			if (!b && getListFiles.getNumberofFiles(dname1, fname1, ".hnd")>0){
			hand_field=HandFileList.get(handFileIndex);	
			}
			else {
			hand_field =fname1+"_"+df1970.getmillisString()+".hnd";
			HandFileList.add(hand_field);
			handFileIndex=HandFileList.size()-1;
			}
			newOrExistingFileToSave=false;
			
			String data="";	
			for (int i=0;i<pts.size()-1;i++) {
			data=data+ Float.toString(pts.get(i))+",";
					}
			data=data+ Float.toString(pts.get(pts.size()-1));
		//	dv.delete_canvas();
			      try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
   String err=	FileWriteString.setFileString(dname1,hand_field, data); 
					if (err.equals("-1")){
						Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
					}

						      
					else {      Toast.makeText(HandDrawBmp1.this, "canvas saved  ! ", Toast.LENGTH_SHORT).show();
							}
			      
			      
//			      Toast.makeText(handdraw.this, "canvas saved  ! "   , Toast.LENGTH_SHORT).show();

			      
			      
			      //			      HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd");
//handFileIndex=HandFileList.size()-1;
	//		      dv.clearDrawing();
//					dv.load_canvas();
			      
			      
			      
					showActionBar();
			      //hand_field=dateMillis+"_hand.hnd";
				}
				
		private void write_file_string(String dwrite_,String fname_, String data) {
			// TODO Auto-generated method stub
			File rootPath = new File(Environment.getExternalStorageDirectory(), dwrite_);
	      if(!rootPath.exists()) {
	          rootPath.mkdirs();
	      }
	      
	      
			
	      File dataFile = new File(rootPath, fname_);  //get file name string
	      if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	          Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
	          finish();
	          return;
	      }
	      
	      try {           
	          FileOutputStream mOutput = new FileOutputStream(dataFile, false);
	          mOutput.write(data.getBytes());
	          mOutput.close();
	      } catch (FileNotFoundException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      
	 }
	
		
		////////////////////////////
		public void save_canvas_bitmap() {
	//		HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd");
	//		hand_field=HandFileList.get(handFileIndex);	
			
			
			File rootPath = new File(Environment.getExternalStorageDirectory(), dname1);
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
		
//		File output = new File(rootPath, hand_field+"_pic.PNG");
		File output = new File(rootPath, "zxc______zxcv.PNG");
		OutputStream os = null;
		 
		try {
		  os = new FileOutputStream(output);
		  mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		  os.flush();
		  os.close();

		} catch (Exception e) {
			}
		Toast.makeText(getApplicationContext(), "Bitmap saved", Toast.LENGTH_SHORT).show();
		
		
		}
		///////////////////////////
		
		
				
//	public String set_handwrite_field(String field1){
//		
//		  String[] a231 = Memo_txt.split(",");
//		  a231[4]=field1;   // over-ride
//		  //Toast.makeText(getBaseContext(),"a23 length.... " +a23.length+"" , Toast.LENGTH_SHORT).show();
//		    if (a231.length>8) {    // break , chnage hand field, combine message again
//		   	for ( int j=8 ;j < a231.length; j++) {
//		        a231[j]="," +a231[j];
//		   		a231[7]=a231[7]+a231[j]; 
//		        	}
//		    	}
//		Memo_txt= a231[0]+","+a231[1]+","+a231[2]+","+a231[3]+","+
//		    	a231[4]+","+a231[5]+","+a231[6]+","+a231[7];
//			return 	Memo_txt;
//		}			
				
				/*
				try
			    {
			      FileOutputStream fos = new FileOutputStream(strFilePath);
			      DataOutputStream dos = new DataOutputStream(fos);
			       for (int i=0;i<pts.size();i++) {
			    	   float f = pts.get(i);  
			    	   dos.writeFloat(f);
			    	       }
			      
			         dos.close();
			       
			    }
			    catch (IOException e)
			    {
			      System.out.println("IOException : " + e);
			    }
		*/
				
		
			
	
		
		
		
		
		
		
		
		
	/////////	
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drawhnd1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
		case R.id.clr_canvas:
			dv.clearDrawing();
			newOrExistingFileToSave=true;
			Toast.makeText(HandDrawBmp1.this, "canvas cleared  ! ",  Toast.LENGTH_SHORT).show();
			break;
		
		case R.id.save_canvas:
			dv.save_canvas(newOrExistingFileToSave);
		//	String err=	FileWriteString.setFileString(dname1, fname1, dv.set_handwrite_field(dateMillis+"_hand.hnd")); 
			//dv.write_file_string(dname1, fname1, Memo_txt);
			
			//  DO SOFT enter to handdraw intent
			
			break;
		
					
		case R.id.save_bitmap:
		//	dv.save_canvas(newOrExistingFileToSave);
	//		dv.load_canvas();
			if (HandFileList.size()>0){
			dv.save_canvas_bitmap();
			}
			break;
				
				
		case R.id.mailto:
			//zxc______zxcv
//		 HandFileList=getListFiles.getListofFiles(dname1, fname1, ".PNG");
//			 String f1=HandFileList.get(handFileIndex);
			if (HandFileList.size()>0){
				dv.save_canvas(newOrExistingFileToSave);
				dv.save_canvas_bitmap();
				ShareViaEmail(dname1,"zxc______zxcv.PNG");
			
			}	 
			else {
Toast.makeText(HandDrawBmp1.this, "No hand draw pictures to send ",  Toast.LENGTH_SHORT).show();

			} 
				break;
				
						
		case R.id.delete1:
			dv.delete_canvas();
			//dv.set_handwrite_field("Z");
			//dv.write_file_string(dname1, fname1, Memo_txt);
			//String errr=	FileWriteString.setFileString(dname1, fname1, dv.set_handwrite_field("Z")); 
			break;
			
			
		case R.id.erase_color:
			
			isEraseing=!isEraseing;
			if (isEraseing) {  
			push_colorID=colorID;
			colorID=8;
			dv.switch_colors();
			push_paintStroke=StrokeWidth;
			StrokeWidth=16.0f;
			dv.swith_StrokeWidth();
			text_status="Erase";
			dv.update_status();
				}
			else {
				colorID=push_colorID;
				//Toast.makeText(MainActivity.this, "color is ! "+ Integer.toString(colorID),  Toast.LENGTH_SHORT).show();
				dv.switch_colors();
				StrokeWidth=push_paintStroke;
				dv.swith_StrokeWidth();
				text_status="Paint";
				dv.update_status();
					}
			
			break;
			
		case R.id.step_color:
			dv.step_Colors();
			break;
			
		case R.id.step_stroke:
			dv.step_StrokeWidth();
			break;
		
		case R.id.background:
			backgroundIndex++;
			if (backgroundIndex>3) backgroundIndex=0;
			
			switch (backgroundIndex) {
			case 0:
				dv.canvas_draw_ARGB(255, 255, 255, 255);
				break;
			case 1:
				dv.canvas_draw_ARGB(255, 255, 255, 0);
				break;
			case 2:
				dv.canvas_draw_ARGB(40, 0, 120, 0);
				break;
			case 3:
				dv.canvas_draw_ARGB(40, 0, 80, 40);
				break;
				
				
			}
			
			//dv.step_StrokeWidth();
			break;
			
			
			
		case R.id.exit:

			HandDrawBmp1.this.finish();
			break;
			
		case R.id.next:
		    //  handFileIndex=getListFiles.getNumberofFiles(dname1, fname1, ".hnd");
		      HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");

			handFileIndex++;  
			
			if (  (handFileIndex>HandFileList.size()-1) && HandFileList.size()>0)
			{
				handFileIndex=0;
			}
			dv.clearDrawing();
			dv.load_canvas();
			showActionBar();						
			break;
			
		case R.id.previous:
		    //  handFileIndex=getListFiles.getNumberofFiles(dname1, fname1, ".hnd");
		      HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");

			handFileIndex--;  
			if (  (handFileIndex<0 && HandFileList.size()>0))
			{
				handFileIndex=HandFileList.size()-1;
			}
			dv.clearDrawing();
			dv.load_canvas();						
			showActionBar();
			break;
			
		default:
			break;
		
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() 
	{
		HandDrawBmp1.this.finish();
			}
	
public void showActionBar(){

    HandFileList=getListFiles.getListofFiles(dname1, fname1, ".hnd",".HND");

		if (HandFileList.size()>0)
			actionBar.setTitle("Hand Draw " +fname1+": "+
					String.valueOf(1+handFileIndex)+"/"+String.valueOf(HandFileList.size())) ;
		else 
			actionBar.setTitle("Hand Draw " +fname1+": "+
					String.valueOf(0)+"/"+String.valueOf(HandFileList.size())+" - No files") ;
	}


// send mail with bitmap
private void ShareViaEmail(String folder_name, String file_name) {
    try {
//    	File Path = new File(Environment.getExternalStorageDirectory(), folder_name);
//			
//			File file = new File(Path, file_name);
//    	String f=file.toString();
    //	File Root= Environment.getExternalStorageDirectory();
    //    String filelocation=Root.getAbsolutePath() + folder_name + "/" + file_name;
    	File root = Environment.getExternalStorageDirectory();
    	String pathToMyAttachedFile = folder_name+"/"+file_name;
    //	String pathToMyAttachedFile = "memo_files/12";
    	File file = new File(root, pathToMyAttachedFile);
       	Uri uri = Uri.fromFile(file);
    	//emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
    	
    	Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
       // String message="File to be shared is " + file_name + ".";
        intent.putExtra(Intent.EXTRA_SUBJECT, headerMail1);
       // Uri path11 = Uri.fromFile(filelocation); 
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, textMail1);
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

//Intent emailIntent = new Intent(Intent.ACTION_SEND);
//emailIntent.setType("text/plain");
//emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
//emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
//emailIntent.putExtra(Intent.EXTRA_TEXT, "body text");
//File root = Environment.getExternalStorageDirectory();
//String pathToMyAttachedFile = "temp/attachement.xml";
//File file = new File(root, pathToMyAttachedFile);
//if (!file.exists() || !file.canRead()) {
//return;
//}
//Uri uri = Uri.fromFile(file);
//emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
//









}




/*
 private void read_file() {
		// TODO Auto-generated method stub
		String strFilePath="data.bin";
		 try
		    {
		      //create FileInputStream object
		      FileInputStream fin = new FileInputStream(strFilePath);
		       DataInputStream din = new DataInputStream(fin);
		     		       
		             for (int i=0;i<pts.length;i++) {
		    	   	   pts[i]= (float)     (din.readFloat());  
		  		       }
		       
		         //System.out.println("float : " + f);
		          din.close();
		       
		    }
		    catch(FileNotFoundException fe)
		    {
		      System.out.println("FileNotFoundException : " + fe);
		    }
		    catch(IOException ioe)
		    {
		      System.out.println("IOException : " + ioe);
		    }
		  }
 
 
 
 */



