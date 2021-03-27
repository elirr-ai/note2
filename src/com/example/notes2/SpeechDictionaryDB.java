package com.example.notes2;

public class SpeechDictionaryDB {

	String s;int numberparams;int opCode;
	public SpeechDictionaryDB (String s, int n, int op){
		this.s=s; this.numberparams=n;this.opCode=op;
	}
	/**
	 * @return the opCode
	 */
	public int getOpCode() {
		return opCode;
	}
	/**
	 * @param opCode the opCode to set
	 */
	public void setOpCode(int opCode) {
		this.opCode = opCode;
	}
	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}
	/**
	 * @param s the s to set
	 */
	public void setS(String s) {
		this.s = s;
	}
	/**
	 * @return the numberparams
	 */
	public int getNumberparams() {
		return numberparams;
	}
	/**
	 * @param numberparams the numberparams to set
	 */
	public void setNumberparams(int numberparams) {
		this.numberparams = numberparams;
	}

	
	
	
	
}
