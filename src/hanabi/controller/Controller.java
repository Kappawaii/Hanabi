package hanabi.controller;

import java.util.function.Consumer;

public interface Controller {
	public Integer getInt();

	public String getString(Consumer<String> callback);

	public void waitForLineBreak();
}
