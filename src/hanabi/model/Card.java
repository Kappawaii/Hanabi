package hanabi.model;

public class Card {
	private int number;
	private CardColor color;
	private boolean revealedForPlayer;
	// score ?

	public Card(int number, CardColor color) {
		if (number < 1 || number > 5) {
			throw new IllegalStateException("number must be between 1 and 5");
		}
		this.number = number;
		this.color = color;
		revealedForPlayer = false;
	}

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
	}

	public boolean getRevealed() {
		return revealedForPlayer;
	}

	public void setRevealed() {
		revealedForPlayer = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (color != other.color)
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return number + " " + color.toString();
	}

}
