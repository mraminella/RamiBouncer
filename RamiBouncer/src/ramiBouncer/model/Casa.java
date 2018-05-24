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

import java.util.*;

public class Casa extends Observable implements Observer {
	/**
	 * 
	 */
	//private List<SensoreListener> listeners;
	private Set<Sensore> sensori;
	private Portiniere portiniere;
	private String nome;
	
	public Casa(String nome, int updateTolerance) {
		this.nome = nome;
		sensori = new HashSet<Sensore>();
		portiniere = new Portiniere(updateTolerance);
		portiniere.Controlla(this);
		//this.listeners = new ArrayList<SensoreListener>();
	}

	public Portiniere getPortiniere() {
		return portiniere;
	}

	public String getNome() {
		return this.nome;
	}
	public boolean addSensore(Sensore sensore) {
		for(Sensore s : sensori)
			if(s.equals(sensore)) return false;
		sensore.addObserver(this);
		return sensori.add(sensore);
	}
	
	public boolean removeSensore(Sensore sensore) {
		return sensori.remove(sensore);
	}
	
	public Sensore getSensore(Sensore sensore) {
		for(Sensore s : sensori)
			if(s.equals(sensore)) return s;
		return null;
	}
	
	public String printSensori() {
		String output = new String();
		Iterator<Sensore> iterator = sensori.iterator();
		while(iterator.hasNext()) {
			output += iterator.next().toString();
		}
		return output;
	}
	
	/* Sostituzione con Observer di Java
	public void AddNotifier(SensoreListener n) {
		
		listeners.add(n);
		for(Sensore s : Sensori)
			s.addListener(n);
		
			
	}
	*/
	public Set<Sensore> getSensori() {
		return sensori;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Porta) {
		setChanged();
		notifyObservers(arg);
		}
		
	}

	public String printPorte() {
		String result = new String();
		for(Sensore sensore : this.getSensori())
			for (Porta porta : sensore.getPorte())
				result += porta.toString() + '\n';
		return result;
	}
}
