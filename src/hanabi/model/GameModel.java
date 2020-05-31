package hanabi.model;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import hanabi.model.card.Card;
import hanabi.model.card.CardColor;
import hanabi.model.card.CardManager;
import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;
import hanabi.utility.Tuple;

public class GameModel {

	private TerminalView view;
	private TerminalController controller;
	private InteractionManager interactionManager;

	private FireworkManager fireworkManager;
	private CardManager cardManager;
	private ArrayList<Player> players;

	private int playerCount;
	private int infoTokens;
	private int fuseTokens;
	private boolean lastTurn;

	/**
	 * Constructs the game model, needs an input and a output stream.
	 */
	public GameModel(InputStream inputStream, PrintStream printStream) {
		view = new TerminalView(Objects.requireNonNull(printStream));
		controller = new TerminalController(Objects.requireNonNull(inputStream));
		interactionManager = new InteractionManager(controller, view);
		players = new ArrayList<Player>();
		playerCount = interactionManager.getPlayerCount(2, 8);
		addPlayers();
		infoTokens = 8;
		fuseTokens = 3;
		cardManager = new CardManager();
	}

	/**
	 * Uses a loop to keep the game going until oneTurn() returns 1 or -1 
	 * ( endgame variable turns true and stops the loop )
	 */
	public void playOneGame() {
		initNewgame();
		boolean endgame = false;
		while (fuseTokens > 0 && !endgame ) {
			switch (oneTurn()) {
			case 1:
				// Victory !
				view.displayEndGame();
				view.displayScore(getScore());
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
	 * Returns codes to indicate the state of the current game.
	 * Code : 
	 *  => 0 : Keeps the game running
	 *  => 1 : Victory of the players
	 *  => -1 : Defeat of the players
	 *  
	 *  @return Returns Integer 
	 */
	public int oneTurn() {
		for (Player p : players) {
			view.splashScreen(p);
			controller.waitForLineBreak();
			displayCardsOfAllPlayersBut(p);
			view.displayFireworkStatus(fireworkManager.getFireworkStatus());
			view.displayTokensRemaining(infoTokens, fuseTokens);
			view.displayDiscardedCards(cardManager.getDiscardedCards());
			p.playTurn();
			view.displayEndofTurn();
			controller.waitForLineBreak();
			if (instantVictoryState()) {
				return 1;
			}
			if (fuseTokens <= 0) {
				return -1;
			}
		}
		if (lastTurn) {
			// if last turn, variable is set to true
			return 1;
		} 
		if (cardManager.getCardsSize() <= 0) {
			lastTurn = true;
		}
		return 0;
	}

	/**
	 * Allows to start a game
	 */
	private void initNewgame() {
		lastTurn = false;
		fireworkManager = new FireworkManager();
		fireworkManager.initNewGame();
		cardManager.initNewgame(players);
	}

	/**
	 * Displays all the cards except the ones the player currently playing his turn has.
	 */
	private void displayCardsOfAllPlayersBut(Player playerNotToDisplay) {
		for (Player player : players) {
			if (player != playerNotToDisplay) {
				view.displayCardsOfPlayer(player);
			}
		}
	}

	/**
	 * Tests if all colors have been completed with cards. Returns true if it's the
	 * case.
	 *
	 * @return Returns a boolean
	 */
	private boolean instantVictoryState() {
		for (CardColor cardcolor : CardColor.values()) {
			// if any '5' card hasn't been played, instantVictoryState returns false
			if (fireworkManager.getFireworkStatus().get(cardcolor) != 5) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests if the card can be placed on the table.
	 *
	 * @return Returns a boolean
	 */
	private boolean canBePlaced(Card c) {
		int index = c.getNumber();
		CardColor color = c.getColor();
		Integer integer = fireworkManager.getFireworkStatus().get(color);
		if (integer == index - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests if the card can be placed on the table. If the card can be placed,
	 * modifies the HashMap with the correct color and changes the value.
	 * 
	 * Else, adds the card to the discarded cards and removes a life token.
	 * 
	 * @return Returns, if available, a new card picked from the stack, else returns
	 *         null
	 */
	public Tuple<Optional<Card>, Boolean> playCard(Card c) {
		Tuple<Optional<Card>, Boolean> tuple = new Tuple<Optional<Card>, Boolean>(null, null);
		if (canBePlaced(c)) {
			fireworkManager.getFireworkStatus().put(c.getColor(), c.getNumber());
			tuple.setY(true);
		} else {
			cardManager.getDiscardedCards().add(c);
			tuple.setY(false);
			fuseTokens--;
		}
		tuple.setX(cardManager.drawCard());
		return tuple;
	}

	/**
	 * Adds the card in the discarded cards arraylist
	 * 
	 * @return Returns, if available, a new card picked from the stack, else returns
	 *         null
	 */
	public Optional<Card> discardCard(Card c) {
		cardManager.getDiscardedCards().add(c);
		addInfoToken();
		return cardManager.drawCard();
	}

	/**
	 * If there's enough informations tokens, proceeds and returns true. Else return
	 * false.
	 *
	 * @return Returns a boolean
	 */
	public boolean giveInformationToPlayer(Player p, String msg) {
		if (infoTokens > 0) {
			p.addIntel(msg);
			infoTokens--;
			return true;
		}
		return false;
	}

	/**
	 * Add the players at the start of the game.
	 */
	private void addPlayers() {
		int i = 0;
		while (i < playerCount) {
			Player temp = new Player(this, interactionManager.getPlayerName(i), controller, view, interactionManager);
			if (!players.contains(temp)) {
				players.add(temp);
				i++;
			} else {
				view.printString("Le joueur existe déjà, veuillez choisir un autre nom");
			}
		}
	}

	/**
	 * @return Returns the number of information tokens. Returns an Integer.
	 */
	public int getInfoTokens() {
		return infoTokens;
	}

	/**
	 * @return Returns the array of players.
	 */
	public ArrayList<Player> getPlayerList() {
		return players;
	}

	/**
	 * Uses the fireworkStatus to calculate the score. Is used in the end of each
	 * game.
	 * 
	 * @return Returns the score.
	 */
	public int getScore() {
		int score = 0;
		int value;
		for (Entry<CardColor, Integer> entry : fireworkManager.getFireworkStatus().entrySet()) {
			value = entry.getValue();
			score += (value * value + 1) / 2;
		}
		return score;
	}
	
	/**
	 * Verifies that infoTokens stays under 8.
	 */
	private void addInfoToken() {
		if (infoTokens < 8 ) {
			infoTokens++;
		}
	}

}
