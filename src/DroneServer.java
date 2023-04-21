import Controllers.DataLoader;
import Controllers.DroneConnection;
import Controllers.DroneManager;
import Models.Drone;
import Models.Fire;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@SuppressWarnings("InfiniteLoopStatement")
public class DroneServer {

    public static void main (String[] args) {

        DroneManager app = DroneManager.getInstance();

        try{
            final int SERVER_PORT = 8888;
            ServerSocket listenSocket = new ServerSocket(SERVER_PORT);

            while(true) {
                Socket clientSocket = listenSocket.accept();
                DroneConnection c = new DroneConnection(clientSocket);
            }

        } catch(IOException e) {
            System.out.println("Listen socket:"+e.getMessage());
        }
    }
}
