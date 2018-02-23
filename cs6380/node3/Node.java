package cs6380.node3;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs6380.message.Message;
import cs6380.message.MsgType;
import cs6380.util.FileUtil;

public class Node {
	private final int id;
	private boolean init;
	private NodeLookup nodeLookup;
	private List<Integer> neighbors;
	private Set<Integer> login_messages;

	public int getMyPort() {
		return myPort;
	}

	public String getMyIP() {
		return myIP;
	}

	private final int myPort;
	private final String myIP;
	private LeaderElection election;

	public List<Integer> getNeighbors() {
		return neighbors;
	}

	class NodeLookup {
		HashMap<Integer, String> map;

		public NodeLookup(HashMap<Integer, String> map) {
			this.map = map;
		}

		public String getIP(int id) {
			return map.get(id).split(":")[1];
		}

		public String getPort(int id) {
			return map.get(id).split(":")[0];
		}
	}

	public Node() {
		this.id = 89;
		init = false;
		neighbors = Collections.synchronizedList(new ArrayList<>());
		nodeLookup = new NodeLookup(FileUtil.readConfig("config.txt", String.valueOf(id), neighbors));
		myIP = nodeLookup.getIP(id);
		myPort = Integer.parseInt(nodeLookup.getPort(id));
		login_messages = Collections.synchronizedSet(new HashSet<>());
	}

	public int getId() {
		return id;
	}

	private void init() {
		election = new LeaderElection(this);
		listenToNeighbors();
		while (login_messages.size() < neighbors.size()) {
			broadcast(MsgType.LOGIN);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(id + " is set up successfully");
		login_messages.clear();
	}

	public synchronized void login(Message message) {
		System.out.println("loging message:" + message);
		this.login_messages.add(new Integer(message.getSender()));
	}

	private void listenToNeighbors() {
		new Thread(new NodeListener(this)).start();
	}

	public synchronized void broadcast(String type) {
		for (int neighbor : neighbors) {
			String ip = nodeLookup.getIP(neighbor);
			String port = nodeLookup.getPort(neighbor);
			Message message = new Message(election.getDist(), election.getMax(), election.getPulse(), id, neighbor);
			message.setType(type);
			System.out.println(id + " broadcast:" + message);
			try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(message);
				oos.close();
				socket.close();
				if(MsgType.LOGIN.equals(type)) {
					init = true;					
				}
			} catch (ConnectException e) {
				if(MsgType.LOGIN.equals(type)) {
					init = false;					
				}
				System.out.println("Please Wait For Other Neighbors To Be Started");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void processMessage(Message message) {
		/*if (election.isDone()) {
			return;
		}*/
		System.out.println(id + " receives " + message);
		if (message.getType().equals(MsgType.ROUND)) {
			if (message.getPulse() == election.getPulse()) {
				election.getMessage_list().add(message);
			} else if (message.getPulse() == election.getPulse() + 1) {
				election.getBuffered_Messages().add(message);
			} else {
				System.err.println(
						"Received Message exceed current pulse more than one round or is smaller than than current");
			}
		} else if (message.getType().equals(MsgType.SUCCESS)) {
			if (!election.isDone()) {
				broadcast(MsgType.SUCCESS);
				election.setDone(true);
				System.out.println(id + "I am Done");
			}
		}
	}

	private void run() {
		while (!election.isDone() && init) {
			broadcast(MsgType.ROUND);
			boolean roundDone = false;
			while (!roundDone && !election.isDone()) {
				if(election.check() && !election.isDone()) {
					if (election.exe_pelege()) {
						broadcast(MsgType.SUCCESS);
						election.setDone(true);
					}
					roundDone = true;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("I am " + id + ", " + election);
	}
	
	private void test() {
		while(init) {
			broadcast(MsgType.SUCCESS);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Node node = new Node();
		node.init();
		node.run();
		//node.test();
		//node.broadcast(MsgType.SUCCESS);
	}

}
