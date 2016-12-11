package Minimax;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinimaxMultiThread extends MinimaxSingleThread {

    public MinimaxMultiThread(Board b) {
        super(b);
//		minimaxCalc();
    }

    public void minimaxCalc() {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < COL; i++) {
            int row = board.firstEmptyInCol(i);
            if (row != -1) {  // når row er -1 hvis rækken er fuld
                Board tempBoard = new Board(this.board.getBoardString().concat(String.valueOf(i)));
                if (tempBoard.isTerminal(row, i)) {
                    //terminal
                    //System.out.println(tempBoard.getBoardString() + " is terminal ");
                    moves[i] = terminalValue(tempBoard);
                } else if (tempBoard.isBoardFull()) {//tie - behøver ikke cutoff i noden
                    moves[i] = 0;
                } else {
                    //not terminal
                    int j = i;

                    threadPool.execute(new Runnable() {
                        public void run() {
                            System.out.println("Thread " + j + " started...");
                            moves[j] = miniCalc(tempBoard, DEPTH + 1);
                            System.out.println("Thread " + j + " Terminated!!!!!");
                        }
                    });
                }
            }
        }

        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
        }
        //print
        System.out.print("(");
        for (int i = 0; i < COL; i++) {
            System.out.print(moves[i] + ", ");
        }
        System.out.println(")");

        //selection
        double best = -1001;
        int bestCol = -1;

        for (int i = 0; i < COL; i++) {
            if (moves[i] == 75) {
                System.out.println("Zugzwang if placing i col: " + i);
            } else if (moves[i] == -100) {
                System.out.println("Opponent wil get Zugzwang if not placed i col: " + i);
            }
        }
        for (int i = 0; i < COL; i++) {
            if (moves[i] >= best && moves[i] != 0) { // missing ground rules - center first
                best = moves[i];
                System.out.println("new the best is: " + best);
                bestCol = i;
            }
        }

        System.out.println("Best move is in col " + bestCol);
    }

}
