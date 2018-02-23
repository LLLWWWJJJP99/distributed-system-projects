package cs6380.node4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cs6380.message.Message;
import cs6380.message.MsgType;

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
				if (message.getType().equals(MsgType.LOGIN)) {
					node.login(message);
				} else {
					node.processMessage(message);
				}

				ois.close();
				socket.close();

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
