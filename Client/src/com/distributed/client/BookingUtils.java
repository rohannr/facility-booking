package com.distributed.client;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BookingUtils {

	private static final String[] facilityNames= new String[]{ "LT1", "LT2", "LT3", "LT3", "LT4", "LT5", "LT6", "LT7", "LT8", "LT8", "LT9", "LT10", "TR1", "TR2", "TR3", "TR4", "TR5", "SQ1", "SQ2", "TENNIS1", "TENNIS2", "TENNIS3" };

	private static final List<String> facList = Arrays.asList(facilityNames);
	
	private BookingUtils(){
		;
	}
	/**
	 * Returns number corresponding to the day of the week
	 * @param day
	 * @return int
	 */
	public static int getDay(String day){
		if(day.equalsIgnoreCase("Monday")){
			return 0;
		} else if(day.equalsIgnoreCase("Tuesday")){
			return 1;
		} else if(day.equalsIgnoreCase("Wednesday")){
			return 2;
		} else if(day.equalsIgnoreCase("Thursday")){
			return 3;
		} else if(day.equalsIgnoreCase("Friday")){
			return 4;
		} else if(day.equalsIgnoreCase("Saturday")){
			return 5;
		}else if(day.equalsIgnoreCase("Sunday")){
			return 6;
		} else {
			return -1;
		}
	}
	
	public static int getFacID(String faculty){
		return facList.indexOf(faculty.toUpperCase());
	}
	
	public static long expiryTime(String interval){
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		
		long days = 86400000 * Integer.parseInt(interval.substring(0,1));
		long hours = 3600000 * Integer.parseInt(interval.substring(1,3));
		
		long expiry = millis + days + hours;
		return expiry;
	}
	
	public static boolean hasExpired(long expiry){
		Calendar calendar = Calendar.getInstance();
		long today = calendar.getTimeInMillis();
		return today > expiry;
	}
}
