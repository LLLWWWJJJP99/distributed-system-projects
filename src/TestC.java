import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestC {
	static int counter = 0;
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 30000);
		socket.bind(new InetSocketAddress("127.0.0.1", 30001));
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream ps = new PrintStream(socket.getOutputStream());
		String line = null;
		if((line = br.readLine()) != null) {
			System.out.println(" Client :" + line);
			ps.println(counter++ + " hello");
		}
	}
	
}
