package hanabi.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.HashMap;

import fr.umlv.zen5.ApplicationContext;
import hanabi.model.GameModel;
import hanabi.model.Player;
import hanabi.model.card.Card;
import hanabi.model.card.CardColor;

public class GraphicalView implements View {
	private final int xOrigin;
	private final int yOrigin;
	private final int width;
	private final int height;
	private final int cardSize;
	private final int playerSpaceWidth;
	private final int playerSpaceHeight;
	private final Color backgroundColor;
	private final ApplicationContext appContext;
	
		
	private GraphicalView( int xOrigin, int yOrigin, int width, int height, int cardWidth, int playerSpaceWidth, int playerSpaceHeight , ApplicationContext context ) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.width = width;
		this.height = height;
		this.cardSize = cardWidth;
		this.playerSpaceWidth = playerSpaceWidth;
		this.playerSpaceHeight = playerSpaceHeight;
		this.backgroundColor = Color.LIGHT_GRAY;
		this.appContext = context;
	}
	
	public static GraphicalView initGameGraphics(int xOrigin, int yOrigin, int width, int height,  ApplicationContext context) {
		int cardSize = (int) ( width * 1 / 15 );
		int playerSpaceHeight = (int) ( width / 2 ); // TODO : change this
		int playerSpaceWidth = (int) ( height * 2 / 5 ); // TODO : change this.
		return new GraphicalView(xOrigin, yOrigin, width, height, cardSize, playerSpaceWidth , playerSpaceHeight, context);
	}
	
	
	/**
	 * Draws the game board from its data, using an existing Graphics2D object.
	 * @param graphics a Graphics2D object provided by the default method {@code draw(ApplicationContext, GameData)}
	 * @param data the GameData containing the game data.
	 */
	void draw(Graphics2D graphics, GameModel gamemodel , Player player) {
		// example
		/*
		 
		graphics.setColor(Color.YELLOW);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width, height));
		
		graphics.clearRect(0, height / 4 * 3, width / 2, height);
		*/
		
		/* DRAW PLAYER */
		ArrayList<Player> arrayPlayers = gamemodel.getAllPlayersBut(player);

		/*
		for (int i = 0; i < arrayPlayers.size(); i++) {
			drawOtherPlayerSpace( graphics, arrayPlayers.get(i), (i%2 == 0) ? 0 : width/2, (i < 2) ? 0 : playerHeight );
		}
		*/
	
		drawOtherPlayerSpace( graphics, arrayPlayers );
		
		// TODO : Draw firework
		
		graphics.setColor(Color.CYAN);
		graphics.fill(new Rectangle2D.Float(0, height * 4/10, width/2, height * 3/10));
		
		// TODO : Draw défausse
		
		/*displayDiscardedCards*/
		graphics.setColor(Color.RED); 
		graphics.fill(new Rectangle2D.Float(width/2, height * 4/10, width/2, height * 3/10));
		
		// TODO : Draw player choices
		
		/* drawPlayerSpace(); */
		
		graphics.setColor(Color.BLACK); 
		graphics.fill(new Rectangle2D.Float(0, height * 7/10, width, height * 3/10));
	}
	
	/**
	 * Draws the game board from its data, using an existing {@code ApplicationContext}.
	 * @param context the {@code ApplicationContext} of the game
	 * @param data the GameData containing the game data.
	 * @param view the GameView on which to draw.
	 */
	public void draw( GameModel gamemodel, Player player) {
		appContext.renderFrame( graphics -> this.draw(graphics, gamemodel, player) ); // do not modify
	}
	
	/**
	 * Draw the space for a one player
	 * @param graphics
	 * @param gamemodel
	 */
	private void drawOtherPlayerSpace( Graphics2D graphics, ArrayList<Player> players ) {
		int x=0,y=0;
		graphics.setColor(Color.RED);
		
		for(Player player : players) {
			graphics.drawString(player.getName(), x, y);
			drawDeck(graphics, player, x, y, true);
			y = ( x == playerSpaceWidth ) ? 0 : playerSpaceHeight;
			x = ( x == playerSpaceWidth ) ? 0 : playerSpaceWidth;
		}
		
	}
	
	private void drawDeck( Graphics2D graphics, Player player , int x, int y, boolean showCards ) {
		int i=0;
		for( Card card : player.getCards()) {
			/*graphics.fill( drawSquare(x + cardSize*i + 10, y) );*/
			graphics.setColor( Color.WHITE );
			graphics.drawLine( (cardSize*i) + 30, y, (cardSize*(i+1)), y);
			graphics.drawLine( (cardSize*(i+1)), y, (cardSize*(i+1)), y+cardSize );
			graphics.drawLine( (cardSize*(i+1)), y+cardSize,(cardSize*i) + 30, y+cardSize);
			graphics.drawLine( (cardSize*i) + 30, y+cardSize, (cardSize*i) + 30, y );
			
			graphics.drawString( String.valueOf( card.getNumber() ), x, y );
			/*graphics.fill( drawSquare(x + cardSize*i + 10, y) );*/
		}
	}
	
	private RectangularShape drawSquare(int x, int y) {
		return new Rectangle2D.Float( x, y, cardSize, cardSize );
	}
	
	
	
	/**
	 * Draw the space for a one player
	 * @param graphics
	 * @param gamemodel
	 */
	private void drawPlayerSpace( Graphics2D graphics, Player player , int x, int y ) {
		
	}
	
	
	public void splashScreen() {
		clearWindow();
		appContext.renderFrame( graphics -> {
			graphics.drawString( "Votre tour est terminé ! \n Appuyez sur entrée", width/2, height/2);
		});
	}
	
	public void splashScreen( Player p ) {
		clearWindow();
		appContext.renderFrame( graphics -> {
			graphics.drawString( "Au tour de : " + p.getName() + " \n Appuyez sur entrée pour jouer votre tour" , width/2, height/2);
		});
	}
	
	public void clearWindow() {
		appContext.renderFrame( graphics -> {
			graphics.setColor(backgroundColor);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height) );
		});
	}

	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus) {
		// TODO Auto-generated method stub
		
	}

	public void displayTokensRemaining(int infoTokens, int fuseTokens) {
		// TODO Auto-generated method stub
		
	}

	public void displayDiscardedCards(ArrayList<Card> discardedCards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printString(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayCardsOfPlayer(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayOwnCards(ArrayList<Card> cards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayEndGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayEndofTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayScore(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayDefeat() {
		// TODO Auto-generated method stub
		
	}
	
}
