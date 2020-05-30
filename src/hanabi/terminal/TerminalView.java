package hanabi.terminal;

import java.io.PrintStream;
import java.util.Objects;

public class TerminalView {

	private PrintStream outStream;

	public TerminalView(PrintStream stream) {
		this.outStream = Objects.requireNonNull(stream);
	}

	public void SplashScreen() {
		outStream.println("****************************************");
		outStream.println("*****************HANABI*****************");
		outStream.println("****************************************");
	}

	public void printString(String str) {
		outStream.println(str);
	}
}
