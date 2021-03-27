package com.example.notes2;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import jcifs.smb.SmbFile;



//public class setup1 extends Activity {
public class setup1 extends Activity implements OnClickListener, SambaShareTaskInformer  {
	
	private Context context=this;
	
	public static final String MyPREFERENCES = "MyPrefs" ;  // my pref internal folder name
	private EditText email1,email2,email3,sms1,sms2,sms3,nd1,nb1,nr1,cameraangelET;
	private EditText remoteIP,remoteFolder,remoteUser,remotePassword;
	private CheckBox chkemail1,chkemail2,chkemail3,Splash,textsize11,smsck1,smsck2,smsck3;
	private TextView textsizeDI;
	private Button pingButton;
	
	
	SharedPreferences sharedpreferences;
	 // keys
    public static final String Initialized = "Initialized";
    public static final String MainDirName = "MainDirName";
    public static final String BackupDirName = "BackupDirName";
    public static final String DeletedDirName = "DeletedDirName";
    public static final String MailAddress = "MailAddress";
    public static final String SMSnumber = "SMSnumber";
    public static final String SoundMusic = "SoundMusic";
    public static final String TextSize = "TextSize";
    public static final String ROTATECAMERAANGEL = "ROTATECAMERAANGEL";

    public static final String REMOTEIP = "REMOTEIP";
    public static final String REMOTEFOLDER = "REMOTEFOLDER";
    public static final String REMOTEUSERNAME = "REMOTEUSERNAME";
    public static final String REMOTEUSERPASSWORD = "REMOTEUSERPASSWORD";

    final static int PING_COMMAND=98;
    
    // 2nd key variable
    String mainDir_ ="";
    String backupDir_ ="";
    String deletedDir_ ="";
    String mailaddr_ ="";
    String smsNumer_ ="";
    String playSplash_ ="";
    String TextSize_ ="";
	boolean isSoundOn=false;
      
    String[] Allmails =new String[10];
    String[] Allsms =new String[10];
    String MailtoSave="";
    displ di;
    double width=0,height=0;
    
    private String remoteIPx,remoteFolderx,remoteUserNamex,remoteUserPassWordx;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		di=new displ();
		double dbl=di.getScreenInches();
		setContentView(R.layout.setup2);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		email1=(EditText)findViewById(R.id.email1);
		email2=(EditText)findViewById(R.id.email2);
		email3=(EditText)findViewById(R.id.email3);
		sms1=(EditText)findViewById(R.id.sms1);
		sms2=(EditText)findViewById(R.id.sms2);
		sms3=(EditText)findViewById(R.id.sms3);
		nd1=(EditText)findViewById(R.id.nd1);
		nb1=(EditText)findViewById(R.id.nb1);
		nr1=(EditText)findViewById(R.id.nr1);
		cameraangelET=(EditText)findViewById(R.id.cameraangelET);
		//Splash=(ToggleButton)findViewById(R.id.Splash);
		//textsize11=(ToggleButton)findViewById(R.id.textsize11);
		chkemail1 = (CheckBox) findViewById(R.id.chkemail1);
		chkemail2 = (CheckBox) findViewById(R.id.chkemail2);
		chkemail3 = (CheckBox) findViewById(R.id.chkemail3);
		
		smsck1 = (CheckBox) findViewById(R.id.smsck1);
		smsck2 = (CheckBox) findViewById(R.id.smsck2);
		smsck3 = (CheckBox) findViewById(R.id.smsck3);
		
		Splash=(CheckBox)findViewById(R.id.Splash);
		textsize11=(CheckBox)findViewById(R.id.textsize11);
		
		textsizeDI=(TextView)findViewById(R.id.textsizeDI);

