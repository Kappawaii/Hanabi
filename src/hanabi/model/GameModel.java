package hanabi.model;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Optional;

import hanabi.model.card.Card;
import hanabi.model.card.CardColor;
import hanabi.model.card.CardManager;
import hanabi.utility.Tuple;

public class GameModel {

	private FireworkManager fireworkManager;
	private CardManager cardManager;
	private ArrayList<Player> players;

	private int infoTokens;
	private int fuseTokens;

	/**
	 * Constructs the game model, needs an input and a output stream.
	 */
	public GameModel() {
		players = new ArrayList<Player>();
		infoTokens = 8;
		fuseTokens = 3;
		cardManager = new CardManager();
	}

	/**
	 * Allows to start a game
	 */
	public void initNewgame() {
		fireworkManager = new FireworkManager();
		fireworkManager.initNewGame();
		cardManager.initNewgame(players);
	}

	/**
	 * Tests if all colors have been completed with cards. Returns true if it's the
	 * case.
	 *
	 * @return Returns a boolean
	 */
	public boolean instantVictoryState() {
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
		if (infoTokens < 8) {
			infoTokens++;
		}
	}

	public FireworkManager getFireworkManager() {
		return fireworkManager;
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getFuseTokens() {
		return fuseTokens;
	}

}
