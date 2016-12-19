package minimax;

import java.util.Date;

public class SingleMachineTester {
    public static void main(String[] args) {
        long startTime, endTime;
        Date date = new Date();
        startTime = date.getTime();
        String s = "333333422224444245215";
        Board board = new Board(s);
        board.display();
        System.out.println(Character.toUpperCase(board.getNextTurn()) + "'s turn");
        double[] res = board.minimaxCalc(true, false);
        double best = -1000;
        int bestCol = 10;
        for (int i = 0; i < board.getCol(); i++) {
            if (res[i] >= best) {
                best = res[i];
                bestCol = i;
            }
        }

        System.out.println("Best Col is :" + bestCol);

        System.out.print("(");
        for (int i = 0; i < board.getCol(); i++) {
            System.out.print(res[i] + ", ");
        }
        System.out.println(")");

        Date date1 = new Date();
        endTime = date1.getTime();
        System.out.println("time used in ms" + (endTime - startTime));
    }
}
