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
package ramiBouncer.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Portiniere  {
	
	private int updateTolerance;
	private static final int updateInterval = 10000;
	
	public int getUpdateTolerance() {
		return updateTolerance;
	}

	public Portiniere(int updateTolerance) {
		this.updateTolerance = updateTolerance;
	}
	
	public synchronized void Controlla(Casa casa) {
		Thread t=new Thread() {
			public void run() {
				try { 	
					while (true) {
						Thread.sleep(updateInterval);
						checkPorteSpente(casa);
					}

				} catch (InterruptedException ie) {}
			}
		};
		t.start();
	}

	public String printPorteSpente(Casa casa) {
		StringBuilder sb = new StringBuilder();
		for(Porta porta : checkPorteSpente(casa)) {
			sb.append("Porta spenta: ");
			sb.append(porta.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private List<Porta> checkPorteSpente(Casa casa) {
		List<Porta> porteSpente = new ArrayList<Porta>();
		LocalDateTime now = LocalDateTime.now();
		for(Sensore sensore : casa.getSensori())
			for(Porta porta : sensore.getPorte())
			{
				if(!porta.getStato().equals(Stato.spento) && porta.getLastUpdate().plusSeconds(porta.getUpdateInterval() + updateTolerance).isBefore(now)){
					porteSpente.add(porta);
					porta.setStato(Stato.spento);
				}
			}
		return porteSpente;
	}
	

}
