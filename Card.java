package comp1140.ass2;

public class Card {
    private Suit mySuit;
    private int myNumber;

    public Card(Suit aSuit, int aNumber) {
        this.myNumber = aNumber;
        this.mySuit = aSuit;
    }

    public int getMyNumber() {
        return myNumber;
    }

    public String getMySuit() {
        return mySuit.toString();
    }

    public String toString() {
        String numStr = "";
        switch (this.myNumber) {
            case 1:
                numStr = "A";
                break;
            case 2:
                numStr = "2";
                break;
            case 3:
                numStr = "3";
                break;
            case 4:
                numStr = "4";
                break;
            case 5:
                numStr = "5";
                break;
            case 6:
                numStr = "6";
                break;
            case 7:
                numStr = "7";
                break;
            case 8:
                numStr = "8";
                break;
            case 9:
                numStr = "9";
                break;
            case 10:
                numStr = "T";
                break;
            case 11:
                numStr = "J";
                break;
            case 12:
                numStr = "Q";
                break;
            case 13:
                numStr = "K";
                break;
        }
        return numStr + mySuit.toString().substring(0,1);
    }

}
