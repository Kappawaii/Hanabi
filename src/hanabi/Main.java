package hanabi;

import java.util.Scanner;

public class Main {

	private static final int maxPlayers = 8;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("****************************************");
		System.out.println("*****************HANABI*****************");
		System.out.println("****************************************");

		int numberOfPlayers = 0;
		do {
			System.out.println("Entrez le nombre de joueurs (entre 0 et " + maxPlayers + ")");
			try {
				numberOfPlayers = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un nombre uniquement");
			}
		} while (numberOfPlayers < 0 || numberOfPlayers > maxPlayers);
		Game g = new Game(sc, numberOfPlayers);

	}

}
