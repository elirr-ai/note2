package com.example.notes2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivityNotepage1 extends Activity implements OnTouchListener,OnClickListener{

	Context context=this;
	RelativeLayout rl;
	TextView tv;
	ImageView iv;
	Button btn;
	int downx,downy;
    int gridX=8,gridY=8;
    int width=0,height=0;
	int functionSelect=0;
	int pageColorSelect=0;
	int gridXcolor=0,gridYcolor=0;
	String[] function={"Set page color", "set vertical color","set horzontal color","set # of vert grid","set # of horiz grids"};
    String[] colors={    "#A9A9A9","#C0C0C0","#D3D3D3","#DCDCDC","#F5F5F5","#FFFFFF","#B0C4DE","#E6E6FA","#FFFAF0","#F0F8FF","#F8F8FF","#F0FFF0",
    	    "#FFFFF0","#F0FFFF","#FFFAFA","#FFE4B5","#FFDEAD","#FFDAB9","#FFE4E1","#FFF0F5","#FAF0E6","#FDF5E6","#FFEFD5","#FFF5EE","#F5FFFA",
    	    "#FFC0CB","#FAEBD7","#F5F5DC","#FFE4C4","#FFEBCD","#F5DEB3","#FFF8DC","#FFFACD","#FAFAD2","#FFFFE0","#00FFFF","#E0FFFF","#00CED1",
    	    "#40E0D0","#48D1CC","#AFEEEE","#7FFFD4","#B0E0E6"};
   
    String[] colorLines={
    		"#000000","#FFFFFF","#FF0000",	"#00FF00","#0000FF","#FFFF00","#00FFFF","#FF00FF","#800000","#8B0000",
    		"#A52A2A","#B22222","#DC143C","#FF0000","#FF6347","#006400","#008000","#228B22","#00BFFF","#1E90FF",
    		"#191970","#000080","#00008B","#0000CD","#0000FF","#4169E1","#8A2BE2","#A0522D","#D2691E"		
    };

//    Bitmap imageBitmap = null;
    SharedPreferences sharedpreferences;;
    
    //keys
    public static final String MyPREFERENCES = "MyPrefs" ;  // my pref internal folder name

//    private static String MYPREFS="MYPREFS";
    private static String PAGECOLORINDEX="PAGECOLORINDEX";
    private static String HORIZONTALPAGECOLORINDEX="HORIZONTALPAGECOLORINDEX";
    private static String VERTICALPAGECOLORINDEX="VERTICALPAGECOLORINDEX";
    private static String HORIZONTALPAGELINESINDEX="HORIZONTALPAGELINESINDEX";
    private static String VERTICALPAGELINESINDEX="VERTICALPAGELINESINDEX";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_notepage1);
		rl=(RelativeLayout)findViewById(R.id.rl);
		iv=(ImageView)findViewById(R.id.iv);
		tv=(TextView)findViewById(R.id.tv);
		btn=(Button)findViewById(R.id.btn);
		iv.setOnTouchListener(MainActivityNotepage1.this);
		btn.setOnClickListener(MainActivityNotepage1.this);
		
		btn.setText(function[functionSelect]+" "+functionSelect);
		
	      sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	      getSharedPerfs();
		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (height==0 || width==0){
//		height=getResources().getDisplayMetrics().heightPixels;
//		width=getResources().getDisplayMetrics().widthPixels;
		
		height=rl.getHeight();
		width=rl.getWidth();
		
		setImageViewParams(iv, height,width);
		setButtonViewParams(btn, height,width);
//		Toast.makeText(getApplicationContext(), "Your "+width+" "+height, Toast.LENGTH_SHORT).show(); 

//		showIV(height/2,width,gridX, gridY, colors[pageColorSelect],colorLines[gridXcolor],colorLines[gridYcolor]);	
		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
				colorLines[gridXcolor],colorLines[gridYcolor]));	

	tv.setText(" h  "+height+"  w  "+width);	
		
		}
		super.onWindowFocusChanged(hasFocus);
	}

	private void showIV(int h,int w,int gridX,int gridY,String color, String colorlineX, String colorlineY) {
//
//		Paint pgridx = new Paint();
//		pgridx.setColor(Color.parseColor(colorlineX));
//		pgridx.setStrokeWidth(2);
//		Paint pgridy = new Paint();
//		pgridy.setColor(Color.parseColor(colorlineY));
//		pgridy.setStrokeWidth(2);
//		
//        imageBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(imageBitmap);
//        canvas.drawColor((Color.parseColor(color)));
//        float scale = getResources().getDisplayMetrics().density;
//        Paint p = new Paint();
//        p.setColor(Color.BLUE);
//        p.setTextSize(48*scale);
//        canvas.drawText("H "+String.valueOf(gridX)+"  X   "+gridY, iv. getWidth()/2,
//        iv.getHeight()/2, p);
//           
//          for (int i=0;i<gridY+1;i++){
//        	  canvas.drawLine(0, i*h/gridY, w, i*h/gridY, pgridx);  // Horizontal
//          }
//          
//          for (int i=0;i<gridX+1;i++){
//        	  canvas.drawLine(i*w/gridX, 0, i*w/gridX, h, pgridy);  
//          }         
//          iv.setImageBitmap(imageBitmap);	
		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, h, w, gridX, gridY, color, colorlineX, colorlineY));	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_notepage1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
