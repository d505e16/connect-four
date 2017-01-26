package minimax;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import master.MasterMain;

public class Board {

    private final int ROW = 6;
	public static final int COL = 7;
	private final int MAX_DEPTH = 8; // skal nok være 10
    private final String BOARD_STRING;
    private final int DEPTH;
    private Character[][] board;
    private Character player;

    public Board(String s) {
        BOARD_STRING = s;
        DEPTH = 1;
        board = new Character[this.ROW][this.COL];
        createBoard(s);
    }
    
    public Board(String s, int depth) {
        BOARD_STRING = s;
        DEPTH = depth;
        board = new Character[this.ROW][this.COL];
        createBoard(s);
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    public String getBoardString() {
        return BOARD_STRING;
    }

    public int getRow() {
        return this.ROW;
    }

    public int getCol() {
        return this.COL;
    }
    
    public int getDepth(){
    	return this.DEPTH;
    }
    
    public Character getPlayer() {
        return player;
    }// end getPlayer

    public Character getNextTurn() {
        if (BOARD_STRING.length() % 2 == 0) {
            return 'x';
        } else {
            return 'o';
        }
    }

    private void createBoard(String s) {
        Character player;
        for (int i = 0; i < s.length(); i++) {
            int col = Character.getNumericValue(s.charAt(i));
            int row = firstEmptyInCol(col);
            if (row != -1) {
                if (i % 2 == 0) {
                    player = 'x';
                } else {
                    player = 'o';
                }
                setPlayer(player);
                board[row][col] = player;
            } else {
                System.out.println("Too many tiles in col" + col);
            }
        }
    }//end createBoard

    public int firstEmptyInCol(int n) {
        for (int i = 0; i < ROW; i++) {
            if (board[i][n] == null) {
                return i;
            }
        }
        return -1;
    }// end firstEmptyIncol
    
    //TODO change it
    public boolean isTied(int tempDepth, int maxDepth) {
        if (BOARD_STRING.length() == (ROW * COL)) {
            return true;
        } else if (tempDepth == maxDepth) {
            return true;
        }
        return false;
    }

    public Boolean isTerminal(int row, int col) {
        boolean winnerFound = false;
        if (horizontalWinCheck(row, col)) {
            winnerFound = true;
        } else if (vertiaclWinCheck(row, col)) {
            winnerFound = true;
        } else if (diagonalLeftToRight(row, col)) {
            winnerFound = true;
        } else if (diagonalRightToLeft(row, col)) {
            winnerFound = true;
        }
        return winnerFound;
    }

    private boolean horizontalWinCheck(int tempRow, int tempCol) {
        int connectedTiles = 0;
        int iteratorDown = tempCol;
        int iteratorUp = tempCol + 1;
        Character player = board[tempRow][tempCol];

        while (iteratorDown >= 0 && board[tempRow][iteratorDown] != null && board[tempRow][iteratorDown].equals(player)) {
            connectedTiles++;
            iteratorDown--;
        }
        while (iteratorUp < this.COL && board[tempRow][iteratorUp] != null && board[tempRow][iteratorUp].equals(player)) {
            connectedTiles++;
            iteratorUp++;
        }

        return connectedTiles >= 4;
    }//end horizontalWinCheck

    private boolean vertiaclWinCheck(int tempRow, int tempCol) {
        int connectedTiles = 0;
        int iteratorDown = tempRow;
        int iteratorUp = tempRow + 1;
        Character player = board[tempRow][tempCol];

        while (iteratorDown >= 0 && board[iteratorDown][tempCol] != null && board[iteratorDown][tempCol].equals(player)) {
            connectedTiles++;
            iteratorDown--;
        }
        while (iteratorUp < this.ROW && board[iteratorUp][tempCol] != null && board[iteratorUp][tempCol].equals(player)) {
            connectedTiles++;
            iteratorUp++;
        }
        return connectedTiles >= 4;
    }//end vertiaclWinCheck


    private boolean diagonalLeftToRight(int tempRow, int tempCol) {
        int connectedTiles = 0;
        int iteratorDownRow = tempRow;
        int iteratorDownCol = tempCol;

        int iteratorUpRow = tempRow + 1;
        int iteratorUpCol = tempCol + 1;

        Character player = board[tempRow][tempCol];

        while (iteratorDownRow >= 0 && iteratorDownCol >= 0 && board[iteratorDownRow][iteratorDownCol] != null && board[iteratorDownRow][iteratorDownCol].equals(player)) {
            connectedTiles++;
            iteratorDownRow--;
            iteratorDownCol--;
        }
        while (iteratorUpRow < this.ROW && iteratorUpCol < this.COL && board[iteratorUpRow][iteratorUpCol] != null && board[iteratorUpRow][iteratorUpCol].equals(player)) {
            connectedTiles++;
            iteratorUpRow++;
            iteratorUpCol++;
        }

        return connectedTiles >= 4;
    }

    private boolean diagonalRightToLeft(int tempRow, int tempCol) {
        int connectedTiles = 0;
        int iteratorDownRow = tempRow;
        int iteratorDownCol = tempCol;

        int iteratorUpRow = tempRow + 1;
        int iteratorUpCol = tempCol - 1;

        Character player = board[tempRow][tempCol];

        while (iteratorDownRow >= 0 && iteratorDownCol < this.COL && board[iteratorDownRow][iteratorDownCol] != null && board[iteratorDownRow][iteratorDownCol].equals(player)) {
            connectedTiles++;
            iteratorDownRow--;
            iteratorDownCol++;
        }
        while (iteratorUpRow < this.ROW && iteratorUpCol >= 0 && board[iteratorUpRow][iteratorUpCol] != null && board[iteratorUpRow][iteratorUpCol].equals(player)) {
            connectedTiles++;
            iteratorUpRow++;
            iteratorUpCol--;
        }

        return connectedTiles >= 4;
    }//end diagonalRightToLeft

    public Boolean isBoardFull() {
        return BOARD_STRING.length() == (this.COL * this.ROW);
    }
    
    public void display() {
    	System.out.println(" _______________________________________________________");
    	for (int i = ROW-1; i >= 0; i--){
    		System.out.print("|");
    		for (int j = 0; j < COL; j++){
    			if( board[i][j] == null){
    				System.out.print("\t\t|");
    			} else {
        			System.out.print("   " +  Character.toUpperCase(board[i][j]) + "   |");	
    			}
    		}
    		System.out.println();
    		System.out.println("|_______|_______|_______|_______|_______|_______|_______|");
    	}
    	System.out.println("    0       1       2       3       4       5       6");
    }
    
    

    //------------------------------------ Minimax Section ----------------------------------------------//
     
    public double[] minimaxCalc(Boolean multiThread, Boolean distabute){
    	double[] moves = new double[COL];
        ExecutorService threadPool = Executors.newCachedThreadPool();
        
		for(int i = 0; i < COL; i++){
			int row = firstEmptyInCol(i);
			if(row != -1){  // når row er -1 hvis rækken er fuld
				Board tempBoard = new Board(BOARD_STRING.concat(String.valueOf(i)), DEPTH + 1);
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					moves[i] = terminalValue();
				} else if(tempBoard.isBoardFull()) {//tie - behøver ikke cutoff i noden 
					moves[i] = 0; // var 1 før
				} else {
					if(distabute){
						MasterMain.notTerminalBoardList.add(tempBoard);
					} else if(multiThread){
						int j = i;
	                    threadPool.execute(new Runnable() {
	                        public void run() {
//	                            System.out.println("Thread " + j + " started...");
	                            moves[j] = tempBoard.minimaxCalc();
//	                            System.out.println("Thread " + j + " Terminated!!!!!");
	                        }
	                    });
					} else {
						moves[i] = tempBoard.minimaxCalc();
					}
				}
			}
		}
		
		threadPool.shutdown();
        while (!threadPool.isTerminated()) {}
        
		return moves;
	}
    
    
    public double minimaxCalc() {
  
    	double returnValue;
		int minOrMaxIdentifier = DEPTH % 2; 
		if (minOrMaxIdentifier == 0){
			returnValue = 1000; // finder min
		} else {
			returnValue = -1000; //finder max
		}
		for(int i = 0; i < COL; i++){
			int row = firstEmptyInCol(i);
			if(row != -1){
				Board tempBoard = new Board(BOARD_STRING.concat(String.valueOf(i)), DEPTH + 1);
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					returnValue = findMinOrMax(minOrMaxIdentifier, returnValue, terminalValue());
				} else if(tempBoard.isBoardFull()) {
					returnValue = 0; 
				} else if (DEPTH == MAX_DEPTH){
					if(minOrMaxIdentifier == 0){ //kun nødvendig hvis man må ændre på MAX_DEPTH
						returnValue = heuristicValue(player); 			
					} else{
						returnValue = heuristicValue(getNextTurn());
					}
				} else {
					returnValue = findMinOrMax(minOrMaxIdentifier, returnValue, tempBoard.minimaxCalc());
				}
				
			}
		}		
		return returnValue;
	}
    
	private double findMinOrMax(int identifier, double oldValue, double newValue) {
		if(identifier == 0){
			return Math.min(oldValue, newValue);
		} else {
			return Math.max(oldValue, newValue);
		}
	}

	private double terminalValue() { // skal bare være +/-100 - kan laves til int
		if(DEPTH % 2 == 1){
//			return 100/DEPTH; //our turn
			return 100; //representere +uendelig
		} else {
			return -100; //representere -uendelig
//			return -100/DEPTH; //opp turn
		}
	}
	
	 //------------------------------ Heuristic value calculation ----------------------------------//
	
	public double heuristicValue(Character player){ //TODO change to int
		return horizontalValue(player) +
				verticalValue(player) + 
				diagonalGoingRightValue(player) +
				diagonalGoingLeftValue(player);
	}
	
	private int horizontalValue(Character player){ 
		int xWins = 0;
		int oWins = 0;
		for (int row = 0; row < board.length; row++){
			int xCounter = 0;
			int oCounter = 0;
	    	int nullForXCounter = 0;
	    	int nullForOCounter = 0; 
			for (int col = 0; col < board[row].length; col++){ // basic - skal gøres mere elegant
		    	 Character brick = board[row][col];
		    	 if( brick == null ) {
		    		 if(xCounter > 0){
		    			 xCounter++;
		    			 nullForOCounter++;
		    			 if(nullForOCounter == 4){
		    				 xCounter = 0;
		    			 } else if(xCounter == 4){
		    				 xWins++;
				    		 xCounter--;
				    	 }
		    		 } else if(oCounter > 0){
		    			 oCounter++;
		    			 nullForXCounter++;
		    			 if(nullForXCounter == 4){
		    				 oCounter = 0;
		    			 } else if(oCounter == 4){
				    		 oWins++;
				    		 oCounter--;
				    	 }
		    		 } else {
		    			 if(nullForXCounter < 3) {
		    				 nullForXCounter++;
		    			 }
		    			 if(nullForOCounter < 3) {
		    				 nullForOCounter++;
		    			 }
		    		 } 
		    		 
		    	 } else if(brick == 'x'){
		    		 xCounter++;
		    		 xCounter += nullForXCounter;
			    	 nullForXCounter = 0;
			    	 nullForOCounter = 0;
		    		 oCounter = 0;
			    	 if(xCounter == 4){
			    		 xWins++;
			    		 xCounter--;
			    	 }
			    	 
		    	 } else {
		    		 oCounter++;
		    		 oCounter += nullForOCounter;
			    	 nullForOCounter = 0;
			    	 nullForXCounter = 0;
		    		 xCounter = 0;
			    	 if(oCounter == 4){
			    		 oWins++;
			    		 oCounter--;
			    	 } 
		    	 }
		    	  
		     } 

		}
		
		if (player == 'x'){
			return xWins - oWins;
		} else {
			return oWins - xWins;
		}
	}
	
	// køre hver col oppefra og ned..
	// mindre arbejde hvis man kunne kunne vinde en gang op "brakeble"
	private int verticalValue(Character player){ 
		int xWins = 0; 
		int oWins = 0;
		for(int col = 0; col < COL; col++){ 
			Character winner = null;
			int winnerCounter = 0;
	    	int nullCounter = 0;
			
			for(int row = ROW - 1; row >= 0; row--){
				if(board[row][col] == null && winner == null){ // if null
					if(nullCounter < 3){
						nullCounter++;
					}
					
				} else if (board[row][col] == 'x'){ // if x
					if(winner == null || winner == 'x'){
						winner = 'x';
						if(nullCounter > 0){
							winnerCounter += nullCounter;
							nullCounter = 0;
						}
						winnerCounter++;
						
						if(winnerCounter == 4){
							xWins++;
							winnerCounter--;
						}
					} else{
						break;
					}
					
				} else { //if o
					if(winner == null || winner == 'o'){
						winner = 'o';
						if(nullCounter > 0){
							winnerCounter += nullCounter;
							nullCounter = 0;
						}
						winnerCounter++;
						
						if(winnerCounter == 4){
							oWins++;
							winnerCounter--;
						}
					} else{
						break;
					}
				}
				
				
			}
		}
		
		if (player == 'x'){
			return xWins - oWins;
		} else {
			return oWins - xWins;
		}
	}
	
	
	// ikke køn, men hvis det virker... nogle coordinater hentes et par gange..
	private int diagonalGoingRightValue(Character player){		
		int xWins = 0; 
		int oWins = 0;
		for(int row = 0; row < ROW - 3; row++){ 						// der tages udgangspunkt i et coordianlt, så efter col 2 kan man ikke vinde opad
			for(int col = 0; col < COL - 3; col++){ 					// der tages udgangspunkt i et coordianlt, så efter col 4 kan man ikke vinde med højre
				if(board[row][col] == null){
					// moving on
				} else {
					Character winner = board[row][col];					// sætter rækkens mulige vinner
																		// checker om det er en modstander brik i vejen for at vinde:
					if( (board[row+1][col+1] == winner || board[row+1][col+1] == null) &&
						(board[row+2][col+2] == winner || board[row+2][col+2] == null) &&
						(board[row+3][col+3] == winner || board[row+3][col+3] == null)) {
						if(winner == 'x'){
							xWins++;
						} else {
							oWins++;
						}
					} else {
						// moving on
					}
				}
			}
		}
	
		if (player == 'x'){
			return xWins - oWins;
		} else {
			return oWins - xWins;
		}
	}
	
	private int diagonalGoingLeftValue(Character player){
		int xWins = 0; 
		int oWins = 0;
		for(int row = 0; row < ROW - 3; row++){ 						// der tages udgangspunkt i et coordianlt, så efter col 2 kan man ikke vinde opad
			for(int col = COL-1; col > 2; col--){ 						// der tages udgangspunkt i et coordianlt, så efter col 4 kan man ikke vinde med højre
				if(board[row][col] == null){
					// moving on
				} else {
					Character winner = board[row][col];					// sætter rækkens mulige vinner
																		// checker om det er en modstander brik i vejen for at vinde:
					if( (board[row+1][col-1] == winner || board[row+1][col-1] == null) &&
						(board[row+2][col-2] == winner || board[row+2][col-2] == null) &&
						(board[row+3][col-3] == winner || board[row+3][col-3] == null)) {
						if(winner == 'x'){
							xWins++;
						} else {
							oWins++;
						}
					} else {
						// moving on
					}
				}
			}
		}
		
		if (player == 'x'){
			return xWins - oWins;
		} else {
			return oWins - xWins;
		}
	}
	
    
}// end board
