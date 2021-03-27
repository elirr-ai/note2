package com.example.notes2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class GestureListViewMainActivity extends Activity  implements OnClickListener{

    private static final String TAG = "GestureListActivity";
    private static final int REQCODEADDGESTURE = 7;
    
    private String mCurrentGestureNaam,navuNaam;
    private ListView mGestureListView;
    private Button btLeft,btRight;
    private static ArrayList<GestureHolder> mGestureList;
    private GestureAdapter mGestureAdapter;
    private GestureLibrary gLib;
    
	private final static String DIR="GestureLib";
	private final static String FILE="gestureaa.txt";

	private String messageB="";
	private String gestureDir;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestures_list);
        Log.d(TAG, getApplicationInfo().dataDir);

        gestureDir = getIntent().getStringExtra("DIR1");
        //openOptionsMenu();

        mGestureListView = (ListView) findViewById((R.id.gestures_list));
        btLeft=(Button)findViewById(R.id.btleftAA);
        btRight=(Button)findViewById(R.id.btrightAA);
        btLeft.setOnClickListener(this);        
        btRight.setOnClickListener(this);  
        
        
        mGestureList = new ArrayList<GestureHolder>();
        listBanav();
        mGestureAdapter = new GestureAdapter(mGestureList, GestureListViewMainActivity.this);
        mGestureListView.setLongClickable(true);
        mGestureListView.setAdapter(mGestureAdapter);

        // displays the popup context top_menu to either delete or resend measurement
        registerForContextMenu(mGestureListView);
        
    }

    
    
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
    	double x,y;
    	Display disp=new Display();
    	x=disp.x;y=disp.y;
    	ViewGroup.MarginLayoutParams ipet1;
    	
		ipet1 = (ViewGroup.MarginLayoutParams) mGestureListView.getLayoutParams();
				ipet1.width=(int) x;
				ipet1.height=(int) ((y*85)/100);
				ipet1.setMargins(0,	0, 	0,0);
				mGestureListView.requestLayout();
				
				ipet1 = (ViewGroup.MarginLayoutParams) btLeft.getLayoutParams();
				ipet1.width=(int) x/3;
				ipet1.height=(int) ((y*6)/100);
				ipet1.setMargins(0,	0, 	0,0);
				btLeft.requestLayout();
				
				ipet1 = (ViewGroup.MarginLayoutParams) btRight.getLayoutParams();
				ipet1.width=(int) x/3;
				ipet1.height=(int) ((y*6)/100);
				ipet1.setMargins(0,	0, 	0,0);
				btRight.requestLayout();
				
				
				
				
		super.onWindowFocusChanged(hasFocus);
	}



	@Override
	public void onBackPressed() {
	       Intent intent = new Intent();
	        intent.putExtra("MESSAGE", messageB);
	        intent.putExtra("STATUS", "OK");
	        setResult(RESULT_OK, intent);
	        finish();
		super.onBackPressed();
	}



	@Override
    public void onResume(){
                   super.onResume();
        setContentView(R.layout.gestures_list);
        mGestureListView = (ListView) findViewById((R.id.gestures_list));
        btLeft=(Button)findViewById(R.id.btleftAA);
        btRight=(Button)findViewById(R.id.btrightAA);
        btLeft.setOnClickListener(this);        
        btRight.setOnClickListener(this);  
        mGestureList = new ArrayList<GestureHolder>();
        listBanav();
        mGestureAdapter = new GestureAdapter(mGestureList, GestureListViewMainActivity.this);
        mGestureListView.setLongClickable(true);
        mGestureListView.setAdapter(mGestureAdapter);
        registerForContextMenu(mGestureListView);
    }

    /**
     * badha gestures laine emne list ma mukse
     */
    private void listBanav() {
        try {
            mGestureList = new ArrayList<GestureHolder>();
            
    		File rootPath = new File(Environment.getExternalStorageDirectory(), gestureDir);
    	    if(!rootPath.exists()) {
    	        rootPath.mkdirs();
    	    }
    	    File dataFile = new File(rootPath, FILE);
    	    gLib = GestureLibraries.fromFile(dataFile);
    	    boolean bload=gLib.load();   		
    		if (!bload) {   			
    		       Intent intent = new Intent();
    		       messageB="";
    		       intent.putExtra("MESSAGE", messageB);
    		       intent.putExtra("STATUS", "NOTOK");
    		       setResult(RESULT_CANCELED, intent);
    		       Toast.makeText(getApplicationContext(), "Gesture file not loaded", Toast.LENGTH_SHORT).show(); 
    		       finish();
    			}
    		
    		else { 
//            gLib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gesture.txt");
//            gLib.load();
            Set<String> gestureSet = gLib.getGestureEntries();
            for(String gestureNaam: gestureSet){
                ArrayList<Gesture> list = gLib.getGestures(gestureNaam);
                for(Gesture g : list) {
                    mGestureList.add(new GestureHolder(g, gestureNaam));
                	}
            	}
    		}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void populateMenu(View view){
        //ImageView idView = (ImageView) view.findViewById(R.id.gesture_id);
        //Log.d(TAG, "ha ha" + idView.getText().toString());
        LinearLayout vwParentRow = (LinearLayout)view.getParent().getParent();
        TextView tv = (TextView)vwParentRow.findViewById(R.id.gesture_name_ref);
        mCurrentGestureNaam = tv.getText().toString();
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.gesture_item_options, popup.getMenu());
        popup.show();
    }

    public void addButtonClick(){  // add new gesture
        Intent addGesture1 = new Intent(GestureListViewMainActivity.this, GestureAddSimple1MainActivity.class);
        addGesture1.putExtra("DIR1", gestureDir);  //directory name
        startActivity(addGesture1);
    }

    public void testButtonClick(){
        Intent addGesture1 = new Intent(GestureListViewMainActivity.this, GestureDetectSimpleMainActivity.class);
        addGesture1.putExtra("DIR1", gestureDir);  //directory name
        startActivityForResult(addGesture1, REQCODEADDGESTURE);
    }

    
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQCODEADDGESTURE)  {

             messageB=data.getStringExtra("MESSAGE");
             String flag1=data.getStringExtra("STATUS");
             
             if (flag1.equals("OK") ) {
             Toast.makeText(getApplicationContext(), "CAP:  "+messageB, Toast.LENGTH_SHORT).show(); 
             	}
             else Toast.makeText(getApplicationContext(), "Message not captured!", Toast.LENGTH_SHORT).show();  
             
         }
    	
    	super.onActivityResult(requestCode, resultCode, data);
	}

	public void deleteButtonClick(MenuItem item){
        gLib.removeEntry(mCurrentGestureNaam);
        gLib.save();
        mCurrentGestureNaam = "";
        onResume();
    }

    public void renameButtonClick(MenuItem item){

        AlertDialog.Builder namePopup = new AlertDialog.Builder(this);
        namePopup.setTitle(getString(R.string.enter_new_name));
        //namePopup.setMessage(R.string.enter_name);

        final EditText nameField = new EditText(this);
        namePopup.setView(nameField);
        nameField.setText(mCurrentGestureNaam);
        namePopup.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!nameField.getText().toString().matches("")) {
                    navuNaam = nameField.getText().toString();
                    saveGesture();
                } else {
                    renameButtonClick(null);  //TODO : validation
                    showToast(getString(R.string.invalid_name));
                }
            }
        });
        namePopup.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navuNaam = "";
                mCurrentGestureNaam = "";
                return;
            }
        });

        namePopup.show();
    }

    private void saveGesture() {
        ArrayList<Gesture> list = gLib.getGestures(mCurrentGestureNaam);
        if (list.size() > 0) {
            gLib.removeEntry(mCurrentGestureNaam);
            gLib.addGesture(navuNaam, list.get(0));
            if (gLib.save()) {
                onResume();
            }
        }
        navuNaam = "";
    }
    private void showToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.btleftAA){
 //       Toast.makeText(this, "vvvvvvvv", Toast.LENGTH_SHORT).show();
		addButtonClick();}
		else	if (v.getId()==R.id.btrightAA){
			testButtonClick();
//	        Toast.makeText(this, "yyyyyyyyyyyyyyyyyyyyyyyyyy", Toast.LENGTH_SHORT).show();
		}
	}

	private static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    try {
	        OutputStream out = new FileOutputStream(dst);
	        try {
	            // Transfer bytes from in to out
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	        } finally {
	            out.close();
	        }
	    } finally {
	        in.close();
	    }
	}
	

	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.gesture_listview_main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {

	            case R.id.Save:
	            	dateFrom1970 dt1=new dateFrom1970();
	            	String dt01= dt1.toDateStr();
	        		File rootPath = new File(Environment.getExternalStorageDirectory(), gestureDir);
	        	    if(!rootPath.exists()) {
	        	        rootPath.mkdirs();
	        	    }
	        	    File dataFile = new File(rootPath, FILE);
	        	    File destFile = new File(rootPath, "gestureaa_"+dt01+".txt");
				try {
					copy(dataFile, destFile);
	                Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();;			
				} catch (IOException e) {
                    Toast.makeText(this, "ERROR: File not saved   (" + e.toString()+")", Toast.LENGTH_SHORT).show();;
					e.printStackTrace();
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
	
	
	
	
}
