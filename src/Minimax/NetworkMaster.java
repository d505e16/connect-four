package Minimax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetworkMaster extends DoubleRecurcive {
	private ArrayList<Board> notTerminalBoardList;
	private ArrayList<Connection> connectionList;
	
	public NetworkMaster(Board b) {
		super(b);
		connectionList = new ArrayList<Connection>();
	}
	
	public NetworkMaster(Board b, String[] args){
		super(b);
		notTerminalBoardList = new ArrayList<Board>();
		connectionList = new ArrayList<Connection>();
		getWorkers(args);
	}
	
	private void getWorkers(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split(":");
            Connection aConnection = new Connection(parts[0], parts[1]);
            connectionList.add(aConnection);
        }
    }
	
	public void minimaxCalc(){
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		int terminalCounter = 0;
		
		
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
					//not terminal
					terminalCounter += 1;
					notTerminalBoardList.add(tempBoard); 
				}
			}
		}
		
		//distebuting threads i terminalThreadString list
		if(terminalCounter > 0){
			//TODO 3 er lidt magisk, skal lave et tjek på hvilket sockets der kører
			int numOfLocalThreads = terminalCounter % 3;
			int numOfWorkerThreads = (terminalCounter-numOfLocalThreads) / 3;	
			
			int nextConnection = 0;
			
			for(int i = 0; i < notTerminalBoardList.size(); i++){
				if(i == 0){
					threadPool.execute(localThread(i));
				} else {
					System.out.println("Thread "  + i + " at connection " + nextConnection + " starts..");
					threadPool.execute(connectionThread(connectionList.get(nextConnection).getPrintWriter(), connectionList.get(nextConnection).getBufferedReader(), i));
					System.out.println("Thread "  + i + " at connection " + nextConnection + " enden!");
					if(nextConnection == connectionList.size() - 1){
						nextConnection = 0;
					} else {
						nextConnection++;
					}
				}
			}	

			
			
		}


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
	
	private Runnable connectionThread(PrintWriter aPrintWriter, BufferedReader aBufferedReader, int i){
	    Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Thread " + i + " started...");
				try {
				System.out.println(i + ". in tryBlock");
				//finder board og sender dets boradString vidre.
				aPrintWriter.println(notTerminalBoardList.get(i).getBoardString());
				System.out.println("send");
				String recivedString = aBufferedReader.readLine();
				System.out.println("read");
				
				//TODO evt. lav metode
				int indexOfColon = recivedString.indexOf(":");
				int col = Character.getNumericValue(recivedString.charAt(indexOfColon-1));
				double branchValue = Double.valueOf(recivedString.substring(indexOfColon+1));
				
				moves[col] = branchValue;
				
				System.out.println("Thread " + i + ": success");
				} catch (IOException e) {
					e.printStackTrace();
				} 
				System.out.println("Thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
	//TODO skal msåke laves oppe ved terminal tjekket..
	private Runnable localThread(int i){
	    Runnable aRunnable = new Runnable(){
	    	public void run() {
				System.out.println("Local thread " + i + " started...");
				
				//TODO skal måske finde index i stringen som ved de andre
				moves[i] = miniCalc(notTerminalBoardList.get(i), DEPTH + 1);
		
				System.out.println("Local thread " + i + " Terminated!!!!");
			} 
	    };
	    return aRunnable;
	}
	
	public static void main(String args[]) {
		Board b = new Board("01011010232332324545545466666102");
//		NetworkMaster mnm = new NetworkMaster(b);
		NetworkMaster mnmArgs = new NetworkMaster(b, args);
//		mnm.minimaxCalc();
		mnmArgs.minimaxCalc();
		b.display();
	}
}
