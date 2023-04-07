import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerManager extends JFrame implements ActionListener {

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

    private JLabel outputLabel = new JLabel("Output");
    private JTextArea outputTextArea = new JTextArea();


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
        leftPanel.add(outputTextArea);
        this.add(leftPanel);

        var spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10,600));
        this.add(spacer);

        rightPanel.setPreferredSize(new Dimension(800, 600));
        rightPanel.add(mapLabel);
        mapPanel.setPreferredSize(new Dimension(800, 500));
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
}
