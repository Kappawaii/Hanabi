package hanabi;

import java.util.ArrayList;
import java.util.Optional;

import hanabi.model.Player;
import hanabi.model.card.Card;

public class UtilityFunctions {

	/**
	 * Returns a display-friendly String containing
	 */
	public static String cardListAsString(ArrayList<Card> cards) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			stringBuilder.append("Carte ").append(i + 1).append(" : ").append(cards.get(i)).append("\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * Searches for the player by name and returns an Optional, containing a Player
	 * if found, and empty if a Player wasn't found
	 */
	public static Optional<Player> searchForPlayerByName(String name, ArrayList<Player> players) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return Optional.of(p);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns a display-friendly String containing all the players names, excluding
	 * the playerName given as argument
	 */
	public static String listPlayerNamesExcept(ArrayList<Player> players, String nameNotToInclude) {
		StringBuilder stringBuilder = new StringBuilder("Joueurs :\n");
		for (Player p : players) {
			if (!p.getName().equals(nameNotToInclude)) {
				stringBuilder.append(p.getName()).append("\n");
			}
		}
		return stringBuilder.toString();
	}
}
