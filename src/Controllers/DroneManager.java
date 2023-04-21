package Controllers;

import Models.Drone;
import Models.DroneMessage;
import Models.DroneStatus;
import Models.Fire;
import ServerManager.DroneManagementConsole;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DroneManager {

    private static final DroneManager instance = new DroneManager();
    public static DroneManager getInstance(){
        return instance;
    };

    private final DroneManagementConsole window;
    private ArrayList<Drone> drones;
    private ArrayList<Fire> fires;
    private final ArrayList<Socket> droneSockets;

    private DroneManager(){
        this.window = new DroneManagementConsole(this);

        try{
            this.drones = DataLoader.readDronesFromCSV();
            this.fires = DataLoader.readFiresFromCSV();
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
        droneSockets = new ArrayList<>();

        for(Drone drone : drones) {
            this.addDrone(drone, DroneStatus.NEW, null);
        }

        for(Fire fire : fires) {
            this.addFire(fire);
        }
    }

    public void moveDrone(int id, int x, int y){
        Drone drone = null;
        for(Drone d : drones){
            if (d.getId() == id){
                drone = d;
                break;
            }
        }
        if (drone == null){
            window.writeOutput("Drone " + id + " not found");
            return;
        }
        drone.setXCoordinate(x);
        drone.setYCoordinate(y);

        Socket s = null;
        for(Socket socket : droneSockets){
            if (socket.getInetAddress().toString().equals(drone.getSocketId())){
                s = socket;
                break;
            }
        }

        try {
            assert s != null;
            ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream() );
            ObjectInputStream in = new ObjectInputStream( s.getInputStream() );

            DroneMessage message = new DroneMessage(DroneStatus.UPDATE, drone, null, "Move drone to " + x + ", " + y);

            out.writeObject(message);

            DroneMessage clientResponse = (DroneMessage) in.readObject();

            if (clientResponse.getStatus() == DroneStatus.SUCCESS){
                window.writeOutput(clientResponse.getMessage());
                window.addUpdateDroneMarker(clientResponse.getDrone());
            }else if (clientResponse.getStatus() == DroneStatus.ERROR){
                window.showError(clientResponse.getMessage(), "Drone Error");
            }

        } catch (IOException e) {
            window.showError(e.getMessage(), "IO Error");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAllDrones(){
        for(Socket socket : droneSockets){
            if (socket == null)
                continue;
            try {
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream());
                DroneMessage message = new DroneMessage(DroneStatus.DELETE, null, null, "Close drone");
                out.writeObject(message);
                socket.close();
            } catch (IOException e) {
                window.showError(e.getMessage(), "IO Error");
            }finally {
                for(Drone drone : drones){
                    if ( drone.getSocketId().equals(socket.getInetAddress().toString()) ){
                        drones.remove(drone);
                        break;
                    }
                }
            }
        }
    }

    public void returnAllDronesToBase(){
        for(Drone drone : drones){
            this.moveDrone(drone.getId(), 0, 0);
        }
    }

    public DroneStatus addDrone(Drone drone, DroneStatus status, Socket socket){
        droneSockets.add(socket);
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

    public void removeFire(int id){
        boolean fireFound = false;
        for(Fire fire : fires){
            if (fire.getId() == id){
                fires.remove(fire);
                fireFound = true;
                break;
            }
        }
        if (!fireFound){
            window.writeOutput("Fire " + id + " not found");
            return;
        }
        try {
            window.removeFireMarker(id);
        }catch(Exception ex){
            this.showUnexpectedError(ex);
        }
        window.writeOutput("Fire " + id + " extinguished");
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


    private void saveData(){
        try{
            DataLoader.writeDronesToCSV(drones);
            DataLoader.writeFiresToCSV(fires);
        } catch (IOException ex) {
            window.showError(ex.getMessage(),"Save Error");
        } catch (Exception ex){
            this.showUnexpectedError(ex);
        }
    }

    public void close(){
        this.closeAllDrones();
        this.saveData();
        System.exit(0);
    }
}
