package cs6380.node3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs6380.message.Message;

public class LeaderElection {
	private Node node;
	private int pulse;
	private int dist;
	private int counter;
	private int max;
	private int candidate;
	private List<Message> buffered_messages;
	private List<Message> message_list;
	private boolean done;
	public int getPulse() {
		return pulse;
	}


	public void setPulse(int pulse) {
		this.pulse = pulse;
	}


	public int getDist() {
		return dist;
	}


	public void setDist(int dist) {
		this.dist = dist;
	}


	public int getCounter() {
		return counter;
	}


	public void setCounter(int counter) {
		this.counter = counter;
	}


	public int getMax() {
		return max;
	}


	public void setMax(int max) {
		this.max = max;
	}


	public int getCandidate() {
		return candidate;
	}


	public void setCandidate(int candidate) {
		this.candidate = candidate;
	}


	public List<Message> getBuffered_Messages() {
		return buffered_messages;
	}

	
	public LeaderElection(Node node) {
		this.node = node;
		this.buffered_messages = Collections.synchronizedList(new ArrayList<>());
		this.message_list = Collections.synchronizedList(new ArrayList<>());
		this.pulse = 0;
		this.dist = 0;
		counter = 0;
		done = false;
		max = node.getId();
		candidate = 1;
	}
	
	public boolean isDone() {
		return done;
	}


	public void setDone(boolean done) {
		this.done = done;
	}


	public List<Message> getMessage_list() {
		return message_list;
	}

	
	public synchronized boolean check() {
		return message_list.size() == node.getNeighbors().size();
	}
	
	public synchronized boolean exe_pelege() {
		pulse += 1;
		Message max_message = message_list.get(0);
		List<Message> list = new ArrayList<>();
		list.add(max_message);
		for(int i = 1; i < message_list.size(); i++) {
			Message next = message_list.get(i);
			if(next.getMax() > max_message.getMax()) {
				max_message = next;
				list.clear();
			}else if(next.getMax() == max_message.getMax()) {
				list.add(next);
			}
		}
		
		if(max < max_message.getMax()) {
			candidate = 0;
			max = max_message.getMax();
			dist = pulse;
		}else if(max > max_message.getMax()) {
			counter = 1;
		}else {
			int z = Integer.MIN_VALUE;
			for(Message m : list) {
				if(m.getDist() > z) {
					z = m.getDist();
				}
			}
			if(z > dist) {
				dist = z;
				counter = 0;
			}else if(z == dist) {
				counter += 1;
			}
		}
		
		message_list.clear();
		message_list.addAll(buffered_messages);
		buffered_messages.clear();
		
		if(counter == 2 && candidate == 1) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		
	}


	@Override
	public String toString() {
		return "LeaderElection [pulse=" + pulse + ", dist=" + dist + ", counter=" + counter + ", max=" + max
				+ ", candidate=" + candidate + ", done=" + done + "]";
	}
	
}
