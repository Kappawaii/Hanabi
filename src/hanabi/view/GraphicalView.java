package hanabi.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.umlv.zen5.ApplicationContext;
import hanabi.model.Player;
import hanabi.model.card.Card;
import hanabi.model.card.CardColor;

public class GraphicalView implements View {
	private final int width;
	private final int height;
	private final int cardSize;
	private final int tokenRadius;
	private final int playerSpaceWidth;
	private final int playerSpaceHeight;
	private final int marginHorizontal;
	private final int marginVertical;

	private final Map<CardColor, Color> colors;
	private final Color backgroundColor;
	private final Font fontPrintString;
	private final Font font;

	private final ApplicationContext appContext;

	private GraphicalView(int width, int height, int cardWidth, int playerSpaceWidth,
			int playerSpaceHeight, ApplicationContext context) {
		this.width = width;
		this.height = height;
		this.cardSize = cardWidth;
		this.playerSpaceWidth = playerSpaceWidth;
		this.playerSpaceHeight = playerSpaceHeight;
		this.backgroundColor = Color.DARK_GRAY;
		this.tokenRadius = 30;
		this.marginHorizontal = 30;
		this.marginVertical = 30;
		this.appContext = context;
		this.fontPrintString = new Font("Arial", Font.BOLD, 28);
		this.font = new Font("Arial", Font.BOLD, 20);
		this.colors = new HashMap<CardColor, Color>();
		colors.put(CardColor.vert, Color.GREEN);
		colors.put(CardColor.rouge, Color.RED);
		colors.put(CardColor.bleu, Color.CYAN);
		colors.put(CardColor.blanc, Color.LIGHT_GRAY);
		colors.put(CardColor.jaune, Color.ORANGE);
		splashScreen();
	}

	/**
	 * 
	 */
	public static GraphicalView initGameGraphics( int width, int height,
			ApplicationContext context) {
		int cardSize = (int) (width * 1 / 20);
		int playerSpaceHeight = (int) (height * 8 / 5 / 4 / 3); 
		int playerSpaceWidth = (int) (width / 3);
		return new GraphicalView(width, height, cardSize, playerSpaceWidth, playerSpaceHeight,
				context);
	}

