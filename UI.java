/*
	Created By: Jared Follet, Edward Yaroslavsky, Adam Undus and Anthony Bruno
	Modified On: 5/12/2020

*/
package miccota;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTextArea;

public class UI {
 
    private JFrame frame; //Entire program UI.
    private JLabel lblstat = new JLabel("CC: Off"); //Default CCM status to off.
    private JLabel currspeed = new JLabel("0"); //Default engine speed to 0.
    private static JLabel speedo = new JLabel("Current Speed: 0.0 MPH"); //Default CCM targetSpeed to 0
    private JSlider speed; //Slider to adjust speed of engine manually.
    private JTextArea console; //CCM UI Console.
    private static CruiseControl cc = new CruiseControl(); //New instance of CCM class.
    private static boolean brakeDown = false;
    private static boolean acceleratorDown = false; //Neither brake nor accelator applied.
    private static JPasswordField pass = new JPasswordField(10); //Password for get log function to enforce security policy.

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UI window = new UI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println("Speedometer Run Time " + count);
            double speedd = cc.getSpeed(); //Constantly update speed.
            double scale = Math.pow(10, 1);
            speedo.setText("Current Speed: " + Double.toString(Math.round(speedd * scale) / scale) + " MPH"); //Display CCM current speed, adjusting for new values.
        }
    }

    /**
     * Create the application.
     */
    public UI() {
        initialize();
    }

    private boolean validSpeed() { //Checks if the desired speed for the CCM is between its correct operating speeds, 25mph and 150mph.
        return cc.getSpeed() >= 25 && cc.getSpeed() <= 150;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
		//Format UI
        frame = new JFrame();
        frame.setTitle("MICCOTA Cruise Control"); //Set UI Window title.
        frame.setBounds(100, 100, 1024, 576);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.setPreferredSize(new Dimension(1024, 576));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setLayout(null);
		//JFrame icon in top left corner. Set to a car for relevance.
        ImageIcon img = new ImageIcon("tcicon.jpeg");
        frame.setIconImage(img.getImage());
	    //Create the console/text area to visually display current status and actions from CCM.
        console = new JTextArea();
        console.setBounds(650, 100, 275, 200);
        console.setEditable(false);
        frame.getContentPane().add(console);
        //Adjust CCM display properties.
        lblstat.setBounds(750, 31, 200, 20);
        lblstat.setFont(new Font("Monospaced", Font.PLAIN, 20));
        frame.getContentPane().add(lblstat);
		//Adjust current speed display properties
        currspeed.setBounds(60, 85, 300, 100);
        currspeed.setFont(new Font("Monospaced", Font.BOLD, 20));
        frame.getContentPane().add(currspeed);
		//Adjust CCM current speed display properties
        speedo.setBounds(60, 160, 300, 100);
        speedo.setFont(new Font("Monospaced", Font.BOLD, 20));
        frame.getContentPane().add(speedo);
		//Create CCM Actiate button, lower left of UI
        JButton On = new JButton("Activate");
        On.setBounds(50, 400, 175, 100);
        On.setFont(new Font("Monospaced", Font.BOLD, 20));
        On.setBackground(Color.GREEN);
        frame.getContentPane().add(On);
		//Create CCM Deactivate button, right of Activate button.
        JButton Off = new JButton("Deactivate");
        Off.setBounds(275, 400, 175, 100);
        Off.setFont(new Font("Monospaced", Font.BOLD, 20));
        Off.setBackground(Color.red);
        frame.getContentPane().add(Off);
		//Create CCM Increase Speed by 1 button, above activation controls.
        JButton Up = new JButton("Increase Speed by 1");
        Up.setBounds(100, 300, 150, 50);
        Up.setFont(new Font("Monospaced", Font.BOLD, 10));
        Up.setBackground(Color.lightGray);
        frame.getContentPane().add(Up);
		//Create CCM Decrease Speed by 1 button, above activation controls.
        JButton Down = new JButton("Decrease Speed by 1");
        Down.setBounds(250, 300, 150, 50);
        Down.setFont(new Font("Monospaced", Font.BOLD, 10));
        Down.setBackground(Color.LIGHT_GRAY);
        frame.getContentPane().add(Down);
		//Create UI Turn Car On button, bottom right
        JButton CarOn = new JButton("Turn Car On");
        CarOn.setBounds(625, 425, 150, 50);
        CarOn.setFont(new Font("Monospaced", Font.BOLD, 15));
        frame.getContentPane().add(CarOn);
		//Create UI Turn Car Off button, right of Turn Car On Button
        JButton CarOff = new JButton("Turn Car Off");
        CarOff.setBounds(800, 425, 150, 50);
        CarOff.setFont(new Font("Monospaced", Font.BOLD, 15));
        frame.getContentPane().add(CarOff);
		//Create UI Turn Car On button, above car on/off buttons.
        JButton Brake = new JButton("Brake");
        Brake.setBounds(625, 350, 150, 50);
        Brake.setFont(new Font("Monospaced", Font.BOLD, 15));
        Brake.setBackground(Color.white);
        frame.getContentPane().add(Brake);
		//Create UI Get Log Button, below turn car on/off buttons.
        JButton getlog = new JButton("Get Logs");
        getlog.setBounds(740, 500, 100, 30);
        getlog.setFont(new Font("Monospaced", Font.BOLD, 14));
        getlog.setBackground(Color.white);
        frame.getContentPane().add(getlog);

        pass.setBounds(845, 505, 100, 20); //add password field.
        frame.getContentPane().add(pass); //Grab users inputted password to be handled and authorized(or not).
		//Create Accelerator Button for Car.
        JButton Accelerator = new JButton("Accelerator");
        Accelerator.setBounds(800, 350, 150, 50);
        Accelerator.setFont(new Font("Monospaced", Font.BOLD, 15));
        Accelerator.setBackground(Color.white);
        frame.getContentPane().add(Accelerator);
		//Create slider properties for setting vehicle speed outside of CCM.
        speed = new JSlider(JSlider.HORIZONTAL, 0, 150, 0);
        speed.setBounds(100, 100, 300, 100);
        speed.setMajorTickSpacing(25);
        speed.setMinorTickSpacing(1);
        speed.setPaintTicks(true);
        speed.setPaintLabels(true);
        speed.addChangeListener(new SliderListener());
        frame.getContentPane().add(speed);

        // LISTENERS

        On.addActionListener(new ActionListener() { //Lisens for the CCM activate button.
            public void actionPerformed(ActionEvent e) {
                if (!cc.isCarOn()) {
                    console.setText("Car must be on to enable CC.");
                }
                if (!validSpeed()) {
                    console.setText("Speed Must be between 25 and 150");

                } else if (cc.getStatus() == Status.READY) {
                    cc.enable();
                    lblstat.setText("CC: On");
                    console.setText("Status is Engaged");
                } else {
                    console.setText("CC is already Engaged");
                }

            }
        });

        Off.addActionListener(new ActionListener() { //Lisens for the CCM Deactivate button.
            public void actionPerformed(ActionEvent e) {
                if (cc.getStatus() == Status.ENGAGED) {
                    cc.disable();
                    lblstat.setText("CC: Off");
                    console.setText("Cruise Control deactivated");
                } else {
                    console.setText("Cruise Control is already deactivated");
                }
            }
        });

        Up.addActionListener(new ActionListener() { //Lisens for the Increase Speed By 1 Button press.
            public void actionPerformed(ActionEvent e) {
                if (!cc.isCarOn()) {
                    console.setText("Turn the car on first");
                } else if (cc.getStatus() != Status.ENGAGED) {
                    console.setText("The Cruise Control must be Engaged");
                } else if (cc.getSpeed() >= 150) {
                    console.setText("Cannot increase speed past 150 MPH");
                } else {
                    cc.increaseSpeed();
                    console.setText("Speed Increased.");
                }

            }
        });

        Down.addActionListener(new ActionListener() { //Lisens for the Decrease Speed By 1 Button press.
            public void actionPerformed(ActionEvent e) {
                if (!cc.isCarOn()) {
                    console.setText("Turn the car on first");
                } else if (cc.getStatus() != Status.ENGAGED) {
                    console.setText("The Cruise Control must be Engaged");
                } else if (cc.getSpeed() <= 25) {
                    console.setText("Cannot decrease speed past 25 MPH");
                } else {
                    cc.decreaseSpeed();
                    console.setText("Speed Decreased.");
                }
            }
        });

        Brake.addActionListener(new ActionListener() { //Lisens for the Brake Button press.
            public void actionPerformed(ActionEvent e) {
                if (cc.isCarOn() == false) {
                    console.setText("Car is off. Cannot use brake");
                } else {
                    if (brakeDown) {
                        brakeDown = false;
                        cc.brakeReleased();
                    } else {
                        brakeDown = true;
                        cc.brakeApplied();
                        lblstat.setText("CC: Off");
                    }
                }
            }
        });

        Accelerator.addActionListener(new ActionListener() { //Lisens for the Accelerator Button press.
            public void actionPerformed(ActionEvent e) {
                if (cc.isCarOn() == false) {
                    console.setText("Car is off. Cannot use Accelerator");
                } else {
                    if (acceleratorDown) {
                        acceleratorDown = false;
                        cc.acceleratorReleased();
                        lblstat.setText("CC: On");
                    } else {
                        acceleratorDown = true;
                        cc.acceleratorApplied();
                        lblstat.setText("CC: Off");
                    }
                }
            }
        });

        CarOn.addActionListener(new ActionListener() { //Lisens for the Turn Car On Button press.
            public void actionPerformed(ActionEvent e) {
                if (cc.isCarOn()) {
                    console.setText("Car is already on");
                } else {
                    cc.turnOnCar();
                    console.setText("Car is on\n");
                }
            }
        });

        CarOff.addActionListener(new ActionListener() { //Lisens for the Turn Car Off Button press.
            public void actionPerformed(ActionEvent e) {
                if (cc.isCarOn() == false) {
                    console.setText("Car is already off");
                } else if (cc.getSpeed() != 0) {
                    console.setText("Cannot turn off the car while in motion");
                } else {
                    currspeed.setText(Integer.toString(0));
                    speed.setValue(0);
                    cc.disable();
                    cc.turnOffCar();
                    console.setText("Car Turned Off.");
                    cc.setTargetSpeed(0);
                }
            }
        });

        getlog.addActionListener(new ActionListener() { //Listens for the Get Log Button Press.
            public void actionPerformed(ActionEvent e) {
                String CP = "miccota"; //Correct Password. 
                String check = new String(pass.getPassword());

                if (CP.equals(check)) { //Check if the user inputted password matches the correct password, if so, print logs to text files.
                    cc.printLogsEngine("engine.txt");
                    cc.printLogsSpeed("speed.txt");
                    cc.printLogsBrake("brake.txt");
                    cc.printLogsCC("cc.txt");
                    console.setText("Log Files Printed");
                } else { //If passwords do not match, reject users request.
                    console.setText("Incorrect admin password. Cannot print logs.");
                }

            }
        });

    }

    class SliderListener implements ChangeListener { //Seperate Class listens for changes/activity with the slider.
        public void stateChanged(ChangeEvent e) {
            int speednum = speed.getValue(); //get the new value of the slider.
            currspeed.setText(Integer.toString(speednum));
            if (cc.getStatus() == Status.ENGAGED) { //Check if the CCM module is engaged. 
                if (speednum >= 25 && speednum <= 150 && cc.isCarOn()) { //Confirm targetSpeed is in valid operating range.
                    cc.setTargetSpeed(speednum); //Adjust speed 
                    lblstat.setText("CC: On"); //Update CCM state display.
                } else {
                    lblstat.setText("Invalid Speed");
                }
            } else {
                console.setText("Cruise control must be enganged.");
            }
        }
    }

}
