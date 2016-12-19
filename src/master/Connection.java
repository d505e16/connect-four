package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class Connection {
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    Connection(String anAdress, String aPort) throws IOException {
        int port = Integer.valueOf(aPort);
        Socket socket = new Socket(anAdress, port);
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    PrintWriter getPrintWriter() {
        return printWriter;
    }

    BufferedReader getBufferedReader() {
        return bufferedReader;
    }
}
