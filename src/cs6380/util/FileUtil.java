package cs6380.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import cs6380.client.Client;

public class FileUtil {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		readConfig(".//config.txt");
	}
	
	/**
	 * read node info from config file and initialize client instance according to config format
	 * @param fileName given config file name
	 * @throws FileNotFoundException
	 */
	public static void readConfig(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		// read number of nodes
		int nodeNums = 0;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(!line.startsWith("#")) {
				nodeNums = Integer.parseInt(line.trim());
				break;
			}
		}
		
		// initialize nodes
		int index = 0;
		//List<Client> list = new ArrayList<>();
		while(index < nodeNums && scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(!line.startsWith("#")){
				Client client = new Client();
				String[] info = line.split("\\s+");
				for(int i = 0; i < info.length; i++) {
					if(i == 0) {
						client.setId(info[i]);
					}else if(i == 1) {
						client.setHostName(info[i]);
					}else if(i == 2) {
						client.setPort(Integer.parseInt(info[i]));
					}else {
						client.getNeighbors().add(info[i]);
					}
				}
				//list.add(client);
				index++;
			}
		}
		System.out.println("Reading Config Ends");
		scanner.close();
	}

}
