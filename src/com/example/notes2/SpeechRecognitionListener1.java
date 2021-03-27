package com.example.notes2;

import java.util.ArrayList;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

public class  SpeechRecognitionListener1  implements RecognitionListener  {
	public MySpeechRecogTaskInformer delegate = null;
	private boolean mIslistening=false; 
	String s="";
	boolean beginningOfSpeech=false,endOfSpeech=false;;
	float RmsChanged=0;
	int speechErrors=0;
	ArrayList<String> matches =new ArrayList<String>();
	
	@Override
	public void onReadyForSpeech(Bundle params) {

	   	mIslistening=true;
	}

	@Override
	public void onBeginningOfSpeech() {
		beginningOfSpeech=true;
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		RmsChanged=rmsdB;
		
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndOfSpeech() {
	endOfSpeech=true;
		
	}

	@Override
	public void onError(int error) {
		speechErrors=error;
		
	}

	@Override
	public void onResults(Bundle results) {
	    matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	       
		SpeechrecognitionHolder sh=new SpeechrecognitionHolder(matches, beginningOfSpeech, endOfSpeech,
		RmsChanged, speechErrors);
		beginningOfSpeech=false;
		endOfSpeech=false;
		RmsChanged=0;
		speechErrors=0;;
	   	mIslistening=false;
 
// for (int i=0;i<matches.size();i++){
//	 s+=matches.get(i)+"\n";	 
// }
 delegate.onTaskDone(sh);
  
	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub
		
	}

	public boolean ismIslistening() {
		return mIslistening;
	}

	public void setmIslistening(boolean mIslistening) {
		this.mIslistening = mIslistening;
	}

	public String getResultString(){
		return s;
	}
	
	
	
	
	
	
}
