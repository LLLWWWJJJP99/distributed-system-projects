package cs6380.server;


import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Set;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

import cs6380.client.Client;

public class Server {
	private static HashMap<String, PrintStream> clients;
	public static int PORT = 30000;
	public static String IP = "127.0.0.1";
	private ServerSocket server;
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.init();
	}
	
	private void init() throws IOException {
		server = new ServerSocket();
		server.bind(new InetSocketAddress(IP, PORT));
		while(true) {
			Socket socket = server.accept();
			
			new Thread(new ServerThread(socket)).start();
		}
	}
	
	public Server() {
		clients = new HashMap<>();
	}
	
	public static HashMap<String, PrintStream> getClients() {
		return clients;
	}

}