	/**
	 * 
	 */
	@Override
	public void splashScreen() {
		printString("Bienvenue sur Hanabi !" + System.lineSeparator() + " Choisissez le nombre de joueurs : "
				+ System.lineSeparator());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void printString(String str) {
		clearWindow();
		appContext.renderFrame(graphics -> {
			int y = height / 2;
			graphics.setFont(fontPrintString);
			graphics.setColor(Color.WHITE);
			
			drawLongText(graphics, str, (width / 3) + marginHorizontal , y );	
		});
	}

	/**
	 * 
	 */
	@Override
	public void splashScreen(Player p) {
		printString(
				"Au tour de : " + p.getName() + System.lineSeparator() + " Appuyez sur entrï¿½e pour jouer votre tour");
	}

	/**
	 * 
	 */
	@Override
	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus) {
		appContext.renderFrame(graphics -> {
			int x = (int) (width - 2 * marginHorizontal - cardSize), y = marginVertical;
			for (Entry<CardColor, Integer> entry : fireworkStatus.entrySet()) {
				y = marginVertical;
				for (int i = 1; i < entry.getValue(); i++) {
					drawCard(graphics, colors.get(entry.getKey()), entry.getValue(), x, y);
					y += (marginVertical / 2);
				}
				graphics.setColor(backgroundColor);
				graphics.fillRect(x, y, cardSize, cardSize);
				drawCard(graphics, colors.get(entry.getKey()), entry.getValue(), x, y);
				x -= (cardSize + marginHorizontal);
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayTokensRemaining(int infoTokens, int fuseTokens) {
		appContext.renderFrame(graphics -> {
			int x = (int) (2 * width / 3), y = (height * 12 / 20);
			for (int i = 1; i <= infoTokens; i++) {
				drawToken(graphics, x + (i * tokenRadius) + (tokenRadius / 2), y, Color.CYAN);
			}
			x = width;
			for (int i = 1; i <= fuseTokens; i++) {
				drawToken(graphics, x - (i * tokenRadius) - (tokenRadius / 2), y, Color.MAGENTA);
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayDiscardedCards(ArrayList<Card> discardedCards) {
		appContext.renderFrame(graphics -> {
			int x = (int) (width / 2), y = (int) (height * 14 / 20);
			drawDeck(graphics, discardedCards, x, y, true);
		});

	}

	/**
	 * 
	 */
	private void drawToken(Graphics2D graphics, int x, int y, Color color) {
		graphics.setColor(color);
		graphics.fillArc(x, y, tokenRadius, tokenRadius, 0, 360);
	}

	/**
	 * Displays the player's cards.
	 */
	@Override
	public void displayCardsOfPlayer(ArrayList<Player> players, Player playerNotToDisplay) {
		appContext.renderFrame(graphics -> {
			int x = marginHorizontal / 2, y = marginVertical;
			for (Player player : players) {
				if (!player.equals(playerNotToDisplay)) {
					graphics.setColor(Color.PINK);
					graphics.setFont(font);
					graphics.drawString(player.toString(), x + (playerSpaceWidth / 2), y);
					drawDeck(graphics, player.getCards(), x, y + (marginVertical / 2), true);
					y += playerSpaceHeight;
				}
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayOwnCards(ArrayList<Card> cards) {
		appContext.renderFrame(graphics -> {
			int x = playerSpaceWidth + marginHorizontal / 2, y = (int) (height * 4 / 5);
			y += marginVertical;
			graphics.setFont(font);
			graphics.setColor(Color.ORANGE);
			graphics.drawString(" Vous ", width / 2, y);
			drawDeck(graphics, cards, x, y + (marginVertical / 2), false);
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayEndGame() {}

	/**
	 * 
	 */
	@Override
	public void displayEndofTurn() {
		printString("FIN DE VOTRE TOUR");
	}

	/**
	 * 
	 */
	@Override
	public void displayScore(int score, String result) {
		StringBuilder stringBuilder = new StringBuilder("Fin de partie ! Votre score est de  ... " + score + " !");
		stringBuilder.append(result);
		printString(stringBuilder.toString());
	}

	/**
	 * 
	 */
	@Override
	public void displayDefeat() {
		StringBuilder stringBuilder = new StringBuilder("DEFAITE" + System.lineSeparator());
		stringBuilder.append("Vous avez perdu tout vos jetons rouge !");
		printString(stringBuilder.toString());
	}

	/**
	 * 
	 */
	private void clearWindow() {
		appContext.renderFrame(graphics -> {
			graphics.setColor(backgroundColor);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		});
	}

	/**
	 * 
	 */
	private void drawDeck(Graphics2D graphics, ArrayList<Card> cards, int x, int y, boolean showCards) {
		int i = marginHorizontal;
		int count = 1;
		for (Card card : cards) {
			if (showCards) {
				drawCard(graphics, colors.get(card.getColor()), card.getNumber(), x + i, y);
			} else {
				drawCard(graphics, Color.BLACK, count, x + i, y);
			}
			i += cardSize + marginHorizontal;
			count++;
		}
	}

	/**
	 * 
	 */
	private void drawCard(Graphics2D graphics, Color color, int numberCard, int x, int y) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(x, y, cardSize, cardSize);

		graphics.setColor(color);
		graphics.fillRect(x, y, cardSize, 10);
		graphics.fillRect(x + cardSize - 10, y, 10, cardSize);
		graphics.fillRect(x, y + cardSize - 10, cardSize, 10);
		graphics.fillRect(x, y, 10, cardSize);

		graphics.setFont(font);
		graphics.drawString(String.valueOf(numberCard), x + (cardSize / 2), y + (cardSize / 2));
	}

	/**
	 * 
	 */
	@Override
	public void prepareBoard() {
		clearWindow();
		appContext.renderFrame(graphics -> {
			graphics.setColor(Color.LIGHT_GRAY);

			/* Horizontal line */
			graphics.fillRect(0, 6 * height / 10, (width / 3) + (2*marginHorizontal), 3);
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayIntel(String string) {
		appContext.renderFrame(graphics -> {
			int y = (int) (6 * height / 10) + 2 * marginVertical;
			graphics.setColor(Color.WHITE);
			graphics.setFont(font);
			for (String newLine : string.split(System.lineSeparator())) {
				graphics.drawString(newLine, marginHorizontal, y += graphics.getFontMetrics().getHeight());
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void displayDepletedInfoTokens() {
		displayBottomRightCorner( " Vous avez épuisé vos jetons d'informations " );
	}

	/**
	 * 
	 */
	@Override
	public void refreshName(String str) {
		printString( "Nom du joueur : " + str + System.lineSeparator() + " Appuyez sur ESPACE pour valider votre choix");
	}
	
	/**
	 * 
	 */
	@Override
	public void displayChoices(String str) {
		displayBottomRightCorner(str);
	}
	
	/**
	 * 
	 */
	private void displayBottomRightCorner(String str) {
		appContext.renderFrame(graphics -> {
			graphics.setColor( backgroundColor );
			graphics.fillRect( width/3, 8 * height / 10, (width / 3), 2 * height / 10);

			graphics.setColor(Color.WHITE);
			graphics.setFont(font);
			drawLongText(graphics, str, width/3, 8 * height / 10 );	
		});
	}
	
	/**
	 * 
	 */
	private void drawLongText(Graphics2D graphics , String str, int x, int y) {
		for (String newLine : str.split( System.lineSeparator()) ) {
			graphics.drawString(newLine, x, y += graphics.getFontMetrics().getHeight());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void refreshStatusChoice(String str) {
		appContext.renderFrame(graphics -> {
			graphics.setColor(Color.WHITE);
			graphics.setFont(font);
			graphics.drawString(str, (width/3)+marginHorizontal, height - marginVertical);
		});
	}
}
