package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Minimax.Board;
import Minimax.MinimaxSingleThread;


public class MasterMain extends MinimaxSingleThread {
	private ArrayList<Board> notTerminalBoardList;
	private ArrayList<Connection> connectionList;
	
//	int nextConnection = 0; //TODO check for race conditions
	
	public MasterMain(Board b) {
		super(b);
		connectionList = new ArrayList<Connection>();
	}
	
	public MasterMain(Board b, String[] args){
		super(b);
		notTerminalBoardList = new ArrayList<Board>();
		connectionList = new ArrayList<Connection>();
		getWorkers(args);
	}
	
	//TODO hvis connectionList er tom, bare kør MinimaxMultiThread
	
	private void getWorkers(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split(":");
            Connection aConnection = null;
			
			try {
				aConnection = new Connection(parts[0], parts[1]);
			} catch (UnknownHostException e) {
				System.out.println("The connection to '" + parts[0] + "': " + parts[1] + " failed and has been omitted");
			} catch (ConnectException e) {
				System.out.println("The connection to '" + parts[0] + "': " + parts[1] + " failed and has been omitted");
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			if(aConnection != null){
				connectionList.add(aConnection);
			}
        }
        System.out.println("args: " + args.length);        
        System.out.println("connectionList.size: " + connectionList.size());
    }
	
	public void minimaxCalc(){
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
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
				} else {//test
					notTerminalBoardList.add(tempBoard); 
				}
			}
		}
		
		distabuteNotTerminalString(threadPool);

		threadPool.shutdown();
		
		while(!threadPool.isTerminated()){}//blocker til stringene er færdige med at køre!
		try {
			for(Connection connection: connectionList){
				connection.getSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
			if(moves[i] == 75){
				System.out.println("Zugzwang if placing i col: " + i);
			} else if(moves[i] == -100){
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
		
		System.out.println("Best move is in col " + bestCol);
	}
	
	private void distabuteNotTerminalString(ExecutorService aThreadPool){
		int nextConnection = 0;
		for(int i = 0; i < notTerminalBoardList.size(); i++){
			Board tempBoard = notTerminalBoardList.get(i);
			if(nextConnection == connectionList.size()){
				System.out.println("Thread "  + i + " local starts..");
				aThreadPool.execute(localThread(tempBoard, i));
				System.out.println("Thread "  + i + " local ends");
				nextConnection = 0;
			} else {
				System.out.println("Thread "  + i + " at connection " + nextConnection + " starts..");
				aThreadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard, i));
				System.out.println("Thread "  + i + " at connection " + nextConnection + " enden!");
				nextConnection++;
			}
		}	
	}
	
	private Runnable connectionThread(Connection aConnection, Board aBoard, int i){
		Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Thread " + i + " started...");
				try {
				System.out.println(i + ". in tryBlock");
				//finder board og sender dets boradString vidre.
				aConnection.getPrintWriter().println(aBoard.getBoardString());
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
					String boardString = aBoard.getBoardString();
					int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
					moves[col] = miniCalc(aBoard, DEPTH + 1);
				} catch (IOException e) {
					e.printStackTrace();
				} 
				System.out.println("Thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
	private Runnable localThread(Board aBoard, int i){
	    Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Local thread " + i + " started...");
				String boardString = aBoard.getBoardString();
				int col = Character.getNumericValue(boardString.charAt(boardString.length()-1));
				moves[col] = miniCalc(aBoard, DEPTH + 1);
				
				System.out.println("Local thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
	public static void main(String args[]) {
		long startTime, endTime;
        Date date = new Date();
        startTime = date.getTime();
		
        Board b = new Board("01011010232332324545545466666");
//		NetworkMaster mnm = new NetworkMaster(b);
		MasterMain mnmArgs = new MasterMain(b, args);
//		mnm.minimaxCalc();
		mnmArgs.minimaxCalc();
		b.display();
		
		Date date1 = new Date();
        endTime = date1.getTime();
        System.out.println("time used in ms" + (endTime - startTime));
	}
}
