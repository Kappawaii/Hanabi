package hanabi.terminal;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;

public class TerminalController {

	private InputStream inStream;
	private Scanner scanner;

	public TerminalController(InputStream stream) {
		scanner = new Scanner(Objects.requireNonNull(stream));
	}

	/*
	 * Tries to return an integer from the user input
	 * 
	 * Returns null if integer not found
	 */
	public Integer getInt() {
		try {
			return Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/*
	 * Returns a string from the user input
	 */
	public String getString() {
		return scanner.nextLine();
	}

}
