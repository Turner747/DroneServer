package Controllers;

import Models.Drone;
import Models.DroneStatus;
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

        for(Drone drone : drones) {
            this.addDrone(drone, DroneStatus.NEW);
        }

        for(Fire fire : fires) {
            this.addFire(fire);
        }
    }

    public DroneStatus addDrone(Drone drone, DroneStatus status) {
        drones.add(drone);
        try {
            window.addUpdateDroneMarker(drone);
        }catch(IllegalArgumentException ex){
            if (drone.getXCoordinate() > DroneManagementConsole.MAP_WIDTH || drone.getXCoordinate() < 0){
                return DroneStatus.OUT_OF_BOUNDS_X;
            }else if (drone.getYCoordinate() > DroneManagementConsole.MAP_HEIGHT || drone.getYCoordinate() < 0) {
                return DroneStatus.OUT_OF_BOUNDS_Y;
            }
        }
        if (status == DroneStatus.NEW) {
            window.writeOutput("Drone " + drone.getId() + " added");
        } else if(status == DroneStatus.UPDATE){
            window.writeOutput("Drone " + drone.getId() + " updated");
        }
        return DroneStatus.SUCCESS;
    }

    public DroneStatus removeDrone(Drone drone){
        drones.remove(drone);
        try {
            window.removeDroneMarker(drone.getId());
        }catch(Exception ex){
            return DroneStatus.ERROR;
        }
        window.writeOutput("Drone " + drone.getId() + " removed");
        return DroneStatus.SUCCESS;
    }

    public void addFire(Fire fire){
        fires.add(fire);
        try {
            window.addFireMarker(fire);
        }catch(Exception ex){
            this.showUnexpectedError(ex);
        }
        window.writeOutput("Fire " + fire.getId() + " with severity " + fire.getSeverity() + " spotted");
    }

    public void removeFire(Fire fire){
        fires.remove(fire);
        try {
            window.removeFireMarker(fire.getId());
        }catch(Exception ex){
            this.showUnexpectedError(ex);
        }
        window.writeOutput("Fire " + fire.getId() + " extinguished");
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
