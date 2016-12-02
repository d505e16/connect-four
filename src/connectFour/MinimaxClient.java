package connectFour;

import java.io.*;
import java.net.*;

public class MinimaxClient {
	//private final String BORAD_STRING;
	
	public MinimaxClient(String s){
		//BORAD_STRING = s;
		runClient(s);
	}
	
	private void runClient(String boardString){
		try {
			Socket socket = new Socket("localhost", 4444);
			
			
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
	
	public static void main(String[] args) {
		MinimaxClient mc = new MinimaxClient("012345601234560123456012345611");
		
//		System.out.println(args[0]);
//		try {
//			Socket socket = new Socket("localhost", 4444);
//			
//			
//			//run minimax på args (constructer parameter) 
//			//i minimax sendes boardString med printWriter til serveren
//			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//			printWriter.println(args[0]);
//			
//			//modtager resultatet fra serveren
//			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			String temp = bufferedReader.readLine();
//			System.out.println(temp);
//			double branchValue = Double.valueOf(temp);
//			//branchValue skal gives vidre til minimax
//			System.out.println("test: " + branchValue);
//			
//			socket.close();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
