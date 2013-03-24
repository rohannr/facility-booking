package com.distributed.server;

import java.util.HashMap;
import java.util.Vector;

public class Facility {

	private String name;
	private  HashMap<Integer, Vector<Booking>> daySchedule;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
