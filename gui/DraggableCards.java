package comp1140.ass2.gui;

import comp1140.ass2.Card;
import javafx.scene.image.ImageView;

public class DraggableCards extends ImageView {
	
	
	static final String CARD_BASE_URI = "/resources/images/";
	
	private double mousex, mousey;     // keep track of the coordinate of cards
    protected Card card;             // the underlying game card that we represent
	protected Board board;                 // the board the card is linked to


	public DraggableCards(Card card, Board board) {
		super(DraggableCards.class.getResource(CARD_BASE_URI+card.toString()+".png").toString());
		this.card = card;
		this.board = board;
		
		this.setOnMousePressed(event -> { 
			mousex = event.getSceneX();
			mousey = event.getSceneY();
			toFront();
			event.consume();
		});

		/* this event tells us for sure we're being dragged */
		this.setOnMouseDragged(event -> { 
			/* move card by same amount as the mouse */
			setLayoutX(getLayoutX() + event.getSceneX() - mousex);
			setLayoutY(getLayoutY() + event.getSceneY() - mousey);
			mousex = event.getSceneX();
			mousey = event.getSceneY();
			/* show the player which square is nearest to where we are */
			board.highlightNearestSquare(this);
			event.consume();
		});
	
		this.setOnMouseReleased(event -> {
            if (board.myHandH.contains(this.card)) {
                board.attemptToMove_run(this);
                board.attemptToMove_pile(this);
                board.attemptToMove_deck(this);
            }else if (board.myPile.contains(this.card)) {
                board.attemptToMove_run((this));
                board.attemptToMove_pile(this);
            }else if (board.myDeck.contains(this.card)) {
                board.attemptToMove_deck(this);
            }else if(board.humanRun.contains(this.card)) {
                board.attemptToMove_run((this));
            }
				
			event.consume();
		});
		
	}

}
