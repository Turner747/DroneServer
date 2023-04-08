package ServerManager;

import Models.Drone;

import javax.swing.*;
import java.awt.*;

public class DroneMarker extends JPanel {
    private int x;
    private int y;
    static final int WIDTH = 75;
    static final int HEIGHT = 30;
    private int id;

    public DroneMarker(Drone drone){
        this.x = drone.getXCoordinate();
        this.y = drone.getYCoordinate();
        this.id = drone.getId();

        JLabel nameLabel = new JLabel("Drone " + id);
        nameLabel.setFont(new Font("Ariel", Font.BOLD, 3));
        this.add(nameLabel);

        this.setBounds(x, y, WIDTH, HEIGHT);
        this.setBackground(Color.blue);

        this.repaint();

    }

    public int getId() {
        return id;
    }
}
