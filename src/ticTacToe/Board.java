package ticTacToe;

public class Board {
	
	private String board;
	private String row1, row2, row3, col1, col2, col3, diaL2R, diaR2L;
	private double value;
	
	public static void main(String[] args){
		
		Board b1 = new Board("1122");
		Board b2 = new Board("1223");
		Board b3 = new Board("1232213");
		Board b4 = new Board("122333");
		Board b5 = new Board("12");
		/*
		Board c1 = new Board("12323");
		Board c2 = new Board("12322");
		Board c3 = new Board("12321");
		
		Board c1c1 = new Board("123231");
		Board c1c2 = new Board("123232");
		Board c1c3 = new Board("123233");
		
		////////////////////////////////////////////
		
		Node t2 = new Node(p1, p1.calculateValue());
		
		Node tc1 = new Node(c1, c1.calculateValue());
		Node tc2 = new Node(c2, c2.calculateValue());
		Node tc3 = new Node(c3, c3.calculateValue());
		
		t2.addChild(tc1);
		t2.addChild(tc2);
		t2.addChild(tc3);

		Node tc1c1 = new Node(c1c1, c1c1.calculateValue());
		Node tc1c2 = new Node(c1c2, c1c2.calculateValue());
		Node tc1c3 = new Node(c1c3, c1c3.calculateValue());

		tc1.addChild(tc1c1);
		tc1.addChild(tc1c2);
		tc1.addChild(tc1c3);
		*/
		//////////////////////////////////////////////////
		
		Node tree3 = b3.makeTree();
		//tree.printTree();
		Seach.minimax(tree3);
		
		System.out.println("////////////////////////////////////////");
		Node tree1 = b1.makeTree();
		Seach.minimax(tree1);
		
		System.out.println("////////////////////////////////////////");
		Node tree2 = b2.makeTree();
		Seach.minimax(tree2);
		
		System.out.println("////////////////////////////////////////");
		Node tree4 = b4.makeTree();
		Seach.minimax(tree4);
		
		System.out.println("////////////////////////////////////////");
		Node tree5 = b5.makeTree();
		Seach.minimax(tree5);
		
		
	}//end main

	Board(String s) {
		this.board = s;
		this.value = calculateValue();
	}
	
	public String getBoardString() {
		return board;
	}
	
	public double getValue() {
		return value;
	}

	/*public void setValue(int value) {
		this.value = value;
	}*/

	public int getCol1Length(){
		return col1.length();
	};
	
	public int getCol2Length(){
		return col2.length();
	};
	
	public int getCol3Length(){
		return col3.length();
	};
	
	void possibleWinners(String s){
		int ones = 0, twos = 0,  threes = 0;
		this.row1 = "";
		this.row2 = "";
		this.row3 = "";
		this.col1 = "";
		this.col2 = "";
		this.col3 = "";
		this.diaL2R = "";
		this.diaR2L = "";
		
		for(int i = 0; i < s.length(); i++){
			String temp = Character.toString(s.charAt(i));
			String player;
			if( i % 2 == 0){
				player = "x";
			} else {
				player = "o";
			}
			//construction of possible winners:
			if(temp.equals("1")){
				ones++;
				if(ones == 1){
					row1 = row1.concat(player);
					col1 = col1.concat(player);
					diaL2R = diaL2R.concat(player);
					
				} else if(ones == 2){
					row2 = row2.concat(player);
					col1 = col1.concat(player);
					
				} else if(ones == 3){
					row3 = row3.concat(player);
					col1 = col1.concat(player);
					diaR2L = diaR2L.concat(player);
				}
				
			} else if(temp.equals("2")){
				twos++;
				if(twos == 1){
					row1 = row1.concat(player);
					col2 = col2.concat(player);
					
				} else if(twos == 2){
					row2 = row2.concat(player);
					col2 = col2.concat(player);
					diaL2R = diaL2R.concat(player);
					diaR2L = diaR2L.concat(player);
					
				} else if(twos == 3){
					row3 = row3.concat(player);
					col2 = col2.concat(player);
					diaR2L = diaR2L.concat(player);
				}
				
			} else if(temp.equals("3")){
				threes++;
				if(threes == 1){
					row1 = row1.concat(player);
					col3 = col3.concat(player);
					diaR2L = diaR2L.concat(player);
					
				} else if(threes == 2){
					row2 = row2.concat(player);
					col3 = col3.concat(player);
					
				} else if(threes == 3){
					row3 = row3.concat(player);
					col3 = col3.concat(player);
					diaL2R = diaL2R.concat(player);	
				}
			}
		}
	}//end posibleWinners
	
	private int winValueSingleString(String s){
		if(s.length() == 3 && s.charAt(0) == s.charAt(1) && s.charAt(0) == s.charAt(2)){
			if(s.charAt(0) == 'x'){
				//X wins"
				return 1;
			}else{
				//o wins
				return -1;
			} 
		}
		return 0;
	}// end win winValueSingleString
	
	private int winValue(){
		int value = winValueSingleString(row1) + winValueSingleString(row2) + winValueSingleString(row3) + 
					winValueSingleString(col1) + winValueSingleString(col2) + winValueSingleString(col3) +
					winValueSingleString(diaR2L) + winValueSingleString(diaL2R);
		return value;
	} // end winValue
	
	public int calculateValue() {
		possibleWinners(board);
		return winValue();
	} //end evaluate
	
	
	public Node makeTree(){
		Board b = new Board(board);
		Node tree = new Node(b, calculateValue());
		tree.addNodeToTree();
		return tree;
    }	
}//end Demo