		remoteIP=(EditText)findViewById(R.id.remoteip);
		remoteFolder=(EditText)findViewById(R.id.remotename);
		remoteUser=(EditText)findViewById(R.id.remoteusername);
		remotePassword=(EditText)findViewById(R.id.remoteuserpass);
		pingButton=(Button)findViewById(R.id.pingbutton);
		pingButton.setBackgroundColor(Color.GREEN);
		
		
		//textsizeDI.setTextSize(28.0f);
		textsizeDI.setText("Screen size is: "+String.valueOf(dbl));
		chkemail1.setOnClickListener(this);
		chkemail2.setOnClickListener(this);
		chkemail3.setOnClickListener(this);
		smsck1.setOnClickListener(this);
		smsck2.setOnClickListener(this);
		smsck3.setOnClickListener(this);
		
		Splash.setOnClickListener(this);
		textsize11.setOnClickListener(this);
		pingButton.setOnClickListener(this);
		
		//ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.Splash);
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		String data = sharedpreferences.getString("Initialized", "");
		if (data.equalsIgnoreCase("")) {
			//Toast.makeText(getApplicationContext(), "String not found!!!!", Toast.LENGTH_SHORT).show();
		init_all_prefs();		
		}
//		else { 
			//Toast.makeText(getApplicationContext(), "String was found!!!!  !!! !!!!", Toast.LENGTH_SHORT).show();
//		}
		get_all_prefs();
		
		parse_mail_list(mailaddr_);
		parse_sms_list(smsNumer_);
		
