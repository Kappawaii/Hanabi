package hanabi.controller;

import java.awt.Color;
import java.awt.geom.Point2D;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;
import hanabi.model.GameModel;
import hanabi.model.Player;
import hanabi.view.GraphicalView;

public class GraphicalController implements Controller {

	static void startGame( ApplicationContext context ) {
		// get the size of the screen
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		System.out.println("size of the screen (" + width + " x " + height + ")");

		GameModel gamemodel = new GameModel();
		
		Player player = gamemodel.getPlayerList().get(0);
		GraphicalView view = GraphicalView.initGameGraphics(0, 0, 1920, 1080, context);
		
		/* view.displayMenu(gamemodel, ); */
		
		
		view.draw( gamemodel, player);
		
		Point2D.Float location;

		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) { // no event
				continue;
			}
			
			Action action = event.getAction();
			if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) {
				System.out.println("Aaaaahh!");
				context.exit(0);
				return;
			}
			
			if (action != Action.POINTER_DOWN) {
				continue;
			}

			view.draw( gamemodel , player);
		}
	}
	
	public static void main(String[] args) {
		/*
		GameModel game = new GameModel(System.in, System.out);
		game.playOneGame();
		*/
		Application.run(Color.LIGHT_GRAY, GraphicalController::startGame); // attention, utilisation d'une lambda.
	}

	@Override
	public Integer getInt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void waitForLineBreak() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
