package cs6380.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cs6380.message.DegreeMessage;
import cs6380.message.LeaderElectionMessage;
import cs6380.message.Message;
import cs6380.message.MsgType;
import cs6380.message.SpanningTreeMessage;

/**
 * @author 29648
 * NodeListener accept message and process it according to the real class of it
 */
public class NodeListener implements Runnable {
	private Node node;

	public NodeListener(Node node) {
		this.node = node;
	}

	public static void main(String[] args) {

	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(node.getMyPort())) {
			while (true) {
				Socket socket = server.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Message message = (Message) ois.readObject();
				if (message.getClass().getSimpleName().equals(LeaderElectionMessage.class.getSimpleName())) {
					LeaderElectionMessage electionMessage = (LeaderElectionMessage) message;
					if (electionMessage.getType().equals(MsgType.LOGIN)) {
						node.login(electionMessage);
					} else if (electionMessage.getType().equals(MsgType.ROUND)
							|| electionMessage.getType().equals(MsgType.SUCCESS)) {
						node.processLeaderElectionMessage(electionMessage);
					}

				} else if (message.getClass().getSimpleName().equals(SpanningTreeMessage.class.getSimpleName())) {
					SpanningTreeMessage treeMessage = (SpanningTreeMessage) message;
					node.processSpanningTreeMessage(treeMessage);
				} else if (message.getClass().getSimpleName().equals(DegreeMessage.class.getSimpleName())) {
					DegreeMessage degreeMessage = (DegreeMessage) message;
					node.processDegreeMessage(degreeMessage);
				}
				ois.close();
				socket.close();

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
