//Name: DrPotluck
//Time: Total: 960 min
//References: Many class notes/My friends Zack and Emily. The internet.

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.media.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.scene.input.MouseDragEvent.*;
import javafx.scene.transform.*;
import java.text.DecimalFormat;
import java.math.RoundingMode;


//Something that increases to represent engine RPMs
//Some key events representing increasing RPMs
//a key that decreases RMPs
//Maybe a key that tells the engine to just keep on going faster


public class ShiftOrDont extends Application {
	private int width = 800;
	private int height = 600;
	private boolean running = true;
	private double rpms; //x1000
	private double topRPM = 7.5;
	private double stall = .3;
	private double ogRPMS;
	private boolean accelerate;
	private boolean decelerate;
	private boolean coast;
	private boolean clutch; //false = let out, true = pushed in
	private boolean ignition;
	private double friction = .005;
	private boolean blownEngine;
	private boolean centerL; //between first and second
	private boolean centerR; //between fifth and sixth
	private boolean center = true; //between third and fourth
	private boolean up;
	private boolean down;
	private int currentGear;//0 is neutral
	private int lastGear;
	private boolean inGear;
	private double speed;
	private double rpmStart;
	private double rpmEnd;
	private double rpmChange;
	private double totalChange;
	private boolean shiftInProg;


