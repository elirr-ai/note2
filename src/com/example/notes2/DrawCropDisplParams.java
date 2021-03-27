package com.example.notes2;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DrawCropDisplParams {


//	public DisplParams( ) {
//		super(); 		}
		
		//(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		//DisplayMetrics dm = new DisplayMetrics();
	//	getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		 //wp=dm.widthPixels;
		// xdpi=dm.xdpi;
		// double x = Math.pow(dm.widthPixels/dm.xdpi,2);
		
		// hp=dm.heightPixels;
		// ydpi=dm.ydpi;
		// double y = Math.pow(dm.heightPixels/dm.ydpi,2);

	
public static double getScreenInches(){
		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
return Math.sqrt(Math.pow(dm.widthPixels/dm.xdpi,2)+
			Math.pow(dm.heightPixels/dm.ydpi,2));
}	
	
public static float getScreendnsity(){
	DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
	return dm.density;
}	
	
	

public static int getScreenWidth(){
	DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
return dm.widthPixels;
}	
	
public static int getScreenHeight(){
	DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
	return dm.heightPixels;
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
