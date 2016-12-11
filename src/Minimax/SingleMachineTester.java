package Minimax;

import java.util.Date;

public class SingleMachineTester {

    public static void main(String[] args) {
        long startTime, endTime;
        Date date = new Date();
        startTime = date.getTime();
        String s = "010110102323323245455454666661";
        Board board = new Board(s);

        System.out.println("We are " + board.getNextTurn());

//		MinimaxSingleThread mm = new MinimaxSingleThread(board); 
//		mm.minimaxCalc();

		MinimaxMultiThread mm = new MinimaxMultiThread(board); 
		mm.minimaxCalc();
		
        board.display();
        System.out.println("\nWe are " + board.getNextTurn() + "\n");
        Date date1 = new Date();
        endTime = date1.getTime();
        System.out.println("time used in ms" + (endTime - startTime));
    }
}
