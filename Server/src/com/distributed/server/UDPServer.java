package com.distributed.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */




	public static void main(String[] args) throws IOException, ParseException {

		boolean atMostOnce = args[0].equalsIgnoreCase("atm") ? true : false;  //At most once Invocation semantics activated
		HashMap<String, String> requestHistory = new HashMap<String, String>(); //Table of Request IDs and their corresponding replies.
		int facID;
		Services services = Services.getServices();
		services.init();
		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket(6789);
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
						replyMsg = "reqID: " + reqID + "\n" + facID + " requested for " + daysToCheck;
						System.out.println(Services.getAvailability(facID, daysToCheck));
						requestHistory.put(reqID, replyMsg);
						//						Services.getAvailability(null,null); //implement
						break;

					case 2:
						//						int facId = Integer.parseInt(sc.next());
						//						int dayOfWeek = Integer.parseInt(sc.next());
						//						String startTime = sc.next();
						//						String endTime = sc.next();
						//						services.reserveFacility(facId, dayOfWeek, startTime, endTime); //implement

					default:
						;
					}

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

}
