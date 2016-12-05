package connectFour;

import java.io.*;
import java.net.*;

public class MinimaxClient {
	private final String HOST_NAME;
	private final int PORT_NUMBER;
	private Socket socket;
	
	public MinimaxClient(String hostName, int portNum){
		this.HOST_NAME = hostName;
		this.PORT_NUMBER = portNum;
//		runClient(hostName, portNum);
		createSocket();
	}
	
	private void createSocket(){
		try {
			socket = new Socket(HOST_NAME, PORT_NUMBER);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runClient(String boardString){
		try {
//			Socket socket = new Socket(HOST_NAME, PORT_NUMBER);
			
			
			//run minimax på args (constructer parameter) 
			//i minimax sendes boardString med printWriter til serveren
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(boardString);
			
			//modtager resultatet fra serveren
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String temp = bufferedReader.readLine();
			System.out.println(temp);
			double branchValue = Double.valueOf(temp);
			//branchValue skal gives vidre til minimax
			System.out.println("test: " + branchValue);
			
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
//	public String getBoardString(){
//		return BORAD_STRING;
//	}
	
//	public static void main(String[] args) {
//		MinimaxClient mc = new MinimaxClient("localhost", 4444);
//		mc.runClient("012345601234560123456012345613");
//	}

}
