package cs6380.client;

import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {
	private String id;
	private String hostName;
	private List<String> neighbors;
	private int port;

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

	public List<String> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<String> neighbors) {
		this.neighbors = neighbors;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Client() {
		neighbors = new ArrayList<>();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
