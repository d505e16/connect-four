package Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Thread extends java.lang.Thread {
    private Socket socket;

    Thread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String boardString = bufferedReader.readLine();

            // run minimax

            // printwrite result
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
