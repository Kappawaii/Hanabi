package hanabi.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
	private final int tokenRadius;
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
		this.tokenRadius = 30;
		this.appContext = context;
	}
	
	public static GraphicalView initGameGraphics(int xOrigin, int yOrigin, int width, int height,  ApplicationContext context) {
		int cardSize = (int) ( width * 1 / 15 );
		int playerSpaceHeight = (int) ( width / 2 ); // TODO : change this
		int playerSpaceWidth = (int) ( height * 2 / 5 ); // TODO : change this.
		return new GraphicalView(xOrigin, yOrigin, width, height, cardSize, playerSpaceWidth , playerSpaceHeight, context);
	}
	
	public void splashScreen() {
		clearWindow();
		printString( "Bienvenue sur Hanabi !" );
	}
	
	@Override
	public void printString(String str) {
		appContext.renderFrame( graphics -> {
			graphics.drawString(str, width/2, height/2);
		});
	}
	
	public void splashScreen( Player p ) {
		clearWindow();
		printString( "Au tour de : " + p.getName() + " \n Appuyez sur entrée pour jouer votre tour" );
	}

	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus) {
		int x=0, y=0;
		appContext.renderFrame( graphics -> {
			for (Entry<CardColor, Integer> entry : fireworkStatus.entrySet()) {
				// entry.getValue()
				// entry.getKey().toString()
				
				drawCard(graphics, entry.getKey() , entry.getValue() , x, y);
			}
		});
	}

	public void displayTokensRemaining(int infoTokens, int fuseTokens) {
		appContext.renderFrame( graphics -> {
			int x = (width/2) ,y = (3*height/5);
			for(int i=1; i <= infoTokens; i++ ) {
				drawToken(graphics, x+ (i * tokenRadius) + ( tokenRadius/2 ), y, Color.BLUE);
			}
			x = width;
			for(int i=1; i <= fuseTokens; i++ ) {
				drawToken(graphics, x- (i * tokenRadius) - (tokenRadius/2), y, Color.RED);
			}
		});
	}

	public void displayDiscardedCards(ArrayList<Card> discardedCards) {
		appContext.renderFrame( graphics -> {
			int x=width/2, y=(int) ( height * 2 / 5 );
			graphics.setColor(Color.BLACK);
			
			
		});
	
	}
	
	private void drawToken( Graphics2D graphics, int x, int y , Color color) {
		graphics.setColor(color);
		graphics.fillArc( x, y, tokenRadius , tokenRadius , 0 , 360 );
	}

	/**
	 * Displays the player's cards.
	 */
	@Override
	public void displayCardsOfPlayer(ArrayList<Player> players) {
		clearWindow();
		appContext.renderFrame( graphics -> {
			int x=0,y=0;
			graphics.setColor(Color.RED);
			for(Player player : players) {
				graphics.setColor(Color.RED);
				graphics.drawString( player.toString(), x, y );
				drawDeck(graphics, player, x, y+30, true);
				y += playerSpaceHeight;
			}
			
		}); 
	}
	
	@Override
	public void displayOwnCards(ArrayList<Card> cards) {
		appContext.renderFrame( graphics -> {
			int x=0,y=0;
			graphics.setColor(Color.BLACK);
			
		});
	}

	@Override
	public void displayEndGame() {
		clearWindow();
		printString("Fin de partie ! Votre score ... ");
	}

	@Override
	public void displayEndofTurn() {
		clearWindow();
		printString("FIN DE VOTRE TOUR");
	}

	@Override
	public void displayScore(int score , String result) {
		StringBuilder stringBuilder = new StringBuilder(score + " !");
		stringBuilder.append(result);
		printString(stringBuilder.toString());
	}

	@Override
	public void displayDefeat() {
		StringBuilder stringBuilder = new StringBuilder("DEFAITE\n");
		stringBuilder.append("Vous avez perdu tout vos jetons rouge !");
		printString(stringBuilder.toString());
	}
	
	private void clearWindow() {
		appContext.renderFrame( graphics -> {
			graphics.setColor(backgroundColor);
			graphics.fill(new Rectangle2D.Float(0, 0, width, height) );
		});
	}
	
	private void drawDeck( Graphics2D graphics, Player player , int x, int y, boolean showCards ) {
		int i = 30;
		int count=0 ;
		for( Card card : player.getCards()) {
			if( showCards ) {
				drawCard( graphics, card.getColor(), card.getNumber(), x + i, y );
			} else {
				graphics.drawRect( x+i, (4*height/5)+y, cardSize, cardSize);
				graphics.drawString( String.valueOf(count) , (width/3)+x+i, y+(cardSize/2) );
			}
			i += cardSize+30; 
			count++;
		}
	}
	
	private void drawCard(Graphics2D graphics, CardColor color, int numberCard, int x, int y ) {
		graphics.setColor( Color.BLACK );

		graphics.drawRect( x, y, cardSize, 10);
		graphics.drawRect( x + cardSize, y, 10, cardSize );
		graphics.drawRect( x, y + cardSize, cardSize , 10);
		graphics.drawRect( x, y, 10, cardSize );
		
		graphics.drawString( String.valueOf( numberCard ), x, y );
	}

	private RectangularShape drawSquare(int x, int y) {
		return new Rectangle2D.Float( x, y, cardSize, cardSize );
	}
	
}
