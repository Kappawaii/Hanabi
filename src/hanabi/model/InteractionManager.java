package hanabi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import hanabi.controller.Controller;
import hanabi.model.card.Card;
import hanabi.utility.UtilityFunctions;
import hanabi.view.View;

public class InteractionManager {
	Controller controller;
	View view;

	/**
	 *
	 */
	public InteractionManager(Controller controller, View view) {
		this.controller = Objects.requireNonNull(controller);
		this.view = Objects.requireNonNull(view);
	}

	/**
	 *
	 */
	public int getPlayerCount(int minPlayers, int maxPlayers) {
		Predicate<Integer> p = (i) -> (i == null || i < minPlayers || i > maxPlayers);
		Supplier<Integer> s = () -> controller.getInt();
		return doMethodWhilePredicateSatisfied(p, s,
				"Entrez le nombre de joueurs (entre " + minPlayers + " et " + maxPlayers + ")");
	}

	/**
	 *
	 */
	public String getPlayerName(int playerNumber, Consumer<String> callback) {
		Predicate<String> p = (name) -> (name == null || name.isBlank());

		Supplier<String> s = () -> controller.getString(callback);
		return doMethodWhilePredicateSatisfied(p, s, "Entrez le nom du Joueur :" + (playerNumber + 1));
	}

	/**
	 *
	 */
	String getAction(String name, Consumer<String> callback) {
		List<String> possibleActionList = Arrays.asList("information", "jeter", "jouer");
		Predicate<String> predicate = (
				action) -> (action == null || action.isBlank() || !possibleActionList.contains(action));
		Supplier<String> supplier = () -> controller.getString(callback);
		return doMethodWhilePredicateSatisfied(predicate, supplier,
				name + ", que voulez vous faire ?" + System.lineSeparator()
						+ "Tapez 'information' pour envoyer une information à un autre joueur" + System.lineSeparator()
						+ "Tapez 'jeter' pour vous défausser d'une carte et récupérer un jeton d'information"
						+ System.lineSeparator() + "Tapez 'jouer' pour jouer une carte" + System.lineSeparator());
	}

	/**
	 * Doesn't let the user select the Player given as parameter
	 */
	protected Player selectPlayer(Player playerNotToSelect, ArrayList<Player> players, Consumer<String> callback) {
		view.printString("Tapez le nom du joueur");
		Predicate<String> p = (playerName) -> playerName == null || playerName.isBlank()
				|| UtilityFunctions.searchForPlayerByName(playerName, players).equals(Optional.empty())
				|| playerNotToSelect.getName().equals(playerName);
		Supplier<String> s = () -> controller.getString(callback);
		String message = UtilityFunctions.listPlayerNamesExcept(players, playerNotToSelect.getName())
				+ "\nTapez le nom du joueur auquel vous voulez envoyer une information :";
		return UtilityFunctions.searchForPlayerByName(doMethodWhilePredicateSatisfied(p, s, message), players).get();
	}

	/**
	 * Selects a card in the deck of the player 
	 * 
	 * @return a Card
	 */
	Card selectCard(String basemsg, ArrayList<Card> cards) {
		Predicate<Integer> predicate = (input) -> (input == null || input < 0 || input > cards.size() - 1);
		Supplier<Integer> supplier = () -> controller.getInt() - 1;
		return cards.get(doMethodWhilePredicateSatisfied(predicate, supplier,
				basemsg /* + UtilityFunctions.cardListAsString(cards) */));
	}

	/**
	 * 
	 */
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
			view.displayChoices(message);
			temp = s.get();
		} while (temp != null && predicate.test(temp));
		return temp;
	}
}
