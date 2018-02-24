package cs6380.message;

import java.io.Serializable;

public class SpanningTreeMessage extends Message implements Serializable {
	private static final long serialVersionUID = 4424057573533868709L;
	private int sender;
	private int receiver;
	private String type;

	public SpanningTreeMessage(int sender, int receiver, String type) {
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
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
		SpanningTreeMessage other = (SpanningTreeMessage) obj;
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
