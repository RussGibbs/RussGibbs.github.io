import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Interface extends JFrame implements ActionListener {
    JPanel mainPanel;

    Color background;

    JTextField dragCoefficientF;
    JTextField fluidDensityF;
    JTextField surfaceAreaF;
    JTextField massF;
    JTextField velocity;
    JTextField launchAngle;
    JTextField gravity;
    JTextField height;

    JPanel toolBar;
    JPanel toolBarR;
    JPanel toolBarL;
    BufferedImage graphics;
    Graphics2D g;
    SwingWorker<Void, Void> worker;
    boolean cancelled;

    Projectile dart;
    double length;
    double maxHeight;


    JButton launch;
    JButton setScale;

    Timer timer;

    public Interface() {
        setSize(1500, 1000);
        background = new Color(0xaacccc);
        loadHome();
        launch.setEnabled(true);
        cancelled = true;
        graphics = null;
        mainPanel.setVisible(false);
        Projectile dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()));
        dart.launch(0.0001);
        length = dart.getX_position();
        maxHeight = dart.getMaxHeight();
        loadHome();
    }

    public void loadHome() {
        setTitle("Trajectory Simulator");
        mainPanel = new JPanel();
        mainPanel.setBackground(background);
        mainPanel.setVisible(true);
        mainPanel.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(background);
        controlPanel.setLayout(new GridLayout(10, 1));
        mainPanel.add(controlPanel, BorderLayout.WEST);

        JPanel[] controlPanels = new JPanel[10];

        for (int i = 0; i < 10; i++) {
            controlPanels[i] = new JPanel();
            controlPanels[i].setLayout(new FlowLayout());
            controlPanel.add(controlPanels[i]);
        }

        controlPanels[0].add(new JLabel("Control Panel"));

        controlPanels[1].add(new JLabel("Drag Coefficient"));
        if (dragCoefficientF == null) {
            dragCoefficientF = new JTextField("0.38", 10);
            dragCoefficientF.addActionListener(this);
        }
        controlPanels[1].add(dragCoefficientF);

        controlPanels[2].add(new JLabel("Fluid Density (kgm^-3)"));
        if (fluidDensityF == null) {
            fluidDensityF = new JTextField("1.225", 10);
            fluidDensityF.addActionListener(this);
        }
        controlPanels[2].add(fluidDensityF);

        controlPanels[3].add(new JLabel("Surface Area (m^3)"));
        if (surfaceAreaF == null) {
            surfaceAreaF = new JTextField("0.000126", 10);
            surfaceAreaF.addActionListener(this);
        }
        controlPanels[3].add(surfaceAreaF);

        controlPanels[4].add(new JLabel("Mass (kg)"));
        if (massF == null) {
            massF = new JTextField("0.001", 10);
            massF.addActionListener(this);
        }
        controlPanels[4].add(massF);


        controlPanels[6].add(new JLabel("Velocity (ms^-1)"));
        if (velocity == null) {
            velocity = new JTextField("30", 10);
            velocity.addActionListener(this);
        }
        controlPanels[6].add(velocity);

        controlPanels[7].add(new JLabel("Launch Angle (degrees)"));
        if (launchAngle == null) {
            launchAngle = new JTextField("45", 10);
            launchAngle.addActionListener(this);
        }
        controlPanels[7].add(launchAngle);

        controlPanels[8].add(new JLabel("Gravity (ms^-2)"));
        if (gravity == null) {
            gravity = new JTextField("-9.81", 10);
            gravity.addActionListener(this);
        }
        controlPanels[8].add(gravity);

        controlPanels[9].add(new JLabel("Launch Height (m)"));
        if (height == null) {
            height = new JTextField("2", 10);
            height.addActionListener(this);
        }
        controlPanels[9].add(height);


        if (graphics == null) {
            graphics = new BufferedImage(getWidth() - 400, getHeight() - 100, BufferedImage.TYPE_INT_RGB);
            g = (Graphics2D) graphics.getGraphics();
            g.setColor(background);
            g.fillRect(0, 0, getWidth() - 400, getHeight() - 100);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(100, getHeight() - 200, getWidth() - 400 - 200, 2);
            g.fillRect(100, 100, 2, getHeight() - 100 - 200);
        }
        mainPanel.add(new JLabel(new ImageIcon(graphics)), BorderLayout.CENTER);


        toolBar = new JPanel();
        toolBar.setLayout(new BorderLayout());
        toolBar.setVisible(true);
        mainPanel.add(toolBar, BorderLayout.SOUTH);

        toolBarL = new JPanel();
        toolBarL.setLayout(new FlowLayout());
        toolBarL.setVisible(true);
        toolBar.add(toolBarL, BorderLayout.WEST);

        toolBarR = new JPanel();
        toolBarR.setLayout(new FlowLayout());
        toolBarR.setVisible(true);
        toolBar.add(toolBarR, BorderLayout.EAST);


        toolBarL.add(new JLabel("        "));

        if (launch == null) {
            launch = new JButton("Launch");
            launch.addActionListener(this);
        }
        toolBarL.add(launch);

        if (setScale == null) {
            setScale = new JButton("Set Scale");
            setScale.addActionListener(this);
        }
        toolBarL.add(setScale);


        add(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void launchProjectile() {
        dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()));
        dart.launch(0.0001);
        toolBarR.add(new JLabel("Distance: " + String.format("%.2f", dart.getX_position()) + "m     "));
        toolBarR.add(new JLabel("Max Height: " + String.format("%.2f", dart.getMaxHeight()) + "m     "));
        toolBarR.add(new JLabel("Time in Flight: " + String.format("%.2f", dart.getTime()) + "s"));

        dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()));

            worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!(dart.getY_position() < 0 && dart.getAngle() < 0)) {
                    if (!cancelled) {
                        long start = System.nanoTime();
                        process(null);
                        dart.move(0.0001);
                        while (System.nanoTime() - start < 100000) {}
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                launch.setEnabled(true);
                super.done();
            }

            @Override
            protected void process(java.util.List<Void> chunks) {
                g.fillRect((int) (100 + dart.getX_position() / length * (getWidth() - 400 - 200)),
                        (int) (getHeight() - 200 - dart.getY_position() / length * (getWidth() - 400 - 200)),
                        3, 3);
                repaint();
            }
        };
        cancelled = false;
        worker.execute();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setScale) {
            launch.setEnabled(true);
            cancelled = true;
            graphics = null;
            mainPanel.setVisible(false);
            Projectile dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                    Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                    Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                    Double.parseDouble(massF.getText()));
            dart.launch(0.0001);
            length = dart.getX_position();
            maxHeight = dart.getMaxHeight();
            loadHome();
        }

        if (e.getSource() == launch) {
            launch.setEnabled(false);
            mainPanel.setVisible(false);
            loadHome();
            launchProjectile();
        }
    }
}