		show_all_prefs();
		
}

	private void get_all_prefs() {
		mainDir_ = sharedpreferences.getString(MainDirName, "");
		 backupDir_ = sharedpreferences.getString(BackupDirName, "");
		 deletedDir_ = sharedpreferences.getString(DeletedDirName, "");
		 mailaddr_ = sharedpreferences.getString(MailAddress, "");
		 smsNumer_ = sharedpreferences.getString(SMSnumber, "");
		 playSplash_ = sharedpreferences.getString(SoundMusic, "");
		 TextSize_ = sharedpreferences.getString(TextSize, "");
		 
		 remoteIPx=  sharedpreferences.getString(REMOTEIP, "");
		 remoteFolderx=  sharedpreferences.getString(REMOTEFOLDER, "");
		 remoteUserNamex=  sharedpreferences.getString(REMOTEUSERNAME, "");
		 remoteUserPassWordx=  sharedpreferences.getString(REMOTEUSERPASSWORD, "");
		 
		 
		 
//Toast.makeText(getApplicationContext(), "CameraAngel1= "+CameraAngel1, Toast.LENGTH_SHORT).show();
		
	}

	
	private void show_all_prefs() {

		cameraangelET.setText(sharedpreferences.getString(ROTATECAMERAANGEL, ""));
		
		nd1.setText(mainDir_);
		nb1.setText(backupDir_);
		nr1.setText(deletedDir_);
		
		email1.setText(Allmails[0]);
		email2.setText(Allmails[1]);
		email3.setText(Allmails[2]);
		
		sms1.setText(Allsms[0]);
		sms2.setText(Allsms[1]);
		sms3.setText(Allsms[2]);
		
		
		if (Integer.parseInt(Allmails[3])==100){
			chkemail1.setChecked(false);
		}
		if (Integer.parseInt(Allmails[3])==101){
			chkemail1.setChecked(true);
		}
		if (Integer.parseInt(Allmails[4])==102){
			chkemail2.setChecked(false);
		}
		if (Integer.parseInt(Allmails[4])==103){
			chkemail2.setChecked(true);
		}
		if (Integer.parseInt(Allmails[5])==104){
			chkemail3.setChecked(false);
		}
		if (Integer.parseInt(Allmails[5])==105){
			chkemail3.setChecked(true);
		}
		
		if (Integer.parseInt(Allsms[3])==100){
			smsck1.setChecked(false);
		}
		if (Integer.parseInt(Allsms[3])==101){
			smsck1.setChecked(true);
		}
		if (Integer.parseInt(Allsms[4])==102){
			smsck2.setChecked(false);
		}
		if (Integer.parseInt(Allsms[4])==103){
			smsck2.setChecked(true);
		}
		if (Integer.parseInt(Allsms[5])==104){
			smsck3.setChecked(false);
		}
		if (Integer.parseInt(Allsms[5])==105){
			smsck3.setChecked(true);
		}
				
			
		if (!playSplash_.equalsIgnoreCase("SoundMusic_on")) Splash.setChecked(false);
		if (playSplash_.equalsIgnoreCase("SoundMusic_on")) Splash.setChecked(true);
		if (TextSize_.equalsIgnoreCase("20")) textsize11.setChecked(false);
		if (TextSize_.equalsIgnoreCase("30")) textsize11.setChecked(true);

		remoteIP.setText(remoteIPx);
		remoteFolder.setText(remoteFolderx);
		remoteUser.setText(remoteUserNamex);
		remotePassword.setText(remoteUserPassWordx);

//remoteIPx=remoteIP.getText().toString();
//remoteFolderx=remoteFolder.getText().toString();
//remoteUserNamex=remoteUser.getText().toString();
//remoteUserPassWordx=remotePassword.getText().toString();

	}
	
	



	private void save_all_prefs() {
		Allmails[0]=email1.getText().toString();
		Allmails[1]=email2.getText().toString();
		Allmails[2]=email3.getText().toString();
		
		Allsms[0]=sms1.getText().toString();
		Allsms[1]=sms2.getText().toString();
		Allsms[2]=sms3.getText().toString();
		
		
		add_prefs_key(Initialized, Initialized);
		
		add_prefs_key(MainDirName, nd1.getText().toString());	
		add_prefs_key(BackupDirName, nb1.getText().toString());	
		add_prefs_key(DeletedDirName, nr1.getText().toString());	
				
		add_prefs_key(MailAddress,
				Allmails[0]+","+Allmails[1]+","+Allmails[2]+","+
				Allmails[3]+","+Allmails[4]+","+Allmails[5] );
		
		add_prefs_key(SMSnumber,
				Allsms[0]+","+Allsms[1]+","+Allsms[2]+","+
						Allsms[3]+","+Allsms[4]+","+Allsms[5] );		
			
		add_prefs_key(SoundMusic, playSplash_);	
		add_prefs_key(TextSize,TextSize_);
		add_prefs_key(ROTATECAMERAANGEL, cameraangelET.getText().toString()); 
		//Toast.makeText(getApplicationContext(), TextSize_+"music xxxxxxxxxxx  ", Toast.LENGTH_SHORT).show();

//		remoteIP.setText(remoteIPx);
//		remoteFolder.setText(remoteFolderx);
//		remoteUser.setText(remoteUserNamex);
//		remotePassword.setText(remoteUserPassWordx);
		add_prefs_key(REMOTEIP, remoteIP.getText().toString());	
		add_prefs_key(REMOTEFOLDER, remoteFolder.getText().toString());	
		add_prefs_key(REMOTEUSERNAME, remoteUser.getText().toString());	
		add_prefs_key(REMOTEUSERPASSWORD, remotePassword.getText().toString());	
	
	}
	
	
	
	private void add_prefs_key(String string, String string2) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putString(string, string2);
		editor.apply();
	}
	
	private void init_all_prefs() {
		add_prefs_key(Initialized, Initialized);
		add_prefs_key(MainDirName, "DNAME");	
		add_prefs_key(BackupDirName, "DNAMEBACKUP");	
		add_prefs_key(DeletedDirName, "DNAMEDELETE");	
		add_prefs_key(MailAddress, "noName@gmail.com,noName@gmail.com,noName@gmail.com,100,102,104");	
		add_prefs_key(SMSnumber, "0000000000,0000000000,0000000000,100,102,104");	
		add_prefs_key(SoundMusic, playSplash_);
		add_prefs_key(TextSize, TextSize_);
		add_prefs_key(ROTATECAMERAANGEL, "0");
	}
	
	private void parse_mail_list(String mailaddr_2) {
		
		String[] a=mailaddr_2.split(",");
		for (int i=0;i<a.length;i++){
			Allmails[i]=a[i];	
				}
			}
	
	private void parse_sms_list(String sms_2) {
	
		String[] h=sms_2.split(",");
		for (int i=0;i<h.length;i++){
			Allsms[i]=h[i];	
				}
			}
	
	protected void parse_mail_to_perf(int i) {
		Allmails[0]=email1.getText().toString();
		Allmails[1]=email2.getText().toString();
		Allmails[2]=email3.getText().toString();
				
		if (i==100  || i==101) {
			Allmails[3]=Integer.toString(i);
		}
		
		if (i==102 || i==103) {
			Allmails[4]=Integer.toString(i);
		}
		
		if (i==104 || i==105) {
				Allmails[5]=Integer.toString(i);
			}
					
			add_prefs_key(MailAddress, 
					Allmails[0]+","+Allmails[1]+","+Allmails[2]+","+Allmails[3]+","	+Allmails[4]+","+Allmails[5]);			
	}	
	
	protected void parse_sms_to_perf(int i) {
		Allsms[0]=sms1.getText().toString();
		Allsms[1]=sms2.getText().toString();
		Allsms[2]=sms3.getText().toString();
				
		if (i==100  || i==101) {
			Allsms[3]=Integer.toString(i);
			Allsms[4]="102";
			Allsms[5]="104";
		}
		
		if (i==102 || i==103) {
			Allsms[3]="100";
			Allsms[4]=Integer.toString(i);
			Allsms[5]="104";
		}
		
		if (i==104 || i==105) {
			Allsms[3]="100";
			Allsms[4]="102";
			Allsms[5]=Integer.toString(i);
			}
		
			add_prefs_key(SMSnumber, 
					Allsms[0]+","+Allsms[1]+","+Allsms[2]+","+Allsms[3]+","	+Allsms[4]+","+Allsms[5]);	
			
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu100, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
switch (id) {
		
		case R.id.Save:
			save_all_prefs();
    		Toast.makeText(getBaseContext(),"Saved...",Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.Back:
			Toast.makeText(getBaseContext(),"Exiting...",Toast.LENGTH_SHORT).show();
    		setup1.this.finish();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pingbutton:
			pingButton.setBackgroundColor(Color.RED);
			Toast.makeText(getApplicationContext(), "ping !!!!", Toast.LENGTH_SHORT).show();
			setup_globals();
			SaveToSmb.opType=PING_COMMAND;	
			SaveToSmb sv =new SaveToSmb(setup1.this);	 // at the end
			sv.execute("");
		break;
		
		case R.id.chkemail1:
			if ((    (CheckBox) v).isChecked()) {
				parse_mail_to_perf(101); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_mail_to_perf(100); //remove mail#1 from the PREFS
			}
		break;
		case R.id.chkemail2:
			if ((    (CheckBox) v).isChecked()) {
				parse_mail_to_perf(103); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_mail_to_perf(102); //remove mail#1 from the PREFS
			}
		break;
		case R.id.chkemail3:
			if ((    (CheckBox) v).isChecked()) {
				parse_mail_to_perf(105); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_mail_to_perf(104); //remove mail#1 from the PREFS
			}
		break;
		
		case R.id.smsck1:
			if ((    (CheckBox) v).isChecked()) {
				parse_sms_to_perf(101); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_sms_to_perf(100); //remove mail#1 from the PREFS
			}
			smsck2.setChecked(false);
			smsck3.setChecked(false);
		break;
		
		case R.id.smsck2:
			if ((    (CheckBox) v).isChecked()) {
				parse_sms_to_perf(103); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_sms_to_perf(102); //remove mail#1 from the PREFS
			}
			smsck1.setChecked(false);
			smsck3.setChecked(false);
		break;
		
		case R.id.smsck3:
			if ((    (CheckBox) v).isChecked()) {
				parse_sms_to_perf(105); //add mail#1 to the PREFS
			}
	
			if (!((CheckBox) v).isChecked()) {
				parse_sms_to_perf(104); //remove mail#1 from the PREFS
			}
			smsck1.setChecked(false);
			smsck2.setChecked(false);
		break;
		
		case R.id.Splash:
			if ((    (CheckBox) v).isChecked()) {
				Toast.makeText(getApplicationContext(), "music on!!!!", Toast.LENGTH_SHORT).show();
	            playSplash_="SoundMusic_on";
			}
	
			if (!((CheckBox) v).isChecked()) {
				Toast.makeText(getApplicationContext(), "music off  ", Toast.LENGTH_SHORT).show();
	            playSplash_="SoundMusic_off";
			}
			break;
		case R.id.textsize11:
			if ((    (CheckBox) v).isChecked()) {
				Toast.makeText(getApplicationContext(), "Font Size selected - 30", Toast.LENGTH_SHORT).show();
			     TextSize_="30";
			}
	
			if (!((CheckBox) v).isChecked()) {
				Toast.makeText(getApplicationContext(), "Font Size selected - 20", Toast.LENGTH_SHORT).show();
			     TextSize_="20";
			}
			break;
	
			}
		}
	
	private void setup_globals(){
		SaveToSmb.context=context;	
//		SaveToSmb.ip="192.168.1.116"; //no michse
////		SaveToSmb.ip="192.168.1.118"; //Irit Hishtaltut
////		SaveToSmb.ip="192.168.1.114"; //toshiba
////		SaveToSmb.ip="192.168.1.105"; //x_201
////		SaveToSmb.ip="192.168.1.100";
		
//		SaveToSmb.username= "elirr";
//		SaveToSmb.password= "elirr";
		
//////		SaveToSmb.username= "tosh1";
//////		SaveToSmb.password= "mnbMNB9988";
		
//		SaveToSmb.sharedFolder= "try//";//no michse
		
		SaveToSmb.ip =  sharedpreferences.getString(REMOTEIP, "");
		SaveToSmb.sharedFolder=  sharedpreferences.getString(REMOTEFOLDER, "");
		 SaveToSmb.username=  sharedpreferences.getString(REMOTEUSERNAME, "");
		 SaveToSmb.password=  sharedpreferences.getString(REMOTEUSERPASSWORD, "");

		
		
		
		
	}

	@Override
	public void onTaskDone(DelegateDataBackHolder s) {
		DelegateDataBackHolder s2=s;
//		Toast.makeText(this, "@@@!!!!!!!!!!!!!!!!!! "+String.valueOf(s2.getOp())+" ***  ", Toast.LENGTH_SHORT).show();
		
		 String[] files=SaveToSmb.files;
		 SmbFile[] smbFiles=SaveToSmb.smbFiles;
		 String readText=SaveToSmb.readText;
		 String error=SaveToSmb.error1 ;
		 String exeption=SaveToSmb.exeption1;
		 int opType=SaveToSmb.opType;
		 String pingParams=SaveToSmb.pingParameters;

		Toast.makeText(this, ""+String.valueOf(s2.getOp())+
				" ***  "+opType, Toast.LENGTH_SHORT).show();
/////////int y=files.length+smbFiles.length+readText.length()+error.length()+opType;
//		Toast.makeText(this, ""+pingParams+
//				" !!!  ", Toast.LENGTH_SHORT).show();
		
//		if (SaveToSmb.opType==98){
//			String s9="OP= "+String.valueOf(s2.getOp()+" "+pingParams);
//            Toast.makeText(context,"ping=   "+String.valueOf(SaveToSmb.pingable),Toast.LENGTH_LONG).show();

//		}	
		String s7="";
		if (SaveToSmb.pingable) s7=SaveToSmb.pingParameters;
            new AlertDialog.Builder(context)
            .setTitle("Ping status  "+String.valueOf(SaveToSmb.pingable))
            .setMessage("Ping time: \n"+s7)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	pingButton.setBackgroundColor(Color.GREEN);
                }
             })

            // A null listener allows the button to dismiss the dialog and take no further action.
//            .setNegativeButton(android.R.string.no, null)
//            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
            
            
            
            
	}
		
}
