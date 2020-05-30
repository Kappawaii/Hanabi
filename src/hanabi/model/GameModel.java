package hanabi.model;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class GameModel {

	TerminalView view;
	TerminalController controller;

	private ArrayList<Player> players;
	private Dictionary<CardColor, Integer> cardsPlayed;
	private int playerCount;

	private ArrayList<Card> cards;
	private int infoTokens;
	private int fuseTokens;

	public GameModel(InputStream inputStream, PrintStream printStream) {
		view = new TerminalView(printStream);
		controller = new TerminalController(inputStream);
		players = new ArrayList<Player>();

		playerCount = getPlayerCount(2, 8);
		cards = generateNewCardSet();
		Collections.shuffle(cards);
		addPlayers();
		infoTokens = 8;
		fuseTokens = 3;
	}

	public void playOneGame() {
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
			p.playTurn();
			if (instantVictoryState()) {
				return 1;
			}
			// TODO check defeat (return -1)
		}
		return 0;
	}

	private boolean instantVictoryState() {
		for (CardColor cardcolor : CardColor.values()) {
			// if any '5' card hasn't been played, instantVictoryState returns false
			if (cardsPlayed.get(cardcolor) != 5) {
				return false;
			}
		}
		return true;
	}

	public boolean playCard() {
		return false;

	}

	public boolean giveInformationToPlayer(Player p, String msg) {
		if (infoTokens > 0) {
			p.addIntel(msg);
			infoTokens--;
			return true;
		}
		return false;
	}

	private Player getPlayerByName(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	protected Player selectPlayer() {
		view.printString("Tapez le nom du joueur");
		Predicate<String> p = (playerName) -> playerName == null || playerName.isBlank()
				|| getPlayerByName(playerName) == null;
		Supplier<String> s = () -> controller.getString();
		return getPlayerByName(doMethodWhilePredicateSatisfied(p, s,
				"Tapez le nom du joueur auquel vous voulez envoyer une information"));

	}

	private void addPlayers() {
		int i = 0;
		while (i < playerCount) {
			Player temp = new Player(this, getPlayerName(i), controller, view);
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

	private String getPlayerName(int playerNumber) {
		Predicate<String> p = (name) -> (name == null || name.isBlank());
		Supplier<String> s = () -> controller.getString();
		return doMethodWhilePredicateSatisfied(p, s, "Entrez le nom du Joueur n°" + (playerNumber + 1));
	}

	private int getPlayerCount(int minPlayers, int maxPlayers) {
		Predicate<Integer> p = (i) -> (i == null || i < minPlayers || i > maxPlayers);
		Supplier<Integer> s = () -> controller.getInt();
		return doMethodWhilePredicateSatisfied(p, s,
				"Entrez le nombre de joueurs (entre " + minPlayers + " et " + maxPlayers + ")");
	}

	/*
	 * Intended for user input validation
	 */
	protected <T> T doMethodWhilePredicateSatisfied(Predicate<T> predicate, Supplier<T> s, String message) {
		T temp;
		do {
			view.printString(message);
			temp = s.get();
		} while (predicate.test(temp));
		return temp;
	}

	public int getInfoTokens() {
		return infoTokens;
	}
}
