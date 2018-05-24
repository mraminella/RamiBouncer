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
package ramiBouncer.persistence;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import ramiBouncer.model.*;

public class SensoriPersister {
	final static String FILE_NAME = "casa.txt";
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static Casa readCasa() {
		Casa result = null;
		Path path = Paths.get(FILE_NAME);
		try ( Scanner scanner = new Scanner(path,ENCODING.name()) ) {
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				int updateTolerance = Integer.parseInt(scanner.nextLine());
				result = new Casa(line, updateTolerance);
				while(scanner.hasNextLine()) {
					line = scanner.nextLine();
					if(line.contains("Sensore")) {
						Sensore sensore = new Sensore(Integer.parseInt(scanner.nextLine()));
						sensore.setNome(scanner.nextLine());
						while(scanner.hasNextLine()) {
							line = scanner.nextLine();
							if(line.contains("Porta")) {
								Porta p = new Porta(Integer.parseInt(scanner.nextLine()), Stato.spento);
								p.setNome(scanner.nextLine());
								p.setUpdateInterval(Integer.parseInt(scanner.nextLine()));
								sensore.addPorta(p);
							}
							else if(line.contains("FineSensore")) {
								result.addSensore(sensore);
								break;
							}
						}
					}
				}
			}
			
		} catch (IOException e) {
			System.out.println("File casa.txt inesistente o malformato");
		}
		return result;
	}
	
	public static boolean writeCasa(Casa casa){
		Path path = Paths.get(FILE_NAME);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
			writer.write(casa.getNome());
			writer.newLine();
			writer.write(Integer.toString(casa.getPortiniere().getUpdateTolerance()));
			writer.newLine();
		      for(Sensore sensore : casa.getSensori()){
		    	writer.write("Sensore");
		    	writer.newLine();
		    	String line = Integer.toString(sensore.getNumero());
		    	writer.write(line);
		    	writer.newLine();		    			    	
		    	line = sensore.getNome();
		        writer.write(line);
		        writer.newLine();
		        for(Porta porta : sensore.getPorte()){
		        	writer.write("Porta");
			    	writer.newLine();
			    	writer.write(Integer.toString(porta.getNumero()));
			    	writer.newLine();
			    	writer.write(porta.getNome());
			    	writer.newLine();
			    	writer.write(Integer.toString(porta.getUpdateInterval()));
			    	writer.newLine();
		        }
		        writer.write("FineSensore");
		        writer.newLine();
		      }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
