import Models.Drone;
import Models.Fire;
import ServerManager.DroneManagementConsole;

import java.io.IOException;
import java.util.ArrayList;

public class DroneManager {
    private DroneManagementConsole window;
    private ArrayList<Drone> drones;
    private ArrayList<Fire> fires;
    private DataLoader dl;

    public DroneManager(){
        this.window = new DroneManagementConsole();
        this.dl = new DataLoader();

        try{
            this.drones = dl.readDronesFromCSV();
            this.fires = dl.readFiresFromCSV();
        }catch(IOException ex){
            window.displayError(ex.getMessage(), "Loading Error");
        } finally {
            if (drones == null) {
                drones = new ArrayList<>();
            }
            if (fires == null) {
                fires = new ArrayList<>();
            }
        }

    }
}
