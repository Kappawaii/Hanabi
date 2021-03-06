package hanabi.view;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import hanabi.model.Player;
import hanabi.model.card.Card;
import hanabi.model.card.CardColor;

public class TerminalView implements View {

	private PrintStream outStream;

	/**
	 * Builds a terminal view. Needs an output stream.
	 */

	public TerminalView(PrintStream stream) {
		this.outStream = Objects.requireNonNull(stream);
		splashScreen();
	}

	/**
	 * Prints the splash screen on the PrintStream given at construct time
	 */
	public void splashScreen() {
		outStream.println("****************************************");
		outStream.println("*****************HANABI*****************");
		outStream.println("****************************************");
	}

	/**
	 * Prints a string with a line separator on the PrintStream given at construct
	 * time
	 */
	public void printString(String str) {
		outStream.println(str);
	}

	/**
	 * Makes a splash screen intended to be displayed just before the player's turn
	 */
	public void splashScreen(Player p) {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			strBuilder.append("MONTER C'EST TRICHER****************MONTER C'EST TRICHER\n");
			strBuilder.append("******************MONTER C'EST TRICHER******************\n");
		}
		for (int i = 0; i < 100; i++) {
			strBuilder.append("********************************************************\n");
		}
		printString(strBuilder.toString());
		printString("C'est le tour de " + p.getName() + " !\nAppuyez sur Entrée pour commencer votre tour");
	}

	/**
	 * Displays the firework status.
	 */
	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus2) {
		StringBuilder stringBuilder = new StringBuilder("État du feu d'artifice :");
		for (Entry<CardColor, Integer> entry : fireworkStatus2.entrySet()) {
			stringBuilder.append("\nCouleur ").append(entry.getKey().toString()).append(" : ").append(entry.getValue());
		}
		stringBuilder.append("\n");
		printString(stringBuilder.toString());
	}

	/**
	 * Displays the player's cards.
	 */
	public void displayCardsOfPlayer(ArrayList<Player> players, Player playerNotToDisplay) {
		StringBuilder stringBuilder = new StringBuilder();

		for (Player player : players) {
			if (!player.equals(playerNotToDisplay)) {
				stringBuilder.append("Cartes de " + player.getName() + " :\n");
				for (Card c : player.getCards()) {
					stringBuilder.append(c.toString()).append(" ; ");
				}
				stringBuilder.append("\n");
			}
		}
		printString(stringBuilder.toString());
	}

	/**
	 * Displays the players own cards (hidden).
	 */
	public void displayOwnCards(ArrayList<Card> cards) {
		StringBuilder stringBuilder = new StringBuilder("Vos cartes :\n");
		for (@SuppressWarnings("unused")
		Card c : cards) {
			stringBuilder.append("* ");
		}
		stringBuilder.append("\n");
		for (int i = 0; i < cards.size(); i++) {
			stringBuilder.append((i + 1) + " ");
		}
		stringBuilder.append("\n\n");
		printString(stringBuilder.toString());
	}

	/**
	 * Displays the remaining tokens on a single line in the console.
	 * 
	 * Doesn't return any value.
	 */
	public void displayTokensRemaining(int infoTokens, int fuseTokens) {
		StringBuilder stringBuilder = new StringBuilder("-------- Jetons Bleus : ");
		for (int i = 0; i < infoTokens; i++)
			stringBuilder.append('O');
		stringBuilder.append(" || Jetons Rouges : ");
		for (int i = 0; i < fuseTokens; i++)
			stringBuilder.append('O');
		stringBuilder.append(" -------- \n");
		printString(stringBuilder.toString());
	}

	/**
	 * Displays the discarded cards from the first to the last.
	 */
	public void displayDiscardedCards(ArrayList<Card> discardedCards) {
		StringBuilder stringBuilder = new StringBuilder("-- Défausse : ");
		for (int i = 0; i < discardedCards.size(); i++)
			stringBuilder.append(discardedCards.get(i)).append(" - ");
		stringBuilder.append(" -- \n");
		printString(stringBuilder.toString());
	}

	/**
	 * Splash screen for the end of the game
	 */
	public void displayEndGame() {
		outStream.println("****************** FIN DE PARTIE ******************\n");
	}

	/**
	 * Splash screen for the end of the current turn.
	 */
	public void displayEndofTurn() {
		printString("Fin du tour, appuyez sur entrée pour passer au prochain joueur");
	}

	/**
	 * Displays the end game score.
	 */
	public void displayScore(int score, String result) {
		StringBuilder stringBuilder = new StringBuilder(
				"******************\n****Score final : " + score + "****\n******************\n\n");
		stringBuilder.append("       ####### Qualité de la prestation ####### \n       ");
		stringBuilder.append(result);
		printString(stringBuilder.toString());
	}

	/**
	 * Displays a defeat screen
	 */
	public void displayDefeat() {
		outStream.println("********************** DEFAITE **********************\n");
		outStream.println("****** Vous avez perdu tout vos jetons rouges ******\n");
	}

	public void displayIntel(String str) {
		printString(str);
	}

	public void displayDepletedInfoTokens() {
		printString("Jetons d'informations épuisés");
	}

	public void prepareBoard() {
	}

	public void refreshName(String str) {
	}

	public void displayChoices(String str) {
		printString(str);
	}

	public void refreshStatusChoice(String str) {
	}

}
