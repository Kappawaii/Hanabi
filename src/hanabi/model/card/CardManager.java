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

	/**
	 * Resets the class data to a "start of game" state
	 */
	public void initNewgame(ArrayList<Player> players) {
		discardedCards = new ArrayList<Card>();
		cards = generateNewCardSet();
		shuffleAndDistributeCards(cards, players);
	}

	/**
	 * Generates a new, sorted card set
	 * 
	 * @return the card set in a ArrayList<Card>
	 */
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

	/**
	 * Shuffles the ArrayList of cards given as argument, and distributes 5 cards to
	 * each player of the playerList given as argument
	 */
	private void shuffleAndDistributeCards(ArrayList<Card> cards, ArrayList<Player> playerList) {
		Collections.shuffle(cards);
		for (Player p : playerList) {
			for (int i = 0; i < 5; i++) {
				Card c = cards.remove(cards.size() - 1);
				p.getCards().add(c);
			}
		}
	}

	/**
	 * @return Returns, if available, a new card picked from the stack, else returns
	 *         null
	 */
	public Optional<Card> drawCard() {
		if (cards.size() > 0) {
			Optional<Card> newCard = Optional.of(cards.get(cards.size() - 1));
			cards.remove(cards.size() - 1);
			return newCard;
		} else {
			return Optional.empty();
		}
	}

	/**
	 * @return Returns the Array of discarded cards
	 */
	public ArrayList<Card> getDiscardedCards() {
		return discardedCards;
	}

	/**
	 * @return Returns the Array of cards in the stack
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * @return Returns the size of the stack
	 */
	public int getCardsSize() {
		return cards.size();
	}
}
