package com.example.notes2;

import java.util.ArrayList;

public class SpeechrecognitionHolder {

	ArrayList<String> matches =new ArrayList<String>();
	boolean beginningOfSpeech=false,endOfSpeech=false;;
	float RmsChanged=0;
	int speechErros=0;
	
	public SpeechrecognitionHolder (ArrayList<String> matches, boolean b, boolean e, float r, int er)
	{
		this.matches=matches;
		this.beginningOfSpeech=b;
		this.endOfSpeech=e;
		this.RmsChanged=r;
		this.speechErros=er;
	}

	public ArrayList<String> getMatches() {
		return matches;
	}

	public void setMatches(ArrayList<String> matches) {
		this.matches = matches;
	}

	public boolean isBeginningOfSpeech() {
		return beginningOfSpeech;
	}

	public void setBeginningOfSpeech(boolean beginningOfSpeech) {
		this.beginningOfSpeech = beginningOfSpeech;
	}

	public boolean isEndOfSpeech() {
		return endOfSpeech;
	}

	public void setEndOfSpeech(boolean endOfSpeech) {
		this.endOfSpeech = endOfSpeech;
	}

	public float getRmsChanged() {
		return RmsChanged;
	}

	public void setRmsChanged(float rmsChanged) {
		RmsChanged = rmsChanged;
	}

	public int getSpeechErrors() {
		return speechErros;
	}

	public void setSpeechErrors(int speechErros) {
		this.speechErros = speechErros;
	}	
	
	
	
	
}
