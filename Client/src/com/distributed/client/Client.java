package com.distributed.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;

public class Client {

	private static final int SO_TIMEOUT = 2000;
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
		
		int reqCtr = 0; //Global request counter for client session
		UUID clientID = UUID.randomUUID(); //Generate Unique ID for User/Session
		
		System.out.println("Welcome to the Facility Booking System!\n");
		while(true){
			System.out.println("You can access the following services:\n" +
					"[1] Check Availability of Facilities\n" +
					"[2] Book Facility\n" +
					"[3] Change Booking\n" +
					"[4] Monitor Facility Availability\n" +
					"[0] Exit\n" +
					"Please enter the corresponding number:");

			int choice = Integer.parseInt(br.readLine());
			
			if(choice == 0){
				System.out.println("Closing Facility Booking System.\n Thank You!");
				return;		//exit application
			}
			
			String msg = String.valueOf(choice);
			String command = null;
			switch(choice){
			case 1: 
				System.out.println("Enter name of facility followed by the required days: \n");
				String line = br.readLine();
				StringTokenizer tok = new StringTokenizer(line, " ");
				command = tok.nextToken(); //get facName
				while(tok.hasMoreTokens()){
					command = command + " " + BookingUtils.getDay(tok.nextToken());
				}
				reqCtr++;
				break;

			case 2:
				System.out.println("Enter name of facility, day and time(for e.g. SquashCourt Tuesday 1030-1230): \n");
				command = br.readLine();
				reqCtr++;
				break;

			case 3:
				System.out.println("Enter the confirmation ID of your booking and the offset(e.g.SQ10 0400 OR SQ10 -0400): \n");
				command = br.readLine();
				reqCtr++;
				break;

			case 4:
				System.out.println("Enter the name of facility and the monitor interval in hours: \n");
				command = br.readLine();
				reqCtr++;
				break;
			default:
				;

			}
			String reqID =clientID.toString() + Integer.toString(reqCtr);
			msg = reqID + " " + msg + " " + command;
			DatagramSocket sock = null;
			try {
				sock = new DatagramSocket();
				sock.setSoTimeout(SO_TIMEOUT);
				InetAddress host = InetAddress.getByName(args[1]);
				byte[] m = msg.getBytes(); //Marshalling
				int serverPort = 6789;
				DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
				sock.send(request);
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				
				//Timeout
				try{
					sock.receive(reply);
				} catch(SocketTimeoutException e){
					sock.send(request);
				}
				
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

}
