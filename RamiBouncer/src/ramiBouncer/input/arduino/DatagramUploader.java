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


public class DatagramUploader implements InputParser {
	private InetAddress address = null;
	private static final int port = 22570;
    private byte[] data = null;
    private DatagramSocket socket = null;
    private DatagramPacket packet = null;

	
	public DatagramUploader(String addr) {
		super();
		 
		try {
			 address = InetAddress.getByName(addr);
	          socket = new DatagramSocket();
	          socket.setSoTimeout(30000);

	          System.out.println("\nServer: avviato");
	          System.out.println("Creata la socket: " + socket);

		    } catch (UnknownHostException e) {
		      System.out
		          .println("Problemi nella determinazione dell'endpoint del server : ");
		      e.printStackTrace();
		      System.out.println("Server: interrompo...");
		      System.exit(2);
		    } catch (SocketException e) {
		    	 System.out.println("Problemi nella creazione della socket: ");
		         e.printStackTrace();
		         System.out.println("Server: interrompo...");
		         System.exit(1);
			}
	}


	@Override
	public void Parse(String string) {
		        // riempimento e invio del pacchetto
		        try {
		          data = string.getBytes();
		          packet = new DatagramPacket(data, data.length, address, port);
		          socket.send(packet);
		          System.out.println("Richiesta inviata a " + address + ", " + port);
		        } catch (IOException e) {
		          System.out.println("Problemi nell'invio della richiesta: ");
		          e.printStackTrace();
		          System.out
		              .print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti RICHIESTA AL SERVER: ");
		        }		    
		    
	}
	
	public void close () {
		System.out.println("Server: termino...");
	    socket.close();
	}

}
