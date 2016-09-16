// SimpleEchoServer.java
// This class is the server side of a simple echo server based on
// UDP/IP. The server receives from a client a packet containing a character
// string, then echoes the string back to the client.
// Last edited January 9th, 2016

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class SimpleEchoServer {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendSocket, receiveSocket;
   
   private static byte[] READREQUEST = {0,3,0,1};
   private static byte[] WRITEREQUEST = {0,4,0,0};

   public SimpleEchoServer()
   {
      try {
         receiveSocket = new DatagramSocket(5000);
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }
   
   private String checkData(DatagramPacket receivePacket){
	   int j,k;
	   byte readWrite;
	   
	   int len = receivePacket.getLength();
	   byte[] data = receivePacket.getData();
	   
	   ByteBuffer fileName = ByteBuffer.allocate(len);
	   ByteBuffer mode = ByteBuffer.allocate(len);
	   
	   if(data[1]==0){ return "ERROR";}
	   else{ readWrite = data[1]; }
	    
	   //Start at index 2 - where filename should start
	   for(j=2;j<len;j++){
		   if(data[j]==0){
			   break;
		   }else{
			   fileName.put(data[j]);
		   }
	   }
	   
	   //Check if anything was pushed to filename array.
	   if(fileName.remaining()==len) return "ERROR";
	   
	   for(k=j+1;k<len;k++){
		   if(data[k]==0){
			   break;
		   }else{
			   mode.put(data[k]);
		   }
	   }
	   
	   //Check if anything was pushed to mode array.
	   if(mode.remaining()==len) return "ERROR";
	   //Validate there is nothing after the final 0.
	   if(k!=len-1) return "ERROR";
	   
	   //At this point, request type, filename and mode are within the data string.
	   if(readWrite==1){ return "READ"; }
	   else if(readWrite==2){ return "WRITE";}
	   else { return "ERROR"; }
	      
   }

   public void receiveAndEcho() throws Exception
   {
      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[100];
      receivePacket = new DatagramPacket(data, data.length);
      System.out.println("Server: Waiting for Packet.");

      // Block until a datagram packet is received from receiveSocket.
      try {        
         System.out.println("Waiting..."); // so we know we're waiting
         receiveSocket.receive(receivePacket);
      } catch (IOException e) {
         System.out.print("IO Exception: likely:");
         System.out.println("Receive Socket Timed Out.\n" + e);
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Server: Packet received:");
      int len = receivePacket.getLength();
      System.out.println("Containing: " );
      
      System.out.print("In bytes: ");
      for(int i=0;i<len;i++) System.out.print(data[i] + " ");
      
      System.out.print("\nString: ");      // Form a String from the byte array.
      String received = new String(data,0,len);   
      System.out.println(received);
      
      byte[] requestData = null;    
      String requestType;
      requestType = checkData(receivePacket);
		
	  if(requestType=="ERROR"){throw new Exception("Package error. Invalid request.");}
	  else if(requestType=="READ"){ requestData=READREQUEST;}
	  else { requestData=WRITEREQUEST;}
  
      sendPacket = new DatagramPacket(requestData, requestData.length,
                               receivePacket.getAddress(), receivePacket.getPort());

      System.out.println();
      System.out.println( "Server: Sending packet:");
      len = sendPacket.getLength();
      System.out.print("Containing bytes: ");
      for(int i=0;i<len;i++) System.out.print(sendPacket.getData()[i] + " ");
        
      // Send the datagram packet to the client via the send socket.
      sendSocket = new DatagramSocket();
      try {
         sendSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      
      System.out.println();
      System.out.println("Server: packet sent\n");


    // We're finished, so close the sockets.
     sendSocket.close();
   }

   public static void main( String args[] )
   {
	  SimpleEchoServer c = new SimpleEchoServer();	
	  while(true){
		  try {
			c.receiveAndEcho();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	  }
   }
}

