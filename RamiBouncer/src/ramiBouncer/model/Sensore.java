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

public class Sensore extends Observable implements Observer {
	/**
	 * 
	 */
	private int numero;
	private Set<Porta> porte;
	private String nome;
	// private List<SensoreListener> listeners;
	
	
	public Sensore(int numero) {
		super();
		this.numero = numero;
		this.nome = "Sensore " + numero;
		porte = new HashSet<Porta>();
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Porta> getPorte() {
		return porte;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public boolean addPorta(Porta porta) {
		for(Porta p : porte)
			if(p.equals(porta)) return false;
		/*
		for(SensoreListener listener : listeners) {
			listener.PortaAggiunta(this, porta);
			porta.addListener(this);
			}
			*/
		porta.addObserver(this);
		return porte.add(porta);

	}
	
	public Porta getPorta(Porta porta) {
		for(Porta p : porte)
			if(p.equals(porta)) return p;
		return null;
	}
	
	public boolean removePorta(Porta porta) {
		return porte.remove(porta);
		
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Sensore))return false;
	    if(((Sensore) o).getNumero() == this.numero) return true;
	    else return false;
	}
	
	
	@Override
	public String toString() {
		String output = new String();
		output +=  this.nome + "ID: "+ this.numero;
		return output;
	}
	
	public String printPorte() {
		Iterator<Porta> iterator = porte.iterator();
		String output = new String();
		while(iterator.hasNext()) {
			output += iterator.next().toString();
			output += "\n";
		}
		return output;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Porta) {
			setChanged();
			notifyObservers(o);
		}
		
	}
}
