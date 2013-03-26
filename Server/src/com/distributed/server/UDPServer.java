package com.distributed.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

		boolean atMostOnce = args[0].equalsIgnoreCase("atm") ? true : false;
		HashMap<String, String> requestHistory = null; //Table of Request IDs and their corresponding replies.
		String facName;
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
					replyMsg.concat(requestHistory.get(reqID));
				} else {
					int servNum = Integer.parseInt(sc.next());
					switch(servNum){

					case 1: 
						facName = sc.next();		
						Vector<Object> daysToCheck = new Vector<Object>();
						while(sc.hasNext()){
							String token = sc.next();
							int day = Integer.parseInt(token);
							daysToCheck.add(token);
						}
						System.out.println(facName + " requested for " + daysToCheck);
						replyMsg = "reqID: " + reqID + "\n" + facName + " requested for " + daysToCheck;
						Services.displayAvailability(); //implement
						break;

					case 2:
//						int facId = Integer.parseInt(sc.next());
//						int dayOfWeek = Integer.parseInt(sc.next());
//						int startTime = Integer.parseInt(sc.next());
//						int endTime = Integer.parseInt(sc.next());
//						services.reserveFacility(facId, dayOfWeek, startTime, endTime); //implement

					default:
						;
					}
				}
				byte[] replyPacket = replyMsg.getBytes();

				DatagramPacket reply = new DatagramPacket(replyPacket, replyPacket.length, request.getAddress(), request.getPort());
				sock.send(reply);
			}
		}

		finally {
			if (sock != null){
				sock.close();
			}
		}

	}

}
