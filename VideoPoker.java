package PJ4_PokerGame.PJ4;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks,
 *    Queens, Kings, or Aces. Lower pairs do not pay out.
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus
 *    a set of two cards of the same denomination.
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit,
 *    starting from 10 and ending with an ace
 *
 */


/* This is the main poker game class. */


public class VideoPoker {

    // default constant values
    private static final int STARTING_BALANCE = 100;
    private static final int NUMBER_OF_CARDS = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,9,25,50,250};
    private static final String[] goodHandTypes={
            "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush",
            "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

   
    private static final Decks oneDeck = new Decks(1);

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /** default constructor, set balance = STARTING_BALANCE */
    public VideoPoker()
    {
	this(STARTING_BALANCE);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table          Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i=size-1; i >= 0; i--) {
            System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /** Check current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {

        boolean isFlushResult = isFlush();
        int handtype = 0;
           Integer lowKey= (Integer)getFrequency().firstKey();
           Integer highKey= (Integer)getFrequency().lastKey();
        if (isFlushResult&&((lowKey == 1 && highKey==13)||(highKey-lowKey==4)) ) {
            if (lowKey == 1 && highKey==13)
                handtype = 8; //Royal Flush
            else if(highKey-lowKey==4){
                handtype = 7; //Straight Flush
            }
        } else if (getFrequency().containsValue(4)) {
            handtype = 6;//Four of a Kind
        } else if ((lowKey<11)&&(highKey>10)&&(getFrequency().size()==2)) {
            handtype = 5;//Full House
        } else if (isFlushResult) {
            handtype = 4;//Flush
        } else if (highKey-lowKey==4&&getFrequency().size()==5) {
            handtype = 3;//Straight
        } else if (getFrequency().containsValue(3)) {
            handtype = 2;//Three of a Kind
        } else if (getFrequency().size()==3) {
            handtype = 1;//Two Pairs
        } else if ((Integer)getFrequency().get(highKey)==2 && (highKey>10||highKey==1)) {
            handtype = 0;//Royal Pair
        } else {
            handtype = -1;//Lost
        }

        switch (handtype) {
            case -1:
                System.out.println("Sorry, you lost!");
                break;
            default:
                int bonus = multipliers[handtype];
                System.out.println(goodHandTypes[handtype]+'!');
                this.playerBalance += (this.playerBet * bonus);
                break;
        }
    }//end checkHands


 
    private TreeMap getFrequency (){
        TreeMap<Integer,Integer> hand = new TreeMap<Integer,Integer>();
        for (int i=0; i<NUMBER_OF_CARDS; i++)  {
            Integer rank = playerHand.get(i).getRank();
            Integer frequency = hand.get(rank);
            if (frequency == null)
            { // add new word to table
               hand.put(  rank,new Integer (1));
            }             else             {
                // increment count of existing word; replace wordTable entry
                frequency++;
                hand.put (rank, frequency);
            }// end if
         }// end for
        return hand;
    }// end getFrequency

    private boolean isFlush() {

    for (int i = 0; i < NUMBER_OF_CARDS-1; i++) {
      if (playerHand.get(i).getSuit() != playerHand.get(i + 1).getSuit()) {
        return false;
      }
    }
        return true;
    }


    private void placeBet(){                      //ask player bet
        System.out.println("Enter Bet:");
        Scanner bet = new Scanner(System.in);
        do {
            this.playerBet = bet.nextInt();
        }while (!(this.playerBet>0)|| !(this.playerBet<=this.playerBalance));
        this.playerBalance -=this.playerBet; // update balance
    }
    private boolean retry(){
        boolean newGame = false;
        Scanner choiceScan = new Scanner(System.in);
        String choice = "n";

        if (playerBalance <= 0) {
            System.out.printf("Your balance is %d\n Bye!\n", playerBalance);
            newGame = false;
            return newGame;
        }

        System.out.printf("Your balance:$%d, one more game (y or n)?\n", playerBalance);
        Scanner playAgain = new Scanner(System.in);
        if (playAgain.hasNext() && (playAgain.nextLine().equalsIgnoreCase("y"))) {
            System.out.printf("Want to see payout table? (y or n) \n" );
            Scanner showTable = new Scanner(System.in);
            if (showTable.hasNext() && (showTable.nextLine().equalsIgnoreCase("y"))) {showPayoutTable();}
            oneDeck.reset();
            newGame = true;
        }
        else {
            System.out.println("Bye");
            return false;
        }
        return newGame;
    }
    private void updateCards(){
        List<Card> tempHand = new ArrayList<Card>();
        ArrayList<Integer> discardArray  = new ArrayList<Integer>();
        Scanner in = new Scanner(System.in);
        String str1=null;

        int count=1;

        while (count>0){
            count=0;
            System.out.println("Enter positions of cards to keep (e.g. 1 4 5): ");

            str1 = in.nextLine();

            if (!str1.isEmpty()){

                String str2[] = str1.split(" ");

                // check if user entered more than 5 cards
                if (str2.length>5){
                    System.out.println("Error, too many values. Choose up to 5 cards only. ");
                    count++;
                }

                for (int i=0; i < str2.length; i++) {
                    int k = Integer.parseInt(str2[i]);
                    discardArray.add(i, k);
                }
                //Check if each card number is between 1 and 5
                for (int i=0; i < discardArray.size(); i++) {

                    if (discardArray.get(i)<1 || discardArray.get(i)>=6){
                        System.out.println("Error, position number out of range. Choose 1 - 5");
                        count++;
                    }
                }

                if(count==0){
                    //extract held cards, add them into new array
                    for (int i=0; i<discardArray.size(); i++){
                        int x =discardArray.get(i);
                        tempHand.add(playerHand.get(x-1));
                    }

                    System.out.println("Held Cards: "+tempHand);

                    //deal more cards
                    try {
                        tempHand.addAll(oneDeck.deal(5-discardArray.size()));
                    } catch (PlayingCardException e) {
                        e.printStackTrace();
                    }
                }//end if(count==0)
                discardArray.clear();
            }//end if(str1.is not Empty())
        }// end while (count>0)

        if (str1.isEmpty()){
            tempHand.clear();
            System.out.println("No cards held.");
            try { tempHand.addAll(oneDeck.deal(5));
            }
            catch (PlayingCardException e) {
                e.printStackTrace();
            }

        }//end if (str1.is Empty())

        playerHand = tempHand;
        displayHand();
    }
    private void displayHand() {
        int currentSize = playerHand.size();
        for (int k = 0; k < currentSize; k++) {
            System.out.print(playerHand.get(k).toString()+"  ");
        }
    }
    public void play()
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     *    +  showPayoutTable()
     *
     *      ++
     *    +  show balance, get bet
     *    + verify bet value, update balance
     *    +  reset deck, shuffle deck,
     *    +  deal cards and display cards
     *    +  ask for positions of cards to keep
     *    +      get positions in one input line
     *    +  update cards
     *      check hands, display proper messages
     *      update balance if there is a payout
     *      if balance = O:
     *          end of program
     *      else
     *          ask if the player wants to play a new game
     *          if the answer is "no" : end of program
     *          else : showPayoutTable() if user wants to see it
     *          goto ++
     */
            showPayoutTable();
            boolean playAgain = true;
        while (playAgain){
            System.out.println("Balance:"+playerBalance);
            placeBet();
            oneDeck.reset();
            oneDeck.shuffle();
            try {
                playerHand = oneDeck.deal(5);
            }catch (PlayingCardException e){ System.out.println("Exception dealing a new hand" + e.getMessage());
            }
            displayHand();
            updateCards();
            checkHands();
            playAgain = retry();
        }

    }




    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    private void testCheckHands() {
        try {
            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(4,1));
            playerHand.add(new Card(4,10));
            playerHand.add(new Card(4,12));
            playerHand.add(new Card(4,11));
            playerHand.add(new Card(4,13));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0,new Card(4,9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight
            playerHand.set(4, new Card(2,8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Flush
            playerHand.set(4, new Card(4,5));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");


            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(4,8));
            playerHand.add(new Card(1,8));
            playerHand.add(new Card(4,12));
            playerHand.add(new Card(2,8));
            playerHand.add(new Card(3,8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(4,11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(2,11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(2,9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Royal Pair
            playerHand.set(0, new Card(2,3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // non Royal Pair
            playerHand.set(2, new Card(4,3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /* Run testCheckHands() */
    public static void main(String args[]) {
        VideoPoker pokergame = new VideoPoker();
        pokergame.testCheckHands();
    }
}
