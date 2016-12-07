package Minimax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Worker;

public class NetworkMaster extends DoubleRecurcive {
	public Socket orvalSocket, chimaySocket, westmalleSocket, tempSocket;
	private PrintWriter orvalPrintWriter, chimayPrintWriter, westmallePrintWriter, tempPrintWriter;
	private InputStreamReader orvalInputStreamReader, chimayInputStreamReader, westmalleInputStreamReader, tempInputStreamReader;
	private BufferedReader orvalBufferedReader, chimayBufferedReader, westmalleBufferedReader, tempBufferedReader;
	private ArrayList<String> terminalBoardStrings;
	
	public NetworkMaster(Board b) {
		super(b);
		terminalBoardStrings = new ArrayList<String>();
		connectSocket();
	}
	
	private void connectSocket(){
		//TODO skal ændres til rigtige adresser 
		try {
			//orval - fakeit
			orvalSocket = new Socket("localhost", 4444);
			
			orvalPrintWriter = new PrintWriter(orvalSocket.getOutputStream(), true);
			
			orvalInputStreamReader = new InputStreamReader(orvalSocket.getInputStream());
			orvalBufferedReader = new BufferedReader(orvalInputStreamReader);
			
			//chimay - fakeit
			chimaySocket = new Socket("localhost", 4445);
			
			chimayPrintWriter = new PrintWriter(chimaySocket.getOutputStream(), true);
			
			chimayInputStreamReader = new InputStreamReader(chimaySocket.getInputStream());
			chimayBufferedReader = new BufferedReader(chimayInputStreamReader);
			
			//westmalle - fakeit
			westmalleSocket = new Socket("localhost", 4446);
			
			westmallePrintWriter = new PrintWriter(westmalleSocket.getOutputStream(), true);
			
			westmalleInputStreamReader = new InputStreamReader(westmalleSocket.getInputStream());
			westmalleBufferedReader = new BufferedReader(westmalleInputStreamReader);
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		connectSocket(orvalSocket, "localhost", 4444, orvalPrintWriter, orvalInputStreamReader, orvalBufferedReader);
//		connectSocket(chimaySocket, "localhost", 4445, chimayPrintWriter, chimayInputStreamReader, chimayBufferedReader);
//		connectSocket(westmalleSocket, "localhost", 4446, westmallePrintWriter, westmalleInputStreamReader, westmalleBufferedReader);
	}
	
//	private void connectSocket(Socket aSocket, String name, int port, PrintWriter out, InputStreamReader in, BufferedReader buff){
//		try {
//			aSocket = new Socket(name, port);
//			
//			out = new PrintWriter(aSocket.getOutputStream(), true);
//			
//			in = new InputStreamReader(aSocket.getInputStream());
//			buff = new BufferedReader(in);
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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
					terminalBoardStrings.add(tempBoard.getBoardString()); 
				}
			}
		}
		
		//distebuting threads i terminalThreadString list
		if(terminalCounter > 0){
			//3 er lidt magisk, skal lave et tjek på hvilket sockets der kører
			int numOfLocalThreads = terminalCounter % 3;
			int numOfWorkerThreads = (terminalCounter-numOfLocalThreads) / 3;
			
			/*
			 *	1 terminal  = kør lokalt
			 *  2 terminale = kør 1 lokal + 1 worker
			 *  3 terminale = kør 1 lokal + 1 på 2 worker
			 *  4 terminale = kør 1 lokal + 1 på 3 worker
			 *  
			 *  5 terminale = kør 1 lokal + 1 på 2 worker + 2 på 1 worker
			 *  6 terminale = kør 1 lokal + 1 på 1 worker + 2 på 2 worker
			 *  7 terminale = kør 1 lokal + 2 på 3 worker
			 * */
			
			
			
			//udilleger før - så kør egne tråde
			for(int i = 0; i < terminalCounter; i++){
//			for(String boardString : terminalBoardStrings){
				// TODO skal laves om
				if(i < numOfWorkerThreads * 1){
					System.out.println("Thread" + i + "Orval starts..");
					threadPool.execute(connectionThread(orvalPrintWriter, orvalBufferedReader, i));
					System.out.println("Thread" + i + "Orval ends");
				} else if(i < numOfWorkerThreads * 2){
					System.out.println("Thread" + i + "Chimay starts..");
					threadPool.execute(connectionThread(chimayPrintWriter, chimayBufferedReader, i));
					System.out.println("Thread" + i + "Chimay ends");
//					tempPrintWriter = chimayPrintWriter;
//					tempBufferedReader = chimayBufferedReader;
				} else if(i < numOfWorkerThreads * 3){
					System.out.println("Thread" + i + "Westmalle starts..");
					threadPool.execute(connectionThread(westmallePrintWriter, westmalleBufferedReader, i));
					System.out.println("Thread" + i + "Westmalle ends");
//					tempPrintWriter = westmallePrintWriter;
//					tempBufferedReader = westmalleBufferedReader;
				} else{
					//TODO run local
				}
				
				int j = i; //i bliver ikke givet vidre til tråden

				//TODO Tror jeg bliver nød til at lave en runnable class
//				threadPool.execute(new Runnable() {
//					public void run() {
//						System.out.println("Thread " + j + " started...");
//						try {
//						System.out.println(j + ". in tryBlock");
//						tempPrintWriter.println(terminalBoardStrings.get(j));//terminalBoardStrings.get(j));
////						tempPrintWriter.println(tempBoard.getBoardString()); // write to localhost
//						System.out.println("send");
//						String recivedString = tempBufferedReader.readLine();
//						System.out.println("read");
////						double branchValue = Double.valueOf(recivedString);
////						System.out.println("Min value for " + tempBoard.getBoardString() + " is: " + branchValue);
//						
//						//hvis der bliver sendt flere boards til samme worker, kan man ikke vide hvilken rækkefølge den kommer tilbage i:
//						//Spliter recivedString 1220125461:-25 finder placeringen for :, (:-1) = col , (:+res)=value 
//						//TODO evt. lav metode
//						int indexOfColon = recivedString.indexOf(":");
//						int col = Character.getNumericValue(recivedString.charAt(indexOfColon-1));
//						double branchValue = Double.valueOf(recivedString.substring(indexOfColon+1));
//						
//						moves[col] = branchValue;
//						
//						System.out.println("Thread " + j + ": success");
//						} catch (IOException e) {
//							e.printStackTrace();
//						} // from localhost
//						System.out.println("Thread " + j + " Terminated!!!!");
//					}
//				}); 
				
				
			}	
			
			
		}


		threadPool.shutdown();
		
		while(!threadPool.isTerminated()){}//blocker til stringene er færdige med at køre!
		try {
			orvalSocket.close();
			chimaySocket.close();
			westmalleSocket.close();
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
				aPrintWriter.println(terminalBoardStrings.get(i));
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
	
	public static void main(String args[]) {
		Board b = new Board("01011010232332324545545466666102");
		NetworkMaster mnm = new NetworkMaster(b);
//		mnm.communicateWithServer("012345601234560123456012345613");
		mnm.minimaxCalc();
		b.display();
	}
}
