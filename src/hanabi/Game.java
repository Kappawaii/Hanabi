package hanabi;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

	Scanner sc;
	private ArrayList<Card> cards;
	private ArrayList<Player> players;
	private int infoTokens;
	private int fuseTokens;

	public Game(Scanner sc, int playerCount) {
		this.sc = sc;
		cards = generateNewDeck();
		players = new ArrayList<Player>();
		for (int i = 0; i < playerCount; i++) {
			players.add(new Player(getPlayerName(i), sc));
		}
		infoTokens = 8;
		fuseTokens = 3;
	}

	private ArrayList<Card> generateNewDeck() {
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
			System.out.print("");
		}
		return deck;
	}

	private String getPlayerName(int playerNumber) {
		String name = null;
		while (name == null || name.isBlank()) {
			System.out.println("Entrez le nom du Joueur n°" + (playerNumber + 1));
			name = sc.nextLine();
		}
		return name;
	}
}
