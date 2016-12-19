package worker;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerMain {
    public static void main(String[] args) {
        System.out.println("Worker ... ");
        ServerSocket serverSocket = setUpServerSocket();
        communicateWithMaster(serverSocket);
        System.out.println("Worker ready ... ");
    }

    private static ServerSocket setUpServerSocket() {
        int port = 4444;
        Scanner keyboard = new Scanner(System.in);
        ServerSocket serverSocket = null;
        do {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Running on port: " + port);
                keyboard.close();
            } catch (BindException e) {
                System.out.println("Port " + port + " is already in use! \nEnter another port number for socket:");
                port = keyboard.nextInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (serverSocket == null);
        return serverSocket;
    }

    private static void communicateWithMaster(ServerSocket serverSocket) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                String recivedBoardString;
                while ((recivedBoardString = bufferedReader.readLine()) != null) {
                    threadPool.execute(new WorkerThread(socket, recivedBoardString));
                }
            }
        } catch (SocketException e) {
            System.out.println("\nCalculations DONE! \n\nWorker ...");
            communicateWithMaster(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
