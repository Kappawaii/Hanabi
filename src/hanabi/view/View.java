package hanabi.view;

import java.util.ArrayList;
import java.util.HashMap;

import hanabi.model.Player;
import hanabi.model.card.Card;
import hanabi.model.card.CardColor;

public interface View {
	public void splashScreen();

	public void printString(String str);

	public void splashScreen(Player p);

	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus2);

	public void displayCardsOfPlayer(ArrayList<Player> players);

	public void displayOwnCards(ArrayList<Card> cards);

	public void displayTokensRemaining(int infoTokens, int fuseTokens);

	public void displayDiscardedCards(ArrayList<Card> discardedCards);

	public void displayEndGame();

	public void displayEndofTurn();

	public void displayScore(int score, String result);

	public void displayDefeat();
}
