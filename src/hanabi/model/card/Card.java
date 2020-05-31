package hanabi.model.card;

import java.util.Objects;

public class Card {
	private final int number;
	private final CardColor color;

	public Card(int number, CardColor color) {
		if (number < 1 || number > 5) {
			throw new IllegalArgumentException("number must be between 1 and 5");
		}
		this.number = number;
		this.color = Objects.requireNonNull(color);
	}

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
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
