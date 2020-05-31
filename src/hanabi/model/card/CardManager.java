package hanabi.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import hanabi.model.Player;

public class CardManager {

	private ArrayList<Card> discardedCards;
	private ArrayList<Card> cards;

	public CardManager() {

	}

	public void initNewgame(ArrayList<Player> players) {
		discardedCards = new ArrayList<Card>();
		cards = generateNewCardSet();
		shuffleAndDistributeCards(cards, players);
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

	private void shuffleAndDistributeCards(ArrayList<Card> cards, ArrayList<Player> players) {
		Collections.shuffle(cards);
		for (Player p : players) {
			for (int i = 0; i < 5; i++) {
				Card c = cards.remove(cards.size() - 1);
				p.getCards().add(c);
			}
		}
	}

	public Optional<Card> drawCard() {
		if (cards.size() > 0) {
			return Optional.of(cards.get(cards.size() - 1));
		} else {
			return Optional.empty();
		}
	}

	public ArrayList<Card> getDiscardedCards() {
		return discardedCards;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
}
