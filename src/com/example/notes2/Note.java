package com.example.notes2;

import java.io.Serializable;

public class Note implements Serializable {

	private static final long serialVersionUID = 4469259838013602956L;
	private String memo_header="";  // file name and also memo header
	private String memo_body="";
	private String date="";
	private String currentDate="";
	private String priority="";
	private String last_note="";
	private String note_count="";
	private String audio_record="";
	private String camera_record="";
	private String hand_note="";
	private String cameracnt="";
	private String noteID="";
		
	public Note(){
	//public Note(String memo_header, String date, String priority , String last_note,String note_count, String audio_record, String camera_record,String hand_note
	//		, String cameracnt) {
		super();
	//	this.memo_header = memo_header;
	//	this.date = date;
	//	this.priority = priority;
	//	this.last_note=last_note;
	//	this.note_count=note_count;
	//	this.audio_record=audio_record;
	//	this.camera_record=camera_record;
	//	this.hand_note=hand_note;
	//	this.cameracnt=cameracnt;
	}

	public void setMemoID(String m){
		this.noteID=m;
	}
	public String getMemoID(){
		return noteID;
	}
	
	
	public void setMemoBody(String mb){
		this.memo_body=mb;
	}
	public String getMemoBody(){
		return memo_body;
	}
	
	public String getMemBodyLength(){
		return String.valueOf(memo_body.length());
	}
	
	public String getCameraCnt() {
		return cameracnt;
	}

	public void setcameracnt(String cameracnt) {
		this.cameracnt = cameracnt;
	}
	
	public String getMemo_header() {
		return memo_header;
	}

	public void setMemo_header(String memo_header) {
		this.memo_header = memo_header;
	}

	public String getDate() {
		return date.substring(0, 14);
	}

	public void setDate(String date) {
		this.date = date;
	}
	public void setCurrentDate(String currentDateFormat) {
		this.currentDate = currentDateFormat;
	}
	public String getCurrentDate() {
		return currentDate.substring(0, 14);
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
	public String getlast_note() {
		return last_note;
	}

	public void setlast_note(String last_note) {
		this.last_note = last_note;
	}
	
	
	public String getnote_count() {
		return note_count;
	}

	public void setnote_count(String note_count) {
		this.note_count = note_count;
	}
	
		
	public String get_audio_record() {
		return audio_record;
	}

	public void set_audio_record(String audio_record) {
		this.audio_record = audio_record;
	}
	
	public String get_camera_record() {
		return camera_record;
	}

	public void set_camera_record(String camera_record) {
		this.camera_record = camera_record;
	}	
	
	public String get_hand() {
		return hand_note;
	}

	public void set_hand(String hand_note) {
		this.hand_note = hand_note;
	}
		
	public String  get_millisString() {
		return   date.substring(15, date.length());
	}

	
	public String  get_millisStringAll() {
		return   date;
	}

	
	
}
