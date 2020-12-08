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

public class Car extends Group{
	public Car(){
		Group vehicle = new Group();
		Rectangle body = new Rectangle(100,75);
		body.setFill(Color.RED);
		vehicle.getChildren().add(body);

		Rectangle hood = new Rectangle(100, 35, 40, 40);
		hood.setFill(Color.RED);
		vehicle.getChildren().add(hood);

		Circle bwheel = new Circle(20, 65, 20);
		Circle fwheel = new Circle(105, 65, 20);
		vehicle.getChildren().add(bwheel);
		vehicle.getChildren().add(fwheel);
		this.getChildren().add(vehicle);
	}

	//create movement
	public void moveCar(int gear, double rpm){
		//mph math done in a different class. move to here?
	}
}