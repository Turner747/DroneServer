package Controllers;

import Models.Drone;
import Models.Fire;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataLoader { // todo: consider making this a static class, no need to instantiate it
    private static final String DELIMITER = ",";
    private static final String FIRE_FILE_NAME = "fires.csv";
    private static final String DRONE_FILE_NAME = "drones.csv";

    public static ArrayList<Fire> readFiresFromCSV() throws IOException {
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
            throw new IOException("Error loading data from " + FIRE_FILE_NAME, ex);
        } catch (NumberFormatException ex) {
            throw new IOException("Data corrupted. Please check csv format of " + FIRE_FILE_NAME, ex);
        } catch (Exception ex) {
            throw new IOException("Unexpected error occurred while loading data from " + FIRE_FILE_NAME, ex);
        }
        return fires;
    }

    public static  ArrayList<Drone> readDronesFromCSV() throws IOException{
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
            throw new IOException("Error loading data from " + DRONE_FILE_NAME, ex);
        } catch (NumberFormatException ex) {
            throw new IOException("Data corrupted. Please check csv format of " + DRONE_FILE_NAME, ex);
        } catch (Exception ex) {
            throw new IOException("Unexpected error occurred while loading data from " + DRONE_FILE_NAME, ex);
        }
        return drones;
    }

    public static void writeFiresToCSV(ArrayList<Fire> fires) throws IOException{
        File file = new File(FIRE_FILE_NAME);

        try (FileWriter fw = new FileWriter(file)){
            for(Fire f : fires){
                fw.write((f.toCSV()));
            }
        } catch (IOException ex) {
            throw new IOException("Error saving data to " + FIRE_FILE_NAME, ex);
        } catch (Exception ex) {
            throw new IOException("Unexpected error occurred while saving data to " + FIRE_FILE_NAME, ex);
        }
    }

    public static void writeDronesToCSV(ArrayList<Drone> drones) throws IOException{
        File file = new File(DRONE_FILE_NAME);

        try (FileWriter fw = new FileWriter(file)){
            for(Drone d : drones){
                fw.write((d.toCSV()));
            }
        } catch (IOException ex) {
            throw new IOException("Error saving data to " + DRONE_FILE_NAME, ex);
        } catch (Exception ex) {
            throw new IOException("Unexpected error occurred while saving data to " + DRONE_FILE_NAME, ex);
        }
    }

    private static Fire createFire(String[] metadata){
        int id = Integer.parseInt(metadata[0]);
        int xCoordinate = Integer.parseInt(metadata[1]);
        int yCoordinate = Integer.parseInt(metadata[2]);
        int severity = Integer.parseInt(metadata[3]);
        return new Fire(id, xCoordinate, yCoordinate, severity);
    }

    private static Drone createDrone(String[] metadata){
        int id = Integer.parseInt(metadata[0]);
        String name = metadata[1];
        int xCoordinate = Integer.parseInt(metadata[2]);
        int yCoordinate = Integer.parseInt(metadata[3]);
        return new Drone(id, name, xCoordinate, yCoordinate);
    }
}
