import java.io.*;
import java.net.*;

public class SimpleEchoIntermediateHost {
	
	DatagramPacket clientReceivePacket, clientSendPacket, serverReceivePacket, serverSendPacket;
	DatagramSocket clientSendSocket, clientReceiveSocket, serverSendReceiveSocket;
	
	public SimpleEchoIntermediateHost(){	
		try{
			clientReceiveSocket = new DatagramSocket(4001);
			clientSendSocket = new DatagramSocket();
			serverSendReceiveSocket = new DatagramSocket();
//			serverReceiveSocket = new DatagramSocket();
			
			
		}catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	    } 		
	}
	
	public void receiveAndEcho(){
	      byte data[] = new byte[100];
	      clientReceivePacket = new DatagramPacket(data, data.length);
	      System.out.println("IntHost: Waiting for Packet.");
	      
	      try {        
	          System.out.println("Waiting... \n"); // so we know we're waiting
	          clientReceiveSocket.receive(clientReceivePacket);
	       } catch (IOException e) {
	          System.out.print("IO Exception: likely:");
	          System.out.println("Receive Socket Timed Out.\n" + e);
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
//	      System.out.println("IntHost: Sending Packet:");
//	      System.out.println("From host: " + receivePacket.getAddress());
//	      System.out.println("Host port: " + receivePacket.getPort());
//	      int len = receivePacket.getLength();
//	      System.out.println("Length: " + len);
//	      System.out.print("Containing: " );
	      
//	      String received = new String(data,0,len);   
//	      System.out.println(received + "\n");
	      System.out.println("IntHost: Packet received from client.");
	      
	      try {
	          serverSendPacket = new DatagramPacket(data, clientReceivePacket.getLength(),
	                                          InetAddress.getLocalHost(), 5000);
	       } catch (UnknownHostException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Sending Packet to server.");
	      
	      try {
	          serverSendReceiveSocket.send(serverSendPacket);
	       } catch (IOException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Packet Sent to server.");
	      
	      serverReceivePacket = new DatagramPacket(data, data.length);
	      
	      try {        
	          System.out.println("Waiting..."); // so we know we're waiting
	          serverSendReceiveSocket.receive(serverReceivePacket);
	       } catch (IOException e) {
	          System.out.print("IO Exception: likely:");
	          System.out.println("Receive Socket Timed Out.\n" + e);
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Packet received from server.");
	      
	      clientSendPacket = new DatagramPacket(data, clientReceivePacket.getLength(),
	                                          clientReceivePacket.getAddress(), clientReceivePacket.getPort());
	      
	      System.out.println("IntHost: Sending Packet to server.");
	      
	      try {
	          clientSendSocket.send(clientSendPacket);
	       } catch (IOException e) {
	          e.printStackTrace();
	          System.exit(1);
	      }  
	}
	
   public static void main( String args[] )
   {
	  SimpleEchoIntermediateHost c = new SimpleEchoIntermediateHost();	
	  while(true){
		  c.receiveAndEcho(); 
	  }

   }

}
