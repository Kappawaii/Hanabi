package hanabi.model;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class GameModel {

	private TerminalView view;
	private TerminalController controller;
	private InteractionManager interactionManager;

	private ArrayList<Player> players;
	private HashMap<CardColor, Integer> fireworkStatus;
	private ArrayList<Card> discardedCards;
	private int playerCount;

	private ArrayList<Card> cards;
	private int infoTokens;
	private int fuseTokens;

	public GameModel(InputStream inputStream, PrintStream printStream) {
		view = new TerminalView(Objects.requireNonNull(printStream));
		controller = new TerminalController(Objects.requireNonNull(inputStream));
		interactionManager = new InteractionManager(controller, view);
		players = new ArrayList<Player>();
		playerCount = interactionManager.getPlayerCount(2, 8);
		addPlayers();
		infoTokens = 8;
		fuseTokens = 3;
	}

	public void playOneGame() {
		initNewgame();
		while (fuseTokens > 0) {
			switch (oneTurn()) {
			case 1:
				// Victoire
				break;
			case -1:
				// Défaite
				break;
			case 0:
				break;
			}

		}
	}

	public int oneTurn() {
		for (Player p : players) {
			view.splashScreen(p);
			controller.waitForLineBreak();
			displayCardsOfAllPlayersBut(p);
			view.displayFireworkStatus(fireworkStatus);
			view.displayTokensRemaining();
			p.playTurn();
			if (instantVictoryState()) {
				return 1;
			}
			// TODO check defeat
			/*
			 * if (something) { return -1; }
			 */
		}
		return 0;
	}

	private void initNewgame() {
		fireworkStatus = new HashMap<CardColor, Integer>();
		for (CardColor color : CardColor.values()) {
			fireworkStatus.put(color, 0);
		}
		discardedCards = new ArrayList<Card>();
		cards = generateNewCardSet();
		System.out.println(cards.size() + "...........");
		shuffleAndDistributeCards(cards, players);
	}

	private void shuffleAndDistributeCards(ArrayList<Card> cards, ArrayList<Player> players) {
		Collections.shuffle(cards);
		for (Player p : players) {
			for (int i = 0; i < 5; i++) {
				Card c = cards.remove(cards.size() - 1);
				p.getCards().add(c);
			}
		}
	}

	private void displayCardsOfAllPlayersBut(Player playerNotToDisplay) {
		for (Player player : players) {
			if (player != playerNotToDisplay) {
				view.displayCardsOfPlayer(player);
			}
		}
	}

	private boolean instantVictoryState() {
		for (CardColor cardcolor : CardColor.values()) {
			// if any '5' card hasn't been played, instantVictoryState returns false
			if (fireworkStatus.get(cardcolor) != 5) {
				return false;
			}
		}
		return true;
	}

	public boolean playCard() {
		// TODO
		return false;
	}

	public void discardCard(Card c) {
		discardedCards.add(c);
		infoTokens++;
	}

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

	private ArrayList<Card> generateNewCardSet() {
		ArrayList<Card> deck = new ArrayList<Card>();

		for (CardColor c : CardColor.values()) {
			for (int i = 0; i < 3; i++) {
				deck.add(new Card(1, c));
			}
			for (int i = 0; i < 2; i++) {
				deck.add(new Card(2, c));
				deck.add(new Card(3, c));
				deck.add(new Card(4, c));
			}
			deck.add(new Card(5, c));
		}
		return deck;
	}

	public int getInfoTokens() {
		return infoTokens;
	}

	public ArrayList<Player> getPlayerList() {
		return players;
	}

}
