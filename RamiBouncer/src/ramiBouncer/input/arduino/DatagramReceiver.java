/*
*    RamiBouncer remote notifying sensor network
*
*    Copyright 2018 Marco Raminella.
*    
*    This file is part of RamiBouncer.
*
*    RamiBouncer is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    RamiBouncer is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with RamiBouncer.  If not, see <http://www.gnu.org/licenses/>.
* 
*/
package ramiBouncer.input.arduino;

import java.io.*;
import java.net.*;

import ramiBouncer.model.Casa;

public class DatagramReceiver extends CasaInputParser {
	private static final int port = 22570;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    private byte[] buf = new byte[256];


	public DatagramReceiver(Casa casa) {
		super(casa);
	}
	
	public void Listen() {
		Thread t=new Thread() {
			public void run() {
				
				 try {
					  packet = new DatagramPacket(buf, buf.length);
			          socket = new DatagramSocket(port);
			          socket.setSoTimeout(30000);
			          
			          System.out.println("\nClient: avviato");
			          System.out.println("Creata la socket: " + socket);
				    } catch (SocketException e) {
				    	 System.out.println("Problemi nella creazione della socket: ");
				         e.printStackTrace();
				         System.out.println("Client: interrompo...");
				         System.exit(1);
					}
				 
				 
				while (true) {
		          try {
		        	//packet.setData(buf);
		            socket.receive(packet);
					String risposta = new String (packet.getData(), 0, packet.getLength());
					System.out.println("UDP: "+risposta);
					if(risposta.length() > 0)
						Parse(risposta);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		};
		t.start();
		System.out.println("Started");
	}

}
