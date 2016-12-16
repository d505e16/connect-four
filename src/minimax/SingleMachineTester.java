package minimax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class SingleMachineTester {

    public static void main(String[] args) {
        long startTime, endTime;
        Date date = new Date();
        startTime = date.getTime();
        
        
        String s = "211565544663324";
        Board board = new Board(s);

       
        board.display();
        System.out.println(Character.toUpperCase(board.getNextTurn()) + "'s turn");
        
		double[] res = board.minimaxCalc(true, false);
		double best = -1000;
		int bestCol = 10;
		int center = 3;
		for(int i = 0; i < board.getCol(); i++){	
			if(res[i] >= best && res[i] != 0){ // missing ground rules - center first
				if (res[i] == best && (Math.abs(i - center) > (Math.abs(bestCol - center)))){
//					System.out.println(i + " the best is still: " + best);
				} else {
					best = res[i];
//					System.out.println(i + " new the best is: " + best);
					bestCol = i;
				}
			}
		}
		
		System.out.println("Best Col is :" + bestCol );
		
        
		
		//print
		System.out.print("(");
		for(int i = 0; i < board.getCol(); i++){
			System.out.print(res[i] + ", ");
		}
		System.out.println(")");

       
        Date date1 = new Date();
        endTime = date1.getTime();
        System.out.println("time used in ms" + (endTime - startTime));
       
    }
    
}