//			SaveBitmapFile.saveBitmap(imageBitmap, "Pictures", "AAAAAAAAAAAAAAA.BMP");

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId()==R.id.iv){
			int maskedAction = event.getActionMasked();

			switch (maskedAction) {

	        case MotionEvent.ACTION_DOWN:{
	        	downx=(int)event.getX();downy=(int)event.getY();
//	        	Toast.makeText(getApplicationContext(), "Your pile is Empty - Press PC GO", Toast.LENGTH_SHORT).show(); 
 	
	        	break;
	        }

	        case MotionEvent.ACTION_MOVE: { // a pointer was moved
	           
	            break;
	        }
	        case MotionEvent.ACTION_UP:{
	        	int xf=(int)event.getX();int yf=(int)event.getY();
//        		Toast.makeText(getApplicationContext(), "shows "+functionSelect, Toast.LENGTH_SHORT).show(); 
	        	
	        	if (functionSelect==0){
	    			pageColorSelect++;
	    			if (pageColorSelect>colors.length-1) pageColorSelect=0;
//	    	    	showIV(height/2,width,gridX, gridY, colors[pageColorSelect],colorLines[gridXcolor],colorLines[gridYcolor]);	
	    			iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
	    					colorLines[gridXcolor],colorLines[gridYcolor]));	

	        	}
	        	
	        	if (functionSelect==2){
	    			gridXcolor++;
	    			if (gridXcolor>colorLines.length-1) gridXcolor=0;
//	    	    	showIV(height/2,width,gridX, gridY, colors[pageColorSelect],colorLines[gridXcolor],colorLines[gridYcolor]);	
	    			iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
	    					colorLines[gridXcolor],colorLines[gridYcolor]));	

	        	}
	        	
	        	if (functionSelect==1){
	        		gridYcolor++;
	    			if (gridYcolor>colorLines.length-1) gridYcolor=0;
//	    	    	showIV(height/2,width,gridX, gridY, colors[pageColorSelect],colorLines[gridXcolor],colorLines[gridYcolor]);	
    			iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
	    					colorLines[gridXcolor],colorLines[gridYcolor]));	

	        	}
	        	
	        	
	        	
	        	else if (functionSelect==3){
		        	if (Math.abs(downy-yf)<50 && (xf-downx)>width/100){
		        		gridX++;
		        		if (gridX>40) gridX=1;
		        		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
		        				colorLines[gridXcolor],colorLines[gridYcolor]));	
//		            	Toast.makeText(getApplicationContext(), "RIGHT "+downx+" "+downy+" "+xf+" "+yf, Toast.LENGTH_LONG).show(); 
		        	}
		        	
		        	if (Math.abs(downy-yf)<50 && (downx-xf)>width/100){
		        		gridX--;
		        		if (gridX<1) gridX=1;
		        		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
		        				colorLines[gridXcolor],colorLines[gridYcolor]));	
//		            	Toast.makeText(getApplicationContext(), "RIGHT "+downx+" "+downy+" "+xf+" "+yf, Toast.LENGTH_LONG).show(); 
		        	}
		        	
	        	} 
	    
	        	else if (functionSelect==4){
		        	if (Math.abs(downy-yf)<50 && (xf-downx)>width/100){
		        		gridY++;
		        		if (gridY>40) gridY=1;
		        		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
		        				colorLines[gridXcolor],colorLines[gridYcolor]));	

