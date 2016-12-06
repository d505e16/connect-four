package Minimax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkMaster extends DoubleRecurcive {
	public Socket socket;
	private PrintWriter printWriter;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	
	public NetworkMaster(Board b) {
		super(b);
		connectSocket();
	}
	
	private void connectSocket(){
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
					//not terminal
					int j = i;
					
					threadPool.execute(new Runnable() {
						public void run() {
							System.out.println("Thread " + j + " started...");
							try {
							printWriter.println(tempBoard.getBoardString()); // write to localhost
							System.out.println("send");
							String recivedString = bufferedReader.readLine();
							System.out.println("read");
//							double branchValue = Double.valueOf(recivedString);
//							System.out.println("Min value for " + tempBoard.getBoardString() + " is: " + branchValue);
							
							//Spliter recivedString 1220125461:-25 finder placeringen for :, (:-1) = col , (:+res)=value 
							//TODO lav metode
							int indexOfColon = recivedString.indexOf(":");
							int col = Character.getNumericValue(recivedString.charAt(indexOfColon-1));
							double branchValue = Double.valueOf(recivedString.substring(indexOfColon+1));
							
							moves[col] = branchValue;
							
							System.out.println("Thread " + j + ": success");
							} catch (IOException e) {
								e.printStackTrace();
							} // from localhost
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

	
	public static void main(String args[]) {
		Board b = new Board("01011010232332324545545466666102");
		NetworkMaster mnm = new NetworkMaster(b);
//		mnm.communicateWithServer("012345601234560123456012345613");
		mnm.minimaxCalc();
		b.display();
	}
}
