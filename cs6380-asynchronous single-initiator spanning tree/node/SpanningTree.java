package cs6380.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs6380.message.DegreeMessage;
import cs6380.message.MsgType;
import cs6380.message.SpanningTreeMessage;

public class SpanningTree {
	private Set<Integer> children;
	// unrelated neighbors
	private Set<Integer> unrelated;
	private Node node;
	private int max;
	private int maxID;
	private Integer parent;
	private Set<Integer> recorder;

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public SpanningTree(Node node) {
		this.node = node;
		children = Collections.synchronizedSet(new HashSet<>());
		unrelated = Collections.synchronizedSet(new HashSet<>());
	}

	public Set<Integer> getChildren() {
		return children;
	}

	public int getMax() {
		return max;
	}

	public int getMaxID() {
		return maxID;
	}

	public Set<Integer> getUnrelated() {
		return unrelated;
	}

	public void init() {
		recorder = Collections.synchronizedSet(new HashSet<>(children));
		max = recorder.size() + 1;
		maxID = node.getId();
	}

	/**
	 * process reject message according to flooding algo
	 * @param message to be processed
	 */
	public synchronized void processReject(SpanningTreeMessage message) {
		int neighbor = message.getSender();
		unrelated.add(neighbor);
	}
	
	/**
	 * process accept message according to flooding algo
	 * @param message to be processed
	 */
	public synchronized void processAccept(SpanningTreeMessage message) {
		int child = message.getSender();
		children.add(child);
	}

	
	/**
	 * @param neighbors of current node
	 * @param parent of current node
	 * @return true if current node get all children' reply otherwise false
	 */
	public synchronized boolean check(List<Integer> neighbors, Integer parent) {
		HashSet<Integer> set = new HashSet<>(neighbors);
		set.remove(parent);
		HashSet<Integer> sumup = new HashSet<>();
		sumup.addAll(children);
		sumup.addAll(unrelated);
		return sumup.equals(set);
	}
	
	/**
	 * process query message according to flooding algo
	 * @param message to be processed
	 */
	public synchronized void processQuery(SpanningTreeMessage message) {
		if (parent == null) {
			parent = new Integer(message.getSender());
			SpanningTreeMessage accept = new SpanningTreeMessage(message.getReceiver(), message.getSender(),
					MsgType.ACCEPT);
			node.sendPrivateMessage(accept);
			node.broadcast(MsgType.QUERY, new Integer(message.getSender()));
		} else {
			SpanningTreeMessage reject = new SpanningTreeMessage(message.getReceiver(), message.getSender(),
					MsgType.REJECT);
			node.sendPrivateMessage(reject);
		}
	}
	
	/**
	 * process ack message from converge cast
	 * @param message to be processed
	 */
	public synchronized void processACK(DegreeMessage degreeMessage) {
		int sender = degreeMessage.getSender();
		int degree = degreeMessage.getDegree();
		int maxId = degreeMessage.getMaxID();
		recorder.remove(new Integer(sender));
		// update max degree and corresponding id
		if (degree > max) {
			maxID = maxId;
			max = degree;
		}
		// if receive all neighbors' reply, then sent ack to it's parent iff the current node is not the root
		if (recorder.size() == 0) {
			if (parent != node.getId()) {
				DegreeMessage message = new DegreeMessage(degreeMessage.getReceiver(), parent, max, MsgType.ACK, maxID);
				node.sendPrivateMessage(message);
			} else {
				System.out.println(node.getId() + " says: " + "max id is " + maxID + " ,max degree is " + max);
			}
		}
	}

	public static void main(String[] args) {
		HashSet<Integer> neighbors = new HashSet<>();
		neighbors.add(1);
		neighbors.add(2);
		neighbors.add(3);
		neighbors.add(4);
		neighbors.add(5);

		SpanningTree tree = new SpanningTree(null);
		tree.children.add(1);
		tree.children.add(2);

		tree.unrelated.add(4);
		tree.unrelated.add(5);

		System.out.println(tree.check(new ArrayList<>(neighbors), 3));
		System.out.println(tree.check(new ArrayList<>(neighbors), 6));
		System.out.println(tree.check(new ArrayList<>(neighbors), 5));
		System.out.println(neighbors.remove(new Integer(1)));
		System.out.println(neighbors);
	}
}
