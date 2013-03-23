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
		
		DatagramSocket sock = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		try {
			sock = new DatagramSocket(6789);
			byte[] buffer = new byte[1000];
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				sock.receive(request);
				String msg = new String(request.getData());
				StringTokenizer tok = new StringTokenizer(msg);
				int service = Integer.parseInt(tok.nextToken());
				
				switch(service){

				case 1: 
					String facName = tok.nextToken();
					Vector<Date> daysToCheck = new Vector<Date>();
					while(tok.hasMoreElements()){
						daysToCheck.add((Date)df.parseObject(tok.nextToken()));
					}
					System.out.println(facName + " requested for " + daysToCheck);


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
