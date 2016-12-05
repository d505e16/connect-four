package ticTacToe;

public class Seach {
	private static int depth;
	public static void minimax(Node tree){
		depth = 1;
		//double tempValue = 0;
		if(tree.getChildren().isEmpty() == false){
			for(Node t: tree.getChildren()){
				recursiveMinimax(t);
				System.out.println(t.getBoardString() + " has the value: " + t.getValue());
			}
		}
	}// end minimax
	
	private static void recursiveMinimax(Node tree){
			depth =+ 1;
			double tempValue = 0;
			for(Node t: tree.getChildren()){
				if(t.getChildren().isEmpty() == false){
					recursiveMinimax(t);
				}
				if(depth % 2 == 0){
					//min
					if(tempValue > t.getValue()){
						tempValue = t.getValue();
						tree.setValueMinimax(tempValue);
					}
				} else {
					//max
					if(tempValue < t.getValue()){
						tempValue = t.getValue();
						tree.setValueMinimax(tempValue);
					}
				}
			}
			
		 
	} // end recursiveMinimax
	
	
	
}
