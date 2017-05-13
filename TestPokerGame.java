/*************************************************************************************
 *
 *  This program is used to test PJ4.VideoPoker class
 * 
 *  PJ4 class allows user to run program as follows:
 *
 *    	java PJ4		// default credit is $100
 *  or 	java PJ4 NNN		// set initial credit to NNN
 *
 *
 **************************************************************************************/

package PJ4_PokerGame.PJ4;
import PJ4_PokerGame.PJ4.VideoPoker;

class TestPokerGame {

    public static void main(String args[]) {
        VideoPoker pokergame;
        if (args.length > 0)
            pokergame = new VideoPoker(Integer.parseInt(args[0]));
        else
            pokergame = new VideoPoker();
        pokergame.play();
    }
}
