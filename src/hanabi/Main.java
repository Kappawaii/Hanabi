package hanabi;

import hanabi.model.GameModel;

public class Main {

	public static void main(String[] args) {
		GameModel game = new GameModel(System.in, System.out);
		game.playOneGame();
	}

}
