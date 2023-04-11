package ServerManager;

import Models.Drone;
import Models.Fire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DroneManagementConsole extends JFrame implements ActionListener {

    private final String TITLE = "Drone Management Console";

    private final int WIDTH = 1100;
    private final int HEIGHT = 650;
    private final int X_POS = 100;
    private final int Y_POS = 100;

    private JLabel titleLabel = new JLabel("Drone Management Console");

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


    public DroneManagementConsole(){
        this.setLayout(new FlowLayout());
        this.setTitle(TITLE);

        titleLabel.setFont(new Font("Ariel", Font.BOLD, 22));
        titleLabel.setSize(1000,20);
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.add(titleLabel);
        this.add(topPanel);

        leftPanel.setPreferredSize(new Dimension(225, 600));
        controlPanel.setPreferredSize(new Dimension(200, 250));
        leftPanel.add(controlPanel);
        leftPanel.add(outputLabel);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 245));
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void addUpdateDroneMarker(Drone drone){

        if (drone.getXCoordinate() > MAP_WIDTH || drone.getYCoordinate() > MAP_HEIGHT)
            throw new IllegalArgumentException("Drone position is out of bounds of the map. " +
                    "Drone position: (" + drone.getXCoordinate() + ", " + drone.getYCoordinate() + "). Map size: (" + MAP_WIDTH + ", " + MAP_HEIGHT + ").");

        this.removeDroneMarker(drone.getId());
        mapPanel.add(new DroneMarker(drone));

        mapPanel.repaint();
    }

    public void addFireMarker(Fire fire){

        if(fire.getXCoordinate() > MAP_WIDTH || fire.getYCoordinate() > MAP_HEIGHT)
            throw new IllegalArgumentException("Fire position is out of bounds of the map. " +
                    "Fire position: (" + fire.getXCoordinate() + ", " + fire.getYCoordinate() + "). Map size: (" + MAP_WIDTH + ", " + MAP_HEIGHT + ").");

        this.removeFireMarker(fire.getId());
        mapPanel.add(new FireMarker(fire));

        mapPanel.repaint();
    }

    public void removeDroneMarker(int id){

        var comps = mapPanel.getComponents();

        for(Component c : comps){
            if(c instanceof DroneMarker){
                if(((DroneMarker) c).getId() == id){
                    mapPanel.remove(c);
                    break;
                }
            }
        }

        mapPanel.repaint();
    }

    public void removeFireMarker(int id){

        var comps = mapPanel.getComponents();

        for(Component c : comps){
            if(c instanceof FireMarker){
                if(((FireMarker) c).getId() == id){
                    mapPanel.remove(c);
                    break;
                }
            }
        }

        mapPanel.repaint();
    }

    public void writeOutput(String output) {
        outputTextArea.append(output + "\n");
    }

    public void displayError(String message) {
        this.displayError(message, "Error");
    }

    public void displayError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

}
