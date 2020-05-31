package hanabi.model;

import java.util.HashMap;

import hanabi.model.card.CardColor;

public class FireworkManager {

	private HashMap<CardColor, Integer> fireworkStatus;

	/**
	 * Builds the Firework Manager.
	 * No parameters needed.
	 */
	public FireworkManager() {
		fireworkStatus = new HashMap<CardColor, Integer>();
	}

	/**
	 * Initiates a new game from scratch.
	 */
	public void initNewGame() {
		fireworkStatus.clear();
		for (CardColor color : CardColor.values()) {
			fireworkStatus.put(color, 0);
		}
	}

	/**
	 * @return Returns the HashMap using a color and an integer.
	 */
	public HashMap<CardColor, Integer> getFireworkStatus() {
		return fireworkStatus;
	}
}
