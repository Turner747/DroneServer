package Controllers;

import Models.ConnectionStatus;
import Models.Drone;
import Models.ServerResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DroneConnection extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    private Socket clientSocket;
    DroneManager app;

    public DroneConnection (Socket aClientSocket, DroneManager app) {

        this.app = app;
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
            Drone drone = (Drone) in.readObject();

            if(app.addDrone(drone)) {
                response.setStatus(ConnectionStatus.SUCCESS);
                response.setMessage("Drone added successfully");
            } else {
                response.setStatus(ConnectionStatus.FAILURE);
                response.setMessage("Drone could not be added");
            }

        } catch(EOFException ex){
            response.setStatus(ConnectionStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "EOF Error");
        } catch(IOException ex) {
            response.setStatus(ConnectionStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "IO Error");
        } catch(ClassNotFoundException ex){
            response.setStatus(ConnectionStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showError(ex.getMessage(), "Class Not Found Error");
        } catch (Exception ex) {
            response.setStatus(ConnectionStatus.ERROR);
            response.setMessage(ex.getMessage());
            app.showUnexpectedError(ex);
        } finally {
            try {
                out.writeObject(response);
                clientSocket.close();
            } catch(IOException ex) {
                app.showError(ex.getMessage(), "IO Error");
            } catch (Exception ex) {
                app.showUnexpectedError(ex);
            }
        }

    }
}
