package com.distributed.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;


public class UDPServer {

	private static final int SERVER_PORT_NO = 6789;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */



	public static DatagramSocket sock = null;

	public static void main(String[] args) throws IOException, ParseException {

		boolean atMostOnce = args[0].equalsIgnoreCase("atm") ? true : false;  //At most once Invocation semantics activated
		HashMap<String, String> requestHistory = new HashMap<String, String>(); //Table of Request IDs and their corresponding replies.
		int facID;
		Services services = Services.getServices();
		services.init();
		try {
			sock = new DatagramSocket(SERVER_PORT_NO);
			byte[] buffer = new byte[1000];
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				sock.receive(request);
				String msg = new String(request.getData(), 0, request.getLength()); //Unmarshalling
				Scanner sc = new Scanner(msg);
				sc.useDelimiter(" ");

				String reqID = sc.next();
				String replyMsg = "";

				if(requestHistory != null && requestHistory.containsKey(reqID) && atMostOnce){
					//No Need to execute message again. Simply resend the reply.
					replyMsg = requestHistory.get(reqID);
					System.out.println("Duplicate Reply Filtered: " + requestHistory.get(reqID));
					byte[] replyPacket = replyMsg.getBytes();
					DatagramPacket reply = new DatagramPacket(replyPacket, replyPacket.length, request.getAddress(), request.getPort());
					sock.send(reply);
					System.out.println("Reply sent!");
				} else {
					int servNum = Integer.parseInt(sc.next());
					switch(servNum){

					case 1: 
						facID = Integer.parseInt(sc.next());	
						Vector<Integer> daysToCheck = new Vector<Integer>();
						while(sc.hasNext()){
							String token = sc.next();
							int day = Integer.parseInt(token);
							daysToCheck.add(day);
						}
						replyMsg = services.getAvailability(facID, daysToCheck);
						break;

					case 2:
						facID = Integer.parseInt(sc.next());
						int dayOfWeek = Integer.parseInt(sc.next());
						String[] times = sc.next().split("-"); //split into start and end times
						replyMsg = services.reserveFacility(facID, dayOfWeek, times[0], times[1]); 
						break;
						
					case 3:
						int confID = Integer.parseInt(sc.next());
						String offset = sc.next();
						replyMsg = services.updateBooking(confID, offset);
						break;
						
					case 4:
						facID = Integer.parseInt(sc.next());
						String interval = sc.next();
						InetAddress clientIP = request.getAddress();
						int clientPort = request.getPort();
						services.monitorFacility(facID, interval, clientIP, clientPort);
						replyMsg = "The requested facility is now being monitored!";
					default:
						;
					}

					if(atMostOnce)
						requestHistory.put(reqID, replyMsg);      //Update request history so as not to re-execute requests
					
					byte[] replyPacket = replyMsg.getBytes();

					DatagramPacket reply = new DatagramPacket(replyPacket, replyPacket.length, request.getAddress(), request.getPort());
					Random rand = new Random();

					if(rand.nextInt(5) > 2){
						System.out.println("Reply sent!");
						sock.send(reply);
					} else {
						System.out.println("Message Lost");
					}
					
				}
			}
		}

		finally {
			if (sock != null){
				sock.close();
			}
		}

	}

	/**
	 * Called when any new booking or update is made. 
	 * Used to notify a monitoring client when its interval becomes available
	 * @param sendPacket - The Datagram packet containing the message that is to be sent
	 */
	public static void sendNotification(DatagramPacket sendPacket) {
			try {
				sock.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
