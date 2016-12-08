package Worker;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "cf.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "d505e16");
        ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(Integer.parseInt(args[0]));

        System.out.println("Ready for work!");

        while (true) {
            new Thread(serverSocket.accept()).start();
        }
    }
}
