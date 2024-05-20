import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Interface extends JFrame implements ActionListener {
    double dt;

    JPanel mainPanel;

    Color background;
    Color axesColor;

    JTextField dragCoefficientF;
    JTextField fluidDensityF;
    JTextField surfaceAreaF;
    JTextField massF;
    JTextField velocity;
    JTextField launchAngle;
    JTextField gravity;
    JTextField height;

    JPanel yAxis;
    JPanel xAxis;

    JPanel toolBar;
    JPanel[] toolBarR;
    JPanel toolBarRSection;
    JPanel toolBarL;
    BufferedImage graphics;
    Graphics2D g;

    JPanel toolBarM;
    BufferedImage gradient;
    JPanel gradientLabelPanel;
    SwingWorker<Void, Void> worker;
    boolean cancelled;

    Projectile dart;
    double length;
    double maxHeight;

    JButton launch;
    JButton setScale;

    double maxVelocity;
    double minVelocity;

    public Interface() {
        dt = 0.0001;
        setSize(4480, 2520);
        background = new Color(0x000000);
        axesColor = new Color(0xaaaaaa);
        loadHome();
        launch.setEnabled(true);
        cancelled = true;
        graphics = null;
        mainPanel.setVisible(false);
        Projectile dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()), Double.parseDouble(gravity.getText()));
        dart.launch(dt);
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

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(1, 2));
        mainPanel.add(leftPanel, BorderLayout.WEST);
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(background);
        controlPanel.setLayout(new GridLayout(10, 1));
        leftPanel.add(controlPanel);

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

        controlPanels[2].add(new JLabel("Fluid Density (kg/m^3)"));
        if (fluidDensityF == null) {
            fluidDensityF = new JTextField("1.225", 10);
            fluidDensityF.addActionListener(this);
        }
        controlPanels[2].add(fluidDensityF);

        controlPanels[3].add(new JLabel("Surface Area (m^3)"));
        if (surfaceAreaF == null) {
            surfaceAreaF = new JTextField("0.00012668", 10);
            surfaceAreaF.addActionListener(this);
        }
        controlPanels[3].add(surfaceAreaF);

        controlPanels[4].add(new JLabel("Mass (kg)"));
        if (massF == null) {
            massF = new JTextField("0.001", 10);
            massF.addActionListener(this);
        }
        controlPanels[4].add(massF);


        controlPanels[6].add(new JLabel("Velocity (m/s)"));
        if (velocity == null) {
            velocity = new JTextField("100", 10);
            velocity.addActionListener(this);
        }
        controlPanels[6].add(velocity);

        controlPanels[7].add(new JLabel("Launch Angle (degrees)"));
        if (launchAngle == null) {
            launchAngle = new JTextField("45", 10);
            launchAngle.addActionListener(this);
        }
        controlPanels[7].add(launchAngle);

        controlPanels[8].add(new JLabel("Gravity (m/s^2)"));
        if (gravity == null) {
            gravity = new JTextField("-9.80665", 10);
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
            g.fillRect(100, getHeight() - 200, getWidth() - 400 - 250, 2);
            g.fillRect(100, 150, 2, getHeight() - 100 - 250);
        }
        mainPanel.add(new JLabel(new ImageIcon(graphics)), BorderLayout.CENTER);


        if (yAxis == null) {
            yAxis = new JPanel();
            yAxis.setLayout(new GridLayout(40, 1));
            yAxis.setBackground(background);
            JPanel[] yAxes = new JPanel[40];

            for (int i = 0; i < 40; i++) {
                yAxes[i] = new JPanel();
                yAxes[i].setLayout(new FlowLayout());
                yAxes[i].setBackground(background);
                yAxis.add(yAxes[i]);
            }

            this.dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                    Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                    Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                    Double.parseDouble(massF.getText()), Double.parseDouble(gravity.getText()));
            this.dart.launch(dt);
            if (maxVelocity == 0)  {
                maxVelocity = Double.parseDouble(velocity.getText()) * 1.4;
                minVelocity = 0;
            }

            JLabel label = new JLabel("                                                         " +
                    String.format("%.0f", length / (getWidth() - 400 - 300) * (getHeight() - 100 - 250) * 4 / 4) +
                    "m");
            label.setForeground(axesColor);
            yAxes[3].add(label);

            label = new JLabel("                                                         " +
                    String.format("%.0f", length / (getWidth() - 400 - 300) * (getHeight() - 100 - 250) * 3 / 4) +
                    "m");
            label.setForeground(axesColor);
            yAxes[12].add(label);

            label = new JLabel("                                                         " +
                    String.format("%.0f", length / (getWidth() - 400 - 300) * (getHeight() - 100 - 250) * 2 / 4) +
                    "m");
            label.setForeground(axesColor);
            yAxes[21].add(label);

            label = new JLabel("                                                         " +
                    String.format("%.0f", length / (getWidth() - 400 - 300) * (getHeight() - 100 - 250) * 1 / 4) +
                    "m");
            label.setForeground(axesColor);
            yAxes[30].add(label);
        }
        leftPanel.add(yAxis);


        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(lowPanel, BorderLayout.SOUTH);

        if (xAxis == null) {
            xAxis = new JPanel();
            xAxis.setLayout(new GridLayout(1, 7));
            xAxis.setBackground(background);
            JPanel[] xAxes = new JPanel[40];
            JPanel[][] xAxes2 = new JPanel[40][4];

            for (int i = 0; i < 40; i++) {
                xAxes[i] = new JPanel();
                xAxes[i].setLayout(new GridLayout(4, 1));
                xAxes[i].setBackground(background);
                xAxis.add(xAxes[i]);

                for (int j = 0; j < 4; j++) {
                    xAxes2[i][j] = new JPanel();
                    xAxes2[i][j].setLayout(new BorderLayout());
                    xAxes2[i][j].setBackground(background);
                    xAxes[i].add(xAxes2[i][j]);
                }
            }

            JLabel label = new JLabel(String.format("%.0f", length * 1 / 4) + "m");
            label.setForeground(axesColor);
            xAxes2[17][0].add(label, BorderLayout.WEST);

            label = new JLabel(String.format("%.0f", length * 2 / 4) + "m");
            label.setForeground(axesColor);
            xAxes2[24][0].add(label, BorderLayout.WEST);

            label = new JLabel(String.format("%.0f", length * 3 / 4) + "m");
            label.setForeground(axesColor);
            xAxes2[31][0].add(label, BorderLayout.WEST);

            label = new JLabel(String.format("%.0f", length * 4 / 4) + "m");
            label.setForeground(axesColor);
            xAxes2[38][0].add(label, BorderLayout.WEST);
        }
        lowPanel.add(xAxis);

        toolBar = new JPanel();
        toolBar.setLayout(new BorderLayout());
        toolBar.setVisible(true);
        lowPanel.add(toolBar);


        toolBarL = new JPanel();
        toolBarL.setLayout(new FlowLayout());
        toolBarL.setVisible(true);
        toolBar.add(toolBarL, BorderLayout.WEST);

        toolBarRSection = new JPanel();
        toolBarRSection.setLayout(new GridLayout(2, 1));
        toolBar.add(toolBarRSection, BorderLayout.EAST);
        toolBarR = new JPanel[2];
        toolBarR[0] = new JPanel();
        toolBarR[0].setLayout(new FlowLayout());
        toolBarR[0].setVisible(true);
        toolBarRSection.add(toolBarR[0]);
        toolBarRSection.add(toolBarR[0]);
        toolBarR[1] = new JPanel();
        toolBarR[1].setLayout(new FlowLayout());
        toolBarR[1].setVisible(true);

        toolBarR[0].add(new JLabel("Distance:                   "));
        toolBarR[0].add(new JLabel("Max Height:                    "));
        toolBarR[0].add(new JLabel("Time in Flight:                  "));
        toolBarR[0].add(new JLabel("Maximum Velocity Reached:                    "));
        toolBarR[0].add(new JLabel("Minimum Velocity Reached:                    "));

        toolBarR[1].add(new JLabel("Current Velocity:                          "));



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

        toolBarM = new JPanel();
        toolBarM.setLayout(new GridLayout(3, 1));
        toolBarM.setVisible(true);
        toolBar.add(toolBarM, BorderLayout.CENTER);

        if (gradient == null) {
            gradient = new BufferedImage(getWidth() / 7, 30, BufferedImage.TYPE_INT_RGB);
            Graphics2D gr = (Graphics2D) gradient.getGraphics();
            int red = 0;
            int green = 0;
            int blue = 255;
            for (int i = 0; i < 52; i++) {
                green = 5 * i;
                gr.setColor(new Color(red, green, blue));
                gr.fillRect(i * getWidth() / (7 * 51 * 4 + 1), 0, getWidth() / (7 * 51 * 4 + 1) * 2, 30);

            }
            for (int i = 1; i < 52; i++) {
                blue -= 5;
                gr.setColor(new Color(red, green, blue));
                gr.fillRect((51 + i) * getWidth() / (7 * 51 * 4 + 1), 0, getWidth() / (7 * 51 * 4 + 1) * 2, 30);
            }
            for (int i = 1; i < 52; i++) {
                red = 5 * i;
                gr.setColor(new Color(red, green, blue));
                gr.fillRect((51 + 51 + i) * getWidth() / (7 * 51 * 4 + 1), 0, getWidth() / (7 * 51 * 4 + 1) * 2, 30);
            }
            for (int i = 1; i < 52; i++) {
                green -= 5;
                gr.setColor(new Color(red, green, blue));
                gr.fillRect((51 + 51 + 51 + i) * getWidth() / (7 * 51 * 4 + 1), 0, getWidth() / (7 * 51 * 4 + 1) * 2, 30);
            }
        }
        JPanel gradientPanel = new JPanel();
        gradientPanel.add(new JLabel(new ImageIcon(gradient)));
        toolBarM.add(gradientPanel);

        if (gradientLabelPanel == null) {
            gradientLabelPanel = new JPanel();
            gradientLabelPanel.setLayout(new FlowLayout());
            gradientLabelPanel.add(new JLabel("0 m/s                                                         "));
            gradientLabelPanel.add(new JLabel(String.format("%.0f", Double.parseDouble(velocity.getText()) * 1.4)  + " m/s"));
        }

        toolBarM.add(gradientLabelPanel);

        add(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void launchProjectile() {
        this.dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()), Double.parseDouble(gravity.getText()));
        this.dart.launch(dt);
        toolBarR[0] = new JPanel();
        toolBarR[0].setLayout(new FlowLayout());
        toolBarR[0].setVisible(true);
        toolBarR[1] = new JPanel();
        toolBarR[1].setLayout(new FlowLayout());
        toolBarR[1].setVisible(true);
        toolBarRSection.setVisible(false);
        toolBarRSection = new JPanel();
        toolBarRSection.setLayout(new GridLayout(2, 1));
        toolBarRSection.add(toolBarR[0]);
        toolBarRSection.add(toolBarR[1]);
        toolBar.add(toolBarRSection, BorderLayout.EAST);
        toolBarR[0].add(new JLabel("Distance: " + String.format("%.2f", dart.getX_position()) + "m     "));
        toolBarR[0].add(new JLabel("Max Height: " + String.format("%.2f", dart.getMaxHeight()) + "m     "));
        toolBarR[0].add(new JLabel("Time in Flight: " + String.format("%.2f", dart.getTime()) + "s      "));
        toolBarR[0].add(new JLabel("Maximum Velocity Reached: " + String.format("%.2f", dart.getMaxVelocity()) + "m/s      "));
        toolBarR[0].add(new JLabel("Minimum Velocity Reached: " + String.format("%.2f", dart.getMinVelocity()) + "m/s      "));

        toolBarR[1] = new JPanel();
        toolBarR[1].setLayout(new FlowLayout());

        this.dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                Double.parseDouble(massF.getText()), Double.parseDouble(gravity.getText()));

        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!(dart.getY_position() < 0 && dart.getAngle() < 0)) {
                    if (!cancelled) {
                        long start = System.nanoTime();
                        process(null);
                        dart.move(dt);
                        while (System.nanoTime() - start < 100000);
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
                int rr = 0;
                int gg = 0;
                int bb = 255;


                int increment = (int) ((maxVelocity - minVelocity) / 4);
                if (dart.getVelocity() > maxVelocity) {
                    rr = 255;
                    bb = 0;
                } else if (!(dart.getVelocity() < minVelocity)) {
                    if (dart.getVelocity() <= minVelocity + increment) {
                        gg += 255 * (int) (dart.getVelocity() - minVelocity) / increment;
                    } else {
                        gg = 255;
                        if (dart.getVelocity() <= minVelocity + increment * 2) {
                            bb -= 255 * (int) (dart.getVelocity() - minVelocity - increment) / increment;
                        } else {
                            bb = 0;
                            if (dart.getVelocity() <= minVelocity + increment * 3) {
                                rr += 255 * (int) (dart.getVelocity() - minVelocity - increment * 2) / increment;
                            } else {
                                rr = 255;
                                if (dart.getVelocity() <= minVelocity + increment * 4) {
                                    gg -= 255 * (int) (dart.getVelocity() - minVelocity - increment * 3) / increment;
                                } else {
                                    gg = 0;
                                }
                            }
                        }
                    }
                }

                g.setColor(new Color(rr, gg, bb));
                g.fillRect((int) (100 + dart.getX_position() / length * (getWidth() - 400 - 300)),
                        (int) (getHeight() - 200 - dart.getY_position() / length * (getWidth() - 400 - 300)),
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
            xAxis = null;
            yAxis = null;
            gradient = null;
            gradientLabelPanel = null;
            mainPanel.setVisible(false);
            Projectile dart = new Projectile(Double.parseDouble(velocity.getText()), Double.parseDouble(launchAngle.getText()),
                    Double.parseDouble(height.getText()), Double.parseDouble(dragCoefficientF.getText()),
                    Double.parseDouble(fluidDensityF.getText()), Double.parseDouble(surfaceAreaF.getText()),
                    Double.parseDouble(massF.getText()), Double.parseDouble(gravity.getText()));
            dart.launch(dt);
            maxVelocity = Double.parseDouble(velocity.getText()) * 1.4;
            minVelocity = 0;
            length = dart.getX_position();
            maxHeight = dart.getMaxHeight();
            if (maxHeight > length * (getHeight() - 350) / (getWidth() - 650)) {
                length = maxHeight * (getWidth() - 650) / (getHeight() - 350);
            }
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