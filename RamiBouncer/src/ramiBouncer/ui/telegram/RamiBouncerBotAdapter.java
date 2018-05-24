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
package ramiBouncer.ui.telegram;

import java.util.Observable;
import java.util.Observer;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ramiBouncer.model.Porta;

public class RamiBouncerBotAdapter implements Observer {
	
	private static RamiBouncerBot bot; // singleton pattern
	
	public RamiBouncerBotAdapter() {
		super();
		InitBot();
	}

	private static void InitBot() {
		ApiContextInitializer.init();
		
	      TelegramBotsApi botsApi = new TelegramBotsApi();
	        try {
	        	bot = new RamiBouncerBot();
	            botsApi.registerBot(bot);
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
//	private List<PortaListener> listeners;
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Porta) {
			bot.StatoPortaCambiato((Porta) arg);
		}
		
	}
		
}