		@Override
		public void start(Stage stage) {
			Group root = new Group();
			Scene scene = new Scene(root, width, height);
			stage.setTitle("Shift The Computer Or Make Your Own Choices Because You're An Adult (possibly)");
			stage.setScene(scene);
		stage.show();

		//Add car
		Car myCar = new Car();
		root.getChildren().add(myCar);

		//Add car gearing
		Gear[] g = new Gear[7];
		g[0] = new Gear("0", 0, 0, .055);//neutral. might run into division by 0 error
		g[1] = new Gear("1", 2, 25, 1.032);
		g[2] = new Gear("2", 3, 45, 1.016);
		g[3] = new Gear("3", 7, 75, 1.008);
		g[4] = new Gear("4", 11, 85, 1.004);
		g[5] = new Gear("5", 15, 150, 1.002);
		g[6] = new Gear("6", 30, 200, 1.001);//Eventually change this to reverse, will currently set as super speed

		//speedometer
		Group speedometer = new Group();
		Rectangle speedBox = new Rectangle(200, 100);
		speedBox.setFill(Color.DARKGREY);
		speedBox.setStroke(Color.GOLD);
		speedBox.setStrokeWidth(10);
		speedometer.getChildren().add(speedBox);
		speedometer.setTranslateX(550);
		speedometer.setTranslateY(50);


		//MPH reading for Speedometer
		Text mph = new Text("0");
		mph.setFont(Font.font("Impact", 60));
		mph.setStroke(Color.TURQUOISE);
		speedometer.getChildren().add(mph);
		mph.setTranslateX(35);
		mph.setTranslateY(75);

		root.getChildren().add(speedometer);

		//Create tachometer
		Group tachometer = new Group();

		Line rpmLevel = new Line (0, 0, 150, 0);
		rpmLevel.setStrokeWidth(5);
		tachometer.getChildren().add(rpmLevel);

		tachometer.setTranslateX(300);
		tachometer.setTranslateY(400);

		root.getChildren().add(tachometer);

		//Key Event Handler for Accelerating and Decelerating and Shifting
		EventHandler<KeyEvent> pedals = new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent e){
				//Reset Vehicle
				if (e.getCode() == KeyCode.R){
					currentGear = 0;
					blownEngine = false;
					ignition = false;
					rpms = 0;
					speed = 0;
					centerL = false;
					center = false;
					centerR = false;
					up = false;
					down = false;
					for (int i = 0; i < g.length; i++){//don't reset 0 because it makes it fail
						g[i].reset();
					}
					System.out.println("Thank you for visiting Mikechanics Car Shop");
				}
				if (e.getCode() == KeyCode.I){
					g[currentGear].errorReport();
				}
				//Turn car on and off
				if (e.getCode() == KeyCode.ENTER && !ignition && clutch){ //Turn car on
					//currentGear = 0;
					ignition = true;
					System.out.println("Igntion: "+ignition);
				} else if (e.getCode() == KeyCode.ENTER && ignition){ //Turn car off
					ignition = false;
					System.out.println("Igntion: "+ignition);
				}

				//Accelerate and Decelerate
				if (e.getCode() == KeyCode.W && !blownEngine){ //Make car accelerate
					coast = false;
					accelerate = true;
				}
				if (e.getCode() == KeyCode.S && !blownEngine){
					coast = false;
					decelerate = true;
				}

				//Shift Gears
				if (e.getCode() == KeyCode.SHIFT){
					inGear = false;
					clutch = true;
					centerL = false;
					center = true;
					centerR = false;
					up = false;
					down = false;
				}

				//Arrowkey shifting method (left and up, left and down, up, down, right and up, right and down)
				if (e.isShiftDown() && e.getCode() == KeyCode.LEFT){
					if (center){
						centerL = true;
						center = false;
						centerR = false;
					} else if (centerR){
						centerL = false;
						center = true;
						centerR = false;
					}
				}
				if (e.isShiftDown() && e.getCode() == KeyCode.RIGHT){
					if (center){
						centerL = false;
						center = false;
						centerR = true;
					} else if (centerL){
						centerL = false;
						center = true;
						centerR = false;
					}
				}
				if (e.isShiftDown() && e.getCode() == KeyCode.UP){
					up = true;
					down = false;
				}
				if (e.isShiftDown() && e.getCode() == KeyCode.DOWN){
					down = true;
					up = false;
				}
			}
		};
		scene.setOnKeyPressed(pedals);

		//key released
		EventHandler<KeyEvent> pedalsOff = new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent e){
				//Accelerate and Decelerate
				if (e.getCode() == KeyCode.W && !blownEngine){ //Make car coast
					coast = true;
					accelerate = false;
				}
				if (e.getCode() == KeyCode.S && !blownEngine){
					coast = true;
					decelerate = false;
				}
				if (e.getCode() == KeyCode.SHIFT){
					clutch = false;
				}

				//Stall car if let out shift too early
				//if (!e.isShiftDown() && ignition && rpms < .3){
				//	ignition = false;
				//}
			}
		};
		scene.setOnKeyReleased(pedalsOff);

		//Animation Timer
		AnimationTimer t = new AnimationTimer(){
			@Override
			public void handle(long time){
				ogRPMS = rpms;
				//System.out.println("Current Gear: "+currentGear);

				//Find current gear
				if (!clutch && up && centerL){
					currentGear = 1;
				} else if (!clutch&& down && centerL){
					currentGear = 2;
				} else if (!clutch&& up && centerR){
					currentGear = 5;
				} else if (!clutch&& down && centerR){
					currentGear = 6;
				} else if (!clutch && up && center){
					currentGear = 3;
				} else if (!clutch && down && center){
					currentGear = 4;
				} else if (clutch){
					currentGear = 0;
				}

				//Get RPMS relative to speed when shifting into a new gear
				if (!inGear && currentGear > 1){
					rpmStart = rpms;
					rpmEnd = g[currentGear].getRpmDrop(speed, rpms);
					rpmChange = Math.abs(rpmStart - rpmEnd);
					inGear = true;
					shiftInProg = true;
				}

				//Smoothly shift the rpms from where they are in one gear to where they should be in the next
				if (totalChange < rpmChange && shiftInProg){
					totalChange += rpmChange/10;
					if (rpmStart > rpmEnd){
						rpms -= rpmChange/10;
						System.out.println("Reducing RPMS: "+rpms);
					} else {
						rpms += rpmChange/10;
						System.out.println("Increasing RPMS: "+rpms);
					}
				} else if (rpmChange - totalChange < .05 && shiftInProg){
					rpmChange = 0;
					totalChange = 0;
					shiftInProg = false;
					System.out.println("Change Complete");
				}

				//Action based on current gear
				switch (currentGear){
					case 0:
							if (ignition){
								speed = g[0].getCoastSpeed(speed);
							}
							if (accelerate){
									rpms += rpms*.055;
								} else {
									rpms -= rpms*.028;
								}
							break;
					case 1:
							rpms = g[1].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[1].getSpeed(rpms);
							speed = g[1].getSpeed(rpms);
							break;
					case 2:
							rpms = g[2].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[2].getSpeed(rpms);
							speed = g[2].getSpeed(rpms);
							break;
					case 3:
							rpms = g[3].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[3].getSpeed(rpms);
							speed = g[3].getSpeed(rpms);
							break;
					case 4:
							rpms = g[4].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[4].getSpeed(rpms);
							speed = g[4].getSpeed(rpms);
							break;
					case 5:
							rpms = g[5].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[5].getSpeed(rpms);
							speed = g[5].getSpeed(rpms);
							break;
					case 6:
							rpms = g[6].drive(ignition, rpms, accelerate, decelerate, coast);
							speed = g[6].getSpeed(rpms);
							speed = g[6].getSpeed(rpms);
							break;

				}
				if (ignition){
					for (int i = 0; i < g.length; i++){
						 if (currentGear != 0){
							 running = g[i].checkEngine(rpms);
							 //speed = g[currentGear].getSpeed(rpms);
						 }
					}
				}

				//System.out.println("RPMS: "+rpms);
				//Update Speedometer
				DecimalFormat myFormatter;
				myFormatter = new DecimalFormat("0.0");
				myFormatter.setRoundingMode(RoundingMode.UP);
				String s = myFormatter.format(speed);
				mph.setText(s);

				//Car is turned on
				if (ignition && clutch && rpms < .5){
					rpms += .03;
				}

				//If engine is turned off while in motion
				if (!ignition){
					if ((rpms > 0.2 || speed > 0.2)){
						rpms -= rpms*.17;
						speed -= speed*.2;
					} else {
						rpms = 0;
						speed = 0;
					}
				}

				//rpm corresponds to angle of tachometer
				double rpmsChange = rpms - ogRPMS;
				if (rpms < .2){
					rpmLevel.getTransforms().add(new Rotate(0, 120, 0));
				} else {
					rpmLevel.getTransforms().add(new Rotate(rpmsChange*25, 120, 0));
				}

				//Note which gear the car was in last
			}
		};
		t.start();
	}
	public double getRPMS(){
		return rpms;
	}
}



