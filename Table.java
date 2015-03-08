package comp1140.ass2;

import comp1140.ass2.gui.Board;
import java.util.ArrayList;
import java.util.Random;

public class Table {

    protected ArrayList<Card> myCards = new ArrayList<Card>();
    protected ArrayList<Card> myHandH = new ArrayList<Card>();
    protected ArrayList<Card> myHandG = new ArrayList<Card>();
    protected ArrayList<Card> myDeck = new ArrayList<Card>();
    protected ArrayList<Card> myPile = new ArrayList<Card>();
    protected final int NumberOfCards = 52;
    protected int HumanPlayerHandSize = 7;
    protected int GodPlayerHandSize = 7;
    private int PileSize = NumberOfCards - HumanPlayerHandSize - GodPlayerHandSize - Board.deckSize;


    public Table(boolean shuffle){
        int c = 0;

        //for each suit
        for(int s = 0;s < 4; s++){
            //for each face number
            for(int n = 1; n<=13; n++) {
                Card new_card = new Card(Suit.values()[s], n);
                this.myCards.add(c, new_card);
                c++;
            }
        }
        //shuffle, if necessary
        if(shuffle) {
            this.shuffle();
        }
    }

    public void shuffle(){
        Random randomNumber = new Random();
        //for each card, pick another random card and swap them
        for (int first = 0; first < NumberOfCards; first++){
            int second = randomNumber.nextInt(NumberOfCards);
            Card temp = myCards.get(first);
            myCards.set(first, myCards.get(second));
            myCards.set(second, temp);
        }
        ArrayList<Card> H = new ArrayList<Card>(myCards.subList(0, HumanPlayerHandSize));
        ArrayList<Card> G = new ArrayList<Card>(myCards.subList(GodPlayerHandSize, HumanPlayerHandSize + GodPlayerHandSize));
        ArrayList<Card> E = new ArrayList<Card>(myCards.subList(HumanPlayerHandSize+GodPlayerHandSize, HumanPlayerHandSize + GodPlayerHandSize + PileSize));
        ArrayList<Card> K = new ArrayList<Card>(myCards.subList(HumanPlayerHandSize + GodPlayerHandSize + PileSize, NumberOfCards));
        this.myHandH = H;
        this.myHandG = G;
        this.myPile = E;
        this.myDeck = K;
    }

    // return cards
    public ArrayList<Card> getMyHandH() {
        return this.myHandH;
    }
    public ArrayList<Card> getMyHandG() {
        return this.myHandG;
    }
    public ArrayList<Card> getMyDeck() {
        return this.myDeck;
    }

    // return the pile
    public ArrayList<Card> getPile() {
        return this.myPile;
    }
}
