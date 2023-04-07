import Models.Drone;
import ServerManager.ServerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("InfiniteLoopStatement")
public class DroneServer {
    public static void main (String[] args) {

        ServerManager app = new ServerManager();

        Drone drone1 = new Drone(1, "Drone 1", 100, 100, null);
        app.addDroneMarker(drone1);
        Drone drone2 = new Drone(2, "Drone 2", 200, 200, null);
        app.addDroneMarker(drone2);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        drone1.setXCoordinate(400);
        app.addDroneMarker(drone1);

        try{
            int serverPort = 8888;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while(true) {
                Socket clientSocket = listenSocket.accept();
                DroneConnection c = new DroneConnection(clientSocket, app);
            }

        } catch(IOException e) {
            System.out.println("Listen socket:"+e.getMessage());
        }
    }
}
