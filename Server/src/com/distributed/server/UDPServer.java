package com.distributed.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;


public class UDPServer {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		
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
				String msg = new String(request.getData());
				StringTokenizer tok = new StringTokenizer(msg, " -");
				int servNum = Integer.parseInt(tok.nextToken());
				
				switch(servNum){

				case 1: 
					facName = tok.nextToken();		
					Vector<Integer> daysToCheck = new Vector<Integer>();
					while(tok.hasMoreElements()){
						daysToCheck.add(BookingUtils.getDay(tok.nextToken()));
					}
					System.out.println(facName + " requested for " + daysToCheck);
					services.displayAvailability(); //implement
					break;
				
				case 2:
					facName = tok.nextToken();
					String dayOfWeek = tok.nextToken();
					String startTime = tok.nextToken();
					String endTime = tok.nextToken();
					services.reserveFacility(); //implement
					
				default:
					;
				}
				
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());
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
