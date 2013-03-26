package com.distributed.server;

import java.util.*;

public class Services {

	private Vector<Facility> facList = new Vector<Facility>();
	private static Services ref = new Services();

	private Services(){
		;
	}

	public static Services getServices(){
		return ref;
	}

	public void init(){
		//initialize List of Facilities
		ref.facList.add(new Facility("LEC1"));
		ref.facList.add(new Facility("LEC2"));
		ref.facList.add(new Facility("LEC3"));
			
	}	
	
	public static String getAvailability(int fac, Vector<Integer> days) {
		//does not check out of range
		Facility facility = ref.facList.get(fac);
		return facility.parseAvailability(days) ;
	}

	public static int reserveFacility(int fac, int day, int startTime, int endTime){
		//does not check out of range
		Facility facility = ref.facList.get(fac);
		return facility.book(fac, day, getSlot(startTime), getSlot(endTime));
	}
	
	public static String updateBooking(int confID, String offset){
		// does not validate offset
//		confID
		return "";
	}
	
	
	public static int getSlot(int hhmm) {
		String time = Integer.toString(hhmm);
		
		String hh = time.substring(0, 1);
		int hr = Integer.parseInt(hh);
		
		if (time.endsWith("00")) {
			return 2 * hr;
		} else {
			return 2 * hr + 1;
		}
	}
}