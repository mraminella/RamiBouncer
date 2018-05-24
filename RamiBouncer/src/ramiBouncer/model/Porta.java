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
import java.util.Observable;


public class Porta extends Observable {
	/**
	 * 
	 */
	private Stato Stato;
	private int Numero;
	private String Nome;
	private LocalDateTime LastUpdate;
	private int updateInterval;
	private int numeroRiaccensioni;
	
	public LocalDateTime getLastUpdate() {
		return LastUpdate;
	}
	
	public String getNome() {
		return Nome;
	}

	public Porta(int numero, Stato stato) {
		super();
		this.Numero = numero;
		this.Stato = stato;
		this.Nome = "Porta" + numero;
		this.numeroRiaccensioni = 0;
	//	this.listeners = new ArrayList<PortaListener>();
		this.updateInterval = 5;
		this.LastUpdate = LocalDateTime.now();
	}
	
	public Porta(int numero) {
		this.Numero = numero;
		this.Nome = "Porta" + numero;
		this.Stato = ramiBouncer.model.Stato.chiuso;
		this.numeroRiaccensioni = 0;
	//	this.listeners = new ArrayList<PortaListener>();
		this.updateInterval = 5;
		this.LastUpdate = LocalDateTime.now();
	}
	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Stato getStato() {
		return this.Stato;
	}
	public void setStato(Stato stato) {
		if(this.Stato != ramiBouncer.model.Stato.disattivato) {
			this.LastUpdate = LocalDateTime.now();
			if(!(this.Stato == stato)) {
				if(this.Stato == ramiBouncer.model.Stato.spento) {
					this.numeroRiaccensioni++;
					if(this.numeroRiaccensioni > 5)
						this.Stato = ramiBouncer.model.Stato.disattivato;
					else this.Stato = stato;
				}
				else this.Stato = stato;
				
				setChanged();
			    notifyObservers();
			}
		}
	}
	public int getNumero() {
		return Numero;
	}
	public void setNumero(int numero) {
		Numero = numero;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (o == this) return true;
	    if (!(o instanceof Porta))return false;
	    if(((Porta) o).getNumero() == this.Numero) return true;
	    else return false;
	}
	
	public String toString() {
		return (this.Nome  + " " + this.Stato);
	}
}
