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

	/**
	 * 
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
	 * 
	 */
	public void playOneGame() {
		initNewgame();
		while (fuseTokens > 0) {
			switch (oneTurn()) {
			case 1:
				// Victory !
				view.displayEndGame();
				view.displayScore( getScore() );
				break;
			case -1:
				// Defeat
				view.displayEndGame();
				view.displayScore( getScore() );
				break;
			case 0:
				break;
			}

		}
	}

	/**
	 * 
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

			/*
			 * 3 façons de stop le tour ( la partie en fait ) : 
			 * - des 5 partout 
			 * - plus de
			 * vies 
			 * - pioche vide
			 */
			if ( instantVictoryState() ) {
				return 1;
			}
			if ( cardManager.getCardsSize() <= 0 ) {
				// TODO : Last turn 
				return 1;
			}
			if (fuseTokens <= 0)
				return -1;
			
		}
		return 0;
	}

	/**
	 * 
	 */
	private void initNewgame() {
		fireworkManager = new FireworkManager();
		fireworkManager.initNewGame();
		cardManager.initNewgame(players);
	}

	/**
	 * 
	 */
	private void displayCardsOfAllPlayersBut(Player playerNotToDisplay) {
		for (Player player : players) {
			if (player != playerNotToDisplay) {
				view.displayCardsOfPlayer(player);
			}
		}
	}

	/**
	 * Tests if all colors have been completed with cards. 
	 * Returns true if it's the case.
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
		Integer integer = fireworkManager.getFireworkStatus().get( color );
		if (integer == index - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Tests if the card can be placed on the table. 
	 * If the card can be placed, modifies the HashMap with the correct color and changes the value.
	 * 
	 * Else, adds the card to the discarded cards and removes a life token.
	 * 
	 * @return Returns, if available, a new card picked from the stack, else returns null
	 */
	public Optional<Card> playCard(Card c) {
		if (canBePlaced(c)) {
			fireworkManager.getFireworkStatus().put(c.getColor(), c.getNumber());
		} else {
			cardManager.getDiscardedCards().add(c);
			fuseTokens--;
		}

		return cardManager.drawCard();
	}

	/**
	 * Adds the card in the discarded cards arraylist
	 * 
	 * @return Returns, if available, a new card picked from the stack, else returns null
	 */
	public Optional<Card> discardCard(Card c) {
		cardManager.getDiscardedCards().add(c);
		infoTokens++;
		
		return cardManager.drawCard();
	}

	/**
	 * If there's enough informations tokens, proceeds and returns true. Else return false.
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

}
