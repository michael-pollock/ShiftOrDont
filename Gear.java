import javafx.animation.*;

public class Gear {
	public String name;
	private boolean running = true;
	private double low;
	private double high;
	private double accel;
	private double decel = .035;
	private double coast = .01; //coasting while in gear
	private double rpmToMph;//ratio of rpm to mph
	private boolean blownEngine;
	private boolean stalled;
	private double topRPM = 7.5;
	private double stall = .3;
	public double friction = .01;
	private double speed;
	private double rpms;
	private boolean inGear;
	private String[] errorMessage = new String[2];
	private int engineCode;

	//Constructor
	Gear(String gear, double speedLow, double speedHigh, double accelRate){
		name = gear;
		low = speedLow;
		high = speedHigh;
		accel = accelRate;
		rpmToMph = (high - low)/(topRPM-stall);
	}

	//Determine if car is running or not
	public boolean checkEngine(double rpms){
		if (getStall(rpms) && !getBlownEngine(rpms)){
			errorMessage[0] = "You were going too slow in gear "+name+". \nVisit Mechanic by pressing 'r' \nTurn the car on and \nDRIVE FASTER";
			running = false;
		} else if (getBlownEngine(rpms)){
			errorMessage[1] = "Blown Engine in gear "+name+". \nVisit Mechanic by pressing 'r'";
			running = false;
		} else {running = true;}
		return running;
	}

	//Print error report
	public void errorReport(){
		for (int i = 0; i < errorMessage.length; i++){
			if (errorMessage[i] != null){
				System.out.println(errorMessage[i]);
			}
		}
	}

	//Get speed of Vehicle
	public double getSpeed(double rpms){
		speed = low + rpms*rpmToMph;
		return speed; //returns speed of vehicle
	}

	//Get coasting speed of vehicle
	public double getCoastSpeed(double currentSpeed){
		if (currentSpeed > 0){
			speed = currentSpeed - friction;
		}
		return speed;
	}

	//get current RPMS
	public double getRpmDrop(double speed, double rpms){
		rpms = (speed - low)/rpmToMph;
		return rpms;
	}



	//Handle acceleration, deceleration, and coasting when in gear
	public double drive(boolean ignition, double rpms, boolean accelerate,
	boolean decelerate, boolean coasting){
		blownEngine = getBlownEngine(rpms);
		stalled = getStall(rpms);
		if ((!ignition || stalled || blownEngine) && rpms > .2){
			return rpms  - rpms*.17;
		} else if (accelerate){
			return Math.pow(rpms, accel);
		} else if (decelerate){
			return rpms - rpms*decel;
		} else if (coasting){
			return rpms - rpms*coast;
		} else {return 0;}
	}

	public boolean getBlownEngine(double rpms){
		if (rpms > topRPM){ blownEngine = true;}
		return blownEngine;
	}

	public boolean getStall(double rpms){
		if (rpms < stall){
			stalled = true;
			rpms -= rpms*.17;
			}
		return stalled;
	}

	public void setStall(double rpm){
		stall = rpm;
	}

	//Updates in ShiftOrDont when the clutch is pushed in. This kicks the car out of gear and prevents rpmsToSpeed from not letting you accelerate
	public void setInGear(boolean inGear){
		this.inGear = inGear;
	}

	public void reset(){
		rpms = 0;
		stalled = false;
		blownEngine = false;
		running = true;
	}
}