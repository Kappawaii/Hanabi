package hanabi.model;

import java.util.ArrayList;
import java.util.Objects;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class Player {
	private GameModel game;
	private String name;
	private ArrayList<Card> cards;
	private TerminalController controller;
	private TerminalView view;
	private ArrayList<String> intelReceived;
	private InteractionManager interactionManager;

	public Player(GameModel game, String name, TerminalController controller, TerminalView view,
			InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
		this.game = Objects.requireNonNull(game);
		this.controller = Objects.requireNonNull(controller);
		this.view = Objects.requireNonNull(view);
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.cards = new ArrayList<Card>();
		intelReceived = new ArrayList<String>();
	}

	public void playTurn() {
		if (intelReceived.size() != 0) {
			displayInformation();
		}
		switch (interactionManager.getAction(name)) {
		case "information":
			if (game.getInfoTokens() < 1) {
				view.printString("Jetons d'informations �puis�s");
				playTurn();
			} else {
				giveInformation();
			}
			break;
		case "jeter":
			discardCard();
			break;
		case "jouer":
			playCard();
			break;
		}
	}

	public void addIntel(String msg) {
		intelReceived.add(msg);
	}

	private void giveInformation() {
		Player target = interactionManager.selectPlayer(this, game.getPlayerList());
		view.printString("Entrez le message � envoyer � " + target.name);
		game.giveInformationToPlayer(target, controller.getString());
	}

	private void discardCard() {
		Card c = interactionManager.selectCard("Choisissez la carte � d�fausser :\n", cards);
		if (cards.remove(c)) {
			game.discardCard(c);
		}
	}

	private void displayInformation() {
		StringBuilder strBuilder = new StringBuilder("Voici les informations dont vous disposez :\n");
		for (String s : intelReceived) {
			strBuilder.append(s).append("\n");
		}
		view.printString(strBuilder.toString());
	}

	private void playCard() {
		Card c = interactionManager.selectCard("Choisissez la carte à jouer :\n", cards);
		if ( cards.remove(c) ) {
			game.playCard(c);
		}
	}

	public String getName() {
		return name;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
