package connectFour;

import java.io.*;
import java.net.*;

public class MinimaxServer {
	public static void main(String[] args) {
		System.out.println("server ... ");
		try {
			System.out.println("hello1");
			ServerSocket serverSocket = new ServerSocket(4444);
			Socket socket = serverSocket.accept();
			
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream()); 
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String recivedBoardString = bufferedReader.readLine();
			Board board = new Board(recivedBoardString);
			
			MinimaxDoubleRecurcive mm = new MinimaxDoubleRecurcive(board);
			
			//TODO - run minimax on board - should return double (mini or max of this branch)
			double branchValue = mm.getReturnValue();
			
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(String.valueOf(branchValue));
			//printWriter.printf(recivedBoardString, branchValue);
			System.out.println(recivedBoardString);
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
