package com.distributed.server;

import java.util.Vector;

public class Services {
	
	private Vector<Facility> facList;
	
	private Services(){
		;
	}
	
	public static Services getServices(){
		if(ref == null){
			ref = new Services();
		}
		
		return ref;
	}
	
	public void init(){
		//initialize List of Facilities
	}

	private static Services ref;
	
	public static void displayAvailability() {
	}
	
	public static int reserveFacility(){
		return 0;
	}
}
