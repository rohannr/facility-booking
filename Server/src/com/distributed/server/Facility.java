package com.distributed.server;

import java.util.*;

public class Facility {

	private String name;
	private Vector<Vector<Booking>> daySchedule;

	public Facility(String name) {
		this.name = name;
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
		Booking booking = new Booking(facId, start, end);
		bookings.add(booking);
		return booking.getID();
	}


}