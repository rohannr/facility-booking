package com.distributed.server;

import java.util.Map;

public class BookingUtils {

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
		}else if(day.equalsIgnoreCase("Friday")){
			return 6;
		} else {
			return -1;
		}
	}
    
    public static String getString(int day){
        if (day == 0){
            return "Monday";
        } else if (day == 1) {
            return "Tuesday";
        } else if (day == 2) {
            return "Wednesday";
        } else if (day == 3) {
            return "Thursday";
        } else if (day == 4) {
            return "Friday";
        } else if (day == 5) {
            return "Saturday";
        } else if (day == 6) {
            return "Sunday":
        } else {
            return "invalid"
        }
    }
        
        
        
}
