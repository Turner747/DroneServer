package Controllers;

import Models.Drone;
import Models.Fire;
import ServerManager.DroneManagementConsole;

import java.io.IOException;
import java.util.ArrayList;

public class DroneManager {

    private static DroneManager instance;
    public static DroneManager getInstance(){
        if (instance == null){
            instance = new DroneManager();
        }
        return instance;
    };

    private final DroneManagementConsole window;
    private ArrayList<Drone> drones;
    private ArrayList<Fire> fires;
    private final DataLoader dl;

    private DroneManager(){
        this.window = DroneManagementConsole.getInstance();
        this.dl = new DataLoader();

        try{
            this.drones = dl.readDronesFromCSV();
            this.fires = dl.readFiresFromCSV();
        } catch(IOException ex) {
            window.showError(ex.getMessage(),"Load Error");
        } catch (Exception ex){
            this.showUnexpectedError(ex);
        }
        finally {
            if (drones == null) {
                drones = new ArrayList<>();
            }
            if (fires == null) {
                fires = new ArrayList<>();
            }
        }

    }

    public boolean addDrone(Drone drone){
        drones.add(drone);

        // todo: finish this

        return true;

    }

    public void showError(String message, String title){
        window.showError(message, title);
    }

    public void showMessage(String message){
        window.writeOutput(message);
    }

    public void showUnexpectedError(Throwable throwable){
        window.showStackTraceDialog(throwable, window,
                "Unexpected Error",
                "An unexpected error occurred. Please check the stace trace for further details.");
    }


    private void SaveData(){
        try{
            dl.writeDronesToCSV(drones);
            dl.writeFiresToCSV(fires);
        } catch (IOException ex) {
            window.showError(ex.getMessage(),"Save Error");
        } catch (Exception ex){
            this.showUnexpectedError(ex);
        }
    }

    public void close(){
        this.SaveData();
        System.exit(0);
    }
}
