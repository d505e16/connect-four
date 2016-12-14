package worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import minimax.Board;

public class WorkerThread implements Runnable {
	
	String boardString;
	
	InputStreamReader inputStreamReader; 
    BufferedReader bufferedReader;
    PrintWriter printWriter;
	
	public WorkerThread(Socket socket, String aBoardString) throws IOException {
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
	        printWriter = new PrintWriter(socket.getOutputStream(), true);
	        boardString = aBoardString;
	}

	public void run() {
        Board board = new Board(boardString, 2);
        String returnString = null;
        
        //det sendte boards "forgænger" TODO refaktoricer minimaxCalc(): klam kode....

        double miniValue = board.minimaxCalc();
        System.out.println("miniCalc run: miniValue: " + miniValue);
        
        //sender boardString tilbage med :miniValue eks. 1220125461:-25
        returnString = (boardString + ":" + miniValue);
                
        printWriter.println(returnString);
	}

}
