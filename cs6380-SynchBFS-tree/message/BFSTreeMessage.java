package cs6380.message;

import java.io.Serializable;

public class BFSTreeMessage extends Message implements Serializable {

	private static final long serialVersionUID = -2241008166305782441L;
	private int[][] matrix;
	private int sender;
	private int receiver;
	private String type;

	public BFSTreeMessage(int sender, int receiver, String type, int[][] matrix) {
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
		this.matrix = matrix;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		BFSTreeMessage other = (BFSTreeMessage) obj;
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

	@Override
	public String toString() {
		return "SpanningTreeMessage [sender=" + sender + ", receiver=" + receiver + ", type=" + type + "]";
	}
}
