package com.distributed.server;

import java.net.InetAddress;
import java.util.Calendar;

public class Monitor {
	private long expiry;
	public InetAddress IP;
	public int port;
	
	public Monitor(long expiry, InetAddress IP, int port) {
		this.expiry = expiry;
		this.IP = IP;
		this.port = port;
	}
	
	public boolean expired() {
		Calendar calendar = Calendar.getInstance();
		long now = calendar.getTimeInMillis();
		
		return now > this.expiry;
	}
}