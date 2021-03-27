package com.example.notes2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MultiClipBoardMainActivity extends Activity implements View.OnClickListener,
	OnItemClickListener,OnItemLongClickListener{

	Context context=this;
///	EditText et1;
	LinearLayout ll;
	Button btna,btnb;
	TextView textView1;
	
	ListView listView;
    private List<ClipBoardHolder> myClipHolder = new  ArrayList<ClipBoardHolder>();
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String CLIPS = "clips" ;
   int positionA;
    private int width=0,height=0;
    float tsize;
    //ArrayList<String> temp_note = new ArrayList<String>();
    
    ArrayAdapter<ClipBoardHolder> adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_clip_board_main);
		setTextSize();
		
		ll = (LinearLayout) findViewById(R.id.ll);
		btna = (Button) findViewById(R.id.btna);
		btnb = (Button) findViewById(R.id.btnb);
		
		btna.setText("Add text");
		btnb.setText("Delate all");
		
		btna.setOnClickListener(this);
		btnb.setOnClickListener(this);
		
//		et1=(EditText)findViewById(R.id.et1);
		textView1=(TextView)findViewById(R.id.textView1);
		
		listView = (ListView) findViewById(R.id.listView);
		
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
			
		adapter = new MyListAdapter();
		//ListView list = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		
