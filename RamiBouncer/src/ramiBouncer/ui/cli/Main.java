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
package ramiBouncer.ui.cli;

import ramiBouncer.input.arduino.*;
import ramiBouncer.input.simulated.ArduinoSimular;
import ramiBouncer.model.*;
import ramiBouncer.persistence.SensoriPersister;
import ramiBouncer.ui.telegram.RamiBouncerBotAdapter;

public class Main {
	
	private static final int saveInterval = 30000;
	
	public static void main(String[] args) throws Exception {
		
		
		//Casa casa = new Casa("casa");
		// Portiniere notifier = new Portiniere();
		// casa.AddNotifier(notifier);	
		// notifier.Controlla(CasaManager.getCasa());
		
		
		/*
		System.out.println("Seleziona modalità: R = Reale S = Simulato");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();
		if(s.contains("R")) {
			ArduinoSerial arduino = new ArduinoSerial(casa);
			arduino.Listen();
		}
		
		else if(s.contains("S")) {
			ArduinoSimular.Listen(casa);
		}
		
	}
	*/
		if(args.length > 0) {
			if(args[0].contains("S")) {
				System.out.println("Modalità remota simulata");
				ArduinoSimular.Listen();
		}
			if(args[0].contains("R")) {
				System.out.println("Modalità reale con arduino");
				CasaInputParser ip = new CasaInputParser(CasaManager.getCasa());
				ArduinoSerial arduino = new ArduinoSerial(ip);
				arduino.Listen();
			}
			if(args[0].contains("T")) {
				System.out.println("Attivazione bot telegram");
			//	RamiBouncerBot bot = RamiBouncerBot.getInstance();
				RamiBouncerBotAdapter botadapter = new RamiBouncerBotAdapter();
				CasaManager.getCasa().addObserver(botadapter);
				
			}
			if(args[0].contains("H")) {
				System.out.println("Modalità server remoto, indirizzo cliente: " + args[1]);
				DatagramUploader uploader = new DatagramUploader(args[1]);
				ArduinoSerial arduino = new ArduinoSerial(uploader);
				arduino.Listen();
			}
			if(args[0].contains("C")) {
				System.out.println("Modalità client remoto, indirzizo server: ");
				DatagramReceiver receiver = new DatagramReceiver(CasaManager.getCasa());
				receiver.Listen();
			}
		}
		
		
		else {
			System.out.println("Modalità terminale simulata");
			ArduinoSimular.Listen();
		
		}
	
		keepPersistence(CasaManager.getCasa());
	}
	
	private static synchronized void keepPersistence(Casa casa) {
		Thread t=new Thread() {
			public void run() {
				try { 	
					while (true) {
						Thread.sleep(saveInterval);
						SensoriPersister.writeCasa(casa);
					}
	
				} catch (InterruptedException ie) {}
			}
		};
		t.start();
	}
	
}
