package hanabi.terminal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import hanabi.model.Card;
import hanabi.model.CardColor;
import hanabi.model.Player;

public class TerminalView {

	private PrintStream outStream;

	public TerminalView(PrintStream stream) {
		this.outStream = Objects.requireNonNull(stream);
		splashScreen();
	}

	/*
	 * Prints the splash screen on the PrintStream given at construct time
	 */
	public void splashScreen() {
		outStream.println("****************************************");
		outStream.println("*****************HANABI*****************");
		outStream.println("****************************************");
	}

	/*
	 * Prints a string with a line separator on the PrintStream given at construct
	 * time
	 */
	public void printString(String str) {
		outStream.println(str);
	}

	/*
	 * Makes a splash screen intented to be displayed just before the player's turn
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

	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus2) {
		StringBuilder stringBuilder = new StringBuilder("État du feu d'artifice :");
		for (Entry<CardColor, Integer> entry : fireworkStatus2.entrySet()) {
			stringBuilder.append("\nCouleur ").append(entry.getKey().toString()).append(" : ").append(entry.getValue());
		}
		stringBuilder.append("\n");
		printString(stringBuilder.toString());
	}

	public void displayCardsOfPlayer(Player player) {
		StringBuilder stringBuilder = new StringBuilder("Cartes de " + player.getName() + " :\n");
		for (Card c : player.getCards()) {
			stringBuilder.append(c.toString()).append(" ; ");
		}
		stringBuilder.append("\n");
		printString(stringBuilder.toString());
	}

	public void displayTokensRemaining(int informationTokens, int fuseTokens) {
		printString("Jetons restants : \nJetons d'information : " + informationTokens + "\nJetons d'erreur : "
				+ fuseTokens + " \n");
	}

	public void displayOwnCards(ArrayList<Card> cards) {
		StringBuilder stringBuilder = new StringBuilder("Vos cartes :\n");
		for (Card c : cards) {
			stringBuilder.append("* ");
		}
		for (int i = 0; i < cards.size(); i++) {
			stringBuilder.append(i + " ");
		}
		stringBuilder.append("\n\n");
		printString(stringBuilder.toString());

	}
}