//		myClipHolder.add(new ClipBoardHolder("1234", 5477474));
//		myClipHolder.add(new ClipBoardHolder("8785", 8798697));
//		myClipHolder.add(new ClipBoardHolder("14578234", 7654784));
//		myClipHolder.add(new ClipBoardHolder("18648234", 86458888));
		loadClipTable();
		adapter.notifyDataSetChanged();
		updatetatus();
	}
	
	
	private void setTextSize() {
        tsize=0;
		double d=displ.getScreenInches();
		if (d<5) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
		if (d>5 && d<6) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
		if (d>6 && d<7) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
		if (d>7 && d<8) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
		if (d>8 && d<9) tsize=getResources().getDimension(R.dimen.textsize3cam) ;
		if (d>9 && d<10) tsize=getResources().getDimension(R.dimen.textsize3cam) ;

		
	}


	/////////////////////////////////////////////////////////
	public void onWindowFocusChanged(boolean hasFocus) {

		if (width==0 || height==0) {
			
//	        tsize=0;
//			displ disp=new displ();
//			double d=disp.screenInches;
//			if (d<5) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
//			if (d>5 && d<6) tsize=getResources().getDimension(R.dimen.textsize1cam) ;
//			if (d>6 && d<7) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
//			if (d>7 && d<8) tsize=getResources().getDimension(R.dimen.textsize2cam) ;
//			if (d>8 && d<9) tsize=getResources().getDimension(R.dimen.textsize3cam) ;
//			if (d>9 && d<10) tsize=getResources().getDimension(R.dimen.textsize3cam) ;

	width=ll.getMeasuredWidth();
	height=ll.getMeasuredHeight();

Toast.makeText(getBaseContext(),"onWindows = "+width+"  "+height,  Toast.LENGTH_SHORT).show();    
/*
	ViewGroup.MarginLayoutParams ipet1 = 
			(ViewGroup.MarginLayoutParams) tv.getLayoutParams();
			ipet1.width=width;
			ipet1.height=(height*4)/100;
			ipet1.setMargins(0,   //left
					0,    //top
					0,0);
			tv.requestLayout();
			///////////////////
			int i=54;
			 ipet1 =(ViewGroup.MarginLayoutParams) vidcam.getLayoutParams();
					ipet1.width=(width*i)/1600;
					ipet1.height=(height*i)/2400;
//					ipet1.setMargins(0,   //left
//							0,    //top
//							0,0);
			vidcam.requestLayout();
					
			 ipet1 =(ViewGroup.MarginLayoutParams) ImageView3.getLayoutParams();
						ipet1.width=(width*i)/1600;
						ipet1.height=(height*i)/2400;
//						ipet1.setMargins(0,   //left
//								0,    //top
//								0,0);
			ImageView3.requestLayout();	
			
			 ipet1 =(ViewGroup.MarginLayoutParams) ImageView2.getLayoutParams();
				ipet1.width=(width*i)/1600;
				ipet1.height=(height*i)/2400;
//				ipet1.setMargins(0,   //left
//						0,    //top
//						0,0);
				ImageView2.requestLayout();	
			
			ipet1 =(ViewGroup.MarginLayoutParams) ImageView1.getLayoutParams();
		ipet1.width=(width*i)/1600;
		ipet1.height=(height*i)/2400;
//		ipet1.setMargins(0,   //left
//				0,    //top
//				0,0);
		ImageView1.requestLayout();	
		
		 ipet1 =(ViewGroup.MarginLayoutParams) ImageView4.getLayoutParams();
			ipet1.width=(width*i)/1600;
			ipet1.height=(height*i)/2400;
//			ipet1.setMargins(0,   //left
//					0,    //top
//					0,0);
			ImageView4.requestLayout();	

			 ipet1 =(ViewGroup.MarginLayoutParams) spin.getLayoutParams();
				ipet1.width=(width*i*14)/1600;
//				ipet1.height=(height*i*3)/2400;
//				ipet1.setMargins(0,   //left
//						0,    //top
//						0,0);
				spin.requestLayout();	
*/
		}

		super.onWindowFocusChanged(hasFocus);
	}
  ///////////////////////////////////////////////////////////
	

	private void updatetatus() {
		textView1.setText("Status: "+myClipHolder.size()+" items");
		
	}

	@Override
	public void onBackPressed() {
		saveClipTable(myClipHolder);
		super.onBackPressed();
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multi_clip_board_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.saveclips) {
			saveClipTable(myClipHolder);
			updatetatus();
			return true;
		}
		
		if (id == R.id.loadclips) {
			loadClipTable();
			adapter.notifyDataSetChanged();
			updatetatus();
			return true;
		}		
		
		return super.onOptionsItemSelected(item);
	}
	
	

		@SuppressWarnings("unchecked")
		private void loadClipTable() {
	    List<ClipBoardHolder> userList   = new ArrayList<ClipBoardHolder>();
	myClipHolder.clear();	    
			SharedPreferences sharedpreferences;
		    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
try {
userList = (List<ClipBoardHolder>) ObjectSerializerMultiClip.deserialize(sharedpreferences.getString(CLIPS,
		ObjectSerializerMultiClip.serialize(new ArrayList<ClipBoardHolder>())));
		      } catch (IOException e) {
		          e.printStackTrace();
		      }		
for (int i=0;i<userList.size();i++){
	myClipHolder.add(userList.get(i));
}
	}

		private void saveClipTable(List<ClipBoardHolder> myClipHolder2) {
			SharedPreferences sharedpreferences;
		    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

			Editor editorC = sharedpreferences.edit();
			try {
			editorC.putString(CLIPS, ObjectSerializerMultiClip.serialize(myClipHolder2));
			} catch (IOException e) {
			e.printStackTrace();
			}
			editorC.commit();
			
			}	
		
	


	private class MyListAdapter extends ArrayAdapter<ClipBoardHolder> {
		public MyListAdapter() {
			super(MultiClipBoardMainActivity.this, R.layout.item_view_multi_clip, myClipHolder);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_view_multi_clip, parent, false);
			}
			
			// Find the car to work with.
			ClipBoardHolder currentClipHolder = myClipHolder.get(position);
			
			// Fill the view

			TextView clp = (TextView) itemView.findViewById(R.id.clipdata);
			clp.setBackgroundColor(Color.YELLOW);
			clp.setTextSize(tsize);
			clp.setText(currentClipHolder.getClipdata());

			TextView date = (TextView) itemView.findViewById(R.id.date1);
			date.setTextSize(tsize);
			date.setText(currentClipHolder.getInsterDate());

			TextView serial = (TextView) itemView.findViewById(R.id.serial);
			serial.setTextSize(tsize);
			serial.setText(""+(1+position));
			
			return itemView;
		}				
	}
   
	   private void setClipboard(Context context,String text) {  // from listview
	        
		   android.content.ClipboardManager clipboard =
		  		 	(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		   android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
		   clipboard.setPrimaryClip(clip);
		       
		      }
	   
		private void copyClip(){  // to list view
//			getClip();
//			StringBuilder str = new StringBuilder(et22.getText().toString());
			// insert character value at offset 8
//			str.insert(pointerText, getClip());
			myClipHolder.add(new ClipBoardHolder(getClip(), System.currentTimeMillis()));
			adapter.notifyDataSetChanged();	
//			et22.setSelection(pointerText);	
		}	

		private String getClip(){
			String pasteFromClipboard="";
			android.content.ClipboardManager clipboardAA =(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData abc = clipboardAA.getPrimaryClip();
			ClipData.Item itemC = abc.getItemAt(0);
			//Toast.makeText(getApplicationContext(), "clip is...  "+text, Toast.LENGTH_SHORT).show(); 

			if (itemC.getText().toString()!=null && itemC.getText().toString().length()>0){
				pasteFromClipboard=itemC.getText().toString();
			}
			else {
				pasteFromClipboard="";
			}
			
			
			return pasteFromClipboard;
		}

		@Override
		public void onClick(View v) {

			   switch (v.getId()) {

		        case R.id.btna:
		         copyClip();
		         updatetatus();
		            break;

		        case R.id.btnb:
myClipHolder.clear();
adapter.notifyDataSetChanged();
updatetatus();
		            break;

		        default:
		            break;
		    }		
			
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
setClipboard(context, myClipHolder.get(position).getClipdata());
			
			Toast.makeText(getApplicationContext(), ":   "+myClipHolder.get(position).getClipdata(), Toast.LENGTH_SHORT).show();

			
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			positionA=position;
showDialog();
			return true;
		}	
		
	
		private void showDialog(){
//			myClipHolder.remove(positionA);
//			adapter.notifyDataSetChanged();
//			updatetatus();
//			Toast.makeText(getApplicationContext(), "LONG   "+positionA, Toast.LENGTH_SHORT).show();
//
//			
//			
			
			
			// custom dialog
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog1);
			dialog.setTitle("Edit item: "+(positionA+1));

			// set the custom dialog components - text, image and button
			final EditText text = (EditText) dialog.findViewById(R.id.text);
			text.setTextSize(tsize);
			text.setText(myClipHolder.get(positionA).getClipdata());
			
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageResource(R.drawable.ic_launcher);

			Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
			Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
			Button dialogButton3 = (Button) dialog.findViewById(R.id.btn3);
			
			dialogButton1.setTextSize(tsize);
			dialogButton2.setTextSize(tsize);
			dialogButton3.setTextSize(tsize);
			
			
			
			ViewGroup.MarginLayoutParams ipet1 = 
					(ViewGroup.MarginLayoutParams) dialogButton1.getLayoutParams();
					ipet1.width=width/4;
					ipet1.height=(height*6)/100;
					ipet1.setMargins(20,   //left
							10,    //top
							0,10);
					dialogButton1.requestLayout();
	
				 ipet1 = (ViewGroup.MarginLayoutParams) dialogButton2.getLayoutParams();
							ipet1.width=width/8;
							ipet1.height=(height*6)/100;
							ipet1.setMargins(20,   //left
									10,    //top
									0,10);
							dialogButton2.requestLayout();
			
			 ipet1 = (ViewGroup.MarginLayoutParams) dialogButton3.getLayoutParams();
									ipet1.width=width/6;
									ipet1.height=(height*6)/100;
									ipet1.setMargins(0,   //left
											10,    //top
											20,10);
									dialogButton3.requestLayout();							
							
							
										// if button is clicked, close the custom dialog
			dialogButton1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myClipHolder.remove(positionA);
					adapter.notifyDataSetChanged();
					updatetatus();
					Toast.makeText(getApplicationContext(), "LONG   "+positionA, Toast.LENGTH_SHORT).show();

					
					
					dialog.dismiss();
				}
			});

			dialogButton2.setOnClickListener(new OnClickListener() {  // save changes of edittext
				@Override
				public void onClick(View v) {
					ClipBoardHolder cl =myClipHolder.get(positionA);
				cl.setClipdata(text.getText().toString());
				myClipHolder.set(positionA, cl);
				adapter.notifyDataSetChanged();
				updatetatus();
				saveClipTable(myClipHolder);
				Toast.makeText(getApplicationContext(), "LONG   "+positionA, Toast.LENGTH_SHORT).show();

					dialog.dismiss();
				}
			});
			
			dialogButton3.setOnClickListener(new OnClickListener() {  //cancel
				@Override
				public void onClick(View v) {
				
					dialog.dismiss();
				}
			});
			
			dialog.show();
		  }
		
	
			
		
		
		
	
}
