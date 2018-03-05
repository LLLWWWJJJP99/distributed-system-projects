package cs6380.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class FileUtil {

	public static void main(String[] args) {
		List<Integer> res = new ArrayList<Integer>();
		HashMap<Integer, Integer> pos = new HashMap<>();
		HashMap<Integer, String> map = readConfig("config.txt", 1043 + "", res, pos );
		System.out.println(res);
		System.out.println(map);
	}

	/**
	 * read node info from config file and initialize client instance according to
	 * config format
	 * 
	 * @param fileName
	 *            given config file name
	 * @return return a list of client info
	 * @throws FileNotFoundException
	 */
	public static HashMap<Integer, String> readConfig(String fileName, String id, List<Integer> neighbors, HashMap<Integer, Integer> pos) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (scanner != null) {
			// read number of nodes
			int nodeNums = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (!line.startsWith("#") && line.length() > 0) {
					nodeNums = Integer.parseInt(line.trim());
					break;
				}
			}

			int index = 0;
			HashMap<Integer, String> map = new HashMap<>();
			while (index < nodeNums && scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.length() == 0) {
					continue;
				}
				if (!line.startsWith("#")) {
					String[] info = line.split("\\s+");
					map.put(Integer.parseInt(info[0]), info[2] + ":" + info[1]);
					pos.put(Integer.parseInt(info[0]), index);
					index++;
				}
			}
			System.out.println("Reading Neighbors");
			
			index = 0;
			
			while (index < nodeNums && scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.length() == 0) {
					continue;
				}
				if (!line.startsWith("#")) {
					String[] info = line.split("\\s+");
					if(info[0].equals(id)) {
						for(int i = 1; i < info.length; i++) {
							neighbors.add(Integer.parseInt(info[i]));
						}
					}
					index++;
				}
			}
			
			System.out.println("Reading Config Ends");
			scanner.close();
			return map;
		} else {
			return null;
		}
	}
}
