package cs6380.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cs6380.clock.MatrixClock;
import cs6380.message.BFSTreeMessage;
import cs6380.message.DegreeMessage;
import cs6380.message.MsgType;

public class SpanningTree {
	private MatrixClock clock;
	private Set<Integer> children;
	// unrelated neighbors
	private Set<Integer> unrelated;
	private Node node;
	private int max;
	private int maxID;

	private Integer parent;
	private Set<Integer> recorder;
	private List<BFSTreeMessage> buffer;

	public MatrixClock getClock() {
		return clock;
	}

	public List<BFSTreeMessage> getBuffer() {
		return buffer;
	}

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
		buffer = Collections.synchronizedList(new ArrayList<>());
		clock = new MatrixClock(node.getNumOfNodes());
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

	public synchronized void init() {
		recorder = Collections.synchronizedSet(new HashSet<>());
		recorder.addAll(children);
		System.out.println("recorder: " + recorder);
		if (parent == null || parent == node.getId()) {
			max = recorder.size();
		} else {
			max = recorder.size() + 1;
		}
		System.out.println("max: " + max);
		maxID = node.getId();
	}

	/**
	 * process reject message according to flooding algo
	 * 
	 * @param message
	 *            to be processed
	 */
	public synchronized void processReject(BFSTreeMessage message) {
		int neighbor = message.getSender();
		unrelated.add(neighbor);
	}

	/**
	 * process accept message according to flooding algo
	 * 
	 * @param message
	 *            to be processed
	 */
	public synchronized void processAccept(BFSTreeMessage message) {
		int child = message.getSender();
		children.add(child);
	}

	/**
	 * @param neighbors
	 *            of current node
	 * @param parent
	 *            of current node
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
	 * process BFSTreeMessage query message according to flooding algo
	 * 
	 * @param BFSTreeMessage
	 *            message to be processed
	 */
	public synchronized void processBFSQuery(BFSTreeMessage message) {
		if (!clock.canBeDelivered(message.getMatrix(), node.getNodeLookup().getIndex(message.getSender()),
				node.getNodeLookup().getIndex(message.getReceiver()))) {
			buffer.add(message);
			return;
		}
		addEdge(message);
		check_buffer();
	}

	/**
	 * deliver given message according SynBFS algorithm
	 * 
	 * @param message
	 */
	public synchronized void addEdge(BFSTreeMessage message) {
		clock.event_clock(message.getMatrix());

		if (parent == null) {
			parent = new Integer(message.getSender());
			BFSTreeMessage accept = new BFSTreeMessage(message.getReceiver(), message.getSender(), MsgType.ACCEPT,
					clock.getMatrix());
			node.sendPrivateMessage(accept);
			node.broadcastBFSTreeMessage(MsgType.QUERY, new Integer(message.getSender()));
		} else {
			// ?????
			BFSTreeMessage reject = new BFSTreeMessage(message.getReceiver(), message.getSender(), MsgType.REJECT,
					clock.getMatrix());
			node.sendPrivateMessage(reject);
		}
	}

	/**
	 * check messages in buffer and deliver messages that are deliverable
	 */
	public synchronized void check_buffer() {
		Iterator<BFSTreeMessage> iterator = buffer.iterator();
		while (iterator.hasNext()) {
			BFSTreeMessage m = iterator.next();
			if (clock.canBeDelivered(m.getMatrix(), node.getNodeLookup().getIndex(m.getSender()),
					node.getNodeLookup().getIndex(m.getReceiver()))) {
				iterator.remove();
				addEdge(m);
			}
		}
	}

	/**
	 * process ack message from converge cast
	 * 
	 * @param message
	 *            to be processed
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
		// if receive all neighbors' reply, then sent ack to it's parent iff the current
		// node is not the root
		if (recorder.size() == 0) {
			if (parent != node.getId()) {
				DegreeMessage message = new DegreeMessage(node.getId(), parent, max, MsgType.ACK, maxID);
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
