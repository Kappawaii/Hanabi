package hanabi.model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import hanabi.controller.GraphicalController;
import hanabi.controller.TerminalController;
import hanabi.model.card.Card;
import hanabi.utility.Tuple;
import hanabi.view.GraphicalView;
import hanabi.view.TerminalView;

public class Player {
	private GameModel game;
	private final String name;
	private ArrayList<Card> cards;
	private TerminalController controller;
	private TerminalView view;
	private ArrayList<String> intelReceived;
	private InteractionManager interactionManager;
	private GraphicalController GUIcontroller;
	private GraphicalView GUIview;
	
	/**
	 * Constructs the player, needs to be linked the game. Needs to be linked the
	 * terminals. The name cannot be changed.
	 */
	public Player(GameModel game, String name, TerminalController controller, TerminalView view, /*GraphicalController GUIcontroller,*/ GraphicalView GUIview ,
			InteractionManager interactionManager) {
		this.interactionManager = interactionManager;
		this.game = Objects.requireNonNull(game);
		this.controller = Objects.requireNonNull(controller);
		this.view = Objects.requireNonNull(view);
		
		/*this.GUIcontroller = */
		this.GUIview = GUIview;
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.cards = new ArrayList<Card>();
		intelReceived = new ArrayList<String>();
	}

	/**
	 * Functions that allows the player to start his turn and get a string used in
	 * order to get his choice.
	 */
	public void playTurn(int infoTokens) {
		if (intelReceived.size() != 0) {
			displayInformation();
		}
		switch (interactionManager.getAction(name)) {
		case "information":
			if (infoTokens < 1) {
				GUIview.displayDepletedInfoTokens();
				view.printString("Jetons d'informations Ã©puisÃ©s");
				playTurn(infoTokens);
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

	/**
	 * Adds the string with the previous informations.
	 */
	public void addIntel(String msg) {
		intelReceived.add(msg);
	}

	/**
	 * Selects another player to give an information to.
	 */
	private void giveInformation() {
		Player target = interactionManager.selectPlayer(this, game.getPlayerList());
		view.printString("Entrez le message à  envoyer à  " + target.name);
		game.giveInformationToPlayer(target, controller.getString());
	}

	/**
	 * Gets the card the player wants to use and will pick a new one, if available.
	 * Also takes a new card.
	 */
	private void playCard() {
		Card c = interactionManager.selectCardInOwnCards("Choisissez la carte Ã  jouer :" + System.lineSeparator(), cards);
		if (cards.remove(c)) {
			Tuple<Optional<Card>, Boolean> tuple = game.playCard(c);
			if (tuple.getX().isPresent()) {
				cards.add(tuple.getX().get());
			}
			if (tuple.getY()) {
				displaySuccess(c);
			} else {
				displayFail(c);
			}
		}
	}

	/**
	 * Gets the card the player wants to discard and removes it from the cards. Also
	 * takes a new card.
	 */
	private void discardCard() {
		Card c = interactionManager.selectCardInOwnCards("Choisissez la carte à défausser :" + System.lineSeparator(), cards);
		Optional<Card> newCard;
		if (cards.remove(c)) {
			newCard = game.discardCard(c);
			if (newCard.isPresent())
				cards.add(newCard.get());
		}
	}

	/**
	 * Can display the informations the player has received from the others.
	 */
	private void displayInformation() {
		StringBuilder strBuilder = new StringBuilder("Voici les informations dont vous disposez :" + System.lineSeparator() );
		for (String s : intelReceived) {
			strBuilder.append(s).append( System.lineSeparator() );
		}
		GUIview.displayIntel( strBuilder.toString() );
		view.printString( strBuilder.toString() );
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Can display a simple message if the card was successfully placed.
	 */
	private void displaySuccess(Card c) {
		GUIview.printString("REUSSITE ! : Carte " + c + " posée avec succès !");
		view.printString("REUSSITE ! : Carte " + c + " posée avec succès !");
	}

	/**
	 * Can display a simple message if the card was dropped in the discarded cards.
	 */
	private void displayFail(Card c) {
		GUIview.printString("Échec. Carte " + c + " part dans la défausse.");
		view.printString("Échec. Carte " + c + " part dans la défausse.");
	}

	/**
	 * @return Returns the name of the player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the cards in the hands of the player.
	 */
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
