package master;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import minimax.Board;

public class MasterMain {
	public static ArrayList<Board> notTerminalBoardList = new ArrayList<Board>();
	private static ArrayList<Connection> connectionList = new ArrayList<Connection>();
	private static double[] moves = new double[Board.COL];
	
	public static void main(String[] args) {
		long startTime, endTime;
		Date date = new Date();
		startTime = date.getTime();
		
		ExecutorService threadPool = Executors.newCachedThreadPool();

		getWorkers(args);
		Board board = new Board("2365245"); // skal hentes fra input
		
		board.minimaxCalc(false, true);
		
		distributeNotTerminalString(threadPool);
		
		threadPool.shutdown();
        while (!threadPool.isTerminated()) {}
		
		System.out.println("notTerminalBoardList.size: " + notTerminalBoardList.size());
		
		System.out.print("(");
		for(int i = 0; i < moves.length; i++){
			System.out.print(moves[i] + ", ");
		}
		System.out.println(")");
		
		board.display();
        System.out.println("\nWe are " + board.getNextTurn() + "\n");
        Date date1 = new Date();
        endTime = date1.getTime();
        System.out.println("time used in ms" + (endTime - startTime));
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
		for(int i = 0; i < notTerminalBoardList.size(); i++){
			Board tempBoard = notTerminalBoardList.get(i);
//			if(nextConnection == connectionList.size()){
//				System.out.println("Thread "  + i + " local starts..");
//				threadPool.execute(localThread(tempBoard, i));
//				System.out.println("Thread "  + i + " local ends");
//				nextConnection = 0;
//			} else {
			if(nextConnection == connectionList.size() ){
				nextConnection = 0;
			}
			System.out.println("Thread "  + i + " at connection " + nextConnection + " starts..");
			threadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard, i));
			System.out.println("Thread "  + i + " at connection " + nextConnection + " enden!");
			nextConnection++;
			
		}	
	}
	
	private static Runnable connectionThread(Connection aConnection, Board tempBoard, int i){
		Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Thread " + i + " started...");
				try {
				System.out.println(i + ". in tryBlock");
				//finder board og sender dets boradString vidre.
				aConnection.getPrintWriter().println(tempBoard.getBoardString());
				System.out.println("send");
				String recivedString = aConnection.getBufferedReader().readLine();
				System.out.println("read");
				
				//TODO evt. lav metode
				int indexOfColon = recivedString.indexOf(":");
				int col = Character.getNumericValue(recivedString.charAt(indexOfColon-1));
				double branchValue = Double.valueOf(recivedString.substring(indexOfColon+1));
				
				moves[col] = branchValue;
				
				System.out.println("Thread " + i + ": success");
				} catch (SocketException e) {
					System.out.println("Socket connection lost");
					if(connectionList.contains(aConnection)){
						System.out.println("connectionList.size before: " + connectionList.size());
						connectionList.remove(aConnection);
						System.out.println("connectionList.size after: " + connectionList.size());
					}
					System.out.println("Just running it locally!");
					String boardString = tempBoard.getBoardString();
					int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
					moves[col] = tempBoard.minimaxCalc();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				System.out.println("Thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
	private static Runnable localThread(Board aBoard, int i){
	    Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Local thread " + i + " started...");
				String boardString = aBoard.getBoardString();
				int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
				moves[col] = aBoard.minimaxCalc();
				
				System.out.println("Local thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
}
