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
	      
	      int len = clientReceivePacket.getLength();


	      System.out.println("IntHost: Packet received from client.");
	      System.out.print("Containing: \n");
	      System.out.println("In bytes: ");
	      for(int i=0;i<len;i++) System.out.print(data[i] + " ");
	      System.out.println();
	      System.out.print("As string: ");
	      System.out.print(new String(data,0,len) + "\n");  
	      
	      try {
	          serverSendPacket = new DatagramPacket(data, clientReceivePacket.getLength(),
	                                          InetAddress.getLocalHost(), 5000);
	       } catch (UnknownHostException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Sending Packet to server.");
	      System.out.print("Containing: \n");
	      System.out.println("In bytes: ");
	      for(int i=0;i<len;i++) System.out.print(serverSendPacket.getData()[i] + " ");
	      System.out.println();
	      System.out.print("As string: ");
	      System.out.print(new String(serverSendPacket.getData(),0,len) + "\n");  
	      
	      try {
	          serverSendReceiveSocket.send(serverSendPacket);
	       } catch (IOException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	      
	      System.out.println("IntHost: Packet Sent to server.\n");
	      
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
	      System.out.print("Containing: \n");
	      System.out.println("In bytes: ");
	      for(int i=0;i<serverReceivePacket.getLength();i++) System.out.print(serverReceivePacket.getData()[i] + " ");
	      System.out.println();
	      System.out.print("As string: ");
	      System.out.print(new String(serverReceivePacket.getData(),0,len) + "\n");  
	      
	      clientSendPacket = new DatagramPacket(serverReceivePacket.getData(), serverReceivePacket.getLength(),
	                                          clientReceivePacket.getAddress(), clientReceivePacket.getPort());
	      
	      System.out.println("IntHost: Sending Packet to client.");
	      System.out.print("Containing: \n");
	      System.out.println("In bytes: ");
	      for(int i=0;i<clientSendPacket.getLength();i++) System.out.print(clientSendPacket.getData()[i] + " ");
	      System.out.println();
	      System.out.print("As string: ");
	      System.out.print(new String(clientSendPacket.getData(),0,len) + "\n"); 
	      
	      
	      try {
	          clientSendSocket.send(clientSendPacket);
	       } catch (IOException e) {
	          e.printStackTrace();
	          System.exit(1);
	      } 
	      
	      System.out.println("IntHost: Packet Sent to client.\n");
	}
	
   public static void main( String args[] )
   {
	  SimpleEchoIntermediateHost c = new SimpleEchoIntermediateHost();	
	  while(true){
		  c.receiveAndEcho(); 
	  }

   }

}
