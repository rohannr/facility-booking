package com.distributed.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class Facility {

	private String name;
	private Vector<Vector<Booking>> daySchedule;
	private Vector<Monitor> monitors = new Vector<Monitor>();

	public Facility(String name) {
		this.name = name;
		daySchedule = new Vector<Vector<Booking>>();
		for(int i=0; i < 7; i++){
			Vector<Booking> day = new Vector<Booking>();
			daySchedule.add(day);
		}
	}

	public String getName() {
		return name;
	}

	public String book(int facId, int day, int start, int end) throws IOException {
		Vector<Booking> bookings = this.daySchedule.get(day);
		for(int i=0; i< bookings.size(); i++){
			if (bookings.get(i).conflict(start, end)) {
				return "Booking unsuccessful due to time conflict with: Booking #" +
						bookings.get(i).getID() + " " + slotToTime(bookings.get(i).getStartSlot()) + " " + slotToTime(bookings.get(i).getEndSlot());
			}
	    }
		Booking booking = new Booking(facId, day, start, end);
		bookings.add(booking);
		triggerMonitors();
		return booking.getID();
	}

	public void monitor(String interval, InetAddress IP, int port) {
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		
		long days = 86400000 * Integer.parseInt(interval.substring(0,1));
		long hours = 3600000 * Integer.parseInt(interval.substring(1,3));
		
		long expiry = millis + days + hours;
		
		this.monitors.add(new Monitor(expiry, IP, port));
	}
	
	public String updateBooking(int day, String confID, int slotOffset) throws IOException {
		Vector<Booking> bookings = this.daySchedule.get(day);
		
		Booking update = null;
		for(int i=0; i< bookings.size(); i++){
			if (bookings.get(i).getID().equals(confID)){
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
			if (!bookings.get(j).getID().equals(confID)){
				if (bookings.get(j).conflict(newStart, newEnd)){
					return "Invalid offset provided. Conflict with another booking.";
				}
			}
		}
		
		//proceed with update
		update.setStartSlot(newStart);
		update.setEndSlot(newEnd);
		triggerMonitors();
		return "Booking " + confID + " updated to " + slotToTime(newStart) + "-" + slotToTime(newEnd);		
	}
	
	public String shiftBookingDate(int day, int facId, String confID, int dayOffset) {
		
		int today = BookingUtils.getToday();
		int newDay = day + dayOffset;
		//check offset validity
		if (newDay < today){
			return "Unsuccessful! You cannot move your booking to a day that has already passed";
		} else if (newDay > 6){
			return "Unsuccessful! You can only book a day in the current week";
		}
		
		//check slots availability
		Vector<Booking> bookings = this.daySchedule.get(day);
		Iterator<Booking> itr = bookings.iterator();
		while(itr.hasNext()){
			Booking b = itr.next();			
			if (b.getID().equals(confID)){
				int bookingStart = b.getStartSlot();
				int bookingEnd = b.getEndSlot();
				
				//error if new day conflicts
				Vector<Booking> newDayBookings = this.daySchedule.get(newDay);
				for(int i=0; i< newDayBookings.size(); i++){
					if (newDayBookings.get(i).conflict(bookingStart, bookingEnd)) {
						return "Unsuccessful! Time conflict with: Booking #" +
								newDayBookings.get(i).getID() + " " + 
								slotToTime(newDayBookings.get(i).getStartSlot()) + " " + 
								slotToTime(newDayBookings.get(i).getEndSlot());
					}
			    }
				
				//update bookings
				Booking booking = new Booking(facId, newDay, bookingStart, bookingEnd);
				newDayBookings.add(booking);
				itr.remove();				
				triggerMonitors();
				return booking.getID();				
			}
		}
		return "Unsuccessful! Invalid booking ID provided."
	}
	
	public String cancelBooking(int day, String confID) throws IOException {
		Vector<Booking> bookings = this.daySchedule.get(day);
		Iterator<Booking> itr = bookings.iterator();
		while(itr.hasNext()){
			Booking b = itr.next();			
			if (b.getID().equals(confID)){
				itr.remove();
				triggerMonitors();
				return "Booking removed successfully; Booking #" +
				b.getID() + " " + slotToTime(b.getStartSlot()) + " " + slotToTime(b.getEndSlot());
			}
		}
		return "Operation failed; no such ID exists";
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

	public String parseAvailability(Vector<Integer> days) {
		String result = "Facility avilability for " + this.name + ":\n";
		
		for (int i=0; i<days.size(); i++){
			 Integer[] span = getAvailability(days.get(i));
			 
			 if (span.length >= 2) {
				 int tail;
				 result += BookingUtils.getString(days.get(i)) + ": ";
				 if (span[0] != 0) { //starts with free period
					 result += slotToTime(0) + "-" + slotToTime(span[0]) + " ";
				 }
				 tail = span[1];
				 for (int j=2; j<span.length; j+=2){
					 if (span[j] != tail) {
						 result += slotToTime(tail) + "-" + slotToTime(span[j]) + " "; 
					 }
					 tail = span[j+1]; //valid cuz they should come in pairs
				 }
				 if (tail < 47) {
					 result += slotToTime(tail) + "-" + slotToTime(47) + " "; 
					 
				 }
				
			 } else {
				 result += "No bookings yet for " + BookingUtils.getString(days.get(i));
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
	
	public void triggerMonitors() throws IOException{
		int today = BookingUtils.getToday();
		Vector<Integer> days = new Vector<Integer>();
		for (int i=today;i<7;i++){
			days.add(i);
		}
		
		String msg = parseAvailability(days);
		Iterator<Monitor> itr = this.monitors.iterator();
		while(itr.hasNext()){
			Monitor m = itr.next();
			if (m.expired()){
				itr.remove();
			} else {
				BookingUtils.notifyClient(msg, m.IP, m.port);
			}
		}
	}

}