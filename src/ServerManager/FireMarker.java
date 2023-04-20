package ServerManager;

import Models.Fire;

import javax.swing.*;
import java.awt.*;

public class FireMarker extends JPanel{
    private int x;
    private int y;
    private int severity;

    private int id;

    public FireMarker(Fire fire){
        this.x = fire.getXCoordinate();
        this.y = fire.getYCoordinate();
        this.severity = fire.getSeverity();
        this.id = fire.getId();

        int WIDTH = 30;
        int HEIGHT = 30;
        this.setBounds(x, y, WIDTH, HEIGHT);
        this.setBackground(Color.red);

        this.add(new JLabel("Models.Fire"));
        this.add(new JLabel("Severity: " + this.severity));

    }

    public int getId() {
        return id;
    }
}
