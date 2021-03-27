package com.example.notes2;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ClipBoardHolder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clipdata;
	private long insterDate;

	public ClipBoardHolder(String clip,long dateInsert ) {
		super();
		this.clipdata = clip;
		this.insterDate = dateInsert;
		}



	/**
	 * @return the clipdata
	 */
	public String getClipdata() {
		return clipdata;
	}



	/**
	 * @param clipdata the clipdata to set
	 */
	public void setClipdata(String clipdata) {
		this.clipdata = clipdata;
	}



	/**
	 * @return the insterDate
	 */
	public String getInsterDate() {
		String format="dd/MM/yyyy hh:mm:ss";
	    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
	    return formatter.format(insterDate);		
			}

	public void setInsterDate(long insterDate) {
				this.insterDate = insterDate;
	}
	
	

	
}

