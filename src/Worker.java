import Minimax.Board;
import Minimax.DoubleRecurcive;

import java.io.*;
import java.net.*;

public class Worker {
    public static void main(String[] args) {
        System.out.println("server ... ");
        try {
            System.out.println("hello");
            ServerSocket serverSocket = new ServerSocket(4444);
            Socket socket = serverSocket.accept();
            System.out.println("soccet accepted");

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println("bufferedReader setup");

            Board board = null;
            int count = 0;
            String recivedBoardString = null;
            while ((recivedBoardString = bufferedReader.readLine()) != null) {
                count += 1;
                System.out.println(count + ". time in readline");
//				String recivedBoardString = bufferedReader.readLine();
                board = new Board(recivedBoardString);


                DoubleRecurcive mm = new DoubleRecurcive(board);
                System.out.println(count + " DoubleRecurcive created");
                double miniValue = mm.miniCalc(board, 2);
                System.out.println(count + " miniCalc run");
                //TODO - run minimax on board - should return double (mini or max of this branch)
//			System.out.println("out of while");
                double branchValue = mm.getReturnValue() / 2;


                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(String.valueOf(miniValue));
                //printWriter.printf(recivedBoardString, branchValue);
                System.out.println(count + " after print");
            }//while

            System.out.println("end of server");//recivedBoardString);
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
