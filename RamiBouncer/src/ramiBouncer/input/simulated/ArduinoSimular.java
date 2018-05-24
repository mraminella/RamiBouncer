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
package ramiBouncer.input.simulated;

import java.util.Random;

import ramiBouncer.input.arduino.CasaInputParser;
import ramiBouncer.input.arduino.DatagramUploader;
import ramiBouncer.model.Casa;

public final class ArduinoSimular {
	
	public static final void Listen() {
		Thread t=new Thread() {
			public void run() {
				DatagramUploader parser = new DatagramUploader("127.0.0.1");
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try { 
					while (true) {
						Thread.sleep(1000); 
						Random r = new Random();
						StringBuilder sb = new StringBuilder();
						// Richiesta: C=2T=1=0

						sb.append("Richiesta: C=");
						sb.append(r.nextInt(5));
						sb.append("T=");
						sb.append(r.nextInt(3));
						sb.append("=");
						sb.append(r.nextInt(2));
						//System.out.println(sb.toString());
						parser.Parse(sb.toString());
					}
					} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}
