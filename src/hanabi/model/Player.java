package hanabi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class Player {
	private GameModel game;
	private String name;
	private ArrayList<Card> cards;
	private TerminalController controller;
	private TerminalView view;
	private ArrayList<String> intelReceived;

	public Player(GameModel game, String name, TerminalController controller, TerminalView view) {
		this.game = game;
		this.controller = controller;
		this.view = view;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.cards = new ArrayList<Card>();
		intelReceived = new ArrayList<String>();
	}

	public void playTurn() {
		switch (getAction()) {
		case "information":
			if (game.getInfoTokens() < 1) {
				view.printString("Jetons d'informations épuisés");
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

	private String getAction() {
		List<String> possibleActionList = Arrays.asList("information", "jeter", "jouer");
		Predicate<String> predicate = (
				action) -> (action == null || action.isBlank() || !possibleActionList.contains(action));
		Supplier<String> supplier = () -> controller.getString();
		return game.doMethodWhilePredicateSatisfied(predicate, supplier,
				name + ", que voulez vous faire ?\n"
						+ "Tapez 'information' pour envoyer une information à un autre joueur\n"
						+ "Tapez 'jeter' pour vous défausser d'une carte et récupérer un jeton d'information\n"
						+ "Tapez 'jouer' pour jouer une carte\n");
	}

	public void addIntel(String msg) {
		intelReceived.add(msg);
	}

	private Card selectCard() {
		Predicate<Integer> predicate = (input) -> (input == null || input < 0 || input > cards.size() - 1);
		Supplier<Integer> supplier = () -> controller.getInt() - 1;
		return cards.get(game.doMethodWhilePredicateSatisfied(predicate, supplier, cardListAsString()));
	}

	private void giveInformation() {
		Player target = game.selectPlayer();
		view.printString("Entrez le message à envoyer à " + target.name);
		game.giveInformationToPlayer(target, controller.getString());
	}

	private boolean discardCard() {
		// TODO

		return cards.remove(null);
	}

	private void playCard() {
		// TODO
	}

	private String cardListAsString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			stringBuilder.append(i + 1).append(" : ").append(cards.get(i - 1));
		}
		return stringBuilder.toString();
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
