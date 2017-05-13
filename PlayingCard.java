package PJ4_PokerGame.PJ4;

import java.util.*;


//=================================================================================

/** class PlayingCardException: It is used for errors related to Card and Deck objects
 *  This is a checked exception! 
 */
class PlayingCardException extends Exception {

    /* Constructor to create a PlayingCardException object */
    PlayingCardException (){
		super ();
    }

    PlayingCardException ( String reason ){
		super ( reason );
    }
}


//=================================================================================

/** class Card : for creating playing card objects
 *  it is an immutable class.
 *  Rank - valid values are 1 to 13
 *  Suit - valid values are 1 to 4
 */
class Card {
    /* constant suits and ranks */
    static final String[] Suit = {"","Clubs", "Diamonds", "Hearts", "Spades" };
    static final String[] Rank = {"","A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    /* Data field of a card: rank and suit */
    private int cardRank;   /* values: 1-13 (see Rank[] above) */
    private int cardSuit;   /* values: 1-4  (see Suit[] above) */

    /* Constructor to create a card */
    /* throw PlayingCardException if rank or suit is invalid */
    public Card(int suit, int rank) throws PlayingCardException {
        if ((rank < 1) || (rank > 13))
            throw new PlayingCardException("Invalid rank:"+rank);
        else
            cardRank = rank;
        if ((suit < 1) || (suit > 4))
            throw new PlayingCardException("Invalid suit:"+suit);
        else
            cardSuit = suit;
    }

    /* Accessor and toString */
    public int getRank() { return cardRank; }
    public int getSuit() { return cardSuit; }
    public String toString() { return Rank[cardRank] + " " + Suit[cardSuit]; }

    
    /* Quick tests */
    public static void main(String args[]) {
        try {
            Card c1 = new Card(4,1);    // A Spades
            System.out.println(c1);
            c1 = new Card(1,10);        // 10 Clubs
            System.out.println(c1);
            c1 = new Card(5,10);        // generate exception here
        }
        catch (PlayingCardException e) {
            System.out.println("PlayingCardException: "+e.getMessage());
        }
    }
}


//=================================================================================

/** class Decks represents : n decks of 52 playing cards
 *  Use class Card to construct n * 52 playing cards!
 */

class Decks {

    /* this is used to keep track of original n*52 cards */
    private List<Card> originalDecks;

    /* this starts by copying all cards from originalDecks   */
    /* it holds remaining cards during games             */
    private List<Card> currentDecks;

    /* number of 52-card decks in this object */
    private int numOfDecks;

    /* number of cards in a deck */
    public static final int CARDS_PER_DECK = 52;


    /**
     * Constructor: Creates default one deck of 52 playing cards in originalDecks and
     *              copy them to currentDecks.
     *              initialize numOfDecks=1
     * Note: You need to catch PlayingCardException from Card constructor
     *       Use ArrayList for both originalDecks & currentDecks
     */
    public Decks()  {  ///exception!!!!offered by compilator
        numOfDecks = 1;
        originalDecks = new ArrayList<Card>();
        // implement this method!
        for(int i=1;i<=13;i++){
            for (int j=1;j<=4;j++){
                //Card n =new Card(i,j);
                try {
                    originalDecks.add(new Card(j,i));
                } catch (PlayingCardException e) {
                    e.printStackTrace();
                }
            }
        }
        currentDecks = new ArrayList<Card>(originalDecks);
    }


    /**
     * Constructor: Creates n 52-card decks of playing cards in
     *              originalDecks and copy them to currentDecks.
     *              initialize numOfDecks=n
     */
    public Decks(int n)  {
        numOfDecks = n;
        originalDecks = new ArrayList<Card>();
        // implement this method!

        for(int k = 0; k < n; k++){
            for(int i=1;i<=13;i++){
                for (int j=1;j<=4;j++){
                    //Card n =new Card(i,j);
                    try {
                        originalDecks.add(new Card(j,i));
                    } catch (PlayingCardException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        currentDecks = new ArrayList<Card>(originalDecks);
    }


    /**
     * Task: Shuffles cards in currentDecks.
     */
    public void shuffle()
    {
        Collections.shuffle(currentDecks);

    }


    /**
     * Task: Deals cards from the deal deck.
     *
     * @param numOfCards number of cards to deal
     * @return a list containing cards that were dealt
     * @throw PlayingCardException if numberCards > number of remaining cards
     *
     */
    public List<Card> deal(int numOfCards) throws PlayingCardException {
        List<Card> dealCard = new ArrayList<Card>();
        
        if (numOfCards > currentDecks.size()){
            throw new PlayingCardException("Not enough cards to deal");
        }
        for (int i = 0; i < numOfCards; i++){
            dealCard.add(currentDecks.remove(0));
        }
        return dealCard;
    }


    /**
     * Task: Resets playedDeck by copying all cards from the resetDeck.
     */
    public void reset()
    {
        currentDecks = new ArrayList<Card>(originalDecks);

    }


    /**
     * Task: Return number of remaining cards in deal deck.
     */
    public int numOfRemainingCards()
    {
	return currentDecks.size();
    }

    /**
     * Task: Returns a string representing cards in the deal deck 
     */
    public String toString()
    {
	return ""+ currentDecks;
    }


    /* Quick test                   */
    /*                              */
    /* Generate 2 decks of cards    */
    /* Loop 2 times:                */
    /*   Deal 30 cards for 4 times  */
    /*   Expect exception last time */
    /*   reset()                    */

    public static void main(String args[]) {

        System.out.println("*******    Create 2 decks of cards      *********\n\n");
        Decks decks  = new Decks(2);

        for (int j=0; j < 2; j++) {
            System.out.println("\n************************************************\n");
            System.out.println("Loop # " + j + "\n");
            System.out.println("Before shuffle:"+decks.numOfRemainingCards()+" cards");
            System.out.println("\n\t"+decks);
            System.out.println("\n==============================================\n");

            int numHands = 4;
            int cardsPerHand = 30;

            for (int i=0; i < numHands; i++) {
                decks.shuffle();
                System.out.println("After shuffle:"+decks.numOfRemainingCards()+" cards");
                System.out.println("\n\t"+decks);

                try {
                    System.out.println("\n\nHand "+i+":"+cardsPerHand+" cards");
                    System.out.println("\n\t"+decks.deal(cardsPerHand));
                    System.out.println("\n\nRemain:"+decks.numOfRemainingCards()+" cards");
                    System.out.println("\n\t"+decks);
                    System.out.println("\n==============================================\n");
                }
                catch (PlayingCardException e) {
                    System.out.println("*** In catch block:PlayingCardException:Error Msg: "+e.getMessage());
                }
            }

        decks.reset();
        }
    }

}
