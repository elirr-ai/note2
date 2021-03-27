package com.example.notes2;


import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class flashlight extends Activity {

    private Camera camera;
    private Parameters params;
    Button fastblink, slowblink,constantlight,offlight,bb1;
    private boolean isFlashlightBlinking=false;
    private boolean isCameraFlash;
    
    private Handler customHandler = new Handler();
    int lightrate=0;
    
    private  SoundPool mSoundPool;
	private  AudioManager  mAudioManager;
	private  HashMap<Integer, Integer> mSoundPoolMap;
	private  int mStream1 = 0;
	private  int mStream2 = 0;
	float streamVolume =0;
	final static int SOUND_FX_01 = 1;
	final static int SOUND_FX_02 = 2;
	final static int LOOP_4_EVER = -1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashlight);
        
        setSoundPool();
        // flashlight on off Image
        constantlight = (Button) findViewById(R.id.constantlight);
        offlight = (Button) findViewById(R.id.offlight);
        fastblink = (Button) findViewById(R.id.fastblink);
        slowblink = (Button) findViewById(R.id.slowblink);
        
        bb1 = (Button) findViewById(R.id.bb);
        bb1.setBackgroundColor(Color.parseColor("#42a5f5"));
        
         streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    	streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);      
        
        isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

        if (!isCameraFlash) {
            showNoCameraAlert();
        } else {
            getCamera();
//            camera = Camera.open();
//            params = camera.getParameters();
        }

        
    
        
    constantlight.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
          customHandler.removeCallbacks(updateTimerThread);
          Set_Flash_on();
          mSoundPool.stop(mStream1);
          mSoundPool.stop(mStream2);
        }
    });
    
    offlight.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	   	
        	customHandler.removeCallbacks(updateTimerThread);
            Set_Flash_off(); 
            mSoundPool.stop(mStream1);
    		mSoundPool.stop(mStream2);
          }
    });
    
    fastblink.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	lightrate=200;      	
    		mSoundPool.stop(mStream1);   		
    		// line below is original
    		//mStream1= mSoundPool.play(mSoundPoolMap.get(SOUND_FX_01), streamVolume, streamVolume, 1, LOOP_1_TIME, 1f);
    		mStream1= mSoundPool.play(mSoundPoolMap.get(SOUND_FX_01), streamVolume, streamVolume, 1, LOOP_4_EVER, 1f);        	
        	//mSoundPool.play(mStream1, streamVolume, streamVolume, 1, -1, 1f);
          customHandler.postDelayed(updateTimerThread, 0);
        }
    });
    
    slowblink.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	lightrate=900;
        	mSoundPool.stop(mStream2);
    		//mStream2= mSoundPool.play(mSoundPoolMap.get(SOUND_FX_02), streamVolume, streamVolume, 1, LOOP_3_TIMES, 1f);
    		mStream2= mSoundPool.play(mSoundPoolMap.get(SOUND_FX_02), streamVolume, streamVolume, 1, LOOP_4_EVER, 1f);
          customHandler.postDelayed(updateTimerThread, 0);
        }
    });
    
    bb1.setOnTouchListener(new OnTouchListener() {
		 
        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                Log.i("repeatBtn", "MotionEvent.ACTION_DOWN");                
                bb1.setBackgroundColor(Color.parseColor("#ffeb3b"));
                Set_Flash_on(); 
                
            } else if (action == MotionEvent.ACTION_UP) {
                Log.i("repeatBtn", "MotionEvent.ACTION_UP");
                 bb1.setBackgroundColor(Color.parseColor("#42a5f5"));
                Set_Flash_off(); 
            	}//end else
            	return false;
        	} //end onTouch
    	}); //end b my button    
    }  // on create
    
    private void setSoundPool() {    	
    	if (mSoundPool==null){
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
     	//mStream1 = mSoundPool.load(this, R.raw.set_trap, 1);
     	//mStream2 = mSoundPool.load(this, R.raw.spring_trap, 1);
        mSoundPoolMap.put(SOUND_FX_01, mSoundPool.load(this, R.raw.set_trap, 1));
        mSoundPoolMap.put(SOUND_FX_02, mSoundPool.load(this, R.raw.spring_trap, 1));
    	}
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSoundPool!=null){
        mSoundPool.release();
        mSoundPool = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Set_Flash_off();
        if (mSoundPool!=null){
        mSoundPool.release();
        mSoundPool = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isCameraFlash){
            Set_Flash_off();
           setSoundPool();
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
            if (mSoundPool!=null){
                mSoundPool.release();
                mSoundPool = null;
                }
        }
    }

    private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No Camera Flash!")
                .setMessage("Camera flashlight not available in this Android device!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // close the Android app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;
    }
    
    
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
	if (isFlashlightBlinking)		{
		
		Set_Flash_on();  isFlashlightBlinking = false;				
					try {
						Thread.sleep(lightrate/3);
						} catch (InterruptedException e) {
								e.printStackTrace();
						}	
									}
	if (!isFlashlightBlinking) {
		Set_Flash_off();     isFlashlightBlinking = true;
    try {
		Thread.sleep(lightrate/3);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
        }
           customHandler.postDelayed(this, 400);
        }
		
    };
    
  

    private void Set_Flash_off() {
     if(isCameraFlash ) {
    	if(camera == null || params == null) {
            return;
        }
        params = camera.getParameters();
    	params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();	
    	}
	}
	
    private void Set_Flash_on() {
        if(isCameraFlash) {
            if(camera == null || params == null) {
                return;
            }
    	params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();
	}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flashlight_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.exit:
			customHandler.removeCallbacks(updateTimerThread);
	        Set_Flash_off(); 
	        if (camera != null) {
	            camera.release();
	            camera = null;
	        }
	        if (mSoundPool!=null){
	        	mSoundPool.stop(mStream1);
	        	mSoundPool.stop(mStream2);
	            mSoundPool.release();
	            mSoundPool = null;
	            }
			flashlight.this.finish();
			
			
//Intent i = new Intent(getApplicationContext(), MainActivity.class);
//	startActivity(i);
			//flashlight.this.finish(); finish();
		//Toast.makeText(getApplicationContext(), "2 Veresion 1.0 - Copyright Eli Rajchert", Toast.LENGTH_SHORT).show(); 
			break;
		
					
		}
		return super.onOptionsItemSelected(item);
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
		customHandler.removeCallbacks(updateTimerThread);
        Set_Flash_off(); 
        if (camera != null) {
            camera.release();
            camera = null;
        }        
        if (mSoundPool!=null){
        	mSoundPool.stop(mStream1);
        	mSoundPool.stop(mStream2);
            mSoundPool.release();
            mSoundPool = null;
            }
		flashlight.this.finish();
			}
	
	   private void getCamera() {
	        if (camera == null) {
	            try {
	                camera = Camera.open();
	                params = camera.getParameters();
	            }catch (Exception e) {
	            }
	        }
	    }
	
}
