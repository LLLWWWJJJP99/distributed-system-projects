package cs6380.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import cs6380.client.ClientProtocol;

public class ServerThread implements Runnable {
	private Socket socket;
	private BufferedReader br;
	private PrintStream ps;
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public static void main(String[] args) {

	}
	
	//clean up the protocol signs to get the real message
	private String getRealMsg(String line) {
		return line.substring(ClientProtocol.PROTOCOL_LEN, line.length() - ClientProtocol.PROTOCOL_LEN);
	}
	
	@Override
	public void run() {
		String line = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ps = new PrintStream(this.socket.getOutputStream());
		} catch (IOException e1) {
			System.out.println("ServerThread: br ps creation error");
			e1.printStackTrace();
			
		}
		try {
			while((line = br.readLine()) != null) {
				String realMsg = this.getRealMsg(line);
				if(line.startsWith(ClientProtocol.LOGIN) && line.endsWith(ClientProtocol.LOGIN)) {
					System.out.println("ServerThread: " + realMsg + " is registered finely");
					Server.getClients().put(realMsg, ps);
				} else if(line.startsWith(ClientProtocol.TEST) && line.endsWith(ClientProtocol.TEST)) {
					System.out.println("ServerThread: " + realMsg);
				} else {
					System.out.println("ServerThread get a unknown message");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
