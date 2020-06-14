package hanabi.controller;

import java.util.Objects;
import java.util.function.Consumer;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class GraphicalController implements Controller {

	private ApplicationContext context;

	private final static char charReference = 'A' - 1;

	/**
	 * Controller for the Zen5 graphical interface.
	 * 
	 * @param context
	 */
	public GraphicalController(ApplicationContext context) {
		this.context = Objects.requireNonNull(context);
	}

	/**
	 * Get int from char to int conversion (A = 1, B = 2, Z = 26)
	 * 
	 * @return Returns an Integer
	 */
	@Override
	public Integer getInt() {
		while (true) {
			String input;
			if ((input = getString()).length() == 1) {
				return (input.charAt(0) - charReference);
			}
		}
	}

	/**
	 * Is used in order to catch characters from the input.
	 * 
	 * @return Returns a Character
	 */
	private Character getChar() {
		while (true) {
			String input;
			if ((input = getString()).length() == 1) {
				return (char) (input.charAt(0));
			} else {
				switch (input) {
				case "SPACE":
					return ' ';
				default:
					break;
				}
			}
		}
	}

	/**
	 * Uses the getChar() method in order to create a String.
	 * Is used for the graphical version and get names or instructions.
	 * 
	 * @return Returns a String
	 */
	@Override
	public String getString(Consumer<String> callback) {

		StringBuilder stringBuilder = new StringBuilder();
		char newChar;
		while ((newChar = getChar()) != ' ') {
			stringBuilder.append(newChar);
			if (callback != null) {
				callback.accept(stringBuilder.toString());
			}
		}
		return stringBuilder.toString().toLowerCase();
	}

	/**
	 * Allows the program to wait for the player to press space in order to continue
	 */
	@Override
	public void waitForLineBreak() {
		while (getKeyEvent().getKey().equals(KeyboardKey.SPACE)) {
		}
	}

	/**
	 * Is used to make sure a key is pressed to catch this event.
	 * @return Returns an Event
	 */
	private Event getKeyEvent() {
		Event e = null;
		while (e == null) {
			Event tempEvent = context.pollOrWaitEvent(1000);
			if (tempEvent != null && tempEvent.getAction() != null
					&& tempEvent.getAction().equals(Action.KEY_PRESSED)) {
				e = tempEvent;
			}
		}
		return e;
	}

	/**
	 * Used to catch characters.
	 * 
	 * @return a String
	 */
	private String getString() {
		return getKeyEvent().getKey().toString();
	}

}
