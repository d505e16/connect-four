package worker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import minimax.Board;

public class WorkerThread implements Runnable {
	
	String boardString;

    PrintWriter printWriter;
	
	public WorkerThread(Socket socket, String aBoardString) throws IOException {
	        printWriter = new PrintWriter(socket.getOutputStream(), true);
	        boardString = aBoardString;
	}

	public void run() {
        Board board = new Board(boardString, 2);
        String returnString = null;
        
        double[] resArray = board.minimaxCalc(true, false);
        
        double miniValue = 1000;

		for(int i = 0; i < resArray.length; i++){	
			if(resArray[i] < miniValue && resArray[i] != 0){ 
				miniValue = resArray[i];
			}
		}
        
        System.out.println("miniValue: " + miniValue);
        
        //sender boardString tilbage med :miniValue eks. 1220125461:-25
        returnString = (boardString + ":" + miniValue);
                
        printWriter.println(returnString);
	}

}
