package connectFour;

public class Minimax {
	protected final int col, depth, row;
	protected final Character player;
	protected Board board;
	protected double[] moves;
	
	
	Minimax(Board b){
		this.board = b;
		this.row = b.getRow();
		this.col = b.getCol();	
		this.player = b.getPlayer();
		moves = new double[col];
		depth = 1;
		minimaxCalc();
	}
	
	
	protected void minimaxCalc(){	
		System.out.println(board.getBoardString());
		
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
					moves[i] = getBoardValue(tempBoard, depth);
				}
			} 
		}
		printMoveValues();
	}
	
	protected double getBoardValue(Board board, int depth) {
		int tempDepth = depth + 1;
		double[] moves = new double[col];
		
		if (tempDepth <= 10) { //a depth of 11 corresponds to 5 moves ahead for each player 
			for (int i = 0; i < col; i++) {
				int row = board.firstEmptyInCol(i);
				if (row != -1) {
					Board tempBoard = new Board(board.getBoardString().concat(String.valueOf(i)));
					if (tempBoard.isTerminal(row, i)) {
						//terminal
						//System.out.println(tempBoard.getBoardString() + " is terminal");
						moves[i] = terminalValue(depth, tempBoard.getPlayer()) / tempDepth;
					} else {
						//not terminal
						//System.out.println(tempBoard.getBoardString() + " is NOT terminal");
						moves[i] = getBoardValue(tempBoard, tempDepth);
					}
				}
				//reset board??
			} 
		}
		if(tempDepth % 2 == 0){
			//System.out.print("depth: " + tempDepth + ": ");
			return findMin(moves);
		} else {
			//System.out.print("depth: " + tempDepth + ": ");
			return findMax(moves);
		}
	}
	
	protected int terminalValue(int depth, Character playerInTurn){
		if(this.player == playerInTurn){
			return -100;
		} else {
			return 100; 
		}
	}

	private double findMin(double[] moves) {
		double result = 0.0;
		//System.out.println("finding MIN in: ");
		for(int i = 0; i < moves.length; i++){
			//System.out.print("(" + i + ": " + moves[i] + ") ,");
			if(result > moves[i]){
				result = moves[i];
			}
		}
		//System.out.println("" + result + " is the smallest\n");
		return result;
	}
	
	private double findMax(double[] moves) {
		double result = 0.0;
		//System.out.println("finding MAX in: ");
		for(int i = 0; i < moves.length; i++){
			//System.out.print("(" + i + ": " + moves[i] + ") ,");
			if(result < moves[i]){
				result = moves[i];
			}
		}
		//System.out.println("" + result + " is the greatest\n");
		return result;
	}
	
	protected void printMoveValues(){
		for(int i = 0; i < moves.length; i++){
			System.out.println("If player '" + player + "' places a tile at " + i + " the value is:" + moves[i]);
		}
	}
	
}//end Minimax
