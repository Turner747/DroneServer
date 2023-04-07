package ServerManager;

import Models.Fire;

import javax.swing.*;
import java.awt.*;

public class FireMarker extends JPanel{
    private int x;
    private int y;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    private int severity;

    public FireMarker(Fire fire){
        this.x = fire.getXCoordinate();
        this.y = fire.getYCoordinate();
        this.severity = fire.getSeverity();

        this.setBounds(x, y, WIDTH, HEIGHT);
        this.setBackground(Color.red);

        this.add(new JLabel("Models.Fire"));
        this.add(new JLabel("Severity: " + this.severity));

    }
}
