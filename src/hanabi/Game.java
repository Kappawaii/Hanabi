package hanabi;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class Game {

	private ArrayList<Card> cards;
	private ArrayList<Player> players;
	private int infoTokens;
	private int fuseTokens;
	private int playerCount;

	TerminalView view;
	TerminalController controller;

	public Game(InputStream inputStream, PrintStream printStream) {
		view = new TerminalView(printStream);
		controller = new TerminalController(inputStream);

		cards = generateNewCardSet();
		Collections.shuffle(cards);
		players = new ArrayList<Player>();
		playerCount = getPlayerCount();
		for (int i = 0; i < playerCount; i++) {
			// TODO noms uniques de joueurs
			players.add(new Player(getPlayerName(i)));
		}
		infoTokens = 8;
		fuseTokens = 3;
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
		String name = null;
		while (name == null || name.isBlank()) {
			System.out.println("Entrez le nom du Joueur n°" + (playerNumber + 1));
			name = controller.getString();
		}
		return name;
	}

	private int getPlayerCount() {
		Integer numberOfPlayers = 0;
		int maxPlayers = 8;
		do {
			System.out.println("Entrez le nombre de joueurs (entre 0 et " + maxPlayers + ")");
			try {
				numberOfPlayers = controller.getInt();
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un nombre uniquement");
			}
		} while (numberOfPlayers == null || numberOfPlayers < 0 || numberOfPlayers > maxPlayers);
		return numberOfPlayers;
	}
}
