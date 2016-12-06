import Minimax.Board;
import Minimax.DoubleRecurcive;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestWorker {
    public static void main(String[] args) {
        System.out.println("server ... ");
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        
       	threadPool.execute(new Runnable() {
			public void run() {			
				runWorker(4444);
			}
		});
       	
       	threadPool.execute(new Runnable() {
			public void run() {			
				runWorker(4445);
			}
		});
       	
       	threadPool.execute(new Runnable() {
			public void run() {			
				runWorker(4446);
			}
		});
    }
    
    public static void runWorker(int port){
        try {
            System.out.println("hello");
            ServerSocket serverSocket = new ServerSocket(port);

            InputStreamReader inputStreamReader; 
            BufferedReader bufferedReader;
            System.out.println("bufferedReader init");
            
            PrintWriter printWriter;

            while(true){
            Board board = null;
            int count = 0; // kun til test
            String recivedBoardString = null;
            String returnString = null;

            Socket socket = serverSocket.accept();
            System.out.println("soccet accepted");
            
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println("bufferedReader setup");

            
            while ((recivedBoardString = bufferedReader.readLine()) != null) {
                count += 1;// kun til test
                System.out.println(count + ". time in readline");
                board = new Board(recivedBoardString);
                
                //det sendte boards "forgænger" TODO refaktoricer minimaxCalc(): klam kode....
                Board motherBoard = new Board(recivedBoardString.substring(0, recivedBoardString.length()-1));
                
                System.out.println("borad.getBoardString: " + motherBoard.getBoardString());
                System.out.println("borad.getBoardString: " + board.getBoardString());
                
                DoubleRecurcive mm = new DoubleRecurcive(motherBoard);
                System.out.println(count + " DoubleRecurcive created");
                double miniValue = mm.miniCalc(board, 2);
                System.out.println(count + " miniCalc run: miniValue: " + miniValue);
                
                //sender boardString tilbage med :miniValue eks. 1220125461:-25
                returnString = (recivedBoardString + ":" + miniValue);
                
                
                printWriter = new PrintWriter(socket.getOutputStream(), true);// flyttes ud af while
                printWriter.println(returnString);
                //printWriter.printf(recivedBoardString, branchValue);
                System.out.println(count + " after print");
            }//while
            }//while
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
