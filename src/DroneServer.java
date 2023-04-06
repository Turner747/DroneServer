import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("InfiniteLoopStatement")
public class DroneServer {
    public static void main (String[] args) {

        ServerManager a = new ServerManager();

        try{
            int serverPort = 8888;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while(true) {
                Socket clientSocket = listenSocket.accept();
                DroneConnection c = new DroneConnection(clientSocket);
            }

        } catch(IOException e) {
            System.out.println("Listen socket:"+e.getMessage());
        }
    }
}
