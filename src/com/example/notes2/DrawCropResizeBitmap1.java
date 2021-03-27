package com.example.notes2;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class DrawCropResizeBitmap1 {

	public static Bitmap getResizedBitmapAA(Bitmap bm, int newWidth, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
//	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//	    bm.recycle();
	    return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	}
	
	
	public static Bitmap getResizedBitmapFromFile(int targetW, int targetH,
			String d, String f) {
		
		String path = Environment.getExternalStorageDirectory()+"/"+d+"/"+f; 
//		File imgFile = new File(path);
		String imagePath=new File(path).toString();
		
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    //inJustDecodeBounds = true <-- will not load the bitmap into memory
	    bmOptions.inJustDecodeBounds = true;
	    bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    BitmapFactory.decodeFile(imagePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//	    int scaleFactor = photoW/targetW;
	    
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
//	    Bitmap bitmapX = BitmapFactory.decodeFile(imagePath, bmOptions);
	    return(BitmapFactory.decodeFile(imagePath, bmOptions));
	}		
	
	public static int[] getD(Activity  a){
		final DisplayMetrics dm = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return new int[]{width,height};
		}
	
	public static int getMaxWH(int[] a){  // ??????????
		if (a[0]>1500 || a[1]>1500)
		{
			return 1500;
			}
		else if  (a[0]<a[1]) return a[0];
		else return a[1];
		
		
		
		}
	}


