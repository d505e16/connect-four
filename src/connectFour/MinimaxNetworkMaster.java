package connectFour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinimaxNetworkMaster extends MinimaxDoubleRecurcive {
	public Socket socket;
	private PrintWriter printWriter;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	
	public MinimaxNetworkMaster(Board b) {
		super(b);
		try {// lav sockets til servere! - alle tre
			socket = new Socket("localhost", 4444);
			
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public double communicateWithServer(String boardString){//kun fra local nu
//		double branchValue = 0;
//		try {
//			//run minimax på args (constructer parameter) 
//			//i minimax sender boardString med printWriter til serveren
////			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
////			printWriter.println(boardString);
//			
//			//modtager resultatet fra serveren
////			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
////			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
////			String temp = bufferedReader.readLine();
////			System.out.println(temp); // test print
//			
////			branchValue = Double.valueOf(temp);
//			//branchValue skal gives vidre til minimax
////			System.out.println("Max value for " + boardString+ " is: " + branchValue);
//
//			socket.close();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return branchValue;
//	}
	
	public void minimaxCalc(){
		
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
//		threadPool.execute(new Runnable() {
//			public void run() {
//				System.out.println("Thread " + 1 + " started...");
//				double temp = communicateWithServer(board.getBoardString());
//				moves[1] = temp;
//				System.out.println("temp is: " + temp); // 
//				System.out.println("Thread " + 1 + " Terminated!!!!!");
//			}
//		});
		
		
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
					int j = i;
					
					threadPool.execute(new Runnable() {
						public void run() {
							System.out.println("Thread " + j + " started...");
							try {
							printWriter.println(tempBoard.getBoardString()); // write to localhost
							System.out.println("send");
							String tempString = bufferedReader.readLine();
							System.out.println("read");
							double branchValue = Double.valueOf(tempString);
							System.out.println("Max value for " + tempBoard.getBoardString() + " is: " + branchValue);
//							double temp = communicateWithServer(board.getBoardString());
							moves[j] = branchValue;//temp;
//							System.out.println("temp is: " + temp); // 
							System.out.println("Thread " + j + ": success");
							} catch (IOException e) {
								e.printStackTrace();
							} // replay from localhost
							System.out.println("Thread " + j + " Terminated!!!!!");
						}
					});
				}
			}
		}

		threadPool.shutdown();
		
		while(!threadPool.isTerminated()){}//blocker til stringene er færdige med at køre!
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

//		while(!threadPool.isTerminated()){}
//		//print
//		System.out.print("(");
//		for(int i = 0; i < COL; i++){
//			System.out.print(moves[i] + ", ");
//		}
//		System.out.println(")");
//		
//		//selection
//		double best = -1001;
//		int bestCol = -1;
//
//		for(int i = 0; i < COL; i++){
//			if(moves[i] == 75){
//				System.out.println("Zugzwang if placing i col: " + i);
//			} else if(moves[i] == -100){
//				System.out.println("Opponent wil get Zugzwang if not placed i col: " + i);
//			} 
//		}
//		for(int i = 0; i < COL; i++){	
//			if(moves[i] >= best && moves[i] != 0){ // missing ground rules - center first
//				best = moves[i];
//				System.out.println("new the best is: " + best);
//				bestCol = i;
//			}
//		}
//		
//		System.out.println("Best move is in col " + bestCol);
	}

	
	public static void main(String args[]) {
		Board b = new Board("012345601234560123456012345613");
		MinimaxNetworkMaster mnm = new MinimaxNetworkMaster(b);
//		mnm.communicateWithServer("012345601234560123456012345613");
		mnm.minimaxCalc();
		
	}
}
