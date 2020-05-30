package hanabi.terminal;

import java.io.PrintStream;
import java.util.Objects;

public class TerminalView {

	private PrintStream outStream;

	public TerminalView(PrintStream stream) {
		this.outStream = Objects.requireNonNull(stream);
	}

	/*
	 * Prints the splash screen on the PrintStream given at construct time
	 */
	public void SplashScreen() {
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
}
