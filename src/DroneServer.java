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

    ArrayList<Drone> drones = new ArrayList<>();
    ArrayList<Fire> fires = new ArrayList<>();
    public static void main (String[] args) {

        DroneManager app = new DroneManager();

        // ----- test region -----


        // ----- end test region -----

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

    private boolean loadData(){
        try {
            DataLoader data = new DataLoader();
            fires = data.readFiresFromCSV();
            drones = data.readDronesFromCSV();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean saveData(){
        try {
            DataLoader data = new DataLoader();
            data.writeFiresToCSV(fires);
            data.writeDronesToCSV(drones);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
