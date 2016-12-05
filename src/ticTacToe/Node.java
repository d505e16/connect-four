package ticTacToe;

import java.util.*;

public class Node {
    private List<Node> children = null;
    private int level;
    private double value;
    private Board board;
    
    public Node(Board b, int v){
        this.children = new ArrayList<>();
        this.level = b.getBoardString().length(); 
        setValue(v);
        this.board = b;
    }

	public List<Node> getChildren(){
    	return children;
    }
    
    public Board getBoard(){
    	return board;
    }
    
    public String getBoardString() {
		return board.getBoardString();
	}


	private void setValue(double value) {
		this.value = (double)value / (double)level;
	}
	
	public void setValueMinimax(double value){
		this.value = value;
	}

	public double getValue() {
		return value;
	}
		
	public void addChild(Node child) {
        children.add(child);
    }
    
    public void addNodeToTree(){
		if(getBoard().getCol1Length() < 3){
			Board b = new Board(getBoardString().concat("1"));
			Node n = new Node(b, b.calculateValue());
			addChild(n);
			n.addNodeToTree();
		}
		if(getBoard().getCol2Length() < 3){
			Board b = new Board(getBoardString().concat("2"));
			Node n = new Node(b, b.calculateValue());
			addChild(n);
			n.addNodeToTree();
		}
		if(getBoard().getCol3Length() < 3){
			Board b = new Board(getBoardString().concat("3"));
			Node n = new Node(b, b.calculateValue());
			addChild(n);
			n.addNodeToTree();
		}
	}
    
	//Debug tool
    public void printTree(){
		System.out.println("board: " + getBoardString());
		System.out.println("level: " + level);
		System.out.println("value: " + value + "\n");
		System.out.println(getBoardString() + " has " + getChildren().size() + " children:\n");
		printLoop();
    		
    }
    //Debug tool
    private void printLoop(){
    	for(Node t : children){
			System.out.println("board: " + t.getBoardString());
    		System.out.println("level: " + t.level);
    		System.out.println("value: " + t.getValue() + "\n");
    		if(t.getChildren().isEmpty() == false){
    			System.out.println(t.getBoardString() + " has " + t.getChildren().size() + " children:\n");
    			t.printLoop();
    		}
    	}
    }
    
}//end Node
