package connectFour;

public class Board {
	
	private final int ROW = 6, COL = 7;
	private final String BOARD_STRING;
	private Character[][] board;
	private Character player; 
	
	Board(String s){
		BOARD_STRING = s;
		board = new Character[this.ROW][this.COL];
		createBoard(s);
	}
	
	public void setPlayer(Character player) {
		this.player = player;
	}

	public String getBoardString(){
		return BOARD_STRING;
	}

	public int getRow(){
		return this.ROW;
	}
	
	public int getCol(){
		return this.COL;
	}
	
	public Character getPlayer(){
		return player;
	}// end getPlayer
	
	public Character getNextTurn(){
		if(BOARD_STRING.length() % 2 == 0){
			return 'x';
		} else {
			return 'o';
		}
	}
	
	private void createBoard(String s){
		Character player; 
		for(int i = 0; i < s.length(); i++){
			int col = Character.getNumericValue(s.charAt(i));
			int row = firstEmptyInCol(col);
			if(row != -1){
				if( i % 2 == 0){
					player = 'x'; 
				} else{
					player = 'o';
				}
				setPlayer(player);
				board[row][col] = player;
			} else {
				System.out.println("Too many tiles in col" + col);
			}
		}
	}//end createBoard

	public int firstEmptyInCol( int n ) {
		for(int i = 0; i < ROW; i++){
			if(board[i][n] == null){
				return i;
			}
		}
		return -1;
	}// end firstEmptyIncol
	
	public boolean isTied(int tempDepth, int maxDepth) {
		if(BOARD_STRING.length() == (ROW*COL)){
			return true;
		} else if(tempDepth == maxDepth){
			return true;
		}
		return false;
	}
	
	public Boolean isTerminal(int row, int col){
		boolean winnerFound = false;
		if(horizontalWinCheck(row, col)){
			winnerFound = true;
		} else if(vertiaclWinCheck(row, col)){
			winnerFound = true;
		} else if(diagonalLeftToRight(row, col)){
			winnerFound = true;
		} else if(diagonalRightToLeft(row, col)){
			winnerFound = true;
		} 
		return winnerFound;
	}

	private boolean horizontalWinCheck(int tempRow, int tempCol) {
		int connectedTiles = 0;
		int iteratorDown = tempCol;
		int iteratorUp = tempCol + 1;
		Character player = board[tempRow][tempCol];
		
		while(iteratorDown >= 0 && board[tempRow][iteratorDown] != null && board[tempRow][iteratorDown].equals(player) ){
			connectedTiles++;
			iteratorDown--;
		}
		while(iteratorUp < this.COL && board[tempRow][iteratorUp] != null && board[tempRow][iteratorUp].equals(player)){
			connectedTiles++;
			iteratorUp++;
		}
		
		if(connectedTiles >= 4){
			return true;
		}
		return false;
	}//end horizontalWinCheck
	
	private boolean vertiaclWinCheck(int tempRow, int tempCol) {
		int connectedTiles = 0;
		int iteratorDown = tempRow;
		int iteratorUp = tempRow + 1;
		Character player = board[tempRow][tempCol];
		
		while(iteratorDown >= 0 && board[iteratorDown][tempCol] != null && board[iteratorDown][tempCol].equals(player) ){
			connectedTiles++;
			iteratorDown--;
		}
		while(iteratorUp < this.ROW && board[iteratorUp][tempCol] != null && board[iteratorUp][tempCol].equals(player)){
			connectedTiles++;
			iteratorUp++;
		}
		if(connectedTiles >= 4){
			return true;
		}
		return false;
	}//end vertiaclWinCheck	
	

	private boolean diagonalLeftToRight(int tempRow, int tempCol) {
		int connectedTiles = 0;
		int iteratorDownRow = tempRow;
		int iteratorDownCol = tempCol;
		
		int iteratorUpRow = tempRow + 1;
		int iteratorUpCol = tempCol + 1;
		
		Character player = board[tempRow][tempCol];
		
		while(iteratorDownRow >= 0 && iteratorDownCol >= 0 && board[iteratorDownRow][iteratorDownCol] != null && board[iteratorDownRow][iteratorDownCol].equals(player) ){
			connectedTiles++;
			iteratorDownRow--;
			iteratorDownCol--;
		}
		while(iteratorUpRow < this.ROW && iteratorUpCol < this.COL && board[iteratorUpRow][iteratorUpCol] != null && board[iteratorUpRow][iteratorUpCol].equals(player)){
			connectedTiles++;
			iteratorUpRow++;
			iteratorUpCol++;
		}
		
		if(connectedTiles >= 4){
			return true;
		}
		return false;
	}
	
	private boolean diagonalRightToLeft(int tempRow, int tempCol) {
		int connectedTiles = 0;
		int iteratorDownRow = tempRow;
		int iteratorDownCol = tempCol;
		
		int iteratorUpRow = tempRow + 1;
		int iteratorUpCol = tempCol - 1;
		
		Character player = board[tempRow][tempCol];
		
		while(iteratorDownRow >= 0 && iteratorDownCol < this.COL && board[iteratorDownRow][iteratorDownCol] != null && board[iteratorDownRow][iteratorDownCol].equals(player) ){
			connectedTiles++;
			iteratorDownRow--;
			iteratorDownCol++;
		}
		while(iteratorUpRow < this.ROW && iteratorUpCol >= 0 && board[iteratorUpRow][iteratorUpCol] != null && board[iteratorUpRow][iteratorUpCol].equals(player)){
			connectedTiles++;
			iteratorUpRow++;
			iteratorUpCol--;
		}
		
		if(connectedTiles >= 4){
			return true;
		}
		return false;
	}//end diagonalRightToLeft
	
	public Boolean isBoardFull(){
		if(BOARD_STRING.length() == (this.COL * this.ROW)){
			return true;
		} else {
			return false;
		}
	}

	public void display(){
		for(int row = 0; row < this.board.length; row++){
			for(int column = 0; column < this.board[row].length; column++){
				System.out.print(this.board[row][column]+ "\t");
			}
			System.out.println();
		}
	}//end display

	
	
}// end board
