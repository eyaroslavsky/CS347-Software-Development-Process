/*
	Created By: Jared Follet, Edward Yaroslavsky, Adam Undus and Anthony Bruno
	Modified On: 5/12/2020

*/
package miccota;

import java.util.*;
import java.text.SimpleDateFormat;

public class Log {
	//Create indpendent hashmaps for 4 seperate logs. Helps organize and enables efficient debugging.
	private HashMap<String, String> engineLog;
	private HashMap<String, Double> speedLog;
	private HashMap<String, Status> brakeLog;
	private HashMap<String, String> ccLog;

	public Log() { //Constructor
		engineLog = new HashMap<String, String>();
		ccLog = new HashMap<String, String>();
		speedLog = new HashMap<String, Double>();
		brakeLog = new HashMap<String, Status>();
	}

	public String getDate() { //Get the current time and date in the format below. Example. 12/05/2020 02:39:12
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //set the format.
		Date date = new Date(); //request the current time and date.
		return formatter.format(date); //format the date to desired output and return.
	}

	public void CClog(Status status) { //CCM Log method that handles the status event changes.
		String date = getDate();
		String logMsg = status.toString();
		this.ccLog.put(date, logMsg);
	}

	public void CClog(Status status, String message) { //Other CCM Log method that handles the status event changes with desired update/exception messages.
		String date = getDate();
		String logMsg = status.toString() + ": " + message;
		this.ccLog.put(date, logMsg);
	}

	public void engineLog(String message) {//Engine Log method that handles the EMS event changes.
		String date = getDate();
		this.engineLog.put(date, message);
	}

	public void speedLog(Double speed) {//Speed Log method that handles any changes in speed.
		String date = getDate();
		this.speedLog.put(date, speed);
	}

	public void brakeLog(Status status) { //Brake Log method that handles any brake presses.
		String date = getDate();
		this.brakeLog.put(date, status);
	}
	//Getters for respective logs.
	public HashMap<String, String> getEngineLog() { 
    	return engineLog;
    }
	
	public HashMap<String, Double> getSpeedLog() {
    	return speedLog;
    }
	
	public HashMap<String, Status> getBrakeLog() {
    	return brakeLog;
    }
	
	public HashMap<String, String> getCCLog() {
    	return ccLog;
    }
}