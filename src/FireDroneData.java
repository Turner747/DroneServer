import Models.Drone;
import Models.Fire;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FireDroneData {
    private final String DELIMITER = ",";
    private final String NEW_LINE = "";
    private final String FIRE_FILE_NAME = "fires.csv";
    private final String DRONE_FILE_NAME = "drones.csv";

    public FireDroneData(){

    }

    public ArrayList<Fire> readFiresFromCSV(){
        ArrayList<Fire> fires = new ArrayList<>();
        Path pathToFile = Paths.get(FIRE_FILE_NAME);

        try(BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(DELIMITER);
                Fire fire = createFire(attributes);
                fires.add(fire);
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fires;
    }

    public ArrayList<Drone> readDronesFromCSV(){
        ArrayList<Drone> drones = new ArrayList<>();
        Path pathToFile = Paths.get(DRONE_FILE_NAME);

        try(BufferedReader br = Files.newBufferedReader(pathToFile)){
            String line = br.readLine();
            while(line != null){
                String[] attributes = line.split(DELIMITER);
                Drone drone = createDrone(attributes);
                drones.add(drone);
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return drones;
    }

    public boolean writeFiresToCSV(ArrayList<Fire> fires){
        File file = new File(FIRE_FILE_NAME);

        try (FileWriter fw = new FileWriter(file)){
            for(Fire f : fires){
                fw.write((f.toCSV()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean writeDronesToCSV(ArrayList<Drone> drones){
        File file = new File(DRONE_FILE_NAME);

        try (FileWriter fw = new FileWriter(file)){
            for(Drone d : drones){
                fw.write((d.toCSV()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private Fire createFire(String[] metadata){
        int id = Integer.parseInt(metadata[0]);
        int xCoordinate = Integer.parseInt(metadata[1]);
        int yCoordinate = Integer.parseInt(metadata[2]);
        int severity = Integer.parseInt(metadata[3]);
        return new Fire(id, xCoordinate, yCoordinate, severity);
    }

    private Drone createDrone(String[] metadata){
        int id = Integer.parseInt(metadata[0]);
        String name = metadata[1];
        int xCoordinate = Integer.parseInt(metadata[2]);
        int yCoordinate = Integer.parseInt(metadata[3]);
        return new Drone(id, name, xCoordinate, yCoordinate);
    }
}
