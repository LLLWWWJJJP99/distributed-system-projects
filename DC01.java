import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DC01 implements Runnable {

	private int PORT = 3332;
	private int UID = 123;
	private int[] neighborPORT = {5678, 5231};
	boolean isLeader = false;
	
	public static void main(String[] args) {
		DC01 dc01 = new DC01();
		Thread t = new Thread(dc01);
		t.start();
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 5231);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("Hello from dc01\nclient");
			String message = reader.readLine();
			System.out.println(message);
		} catch (IOException e) {
			System.out.println("Read failed");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			while(true) {
				ClientManager manager;
				try {
					manager = new ClientManager(serverSocket.accept());
					Thread t = new Thread(manager);
					t.start();
				} catch (IOException e) {
					System.out.println("accept failed");
					System.exit(100);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
