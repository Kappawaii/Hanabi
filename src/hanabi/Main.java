package hanabi;

import java.awt.Color;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

public class Main {

	static void startGraphical(ApplicationContext context) {
		Hanabi hanabi = new Hanabi(context);
		hanabi.play();
	}

	static void startTerminal() {
		Hanabi hanabi = new Hanabi(System.in, System.out);
		hanabi.play();
	}

	public static void main(String[] args) {
		/*
		 * Hanabi hanabi = new Hanabi(System.in, System.out); hanabi.play();
		 */
		if (args.length == 1) {

			String input = args[0];
			if (input.equals("g")) {
				Application.run(Color.LIGHT_GRAY, Main::startGraphical);
			} else if (input.equals("t")) {
				startTerminal();
			} else {
				splashOut();
			}
		} else {
			splashOut();
		}
	}

	static void splashOut() {
		System.out.println("Usage : java -jar hanabi.jar <argument>");
		System.out.println("Entrez g en argument pour l'affichage graphique, t pour l'affichage terminal");
	}

}
