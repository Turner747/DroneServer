package ServerManager;

import Models.Drone;
import Models.Fire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ServerManager extends JFrame implements ActionListener {

    private final String TITLE = "Drone Management Console";

    private final int WIDTH = 1100;
    private final int HEIGHT = 650;
    private final int X_POS = 100;
    private final int Y_POS = 100;

    private JLabel titleLabel = new JLabel("Models.Drone Management Console");

    private JPanel rightPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JLabel controlLabel = new JLabel("Control:");
    private JPanel controlPanel = new JPanel();

    private JLabel mapLabel = new JLabel("Map");
    private JPanel mapPanel = new JPanel();
    private final int MAP_WIDTH = 800;
    private final int MAP_HEIGHT = 500;
    private JLabel outputLabel = new JLabel("Output");
    private JTextArea outputTextArea = new JTextArea();

    private ArrayList<Drone> drones;
    private ArrayList<Fire> fires;

    public ServerManager(){
        this.setLayout(new FlowLayout());
        this.setTitle(TITLE);

        titleLabel.setFont(new Font("Ariel", Font.BOLD, 22));
        titleLabel.setSize(1000,20);
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.add(titleLabel);
        this.add(topPanel);

        leftPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setPreferredSize(new Dimension(200, 250));
        leftPanel.add(controlPanel);
        leftPanel.add(outputLabel);
        outputTextArea.setPreferredSize(new Dimension(200, 245));
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        leftPanel.add(scrollPane);
        this.add(leftPanel);

        var spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10,600));
        this.add(spacer);

        rightPanel.setPreferredSize(new Dimension(800, 600));
        rightPanel.add(mapLabel);
        mapPanel.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        mapPanel.setBackground(Color.lightGray);
        rightPanel.add(mapPanel);
        this.add(rightPanel);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0); // todo: consider sending stop message to drones
                    }
                }
        );

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(X_POS, Y_POS, WIDTH, HEIGHT);
        this.setVisible(true);
        this.setResizable(false);

        drones = new ArrayList<>();
        fires = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void addDroneMarker(Drone drone){

        if (drone.getXCoordinate() > MAP_WIDTH || drone.getYCoordinate() > MAP_HEIGHT)
            throw new IllegalArgumentException("Drone position is out of bounds of the map. " +
                    "Drone position: (" + drone.getXCoordinate() + ", " + drone.getYCoordinate() + "). Map size: (" + MAP_WIDTH + ", " + MAP_HEIGHT + ").");

        Drone newDrone = null;
        for (Drone d : drones) {
            if (d.getId() == drone.getId()){
                d.setXCoordinate(drone.getXCoordinate());
                d.setYCoordinate(drone.getYCoordinate());
                d.setFire(drone.getFire());
                newDrone = d;

                mapPanel.remove(d.getId() - 1);

                break;
            }
        }

        if(newDrone == null){
            drones.add(drone);
            newDrone = drone;
        }

        mapPanel.add(new DroneMarker(newDrone));
    }

    public void addFireMarker(Fire fire){

        if(fire.getXCoordinate() > MAP_WIDTH || fire.getYCoordinate() > MAP_HEIGHT)
            throw new IllegalArgumentException("Fire position is out of bounds of the map. " +
                    "Fire position: (" + fire.getXCoordinate() + ", " + fire.getYCoordinate() + "). Map size: (" + MAP_WIDTH + ", " + MAP_HEIGHT + ").");

        Fire newFire = null;
        for (Fire f : fires) {
            if(f.getId() == fire.getId()) {
                f.setXCoordinate(fire.getXCoordinate());
                f.setYCoordinate(fire.getYCoordinate());
                f.setSeverity(fire.getSeverity());
                newFire = f;

                mapPanel.remove(f.getId() - 1);

                break;
            }
        }

        if(newFire == null){
            fires.add(fire);
            newFire = fire;
        }

        mapPanel.add(new FireMarker(newFire));
    }
}
