package connectFour;

import java.util.Date;

public class ConnectFourMain {
	
	public static void main(String[] args) {
		long startTime, endTime;
		Date date = new Date();
		startTime = date.getTime();
		String s = "01234560123456012345601234561";
		//her under er til 4*5 board
		//String s = "0101234322323444110";
		//String s = "010123430"; // single thread 10500 ms
		//String s = "010101344322324"; // single thread 20 ms
		//String s = "24310324130200423411";//'o' kan vinde i n�ste runde
		//String s = "23140320140332"; //hvis 2'er s�ttes ender man i double attack til modstanderen - skal l�ses
		//String s = "231403201403322"; //double attack - skal l�ses - 'o' - i 2 og 4 har zugzwang 
		//String s = "2314032014033224";
		//String s = "231403201403324210";
		Board board = new Board(s);
		//System.out.println();
		
		System.out.println("We are " + board.getNextTurn());
		//MinimaxNewValueEstamation mm = new MinimaxNewValueEstamation(board);
		//MinimaxParallel mp = new MinimaxParallel(board);
//		MinimaxDoubleRecurcive mm = new MinimaxDoubleRecurcive(board); mm.minimaxCalc();
		
		MinimaxDoubleRecurciveParallel mp = new MinimaxDoubleRecurciveParallel(board); mp.minimaxCalc();
		System.out.println();
		board.display();
		System.out.println();
		System.out.println("We are " + board.getNextTurn() + "\n");
		Date date1 = new Date();
		endTime = date1.getTime(); 
		System.out.println("time used in ms" + (endTime - startTime));
	}
}
