package com.example.notes2;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

public class GestureAddSimple1MainActivity extends Activity {

	File dataFile;
	
	
    private GestureLibrary gLib;
    private static final String TAG = "SaveGestureActivity";
    private boolean mGestureDrawn;                      //tc
    private Gesture mCurrentGesture;                    //tc
    private String mGesturename;                        //tc

//	private final static String DIR="GestureLib";
	private final static String FILE="gestureaa.txt";
	private String gestureDir;
	ArrayList<Gesture> al =new ArrayList<Gesture>();
//	ArrayList<ArrayList<Gesture>>   alLIST  =new ArrayList<ArrayList<Gesture>>();
	ArrayList<GestureHolder> alholder =new ArrayList<GestureHolder>();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesture_add_activity_main);
        Log.d(TAG, "path = " + Environment.getExternalStorageDirectory().getAbsolutePath());
		gestureDir = getIntent().getStringExtra("DIR1");

        //openOptionsMenu();

		File rootPath = new File(Environment.getExternalStorageDirectory(), gestureDir);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
	    dataFile = new File(rootPath, FILE);
//		mLibrary = GestureLibraries.fromFile(dataFile);
//	boolean bload=mLibrary.load();
        
//        gLib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gestureabcdef.txt");
	    gLib = GestureLibraries.fromFile(dataFile);
	  boolean b7=  gLib.load();
//gLib.

Set<String> setStr = gLib.getGestureEntries();
ArrayList<String> list2 = new ArrayList<String>(setStr);

for (int i=0;i<list2.size();i++){
	al=gLib.getGestures(list2.get(i));
	Gesture gs=al.get(0);
//	Bitmap bitmap=gs.toBitmap(60, 60, 3, Color.YELLOW);
//	GetPicturefromFile.writeBMPtoFile1(bitmap, DIR, FILE+i+".jpg");
//	alLIST.add(al);
	alholder.add(new GestureHolder(gs, list2.get(i)));
}
	
	  
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.save_gesture);
//        gestures.setGestureColor(Color.GREEN);
        gestures.setGestureStrokeWidth(5.0f);
        gestures.setBackgroundColor(Color.BLUE);
        gestures.addOnGestureListener(mGestureListener);
        resetEverything();
    }

    /**
     * our gesture listener
     */
    private GestureOverlayView.OnGestureListener mGestureListener = new GestureOverlayView.OnGestureListener() {
        @Override
        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
            mGestureDrawn = true;
            Log.d(TAG, "andar");
        }

        @Override
        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
            mCurrentGesture = overlay.getGesture();
        }

        @Override
        public void onGestureEnded(GestureOverlayView gestureView, MotionEvent motion) {
            Log.d(TAG, "bahar");
        }

        @Override
        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
            Log.d(TAG, "cancel");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gesture_add_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.DELETE:
                reDrawGestureView();
                break;

            case R.id.Save:
                if(mGestureDrawn){
                   getName();
                } else{
                    showToast(getString(R.string.no_gesture));
                }

                //TODO : Save gesture as image, dont delete this code
                /*
                String pattern = "mm ss";
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                String time = formatter.format(new Date());
                String path = ("/d-codepages" + time + ".png");
                File file = new File(Environment.getExternalStorageDirectory()
                        + path);
                try {
                    //DrawBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    //new FileOutputStream(file));
                    Toast.makeText(this, "File Saved ::" + path, Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    Toast.makeText(this, "ERROR" + e.toString(), Toast.LENGTH_SHORT)
                            .show();
                }   */
        }
        return super.onOptionsItemSelected(item);
    }

    private void getName() {
        AlertDialog.Builder namePopup = new AlertDialog.Builder(this);
        namePopup.setTitle(getString(R.string.enter_name));
        //namePopup.setMessage(R.string.enter_name);


        final EditText nameField = new EditText(this);
        namePopup.setView(nameField);
        namePopup.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //db.updateExistingMeasurement(measurement);
                if (!nameField.getText().toString().matches("")) {
                    mGesturename = nameField.getText().toString();
                    saveGesture();
                } else {
                    getName();  //TODO : set name field with old name string user added
                    showToast(getString(R.string.invalid_name));
                }
                //return;
            }
        });
        namePopup.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGesturename = "";
                return;
            }
        });

        namePopup.show();

    }

    private void showToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    //private void checkforDuplicateName

    private void saveGesture() {
        // if(!mGesturename.matches("")) {
        //gLib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gesture.txt");
        //gLib.load();
        //TODO: check kar k same naam valu gesture che k nai
        gLib.addGesture(mGesturename, mCurrentGesture);
        if (!gLib.save()) {
            Log.e(TAG, "gesture not saved!");
        }else {
            showToast(getString(R.string.gesture_saved) +" \n "+ dataFile.toString());
        }
        reDrawGestureView();
        // }
    }
    private void resetEverything(){
        mGestureDrawn = false;
        mCurrentGesture = null;
        mGesturename = "";
    }

    private void reDrawGestureView() {
        setContentView(R.layout.gesture_add_activity_main);
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.save_gesture);
        gestures.removeAllOnGestureListeners();
        gestures.addOnGestureListener(mGestureListener);
        gestures.setGestureStrokeWidth(5.0f);
        gestures.setBackgroundColor(Color.BLUE);
        resetEverything();
    }
}
