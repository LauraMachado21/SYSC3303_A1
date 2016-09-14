import java.io.*;
import java.net.*;

public class SimpleEchoIntermediateHost {
	
	DatagramPacket receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	
	public SimpleEchoIntermediateHost(){	
		try{
			receiveSocket = new DatagramSocket(23);
			sendSocket = new DatagramSocket();
			
		}catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	    } 		
	}
	
	public void receiveAndEcho(){
	      byte data[] = new byte[100];
	      receivePacket = new DatagramPacket(data, data.length);
	      System.out.println("IntHost: Waiting for Packet.\n");
	      
	      try {        
	          System.out.println("Waiting..."); // so we know we're waiting
	          receiveSocket.receive(receivePacket);
	       } catch (IOException e) {
	          System.out.print("IO Exception: likely:");
	          System.out.println("Receive Socket Timed Out.\n" + e);
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Packet received:");
	      System.out.println("From host: " + receivePacket.getAddress());
	      System.out.println("Host port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: " );
	      
	      String received = new String(data,0,len);   
	      System.out.println(received + "\n");
	}

}
