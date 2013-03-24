package com.distributed.server;

import java.sql.Time;

public class Booking {

	private int startSlot;
	private int endSlot;
	private Time startTime;
	private Time endTime;
	
	private int UID;
	
	public static boolean conflicts(Booking b1, Booking b2){
		return false;
	}
	
	public Booking(Time start, Time end){
		this.startTime = start;
		this.endTime = end;
	}

	public int getStartSlot() {
		return startSlot;
	}

	public void setStartSlot(int startSlot) {
		this.startSlot = startSlot;
	}

	public int getEndSlot() {
		return endSlot;
	}

	public void setEndSlot(int endSlot) {
		this.endSlot = endSlot;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	
}
