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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;
	
public class ArduinoSerial implements SerialPortEventListener {

			InputParser parser;
	
			SerialPort serialPort;
		        /** The port we're normally going to use. */
			private static final String PORT_NAMES[] = { 
					"/dev/tty.usbserial-A9007UX1", // Mac OS X
		                        "/dev/ttyACM0", "/dev/serial/by-id/usb-Arduino__www.arduino.cc__0043_85430343238351600220-if00\n",
		                        "/dev/ttyACM1", "/dev/ttyACM2",// Raspberry Pi
					"/dev/ttyUSB0", // Linux
					"COM3", // Windows
			};
			/**
			* A BufferedReader which will be fed by a InputStreamReader 
			* converting the bytes into characters 
			* making the displayed results codepage independent
			*/
			private BufferedReader input;
			/** The output stream to the port */
			@SuppressWarnings("unused")
			private OutputStream output;
			/** Milliseconds to block while waiting for port open */
			private static final int TIME_OUT = 2000;
			/** Default bits per second for COM port. */
			private static final int DATA_RATE = 57600;
	
			
			public ArduinoSerial(InputParser parser) {
				this.parser = parser;
			       // the next line is for Raspberry Pi and 
	            // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
	            System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
	
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}
	
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
	
			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
	
			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
	
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
		

		/**
		 * This should be called when you stop using the port.
		 * This will prevent port locking on platforms like Linux.
		 */
		public synchronized void close() {
			if (serialPort != null) {
				serialPort.removeEventListener();
				serialPort.close();
			}
		}

		/**
		 * Handle an event on the serial port. Read the data and print it.
		 */
		public synchronized void serialEvent(SerialPortEvent oEvent) {
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String inputLine=input.readLine();
					System.out.println(System.currentTimeMillis()+inputLine);
					parser.Parse(inputLine);
				} catch (Exception e) {
					System.err.println(e.toString());
				}
			}
			// Ignore all the other eventTypes, but you should consider the other ones.
		}

		public void Listen() {
			Thread t=new Thread() {
				public void run() {
					//the following line will keep this app alive for 1000 seconds,
					//waiting for events to occur and responding to them (printing incoming messages to console).
					try { while (true) Thread.sleep(1000000); } catch (InterruptedException ie) {}
				}
			};
			t.start();
			System.out.println("Started");
		}
		
}
