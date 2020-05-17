package hanabi;

public class Card {
	private int number;
	private CardColor color;
	// score ? 
	
	public Card(int number, CardColor color ) {
		if ( number < 1 || number > 5) {
			throw new IllegalStateException("number must be between 1 and 5");
		}
		this.number = number;
		this.color = color;
	}

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
	}

}
