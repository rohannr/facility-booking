package com.distributed.server;

import java.util.*;

public class Services {

	private Vector<Facility> facList = new Vector<Facility>();
	private static final String[] facilityNames= new String[]{ "LT1", "LT2", "LT3", "LT3", "LT4", "LT5", "LT6", "LT7", "LT8", "LT8", "LT9", "LT10", "TR1", "TR2", "TR3", "TR4", "TR5", "SQ1", "SQ2", "TENNIS1", "TENNIS2", "TENNIS3" };
	private static Services ref = new Services();

	private Services(){
		;
	}

	public static Services getServices(){
		return ref;
	}

	public void init(){
		//initialize List of Facilities
		for (String name : facilityNames){
			ref.facList.add(new Facility(name));
		}
	}	
	
	public static String getAvailability(int fac, Vector<Integer> days) {
		//does not check out of range
		Facility facility = ref.facList.get(fac);
		return facility.parseAvailability(days) ;
	}

	public static int reserveFacility(int fac, int day, String startTime, String endTime){
		//does not check out of range
		Facility facility = ref.facList.get(fac);
		return facility.book(fac, day, getSlot(startTime), getSlot(endTime));
	}
	
	public static String updateBooking(int confID, String offset){
		// does not validate offset
        String id = Integer.toString(confID);
		int facID = Integer.parseInt(id.substring(0,1));
        int day = Integer.parseInt(id.substring(1,2));
        Facility facility = ref.facList.get(facID);
        
        int slotOffset;
        if (offset.length() == 4) {
            slotOffset = getSlot(offset);
        } else {
            slotOffset = getSlot(offset.substring(1));
            slotOffset *= -1;
        }
        
        return facility.updateBooking(day, confID, slotOffset);
	}   
	
	
	public static int getSlot(String hhmm) {		
		String hh = hhmm.substring(0, 2);
		int hr = Integer.parseInt(hh);
		
		if (hhmm.endsWith("00")) {
			return 2 * hr;
		} else {
			return 2 * hr + 1;
		}
	}
}