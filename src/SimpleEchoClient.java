// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

//5000=69 & 4001=23

import java.io.*;
import java.net.*;

public class SimpleEchoClient {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

   public SimpleEchoClient()
   {
      try {
         sendReceiveSocket = new DatagramSocket();
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }
   
   public void sendMessages(){
	   byte read = 1;
	   byte write = 2;
	   String fileName = "test.txt";
	   String mode = "ocTET";
	   
	   for(int i=0;i<10;i++){
		   System.out.println("Package: #" + i);
		   if(i%2==0){
			   sendAndReceive(read, fileName, mode);
		   }else{
			   sendAndReceive(write,fileName,mode);
		   }
	   }
	   
	   System.out.println("Package: #11");
	   sendAndReceive((byte) 3,fileName,mode);
	   sendReceiveSocket.close();

   }

   public void sendAndReceive(byte readwrite, String filename, String mode)
   {
 
	   byte zeroByte[] = new byte[1];
	   zeroByte[0] = 0;
	   
	   byte[] readWrite = new byte[1];
	   readWrite[0] = readwrite;
	   byte fileName[] = filename.getBytes();	   
	   byte modeName[] = mode.getBytes();
	   
	   byte[] msg = new byte[zeroByte.length 
	                         + readWrite.length
	                         + fileName.length
	                         + zeroByte.length
	                         + modeName.length
	                         +zeroByte.length];
	   
	   System.arraycopy(zeroByte, 0, msg, 0, zeroByte.length);
	   System.arraycopy(readWrite, 0, msg, zeroByte.length, readWrite.length);
	   System.arraycopy(fileName, 0, msg,(zeroByte.length+readWrite.length), fileName.length);
	   System.arraycopy(zeroByte, 0, msg, (zeroByte.length+readWrite.length+fileName.length), zeroByte.length);
	   System.arraycopy(modeName, 0, msg,(zeroByte.length+readWrite.length+fileName.length+zeroByte.length), modeName.length);
	   System.arraycopy(zeroByte, 0, msg,(zeroByte.length+readWrite.length+fileName.length+zeroByte.length+modeName.length), zeroByte.length);
      
      try {
         sendPacket = new DatagramPacket(msg, msg.length,
                                         InetAddress.getLocalHost(), 4001);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
      System.out.print("Containing: \n");
      System.out.println("In bytes: ");
      for(int i=0;i<sendPacket.getLength();i++) System.out.print(sendPacket.getData()[i] + " ");
      System.out.println();
      System.out.print("As string: ");
      System.out.print(new String(sendPacket.getData(),0,sendPacket.getLength()) + "\n"); 

      // Send the datagram packet to the server via the send/receive socket. 

      try {
         sendReceiveSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Packet sent.\n");

      // Construct a DatagramPacket for receiving packets up 
      // to 100 bytes long (the length of the byte array).

      byte data[] = new byte[100];
      receivePacket = new DatagramPacket(data, data.length);

      try {
         // Block until a datagram is received via sendReceiveSocket.  
         sendReceiveSocket.receive(receivePacket);
      } catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      // Process the received datagram.
      System.out.println("Client: Packet received:");
      int len = receivePacket.getLength();
      System.out.println("Containing: " );
      
      System.out.print("In bytes: ");
      for(int i=0;i<len;i++) System.out.print(receivePacket.getData()[i] + " ");
      
      System.out.print("\nString: ");      // Form a String from the byte array.
      System.out.println(new String(receivePacket.getData(),0,len));
      System.out.println();

      // We're finished, so close the socket.
	  
   }

   public static void main(String args[])
   {
      SimpleEchoClient c = new SimpleEchoClient();
      c.sendMessages();
   }
}