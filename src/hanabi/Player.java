package hanabi;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Player {
	private String name;
	private Scanner sc;
	private ArrayList<Card> cards;

	public Player(String name, Scanner sc) {
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.cards = new ArrayList<Card>();
	}

	public Card playCard(Card card) {
		// TODO
		return card;
	}

	public String getName() {
		return name;
	}

}
