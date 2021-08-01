/*
	Created By: Jared Follet, Edward Yaroslavsky, Adam Undus and Anthony Bruno
	Modified On: 5/12/2020

*/
package miccota;

import java.util.Timer;
import java.util.TimerTask;

public class EMS extends TimerTask { //Extends TimerTask class 
									//A task that can be scheduled for one-time or repeated execution by a Timer.
    private double speed;
    private double targetSpeed;
    public final int ACCELERATION = 5; // us^2
    private Timer timer; //New timer instance.
    private final int REFRESH = 200; // ms
    private boolean acceleratorEngaged;
    private boolean brakeEngaged;
    private final double ACCELERATION_PER_REFRESH = ACCELERATION * ((double) REFRESH / 1000.0); //declare vehicle accerlation rate
    private final double DRIFT_SLOW = 0.2;
    private boolean isCarOn; //Var to hold status of cars current state.
    private boolean ccOn; //Holds CCMs current state.
    // private Log logger;

    public EMS() { //Constructor.
        super(); //Call TimerTask constructor.
		//Declare default variable values.
        speed = 0;
        targetSpeed = 0;
        timer = new Timer();
        acceleratorEngaged = false;
        brakeEngaged = false;
        isCarOn = false;
        ccOn = false;
        startAutoAcceleration();
        // TimerTask task = new EMS();

        // logger = new Log();
        // logger.engineLog("Engine Started.");
        // logger.speedLog(speed);
    }

    public boolean requestSpeed(double newSpeed) { //Method that updates the target speed.
        // logger.engineLog("New Target Speed Requested by CC: " + newSpeed);
        ccOn = true;
        targetSpeed = newSpeed;
        return true;
    }

    public double getSpeed() { //Getter for current ems speed.
        return speed;
    }

    private void startAutoAcceleration() { //Begin timer scheduling to be used to mimic vehicle acceleration.
        // logger.engineLog("Starting to Maintain Target Speed");
        System.out.println("starting task");
        timer.scheduleAtFixedRate(this, 0, (long) REFRESH);
    }

    public void stopAutoAcceleration() { //Set acceleration to off.
        ccOn = false;
    }

    @Override
    public void run() { //Run the timer, updating the speed according to the refresh rate.
        if (brakeEngaged) { // brake takes presedence over acceleration.
            this.speed -= ACCELERATION_PER_REFRESH;
            if (this.speed <= 0) {
            	this.speed = 0;
            }
            return;
        } else if (acceleratorEngaged) { //if accelerating, increase speed.
            this.speed += ACCELERATION_PER_REFRESH;
            if (this.speed >= 150) {
            	this.speed = 150;
            }
            return;
        }

        if (ccOn) { //IF CCM is engaged.
        	//System.out.println("Here");
            if ((int) speed == (int) targetSpeed) { //check if the new speed request is equal to ems current speed. If so, do nothing.
                return;
            } else if (targetSpeed > speed) {
                this.speed += ACCELERATION_PER_REFRESH; //begin vehicle acceleration up to target speeed.
            } else {
                this.speed -= ACCELERATION_PER_REFRESH; //begin vehicle deceleration down to target speed.
            }
        } else {
        	if (speed >= 150) { //Cant go over 150mph
        		this.speed = 150; 
        		return;
        	}
        	else if (speed > 0) { 
                this.speed -= DRIFT_SLOW;
                return;
            }
            else {
            	this.speed = 0;
            	return;
            }
        }

        // logger.speedLog(speed);
    }

    public void startAcceleration() { //Engage Acceleration
        // logger.engineLog("Manual Acceleration Started.");
        acceleratorEngaged = true;
    }

    public void stopAcceleration() { //Disengage Acceleration, maintains current speed.
        // logger.engineLog("Manual Acceleration Stopped.");
        acceleratorEngaged = false;
    }

    public void startDeceleration() { //Engage Deceleration, decreasing speed.
        // logger.engineLog("Manual Deceleration Started.");
        brakeEngaged = true;
    }

    public void stopDeceleration() { //Stop decreasing speed, maintain current.
        // logger.engineLog("Manual Deceleration Stopped.");
        brakeEngaged = false;
    }

    public boolean turnOnCar() { //Turns Car/EMS on.
        if (isCarOn) {
            System.out.println("Cannot turn car on when it is already on.");
            return true;
        }
        isCarOn = true;
        return isCarOn;

    }

    public boolean turnOffCar() { //Turns Car/EMS off.
        if (!isCarOn) {
            System.out.println("Cannot turn car off when it is already off.");
            return false;
        }
        isCarOn = false;
        ccOn = false;
        acceleratorEngaged = false;
        return isCarOn;

    }

    public boolean isCarOn() { //returns if the car is on. True if On, false if Off.
        return isCarOn;
    }

}


