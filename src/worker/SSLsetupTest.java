package worker;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;

public class SSLsetupTest {
    public static void main(String[] args) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", "cf.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "d505e16");
        ServerSocket serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(Integer.parseInt(args[0]));

        System.out.println("Ready for work!");

    }
}
