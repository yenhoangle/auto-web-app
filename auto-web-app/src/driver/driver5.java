/*
 * Yen Le
 * 20123455
 *
 * driver5.java
 * Driver class to test server implementation. Output of client-server interaction is located in output.txt
 * */

package driver;
import server.*;

public class driver5 {
    public static void main(String[] args) {
        int port = 4444; //port to listen to
        DefaultServerSocket server = new DefaultServerSocket(port);
        server.start();
    }
}
