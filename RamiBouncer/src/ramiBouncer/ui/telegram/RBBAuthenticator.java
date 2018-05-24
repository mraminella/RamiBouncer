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

import java.util.List;

import org.telegram.telegrambots.api.objects.Update;

public class RBBAuthenticator {

	private List<Long> recognisedChatIds;
	private RamiBouncerBot rbbot;

	public RBBAuthenticator(RamiBouncerBot rbbot) {
		recognisedChatIds = AuthorizationPersister.readAuthorizations();
		this.rbbot = rbbot;
		keepPersistence();
		
	}
	
	protected boolean checkIfRecognisedChat (Update update) {
		for(Long chatId : recognisedChatIds) {
			if(update.getMessage().getChatId().equals(chatId))
				return true;
		}
		return false;
	}
	
	protected void tryLogin(Update update) {
		
		if(update.getMessage().hasText() && update.getMessage().getText().equals("/enterPasshprase")) {
	        rbbot.replyWithText(update,"Devi fare login!");
    	}
		else if(update.getMessage().getContact() != null) {
			/*
			 *		Inserire la politica di login
			 * 
			 */
				if(!recognisedChatIds.contains(update.getMessage().getChatId()))recognisedChatIds.add(update.getMessage().getChatId());
				rbbot.replyWithText(update,"Benvenuto!");
			 
		}
		
	}
	
	private synchronized void keepPersistence() {
		Thread t=new Thread() {
			public void run() {
				try { 	
					while (true) {
						Thread.sleep(5000);
						AuthorizationPersister.writeAuthorizations(recognisedChatIds);
					}
	
				} catch (InterruptedException ie) {}
			}
		};
		t.start();
	}

}
