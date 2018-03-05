package cs6380.message;

import java.io.Serializable;

public class LeaderElectionMessage extends Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int dist;
	private int max;
	private int pulse;
	private int sender;
	private int receiver;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	@Override
	public String toString() {
		return "type=" + type + ", dist=" + dist + ", max=" + max + ", pulse=" + pulse + ", sender=" + sender
				+ ", receiver=" + receiver;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getPulse() {
		return pulse;
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public LeaderElectionMessage(int dist, int max, int pulse, int sender, int receiver) {
		this.dist = dist;
		this.max = max;
		this.pulse = pulse;
		this.sender = sender;
		this.receiver = receiver;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dist;
		result = prime * result + max;
		result = prime * result + pulse;
		result = prime * result + receiver;
		result = prime * result + sender;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaderElectionMessage other = (LeaderElectionMessage) obj;
		if (dist != other.dist)
			return false;
		if (max != other.max)
			return false;
		if (pulse != other.pulse)
			return false;
		if (receiver != other.receiver)
			return false;
		if (sender != other.sender)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
