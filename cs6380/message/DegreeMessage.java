package cs6380.message;

import java.io.Serializable;

public class DegreeMessage extends Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -308486581181394883L;

	private int sender;

	public DegreeMessage(int sender, int receiver, int degree, String type, int maxID) {
		this.sender = sender;
		this.receiver = receiver;
		this.degree = degree;
		this.type = type;
		this.maxID = maxID;
	}

	@Override
	public String toString() {
		return "DegreeMessage [sender=" + sender + ", receiver=" + receiver + ", degree=" + degree + ", type=" + type
				+ ", maxID=" + maxID + "]";
	}

	private int receiver;
	private int degree;
	private String type;
	private int maxID;

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

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaxID() {
		return maxID;
	}

	public void setMaxID(int maxID) {
		this.maxID = maxID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + degree;
		result = prime * result + maxID;
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
		DegreeMessage other = (DegreeMessage) obj;
		if (degree != other.degree)
			return false;
		if (maxID != other.maxID)
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
