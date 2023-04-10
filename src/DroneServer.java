import Models.Drone;
import ServerManager.ServerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("InfiniteLoopStatement")
public class DroneServer {
    public static void main (String[] args) {

        ServerManager app = new ServerManager();

        Drone drone1 = new Drone(1, "Drone 1", 100, 100, null);
        app.addUpdateDroneMarker(drone1);
        Drone drone2 = new Drone(2, "Drone 2", 200, 200, null);
        app.addUpdateDroneMarker(drone2);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drone1.setXCoordinate(400);
        app.addUpdateDroneMarker(drone1);
        app.writeOutput("Drone 1 has been moved");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        app.removeDroneMarker(drone1.getId());
        app.writeOutput("Drone 1 has been removed");

        ArrayList<Drone> drones = new ArrayList<>();
        drones.add(drone1);
        drones.add(drone2);

        FireDroneData data = new FireDroneData();
        data.writeDronesToCSV(drones);

        ArrayList<Drone> drones2 = data.readDronesFromCSV();

        for(Drone drone : drones2){
            app.writeOutput(drone.toString());
        }


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
