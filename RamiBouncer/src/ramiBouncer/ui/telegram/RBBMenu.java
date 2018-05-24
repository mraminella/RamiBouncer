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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import ramiBouncer.model.CasaManager;
import ramiBouncer.model.Porta;
import ramiBouncer.model.Sensore;
import ramiBouncer.model.Stato;

public class RBBMenu {

	private RamiBouncerBot rbbot;	
	
	public RBBMenu(RamiBouncerBot rbbot) {
		this.rbbot = rbbot;
	}
	
	protected void selectOption(Update update) {
		String option = update.getMessage().getText();
		/*
		if(option.contains("/options")){
			advancedOptions(update);
		}
		*/
		if(option.contains("/reset")) {
			resetSensors(update);
		}
		else if(option.contains("/sorveglia")) {
			rbbot.addToNotifyList(update);
			inviaStato(update);
		}
		else if (option.contains("/stop")) {
			rbbot.stopNotify(update);
		}
		else if(option.contains("/stato")) {
			inviaStato(update);
		}
		/*
		else if(option.contains("/telecamere")) {
			inviaVideo(update);
		} */
		else {		
			List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
			KeyboardRow row = new KeyboardRow();
			row.add(new KeyboardButton("/sorveglia"));
			row.add(new KeyboardButton("/stop"));
			keyboard.add(row);
			row = new KeyboardRow();
			row.add(new KeyboardButton("/stato"));
			row.add(new KeyboardButton("/reset"));
			keyboard.add(row);
			ReplyKeyboard replyMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboard );
			rbbot.replyWithKeyboard(update, replyMarkup,
					"/sorveglia: Attiva sorveglianza"+'\n'+"/stop: Ferma sorveglianza"+'\n'+"/stato: visualizza lo stato dei sensori"+'\n'+"/reset: Resetta sensori disattivati");
		}
	}
	
	private void inviaStato(Update update) {
		rbbot.replyWithText(update, CasaManager.getCasa().printPorte());
		
	}

	private void inviaVideo(Update update) {
		rbbot.replyWithText(update, "Attendi..");
		Runtime rt = Runtime.getRuntime();
		
			Thread t=new Thread() {
				public void run() {
					try { 	
						Process pr;
							pr = rt.exec("/home/marco/registra.sh");
							Thread.sleep(65000);
							
							
						for(int i = 1; i <= 5; i++){
							File file = new File(i+".mp4");
							rbbot.replyWithVideo(update, file);
							Files.delete(FileSystems.getDefault().getPath(i+".mp4"));
						}

					} catch (Exception ie) {ie.printStackTrace();}
				}
			};

			t.start();		
		
		
	}
	
	private void resetSensors(Update update) {
		StringBuilder sb = new StringBuilder();
		for( Sensore sensore : CasaManager.getCasa().getSensori()) {
			Iterator<Porta> portaIterator = sensore.getPorte().iterator();
			while(portaIterator.hasNext()) {
				Porta porta = portaIterator.next();
				if (porta.getStato().equals(Stato.disattivato)) {
					porta.setStato(Stato.spento);
					sb.append(porta.getNome());
					sb.append(" ");
				}
			}
		}
		rbbot.replyWithText(update,sb.toString());
	}
	/*
	private void showSensori(Update update) {
		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		KeyboardRow row;
		row = new KeyboardRow();
		int i = 0;
			for(Sensore sensore : CasaManager.getCasa().getSensori()) {
				row.add(new KeyboardButton("/options getSensor "+sensore.getNumero()));
				i++;
				if(i == 4) {
					keyboard.add(row); row = new KeyboardRow();
				}
			}
		row.add(new KeyboardButton("Torna indietro"));
		keyboard.add(row);
		ReplyKeyboard replyMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboard );
		rbbot.replyWithKeyboard(update, replyMarkup,"Scegli il numero di sensore da modificare: ");
	}
	
	private void showSensore(Update update) {
		String option = update.getMessage().getText();
		StringTokenizer st = new StringTokenizer(option," ");
		option = st.nextToken();
		option = st.nextToken();
		option = st.nextToken();
		int numeroSensore = Integer.parseInt(option);
		Sensore sensore = CasaManager.getCasa().getSensore(new Sensore(numeroSensore));
		rbbot.replyWithText(update, CasaManager.getCasa().getSensore(sensore).toString()+'\n'
				+"/options setNomeSensore "+numeroSensore+" nuovoNome");
		for(Porta porta : sensore.getPorte()) {
			rbbot.replyWithText(update, "/options setNomePorta "+numeroSensore+" "+porta.getNumero()+" Nuovonome");
			rbbot.replyWithText(update, "/options riattivaPorta "+numeroSensore+" "+porta.getNumero());
		}
	}
	
	
	private void setNomeSensore(Update update) {
		String option = update.getMessage().getText();
		StringTokenizer st = new StringTokenizer(option," ");
		option = st.nextToken();
		option = st.nextToken();
		option = st.nextToken();
		int numeroSensore = Integer.parseInt(option);
		String nuovoNome = st.nextToken();
		CasaManager.getCasa().getSensore(new Sensore(numeroSensore)).setNome(nuovoNome);
		rbbot.replyWithText(update, "OK!"+CasaManager.getCasa().getSensore(new Sensore(numeroSensore)).toString());
	}
	
	private void setNomePorta(Update update) {
		String option = update.getMessage().getText();
		StringTokenizer st = new StringTokenizer(option," ");
		option = st.nextToken();
		option = st.nextToken();
		option = st.nextToken();
		int numeroSensore = Integer.parseInt(option);
		String nuovoNome = st.nextToken();
		Sensore sensore = CasaManager.getCasa().getSensore(new Sensore(numeroSensore));
		Porta porta = sensore.getPorta(new Porta(Integer.parseInt(st.nextToken())));
		porta.setNome(nuovoNome);
		rbbot.replyWithText(update, "OK!"+CasaManager.getCasa().getSensore(new Sensore(numeroSensore)).getPorta(porta).toString());
	}
	
	private void riattivaPorta(Update update) {
		String option = update.getMessage().getText();
		StringTokenizer st = new StringTokenizer(option," ");
		option = st.nextToken();
		option = st.nextToken();
		option = st.nextToken();
		int numeroSensore = Integer.parseInt(option);
		Sensore sensore = CasaManager.getCasa().getSensore(new Sensore(numeroSensore));
		Porta porta = sensore.getPorta(new Porta(Integer.parseInt(st.nextToken())));
		porta.setStato(ramiBouncer.model.Stato.spento);
		rbbot.replyWithText(update, "OK!"+CasaManager.getCasa().getSensore(new Sensore(numeroSensore)).getPorta(porta).toString());
	}

	protected void advancedOptions(Update update) {
		String option = update.getMessage().getText();
		if(option.contains("sensors")) {
			showSensori(update);
		} else if (option.contains("getSensor")) {
			showSensore(update);
		} else if (option.contains("setNomeSensore")) {
			setNomeSensore(update);
		} else if (option.contains("setNomePorta")) {
			setNomePorta(update);
		}
			else if (option.contains("riattivaPorta")) {
			riattivaPorta(update);
		}
		else {
		List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
		KeyboardRow row = new KeyboardRow();
		row.add(new KeyboardButton("/options sensors"));
		row.add(new KeyboardButton("Torna indietro"));
		keyboard.add(row);
		ReplyKeyboard replyMarkup = new ReplyKeyboardMarkup().setKeyboard(keyboard );
		rbbot.replyWithKeyboard(update, replyMarkup,"/options sensors Mostra i sensori");
		}
	}
	
	*/
}
