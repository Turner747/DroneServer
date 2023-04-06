import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DroneConnection extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;

    public DroneConnection (Socket aClientSocket) {

        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream( clientSocket.getInputStream());
            out =new ObjectOutputStream( clientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public void run(){

        try {

            DroneState drone = (DroneState) in.readObject();

            // do stuff todo: process drone state

            // reply to client todo: make response class
            ServerResponse response = new ServerResponse();
            out.writeObject(response);

        } catch(EOFException e){
            System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {
            System.out.println("readline:"+e.getMessage());
        } catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }finally{
            try {
                clientSocket.close();
            } catch(IOException e) {
                /*close failed*/
            }
        }
    }
}
