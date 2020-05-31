package hanabi.model;

import java.util.HashMap;

import hanabi.model.card.CardColor;

public class FireworkManager {

	private HashMap<CardColor, Integer> fireworkStatus;

	public FireworkManager() {
		fireworkStatus = new HashMap<CardColor, Integer>();
	}

	public void initNewGame() {
		for (CardColor color : CardColor.values()) {
			fireworkStatus.put(color, 0);
		}
	}

	public HashMap<CardColor, Integer> getFireworkStatus() {
		return fireworkStatus;
	}
}
