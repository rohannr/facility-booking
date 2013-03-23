package com.distributed.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

public class Client {

	String facName;
	int service;
	int UID;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException, ParseException {
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		
		System.out.println("Welcome to the Facility Booking System!\n" +
				"You can access the following services:\n" +
				"[1] Check Availability of Facilities\n" +
				"[2] Book Facility\n" +
				"[3] Change Booking\n" +
				"[4] Monitor Facility Availability\n" +
				"Please enter the corresponding number:");
		
		int choice = Integer.parseInt(br.readLine());
		String msg = String.valueOf(choice);
		switch(choice){
		case 1: 
			System.out.println("Enter name of facility followed by the required days: \n");
			String line = br.readLine();
			
			
			msg = msg + " " + line;
			break;
			
		default:
			;
			
		}
		
		
		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket();
			InetAddress host = InetAddress.getByName(args[1]);
			byte[] m = msg.getBytes(); //Marshalling lol
			int serverPort = 6789;
			DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
			sock.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			System.out.println("Reply: " + new String(reply.getData()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e.getMessage() + " " + e.getCause());
		}
		
		finally {
			if(sock != null){
				sock.close();
			}
		}

	}

}
