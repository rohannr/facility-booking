package com.distributed.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;

public class Client {

	private static final int SO_TIMEOUT = 1000;
	private static final int SERVER_PORT = 2222;
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
		boolean block = false;
		
		int reqCtr = 0; //Global request counter for client session
		UUID clientID = UUID.randomUUID(); //Generate Unique ID for User/Session
		long expiry=0;
		
		
		System.out.println("Welcome to the Facility Booking System!\n");
		while(true){
			System.out.println("You can access the following services:\n" +
					"[1] Check Availability of Facilities\n" +
					"[2] Book Facility\n" +
					"[3] Change Booking\n" +
					"[4] Monitor Facility Availability\n" +
					"[5] Cancel Booking\n" +
					"[6] Change day of Booking\n" +
					"[0] Exit\n" +
					"Please enter the corresponding number:");

			int choice = Integer.parseInt(br.readLine());
			
			if(choice == 0){
				System.out.println("Closing Facility Booking System.\n Thank You!");
				return;		//exit application
			}
			
			String msg = String.valueOf(choice);
			
			//Get command from user
			String line = "";
			StringTokenizer tok = null;
			
			String command = null;
			switch(choice){
			case 1: 
				System.out.println("Enter name of facility followed by the required days: \n");
				line = br.readLine();
				tok = new StringTokenizer(line, " ");
				command = Integer.toString(BookingUtils.getFacID((tok.nextToken())));  //get facilityID
				while(tok.hasMoreTokens()){
					command = command + " " + BookingUtils.getDay(tok.nextToken());
				}
				reqCtr++;
				break;

			case 2:
				System.out.println("Enter name of facility, day and time(for e.g. LT3 Tuesday 1030-1230): \n");
				line = br.readLine();
				tok = new StringTokenizer(line, " ");
				command = Integer.toString(BookingUtils.getFacID(tok.nextToken())); //facility
				command = command + " " + BookingUtils.getDay(tok.nextToken()); //day
				command = command + " " + tok.nextToken(); // times
				reqCtr++;
				break;

			case 3:
				System.out.println("Enter the confirmation ID of your booking and the offset(e.g. 251 0400 OR 1563 -0400): \n");
				command = br.readLine();
				reqCtr++;
				break;

			case 4:
				System.out.println("Enter the name of facility and the monitor interval as DHH: \n");
				line = br.readLine();
				tok = new StringTokenizer(line, " ");
				command = Integer.toString(BookingUtils.getFacID(tok.nextToken()));
				String interval = tok.nextToken();
				expiry = BookingUtils.expiryTime(interval);
				command = command + " " + interval;
				block = true;
				reqCtr++;
				break;
				
			case 5:
				System.out.println("Enter the confirmation ID of the booking to be cancelled: \n");
				command = br.readLine();
				reqCtr++;
				break;
			case 6:
				System.out.println("Enter the confirmation ID and the day offset: \n");
				reqCtr++;
				break;
			default:
				System.out.println("You entered an invalid command. Please try again.");
				continue;

			}
			String reqID =clientID.toString() + Integer.toString(reqCtr);
			msg = reqID + " " + msg + " " + command;
			DatagramSocket sock = null;
			try {
				sock = new DatagramSocket();
				InetAddress host = InetAddress.getByName(args[0]);
				byte[] m = msg.getBytes(); //Marshalling
				int serverPort = SERVER_PORT;
				DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
				sock.send(request);
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				
				
				if(block){
					int t = 0;
					System.out.println(retransmitUDPMessage(request, reply, sock));
					while(!BookingUtils.hasExpired(expiry)){
						
						do{
							try {
								sock.receive(reply);
								t=1;
							} catch(SocketTimeoutException e1){
								;
							}
						} while(t == 0);
						System.out.println(new String(reply.getData(), 0, reply.getLength()));
						t=0;
					}
				} else {
					String res = retransmitUDPMessage(request, reply, sock);
					System.out.println("Reply: " + res);
				}
				
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				sock.close();
				System.out.println("Socket Closed!" + e1);
			} catch(IOException e2) {
				e2.printStackTrace();
			}

			finally {
				if(sock != null){
					sock.close();
				}
			}
		}
	}

	/**
	 * Retransmits request until reply is received from server.
	 * Demonstrates At-Least-Once and At-Most-Once Semantics
	 * @param request
	 * @param reply
	 * @return
	 * @throws IOException 
	 */
	private static String retransmitUDPMessage(DatagramPacket request,
			DatagramPacket reply, DatagramSocket sock) throws IOException {
		sock.setSoTimeout(SO_TIMEOUT);
		// The following section demonstrates how the client will retransmit request until a reply is received.
		// This is in line with At-least-once and At-most-once invocation semantics.
		String ans = null;
		do {
			try{
				sock.receive(reply);
				ans = new String(reply.getData(), 0, reply.getLength());
			} catch(SocketTimeoutException e){
				System.out.println("Client timeout");
				ans = null;
				sock.send(request);
			}
		} while (ans == null);
		
		return ans;
		
	}

}
