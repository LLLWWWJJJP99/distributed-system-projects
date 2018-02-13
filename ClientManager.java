import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManager implements Runnable {

	private Socket client;
	
	public ClientManager(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		String line;
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			line = in.readLine();
			while(line != null) {
				System.out.println(line);
				String lline = "Hello From Server";
				out.println(lline);
				System.out.println("Message sent to client");
				line = in.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
