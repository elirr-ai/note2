package com.example.notes2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dateFrom1970 {
	Date dt;
	String dt1;
	long curMillis;
	
	public dateFrom1970(){
		super();
	}
	
	public String getmillisString() {
		dt= new Date();
		return String.valueOf(dt.getTime());
	}
	
	public long getmillisLong() {
		dt= new Date();
		long curMillis = dt.getTime();
		return curMillis;
	}
	


public String toDateStr()
{
	dt= new Date();
	String format="dd_MM_yyyy__hh_mm_ss";
    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
    return formatter.format(dt.getTime());
}

public String toDateStrfromStringdae(String d)
{
	String format="dd/MM/yyyy hh:mm:ss";
    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
    return formatter.format(Long.valueOf(d));
}
	
	
	
	
}
