package com.example.notes2;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CameraCapture1 extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mediaRecorder;
	private TextView tv1;
	private Button capture, switchCamera,stat1;
	private Context myContext = this;
	private RelativeLayout cameraPreview, rl1;
	private boolean cameraFront = false;
	private int width, height;
	public int DisplayOrientation = 90, OrientationHint = 90;
	public Drawable drrfront, drrback, redcircle, yelloecircle;
	private static final String MyPREFERENCES = "MyPrefs";
	private static final String INIT = "init";
	private static final String FRONT = "FRONT";
	private static final String HINTROTATE = "HINTROTATE";
	private static final String VIEWROTATE = "VIEWROTATE";
	private static final String RESOLUTION = "RESOLTION";
	private Handler count = new Handler();
	long ctime;
	int mins = 0;

	String dname = "";
	String fname = "";

	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;

	public dateFrom1970 df1970;
	String outputFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_capture1);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize();
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (width == 0 || height == 0) {

			float tsize = 0;
//			displ disp = new displ();
			double d = displ.getScreenInches();
			if (d < 5) tsize = getResources().getDimension(R.dimen.textsize1cam);
			if (d > 5 && d < 6) tsize = getResources().getDimension(R.dimen.textsize2cam);
			if (d > 6 && d < 7) tsize = getResources().getDimension(R.dimen.textsize3cam);
			if (d > 7 && d < 8) tsize = getResources().getDimension(R.dimen.textsize4cam);
			if (d > 8 && d < 9) tsize = getResources().getDimension(R.dimen.textsize5cam);
			if (d > 9 && d < 10) tsize = getResources().getDimension(R.dimen.textsize6cam);

			width = rl1.getMeasuredWidth();
			height = rl1.getMeasuredHeight();
			Toast.makeText(getBaseContext(), "onWindows = " + width + "  " + height, Toast.LENGTH_SHORT).show();

			ViewGroup.MarginLayoutParams ipet1 = (ViewGroup.MarginLayoutParams) cameraPreview.getLayoutParams();
			ipet1.width = (rl1.getWidth() * 45) / 60;
			ipet1.height = (rl1.getHeight() * 60) / 60;
			ipet1.setMargins(0,	0,0, 0);
			cameraPreview.requestLayout();

			ViewGroup.MarginLayoutParams ptv1 = (ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
			ptv1.width = (rl1.getWidth() * 15) / 60;
			ptv1.height = rl1.getHeight() / 9;
			ptv1.setMargins(rl1.getWidth() * 10 / 100, // left
					rl1.getHeight() / 80, // top
					0, 0);
			tv1.setTextSize(tsize);
			tv1.requestLayout();

			ViewGroup.MarginLayoutParams pstat1 = 
					(ViewGroup.MarginLayoutParams) stat1.getLayoutParams();
			pstat1.width=(rl1.getWidth()*15)/60;
			pstat1.height=rl1.getHeight()/6;
			pstat1.setMargins(0,0,0,rl1.getHeight()/80);
			stat1.setTextSize(8 * tsize / 10);
			stat1.requestLayout();  
			
			ViewGroup.MarginLayoutParams btn_switch = (ViewGroup.MarginLayoutParams) switchCamera.getLayoutParams();
			btn_switch.width = (rl1.getWidth() * 15) / 60;
			btn_switch.height = rl1.getHeight() / 6;

			btn_switch.setMargins(20,	20, 0, 0);
			switchCamera.requestLayout();
			switchCamera.setTextSize(8 * tsize / 10);

			ViewGroup.MarginLayoutParams btn_rec = (ViewGroup.MarginLayoutParams) capture.getLayoutParams();
			btn_rec.width = (rl1.getWidth() * 15) / 60;
			btn_rec.height = rl1.getHeight() / 6;

			btn_rec.setMargins(20,	20,0, 0);
			capture.requestLayout();
			capture.setTextSize(tsize);
		}
		String s[]=createResolutionString(false);
		stat1.setText(s[sharedpreferences.getInt(RESOLUTION, 0)]);

		super.onWindowFocusChanged(hasFocus);
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}

	public void onResume() {
		super.onResume();
		
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}		

		if (mCamera == null) {
			if (Camera.getNumberOfCameras()>=2 && sharedpreferences.getBoolean(FRONT, false)) {	
				mCamera = Camera.open(findFrontFacingCamera());
				mPreview.refreshCamera(mCamera);	
				switchCamera.setText("Switch Camera \n\r --Now Front");
				switchCamera.setCompoundDrawables(drrfront, null, null, null);			
			}
			else if (Camera.getNumberOfCameras()>=2 && !sharedpreferences.getBoolean(FRONT, false)){ 
				mCamera = Camera.open(findBackFacingCamera());
				mPreview.refreshCamera(mCamera);
				switchCamera.setText("Switch Camera \n\r --Now Back");
				switchCamera.setCompoundDrawables(drrback, null, null, null);	
							}
		}
	}

	public void initialize() {
		df1970 = new dateFrom1970();
		Bundle extras = getIntent().getExtras();
		dname = extras.getString("Value1");
		fname = extras.getString("Value4");
		outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dname + "/" + fname + "_"
				+ df1970.getmillisString() + ".mp4";

		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		if (sharedpreferences.getString(INIT, "").equals("")) {

			editor.putString(INIT, "INIT").commit();
			editor.putBoolean(FRONT, true).commit();
			editor.putInt(VIEWROTATE, 90).commit();
			editor.putInt(HINTROTATE, 90).commit();
			editor.putInt(RESOLUTION, 1).commit();
		}
		cameraFront = sharedpreferences.getBoolean(FRONT, false);
		DisplayOrientation = sharedpreferences.getInt(VIEWROTATE, 0);
		OrientationHint = sharedpreferences.getInt(HINTROTATE, 0);
		Toast.makeText(myContext, "front = " + String.valueOf(cameraFront), Toast.LENGTH_LONG).show();

		cameraPreview = (RelativeLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(myContext, mCamera, DisplayOrientation);
		cameraPreview.addView(mPreview);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		tv1 = (TextView) findViewById(R.id.tv1);
		stat1=(Button)findViewById(R.id.stat1);
		stat1.setOnClickListener(stat1Listener);
		
		capture = (Button) findViewById(R.id.button_capture);
		capture.setOnClickListener(captrureListener);
		switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
		switchCamera.setOnClickListener(switchCameraListener);
		capture.setText("Capture/Stop \n\r --Not Capturing");
		capture.setBackgroundColor(Color.GREEN);
		drrfront = (Drawable) getResources().getDrawable(R.drawable.camerafront);
		drrfront.setBounds(0, 0, 80, 80);
		drrback = (Drawable) getResources().getDrawable(R.drawable.cameraback);
		drrback.setBounds(0, 0, 80, 80);
		switchCamera.setCompoundDrawables(null, null, null, drrback);

		redcircle = (Drawable) getResources().getDrawable(R.drawable.redcircle);
		redcircle.setBounds(0, 0, 25, 25);
		yelloecircle = (Drawable) getResources().getDrawable(R.drawable.yellowcircle);
		yelloecircle.setBounds(0, 0, 25, 25);
		// tv1.setCompoundDrawables(redcircle, null, null, null);

		tv1.setBackgroundColor(Color.YELLOW);
		tv1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		stat1.setBackgroundColor(Color.CYAN);
		stat1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		updateStat1();
		
	}

	
	
	
	OnClickListener stat1Listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			Toast.makeText(myContext, "stat1",Toast.LENGTH_LONG).show();
			showAlertList();

		}
	};
	
	OnClickListener switchCameraListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// get the number of cameras
			if (!recording) {

				if (Camera.getNumberOfCameras()>=2) {
					// release the old camera instance
					// switch camera, from the front and the back and vice versa
	Toast.makeText(myContext, "resolutionvbbbb!! = "+sharedpreferences.getInt(RESOLUTION, 0),Toast.LENGTH_LONG).show();
				cameraFront=sharedpreferences.getBoolean(FRONT, false);
	        	editor.putBoolean(FRONT, !cameraFront).commit();  // invert
				cameraFront=sharedpreferences.getBoolean(FRONT, false);
	        	releaseCamera();
				releaseMediaRecorder1();

					if (cameraFront) {  // true is front camera
					switchCamera.setText("Switch Camera \n\r --Now Front");
					switchCamera.setCompoundDrawables(drrfront, null, null, null);
					mCamera = Camera.open(findFrontFacingCamera());
					mPreview.refreshCamera(mCamera);					
					}
					
					else {
//    	editor.putBoolean(FRONT, !cameraFront).commit();
		switchCamera.setText("Switch Camera \n\r --Now Back");
		switchCamera.setCompoundDrawables(drrback, null, null, null);
		mCamera = Camera.open(findBackFacingCamera());
		mPreview.refreshCamera(mCamera);
	}
				} else {
					Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
					toast.show();
				}
				
			}
		}
	};

	public void chooseCamera() {
		// if the camera preview is the front
		if (cameraFront) {
			if (findBackFacingCamera() >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview
				mCamera = Camera.open(findBackFacingCamera());
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		} else {
			if (findFrontFacingCamera() >= 0) {
				// open the frontFacingCamera
				// set a picture callback
				// refresh the preview
				mCamera = Camera.open(findFrontFacingCamera());
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		// when on Pause, release camera in order to be used from other
		// applications
		releaseCamera();
		releaseMediaRecorder1();
	}

	private boolean hasCamera(Context context) {
		// check if the device has camera
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	boolean recording = false;
	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (recording) {
				// stop recording and release camera
				mediaRecorder.stop(); // stop the recording
				releaseMediaRecorder(); // release the MediaRecorder object
				Toast.makeText(CameraCapture1.this, "Video captured!", Toast.LENGTH_LONG).show();
				recording = false;
				capture.setText("Capture/Stop \n\rNot Capturing");
				capture.setBackgroundColor(Color.GREEN);
				count.removeCallbacks(timeCount);
				tv1.setCompoundDrawables(yelloecircle, null, null, null);
				tv1.setText("00 : 00");

			} else {
				if (!prepareMediaRecorder()) {
					Toast.makeText(CameraCapture1.this, "Fail in prepareMediaRecorder()!\n - Ended -",
							Toast.LENGTH_LONG).show();
					finish();
				}
				mins = 0;
				ctime = System.currentTimeMillis();
				count.post(timeCount);
				capture.setText("Capture/Stop \n\rCapturing");
				capture.setBackgroundColor(Color.RED);

				// work on UiThread for better performance
				runOnUiThread(new Runnable() {
					public void run() {
						// If there are stories, add them to the table

						try {
							mediaRecorder.start();
						} catch (final Exception ex) {
							// Log.i("---","Exception in thread");
						}
					}
				});

				recording = true;
			}
		}
	};

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private boolean prepareMediaRecorder() {
		Toast.makeText(CameraCapture1.this, "prep", Toast.LENGTH_LONG).show();

		mediaRecorder = new MediaRecorder();
		// mCamera.setDisplayOrientation(DisplayOrientation); // in preview
		// class use for set the orientation of the preview
		mediaRecorder.setOrientationHint(OrientationHint);
		mCamera.unlock();
		mediaRecorder.setCamera(mCamera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		
		try {
			int r1=sharedpreferences.getInt(RESOLUTION, 0);
			if (r1==1) mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
			else if (r1==2) mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
			else if (r1==3) mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		} catch (Exception e1) {
			mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
			Toast.makeText(CameraCapture1.this, "got exeption...", Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		}	

		// mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));

		// String s1="/sdcard/myvideo"+String.valueOf(fileCounter++);
		// mediaRecorder.setOutputFile(s1+".mp4");
		outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dname + "/" + 
		fname + "_"+
		getCameraFacingResolution()+"_"+
				 df1970.getmillisString() + ".mp4";

		mediaRecorder.setOutputFile(outputFile);

		mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
		mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M
		Toast.makeText(CameraCapture1.this, "reskk " + sharedpreferences.getInt(RESOLUTION, 0), Toast.LENGTH_LONG)
				.show();

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	private String[] createResolutionString(boolean b){
		String[] s=new String[4];
		s[0]="resolution no change";
		s[1]="Lowest resolution";
		s[2]= "Middle resolution";
		s[3]= "Highest resolution";
		int i1=sharedpreferences.getInt(RESOLUTION, 0);
		if (i1>s.length-1){
			i1=1;
			editor.putInt(RESOLUTION, i1).commit();
					}
		if (b)	{
			if (i1!=0) {
				String x="* "+s[i1];
				s[i1]=x;
					}	
			}
		return s;
	}

	private void updateStat1(){
		String s[]=createResolutionString(false);
		int x=sharedpreferences.getInt(RESOLUTION, 0);
		if (x>s.length-1){ x=1;
		editor.putInt(RESOLUTION, x).commit();		
		}
		stat1.setText(s[x]);
	}
	
	
	private void showAlertList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
		builder.setTitle("Choose resolution");
		String[] s=createResolutionString(true);
		updateStat1();
		builder.setItems(s, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which!=0) {
				editor.putInt(RESOLUTION, which).commit();
				String s[]=createResolutionString(false);
				stat1.setText(s[which]);
					}
				}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_camera_capture1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
        	showAlertList();
 //       	releaseCamera();
 //       	releaseMediaRecorder();
//        	recreate();
        	return true;
		}

		if (id == R.id.front) {
			// cameraFront=!cameraFront;
			// editor.putBoolean(FRONT, cameraFront).commit();
			// if (cameraFront)
			Toast.makeText(this, "Do nothing ", Toast.LENGTH_LONG).show();
			// else Toast.makeText(this, "camera back
			// ",Toast.LENGTH_LONG).show();
			return true;
		}

		if (id == R.id.hintdeg) { // DisplayOrientation=90,OrientationHint=90;
        	OrientationHint+=90;
        	if (OrientationHint>=360) OrientationHint=0;
        	editor.putInt(HINTROTATE, OrientationHint).commit();
       	Toast.makeText(this, "hint rotate "+String.valueOf(OrientationHint),Toast.LENGTH_LONG).show();
       	releaseCamera();
       	releaseMediaRecorder();
       	recreate();
       	return true;
		}

		if (id == R.id.viewdeg) {
        	DisplayOrientation+=90;
        	if (DisplayOrientation>=360) DisplayOrientation=0;
        	editor.putInt(VIEWROTATE, DisplayOrientation).commit();
           	Toast.makeText(this, "disp orient rotate "+String.valueOf(DisplayOrientation),Toast.LENGTH_LONG).show();
           	releaseCamera();
           	releaseMediaRecorder1();
           	recreate();
           	return true;
		}

		if (id == R.id.info) {

			Toast.makeText(this, "res MM   " + String.valueOf(sharedpreferences.getInt(RESOLUTION, 0)),
					Toast.LENGTH_LONG).show();

			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {	
		if (recording){
		mediaRecorder.stop(); // stop the recording
		releaseMediaRecorder(); // release the MediaRecorder object
		releaseMediaRecorder1();
		count.removeCallbacks(timeCount);
		}
       	releaseCamera();
       	releaseMediaRecorder1();
       	finish();
		super.onBackPressed();
	}

	private Runnable timeCount = new Runnable() {
		public void run() {

			int secs = (int) (((-ctime + System.currentTimeMillis()) / 1000)) % 60;
			// tv1.setCompoundDrawables(redcircle, null, null, null);
			// else tv1.setCompoundDrawables(yelloecircle, null, null, null);

			if (secs == 59)
				mins++;
			String m = String.valueOf(mins);
			if (m.length() == 1)
				m = "0" + m;
			String s = String.valueOf(secs);
			if (s.length() == 1)
				s = "0" + s;
			tv1.setText(m + " : " + s);
			if (secs % 2 == 0)
				tv1.setCompoundDrawables(redcircle, null, null, null);
			if (secs % 2 != 0)
				tv1.setCompoundDrawables(yelloecircle, null, null, null);
			count.postDelayed(this, 1000);
		}
	};

private List<Size> getCameraSupportedResolutionList(){
	List<Size> mSize = null;
	Camera.Parameters params=null;
	if (mCamera!=null){
	params = mCamera.getParameters();
	mSize=params.getSupportedVideoSizes();
	int aaa=mSize.size();
	int aa3333=aaa+1;
	}	
//		Camera.Parameters params1 = mCamera.getParameters();
//	params.setPictureSize(mSize.get(0).width, mSize.get(0).height);
//	mCamera.setParameters(params); 
//	Size vSize=params.getPictureSize();
//	int a88=vSize.height;
//	int a89=vSize.width;
//	int a90=a88+a89;
	return mSize;
}

private Size getCameraSupportedResolution(){
	Size vSize=null;
	Camera.Parameters params=null;
	if (mCamera!=null){
		params = mCamera.getParameters();
//	mCamera.setParameters(params); 
	vSize=params.getPictureSize();
	}
	return vSize;
//		Camera.Parameters params1 = mCamera.getParameters();
//	params.setPictureSize(mSize.get(0).width, mSize.get(0).height);

}
	
private String getCameraFacingResolution (){
	String s=null;
	if (sharedpreferences.getBoolean(FRONT, false)){
		s="F"+String.valueOf(sharedpreferences.getInt(RESOLUTION, 0));
	}
	else s="B"+String.valueOf(sharedpreferences.getInt(RESOLUTION, 0));
	return s;
	
	
	
}

private void releaseMediaRecorder1() {

if (mediaRecorder != null) {
	mediaRecorder.reset(); // clear recorder configuration
	mediaRecorder.release(); // release the recorder object
	mediaRecorder = null;
//	mCamera.lock(); // lock camera for later use
			}
		}

}

/*
 * 
 * 
 CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);                    
mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);                        
mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);                        
mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);                    
mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);                    
mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);                    
mMediaRecorder.setVideoEncodingBitRate(cpHigh.videoBitRate);                    
mMediaRecorder.setVideoFrameRate(cpHigh.videoFrameRate);
int rotation = mWindowManager.getDefaultDisplay().getRotation();
int orientation = ORIENTATIONS.get(rotation + 90);
mMediaRecorder.setOrientationHint(orientation);
Use below code to get the DISPLAY_HEIGHT,DISPLAY_WIDTH

DisplayMetrics metrics = new DisplayMetrics();      
mWindowManager.getDefaultDisplay().getMetrics(metrics);          
DISPLAY_WIDTH = metrics.widthPixels;
DISPLAY_HEIGHT = metrics.heightPixels;
Define ORIENTATIONS as shown below

public static final SparseIntArray ORIENTATIONS = new SparseIntArray();
static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
}
 
 
 
 
 
 
 */


