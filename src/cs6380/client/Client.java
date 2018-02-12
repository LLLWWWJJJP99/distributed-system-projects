package cs6380.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpSocketOption;
import com.sun.nio.sctp.SctpStandardSocketOptions;

public class Client {
	private String id;
	private String hostName;
	private HashSet<String> neighbors;
	private HashMap<String, String> buffer;
	private int port;
	private Socket socket;

	private int pulse;
	private int dist;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Setting Client InputStream Error");
			e.printStackTrace();
		}

		try {
			this.ps = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Setting Client OutputStream Error");
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public HashSet<String> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(HashSet<String> neighbors) {
		this.neighbors = neighbors;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Client() {
		neighbors = new HashSet<>();
		this.pulse = 0;
		this.dist = Integer.MAX_VALUE;
		this.buffer = new HashMap<>();
	}

	@Override
	public int hashCode() {
		return hostName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Client other = (Client) obj;

		if (other == this) {
			return true;
		}

		return other.getHostName().equals(this.getHostName());
	}

	private BufferedReader br;
	private PrintStream ps;

	public void register() {
		ps.println(ClientProtocol.LOGIN + hostName + ClientProtocol.LOGIN);
	}
	
	public void test() {
		for(int i = 0 ; i < 5; i++) {
			ps.println(ClientProtocol.TEST + this.toString() + ClientProtocol.TEST);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		closeResource();
	}
	
	private void readAndWrite() {
		/*String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] infos = line.split(ClientProtocol.MSG_ROUND);
				int p = Integer.parseInt(infos[0]);
				String from = infos[1];
				if (p == this.pulse + 1) {
					buffer.put(from, infos[2]);
				} else {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	private void closeResource() {
		try {
			if(ps != null) {
				ps.close();
			}
			
			if(br != null) {
				br.close();
			}
			
			if(socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println("ClientInfo: close resource error");
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "ClientInfo [id=" + id + ", hostName=" + hostName + ", neighbors=" + neighbors + ", port=" + port
				+ ", pulse=" + pulse + ", dist=" + dist + "]";
	}

	public static void main(String[] args) throws IOException {

	}

}
