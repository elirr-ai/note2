package com.example.notes2;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class DrawCropMainActivity extends Activity implements View.OnClickListener{
	private Bitmap bitmap=null;
	private DrawCropCanvasView customCanvas;
	private Button button1,button2,rotate,button3,button4;
	private int mode=0;
	private int width=0;
	private int height=0;
	int[] shapes =new int[]{R.drawable.drawcropstar, R.drawable.drawcropcircle, R.drawable.drawcroprect,};
	private BitmapTask task;
	private ShowBMPTask taskLoadBitmap;
	public boolean busyFlag=false;
	public boolean rotateMode=false;
	
//	static final private String DIR="ZZZ"; 
//	static final private String INPUTFILE="1.png";
	static final private String OUTPUTFILE="shapes_";
	static final private String EXTENSION=".png";
	static final private int hScale = 80;

	static final private String Rotate_MSG_OFF ="Rotate\n\rOff";
	static final private String Rotate_MSG_ON ="Rotate\n\rOn";

	String dname1,fname1,fnameLong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_draw_crop_main);
		
		Bundle extras = getIntent().getExtras();  
        dname1 = extras.getString("dirName");  
        fname1 = extras.getString("fileNameShort");
        fnameLong= extras.getString("fileNameFull");
		
        customCanvas = (DrawCropCanvasView) findViewById(R.id.signature_canvas);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        rotate = (Button) findViewById(R.id.rotate);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);  
        
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        rotate.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        
        button1.setTextColor(Color.WHITE);
        button2.setTextColor(Color.YELLOW);
        button3.setTextColor(Color.YELLOW);
        button4.setTextColor(Color.YELLOW);
        rotate.setTextColor(Color.YELLOW);

        
        button1.setBackgroundResource(R.drawable.drawcropclear);
        button2.setBackgroundResource(shapes[mode]);
        button3.setBackgroundResource(R.drawable.drawcropsave);
        button4.setBackgroundResource(R.drawable.drawcropiconexit);
        rotate.setBackgroundResource(R.drawable.drawcroprotate);

        rotate.setText(Rotate_MSG_OFF);
