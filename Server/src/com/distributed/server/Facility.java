package com.distributed.server;

import java.util.*;

public class Facility {

	private String name;
	private Vector<Vector<Booking>> daySchedule;

	public Facility(String name) {
		this.name = name;
		daySchedule = new Vector<Vector<Booking>>();
		for (Vector<Booking> day : daySchedule){
			day = new Vector<Booking>();
		}
	}

	public String getName() {
		return name;
	}

	public int book(int facId, int day, int start, int end) {
		Vector<Booking> bookings = this.daySchedule.get(day);
		for(int i=0; i< bookings.size(); i++){
			if (bookings.get(i).conflict(start, end)) {
				return 0;
			}
	    }
		Booking booking = new Booking(facId, day, start, end);
		bookings.add(booking);
		return booking.getID();
	}
		
	public Integer[] getAvailability(int day){
		Vector<Booking> bookings = this.daySchedule.get(day);
		Vector<Integer> slots = new Vector<Integer>();
		for(int i=0; i< bookings.size(); i++){
			slots.add(bookings.get(i).getStartSlot());
			slots.add(bookings.get(i).getEndSlot());
		}
		Integer[] span = (Integer[]) slots.toArray(new Integer[0]);
		Arrays.sort(span);
		return span;
	}
	
	public String updateBooking(int day, int confID, int slotOffset) {
		Vector<Booking> bookings = this.daySchedule.get(day);
		
		Booking update = null;
		for(int i=0; i< bookings.size(); i++){
			if (bookings.get(i).getID() == confID){
				update = bookings.get(i);
				i = bookings.size();
			}
		}
		
		if (update == null){
			return "Invalid booking ID provided.";
		}
		int newStart = slotOffset + update.getStartSlot();
		int newEnd = slotOffset + update.getEndSlot();
		
		if (newStart < 0 || newEnd > 47){
			return "Invalid offset provided. New booking must remain on same day.";
		}
		
		for (int j=0; j<bookings.size();j++){
			if (bookings.get(j).getID() != confID){
				if (bookings.get(j).conflict(newStart, newEnd)){
					return "Invalid offset provided. Conflict with another booking.";
				}
			}
		}
		
		//proceed with update
		update.setStartSlot(newStart);
		update.setEndSlot(newEnd);
		return "Booking " + confID + " updated to " + slotToTime(newStart) + "-" + slotToTime(newEnd);		
	}

	public String parseAvailability(Vector<Integer> days) {
		String result = "Facility avilability for " + this.name + ":\n";
		
		for (int i=0; i<days.size(); i++){
			 Integer[] span = getAvailability(days.get(i));
			 
			 if (span.length >= 2) {
				 result += BookingUtils.getString(days.get(i)) + ": ";
				 if (span[0] != 0) { //starts with free period
					 result += slotToTime(0) + "-" + slotToTime(span[0]) + "  ";
				 }
				 int tail = span[1];
				 for (int j=2; j<span.length; j+=2){
					 if (span[j] != tail) {
						 result += slotToTime(tail) + "-" + slotToTime(span[j]) + "  "; 
					 }
					 tail = span[j+1]; //valid cuz they should come in pairs
				 }
			 } else {
				 result += "No bookings yet for day " + days.get(i);
			 }
			 result += "\n";
		}
		
		return result;
	}

	private String slotToTime(Integer slot) {
		int hh = slot / 2;
		if ( (slot % 2) == 0) { //even
			return String.format("%02d", hh) + "00";
		} else {
			return String.format("%02d", hh) + "30";
		}
	}



}