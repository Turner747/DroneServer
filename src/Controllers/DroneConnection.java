package Controllers;

import Models.DroneStatus;
import Models.DroneMessage;
import Models.ServerResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DroneConnection extends Thread {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;
    private final DroneManager app;

    public DroneConnection (Socket aClientSocket) {

        this.app = DroneManager.getInstance();
        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream( clientSocket.getInputStream());
            out = new ObjectOutputStream( clientSocket.getOutputStream());
            this.start();
        } catch(IOException ex) {
            app.showError(ex.getMessage(), "IO Error");
        } catch (Exception ex) {
            app.showUnexpectedError(ex);
        }
    }

    public void run(){

        ServerResponse response = new ServerResponse();
        try {
            DroneMessage inMessage = (DroneMessage) in.readObject();
            inMessage.getDrone().setSocketId(clientSocket.getInetAddress().toString());
            if (inMessage.getStatus() == DroneStatus.DELETE) {
                response.setStatus(app.removeDrone(inMessage.getDrone()));
            }else if (inMessage.getStatus() == DroneStatus.UPDATE ||
                    inMessage.getStatus() == DroneStatus.NEW) {
                response.setStatus(app.addDrone(inMessage.getDrone(), inMessage.getStatus(), clientSocket));
            }

            if (inMessage.getFire() != null) {
                app.addFire(inMessage.getFire());
            }

            switch (response.getStatus()) {
                case SUCCESS -> response.setMessage("Drone update successful");
                case OUT_OF_BOUNDS_X -> response.setMessage("Drone could not be added: X coordinate out of bounds");
                case OUT_OF_BOUNDS_Y -> response.setMessage("Drone could not be added: Y coordinate out of bounds");
                case ERROR -> response.setMessage("Drone could not be added");
                default -> response.setMessage("Something went wrong");
            }

        } catch(EOFException ex){
            response.setStatus(DroneStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "EOF Error");
        } catch(IOException ex) {
            response.setStatus(DroneStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "IO Error");
        } catch(ClassNotFoundException ex){
            response.setStatus(DroneStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "Class Not Found Error");
        } catch (Exception ex) {
            response.setStatus(DroneStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showUnexpectedError(ex);
        } finally {
            try {
                out.writeObject(response);
            } catch(IOException ex) {
                app.showError(ex.getMessage(), "IO Error");
            } catch (Exception ex) {
                app.showUnexpectedError(ex);
            }
        }

    }
}
