package hanabi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hanabi.terminal.TerminalController;
import hanabi.terminal.TerminalView;

public class InteractionManager {
	TerminalController controller;
	TerminalView view;

	public InteractionManager(TerminalController controller, TerminalView view) {
		this.controller = Objects.requireNonNull(controller);
		this.view = Objects.requireNonNull(view);
	}

	public int getPlayerCount(int minPlayers, int maxPlayers) {
		Predicate<Integer> p = (i) -> (i == null || i < minPlayers || i > maxPlayers);
		Supplier<Integer> s = () -> controller.getInt();
		return doMethodWhilePredicateSatisfied(p, s,
				"Entrez le nombre de joueurs (entre " + minPlayers + " et " + maxPlayers + ")");
	}

	public String getPlayerName(int playerNumber) {
		Predicate<String> p = (name) -> (name == null || name.isBlank());
		Supplier<String> s = () -> controller.getString();
		return doMethodWhilePredicateSatisfied(p, s, "Entrez le nom du Joueur :" + (playerNumber + 1));
	}

	String getAction(String name) {
		List<String> possibleActionList = Arrays.asList("information", "jeter", "jouer");
		Predicate<String> predicate = (
				action) -> (action == null || action.isBlank() || !possibleActionList.contains(action));
		Supplier<String> supplier = () -> controller.getString();
		return doMethodWhilePredicateSatisfied(predicate, supplier,
				name + ", que voulez vous faire ?\n"
						+ "Tapez 'information' pour envoyer une information à un autre joueur\n"
						+ "Tapez 'jeter' pour vous défausser d'une carte et récupérer un jeton d'information\n"
						+ "Tapez 'jouer' pour jouer une carte\n");
	}

	/*
	 * Doesn't let the user select the Player given as parameter
	 */
	protected Player selectPlayer(Player playerNotToSelect, ArrayList<Player> players) {
		view.printString("Tapez le nom du joueur");
		Predicate<String> p = (playerName) -> playerName == null || playerName.isBlank()
				|| UtilityFunctions.getPlayerByName(playerName, players) == null
				|| playerNotToSelect.getName().equals(playerName);
		Supplier<String> s = () -> controller.getString();
		String message = UtilityFunctions.listPlayerNamesExcept(players, playerNotToSelect.getName())
				+ "\nTapez le nom du joueur auquel vous voulez envoyer une information :";
		return UtilityFunctions.getPlayerByName(doMethodWhilePredicateSatisfied(p, s, message), players);
	}

	Card selectCard(String basemsg, ArrayList<Card> cards) {
		Predicate<Integer> predicate = (input) -> (input == null || input < 0 || input > cards.size() - 1);
		Supplier<Integer> supplier = () -> controller.getInt() - 1;
		return cards.get(doMethodWhilePredicateSatisfied(predicate, supplier,
				basemsg + UtilityFunctions.cardListAsString(cards)));
	}

	Card selectCardInOwnCards(String basemsg, ArrayList<Card> cards) {
		view.displayOwnCards(cards);
		return selectCard(basemsg, cards);
	}

	/*
	 * Intended for user input validation
	 */
	private <T> T doMethodWhilePredicateSatisfied(Predicate<T> predicate, Supplier<T> s, String message) {
		T temp;
		do {
			view.printString(message);
			temp = s.get();
		} while (predicate.test(temp));
		return temp;
	}
}
