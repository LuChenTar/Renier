package comp1140.ass2.gui;


import comp1140.ass2.Card;
import javafx.scene.image.ImageView;

public class MovableCard extends ImageView{

static final String CARD_BASE_URI = "/resources/images/";


    protected Card card;                 // the underlying game card that we represent
	protected Board board;                       // the board the card is linked to


	public MovableCard(Card card, Board board) {
		super(MovableCard.class.getResource(CARD_BASE_URI+card.toString().charAt(0) + card.toString().charAt(1)+".png").toString());
		this.card = card;
		this.board = board;
		
	}
}
