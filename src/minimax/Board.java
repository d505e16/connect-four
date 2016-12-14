package minimax;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import master.MasterMain;

public class Board {

    private final int ROW = 6;
	private final int COL = 7;
	private final int MAX_DEPTH = 10;
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
    
    public int getMaxDepth(){
    	return MAX_DEPTH;
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
    				System.out.print("\t|");
    			} else {
        			System.out.print("   " +  Character.toUpperCase(board[i][j]) + "   |");	
    			}
    		}
    		System.out.println();
    		System.out.println("|_______|_______|_______|_______|_______|_______|_______|");
    	}
    	System.out.println("    1       2       3       4       5        6      7");
    }
    
    

    //---------------------- Minimax Section ---------------------//
     
    public double[] minimaxCalc(Boolean multiThread, Boolean distabute){
    	double[] moves = new double[COL];
        ExecutorService threadPool = Executors.newCachedThreadPool();
        
		for(int i = 0; i < COL; i++){
			int row = firstEmptyInCol(i);
			if(row != -1){  // når row er -1 hvis rækken er fuld
				Board tempBoard = new Board(BOARD_STRING.concat(String.valueOf(i)), DEPTH + 1);
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					//System.out.println(tempBoard.getBoardString() + " is terminal ");
					moves[i] = terminalValue();
				} else if(tempBoard.isBoardFull()) {//tie - behøver ikke cutoff i noden 
					moves[i] = 1;
				} else {
					if(distabute){
						MasterMain.notTerminalBoardList.add(tempBoard);
					} else if(multiThread){
						int j = i;
	                    threadPool.execute(new Runnable() {
	                        public void run() {
	                            System.out.println("Thread " + j + " started...");
	                            moves[j] = tempBoard.minimaxCalc();
	                            System.out.println("Thread " + j + " Terminated!!!!!");
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
		
		int terminalCounter = 0;
		int minOrMaxIdentifier = DEPTH % 2; 
		double tempValue;
		if (minOrMaxIdentifier == 0){
			tempValue = 1000; // finder min
		} else {
			tempValue = -1000; //finder max
		}
		for(int i = 0; i < COL; i++){
			int row = firstEmptyInCol(i);
			if(row != -1){
				Board tempBoard = new Board(BOARD_STRING.concat(String.valueOf(i)), DEPTH + 1);
				if(tempBoard.isTerminal(row, i)){ 
					//terminal
					terminalCounter++;
					tempValue = findMinOrMax(minOrMaxIdentifier, tempValue, terminalValue());
				} else if(tempBoard.isBoardFull()) {//tie - TODO cutoff
					tempValue = 1;//findMinOrMax(minOrMaxIdentifier, tempValue, 50/DEPTH);
				} else if (DEPTH == MAX_DEPTH){
					tempValue = 1;//findMinOrMax(minOrMaxIdentifier, tempValue, 50/DEPTH);
				} else {
					tempValue = findMinOrMax(minOrMaxIdentifier, tempValue, tempBoard.minimaxCalc());
				}
			}
		}
		
		if(terminalCounter >= 2){
			if(minOrMaxIdentifier == 0){
				tempValue = -99/DEPTH; //ikke sikke på at dybden skal med
			} else {
				tempValue = 99/DEPTH; 
			}
		} 
		
		return tempValue;
	}

	private double findMinOrMax(int identifier, double oldValue, double newValue) {
		if(identifier == 0){
			return Math.min(oldValue, newValue);
		} else {
			return Math.max(oldValue, newValue);
		}
	}

	private double terminalValue() {
		if(DEPTH % 2 == 1){
			return 100/DEPTH; //our turn
		} else {
			return -100/DEPTH; //opp turn
		}
	}

    
}// end board
