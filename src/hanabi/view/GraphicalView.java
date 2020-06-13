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
		splashScreen();
	}
	
	public static GraphicalView initGameGraphics(int xOrigin, int yOrigin, int width, int height,  ApplicationContext context) {
		int cardSize = (int) ( width * 1 / 20 );
		int playerSpaceHeight = (int) ( height * 4 / 5 / 4 ); // TODO : change this
		int playerSpaceWidth = (int) ( width / 3 ); // TODO : change this.
		return new GraphicalView(xOrigin, yOrigin, width, height, cardSize, playerSpaceWidth , playerSpaceHeight, context);
	}
	
	public void splashScreen() {
		printString( "Bienvenue sur Hanabi !" );
	}
	
	@Override
	public void printString(String str) {
		clearWindow();
		appContext.renderFrame( graphics -> {
			graphics.drawString(str, width/2, height/2);
		});
	}
	
	public void splashScreen( Player p ) {
		printString( "Au tour de : " + p.getName() + "\n Appuyez sur entrée pour jouer votre tour" );
	}

	public void displayFireworkStatus(HashMap<CardColor, Integer> fireworkStatus) {
		appContext.renderFrame( graphics -> {
			for (Entry<CardColor, Integer> entry : fireworkStatus.entrySet()) {
				// entry.getValue()
				// entry.getKey().toString()
				int x=(int) (width/3) + 45, y=15;
				for( int i=1; i <= entry.getValue();i++ ) {
					drawCard(graphics, entry.getKey() , entry.getValue() , x, y);
					y += 15; 
				}
				x += cardSize + 30;
			}
		});
	}

	public void displayTokensRemaining(int infoTokens, int fuseTokens) {
		appContext.renderFrame( graphics -> {
			int x = (int) (width/2) ,y = (height*12/20);
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
			int x=(int) (width/2), y=(int) ( height * 14 / 20 );
			graphics.setColor(Color.BLACK);
			drawDeck(graphics, discardedCards, x, y, true);
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
	public void displayCardsOfPlayer(ArrayList<Player> players , Player playerNotToDisplay) {
		appContext.renderFrame( graphics -> {
			int x= 15, y= 15 ;
			for(Player player : players) {
				if( !player.equals(playerNotToDisplay)) {
					graphics.setColor(Color.RED);
					graphics.drawString( player.toString(), x + (playerSpaceWidth/2), y );
					graphics.setColor(Color.RED);
					drawDeck(graphics, player.getCards(), x, y+15, true);
					y += playerSpaceHeight;
				}
			}
		}); 
	}
	
	@Override
	public void displayOwnCards(ArrayList<Card> cards) {
		appContext.renderFrame( graphics -> {
			int x=15, y=(int) ( height * 4/5 );
			graphics.setColor(Color.BLACK);
			y += 30; 
			graphics.setColor(Color.GREEN);
			graphics.drawString( " Vous " , x + (playerSpaceWidth/2), y );
			drawDeck(graphics, cards, x, y+15, false);
		}); 
	}

	@Override
	public void displayEndGame() {
		clearWindow();
		printString("Fin de partie ! Votre score ... ");
	}

	@Override
	public void displayEndofTurn() {
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
	
	private void drawDeck( Graphics2D graphics, ArrayList<Card> cards , int x, int y, boolean showCards ) {
		int i = 30;
		int count=1 ;
		for( Card card : cards) {
			graphics.setColor(Color.RED);
			if( showCards ) {
				graphics.setColor(Color.RED);
				drawCard( graphics, card.getColor(), card.getNumber(), x + i, y );
			} else {
				graphics.setColor(Color.BLACK);
				graphics.drawRect( x+i, (4*height/5)+y, cardSize, cardSize);
				graphics.drawString( String.valueOf(count) , x+i, y+(cardSize/2) );
			}
			i += cardSize+30; 
			count++;
		}
	}
	
	private void drawCard(Graphics2D graphics, CardColor color, int numberCard, int x, int y ) {
		graphics.setColor( Color.BLACK );

		graphics.fillRect( x, y, cardSize, 10);
		graphics.fillRect( x + cardSize - 10, y, 10, cardSize );
		graphics.fillRect( x, y + cardSize - 10, cardSize , 10);
		graphics.fillRect( x, y, 10, cardSize );
		
		graphics.drawString( String.valueOf( numberCard ), x + (cardSize/2) , y + (cardSize/2));
	}

	public void prepareBoard() {
		clearWindow();
		appContext.renderFrame( graphics -> {
			graphics.setColor(Color.DARK_GRAY);
			graphics.fillRect(0 , (4*height/5), width, 6);
			graphics.fillRect(width/3 + 30, 0, 6, 4*height/5 );
		});
	}

	public void displayIntel(String string) {
		
	}

	public void displayDepletedInfoTokens() {
		// TODO Auto-generated method stub
		
	}
	
}
