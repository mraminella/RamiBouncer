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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuthorizationPersister {
	final static String FILE_NAME = "authorizations.txt";
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static List<Long> readAuthorizations() {
		List<Long> recognisedChatIds = new ArrayList<Long>();
		Path path = Paths.get(FILE_NAME);
		try ( Scanner scanner = new Scanner(path,ENCODING.name()) ) {
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				recognisedChatIds.add(Long.parseLong(line));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recognisedChatIds;
	}
	
	public static boolean writeAuthorizations(List<Long> authorizations){
		Path path = Paths.get(FILE_NAME);
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
		      for(Long id : authorizations){
		    	String line = id.toString();
		        writer.write(line);
		        writer.newLine();
		      }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
