import java.io.IOException;
import java.net.ServerSocket;

public class DC21 implements Runnable {

	private int PORT = 5231;
	private int UID = 23;
	private int[] neighborPORT = {3332, 2331, 3124};
	boolean isLeader = false;
	
	public static void main(String[] args) {
		DC21 dc21 = new DC21();
		Thread t = new Thread(dc21);
		t.start();
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
