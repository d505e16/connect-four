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

    public static void main(String[] args) throws NumberFormatException {
        Scanner keyboard = new Scanner(System.in);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        getWorkers(args);
        String boardString = "";
        Board board = new Board(boardString);
        long startTime = 0, endTime = 0;
        Boolean imputIsNumber = false;
        Boolean helpRecived = false;
        do {
            if (threadPool.isShutdown()) {
                threadPool = Executors.newCachedThreadPool();
            }
            board.display();
            if (helpRecived) {
                System.out.println();
                for (int i = 0; i < moves.length; i++) {
                    System.out.println("Value at " + i + ": " + moves[i]);
                }
                double best = -1000;
                int bestCol = 10;
                int center = 3;
                for (int i = 0; i < board.getCol(); i++) {
                    if (moves[i] >= best && moves[i] != 0) {
                        if (moves[i] != best || (Math.abs(i - center) <= (Math.abs(bestCol - center))) || board.firstEmptyInCol(i) != -1) {
                            best = moves[i];
                            bestCol = i;
                        }
                    }
                }
                System.out.println("\nThe best move is in " + bestCol);
                System.out.println("\nCalculationtime: " + (endTime - startTime) + " ms");
                helpRecived = false;
            }
            System.out.println("\n" + Character.toUpperCase(board.getNextTurn()) + "'s turn\n");
            System.out.println("Type command: ");
            String input = keyboard.next();
            try {
                float i = Float.parseFloat(input);
                String tempBoardString;
                if (i >= 0 && i <= 6) {
                    tempBoardString = boardString.concat(input);
                } else {
                    tempBoardString = input;
                }
                board = new Board(tempBoardString);
                boardString = tempBoardString;
                imputIsNumber = true;
            } catch (ArrayIndexOutOfBoundsException e1) {
                board = new Board(boardString);
            }
            if (input.toLowerCase().contains("quit") || input.toLowerCase().equals("q") || input.toLowerCase().contains("exit")) {
                break;
            } else if (input.toLowerCase().contains("help") || input.toLowerCase().equals("h")) {
                System.out.println("Calculating.. ");
                Date date = new Date();
                startTime = date.getTime();
                board.minimaxCalc(false, true);
                distributeNotTerminalString(threadPool);
                threadPool.shutdown();
                while (!threadPool.isTerminated()) {

                }
                Date date1 = new Date();
                endTime = date1.getTime();
                helpRecived = true;
            } else if (input.toLowerCase().contains("clear") || input.toLowerCase().contains("c")) {
                boardString = "";
                board = new Board(boardString);
            } else if (!imputIsNumber) {
                System.out.println("Command not recognized");
            }
            clearConsole();
        } while (true);
        keyboard.close();
    }

    private static void getWorkers(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split(":");
            Connection aConnection = null;
            try {
                aConnection = new Connection(parts[0], parts[1]);
            } catch (UnknownHostException | ConnectException e) {
                System.out.println("The connection to " + parts[0] + ":" + parts[1] + " failed and has been omitted");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (aConnection != null) {
                connectionList.add(aConnection);
            }
        }
    }

    private static void distributeNotTerminalString(ExecutorService threadPool) {
        int nextConnection = 0;
        while (!notTerminalBoardList.isEmpty()) {
            Board tempBoard = notTerminalBoardList.get(0);
            if (nextConnection == connectionList.size()) {
                nextConnection = 0;
            }
            try {
                threadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard));
                notTerminalBoardList.remove(tempBoard);
            } catch (IndexOutOfBoundsException e) {
                nextConnection = 0;
                threadPool.execute(connectionThread(connectionList.get(nextConnection), tempBoard));
                notTerminalBoardList.remove(tempBoard);
            }
            nextConnection++;
        }
    }

    private static Runnable connectionThread(Connection aConnection, Board tempBoard) {
        Runnable aRunnable = () -> {
            try {
                aConnection.getPrintWriter().println(tempBoard.getBoardString());
                String recivedString = aConnection.getBufferedReader().readLine();
                int indexOfColon = recivedString.indexOf(":");
                int col = Character.getNumericValue(recivedString.charAt(indexOfColon - 1));
                double branchValue = Double.valueOf(recivedString.substring(indexOfColon + 1));
                moves[col] = branchValue;
            } catch (SocketException e) {
                if (connectionList.contains(aConnection)) {
                    connectionList.remove(aConnection);
                }
                if (connectionList.isEmpty()) {
                    tempBoard.minimaxCalc(true, false);
                } else {
                    System.out.println("køre på " + connectionList.get(0) + "istedet");
                    connectionThread(connectionList.get(0), tempBoard);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        return aRunnable;
    }

    private static void clearConsole() {
        try {
            final String opperationSystem = System.getProperty("os.name");
            if (opperationSystem.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            e.getStackTrace();
        }
    }
}
