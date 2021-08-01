/*
	Created By: Jared Follet, Edward Yaroslavsky, Adam Undus and Anthony Bruno
	Modified On: 5/12/2020

*/
package miccota;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CruiseControl {
    private Status status; //Status of the CC. Either Off, Ready or Engaged.
    private double targetSpeed; //The targetspeed set by the CCM after activation.
    private EMS ems; //
    private Log log; //Log files to keep track of all EMS and CCM events.

    public CruiseControl() {
        status = Status.READY;
        targetSpeed = 0;
        ems = new EMS();
        log = new Log();
    }

    public double getSpeed() { 
        return ems.getSpeed(); //Return the current speed of the engine.
    }

    public boolean setTargetSpeed(double newSpeed) {
        if (status != Status.ENGAGED) { //Check if the CCM is engaged, if it is not you cannot set targetSpeed.
            System.out.println("Cannot set speed when CC is not in engaged state");
            return false;
        }
        if (newSpeed < 25 || newSpeed > 150) { //Make sure that the new targetSpeed is between the CCM operating values, 25mph->150mph
            System.out.println("Invalid speed: " + newSpeed);
            return false;
        }

        targetSpeed = newSpeed;
        ems.requestSpeed(targetSpeed); //Update target speed.
        log.speedLog(targetSpeed); //Log the event.
        return true;
    }

    public boolean isCarOn() {
        return ems.isCarOn(); //Return the current state of the car. Either On(true) or Off(False).
    }

    public Status enable() { //Method to change the status of the CCM to Engaged from Ready.
        if (status == Status.READY) { //CCM status be ready, implying the car has a speed between 25mph and 150mph.
            status = Status.ENGAGED;
        }
        log.CClog(status); //Log the event to status log file.
        targetSpeed = ems.getSpeed(); //Update target speed.
        ems.requestSpeed(targetSpeed);
        return status;
    }

    public Status disable() { //Method to disable the CCM, which 
        status = Status.READY;
        log.CClog(status); //Log the status update to the status log file.
        ems.stopAutoAcceleration(); //CCM is now off so Stop engine acceleration to a new target speed.
        return status;
    }

    public Status getStatus() { //Getter. Returns the current status.
        return status;
    }

    public boolean increaseSpeed() { //Method used by Increase Speed By 1 Button.
	    if (status != Status.ENGAGED) { //Check if CCM is engaged, otherwise cant increase.
            System.out.println("Cannot increase speed if system is not in engaged status");
        }
        if (targetSpeed == 150) {
			System.out.println("Cannot increase speed more than the max speed");
        }
        targetSpeed++; //Update target speed +=1
        ems.requestSpeed(targetSpeed); //Send a request to the EMS to change current speed.
        log.speedLog(targetSpeed); //Log the change in speed to speed log file.
        return true;
    }

    public boolean decreaseSpeed() { //Method used by Increase Speed By 1 Button.
        if (status != Status.ENGAGED) { //Check if CCM is engaged, otherwise cant increase.
            System.out.println("Cannot decrease speed if system is not in engaged status");
        }
        if (targetSpeed == 0) {
			System.out.println("Cannot decrease speed if there is no set speed");
        }
        if (targetSpeed == 25) { //Ensure that the target speed does not fall below 25mph.
			System.out.println("Cannot decrease speed more than the min speed");
        }
        targetSpeed--; //decrease speed.
        ems.requestSpeed(targetSpeed); //Update ems.
        log.speedLog(targetSpeed); //Log the event.
        return true;
    }

    public void brakeApplied() { //Method used by Brake Button.
        if (status == Status.ENGAGED) { //Follow these actions when CCM engaged.
            status = Status.READY; //Dis-engage CCM.
            ems.startDeceleration(); //slow down vehicle.
            ems.stopAutoAcceleration();
            log.brakeLog(status); //Log the event.
        } else { //Else just apply brake.
            ems.startDeceleration();
            log.brakeLog(status); //Log the event to Brake log.
        }
    }

    public void brakeReleased() { //
        ems.stopDeceleration(); //Stop braking, maintain current speed.
    }

    public void acceleratorApplied() { //Begin to speed up vehicle.
        ems.startAcceleration();
    }

    public void acceleratorReleased() {
        ems.stopAcceleration(); //Stop acceleration, maintain current speed.
    }

    public void turnOnCar() {
        ems.turnOnCar();
        log.engineLog("On");
    }

    public void turnOffCar() {
        ems.turnOffCar();
        log.engineLog("Off");
    }
    //Print methods below to print the logs to text files. All are called upon entering correct password to get log.
    public void printLogsEngine(String fileName) {
        try {
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);

            int count = 0;
            int recordsToPrint = log.getEngineLog().size(); //get the size of the hashmap to print every record to the text file.
            Iterator<Entry<String, String>> it = log.getEngineLog().entrySet().iterator(); //Create iterator to loop over HASHmap.
            while (it.hasNext() && count < recordsToPrint) { //Use iterator to loop over all entries.
                Map.Entry<String, String> pairs = it.next(); 
                out.write(pairs.getKey() + ": " + pairs.getValue() + "\n"); //Print the Key which is the entry time and date as well as the event that took place.
                count++;
            }
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printLogsSpeed(String fileName) {
        try {
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);

            int count = 0;
            int recordsToPrint = log.getSpeedLog().size();
            Iterator<Entry<String, Double>> it = log.getSpeedLog().entrySet().iterator();
            while (it.hasNext() && count < recordsToPrint) {
                Map.Entry<String, Double> pairs = it.next();
                out.write(pairs.getKey() + ": " + Double.toString(pairs.getValue()) + "\n");
                count++;
            }
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printLogsBrake(String fileName) {
        try {
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);

            int count = 0;
            int recordsToPrint = log.getBrakeLog().size();
            Iterator<Entry<String, Status>> it = log.getBrakeLog().entrySet().iterator();
            while (it.hasNext() && count < recordsToPrint) {
                Map.Entry<String, Status> pairs = it.next();
                out.write(pairs.getKey() + ": Brake Activated" + "\n");
                count++;
            }
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printLogsCC(String fileName) {
        try {
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);

            int count = 0;
            int recordsToPrint = log.getCCLog().size();
            Iterator<Entry<String, String>> it = log.getCCLog().entrySet().iterator();
            while (it.hasNext() && count < recordsToPrint) {
                Map.Entry<String, String> pairs = it.next();
                out.write(pairs.getKey() + ": " + pairs.getValue() + "\n");
                count++;
            }
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
