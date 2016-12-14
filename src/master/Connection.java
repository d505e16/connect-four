package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	private Socket socket;
	private PrintWriter printWriter;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private int port;
	private String adress;
	
	public Connection(String anAdress, String aPort) throws UnknownHostException, IOException {
		this.adress = anAdress;
		this.port = Integer.valueOf(aPort);

		socket = new Socket(adress, port);
		printWriter = new PrintWriter(socket.getOutputStream(), true);
		inputStreamReader = new InputStreamReader(socket.getInputStream());
		bufferedReader = new BufferedReader(inputStreamReader);
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}


}
