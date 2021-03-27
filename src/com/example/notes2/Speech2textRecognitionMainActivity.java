package com.example.notes2;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Speech2textRecognitionMainActivity extends Activity  implements MySpeechRecogTaskInformer{
	SpeechRecognitionListener1 listener;
	private SpeechRecognizer mSpeechRecognizer;
	private Intent mSpeechRecognizerIntent; 
	int speechResult;
	int speechErrors=0;
	TextView txtSpeechInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speechrecognitionactivity_main);
		txtSpeechInput = (TextView) findViewById(R.id.ti);
		setupSpeechRecognizer();
	}

	
	
	@Override
	protected void onStart() {
		if (listener.ismIslistening()) {
			setupSpeechRecognizer();			
			}
	    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
		super.onStart();
	}



	private void setupSpeechRecognizer() {
	    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
	    mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

//	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-UK");
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	    		"en-UK");
	    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
	                                     this.getPackageName());
	    
	    
	    
	    

	     listener = new SpeechRecognitionListener1();
	     listener.delegate=Speech2textRecognitionMainActivity.this;
	    mSpeechRecognizer.setRecognitionListener(listener);
	    
		
	}

	@Override
	protected void onDestroy() {
		if (mSpeechRecognizer != null) 		{
		        mSpeechRecognizer.destroy();
			}
		super.onDestroy();
	}

	


	@Override
	public void onBackPressed() {
		if (mSpeechRecognizer != null) {
		        mSpeechRecognizer.destroy();
			}
		Intent returnIntent = new Intent();
		setResult(Activity.RESULT_CANCELED,returnIntent);
		finish();
		super.onBackPressed();
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.speechrecognitionmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.recognize) {
			if (listener.ismIslistening()) {
					setupSpeechRecognizer();			
				}
			    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);			
			return true;
		}

		if (id == R.id.read) {			
			if (!listener.ismIslistening() || speechErrors!=0) {
				String s=listener.getResultString();
				txtSpeechInput.setText(s);			}		
			return true;
		}

		if (id == R.id.clear) {
			
setupSpeechRecognizer();
		
			return true;
		}	
		
		return super.onOptionsItemSelected(item);
	}*/
	
	
/*	
	protected class SpeechRecognitionListener implements RecognitionListener	{
	    @Override
	    public void onBeginningOfSpeech()
	    {     }

    @Override
    public void onBufferReceived(byte[] buffer)
    {    }

    @Override
    public void onEndOfSpeech()
    {   	}

    @Override
    public void onError(int error)
    {     }

    @Override
    public void onEvent(int eventType, Bundle params)
    {    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {    }

    @Override
    public void onReadyForSpeech(Bundle params)
    {    }

    @Override
    public void onResults(Bundle results)
    {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
 String s="";
 for (int i=0;i<matches.size();i++){
	 s+=matches.get(i)+"\n";
	 
 }
        
        txtSpeechInput.setText(s);
        
        // matches are the return values of speech recognition engine
        // Use these values for whatever you wish to do
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {     }
}
*/

	@Override
	public void onTaskDone(SpeechrecognitionHolder result) {
		speechResult=0;
		speechErrors=0;
		ArrayList<String> al = null;
		try {
			al = result.getMatches();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error 187 "+e, Toast.LENGTH_LONG).show();  	    	
			e.printStackTrace();
		}

		
		
		SpeechRecognitionActivityTwo.init(2);
if (al!=null && al.size()>0){
		try {
			speechResult = SpeechRecognitionActivityTwo.recognize(al);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error 198 "+e, Toast.LENGTH_LONG).show();  	    	
			e.printStackTrace();
		}
}
else {
	al.add("!");
	Toast.makeText(getBaseContext(), "Error 203 ", Toast.LENGTH_LONG).show();  	    		
}
		String s="";
		s+="result   "+speechResult+"\n\n";
		try {
			for (int i=0;i<al.size();i++){
				 s+=al.get(i)+"\n";			 
			 }
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error 203 "+e, Toast.LENGTH_LONG).show();  	    		
			e.printStackTrace();
		}
		try {
			speechErrors = result.getSpeechErrors();
			s+="Errors   "+speechErrors+"\n";
////////////			txtSpeechInput.setText(s);
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error 221=  "+e, Toast.LENGTH_SHORT).show();  	    	
						e.printStackTrace();
		}
	
		try {
			if (mSpeechRecognizer != null) {
			    mSpeechRecognizer.destroy();
}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Error 231=  "+e, Toast.LENGTH_SHORT).show();  	    	
			e.printStackTrace();
		}
	try {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("result",speechResult);
		returnIntent.putExtra("speechErrors",speechErrors);
		returnIntent.putExtra("resultsSentences",al);// check it...........................
		if (al!=null && al.size()>0 && !al.get(0).equals("!")) 		
			setResult(Activity.RESULT_OK,returnIntent);
		else setResult(Activity.RESULT_CANCELED,returnIntent);
	} catch (Exception e) {
		Toast.makeText(getBaseContext(), "Error 241=  "+e, Toast.LENGTH_SHORT).show();  	    	
		e.printStackTrace();
	}
	finish();
	}
	
}
