package com.example.notes2;

import java.io.Serializable;

public class AlarmPostActivityHolder1 implements Serializable{

	private static final long serialVersionUID = 1L;
	Note notee;
	int timeStamp,indx, random;
	String hourMinuteAlarm;
	long timeMillis;
	
	public AlarmPostActivityHolder1(Note notee, int timeStamp, int indx,
			String hm, int random, long time) {
		super();
		this.notee = notee;
		this.timeStamp = timeStamp;
		this.indx=indx;
		this.hourMinuteAlarm=hm;
		this.random=random;
		this.timeMillis=time;
	}
	
	public long getTimeMillis(){
		return timeMillis;
	}
	
	public int getRandom() {
		return random;
	}
	
	public String getHM() {
		return hourMinuteAlarm;
	}
	
	public int getIndx() {
		return indx;
	}
	
	public Note getNotee() {
		return notee;
	}
	
	public void setNotee(Note notee) {
		this.notee = notee;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
}
