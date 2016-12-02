package connectFour;

public class MinimaxDoubleRecurcive {
	protected final int COL, DEPTH, ROW, MAX_DEPTH;
	protected final Character PLAYER;
	protected Board board;
	protected double[] moves;
	private double returnValue;
	
	public MinimaxDoubleRecurcive(Board b) {
		this.board = b;
		this.ROW = b.getRow();
		this.COL = b.getCol();	
		this.PLAYER = b.getPlayer();
		this.moves = new double[COL];
		this.DEPTH = 1;
		this.MAX_DEPTH = 100;
		this.returnValue = 0;
//		minimaxCalc();
	}
	
	public void minimaxCalc(){
		for(int i = 0; i < COL; i++){
			int row = board.firstEmptyInCol(i);
			if(row != -1){  // når row er -1 hvis rækken er fuld
				Board tempBoard = new Board(this.board.getBoardString().concat(String.valueOf(i)));
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					//System.out.println(tempBoard.getBoardString() + " is terminal ");
					moves[i] = terminalValue(tempBoard);
				} else if(tempBoard.isBoardFull()) {//tie - behøver ikke cutoff i noden 
					moves[i] = 0;
				} else {
					moves[i] = miniCalc(tempBoard, DEPTH + 1);
				}
			}
		}
		
		//print
		System.out.print("(");
		for(int i = 0; i < COL; i++){
			System.out.print(moves[i] + ", ");
		}
		System.out.println(")");
		
		//selection
		double best = -1001;
		int bestCol = -1;

		for(int i = 0; i < COL; i++){
			if(moves[i] == 1000){
				System.out.println("Zugzwang if placing i col: " + i);
			} else if(moves[i] == -1000/2){
				System.out.println("Opponent wil get Zugzwang if not placed i col: " + i);
			} 
		}
		for(int i = 0; i < COL; i++){	
			if(moves[i] >= best && moves[i] != 0){ // missing ground rules - center first
				best = moves[i];
				System.out.println("new the best is: " + best);
				bestCol = i;
			}
		}
		setReturnValue(best);
		System.out.println("Best move is in col " + bestCol);
	}
	
	protected double miniCalc(Board aBoard, int depth) {
		double[] moves = new double[COL];
		int terminalCounter = 0;
		double mini = 1000;
		for(int i = 0; i < COL; i++){
			int row = aBoard.firstEmptyInCol(i);
			if(row != -1){
				Board tempBoard = new Board(aBoard.getBoardString().concat(String.valueOf(i)));
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					//System.out.println(tempBoard.getBoardString() + " is terminal");
					moves[i] = terminalValue(tempBoard) / depth;
					terminalCounter += 1;
					mini = findMin(mini, moves[i]);
				} else if(tempBoard.isBoardFull()) {//tie - TODO cutoff
					moves[i] = 50/depth;
					mini = findMin(mini, moves[i]);
				} else {
					moves[i] = maxCalc(tempBoard, depth + 1);
					mini = findMin(mini, moves[i]);
				}
			}
		}
		//System.out.print("Min: ");
		if(terminalCounter >= 2){
			//System.out.println("Result: zugzwang " + (-1000/depth));
			return -1000/depth;
		} else {
			//System.out.println("Result: " + mini);
			return mini;	
		}
	}
	
	private double maxCalc(Board aBoard, int depth) {
		double[] moves = new double[COL];
		int counter = 0;
		double max = -1234;
		for(int i = 0; i < COL; i++){
			int row = aBoard.firstEmptyInCol(i);
			if(row != -1){
				Board tempBoard = new Board(aBoard.getBoardString().concat(String.valueOf(i)));
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					//System.out.println(tempBoard.getBoardString() + " is terminal ");
					moves[i] = terminalValue(tempBoard) / depth;
					counter += 1;
					max = findMax(max, moves[i]);
				} else if(tempBoard.isBoardFull()) {//tie - TODO cutoff
					moves[i] = 50/depth;
					max = findMax(max, moves[i]);
				} else {
					moves[i] = miniCalc(tempBoard, depth + 1);
					max = findMax(max, moves[i]);
				}
			}
			
		}
		//System.out.print("max: ");
		if(counter >= 2){
			//System.out.println("Result: zugzwang " + 1000/depth);
			return 1000/depth;
		} else {
			//System.out.println("Result: " + max);
			return max;	
		}
	}

	
	protected int terminalValue(Board aBoard){
		if(PLAYER == aBoard.getNextTurn()){
			return 100;
		} else {
			return -100;
		}
	}
	
	private double findMin(double oldValue, double newValue){
		if(oldValue >= newValue){
			return newValue;
		} else {
			return oldValue;
		}
	}
	
	protected double findMax(double oldValue, double newValue){
		if(oldValue <= newValue){
			return newValue;
		} else {
			return oldValue;
		}
	}
	
	private void setReturnValue(double d){
		this.returnValue = d;
	}
	
	protected double getReturnValue(){
		return returnValue;
	}
}
