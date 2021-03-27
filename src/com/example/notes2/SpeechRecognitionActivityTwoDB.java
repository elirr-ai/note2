package com.example.notes2;

import java.util.ArrayList;

public class SpeechRecognitionActivityTwoDB {

	String OPcode;
	int OpcodeCmd;

	public SpeechRecognitionActivityTwoDB(String a, int c){
		this.OPcode=a;this.OpcodeCmd=c;
	}
	
	
	public String getOPcode() {
		return OPcode;
	}

	public void setOPcode(String oPcode) {
		OPcode = oPcode;
	}


	public int getOpcodeCmd() {
		return OpcodeCmd;
	}

	public void setOpcodeCmd(int opcodeCmd) {
		OpcodeCmd = opcodeCmd;
	}

	


	
}
