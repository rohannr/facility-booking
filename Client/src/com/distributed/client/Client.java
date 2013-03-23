package com.distributed.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket();
			byte[] m = args[0].getBytes();
			InetAddress host = InetAddress.getByName(args[1]);
			
			int serverPort = 6789;
			DatagramPacket request = new DatagramPacket(m, m.length, host, serverPort);
			sock.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			sock.receive(reply);
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
