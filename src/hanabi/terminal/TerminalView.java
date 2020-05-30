package hanabi.terminal;

import java.io.PrintStream;
import java.util.Objects;

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
		for (int i = 0; i < 100; i++) {
			strBuilder.append("******************MONTER C'EST TRICHER******************\n");
		}
		for (int i = 0; i < 100; i++) {
			strBuilder.append("********************************************************\n");
		}
		printString(strBuilder.toString());
		printString("C'est le tour de " + p.getName() + " !\nAppuyez sur Entrée pour commencer votre tour");
	}
}
