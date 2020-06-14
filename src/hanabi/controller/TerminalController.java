package hanabi.controller;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class TerminalController implements Controller {

	@SuppressWarnings("unused")
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
			return -1;
		}
	}

	/*
	 * Returns a string from the user input
	 */
	public String getString(Consumer<String> callback) {
		return scanner.nextLine();
	}

	/*
	 * Returns when line break is scanned on inStream
	 * 
	 * Doesn't return any value
	 */
	public void waitForLineBreak() {
		scanner.nextLine();
	}

}