//		            	Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show(); 
		        	}
		        	
		        	else if (Math.abs(downy-yf)<50 && (downx-xf)>width/100){
		        		gridY--;
		        		if (gridY<1) gridY=1;
		        		iv.setImageBitmap(CreateBMPfromParams1.getIVBMP(context, height/2, width, gridX, gridY, colors[pageColorSelect],
		        				colorLines[gridXcolor],colorLines[gridYcolor]));	

//		            	Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show(); 
		        	}
	        	
	        	}
	        	break;
	        }

	        case MotionEvent.ACTION_CANCEL: {
	          
	            break;
	        }
	    }
	    

	        return true;
	}

		return false;
	}
	
	   private void setImageViewParams(ImageView iv, int h,int w){
		   
		   ViewGroup.MarginLayoutParams ipet1 = (ViewGroup.MarginLayoutParams) iv.getLayoutParams();
		   ipet1.width=w;
		   ipet1.height=(80*h)/100;
		   ipet1.setMargins(0,   //left
		 		0,    //top
		 		0,0);
		   
//		   tv.setCompoundDrawablesWithIntrinsicBounds(null, null,
//		   		scaleDrawable((Drawable)iconWeatherMap.get(draw), tv), null);
//		   tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,(heightTvPb*factorFontSize/100));
//		   tv.setBackgroundColor(color);
//		   iv.setPadding(pad, 0, 0, 0);
		   iv.requestLayout();		   
	   }
	   
	   private void setButtonViewParams(Button btn, int h,int w){
		   
		   ViewGroup.MarginLayoutParams ipet1 = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();
		   ipet1.width=w;
		   ipet1.height=(15*h)/100;
		   ipet1.setMargins(0,   //left
		 		0,    //top
		 		0,0);
		   btn.requestLayout();		   
	   }


	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.btn){

			functionSelect++;
			if (functionSelect>function.length-1) functionSelect=0;
			btn.setText(function[functionSelect]+" "+functionSelect);	

		}
		
	}
	

	private void getSharedPerfs() {
	
		if (sharedpreferences.getString(PAGECOLORINDEX, "").equals("")){
			SharedPreferences.Editor editor = sharedpreferences.edit();			
          editor.putString(HORIZONTALPAGELINESINDEX, "2").commit();
          editor.putString(VERTICALPAGELINESINDEX, "2").commit();
          editor.putString(HORIZONTALPAGECOLORINDEX, "2").commit();
          editor.putString(VERTICALPAGECOLORINDEX, "2").commit();
          editor.putString(PAGECOLORINDEX, "2").commit();
		}
		pageColorSelect=Integer.valueOf(sharedpreferences.getString(PAGECOLORINDEX, ""));
		gridX=Integer.valueOf(sharedpreferences.getString(HORIZONTALPAGELINESINDEX, ""));
		gridY=Integer.valueOf(sharedpreferences.getString(VERTICALPAGELINESINDEX, ""));
		gridXcolor=Integer.valueOf(sharedpreferences.getString(HORIZONTALPAGECOLORINDEX, ""));
		gridYcolor=Integer.valueOf(sharedpreferences.getString(VERTICALPAGECOLORINDEX, ""));

	}
	
	private void saveSharedPerfs() {
		
		SharedPreferences.Editor editor = sharedpreferences.edit();	
		
          editor.putString(HORIZONTALPAGELINESINDEX, String.valueOf(gridX)).commit();
          editor.putString(VERTICALPAGELINESINDEX, String.valueOf(gridY)).commit();
          editor.putString(HORIZONTALPAGECOLORINDEX, String.valueOf(gridXcolor)).commit();
          editor.putString(VERTICALPAGECOLORINDEX, String.valueOf(gridYcolor)).commit();
          editor.putString(PAGECOLORINDEX, String.valueOf(pageColorSelect)).commit();
	}

	@Override
	public void onBackPressed() {
		saveSharedPerfs();
		MainActivityNotepage1.this.finish();
		super.onBackPressed();
	}
	
	
	
	
}
