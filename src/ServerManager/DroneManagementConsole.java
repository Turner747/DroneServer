package ServerManager;

import Controllers.DroneManager;
import Models.Drone;
import Models.Fire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DroneManagementConsole extends JFrame implements ActionListener {

    private DroneManager dm;
    private final JTextArea outputTextArea = new JTextArea();
    private final JPanel mapPanel = new JPanel();

    private final JTextField droneIdField = new JTextField();
    private final JTextField xField = new JTextField();
    private final JTextField yField = new JTextField();
    private final JTextField fireIdField = new JTextField();
    public static final int MAP_WIDTH = 800;
    public static final int MAP_HEIGHT = 500;

    public DroneManagementConsole(DroneManager dm){

        this.dm = dm;

        this.setLayout(new FlowLayout());
        String TITLE = "Drone Management Console";
        this.setTitle(TITLE);

        JLabel titleLabel = new JLabel("Drone Management Console");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 22));
        titleLabel.setSize(1000,20);
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.add(titleLabel);
        this.add(topPanel);

        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(225, 600));
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 250));
        JButton moveButton = new JButton("Move");
        moveButton.addActionListener(this);
        //moveButton.setPreferredSize(new Dimension(30, 20));
        JLabel droneLabel = new JLabel("Drone:");
        droneLabel.setPreferredSize(new Dimension(200, 20));
        droneIdField.setPreferredSize(new Dimension(40, 20));
        droneIdField.setToolTipText("id");
        JLabel xLabel = new JLabel("x:");
        JLabel yLabel = new JLabel("y:");
        JLabel idLabel = new JLabel("id:");
        JLabel idLabel2 = new JLabel("id:");
        xField.setPreferredSize(new Dimension(40, 20));
        xField.setToolTipText("x");
        yField.setPreferredSize(new Dimension(40, 20));
        yField.setToolTipText("y");
        controlPanel.add(droneLabel);
        controlPanel.add(idLabel);
        controlPanel.add(droneIdField);
        controlPanel.add(xLabel);
        controlPanel.add(xField);
        controlPanel.add(yLabel);
        controlPanel.add(yField);
        controlPanel.add(moveButton);
        JButton recallButton = new JButton("Recall Drones to Base");
        controlPanel.add(recallButton);
        JLabel fireLabel = new JLabel("Fire:");
        fireLabel.setPreferredSize(new Dimension(200, 20));
        fireIdField.setPreferredSize(new Dimension(40, 20));
        fireIdField.setToolTipText("id");
        JButton deleteFireButton = new JButton("Delete");
        controlPanel.add(fireLabel);
        controlPanel.add(idLabel2);
        controlPanel.add(fireIdField);
        controlPanel.add(deleteFireButton);
        leftPanel.add(controlPanel);
        JLabel outputLabel = new JLabel("Output");
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

        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(800, 600));
        JLabel mapLabel = new JLabel("Map");
        rightPanel.add(mapLabel);
        mapPanel.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        mapPanel.setBackground(Color.lightGray);
        rightPanel.add(mapPanel);
        this.add(rightPanel);

        addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                dm.close();
            }
        });

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int WIDTH = 1100;
        int HEIGHT = 650;
        int x_POS = 100;
        int y_POS = 100;
        this.setBounds(x_POS, y_POS, WIDTH, HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.compareTo("Move") == 0){
            dm.moveDrone(Integer.parseInt(droneIdField.getText()),
                    Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));

        } else if (command.compareTo("Delete") == 0){
            dm.removeFire(Integer.parseInt(fireIdField.getText()));
        }
        else if (command.compareTo("Recall Drones to Base") == 0){
            dm.returnAllDronesToBase();
        }
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

    public void showError(String message) {
        this.showError(message, "Error");
    }

    public void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showStackTraceDialog(Throwable throwable,
                    Component parentComponent, String title, String message) {
        final String more = "More";
        final String less = "Less";
        // create unexpanded area
        JPanel expandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel expandLabel = new JLabel(more + ">>");
        expandLabel.setForeground(Color.BLUE);
        expandPanel.add(expandLabel);

        //create stack trace area
        JTextArea stackTraceTextArea = new JTextArea();
        final JScrollPane taPane = new JScrollPane(stackTraceTextArea);
        taPane.setPreferredSize(new Dimension(360, 240));
        taPane.setVisible(false);

        // print stack trace into textarea
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(out));
        stackTraceTextArea.setText(out.toString());

        final JPanel stackTracePanel = new JPanel(new BorderLayout());
        stackTracePanel.add(expandPanel, BorderLayout.NORTH);
        stackTracePanel.add(taPane, BorderLayout.CENTER);

        expandLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        expandLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JLabel tempLabel = (JLabel) e.getSource();
                if (tempLabel.getText().equals(more + ">>")) {
                    tempLabel.setText("<<" + less);
                    taPane.setVisible(true);
                } else {
                    tempLabel.setText(more + ">>");
                    taPane.setVisible(false);
                }
                SwingUtilities.getWindowAncestor(taPane).pack();
            };
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(stackTracePanel, BorderLayout.CENTER);

        JOptionPane pane = new JOptionPane(panel, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.dispose();
    }
}
