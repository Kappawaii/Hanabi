package hanabi;

import java.awt.Color;
import java.awt.geom.Point2D;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;
import hanabi.controller.GraphicalController;
import hanabi.model.GameModel;
import hanabi.model.Player;
import hanabi.view.GraphicalView;

public class Main {

	static void startGame( ApplicationContext context ) {
		Hanabi hanabi = new Hanabi(context , System.in, System.out );
		hanabi.play();
	}
	
	public static void main(String[] args) {
		/*
		Hanabi hanabi = new Hanabi(System.in, System.out);
		hanabi.play();
		*/
		
		Application.run(Color.LIGHT_GRAY, Main::startGame); // attention, utilisation d'une lambda.
	}

}
