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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ramiBouncer.model.Porta;
@SuppressWarnings("deprecation")
public class RamiBouncerBot extends TelegramLongPollingBot {

	/**
	 * 
	 */

	
	private List<Update> notifyList;
	RBBAuthenticator rbbauth;
	RBBMenu rbbmenu;
	
	
	public RamiBouncerBot() {
		rbbauth = new RBBAuthenticator(this);
		rbbmenu = new RBBMenu(this);
		notifyList = new ArrayList<Update>();
	}

	@Override
	public String getBotUsername() {
		return "RamiBouncerBot";
	}

	
	public void replyWithText(Update update, String text) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(text);
		
		try {
            sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
	/*
	public void replyWithFile(Update update, String text) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(text);
		
		try {
            sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
	*/
	public void replyWithKeyboard(Update update, ReplyKeyboard replyMarkup, String text) {
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId()).setText(text);
		message.setReplyMarkup(replyMarkup );
		
		try {
            sendMessage(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
	
	public void replyWithVideo(Update update, File filepath){
		SendVideo sendVideoRequest = new SendVideo();
		sendVideoRequest.setChatId(update.getMessage().getChatId());
		sendVideoRequest.setNewVideo(filepath);
		 try {
	            // Execute the method
	            sendVideo(sendVideoRequest);
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	}
	
	@Override
	public synchronized void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		
	    if (update.hasMessage()) {
	    	if(update.getMessage().hasText()) {
		    	if(rbbauth.checkIfRecognisedChat(update)) {
			    	rbbmenu.selectOption(update);
		    	}
		    	rbbauth.tryLogin(update);
	    	}
	    	else if(update.getMessage().getContact() != null) 
	    		rbbauth.tryLogin(update);
	    }
	}
	
	@Override
	public String getBotToken() {
		return ""; // inserire qui bot token di Telegram
	}

	public void StatoPortaCambiato(Porta p) {
		for(Update update : notifyList) {
			replyWithText(update,p.toString());
		}
		
	}

	public List<Update> getNotifyList() {
		return notifyList;
		
	}

	public synchronized void addToNotifyList(Update update) {
		if(!notifyList.contains(update)) notifyList.add(update);
	}

	public synchronized void addAllToNotifyList() {
		
	}

	public synchronized void stopNotify(Update update) {
		for(Iterator<Update> it = getNotifyList().iterator(); it.hasNext();) {
				it.next(); it.remove();
	}
	replyWithText(update, "Hai fermato la sorveglianza per tutti!");
	}
	
	
	
}
