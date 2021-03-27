package com.example.notes2;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Display {
double screenInches;
//double wp;
//double xdpi;
double x;
//double hp;
//double ydpi;
double y ;


	public Display( ) {
		super();
		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
		screenInches =	Math.sqrt(Math.pow(dm.widthPixels/dm.xdpi,2)+
						Math.pow(dm.heightPixels/dm.ydpi,2));
		x=dm.widthPixels;
		y=dm.heightPixels;
		}
		
		//(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		//DisplayMetrics dm = new DisplayMetrics();
	//	getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		 //wp=dm.widthPixels;
		// xdpi=dm.xdpi;
		// double x = Math.pow(dm.widthPixels/dm.xdpi,2);
		
		// hp=dm.heightPixels;
		// ydpi=dm.ydpi;
		// double y = Math.pow(dm.heightPixels/dm.ydpi,2);

	
public double getScreenInches(){
	return screenInches;
}	
	
public double getScreenWidth(){
	return x;
}	

public double getScreenHeight(){
	return y;
}	



}
