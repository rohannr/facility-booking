package com.distributed.server;

import java.util.Map;

public class BookingUtils {

	private BookingUtils(){
		;
	}
	
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
		}else if(day.equalsIgnoreCase("Friday")){
			return 6;
		} else {
			return -1;
		}
	}
}
