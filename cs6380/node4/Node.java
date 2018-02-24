package cs6380.node4;

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

import cs6380.message.DegreeMessage;
import cs6380.message.LeaderElectionMessage;
import cs6380.message.MsgType;
import cs6380.message.SpanningTreeMessage;
import cs6380.util.FileUtil;

public class Node {
	private final int id;
	private boolean init;
	private NodeLookup nodeLookup;
	private List<Integer> neighbors;
	private Set<Integer> login_messages;

	
	private SpanningTree tree;
	private final int myPort;
	private final String myIP;
	private LeaderElection election;

	public int getMyPort() {
		return myPort;
	}

	public String getMyIP() {
		return myIP;
	}

	public int getId() {
		return id;
	}

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
		this.id = 1043;
		init = false;
		neighbors = Collections.synchronizedList(new ArrayList<>());
		nodeLookup = new NodeLookup(FileUtil.readConfig("config.txt", String.valueOf(id), neighbors));
		myIP = nodeLookup.getIP(id);
		myPort = Integer.parseInt(nodeLookup.getPort(id));
		login_messages = Collections.synchronizedSet(new HashSet<>());
	}

	private void init() {
		election = new LeaderElection(this);
		tree = new SpanningTree(this);
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

	public synchronized void login(LeaderElectionMessage message) {
		System.out.println("loging message:" + message);
		this.login_messages.add(new Integer(message.getSender()));
	}

	private void listenToNeighbors() {
		new Thread(new NodeListener(this)).start();
	}

	public synchronized void broadcast(String type) {
		if (type.equals(MsgType.ROUND) || type.equals(MsgType.SUCCESS) || type.equals(MsgType.LOGIN)) {
			for (int neighbor : neighbors) {
				String ip = nodeLookup.getIP(neighbor);
				String port = nodeLookup.getPort(neighbor);
				LeaderElectionMessage message = new LeaderElectionMessage(election.getDist(), election.getMax(),
						election.getPulse(), id, neighbor);
				message.setType(type);
				System.out.println(id + " broadcast:" + message);
				try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(message);
					oos.close();
					socket.close();
					if (MsgType.LOGIN.equals(type)) {
						init = true;
					}
				} catch (ConnectException e) {
					if (MsgType.LOGIN.equals(type)) {
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
		} else {
			System.err.println("ROUND|SUCCESS: Wrong type of message to be sent");
		}
	}

	public synchronized void broadcast(String type, Integer except) {
		if (type.equals(MsgType.QUERY)) {
			for (int neighbor : neighbors) {
				if (except != null && except == neighbor) {
					continue;
				}
				String ip = nodeLookup.getIP(neighbor);
				String port = nodeLookup.getPort(neighbor);
				SpanningTreeMessage message = new SpanningTreeMessage(id, neighbor, type);
				System.out.println(id + " broadcast:" + message);
				try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(message);
					oos.close();
					socket.close();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("QUERY: Wrong type of message to be sent");
		}
	}

	public synchronized void sendPrivateMessage(SpanningTreeMessage message) {
		int receiver = message.getReceiver();
		String ip = nodeLookup.getIP(receiver);
		String port = nodeLookup.getPort(receiver);
		try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			oos.close();
			socket.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendPrivateMessage(DegreeMessage message) {
		int receiver = message.getReceiver();
		String ip = nodeLookup.getIP(receiver);
		String port = nodeLookup.getPort(receiver);
		try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			oos.close();
			socket.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void processLeaderElectionMessage(LeaderElectionMessage message) {
		/*
		 * if (election.isDone()) { return; }
		 */
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

	public synchronized void processSpanningTreeMessage(SpanningTreeMessage message) {
		String type = message.getType();
		if (type.equals(MsgType.QUERY)) {
			tree.processQuery(message);
		} else if (type.equals(MsgType.REJECT)) {
			tree.processReject(message);
		} else if (type.equals(MsgType.ACCEPT)) {
			tree.processAccept(message);
		}

		if (tree.check(neighbors, tree.getParent())) {
			System.out.println(id + " parent " + tree.getParent());
			System.out.println(id + " children " + tree.getChildren());
			System.out.println(id + " unrelated " + tree.getUnrelated());
		}
	}

	public synchronized void processDegreeMessage(DegreeMessage degreeMessage) {
		if(degreeMessage.getType().equals(MsgType.ACK)) {
			tree.processACK(degreeMessage);
		}else {
			System.err.println("Degree Message: wrong message type");
		}
	}
	
	private void startFindMaxDegree() {
		if (tree.getChildren().size() == 0) {
			DegreeMessage message = new DegreeMessage(id, tree.getParent(), tree.getMax(), MsgType.ACK, id);
			sendPrivateMessage(message);
		}
	}

	private void startBuildSpanningTree() {
		if (election.getCandidate() == 1 && tree.getParent() == null) {
			tree.setParent(new Integer(id));
			broadcast(MsgType.QUERY, null);
		}
	}

	private void startLeaderElection() {
		while (!election.isDone() && init) {
			broadcast(MsgType.ROUND);
			boolean roundDone = false;
			while (!roundDone && !election.isDone()) {
				if (election.check() && !election.isDone()) {
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
		while (init) {
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
		node.startLeaderElection();

		if (node.election.isDone()) {
			try {
				Thread.sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			node.startBuildSpanningTree();
			node.tree.init();
			try {
				Thread.sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			node.startFindMaxDegree();
		}
		// node.test();
		// node.broadcast(MsgType.SUCCESS);
	}

}