//		 bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.image1);
//		 customCanvas.setBitmap(bitmap);
        
	}
	
	
	
	@Override
	public void onBackPressed() {
		if (!busyFlag){
	        Intent intent = new Intent();
	        intent.putExtra("keyName", "1234");
	        setResult(RESULT_OK, intent);
			finish();
		}
		else { 
//			Toast.makeText(context, "Busy - Please wait... ",	Toast.LENGTH_LONG).show();
			}
		super.onBackPressed();
	}



	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (height==0 || width==0){
			width=DrawCropDisplParams.getScreenWidth();
			height=DrawCropDisplParams.getScreenHeight();
			int mh=height/120,mw=width/80;
			ViewGroup.MarginLayoutParams i1=null;
		i1 =(ViewGroup.MarginLayoutParams) customCanvas.getLayoutParams();
					i1.width=width;
					i1.height=(height*hScale)/100;
					i1.setMargins(0, 0,0,0);
					customCanvas.requestLayout();
					
					i1 =(ViewGroup.MarginLayoutParams) button1.getLayoutParams();
					i1.width=width*16/100;
					i1.height=(height*10)/100;
					i1.setMargins(mw, mh,0,0);
					button1.requestLayout();
					
					i1 =(ViewGroup.MarginLayoutParams) button2.getLayoutParams();
					i1.width=width*16/100;
					i1.height=(height*10)/100;
					i1.setMargins(mw, mh,0,0);
					button2.requestLayout();
					
					i1 =(ViewGroup.MarginLayoutParams) rotate.getLayoutParams();
					i1.width=width*16/100;
					i1.height=(height*10)/100;
					i1.setMargins(mw, mh,0,0);
					rotate.requestLayout();

					i1 =(ViewGroup.MarginLayoutParams) button3.getLayoutParams();
					i1.width=width*16/100;
					i1.height=(height*10)/100;
					i1.setMargins(mw, mh,0,0);
					button3.requestLayout();
					
					i1 =(ViewGroup.MarginLayoutParams) button4.getLayoutParams();
					i1.width=width*16/100;
					i1.height=(height*10)/100;
					i1.setMargins(mw, mh,0,0);
					button4.requestLayout();
					
					taskLoadBitmap = new ShowBMPTask();
					taskLoadBitmap.execute();	
					
					customCanvas.seRotateAngel(0);
		}

		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.draw_crop_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			customCanvas.RotateBitmap(90.0f);
			return true;
		}

		if (id == R.id.item1) {
			mode=0;
			customCanvas.setMode(mode);
			return true;
		}
		
		if (id == R.id.item2) {
			mode=1;
			customCanvas.setMode(mode);
			return true;
		}
		
		if (id == R.id.item3) {
			mode=2;
			customCanvas.setMode(mode);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.button1){
			customCanvas.clearPath();
			customCanvas.setBitmap(bitmap);
		} 
		if(v.getId()==R.id.button2){  // mode
			if (mode>=10) mode-=10;
			mode++;
			if (mode==3) mode=0;
			button2.setBackgroundResource(shapes[mode]);
			customCanvas.setMode(mode);
			customCanvas.clearPath();
			
		}
		if(v.getId()==R.id.rotate){
			rotateMode=!rotateMode;
			if (!rotateMode){
				rotate.setText(Rotate_MSG_OFF);
				customCanvas.seRotateAngel(0);
				if (mode>=10) mode-=10;  // restore mode
//				button2.setBackgroundResource(shapes[mode]);
				customCanvas.setMode(mode);
				customCanvas.clearPath();				
			}
			else {
				rotate.setText(Rotate_MSG_ON);
				mode+=10;
				customCanvas.setMode(mode);
				customCanvas.seRotateAngel(0);
			}
			
		}		
		
		if(v.getId()==R.id.button3){  // save bitmap
			int m=customCanvas.getMode();
			Bitmap b=customCanvas.getBitmap();
			String s=OUTPUTFILE+String.valueOf(m)+EXTENSION;
	           task = new BitmapTask(b,dname1,fname1+"_"+String.valueOf(System.currentTimeMillis())+".JPG");
	            task.execute(0);	
		} 

		if(v.getId()==R.id.button4){  // exit
		if (!busyFlag){
	        Intent intent = new Intent();
	        intent.putExtra("keyName", "1234");
	        setResult(RESULT_OK, intent);
			finish();
		}
		
		
		
		else { 
//			Toast.makeText(context, "Busy - Please wait... ",	Toast.LENGTH_LONG).show();
			}
		} 

	}

    class BitmapTask extends AsyncTask<Integer, Integer, Long> {
    	Bitmap bm;String dir;String file;
        public BitmapTask (Bitmap b,String d, String s){
        	bm=b;dir=d;file=s;
        }
        
        @Override
		protected void onPreExecute() {
        	busyFlag=true;
            button3.setBackgroundResource(R.drawable.drawcropsavered);
			super.onPreExecute();
		}

		@Override
        protected Long doInBackground(Integer... params) {
//          publishProgress(0); 
			SaveBitmapFile sbm=new SaveBitmapFile();
			sbm.saveBitmapJPG(bm, dir, file);
//			DrawCropSaveBitmapFile.saveBitmap(bm, dir, file); 
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            button3.setBackgroundResource(R.drawable.drawcropsave);
        	busyFlag=false;
        } 

        
        protected void onProgressUpdate(Integer... values) {
            }
}
	
    private class ShowBMPTask extends AsyncTask<Void,Boolean,Bitmap>{ 
    	protected void onPreExecute(){
    			   super.onPreExecute();
    		     }
    		   protected Bitmap doInBackground(Void...params){ 

//Bitmap b3a=DrawCropResizeBitmap1.getResizedBitmapFromFile(width,height,DIR,INPUTFILE);
Bitmap b3a=DrawCropResizeBitmap1.getResizedBitmapFromFile(width,height,dname1,fnameLong);
Bitmap b4 = Bitmap.createScaledBitmap(b3a,width, (hScale*height)/100, true);
  						publishProgress(true);
   			   return b4;
    		     }  
    		   @Override
    	protected void onProgressUpdate(Boolean... values) {		   
    		super.onProgressUpdate(values);
    	}
    		   @Override	    
    	protected void onPostExecute(Bitmap b){
    			   bitmap=b;
    			   customCanvas.setBitmap(bitmap);
    Toast.makeText(getApplicationContext(), "From backgroud: bitmap completed",Toast.LENGTH_SHORT).show();
    		     }//end of post....
    }

    
}

