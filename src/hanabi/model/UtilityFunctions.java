package hanabi.model;

import java.util.ArrayList;

public class UtilityFunctions {

	static String cardListAsString(ArrayList<Card> cards) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			stringBuilder.append("Carte ").append(i + 1).append(" : ").append(cards.get(i)).append("\n");
		}
		return stringBuilder.toString();
	}

	static Player getPlayerByName(String name, ArrayList<Player> players) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	static String listPlayerNamesExcept(ArrayList<Player> players, String nameNotToInclude) {
		StringBuilder stringBuilder = new StringBuilder("Joueurs :\n");
		for (Player p : players) {
			if (!p.getName().equals(nameNotToInclude)) {
				stringBuilder.append(p.getName()).append("\n");
			}
		}
		return stringBuilder.toString();
	}
}
