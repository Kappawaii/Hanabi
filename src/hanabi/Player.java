package hanabi;

import java.util.ArrayList;
import java.util.Objects;

public class Player {
	private String name;
	private ArrayList<Card> cards;

	public Player(String name) {
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
