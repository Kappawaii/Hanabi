package hanabi;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

import hanabi.controller.TerminalController;
import hanabi.model.GameModel;
import hanabi.model.InteractionManager;
import hanabi.model.Player;
import hanabi.view.TerminalView;

public class Hanabi {

	private GameModel game;
	private TerminalView view;
	private TerminalController controller;
	private InteractionManager interactionManager;

	private int playerCount;

	private Hanabi() {
		game = new GameModel();
	}

	/*
	 * Terminal version
	 */
	public Hanabi(InputStream inputStream, PrintStream printStream) {
		this();
		view = new TerminalView(Objects.requireNonNull(printStream));
		controller = new TerminalController(Objects.requireNonNull(inputStream));
		interactionManager = new InteractionManager(controller, view);
		playerCount = interactionManager.getPlayerCount(2, 8);
	}

	/*
	 * Graphical version
	 */
	public Hanabi(int jsaispasquoimettreici) {
		this();
		// TODO
	}

	/**
	 * Uses a loop to keep the game going until oneTurn() returns 1 or -1 ( endgame
	 * variable turns true and stops the loop )
	 */
	public void play() {
		boolean lastTurn = false;
		addPlayers(game.getPlayerList());
		game.initNewgame();
		boolean endgame = false;
		while (game.getFuseTokens() > 0 && !endgame) {
			switch (oneTurn(lastTurn)) {
			case 1:
				// Victory !
				view.displayEndGame();
				view.displayScore(game.getScore());
				endgame = true;
			case -1:
				// Defeat
				view.displayEndGame();
				view.displayDefeat();
				endgame = true;
				break;
			case 0:
				break;
			}
		}
	}

	/**
	 * Makes one turn for all the players involved and displays all informations
	 * Also tests if the game is over.
	 * 
	 * Returns codes to indicate the state of the current game. Code : => 0 : Keeps
	 * the game running => 1 : Victory of the players => -1 : Defeat of the players
	 * 
	 * @return Returns Integer
	 */
	public int oneTurn(boolean lastTurn) {
		for (Player p : game.getPlayerList()) {
			view.splashScreen(p);
			controller.waitForLineBreak();
			displayCardsOfAllPlayersBut(p);
			view.displayFireworkStatus(game.getFireworkManager().getFireworkStatus());
			view.displayTokensRemaining(game.getInfoTokens(), game.getFuseTokens());
			view.displayDiscardedCards(game.getCardManager().getDiscardedCards());
			p.playTurn(game.getInfoTokens());
			view.displayEndofTurn();
			controller.waitForLineBreak();
			if (game.instantVictoryState()) {
				return 1;
			}
			if (game.getFuseTokens() <= 0) {
				return -1;
			}
		}
		if (lastTurn) {
			// if last turn, variable is set to true
			return 1;
		}
		if (game.getCardManager().getCardsSize() <= 0) {
			lastTurn = true;
		}
		return 0;
	}

	/**
	 * Add the players at the start of the game.
	 */
	private void addPlayers(ArrayList<Player> players) {
		int i = 0;
		while (i < playerCount) {
			Player temp = new Player(game, interactionManager.getPlayerName(i), controller, view, interactionManager);
			if (!players.contains(temp)) {
				players.add(temp);
				i++;
			} else {
				view.printString("Le joueur existe déjà, veuillez choisir un autre nom");
			}
		}
	}

	/**
	 * Displays all the cards except the ones the player currently playing his turn
	 * has.
	 */
	private void displayCardsOfAllPlayersBut(Player playerNotToDisplay) {
		for (Player player : game.getPlayerList()) {
			if (player != playerNotToDisplay) {
				view.displayCardsOfPlayer(player);
			}
		}
	}
}
