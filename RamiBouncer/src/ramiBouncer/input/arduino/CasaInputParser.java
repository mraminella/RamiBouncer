package ramiBouncer.input.arduino;

import java.util.StringTokenizer;

import ramiBouncer.model.*;

public class CasaInputParser implements InputParser {
	private Casa casa;
	
	
	public CasaInputParser(Casa casa) {
		super();
		this.casa = casa;
	}


	public void Parse(String string) {
		StringTokenizer st = new StringTokenizer(string,"=");
		
		String token = st.nextToken();
		if (token.contains("Richiesta: "))  {	
			token = st.nextToken(); // numero sensore
			Sensore sensore = new Sensore(Integer.parseInt(token.replaceAll("[\\D]", "")));
			casa.addSensore(sensore);
			token = st.nextToken();
			Porta porta = new Porta(Integer.parseInt(token));
			casa.getSensore(sensore).addPorta(porta);
			token = st.nextToken();
			int statoint =  Integer.parseInt(token.replaceAll("[\\D]", ""));
			Stato stato = Stato.chiuso;
			if (statoint == 0) stato = Stato.aperto; else stato = Stato.chiuso;
			casa.getSensore(sensore).getPorta(porta).setStato(stato);
		}
	}
}
