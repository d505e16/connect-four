package worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Minimax.Board;
import Minimax.MinimaxMultiThread;
import Minimax.MinimaxSingleThread;

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
        Board board = new Board(boardString);
        String returnString = null;
        
        //det sendte boards "forgænger" TODO refaktoricer minimaxCalc(): klam kode....
        Board motherBoard = new Board(boardString.substring(0, boardString.length()-1));
        
        System.out.println("borad.getBoardString: " + motherBoard.getBoardString());
        System.out.println("borad.getBoardString: " + board.getBoardString());
        
        MinimaxMultiThread mm = new MinimaxMultiThread(motherBoard);
//        MinimaxSingleThread mm = new MinimaxSingleThread(motherBoard);
        System.out.println("DoubleRecurcive created");
        double miniValue = mm.miniCalc(board, 2);
        System.out.println("miniCalc run: miniValue: " + miniValue);
        
        //sender boardString tilbage med :miniValue eks. 1220125461:-25
        returnString = (boardString + ":" + miniValue);
                
        printWriter.println(returnString);
        //printWriter.printf(recivedBoardString, branchValue);
        System.out.println("after print");
	}

}
