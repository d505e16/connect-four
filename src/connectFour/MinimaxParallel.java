package connectFour;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinimaxParallel extends Minimax {
	
	public MinimaxParallel(Board b) {
		super(b);
	}
	
	protected void minimaxCalc(){
		System.out.println(board.getBoardString());

		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		for(int i = 0; i < col; i++){
			int row = board.firstEmptyInCol(i);
			if(row != -1){
				//old board with the rows with free space added to it.
				Board tempBoard = new Board(this.board.getBoardString().concat(String.valueOf(i)));
				if(tempBoard.isTerminal(row, i)){
					//terminal
					System.out.println(tempBoard.getBoardString() + " is terminal");
					moves[i] = terminalValue(depth, tempBoard.getNextTurn()) / depth;
				} else {
					//not terminal
					System.out.println(tempBoard.getBoardString() + " is NOT terminal");
					//threadPool.execute(new MinimaxThread(tempBoard, depth));
					int j = i;
					threadPool.execute(new Runnable() {
						public void run() {
							moves[j] = getBoardValue(tempBoard, depth);
						}
					});					
				}
			} 
		}
		threadPool.shutdown();
		while(!threadPool.isTerminated()){}
		
		printMoveValues();
	}
	
}
