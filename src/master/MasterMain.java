package master;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import minimax.Board;

public class MasterMain {
	public static ArrayList<Board> notTerminalBoardList = new ArrayList<Board>();
	private static ArrayList<Connection> connectionList = new ArrayList<Connection>();
	private static double[] moves = new double[Board.COL];
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		getWorkers(args);
		
		String boardString = "";
		Board board = new Board(boardString);
		long startTime = 0, endTime = 0;
		Boolean imputIsNumber = false;
		Boolean helpRecived = false;
		
		do{
			if(threadPool.isShutdown()){
				threadPool = Executors.newCachedThreadPool();
			}
			
			board.display();
			
			if(helpRecived){ //showing result 
				System.out.println();
				for(int i = 0; i < moves.length; i++){
					System.out.println("Value at "+ i + ": " + moves[i]);
				}

				double best = -1000;
				int bestCol = 10;
				int center = 3;
				for(int i = 0; i < board.getCol(); i++){	
					if(moves[i] >= best && moves[i] != 0){ // missing ground rules - center first
						if (moves[i] == best && (Math.abs(i - center) > (Math.abs(bestCol - center)))){
							//still best
						} else {
							best = moves[i];
							bestCol = i;
						}
					}
				}
				
				System.out.println("\nThe best move is in " + bestCol );
				System.out.println("\nCalculationtime: " + (endTime - startTime) + " ms");
				helpRecived = false;
			}
			
			System.out.println("\n" + Character.toUpperCase(board.getNextTurn()) + "'s turn\n");
	        System.out.println("Type command: ");
	        String input = keyboard.next();
	        
	        // kunne ikke finde en god metode til at tjekke om inputtet var en int,
	        // så nu behandles som om den er, og elles bliver den grebet,
	        try {
	        	float i = Float.parseFloat(input);
				String tempBoardString;
	        	if(i >= 0 && i <= 6){
		        	tempBoardString = boardString.concat(input);
		        } else {
		        	tempBoardString = input;
		        }
				
				board = new Board(tempBoardString); 
				boardString = tempBoardString; // hvis ikke den brokker sig
				
				imputIsNumber = true;
			} catch (NumberFormatException e) {
				//just a number check
			} catch (ArrayIndexOutOfBoundsException e1) {
				board = new Board(boardString);
			} 
	        
	        if(input.toLowerCase().contains("quit") || input.toLowerCase().equals("q") || input.toLowerCase().contains("exit")){
	        	break;
	        	
			} else if (input.toLowerCase().contains("help") || input.toLowerCase().equals("h")){
				System.out.println("Calculating.. ");

				Date date = new Date();
				startTime = date.getTime();
				
				board.minimaxCalc(false, true);				//creating the borad non terminal boards that has to be distributed
				distributeNotTerminalString(threadPool);	//distributes the boards created in minimaxCalc(false, true);
				
				threadPool.shutdown();
		        while (!threadPool.isTerminated()) {}

		        Date date1 = new Date();
		        endTime = date1.getTime();
		        helpRecived = true;
		        
			} else if (input.toLowerCase().contains("clear") || input.toLowerCase().contains("c")){
				boardString = "";
				board = new Board(boardString);
				
			} else if (!imputIsNumber) {
				System.out.println("Command not recognized");
			}
	        clearConsole();	// skulle virke når den køres i promt 
	        
		} while(true);
		
		keyboard.close();
	}
	
	private static void getWorkers(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split(":");
            Connection aConnection = null;
			
			try {
				aConnection = new Connection(parts[0], parts[1]);
			} catch (UnknownHostException e) {
				System.out.println("The connection to " + parts[0] + ":" + parts[1] + " failed and has been omitted"); //skal ikke ændres til args, så skriver den bare ref
			} catch (ConnectException e) {
				System.out.println("The connection to " + parts[0] + ":" + parts[1] + " failed and has been omitted"); //skal ikke ændres til args, så skriver den bare ref
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			if(aConnection != null){
				connectionList.add(aConnection);
			}
        }
    }
	
	private static void distributeNotTerminalString(ExecutorService threadPool){
		int nextConnection = 0;
//		for(int i = 0; i < notTerminalBoardList.size(); i++){
		while(!notTerminalBoardList.isEmpty()) {
			Board tempBoard = notTerminalBoardList.get(0);//notTerminalBoardList.get(i);
			if(nextConnection == connectionList.size() ){
				nextConnection = 0;
			}
//			System.out.println("Thread "  + i + " at connection " + nextConnection + " starts..");
			try {
				threadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard));
				notTerminalBoardList.remove(tempBoard);
			} catch (IndexOutOfBoundsException e){
				nextConnection = 0;
				threadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard));
				notTerminalBoardList.remove(tempBoard);
			}
//			System.out.println("Thread "  + i + " at connection " + nextConnection + " enden!");
			nextConnection++;
		}	
	}
	
	private static Runnable connectionThread(Connection aConnection, Board tempBoard) {
		Runnable aRunnable = new Runnable(){
	    	public void run() {
//				System.out.println("Thread " + i + " started...");
				try {
//				System.out.println(i + ". in tryBlock");
				//finder board og sender dets boradString og dybde vidre.
				aConnection.getPrintWriter().println(tempBoard.getBoardString());
//				System.out.println("send");
				String recivedString = aConnection.getBufferedReader().readLine();
//				System.out.println("read");
				
				int indexOfColon = recivedString.indexOf(":");
				int col = Character.getNumericValue(recivedString.charAt(indexOfColon-1));
				double branchValue = Double.valueOf(recivedString.substring(indexOfColon+1));
				
				moves[col] = branchValue;
				
//				System.out.println("Thread " + i + ": success");
				} catch (SocketException e) {
					if(connectionList.contains(aConnection)){
						connectionList.remove(aConnection);
					}
					if(connectionList.isEmpty()){ // tom liste, kør lokant
						tempBoard.minimaxCalc(true, false);
					} else {
						System.out.println("køre på " + connectionList.get(0) + "istedet");
						connectionThread(connectionList.get(0), tempBoard);
					}
//					String boardString = tempBoard.getBoardString();
//					int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
//					moves[col] = tempBoard.minimaxCalc();
				} catch (IOException e) {
					e.printStackTrace();
				} 
//				System.out.println("Thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
//	private static Runnable localThread(Board aBoard, int i){
//	    Runnable aRunnable = new Runnable(){
//	    	public void run() {
//				System.out.println("Local thread " + i + " started...");
//				String boardString = aBoard.getBoardString();
//				int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
//				moves[col] = aBoard.minimaxCalc();
//				
//				System.out.println("Local thread " + i + " Terminated!!!!");
//			} 
//	    };
//	    return aRunnable;
//	}
	
    public static void clearConsole()
    {
        try
        {
            final String opperationSystem = System.getProperty("os.name");
            if (opperationSystem.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {}
    }
	
}
