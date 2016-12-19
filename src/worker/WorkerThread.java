package worker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import minimax.Board;

public class WorkerThread implements Runnable {
    private String boardString;
    private PrintWriter printWriter;

    WorkerThread(Socket socket, String aBoardString) throws IOException {
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        boardString = aBoardString;
    }

    public void run() {
        Board board = new Board(boardString, 2);
        String returnString;
        double[] resArray = board.minimaxCalc(true, false);
        double miniValue = 1000;
        for (double aResArray : resArray) {
            if (aResArray < miniValue && aResArray != 0) {
                miniValue = aResArray;
            }
            System.out.println("miniValue: " + miniValue);
            returnString = (boardString + ":" + miniValue);
            printWriter.println(returnString);
        }

    }
}
